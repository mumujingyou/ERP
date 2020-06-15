package com.example.qunxin.erp.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.example.qunxin.erp.R;
import com.example.qunxin.erp.UserBaseDatus;

import org.json.JSONException;

import java.util.Map;

public class EditShangpinTypeActivity extends AppCompatActivity implements View.OnClickListener {
    View backBtn;
    View saveBtn;
    EditText typeNameText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_shangpin_type);

        initView();
    }

    void initView(){
        backBtn=findViewById(R.id.edit_shangpintype_back);
        saveBtn=findViewById(R.id.edit_shangpintype_save);
        typeNameText =findViewById(R.id.edit_shangpintype_name);

        backBtn.setOnClickListener(this);
        saveBtn.setOnClickListener(this);



    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.edit_shangpintype_back:
                finish();
                break;
            case R.id.edit_shangpintype_save:
                String typeName= typeNameText.getText().toString();

                Intent intent=new Intent();
                intent.putExtra("typeName",typeName);

                setResult(1,intent);
                save();
                finish();
                break;
        }
    }

    void save(){
        Intent intent=getIntent();
        final String type=intent.getStringExtra("type");

        Log.d("type", type);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    editShangpinType(type);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    //编辑商品类型
    boolean editShangpinType(String type) throws JSONException {
        String typeName= typeNameText.getText().toString();
        final String contentType = "application/x-www-form-urlencoded";
        final String url = UserBaseDatus.getInstance().url + "api/app/proType/edit";
        final String data = "signa=" + UserBaseDatus.getInstance().getSign() + "&&id=" + type+"&&userId="+UserBaseDatus.getInstance().userId+"&&typeName="+typeName;
        Map map = UserBaseDatus.getInstance().isSuccessPost(url, data, contentType);
        if ((boolean) (map.get("isSuccess"))) {

            return true;
        }
        return false;
    }

}
