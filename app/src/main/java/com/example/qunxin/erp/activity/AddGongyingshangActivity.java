package com.example.qunxin.erp.activity;


import android.app.FragmentManager;
import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qunxin.erp.R;
import com.example.qunxin.erp.UserBaseDatus;
import com.example.qunxin.erp.fragment.GongyingshangFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class AddGongyingshangActivity extends AppCompatActivity implements View.OnClickListener {

    EditText gongyingshangNameText;
    EditText lianxirenText;
    EditText lianxidianhuaText;
    EditText youxiangText;
    TextView gongyingshangTypeText;
    EditText lianxidizhiText;
    EditText bengongsifuzerenText;
    //EditText qichuqiankuanText;
    EditText remarksText;

    View backBtn;
    View saveBtn;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_gongyingshang);

        initView();
    }

    void initView(){
        gongyingshangNameText =findViewById(R.id.add_gongyingsahang_name);
        lianxirenText=findViewById(R.id.add_gongyingsahang_lianxiren);
        lianxidianhuaText=findViewById(R.id.add_gongyingsahang_lianxidianhua);;
        youxiangText=findViewById(R.id.add_gongyingsahang_youxiang);;
        gongyingshangTypeText =findViewById(R.id.add_gongyingsahang_kind);;
        lianxidizhiText=findViewById(R.id.add_gongyingsahang_lianxidizhi);;
        bengongsifuzerenText=findViewById(R.id.add_gongyingsahang_fuzeren);;
        //qichuqiankuanText=findViewById(R.id.add_gongyingsahang_chuqiqiankuan);;
        remarksText=findViewById(R.id.add_gongyingshang_remarksText);;

        backBtn=findViewById(R.id.add_gongyingshang_back);
        saveBtn=findViewById(R.id.add_gongyingshang_save);
        gongyingshangTypeText.setOnClickListener(this);
        backBtn.setOnClickListener(this);
        saveBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.add_gongyingshang_back:
                finish();
                break;
            case R.id.add_gongyingshang_save:
                save();
                break;
            case R.id.add_gongyingsahang_kind:
                Intent intent1=new Intent(AddGongyingshangActivity.this,XuanzegongyingshangfengleiActivity.class);
                startActivityForResult(intent1,1);
                break;
        }
    }

    private String gongyingshangTypeId="";
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 1:
                if(resultCode==5){
                    String s=data.getStringExtra("gongyingshangYypeName");
                    gongyingshangTypeText.setText(s);
                    gongyingshangTypeId=data.getStringExtra("gongyingshangID");
                }
        }


    }

    String data="";
    public static boolean isLoad=false;

    @Override
    public void finishActivity(int requestCode) {
        super.finishActivity(requestCode);
    }

    void save(){


        UserBaseDatus.getInstance().judPhone(AddGongyingshangActivity.this,lianxidianhuaText);
        UserBaseDatus.getInstance().isEmail(youxiangText.getText().toString(),AddGongyingshangActivity.this);



        String type=gongyingshangTypeId;
        String supName= gongyingshangNameText.getText().toString();
        String signa= UserBaseDatus.getInstance().getSign();
        String contactPeople=lianxirenText.getText().toString();
        String phone=lianxidianhuaText.getText().toString();
        String location=lianxidizhiText.getText().toString();
        String qxLeader=bengongsifuzerenText.getText().toString();
        String userId=UserBaseDatus.getInstance().userId;

        final String string ="supName="+supName+"&&signa="+signa+"&&contactPeople="+contactPeople+"&&phone="+phone+
                "&&localtion="+location+"&&qxLeader="+qxLeader+"&&userId="+UserBaseDatus.getInstance().userId+"&&type="+type;
        final String contentType = "application/x-www-form-urlencoded";
        final String url=UserBaseDatus.getInstance().url+"api/suppliers/add";

        new Thread(new Runnable() {
            @Override
            public void run() {
                Map map=UserBaseDatus.getInstance().isSuccessPost(url,string, contentType);
                if((boolean)(map.get("isSuccess"))){
                    isLoad=true;
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
                            Toast.makeText(AddGongyingshangActivity.this, data, Toast.LENGTH_SHORT).show();
                            data="";
                        }
                    });
                }
            }
        }).start();
    }
}
