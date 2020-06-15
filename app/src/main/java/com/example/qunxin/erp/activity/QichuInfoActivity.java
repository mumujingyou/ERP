package com.example.qunxin.erp.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.qunxin.erp.R;

public class QichuInfoActivity extends AppCompatActivity implements View.OnClickListener {

    View kucun;
    View fukuan;
    View shoukuan;
    View yue;
    View backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qichu_info);
        initView();
    }

    void initView(){
        kucun=findViewById(R.id.qichuxinxi_kucun);
        fukuan=findViewById(R.id.qichuxinxi_fukuan);
        shoukuan=findViewById(R.id.qichuxinxi_shoukuan);
        yue=findViewById(R.id.qichuxinxi_yue);
        backBtn=findViewById(R.id.qichuxinxi_back);

        kucun.setOnClickListener(this);
        fukuan.setOnClickListener(this);
        shoukuan.setOnClickListener(this);
        yue.setOnClickListener(this);
        backBtn.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.qichuxinxi_kucun:
                Intent intent=new Intent(QichuInfoActivity.this,QichushangpinkucunActivity.class);
                startActivity(intent);
                break;
            case R.id.qichuxinxi_fukuan:
                Intent intent2=new Intent(QichuInfoActivity.this,QichuyingfuqiankuanActivity.class);
                startActivity(intent2);
                break;

            case R.id.qichuxinxi_shoukuan:
                Intent intent3=new Intent(QichuInfoActivity.this,QichuyingshouqiankuanActivity.class);
                startActivity(intent3);
                break;
            case R.id.qichuxinxi_yue:
                Intent intent4=new Intent(QichuInfoActivity.this,QichuzhanghuyueActivity.class);
                startActivity(intent4);
                break;
            case R.id.qichuxinxi_back:
               finish();
               break;
        }
    }
}
