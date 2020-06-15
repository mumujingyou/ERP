package com.example.qunxin.erp.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qunxin.erp.R;
import com.example.qunxin.erp.UserBaseDatus;
import com.example.qunxin.erp.modle.KehuDatus;

import org.json.JSONException;

import java.util.Map;

public class EditKehuActivity extends AppCompatActivity implements View.OnClickListener {

    View backBtn;
    View saveBtn;
    View deleteBtn;

    TextView kehumingchengText;
    TextView lianxirenText;
    TextView lianxidianhuaText;
    TextView bianhaoText;
    TextView youxiangText;
    TextView chuanzhenText;
    TextView lianxidizhiText;
    TextView fuzerenText;
    //TextView qichuqinakuanText;
    TextView remarksText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_kehu);


        initView();
    }

    String id;

    void initView(){
        kehumingchengText=findViewById(R.id.edit_kehu_kehumingcheng);
        lianxirenText=findViewById(R.id.edit_kehu_lianxiren);
        lianxidianhuaText=findViewById(R.id.edit_kehu_lianxirendianhua);
        bianhaoText=findViewById(R.id.edit_kehu_bianhao);
        youxiangText=findViewById(R.id.edit_kehu_youxiang);

        lianxidizhiText=findViewById(R.id.edit_kehu_lianxidizhi);
        fuzerenText=findViewById(R.id.edit_kehu_fuzeren);
        //qichuqinakuanText=findViewById(R.id.edit_kehu_qichuqiankuan);
        remarksText=findViewById(R.id.edit_kehu_remark);

        backBtn=findViewById(R.id.edit_kehu_back);
        saveBtn=findViewById(R.id.edit_kehu_save);
        deleteBtn=findViewById(R.id.btn_delete);

        backBtn.setOnClickListener(this);
        saveBtn.setOnClickListener(this);
        deleteBtn.setOnClickListener(this);

        Intent intent=getIntent();
        id=intent.getStringExtra("id");

        initModel();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.edit_kehu_back:
                finish();
                break;
            case R.id.edit_kehu_save:


                save();
                break;
            case R.id.btn_delete:
                delete(id);
                break;
        }
    }


    void initModel(){
        Intent intent=getIntent();

        kehumingchengText.setText(intent.getStringExtra("kehumingcheng"));
        lianxirenText.setText(intent.getStringExtra("lianxiren"));
        lianxidianhuaText.setText(intent.getStringExtra("lianxidianhua"));
        bianhaoText.setText(intent.getStringExtra("bianhao"));
        youxiangText.setText(intent.getStringExtra("youxiang"));

        lianxidizhiText.setText(intent.getStringExtra("lianxidizhi"));
        fuzerenText.setText(intent.getStringExtra("fuzeren"));
        //qichuqinakuanText.setText(intent.getStringExtra("qichuqiankuan"));
        remarksText.setText(intent.getStringExtra("remarks"));
    }


    void save(){



        new Thread(new Runnable() {
            @Override
            public void run() {
                saveKehu();

            }
        }).start();
    }

    void saveKehu(){
        String custName=kehumingchengText.getText().toString();
        String signa=UserBaseDatus.getInstance().getSign();
        String contactPeople=lianxirenText.getText().toString();
        String phone=lianxidianhuaText.getText().toString();
        String localtion=lianxidizhiText.getText().toString();
        String qxLeader=fuzerenText.getText().toString();
        String userId=UserBaseDatus.getInstance().userId;
        String id=this.id;


        final String stringData = "custName="+custName+"&&signa="+signa+"&&contactPeople="+contactPeople+"&&phone="+phone+
                "&&localtion="+localtion+"&&qxLeader="+qxLeader+"&&userId="+userId+"&&id="+id;


        final String contentType = "application/x-www-form-urlencoded";

        final String url=UserBaseDatus.getInstance().url+"api/custs/edit";

        Map map=UserBaseDatus.getInstance().isSuccessPost(url, stringData, contentType);
        if((boolean)(map.get("isSuccess"))){
            finish();
        }

    }


    void delete(final String id){
        new Thread(new Runnable() {
            @Override
            public void run() {
                final String strUrl = UserBaseDatus.getInstance().url+"api/custs/remove";
                final String strData = "id="+id+"&&signa="+UserBaseDatus.getInstance().getSign();
                final String contentType = "application/x-www-form-urlencoded";
                Map map=UserBaseDatus.getInstance().isSuccessPost(strUrl, strData, contentType);
                if ((boolean)(map.get("isSuccess"))) {

                    Intent intent=new Intent(EditKehuActivity.this,MainActivity.class);
                    startActivity(intent);
                }
            }
        }).start();
    }
}
