package com.example.qunxin.erp.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.qunxin.erp.R;

public class RukushangpinxiangqingActivity extends AppCompatActivity {

    TextView nameText;
    TextView unitText;
    TextView shangpinjiaText;
    TextView shijijiaText;
    TextView caigoushuliangText;
    TextView shijicaigoushuliangText;

    View backBtn;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rukushangpinxiangqing);

        initView();
        initModel();
    }

    void initView(){
        nameText=findViewById(R.id.proName);
        unitText=findViewById(R.id.unit);
        shangpinjiaText=findViewById(R.id.shangpinjiaText);
        shijijiaText=findViewById(R.id.shijijiaText);
        caigoushuliangText=findViewById(R.id.caigouCountText);
        shijicaigoushuliangText=findViewById(R.id.shijicaigouCountText);
        backBtn=findViewById(R.id.back);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    void initModel(){
        Intent intent=getIntent();
        String name=intent.getStringExtra("name");
        String unit=intent.getStringExtra("unit");
        float shangpinjia=intent.getFloatExtra("shangpinjia",0);
        float shijijia=intent.getFloatExtra("shijijia",0);
        int caigoushuliang=intent.getIntExtra("caigoushuliang",0);
        int shijicaigoushuliang=intent.getIntExtra("shijicaigoushuliang",0);

        nameText.setText(name);
        unitText.setText(unit);
        shangpinjiaText.setText(shangpinjia+"￥");
        shijijiaText.setText(shijijia+"￥");
        caigoushuliangText.setText(caigoushuliang+"");
        shijicaigoushuliangText.setText(shijicaigoushuliang+"");
    }
}
