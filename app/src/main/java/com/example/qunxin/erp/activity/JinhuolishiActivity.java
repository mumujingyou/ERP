package com.example.qunxin.erp.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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
import android.widget.Toast;

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

public class JinhuolishiActivity extends AppCompatActivity implements TextWatcher, AbsListView.OnScrollListener, View.OnClickListener {


    EditText searchText;
    ListView listView;
    View backBtn;
    View addBtn;
    private RelativeLayout footView;
    private RelativeLayout bottomView;
    private RelativeLayout loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jinhuolishi);

        nodatusImage = findViewById(R.id.nodatusImage);
        loading = findViewById(R.id.loading);
        listView = findViewById(R.id.listView);
        backBtn = findViewById(R.id.back);
        addBtn = findViewById(R.id.add);
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
    }


    List<JinhuoBaseDatus> lists = new ArrayList<>();
    String searchValue = "";

    void loadModel(int index) {

        final String contentTypeList = "application/json";
        final String url = UserBaseDatus.getInstance().url + "api/app/buy/pageList";
        Map<String, String> jsonMap = new HashMap<>();
        jsonMap.put("searchValue", searchValue);
        jsonMap.put("pageNum", index + "");
        jsonMap.put("pageSize", "10");
        jsonMap.put("buyNo", searchValue);
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

                    String supplier = jsonObject.getString("supplierName");

                    String leader = jsonObject.getString("leader");

                    String buyNo = jsonObject.getString("buyNo");

                    String proName = jsonObject.getString("proName");

                    String totalAmount = jsonObject.getString("totalAmount");

                    String refundFlag = jsonObject.getString("refundFlag");


                    String id = jsonObject.getString("id");

                    JinhuoBaseDatus datus = new JinhuoBaseDatus();
                    datus.setSupplierName(supplier);
                    datus.setLeader(leader);
                    datus.setBuyNo(buyNo);
                    datus.setProName(proName);
                    datus.setTotalAmount(totalAmount);
                    datus.setId(id);
                    datus.setStatus(refundFlag);
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

    //第几页
    int index = 1;

    CommonAdapter<JinhuoBaseDatus> adapter = null;

    void loadView() {
        if (searchValue.equals("") == false) {
            removeFootView();
            removeBottomView();
        } else {
            addFootView();
        }

        if (adapter == null) {
            adapter = new CommonAdapter<JinhuoBaseDatus>(JinhuolishiActivity.this, lists, R.layout.item_jinhuolishi) {

                @Override
                public void convert(final ViewHolder holder, JinhuoBaseDatus jinhuoBaseDatus, final int position, View convertView) {

                    if (jinhuoBaseDatus == null) return;
                    holder.setText(R.id.name, jinhuoBaseDatus.getSupplierName());
                    holder.setText(R.id.danhao, jinhuoBaseDatus.getBuyNo());
                    holder.setText(R.id.proName, jinhuoBaseDatus.getProName());
                    holder.setText(R.id.sunCount, jinhuoBaseDatus.getTotalAmount() + "￥");

                    TextView textView = holder.getView(R.id.textView);

                    if (jinhuoBaseDatus.getStatus().equals("1")) {
                        textView.setText("退款");
                        textView.setBackgroundColor(Color.parseColor("#ED5565"));
                    } else {
                        textView.setText("正常");
                        textView.setBackgroundColor(Color.parseColor("#51e1ad"));
                    }
                    //可以根据自己需求设置一些选项(这里设置了IOS阻塞效果以及item的依次左滑、右滑菜单)
                    ((SwipeMenuLayout) holder.getConvertView()).setIos(true).setLeftSwipe(true);
                    //监听事件
                    holder.setOnClickListener(R.id.ll_content, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //Toast.makeText(ShangpinliebiaoActivity.this,"点击了："+position, Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(JinhuolishiActivity.this, JinhuodanXiangqiangActivity.class);
                            JinhuoBaseDatus datus = lists.get(position);
                            String id = datus.getId();
                            intent.putExtra("id", id);
                            startActivity(intent);
                            isLoad = true;
                        }
                    });

                    holder.setOnClickListener(R.id.btn_delete, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // Toast.makeText(MainActivity.this,"点击了删除选项", Toast.LENGTH_SHORT).show();
                            //在ListView里，点击侧滑菜单上的选项时，如果想让侧滑菜单同时关闭，调用这句话
                            ((SwipeMenuLayout) holder.getConvertView()).quickClose();
                            //删除操作

                            delete(position);
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    /**
                                     *要执行的操作
                                     */
                                    if ("".equals(data)) {
                                        lists.remove(position);
                                        notifyDataSetChanged();
                                    } else {
                                        Toast.makeText(mContext, data, Toast.LENGTH_SHORT).show();
                                    }
                                    data = "";
                                    nodatusImage();

                                }
                            }, 500);//3秒后执行Runnable中的run方法

                        }
                    });

                }
            };
            listView.setAdapter(adapter);
        }


        listView.setOnScrollListener(this);
    }

    String data = "";

    void delete(final int position) {
        final JinhuoBaseDatus datus = lists.get(position);

        new Thread(new Runnable() {
            @Override
            public void run() {
                final String strUrl = UserBaseDatus.getInstance().url + "api/app/buy/remove";
                final String strData = "ids=" + datus.getId() + "&&signa=" + UserBaseDatus.getInstance().getSign() + "&&userId=" + UserBaseDatus.getInstance().userId;
                Log.d("strData", strData);
                final String contentType = "application/x-www-form-urlencoded";
                //final String contentType = "application/json";

                Map map = UserBaseDatus.getInstance().isSuccessPost(strUrl, strData, contentType);
                if ((boolean) (map.get("isSuccess"))) {
                    Log.d("delete", "run: 删除成功");
                } else {
                    JSONObject jsonObject = (JSONObject) map.get("json");
                    try {
                        data = jsonObject.getString("data");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

    }


    void search(String searchValue) {
        this.searchValue=searchValue;
        index=1;
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
                Intent intent = new Intent(JinhuolishiActivity.this, AddjinhuoActivity.class);
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
