package com.example.qunxin.erp.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
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

public class XuanzexiaoshoudanActivity extends AppCompatActivity implements TextWatcher {

    View backBtn;
    ListView listView;
    private RelativeLayout container;
    EditText searchText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xuanzexiaoshoudan);


        initView();
    }

    void initView(){
        backBtn=findViewById(R.id.back);
        listView=findViewById(R.id.listView);
        container=findViewById(R.id.container);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        new Thread(new Runnable() {
            @Override
            public void run() {
                loadModel(1);
            }
        }).start();
        searchText=findViewById(R.id.search);


        searchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override

            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_SEARCH){
                    search(searchText.getText().toString());
                    return true;
                }
                return false;

            }
        });

        searchText.addTextChangedListener(this);

    }





    List<JinhuotuihuoBaseDatus> lists=new ArrayList<>();
    String searchValue="";

    void loadModel(int index){

        final String contentTypeList = "application/json";
        final String url= UserBaseDatus.getInstance().url+"api/sells/list";
        Map<String, String> jsonMap = new HashMap<>();
        //jsonMap.put("searchValue",searchValue);
        jsonMap.put("pageNum", index+"");
        jsonMap.put("sellNo",searchValue);
        jsonMap.put("pageSize", "100");
        jsonMap.put("signa", UserBaseDatus.getInstance().getSign());
        String jsonString = JSON.toJSONString(jsonMap);
        Log.d("jsonString", jsonString);

        Map map=UserBaseDatus.getInstance().isSuccessPost(url, jsonString, contentTypeList);
        if((boolean)(map.get("isSuccess"))){

            JSONObject json=(JSONObject) map.get("json");
            try {
                JSONObject jsonData=json.getJSONObject("data");
                JSONArray jsonArray=jsonData.getJSONArray("rows");
                for (int i=0;i<jsonArray.length();i++){
                    JSONObject jsonObject=jsonArray.getJSONObject(i);

                    String name=jsonObject.getString("custName");


                    String buyNo=jsonObject.getString("sellNo");

                    String proName=jsonObject.getString("proName");


                    String id=jsonObject.getString("id");

                    String totalAmount=jsonObject.getString("amount");

                    String rejectFlag=jsonObject.getString("rejectFlag");

                    if("1".equals(rejectFlag)) continue;

                    JinhuotuihuoBaseDatus datus = new JinhuotuihuoBaseDatus();
                    datus.setKehuName(name);
                    datus.setBuyNo(buyNo);
                    datus.setProName(proName);
                    datus.setId(id);
                    datus.setTotalAmount(totalAmount);
                    lists.add(datus);
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
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

    void loadView(){

        if(lists.size()==0){
            if(nodatusImage==null){
                nodatusImage=new ImageView(this);
            }
            nodatusImage.setImageResource(R.drawable.nodatus);
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(300, 300);

            layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
            layoutParams.topMargin=30;
            nodatusImage.setLayoutParams(layoutParams);
            if(nodatusImage.getParent()==null){
                container.addView(nodatusImage);
            }
        }else {
            container.removeView(nodatusImage);
        }


        if(index<=0){
            return;
        }

        if(lists.size()==0){
            index--;
            return;
        }

        listView.setAdapter(new CommonAdapter<JinhuotuihuoBaseDatus>(XuanzexiaoshoudanActivity.this,lists, R.layout.item_xuanzedan) {

            @Override
            public void convert(final ViewHolder holder, JinhuotuihuoBaseDatus jinhuotuihuoBaseDatus, final int position, View convertView) {

                if(jinhuotuihuoBaseDatus==null) return;
                holder.setText(R.id.supplierName, jinhuotuihuoBaseDatus.getKehuName());
                holder.setText(R.id.danhao, jinhuotuihuoBaseDatus.getBuyNo());
                holder.setText(R.id.proName, jinhuotuihuoBaseDatus.getProName());
                holder.setText(R.id.sunCount, jinhuotuihuoBaseDatus.getTotalAmount()+"￥");

                //可以根据自己需求设置一些选项(这里设置了IOS阻塞效果以及item的依次左滑、右滑菜单)
               // ((SwipeMenuLayout)holder.getConvertView()).setIos(true).setLeftSwipe(true);
                //监听事件
                holder.setOnClickListener(R.id.body, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent=new Intent(XuanzexiaoshoudanActivity.this,XuanzeXiaoshou_tuihuoshangpinActivity.class);
                        JinhuotuihuoBaseDatus datus=lists.get(position);
                        String id=datus.getId();
                        intent.putExtra("id",id);
                        startActivity(intent);
                        finish();
                    }
                });

            }
        });

    }

    void search(String searchValue){
        this.searchValue=searchValue;
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
        lists.clear();
        new Thread(new Runnable() {
            @Override
            public void run() {
                loadModel(index);
            }
        }).start();
    }

    void searchLoad(){
        lists.clear();
        new Thread(new Runnable() {
            @Override
            public void run() {
                loadModel(1);
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
        index=1;
    }
}


