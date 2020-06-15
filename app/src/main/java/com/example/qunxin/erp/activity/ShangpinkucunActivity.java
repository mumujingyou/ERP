package com.example.qunxin.erp.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.example.qunxin.erp.R;
import com.example.qunxin.erp.UserBaseDatus;
import com.example.qunxin.erp.modle.ShangpinBaseDatus;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShangpinkucunActivity extends AppCompatActivity {

    TextView proNameText;
    TextView normsText;
    TextView propertyText;
    TextView unitText;
    TextView depotNmaetext;
    TextView kucunCountText;
    TextView qichukucunCountText;
    TextView chengbenjiaText;
    TextView kucunzongeText;
    View backBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shangpinkucun);

        initView();
    }

    void initView(){
        proNameText=findViewById(R.id.name);
        normsText=findViewById(R.id.norms);
        propertyText=findViewById(R.id.property);
        unitText=findViewById(R.id.unit);
        depotNmaetext=findViewById(R.id.depotName);
        kucunCountText=findViewById(R.id.kucunCount);
        kucunzongeText=findViewById(R.id.kucunzonge);
        chengbenjiaText=findViewById(R.id.chengbenjia);
        backBtn=findViewById(R.id.back);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Intent intent=getIntent();
        proNameText.setText(intent.getStringExtra("proName"));
        normsText.setText(intent.getStringExtra("norms"));
        propertyText.setText(intent.getStringExtra("property"));
        unitText.setText(intent.getStringExtra("unit"));
        depotNmaetext.setText(intent.getStringExtra("depotName"));
        kucunCountText.setText(intent.getIntExtra("stockTotal",0)+"");
        kucunzongeText.setText(intent.getFloatExtra("stockAmount",0)+"");
        chengbenjiaText.setText(intent.getFloatExtra("price",0)+"");

    }







}
