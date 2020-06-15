package com.example.qunxin.erp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.qunxin.erp.R;

/**
 * Created by qunxin on 2019/8/12.
 */

public class GuigeshuxingActivity extends AppCompatActivity implements View.OnClickListener {
    View backBtn;
    View saveBtn;
    EditText guigeText;
    EditText shuxingText;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guigeshuxing);
        initView();
    }

    void initView(){
         backBtn=findViewById(R.id.guigeshuxing_back);
         saveBtn=findViewById(R.id.guigeshuxing_save);
         guigeText=findViewById(R.id.guigeshuxing_guige);
         shuxingText=findViewById(R.id.guigeshuxing_shuxing);

         backBtn.setOnClickListener(this);
        saveBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.guigeshuxing_back:
                finish();
                break;
            case R.id.guigeshuxing_save:

                String guige=guigeText.getText().toString();
                String shuxing=shuxingText.getText().toString();
                String data=guige+" "+shuxing;

                Intent intent=new Intent();
                intent.putExtra("guigeshuxing",data);

                setResult(3,intent);
                finish();

                break;
        }
    }
}
