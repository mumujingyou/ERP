package com.example.qunxin.erp.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.qunxin.erp.R;

public class UnitActivity extends AppCompatActivity implements View.OnClickListener {

     String[] strings={"个", "袋", "套", "箱", "组", "只", "米","件", "罐", "升", "双", "粒", "台", "斤",
            "筒", "瓶", "卷", "克", "条", "升", "杯","公斤", "盒", "支", "卡", "张", "板", "斤"};

     ListView listView;

     View backBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unit);

        initView();



    }

    void initView(){
        listView=findViewById(R.id.xuanzedanwei_listview);
        backBtn=findViewById(R.id.xuanzedanwei_back);


        ArrayAdapter<String> adapter=new ArrayAdapter<String>(UnitActivity.this,android.R.layout.simple_list_item_1,strings);
        listView.setAdapter(adapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> arg0,View arg1,int arg2,long arg3){

                //arg0表示点击发生的所在的AdapterView，arg1是在AdapterView中被点击的view，arg2表示adapter中view的位置（position），arg3表示被点击的item的行id

                Intent intent=new Intent();

                intent.putExtra("unit", strings[arg2]);

                setResult(2,intent);

                finish();
            }
        });

        backBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.xuanzedanwei_back:
                finish();
                break;
        }
    }



}
