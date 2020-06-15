package com.example.qunxin.erp.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class PandianlishiActivity extends AppCompatActivity implements View.OnClickListener, AbsListView.OnScrollListener {


    ListView listView;
    View backBtn;
    View addBtn;
    View jiesuoView;
    View searchView;
    private RelativeLayout container;
    private RelativeLayout footView;
    private RelativeLayout bottomView;
    private RelativeLayout loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pandianlishi);

        nodatusImage=findViewById(R.id.nodatusImage);

        listView=findViewById(R.id.listView);
        backBtn=findViewById(R.id.back);
        addBtn=findViewById(R.id.add);
        container=findViewById(R.id.container);
        jiesuoView=findViewById(R.id.jiesuo);
        searchView=findViewById(R.id.search);
        loading = findViewById(R.id.loading);

        addLoading();

        new Thread(new Runnable() {
            @Override
            public void run() {
                loadModel(index);
            }
        }).start();



        jiesuoView.setOnClickListener(this);
        searchView.setOnClickListener(this);
        backBtn.setOnClickListener(this);
        addBtn.setOnClickListener(this);
        listView.setOnScrollListener(this);

    }


    List<JinhuoBaseDatus> lists=new ArrayList<>();
    String searchValue="";
    void loadModel(int index){

        final String contentTypeList = "application/json";
        final String url= UserBaseDatus.getInstance().url+"api/app/stockCheck/list";
        Map<String, String> jsonMap = new HashMap<>();
        jsonMap.put("pageNum", index+"");
        jsonMap.put("pageSize", "10");
        jsonMap.put("signa", UserBaseDatus.getInstance().getSign());
        String jsonString = JSON.toJSONString(jsonMap);

        Map map=UserBaseDatus.getInstance().isSuccessPost(url, jsonString, contentTypeList);
        if((boolean)(map.get("isSuccess"))){

            JSONObject json=(JSONObject) map.get("json");
            try {
                JSONObject jsonData=json.getJSONObject("data");
                final JSONArray jsonArray=jsonData.getJSONArray("rows");
                for (int i=0;i<jsonArray.length();i++){
                    JSONObject jsonObject=jsonArray.getJSONObject(i);

                    String no=jsonObject.getString("stockCheckNo");

                    String createTime=jsonObject.getString("createTime");

                    String depot=jsonObject.getString("depot");

                    String depotName=getDepotName(depot);

                    String status=jsonObject.getString("status");

                    String id=jsonObject.getString("id");

                    JinhuoBaseDatus datus = new JinhuoBaseDatus();
                    datus.setBuyNo(no);
                    datus.setCreateTime(createTime);
                    datus.setStatus(status);
                    datus.setDepotName(depotName);
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

    //第几页
    int index=1;

    CommonAdapter<JinhuoBaseDatus> adapter=null;
    void loadView(){

        if (searchValue.equals("") == false) {
            removeFootView();
            removeBottomView();
        } else {
            addFootView();
        }
        if(adapter==null){
            adapter=new CommonAdapter<JinhuoBaseDatus>(PandianlishiActivity.this,lists, R.layout.item_pandianlishi) {

                @Override
                public void convert(final ViewHolder holder, JinhuoBaseDatus jinhuoBaseDatus, final int position, View convertView) {

                    if(jinhuoBaseDatus==null) return;
                    holder.setText(R.id.danhao, jinhuoBaseDatus.getBuyNo());
                    holder.setText(R.id.creatTime, jinhuoBaseDatus.getCreateTime());
                    holder.setText(R.id.cangku, jinhuoBaseDatus.getDepotName());
                    holder.setText(R.id.status, jinhuoBaseDatus.getTotalAmount()+"￥");

                    TextView statusText=holder.getView(R.id.status);
                    /** 0无盈亏 1.有盈亏3盘盈4.盘亏 */
                    if(jinhuoBaseDatus.getStatus().equals("0")){
                        statusText.setText("无盈亏");
                    }else if(jinhuoBaseDatus.getStatus().equals("1")){
                        statusText.setText("有盈亏");
                    }else if(jinhuoBaseDatus.getStatus().equals("3")){
                        statusText.setText("盘盈");
                    }else if(jinhuoBaseDatus.getStatus().equals("4")){
                        statusText.setText("盘亏");
                    }

                    holder.setOnClickListener(R.id.ll_content, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            isLoad=true;
                            //Toast.makeText(ShangpinliebiaoActivity.this,"点击了："+position, Toast.LENGTH_SHORT).show();
                            Intent intent=new Intent(PandianlishiActivity.this,PandianxiangqingListActivity.class);
                            JinhuoBaseDatus datus=lists.get(position);
                            String id=datus.getId();
                            String status=datus.getStatus();
                            intent.putExtra("id",id);
                            intent.putExtra("status",status);

                            startActivity(intent);
                        }
                    });



                }
            };

            listView.setAdapter(adapter);
        }



    }

    String data="";




    boolean isLoad=false;

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

    void pageLoad(){

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
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.add:
                Intent intent=new Intent(PandianlishiActivity.this,AddpandianActivity.class);
                isLoad=true;
                startActivity(intent);
                break;
            case R.id.jiesuo:
                Intent intent1=new Intent(PandianlishiActivity.this,CangkujiesuoActivity.class);
                startActivity(intent1);
                break;
            case R.id.search:
                Intent intent2=new Intent(PandianlishiActivity.this,PandianlishiSearchActivity.class);
                startActivity(intent2);
                break;
        }
    }


    String getDepotName(String depot) throws JSONException {
        final String contentType = "application/x-www-form-urlencoded";
        final String url = UserBaseDatus.getInstance().url + "api/app/depot/getDepotById";
        final String data = "signa=" + UserBaseDatus.getInstance().getSign() + "&&id=" + depot;
        Map map = UserBaseDatus.getInstance().isSuccessPost(url, data, contentType);
        if ((boolean) (map.get("isSuccess"))) {
            JSONObject jsonObject=(JSONObject) map.get("json");

            JSONObject json=jsonObject.getJSONObject("data");
            return json.getString("name");

        }else {
            return "";
        }
    }

    private void nodatusImage(){
        if(lists.size()==0){
            nodatusImage.setVisibility(View.VISIBLE);
        }else {
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
