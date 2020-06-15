package com.example.qunxin.erp.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.example.qunxin.erp.R;
import com.example.qunxin.erp.UserBaseDatus;
import com.example.qunxin.erp.modle.ShangpinDatus;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by qunxin on 2019/8/20.
 */

public class PrintTagActivity extends AppCompatActivity implements View.OnClickListener {

    ListView listView;
    List<ShangpinDatus> lists=new ArrayList<>();

    Button selectAllBtn;
    Button printBtn;
    View backBtn;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_printtag);
        initView();


    }

    void initView(){
        listView=findViewById(R.id.printtag_listView);
        selectAllBtn=findViewById(R.id.printtag_selectAll);
        printBtn=findViewById(R.id.printtag_print);
        backBtn=findViewById(R.id.printtag_back);
        selectAllBtn.setOnClickListener(this);
        printBtn.setOnClickListener(this);
        backBtn.setOnClickListener(this);
        new Thread(new Runnable() {
            @Override
            public void run() {
                loadModel();
            }
        }).start();




    }

    @Override
    protected void onResume() {
        super.onResume();
        check();
    }

    int count=0;
    void check(){

        for (int i=0;i<checkBoxes.size();i++){

            checkBoxes.get(i).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    //
                    if(isChecked){
                        count++;
                        System.out.println(count);
                    }else {
                        count--;
                        System.out.println(count);
                    }
                    if(count<=0){
                        count=0;
                    }
                    printBtn.setText("打印("+count+")");
                    if(count==0){
                        printBtn.setEnabled(false);

                    }else {
                        printBtn.setEnabled(true);

                    }
                    System.out.println(count);
                }
            });
        }
    }



    List<CheckBox> checkBoxes=new ArrayList<>();
    void loadView() {
        MyAdapter myAdapter = new MyAdapter(this, R.layout.item_print_shangpin, lists);
        listView.setAdapter(myAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CheckBox checkBox = view.findViewById(R.id.item_shangping_checkBox);
                if (checkBox.isChecked()) {
                    count--;
                    checkBox.setChecked(false);

                } else {
                    count++;
                    checkBox.setChecked(true);
                }
                printBtn.setText("打印("+count+")");
                if(count==0){
                    printBtn.setEnabled(false);
                }else {
                    printBtn.setEnabled(true);
                }
                Log.d("count", count+"");
            }
        });


    }

    String searchValue="";
    void loadModel(){

        final String contentTypeList = "application/json";
        final String url= UserBaseDatus.getInstance().url+"api/app/proProduct/list";
        Map<String, String> jsonMap = new HashMap<>();
        jsonMap.put("searchValue",searchValue);
        jsonMap.put("pageNum", "1");
        jsonMap.put("pageSize", "10");
        jsonMap.put("signa", UserBaseDatus.getInstance().getSign());
        String jsonString = JSON.toJSONString(jsonMap);

        Map map=UserBaseDatus.getInstance().isSuccessPost(url, jsonString, contentTypeList);
        if((boolean)(map.get("isSuccess"))){

            JSONObject json=(JSONObject) map.get("json");
            try {
                JSONObject jsonData=json.getJSONObject("data");
                JSONArray jsonArray=jsonData.getJSONArray("rows");
                for (int i=0;i<jsonArray.length();i++){
                    JSONObject jsonObject=jsonArray.getJSONObject(i);
                    String typeName=jsonObject.getString("proName");

                    String price=jsonObject.getString("price");

                    String id=jsonObject.getString("id");

                    ShangpinDatus datus = new ShangpinDatus();
                    datus.setPrice(price);
                    datus.setName(typeName);
                    datus.setId(id);
                    datus.setImgRes(R.mipmap.shop_icon);
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

    boolean isAll=false;
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.printtag_selectAll:

                isAll=!isAll;
                if(isAll){
                    count=lists.size();
                    selectAllBtn.setText("全选");
                    printBtn.setText("打印("+count+")");
                    for(int i=0;i<checkBoxes.size();i++){
                        checkBoxes.get(i).setChecked(true);
                    }
                    printBtn.setEnabled(true);
                }else {
                    selectAllBtn.setText("全不选");
                    count=0;
                    printBtn.setText("打印("+count+")");
                    for(int i=0;i<checkBoxes.size();i++){
                        checkBoxes.get(i).setChecked(false);
                    }
                    printBtn.setEnabled(false);
                }
                break;
            case R.id.printtag_print:
                break;
            case R.id.printtag_back:
                finish();
                break;
        }
    }

    class MyAdapter extends ArrayAdapter {

        public MyAdapter(Context context, int resource, List<ShangpinDatus> objects) {

            super(context, resource, objects);

        }
        @Override

        public View getView(int position, View convertView, ViewGroup parent) {

            ShangpinDatus datus = (ShangpinDatus)getItem(position);

            View view = LayoutInflater.from(getContext()).inflate(R.layout.item_print_shangpin, null);

            TextView name = (TextView)view.findViewById(R.id.listview_tv);

            TextView price = (TextView)view.findViewById(R.id.shangpinliebiao_price);

            CheckBox checkBox=view.findViewById(R.id.item_shangping_checkBox);

            checkBoxes.add(checkBox);

            name.setText(datus.getName());

            price.setText(datus.getPrice());

            return view;
        }
    }



}
