package com.example.qunxin.erp.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.qunxin.erp.R;

public class QichuzhanghuyueActivity extends AppCompatActivity implements View.OnClickListener {

    private View backBtn;
    private View searchBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qichuzhanghuyue);
        initView();
    }

    void initView(){
        backBtn=findViewById(R.id.back);
        searchBtn=findViewById(R.id.search);

        backBtn.setOnClickListener(this);
        searchBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.search:
                Intent intent=new Intent(QichuzhanghuyueActivity.this,QichuzhanghuyueSearchActivity.class);
                startActivity(intent);
                break;
        }
    }
}
