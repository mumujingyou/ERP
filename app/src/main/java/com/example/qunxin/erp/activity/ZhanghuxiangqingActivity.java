package com.example.qunxin.erp.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.qunxin.erp.R;
import com.example.qunxin.erp.UserBaseDatus;
import com.example.qunxin.erp.util.Constant;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class ZhanghuxiangqingActivity extends AppCompatActivity implements View.OnClickListener {

    TextView acNameText;
    TextView bankNameText;
    TextView bankNoText;
    TextView originalAmountText;
    TextView amountText;
    TextView remarksText;
    View backBtn;
    View editBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zhanghuxiangqing);

        initView();
    }

    String id;
    void initView(){
        acNameText=findViewById(R.id.zhanghuxiangqiang_zhanghuName);
        bankNameText=findViewById(R.id.zhanghuxiangqiang_yinhangName);
        bankNoText=findViewById(R.id.zhanghuxiangqiang_yinhangzhanghao);
        originalAmountText=findViewById(R.id.zhanghuxiangqiang_chuqiyue);
        amountText=findViewById(R.id.zhanghuxiangqiang_zhanghuyue);
        remarksText=findViewById(R.id.zhanghuxiangqiang_remark);
        backBtn=findViewById(R.id.zhanghuxiangqiang_back);
        editBtn=findViewById(R.id.zhanghuxiangqiang_edit);

        backBtn.setOnClickListener(this);
        editBtn.setOnClickListener(this);


        Intent intent=getIntent();
        id=intent.getStringExtra("id");

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    loadModle();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.zhanghuxiangqiang_back:
                finish();
                break;
            case R.id.zhanghuxiangqiang_edit:
                Intent intent=new Intent(ZhanghuxiangqingActivity.this,EditZhanghuActivity.class);
                intent.putExtra("acName",acName);
                intent.putExtra("bankName",bankName);
                intent.putExtra("bankNo",bankNo);
                intent.putExtra("remarks",remarks);
                intent.putExtra("id",id);
                startActivityForResult(intent,1);
                break;
        }
    }

    String acName;
    String bankName;
    String bankNo;
    String originalAmount;
    String amount;
    String isDefault;
    String remarks;

    void loadModle() throws JSONException {
        final String contentType = "application/x-www-form-urlencoded";
        final String url = UserBaseDatus.getInstance().url + "api/app/settleAccount/getSettleAccountById";
        final String data = "signa=" + UserBaseDatus.getInstance().getSign() + "&&id=" + id;
        Map map = UserBaseDatus.getInstance().isSuccessPost(url, data, contentType);
        if ((boolean) (map.get("isSuccess"))) {
            JSONObject jsonObject=(JSONObject) map.get("json");

            JSONObject json=jsonObject.getJSONObject("data");

            acName=json.getString("acName");
            bankName=json.getString("bankName");
            bankNo=json.getString("bankNo");
            originalAmount=json.getString("originalAmount");
            amount=json.getString("amount");
            remarks=json.getString("remarks");
            loadView(acName,bankName,bankNo,originalAmount,amount,remarks);

        }

    }

    private void loadView(final String acName,final String bankName,final String bankNo,final String originalAmount,final String amount,final String remarks) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                acNameText.setText(acName);
                bankNameText.setText(bankName);
                bankNoText.setText(bankNo);
                originalAmountText.setText(originalAmount);
                amountText.setText(amount);
                remarksText.setText(remarks);

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 1:
                if(resultCode==1){
                    String acName=data.getStringExtra("acName");
                    String bankName=data.getStringExtra("bankName");
                    String bankNo=data.getStringExtra("bankNo");
                    String remarks=data.getStringExtra("remarks");
                    loadView(acName,bankName,bankNo,originalAmount,amount,remarks);
                }
        }
    }
}
