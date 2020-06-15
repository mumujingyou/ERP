package com.example.qunxin.erp.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qunxin.erp.R;
import com.example.qunxin.erp.UserBaseDatus;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class AddKehuActivity extends AppCompatActivity implements View.OnClickListener {


    TextView kehumingchengText;
    TextView lianxirenText;
    TextView lianxidianhuaText;
    TextView youxiangText;
    TextView chuanzhenText;
    TextView lianxidizhiText;
    TextView bengongsifuzerenText;
    //TextView qichuqiankuanText;
    TextView remarksText;

    View backBtn;
    View saveBtn;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_kuhu);

        initView();
    }

    void initView(){
        kehumingchengText=findViewById(R.id.add_kehu_kehumingcheng);
        lianxirenText=findViewById(R.id.add_kehu_lianxiren);
        lianxidianhuaText=findViewById(R.id.add_kehu_lianxidianhua);;
        youxiangText=findViewById(R.id.add_kehu_youxiang);;
        chuanzhenText=findViewById(R.id.add_kehu_chuanzhen);;
        lianxidizhiText=findViewById(R.id.add_kehu_lianxidizhi);;
        bengongsifuzerenText=findViewById(R.id.add_kehu_bengongsifuzeren);;
        //qichuqiankuanText=findViewById(R.id.add_kehu_qichuqinakuan);;
        remarksText=findViewById(R.id.add_kehu_remarksText);;

        backBtn=findViewById(R.id.add_kehu_back);
        saveBtn=findViewById(R.id.add_kehu_save);
        backBtn.setOnClickListener(this);
        saveBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.add_kehu_back:
                finish();
                break;
            case R.id.add_kehu_save:
                save();
                break;
        }
    }




    public static boolean isLoad=false;
    String data="";
    void save(){

        if(!UserBaseDatus.getInstance().judPhone(AddKehuActivity.this,lianxidianhuaText)){
            return;
        }

//        if(!UserBaseDatus.getInstance().isEmail(youxiangText.getText().toString(),AddKehuActivity.this)){
//            return;
//        }


        String custName=kehumingchengText.getText().toString();
        String signa= UserBaseDatus.getInstance().getSign();
        String contactPeople=lianxirenText.getText().toString();
        String phone=lianxidianhuaText.getText().toString();
        String location=lianxidizhiText.getText().toString();
        String qxLeader=bengongsifuzerenText.getText().toString();
        String email=youxiangText.getText().toString();
        String fax=chuanzhenText.getText().toString();
        String remarks=remarksText.getText().toString();
        String userId="1";
        Log.d("location", location);

        final String string ="custName="+custName+"&&signa="+signa+"&&contactPeople="+contactPeople+"&&phone="+phone+
                "&&localtion="+location+"&&qxLeader="+qxLeader+"&&userId="+userId+"&&email="+email+
                "&&fax="+fax+"&&remarks="+remarks;
        final String contentType = "application/x-www-form-urlencoded";
        final String url=UserBaseDatus.getInstance().url+"api/custs/add";

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
                            Toast.makeText(AddKehuActivity.this, data, Toast.LENGTH_SHORT).show();
                            data="";
                        }
                    });
                }
            }
        }).start();
    }
}
