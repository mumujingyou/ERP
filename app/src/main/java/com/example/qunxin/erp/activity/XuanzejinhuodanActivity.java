package com.example.qunxin.erp.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

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

public class XuanzejinhuodanActivity extends AppCompatActivity implements AbsListView.OnScrollListener {


    View backBtn;
    ListView listView;
    private RelativeLayout container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xuanzejinhuodan);


        initView();
    }

    void initView(){
        nodatusImage=findViewById(R.id.nodatusImage);

        backBtn=findViewById(R.id.back);
        listView=findViewById(R.id.listView);
        container=findViewById(R.id.container);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        listView.setOnScrollListener(this);


        new Thread(new Runnable() {
            @Override
            public void run() {
                loadModel(index);
            }
        }).start();
    }



    List<JinhuotuihuoBaseDatus> lists=new ArrayList<>();
    String searchValue="";

    void loadModel(int index){

        final String contentTypeList = "application/json";
        final String url= UserBaseDatus.getInstance().url+"api/app/refund/getRefundBuyList";
        Map<String, String> jsonMap = new HashMap<>();
        jsonMap.put("searchValue",searchValue);
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

                    String supplier=jsonObject.getString("supplierName");

                    String leader=jsonObject.getString("leader");

                    String buyNo=jsonObject.getString("buyNo");

                    String proName=jsonObject.getString("proName");

                    String totalAmount=jsonObject.getString("totalAmount");

                    String id=jsonObject.getString("id");

                    JinhuotuihuoBaseDatus datus = new JinhuotuihuoBaseDatus();
                    datus.setSupplierName(supplier);
                    datus.setLeader(leader);
                    datus.setBuyNo(buyNo);
                    datus.setProName(proName);
                    datus.setTotalAmount(totalAmount);
                    datus.setId(id);
                    lists.add(datus);
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        nodatusImage();
                        if(jsonArray.length()==0) return;
                        loadView();
                    }
                });

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    //第几页
    int index=1;
    ImageView nodatusImage;

    CommonAdapter<JinhuotuihuoBaseDatus> adapter=null;
    void loadView(){


        if(adapter==null){
            adapter=new CommonAdapter<JinhuotuihuoBaseDatus>(XuanzejinhuodanActivity.this,lists, R.layout.item_xuanzedan) {

                @Override
                public void convert(final ViewHolder holder, JinhuotuihuoBaseDatus jinhuotuihuoBaseDatus, final int position, View convertView) {

                    if(jinhuotuihuoBaseDatus==null) return;
                    holder.setText(R.id.supplierName, jinhuotuihuoBaseDatus.getSupplierName());
                    holder.setText(R.id.danhao, jinhuotuihuoBaseDatus.getBuyNo());
                    holder.setText(R.id.proName, jinhuotuihuoBaseDatus.getProName());
                    holder.setText(R.id.sunCount, jinhuotuihuoBaseDatus.getTotalAmount()+"￥");

                    //可以根据自己需求设置一些选项(这里设置了IOS阻塞效果以及item的依次左滑、右滑菜单)
                    //((SwipeMenuLayout)holder.getConvertView()).setIos(true).setLeftSwipe(true);
                    //监听事件
                    holder.setOnClickListener(R.id.body, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Intent intent=new Intent(XuanzejinhuodanActivity.this,XuanzeJinhuo_tuihuoshangpinActivity.class);
                            JinhuotuihuoBaseDatus datus=lists.get(position);
                            String id=datus.getId();
                            intent.putExtra("id",id);
                            startActivity(intent);
                            finish();
                        }
                    });

                }
            };

            listView.setAdapter(adapter);

        }



    }

    void search(String searchValue){
        this.searchValue=searchValue;
        index=1;
        searchLoad();
    }

    boolean isLoad=false;

    @Override
    public void onResume() {
        super.onResume();
        if(isLoad==false) return;
        pageLoad();
    }

    void pageLoad(){

        new Thread(new Runnable() {
            @Override
            public void run() {
                loadModel(index);
            }
        }).start();
        adapter.notifyDataSetChanged();

    }

    void searchLoad(){
        lists.clear();
        new Thread(new Runnable() {
            @Override
            public void run() {
                loadModel(index);
            }
        }).start();
        adapter.notifyDataSetChanged();

    }


    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
            if (view.getLastVisiblePosition() == view.getCount() - 1&&view.getLastVisiblePosition()==9) {
                index++;
                pageLoad();
            }

        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }

    private void nodatusImage(){
        if(lists.size()==0){
            nodatusImage.setVisibility(View.VISIBLE);
        }else {
            nodatusImage.setVisibility(View.GONE);

        }
    }

}
