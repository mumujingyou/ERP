package com.example.qunxin.erp.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.qunxin.erp.R;
import com.example.qunxin.erp.UserBaseDatus;

import java.util.Map;

public class EditGongyingshangActivity extends AppCompatActivity implements View.OnClickListener {

    View backBtn;
    View saveBtn;
    View deleteBtn;

    TextView supNameText;
    TextView lianxirenText;
    TextView lianxidianhuaText;
    TextView bianhaoText;
    TextView youxiangText;
    TextView supTypeText;
    TextView lianxidizhiText;
    TextView fuzerenText;
    //TextView qichuqinakuanText;
    TextView remarksText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_gongyingshang);


        initView();
    }

    String id;

    void initView(){
        supNameText=findViewById(R.id.edit_gongyingshang_name);
        lianxirenText=findViewById(R.id.edit_gongyingshang_lianxiren);
        lianxidianhuaText=findViewById(R.id.edit_gongyingshang_lianxirendianhua);
        bianhaoText=findViewById(R.id.edit_gongyingshang_bianhao);
        youxiangText=findViewById(R.id.edit_gongyingshang_youxiang);
        supTypeText=findViewById(R.id.edit_gongyingshang_supType);

        lianxidizhiText=findViewById(R.id.edit_gongyingshang_lianxidizhi);
        fuzerenText=findViewById(R.id.edit_gongyingshang_fuzeren);
        //qichuqinakuanText=findViewById(R.id.edit_gongyingshang_qichuqiankuan);
        remarksText=findViewById(R.id.edit_gongyingshang_remark);

        backBtn=findViewById(R.id.edit_gongyingshang_back);
        saveBtn=findViewById(R.id.edit_gongyingshang_save);
        deleteBtn=findViewById(R.id.btn_delete);

        backBtn.setOnClickListener(this);
        saveBtn.setOnClickListener(this);
        deleteBtn.setOnClickListener(this);
        supTypeText.setOnClickListener(this);

        Intent intent=getIntent();
        id=intent.getStringExtra("id");

        initModel();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.edit_gongyingshang_back:
                finish();
                break;
            case R.id.edit_gongyingshang_save:


                save();
                break;
            case R.id.btn_delete:
                delete(id);
                break;

            case R.id.edit_gongyingshang_supType:
                Intent intent1=new Intent(EditGongyingshangActivity.this,XuanzegongyingshangfengleiActivity.class);
                startActivityForResult(intent1,1);
        }
    }

    String gongyingshgnID;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case 1:
                if(resultCode==5){
                    String s=data.getStringExtra("gongyingshangYypeName");
                    supTypeText.setText(s);
                    gongyingshgnID=data.getStringExtra("gongyingshangID");
                    Log.d("gongyingshgnID", gongyingshgnID);
                }
        }
    }

    void initModel(){
        Intent intent=getIntent();

        supNameText.setText(intent.getStringExtra("supName"));
        lianxirenText.setText(intent.getStringExtra("lianxiren"));
        lianxidianhuaText.setText(intent.getStringExtra("lianxidianhua"));
        bianhaoText.setText(intent.getStringExtra("bianhao"));
        youxiangText.setText(intent.getStringExtra("youxiang"));

        lianxidizhiText.setText(intent.getStringExtra("lianxidizhi"));
        fuzerenText.setText(intent.getStringExtra("fuzeren"));
        //qichuqinakuanText.setText(intent.getStringExtra("qichuqiankuan"));
        remarksText.setText(intent.getStringExtra("remarks"));
        supTypeText.setText(intent.getStringExtra("supType"));
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
        String supName=supNameText.getText().toString();
        String signa= UserBaseDatus.getInstance().getSign();
        String contactPeople=lianxirenText.getText().toString();
        String phone=lianxidianhuaText.getText().toString();
        String localtion=lianxidizhiText.getText().toString();
        String qxLeader=fuzerenText.getText().toString();
        String userId=UserBaseDatus.getInstance().userId;
        String id=this.id;
        String type=gongyingshgnID;
        String supTypeName=supTypeText.getText().toString();


        final String stringData = "supName="+supName+"&&signa="+signa+"&&contactPeople="+contactPeople+"&&phone="+phone+
                "&&localtion="+localtion+"&&qxLeader="+qxLeader+"&&userId="+userId+"&&id="+id+"&&type="
                +type+"&&supTypeName="+supTypeName;


        final String contentType = "application/x-www-form-urlencoded";

        final String url=UserBaseDatus.getInstance().url+"api/suppliers/edit";

        Map map=UserBaseDatus.getInstance().isSuccessPost(url, stringData, contentType);
        if((boolean)(map.get("isSuccess"))){
            finish();
        }

    }


    void delete(final String id){
        new Thread(new Runnable() {
            @Override
            public void run() {
                final String strUrl = UserBaseDatus.getInstance().url+"api/suppliers/deleteSupplier";
                final String strData = "id="+id+"&&signa="+UserBaseDatus.getInstance().getSign();
                final String contentType = "application/x-www-form-urlencoded";
                Map map=UserBaseDatus.getInstance().isSuccessPost(strUrl, strData, contentType);
                if ((boolean)(map.get("isSuccess"))) {

                    Intent intent=new Intent(EditGongyingshangActivity.this,MainActivity.class);
                    startActivity(intent);
                }
            }
        }).start();
    }
}
