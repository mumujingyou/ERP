package com.example.qunxin.erp.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.qunxin.erp.R;

public class AddXiaoshoutuihuodanSuccessActivity extends AppCompatActivity {

    View finishBtn;
    View addBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_xiaoshoudan_success);
        initView();
    }



    void initView(){
        finishBtn=findViewById(R.id.finish);
        addBtn=findViewById(R.id.add);

        finishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AddXiaoshoutuihuodanSuccessActivity.this,XiaoshoutuihuolishiActivity.class);
                startActivity(intent);
                finish();
            }
        });

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AddXiaoshoutuihuodanSuccessActivity.this,AddXiaoshoutuihuodanActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
