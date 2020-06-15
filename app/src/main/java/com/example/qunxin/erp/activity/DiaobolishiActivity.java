package com.example.qunxin.erp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.example.qunxin.erp.R;
import com.example.qunxin.erp.UserBaseDatus;
import com.example.qunxin.erp.modle.JinhuoBaseDatus;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import swipedelete.utils.CommonAdapter;
import swipedelete.utils.ViewHolder;
import swipedelete.view.SwipeMenuLayout;

public class DiaobolishiActivity extends AppCompatActivity implements TextWatcher, AbsListView.OnScrollListener, View.OnClickListener {

    EditText searchText;
    ListView listView;
    View backBtn;
    View addBtn;
    private RelativeLayout container;
    private RelativeLayout footView;
    private RelativeLayout bottomView;
    private RelativeLayout loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diaobolishi);

        nodatusImage = findViewById(R.id.nodatusImage);
        loading = findViewById(R.id.loading);
        listView = findViewById(R.id.listView);
        backBtn = findViewById(R.id.back);
        addBtn = findViewById(R.id.add);
        container = findViewById(R.id.container);
        addLoading();

        new Thread(new Runnable() {
            @Override
            public void run() {
                loadModel(index);
            }
        }).start();

        searchText = findViewById(R.id.search);


        searchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override

            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    search(searchText.getText().toString());
                    return true;
                }
                return false;

            }
        });

        searchText.addTextChangedListener(this);
        backBtn.setOnClickListener(this);
        addBtn.setOnClickListener(this);
        listView.setOnScrollListener(this);

    }


    List<JinhuoBaseDatus> lists = new ArrayList<>();
    String searchValue = "";

    void loadModel(int index) {

        final String contentTypeList = "application/json";
        final String url = UserBaseDatus.getInstance().url + "api/app/allocation/getPageList";
        Map<String, String> jsonMap = new HashMap<>();
        jsonMap.put("allocationNo", searchValue);
        jsonMap.put("pageNum", index + "");
        jsonMap.put("pageSize", "10");
        jsonMap.put("signa", UserBaseDatus.getInstance().getSign());
        String jsonString = JSON.toJSONString(jsonMap);

        Map map = UserBaseDatus.getInstance().isSuccessPost(url, jsonString, contentTypeList);
        if ((boolean) (map.get("isSuccess"))) {

            JSONObject json = (JSONObject) map.get("json");
            try {
                JSONObject jsonData = json.getJSONObject("data");
                final JSONArray jsonArray = jsonData.getJSONArray("rows");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    String refundDepotName = jsonObject.getString("refundDepotName");

                    String buyDepotName = jsonObject.getString("buyDepotName");

                    String allocationNo = jsonObject.getString("allocationNo");

                    String proName = jsonObject.getString("proName");

                    String id = jsonObject.getString("id");

                    JinhuoBaseDatus datus = new JinhuoBaseDatus();
                    datus.setRefundDepotName(refundDepotName);
                    datus.setBuyDepotName(buyDepotName);
                    datus.setBuyNo(allocationNo);
                    datus.setProName(proName);
                    datus.setId(id);
                    lists.add(datus);
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        nodatusImage();
                        removeLoading();
                        if (jsonArray.length() > 0) {
                            loadView();
                        } else {
                            if (searchValue.equals("") == false) {//搜索情况下
                                removeFootView();
                                removeBottomView();
                            } else {
                                removeFootView();
                                addBottomView();
                            }

                        }
                    }
                });

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    ImageView nodatusImage;

    CommonAdapter<JinhuoBaseDatus> adapter = null;

    //第几页
    int index = 1;

    void loadView() {
        if (searchValue.equals("") == false) {
            removeFootView();
            removeBottomView();
        } else {
            addFootView();
        }
        if (adapter == null) {
            adapter = new CommonAdapter<JinhuoBaseDatus>(DiaobolishiActivity.this, lists, R.layout.item_diaobolishi) {

                @Override
                public void convert(final ViewHolder holder, JinhuoBaseDatus jinhuoBaseDatus, final int position, View convertView) {

                    if (jinhuoBaseDatus == null) return;
                    holder.setText(R.id.busNo, jinhuoBaseDatus.getBuyNo());
                    holder.setText(R.id.refundDepotName, jinhuoBaseDatus.getRefundDepotName());
                    holder.setText(R.id.buyDepotName, jinhuoBaseDatus.getBuyDepotName());
                    holder.setText(R.id.proName, jinhuoBaseDatus.getProName());


                    //可以根据自己需求设置一些选项(这里设置了IOS阻塞效果以及item的依次左滑、右滑菜单)
                    ((SwipeMenuLayout) holder.getConvertView()).setIos(true).setLeftSwipe(true);
                    //监听事件
                    holder.setOnClickListener(R.id.ll_content, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            isLoad = true;
                            //Toast.makeText(ShangpinliebiaoActivity.this,"点击了："+position, Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(DiaobolishiActivity.this, DiaobodanxiangqingActivity.class);
                            JinhuoBaseDatus datus = lists.get(position);
                            String id = datus.getId();
                            intent.putExtra("id", id);
                            startActivity(intent);
                        }
                    });


                }
            };
            listView.setAdapter(adapter);

        }
    }


    void search(String searchValue) {
        this.searchValue = searchValue;
        index = 1;
        searchLoad();
    }

    boolean isLoad = false;

    @Override
    public void onResume() {
        super.onResume();
        removeFootView();
        removeBottomView();
        addLoading();
        if (isLoad == false) {
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                initData();
                loadModel(index);
            }
        }).start();
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    void pageLoad() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                loadModel(index);
            }
        }).start();
        Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                adapter.notifyDataSetChanged();
            }
        }, 1000);//3秒后执行Runnable中的run方法
    }

    void searchLoad() {
        initData();
        new Thread(new Runnable() {
            @Override
            public void run() {
                loadModel(index);
            }
        }).start();
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }

    }


    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
            if (view.getLastVisiblePosition() == view.getCount() - 1) {
                index++;
                pageLoad();
            }

        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

        search(searchText.getText().toString());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.add:
                Intent intent = new Intent(DiaobolishiActivity.this, AddDiaobodanActivity.class);
                isLoad = true;
                startActivity(intent);
                break;
        }
    }

    private void nodatusImage() {
        if (lists.size() == 0) {
            nodatusImage.setVisibility(View.VISIBLE);
        } else {
            nodatusImage.setVisibility(View.GONE);

        }
    }

    void initData() {
        lists.clear();
        index = 1;
        //adapter=null;
        isLoad = false;
    }


    void removeFootView() {
        if (footView != null) {
            listView.removeFooterView(footView);
            footView = null;
        }
    }

    void addFootView() {
        if (footView == null) {
            footView = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.foot_view, null);
            listView.addFooterView(footView);
        }
    }

    void removeBottomView() {
        if (bottomView != null) {
            listView.removeFooterView(bottomView);
            bottomView = null;
        }
    }

    void addBottomView() {
        if (bottomView == null) {
            bottomView = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.foot_bottom_view, null);
            listView.addFooterView(bottomView);
        }
    }


    void addLoading() {
        loading.setVisibility(View.VISIBLE);
    }

    void removeLoading() {
        loading.setVisibility(View.GONE);


    }
}
