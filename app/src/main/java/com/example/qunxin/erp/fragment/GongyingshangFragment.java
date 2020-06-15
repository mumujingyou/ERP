package com.example.qunxin.erp.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.qunxin.erp.activity.AddGongyingshangActivity;
import com.example.qunxin.erp.activity.GongyingshangXiangqingActivity;
import com.example.qunxin.erp.modle.GongyingshangDatus;

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

/**
 * Created by qunxin on 2019/8/28.
 */

public class GongyingshangFragment extends Fragment implements AbsListView.OnScrollListener, TextWatcher {
    ListView listView;
    EditText searchText;
    private RelativeLayout loading;
    private RelativeLayout bottomView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gongyingshang, container, false);

        listView = view.findViewById(R.id.listView);
        nodatusImage = view.findViewById(R.id.nodatusImage);
        loading = view.findViewById(R.id.loading);
        addLoading();

        new Thread(new Runnable() {
            @Override
            public void run() {
                loadModel(1);
            }
        }).start();

        searchText = view.findViewById(R.id.search);

        searchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override

            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                    Log.d("search", "onEditorAction: search");
                    search(searchText.getText().toString());
                    return true;
                }
                return false;

            }
        });

        searchText.addTextChangedListener(this);

        listView.setOnScrollListener(this);

        return view;
    }


    List<GongyingshangDatus> lists = new ArrayList<>();
    String searchValue = "";

    void loadModel(int index) {

        final String contentTypeList = "application/json";
        final String url = UserBaseDatus.getInstance().url + "api/suppliers/list";
        Map<String, String> jsonMap = new HashMap<>();
        jsonMap.put("searchValue", searchValue);
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
                    String supName = jsonObject.getString("supName");

                    String contactPeople = jsonObject.getString("contactPeople");

                    String id = jsonObject.getString("id");

                    String phone = jsonObject.getString("phone");

                    GongyingshangDatus datus = new GongyingshangDatus();
                    datus.setSupName(supName);
                    datus.setLianxiren(contactPeople);
                    datus.setId(id);
                    datus.setPhone(phone);
                    lists.add(datus);
                }
                FragmentActivity fragmentActivity = getActivity();


                if (fragmentActivity != null) {
                    fragmentActivity.runOnUiThread(new Runnable() {
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
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    int index = 1;

    RelativeLayout footView;
    ImageView nodatusImage;
    CommonAdapter<GongyingshangDatus> adapter = null;

    void loadView() {
        if (searchValue.equals("") == false) {
            removeFootView();
            removeBottomView();
        } else {
            addFootView();
        }

        if (adapter == null) {
            adapter = new CommonAdapter<GongyingshangDatus>(getContext(), lists, R.layout.item_gongyingshang) {

                @Override
                public void convert(final ViewHolder holder, GongyingshangDatus gongyingshangModel, final int position, View convertView) {

                    if (gongyingshangModel == null) return;
                    holder.setText(R.id.gongyingshang_name, gongyingshangModel.getSupName());
                    holder.setText(R.id.lianxiren, gongyingshangModel.getLianxiren());
                    //holder.setText(R.id.count, gongyingshangModel.getCount());
                    //holder.setImageResource(R.id.listview_iv,zhanghuDatus.getImgRes());

                    //可以根据自己需求设置一些选项(这里设置了IOS阻塞效果以及item的依次左滑、右滑菜单)
                    ((SwipeMenuLayout) holder.getConvertView()).setIos(true).setLeftSwipe(true);
                    //监听事件
                    holder.setOnClickListener(R.id.ll_content, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //Toast.makeText(ShangpinliebiaoActivity.this,"点击了："+position, Toast.LENGTH_SHORT).show();
                            isLoad = true;
                            Intent intent = new Intent(getContext(), GongyingshangXiangqingActivity.class);
                            GongyingshangDatus datus = lists.get(position);
                            String id = datus.getId();
                            intent.putExtra("id", id);
                            isLoad = true;
                            startActivity(intent);
                        }
                    });

                    holder.setOnClickListener(R.id.btn_bodadianhua, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            GongyingshangDatus datus = lists.get(position);
                            String phone = datus.getPhone();
                            Log.d("bodadianhua", phone);
                            callPhone(phone);
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
                            lists.remove(position);
                            notifyDataSetChanged();
                            nodatusImage();
                        }
                    });
                    //长按监听
                    holder.setOnLongClickListener(R.id.ll_content, new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            // Toast.makeText(MainActivity.this,"正在进行长按操作", Toast.LENGTH_SHORT).show();
                            return true;
                        }
                    });
                }
            };
            listView.setAdapter(adapter);
        }
    }

    /**
     * 拨打电话（跳转到拨号界面，用户手动点击拨打）
     *
     * @param phoneNum 电话号码
     */

    public void callPhone(String phoneNum) {

        Intent intent = new Intent(Intent.ACTION_DIAL);

        Uri data = Uri.parse("tel:" + phoneNum);

        intent.setData(data);

        startActivity(intent);

    }

    void delete(final int position) {
        final GongyingshangDatus datus = lists.get(position);
        new Thread(new Runnable() {
            @Override
            public void run() {
                final String strUrl = UserBaseDatus.getInstance().url + "api/suppliers/deleteSupplier";
                final String strData = "id=" + datus.getId() + "&&signa=" + UserBaseDatus.getInstance().getSign();
                final String contentType = "application/x-www-form-urlencoded";
                Map map = UserBaseDatus.getInstance().isSuccessPost(strUrl, strData, contentType);
                if ((boolean) (map.get("isSuccess"))) {
                    Log.d("delete", "run: 删除成功");
                }
            }
        }).start();
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
        if (AddGongyingshangActivity.isLoad) {
            isLoad = true;
        }
        if (isLoad == false) return;
        new Thread(new Runnable() {
            @Override
            public void run() {
                AddGongyingshangActivity.isLoad = false;
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
                loadModel(1);
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
        index = 1;

    }

    void initData() {
        lists.clear();
        index = 1;
        //adapter=null;
        isLoad = false;
    }

    private void nodatusImage() {
        if (lists.size() == 0) {
            nodatusImage.setVisibility(View.VISIBLE);
        } else {
            nodatusImage.setVisibility(View.GONE);

        }
    }


    void removeFootView() {
        if (footView != null) {
            listView.removeFooterView(footView);
            footView = null;
        }
    }

    void addFootView() {
        if (footView == null) {
            footView = (RelativeLayout) LayoutInflater.from(getContext()).inflate(R.layout.foot_view, null);
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
            bottomView = (RelativeLayout) LayoutInflater.from(getContext()).inflate(R.layout.foot_bottom_view, null);
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