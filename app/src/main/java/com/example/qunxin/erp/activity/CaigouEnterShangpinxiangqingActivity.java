package com.example.qunxin.erp.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.qunxin.erp.R;

public class CaigouEnterShangpinxiangqingActivity extends AppCompatActivity {


    TextView nameText;
    TextView unitText;
    TextView shichangjiaText;
    TextView caigoujiaText;
    TextView caigoushuliangText;
    TextView shijicaigoushuliangText;

    View backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caigou_enter_shangpinxiangqing);

        initView();
        initModel();
    }

    void initView(){
        nameText=findViewById(R.id.proName);
        unitText=findViewById(R.id.unit);
        shichangjiaText=findViewById(R.id.shicangjiaText);
        caigoujiaText=findViewById(R.id.caigoujiaText);
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
        float shichangjia=intent.getFloatExtra("shichangjia",0);
        float caigoujia=intent.getFloatExtra("caigoujia",0);
        int caigoushuliang=intent.getIntExtra("caigoushuliang",0);
        int shijicaigoushuliang=intent.getIntExtra("shijicaigoushuliang",0);

        System.out.println(name+unit+shichangjia+caigoujia+caigoushuliang+shijicaigoushuliang);
        nameText.setText(name);
        unitText.setText(unit);
        shichangjiaText.setText(shichangjia+"￥");
        caigoujiaText.setText(caigoujia+"￥");
        caigoushuliangText.setText(caigoushuliang+"");
        shijicaigoushuliangText.setText(shijicaigoushuliang+"");
    }
}
