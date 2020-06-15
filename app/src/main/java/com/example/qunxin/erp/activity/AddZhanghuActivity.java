package com.example.qunxin.erp.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.qunxin.erp.R;
import com.example.qunxin.erp.UserBaseDatus;
import com.example.qunxin.erp.utils.CheckBankCard;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class AddZhanghuActivity extends AppCompatActivity implements View.OnClickListener {

    TextView acNameText;
    TextView bankNameText;
    TextView bankNoText;
    TextView amoumtText;
    TextView remarksText;

    View backBtn;
    View saveBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_zhanghu);

        initView();
    }

    void initView(){
        acNameText=findViewById(R.id.add_zhanghu_zhanghuName);
        bankNameText=findViewById(R.id.add_zhanghu_yinhangName);
        bankNoText=findViewById(R.id.add_zhanghu_yinhangzhanghao);
        amoumtText=findViewById(R.id.add_zhanghu_chuqiyue);
        remarksText=findViewById(R.id.add_zhanghu_remarksText);

        backBtn=findViewById(R.id.add_zhanghu_back);
        saveBtn=findViewById(R.id.add_zhanghu_save);

        backBtn.setOnClickListener(this);
        saveBtn.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.add_zhanghu_back:
                finish();
                break;
            case R.id.add_zhanghu_save:
                save();
                break;
        }
    }

    String data="";
    private void save() {
        String bankNo="";
        if(CheckBankCard.getInstance().checkBankCard(bankNoText.getText().toString())){
            bankNo=bankNoText.getText().toString();
        }else {
            Toast.makeText(this, "请输入正确的银行账号", Toast.LENGTH_SHORT).show();
            return;
        }

        String acName=acNameText.getText().toString();
        String amount=amoumtText.getText().toString();
        String bankName=bankNameText.getText().toString();
        String createBy=UserBaseDatus.getInstance().userId;
        String remarks=remarksText.getText().toString();
        String signa= UserBaseDatus.getInstance().getSign();


        Map<String, String> jsonMap = new HashMap<>();
        jsonMap.put("acName", acName);
        jsonMap.put("amount", amount);
        jsonMap.put("bankName",bankName);
        jsonMap.put("bankNo",bankNo);
        jsonMap.put("createBy",createBy);
        jsonMap.put("signa",signa);
        jsonMap.put("remarks",remarks);

        final String jsonString = JSON.toJSONString(jsonMap);


        final String contentType = "application/json";
        final String url=UserBaseDatus.getInstance().url+"api/app/settleAccount/add";

        new Thread(new Runnable() {
            @Override
            public void run() {
                Map map=UserBaseDatus.getInstance().isSuccessPost(url, jsonString, contentType);
                if((boolean)(map.get("isSuccess"))){

                    finish();
                }else {
                    JSONObject jsonObject= (JSONObject) map.get("json");
                    try {
                        data =jsonObject.getString("data");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(AddZhanghuActivity.this, data, Toast.LENGTH_SHORT).show();
                            data="";
                        }
                    });
                }
            }
        }).start();

    }





}
