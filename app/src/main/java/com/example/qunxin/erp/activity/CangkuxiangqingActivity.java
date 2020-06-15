package com.example.qunxin.erp.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.example.qunxin.erp.R;
import com.example.qunxin.erp.UserBaseDatus;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.Map;

public class CangkuxiangqingActivity extends AppCompatActivity implements View.OnClickListener {
    TextView nameText;
    TextView noText;
    Switch isLockSwitch;
    TextView addressText;
    TextView remarksText;
    private View backBtn;
    private View editBtn;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cangkuxiangqing);
        initView();
    }

    String id;
    void initView(){
        nameText=findViewById(R.id.cangkuxiangqiang_Name);
        noText=findViewById(R.id.cangkuxiangqiang_No);
        addressText=findViewById(R.id.cangkuxiangqiang_address);
        remarksText=findViewById(R.id.cangkuxiangqiang_remark);
        isLockSwitch=findViewById(R.id.cangkuxiangqiang_isLock);

        backBtn=findViewById(R.id.cangkuxiangqiang_back);
        editBtn=findViewById(R.id.cangkuxiangqiang_edit);

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
            case R.id.cangkuxiangqiang_back:
                finish();
                break;
            case R.id.cangkuxiangqiang_edit:
                Intent intent=new Intent(CangkuxiangqingActivity.this,EditCangkuActivity.class);
                intent.putExtra("name",name);

                intent.putExtra("address",address);
                intent.putExtra("isLock",isLock);
                intent.putExtra("id",id);

                startActivity(intent);
                break;
        }
    }



    String name;
    String No;
    String address;
    String isLock;
    String remarks;
    void loadModle() throws JSONException {


        final String contentType = "application/x-www-form-urlencoded";
        final String url = UserBaseDatus.getInstance().url + "api/app/depot/getDepotById";
        final String data = "signa=" + UserBaseDatus.getInstance().getSign() + "&&id=" + id;
        Map map = UserBaseDatus.getInstance().isSuccessPost(url, data, contentType);
        if ((boolean) (map.get("isSuccess"))) {
            JSONObject jsonObject=(JSONObject) map.get("json");

            JSONObject json=jsonObject.getJSONObject("data");

            name=json.getString("name");
            No=json.getString("depotNo");
            address=json.getString("addr");
            isLock=json.getString("status");
            remarks=json.getString("remark");

            loadView(name,No,address,isLock,remarks);
        }
    }

    private void loadView(final String name,final String No,final String address,final String isLock,final String remarks) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                nameText.setText(name);
                noText.setText(No);
                addressText.setText(address);
                Log.d("isLock", isLock);
                if(isLock.equals("1")){
                    isLockSwitch.setChecked(true);
                }
                if(isLock.equals("0")){
                    isLockSwitch.setChecked(false);
                }
                remarksText.setText(remarks);

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
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
}
