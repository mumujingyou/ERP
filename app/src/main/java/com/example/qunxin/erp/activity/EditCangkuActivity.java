package com.example.qunxin.erp.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.qunxin.erp.R;
import com.example.qunxin.erp.UserBaseDatus;
import com.example.qunxin.erp.utils.CheckBankCard;

import java.util.HashMap;
import java.util.Map;

public class EditCangkuActivity extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    EditText nameText;
    Switch isLockSwitch;
    TextView addressText;
    private View backBtn;
    private View saveBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_cangku);

        initView();
    }
    
    void initView(){
        nameText=findViewById(R.id.edit_cangku_Name);

        addressText=findViewById(R.id.edit_cangku_address);

        isLockSwitch=findViewById(R.id.edit_cangku_isLock);

        backBtn=findViewById(R.id.edit_cangku_back);
        saveBtn=findViewById(R.id.edit_cangku_save);

        backBtn.setOnClickListener(this);
        saveBtn.setOnClickListener(this);

        isLockSwitch.setOnCheckedChangeListener(this);

        initModel();
    }

    String id;
    void initModel(){
        Intent intent=getIntent();
        String name=intent.getStringExtra("name");
        String address=intent.getStringExtra("address");
        String isLock=intent.getStringExtra("isLock");
        id=intent.getStringExtra("id");

        nameText.setText(name);
        addressText.setText(address);
        if(isLock.equals("1")){
            isLockSwitch.setChecked(true);
        }else if(isLock.equals("0")){
            isLockSwitch.setChecked(false);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.edit_cangku_back:

                finish();
                break;
            case R.id.edit_cangku_save:
                save();
                break;
        }
    }

    void save(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                saveChangeShangpin();
                if(isChange){
                    lock();
                }
            }
        }).start();
    }

    void saveChangeShangpin(){


        String name=nameText.getText().toString();
        String addr=addressText.getText().toString();
        String id=this.id;
        String signa= UserBaseDatus.getInstance().getSign();
        String updateBy=UserBaseDatus.getInstance().userId;

        final String stringData = "id="+id+"&&signa="+signa+"&&updateBy="+updateBy+"&&name="+name+"&&addr="+addr;


        final String contentType = "application/x-www-form-urlencoded";

        final String url=UserBaseDatus.getInstance().url+"api/app/depot/editDepot";

        Map map=UserBaseDatus.getInstance().isSuccessPost(url, stringData, contentType);
        if((boolean)(map.get("isSuccess"))){
            finish();
        }

    }

    Boolean lock(){
        String id=this.id;
        String signa= UserBaseDatus.getInstance().getSign();
        final String stringData = "id="+id+"&&signa="+signa;


        final String contentType = "application/x-www-form-urlencoded";

        final String url=UserBaseDatus.getInstance().url+"api/app/depot/updateStatus";

        Map map=UserBaseDatus.getInstance().isSuccessPost(url, stringData, contentType);
        return  (boolean)(map.get("isSuccess"));
    }


    boolean isChange=false;
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        //防止初始化的时候出发监听
        if (!buttonView.isPressed()) {
            return;
        }
       isChange=!isChange;
    }

}
