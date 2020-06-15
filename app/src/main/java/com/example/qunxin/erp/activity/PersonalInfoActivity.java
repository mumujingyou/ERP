package com.example.qunxin.erp.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.qunxin.erp.BaseActivity;
import com.example.qunxin.erp.R;

/**
 * Created by qunxin on 2019/8/5.
 */


public class PersonalInfoActivity extends BaseActivity {

    View backBtn;
    View saveBtn;
    EditText userNameText;
    Spinner sexSpinner;
    EditText partmentText;
    EditText phoneNumberText;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_personal_info);
        super.onCreate(savedInstanceState);
        initView();
        ReadData();
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(judPhone()){
                    saveData();
                    finish();
                }else {

                }

            }
        });


    }

    void initView(){
         backBtn=findViewById(R.id.personalInfo_back);
         saveBtn=findViewById(R.id.personalInfo_save);
         userNameText =findViewById(R.id.personalInfo_userName);
         sexSpinner =findViewById(R.id.personalInfo_sexSpinner);
         partmentText =findViewById(R.id.personalInfo_partment);
         phoneNumberText =findViewById(R.id.personalInfo_phoneNumber);


        final String[] ctype = new String[]{"男","女"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, ctype);
        //创建一个数组适配器
         adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // 设置下拉列表框的下拉选项样式

        sexSpinner.setAdapter(adapter);

        sexSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                index=position+"";
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    String index="0";
    void saveData(){
        String userName=userNameText.getText().toString();

        String partment=partmentText.getText().toString();
        String phoneNumber=phoneNumberText.getText().toString();

        SharedPreferences.Editor editor = getSharedPreferences("baseData", Activity.MODE_PRIVATE).edit();
        editor.putString("userName",userName);
        editor.putString("sex",index);
        editor.putString("partment",partment);
        editor.putString("phoneNumber",phoneNumber);
        editor.commit();

    }

    void ReadData(){
        SharedPreferences read = getSharedPreferences("baseData", Activity.MODE_PRIVATE);
        userNameText.setText(read.getString("userName",""));

        try{
            int indexPos=Integer.parseInt(read.getString("sex","0"));
            sexSpinner.setSelection(indexPos,true);
        }catch (NumberFormatException e){

        }
        partmentText.setText(read.getString("partment",""));
        phoneNumberText.setText(read.getString("phoneNumber",""));
    }


    //验证手机号
    private boolean judPhone() {
        if (TextUtils.isEmpty(phoneNumberText.getText().toString().trim())) {
            Toast.makeText(PersonalInfoActivity.this, "请输入您的电话号码", Toast.LENGTH_SHORT).show();
            phoneNumberText.requestFocus();
            return false;
        } else if (phoneNumberText.getText().toString().trim().length() != 11) {
            Toast.makeText(PersonalInfoActivity.this, "您的电话号码位数不正确", Toast.LENGTH_SHORT).show();
            phoneNumberText.requestFocus();
            return false;
        } else {
            String phone_number = phoneNumberText.getText().toString().trim();
            String num = "[1][358]\\d{9}";
            if (phone_number.matches(num))
                return true;
            else {
                Toast.makeText(PersonalInfoActivity.this, "请输入正确的手机号码", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
    }

}
