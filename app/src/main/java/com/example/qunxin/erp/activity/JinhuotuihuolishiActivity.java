package com.example.qunxin.erp.activity;

import android.content.Intent;
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
import com.example.qunxin.erp.modle.JinhuotuihuoBaseDatus;

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

public class JinhuotuihuolishiActivity extends AppCompatActivity implements AbsListView.OnScrollListener, TextWatcher {

    EditText searchText;

    View backBtn;
    ListView listView;
    View addBtn;
    private RelativeLayout footView;
    private RelativeLayout bottomView;
    private RelativeLayout loading;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jinhuotuihuolishi);

        initView();
    }

    void initView() {
        nodatusImage = findViewById(R.id.nodatusImage);

        backBtn = findViewById(R.id.back);
        listView = findViewById(R.id.listView);
        addBtn = findViewById(R.id.add);
        searchText = findViewById(R.id.search);
        listView.setOnScrollListener(this);
        loading = findViewById(R.id.loading);

        addLoading();
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


        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(JinhuotuihuolishiActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(JinhuotuihuolishiActivity.this, AddJinhuotuihuodanActivity.class);
                isLoad = true;
                startActivity(intent);
            }
        });


        new Thread(new Runnable() {
            @Override
            public void run() {
                loadModel(index);
            }
        }).start();
    }


    List<JinhuotuihuoBaseDatus> lists = new ArrayList<>();
    String searchValue = "";

    void loadModel(int index) {

        final String contentTypeList = "application/json";
        final String url = UserBaseDatus.getInstance().url + "api/app/refund/pageList";
        Map<String, String> jsonMap = new HashMap<>();
        jsonMap.put("pageNum", index + "");
        jsonMap.put("pageSize", "10");
        jsonMap.put("refundNo", searchValue);
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

                    String supplier = jsonObject.getString("supplier");

                    String leader = jsonObject.getString("leader");

                    String refundNo = jsonObject.getString("refundNo");

                    String proName = jsonObject.getString("proName");

                    String totalAmount = jsonObject.getString("totalAmount");

                    String id = jsonObject.getString("id");


                    JinhuotuihuoBaseDatus datus = new JinhuotuihuoBaseDatus();
                    datus.setSupplierName(supplier);
                    datus.setLeader(leader);
                    datus.setBuyNo(refundNo);
                    datus.setProName(proName);
                    datus.setTotalAmount(totalAmount);
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

    //第几页
    int index = 1;
    ImageView nodatusImage;

    CommonAdapter<JinhuotuihuoBaseDatus> adapter = null;

    void loadView() {
        if (searchValue.equals("") == false) {
            removeFootView();
            removeBottomView();
        } else {
            addFootView();
        }
        if (adapter == null) {
            adapter = new CommonAdapter<JinhuotuihuoBaseDatus>(JinhuotuihuolishiActivity.this, lists, R.layout.item_jinhuotuihuolishi) {

                @Override
                public void convert(final ViewHolder holder, JinhuotuihuoBaseDatus jinhuotuihuoBaseDatus, final int position, View convertView) {

                    if (jinhuotuihuoBaseDatus == null) return;
                    holder.setText(R.id.supplierName, jinhuotuihuoBaseDatus.getSupplierName());
                    holder.setText(R.id.danhao, jinhuotuihuoBaseDatus.getBuyNo());
                    holder.setText(R.id.proName, jinhuotuihuoBaseDatus.getProName());
                    holder.setText(R.id.sunCount, jinhuotuihuoBaseDatus.getTotalAmount() + "￥");


                    //可以根据自己需求设置一些选项(这里设置了IOS阻塞效果以及item的依次左滑、右滑菜单)
                    ((SwipeMenuLayout) holder.getConvertView()).setIos(true).setLeftSwipe(true);
                    //监听事件
                    holder.setOnClickListener(R.id.ll_content, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Intent intent = new Intent(JinhuotuihuolishiActivity.this, JinhuoTuihuodanxiangqingActivity.class);
                            JinhuotuihuoBaseDatus datus = lists.get(position);
                            String id = datus.getId();
                            intent.putExtra("id", id);
                            isLoad = true;
                            startActivity(intent);
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
                isLoad = false;
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

    String data = "";

    void delete(final int position) {
        final JinhuotuihuoBaseDatus datus = lists.get(position);
        new Thread(new Runnable() {
            @Override
            public void run() {
                final String strUrl = UserBaseDatus.getInstance().url + "api/app/refund/appDeleteRefund";
                final String strData = "id=" + datus.getId() + "&&signa=" + UserBaseDatus.getInstance().getSign() + "&&userId=" + UserBaseDatus.getInstance().userId;
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