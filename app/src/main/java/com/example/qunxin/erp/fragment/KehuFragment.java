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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.qunxin.erp.R;
import com.example.qunxin.erp.UserBaseDatus;
import com.example.qunxin.erp.activity.AddKehuActivity;
import com.example.qunxin.erp.activity.KehuxiangqingActivity;
import com.example.qunxin.erp.modle.KehuDatus;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import swipedelete.utils.CommonAdapter;
import swipedelete.utils.ViewHolder;
import swipedelete.view.SwipeMenuLayout;

/**
 * Created by qunxin on 2019/8/28.
 */

public class KehuFragment extends Fragment implements AbsListView.OnScrollListener {

    ListView listView;

    TextView searchText;


    RelativeLayout loading;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_kehu, container, false);

        listView = view.findViewById(R.id.listView);
        listView.setOnScrollListener(this);
        nodatusImage = view.findViewById(R.id.nodatusImage);
        loading = view.findViewById(R.id.loading);

        addLoading();

        new Thread(new Runnable() {
            @Override
            public void run() {
                loadModel(index);
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

        searchText.addTextChangedListener(new TextWatcher() {
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
        });

        return view;
    }


    List<KehuDatus> lists = new ArrayList<>();

    String searchValue = "";

    void loadModel(int index) {
        Log.d("haha", "run: haha");

        final String pageNum = index + "";
        final String pageSize = "10";
        final String contentType = "application/x-www-form-urlencoded";
        final String url = UserBaseDatus.getInstance().url + "api/custs/pageList";

        final String jsonString = "signa=" + UserBaseDatus.getInstance().getSign() + "&&pageSize=" + pageSize + "&&pageNum=" + pageNum +
                "&&searchValue=" + searchValue;

        Map map = UserBaseDatus.getInstance().isSuccessPost(url, jsonString, contentType);

        if ((boolean) (map.get("isSuccess"))) {

            JSONObject json = (JSONObject) map.get("json");
            try {
                JSONObject jsonData = json.getJSONObject("data");
                total=jsonData.getInt("total");

                final JSONArray jsonArray = jsonData.getJSONArray("rows");
                for (int i = 0; i < jsonArray.length(); i++) {


                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String custName = jsonObject.getString("custName");

                    String contactPeople = jsonObject.getString("contactPeople");

                    String id = jsonObject.getString("id");

                    String phone = jsonObject.getString("phone");


                    String dept = getYingshouqiankuan(id);
                    KehuDatus datus = new KehuDatus();
                    datus.setKehu(custName);
                    datus.setLianxiren(contactPeople);
                    datus.setId(id);
                    datus.setPhone(phone);
                    datus.setCount(dept);
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

    RelativeLayout bottomView = null;


    String getYingshouqiankuan(String id) {
        String result = "";
        final String contentType = "application/x-www-form-urlencoded";
        final String url = UserBaseDatus.getInstance().url + "api/custs/selectCustDeptById";
        final String jsonString = "sign=" + UserBaseDatus.getInstance().getSign() + "&&id=" + id;
        System.out.println(jsonString);
        Map map = UserBaseDatus.getInstance().isSuccessPost(url, jsonString, contentType);
        if ((boolean) (map.get("isSuccess"))) {
            JSONObject json = (JSONObject) map.get("json");
            try {
                JSONObject jsonObject = json.getJSONObject("data");
                result = jsonObject.getString("dept");
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return result;
    }


    int index = 1;
    ImageView nodatusImage;
    RelativeLayout footView;
    CommonAdapter<KehuDatus> adapter = null;

    int total=0;
    void loadView() {

        if (searchValue.equals("") == false) {
            removeFootView();
            removeBottomView();
        } else {
            addFootView();
            if(lists.size()==total){
                removeFootView();
                addBottomView();
            }
        }

        if (adapter == null) {
            adapter = new CommonAdapter<KehuDatus>(getContext(), lists, R.layout.item_kehu) {

                @Override
                public void convert(final ViewHolder holder, KehuDatus kehuModel, final int position, View convertView) {
                    if (kehuModel == null) return;

                    holder.setText(R.id.name, kehuModel.getKehu());
                    holder.setText(R.id.phone, kehuModel.getPhone());

                    holder.setText(R.id.count, kehuModel.getCount());

                    //可以根据自己需求设置一些选项(这里设置了IOS阻塞效果以及item的依次左滑、右滑菜单)
                    ((SwipeMenuLayout) holder.getConvertView()).setIos(true).setLeftSwipe(true);
                    //监听事件
                    holder.setOnClickListener(R.id.ll_content, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //Toast.makeText(ShangpinliebiaoActivity.this,"点击了："+position, Toast.LENGTH_SHORT).show();
                            isLoad = true;
                            Intent intent = new Intent(getContext(), KehuxiangqingActivity.class);
                            KehuDatus datus = lists.get(position);
                            String id = datus.getId();
                            intent.putExtra("id", id);

                            startActivity(intent);
                            isLoad = true;

                        }
                    });


                    holder.setOnClickListener(R.id.btn_bodadianhua, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            KehuDatus datus = lists.get(position);
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


    void callPhone(String phoneNum) {

        Intent intent = new Intent(Intent.ACTION_DIAL);

        Uri data = Uri.parse("tel:" + phoneNum);

        intent.setData(data);

        startActivity(intent);

    }

    void delete(final int position) {
        final KehuDatus datus = lists.get(position);
        new Thread(new Runnable() {
            @Override
            public void run() {
                final String strUrl = UserBaseDatus.getInstance().url + "api/custs/remove";
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
       // addLoading();
        if (AddKehuActivity.isLoad) {
            isLoad = true;
        }
        if (isLoad == false) return;
        new Thread(new Runnable() {
            @Override
            public void run() {
                AddKehuActivity.isLoad = false;
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