package com.example.qunxin.erp.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.qunxin.erp.R;
import com.example.qunxin.erp.UserBaseDatus;
import com.example.qunxin.erp.utils.CheckBankCard;

import java.util.HashMap;
import java.util.Map;

public class EditZhanghuActivity extends AppCompatActivity implements View.OnClickListener {


    TextView acNameText;
    TextView bankNameText;
    TextView bankNoText;
    TextView remarksText;
    View backBtn;
    View saveBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_zhanghu);
        initView();
    }

    void initView(){
        acNameText=findViewById(R.id.edit_zhanghu_zhanghuName);
        bankNameText=findViewById(R.id.edit_zhanghu_yinhangName);
        bankNoText=findViewById(R.id.edit_zhanghu_yinhangzhanghao);
        remarksText=findViewById(R.id.edit_zhanghu_remark);

        backBtn=findViewById(R.id.edit_zhanghu_back);
        saveBtn=findViewById(R.id.edit_zhanghu_save);

        backBtn.setOnClickListener(this);
        saveBtn.setOnClickListener(this);

        initModel();

    }
    String id="";
    void initModel(){
        Intent intent=getIntent();
        String acName=intent.getStringExtra("acName");
        String bankName=intent.getStringExtra("bankName");
        String bankNo=intent.getStringExtra("bankNo");
        String remarks=intent.getStringExtra("remarks");
        id=intent.getStringExtra("id");
        acNameText.setText(acName);
        bankNameText.setText(bankName);
        bankNoText.setText(bankNo);
        remarksText.setText(remarks);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.edit_zhanghu_back:
                finish();
                break;
            case R.id.edit_zhanghu_save:
                save();
                break;
        }
    }


    void save(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                saveChangeShangpin();
            }
        }).start();
    }

    void saveChangeShangpin(){

        String bankNo="";
        if(CheckBankCard.getInstance().checkBankCard(bankNoText.getText().toString())){
            bankNo=bankNoText.getText().toString();
        }else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(EditZhanghuActivity.this, "请输入正确的银行账号", Toast.LENGTH_SHORT).show();
                }
            });

            return;
        }
        String acName=acNameText.getText().toString();
        String bankName=bankNameText.getText().toString();
        String id=this.id;
        String remarks=remarksText.getText().toString();
        String signa= UserBaseDatus.getInstance().getSign();
        String updateBy="1";

        Map<String, String> jsonMap = new HashMap<>();

        jsonMap.put("acName", acName);
        jsonMap.put("bankName", bankName);
        jsonMap.put("bankNo",bankNo);
        jsonMap.put("id",id);
        jsonMap.put("remarks",remarks);
        jsonMap.put("signa",signa);
        jsonMap.put("updateBy",updateBy);

        final String jsonString = JSON.toJSONString(jsonMap);

        final String contentType = "application/json";
        final String url=UserBaseDatus.getInstance().url+"api/app/settleAccount/edit";

        Map map=UserBaseDatus.getInstance().isSuccessPost(url, jsonString, contentType);
        if((boolean)(map.get("isSuccess"))){
            Intent intent=new Intent();
            intent.putExtra("acName",acName);
            intent.putExtra("bankName",bankName);
            intent.putExtra("bankNo",bankNo);
            intent.putExtra("remarks",remarks);
            setResult(1,intent);
            finish();
        }
    }
}
