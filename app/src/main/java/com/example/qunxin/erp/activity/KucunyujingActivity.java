package com.example.qunxin.erp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.example.qunxin.erp.R;

/**
 * Created by qunxin on 2019/8/12.
 */

public class KucunyujingActivity extends AppCompatActivity implements View.OnClickListener {
    View backBtn;
    View saveBtn;
    EditText editText;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kucunyujing);

        initView();
    }

    void initView(){
        backBtn=findViewById(R.id.kucunyujing_back);
        saveBtn=findViewById(R.id.kucunyujing_save);
        editText=findViewById(R.id.kucunyujing_number);

        backBtn.setOnClickListener(this);
        saveBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.kucunyujing_back:
                finish();
                break;
            case R.id.kucunyujing_save:
                String number=editText.getText().toString();

                Intent intent=new Intent();
                intent.putExtra("number",number);

                setResult(4,intent);
                finish();
                break;
        }
    }


}
