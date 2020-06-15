package com.example.qunxin.erp.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.qunxin.erp.R;
import com.example.qunxin.erp.UserBaseDatus;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class AddCangkuActivity extends AppCompatActivity implements View.OnClickListener {

    EditText nameText;
    //EditText noText;
    EditText addressText;
    EditText remarksText;
    View backBtn;
    View saveBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_cangku);

        initView();
    }

    void initView(){
        nameText=findViewById(R.id.add_cangku_Name);
        //noText=findViewById(R.id.add_cangku_No);
        addressText=findViewById(R.id.add_cangku_address);
        remarksText=findViewById(R.id.add_cangku_remarksText);
        backBtn=findViewById(R.id.add_cangku_back);
        saveBtn=findViewById(R.id.add_cangku_save);

        backBtn.setOnClickListener(this);
        saveBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.add_cangku_back:
                finish();
                break;
            case R.id.add_cangku_save:
                save();
                break;
        }
    }

    String data="";
    void save(){
        String signa= UserBaseDatus.getInstance().getSign();
        String createBy=UserBaseDatus.getInstance().userId;
        String name=nameText.getText().toString();
        String addr=addressText.getText().toString();

        final String string ="signa="+signa+"&&createBy="+createBy+"&&name="+name+"&&addr="+addr;
        final String contentType = "application/x-www-form-urlencoded";
        final String url=UserBaseDatus.getInstance().url+"api/app/depot/addDepot";

        new Thread(new Runnable() {
            @Override
            public void run() {
                Map map=UserBaseDatus.getInstance().isSuccessPost(url,string, contentType);
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
                            Toast.makeText(AddCangkuActivity.this, data, Toast.LENGTH_SHORT).show();
                            data="";
                        }
                    });
                }
            }
        }).start();
    }
}
