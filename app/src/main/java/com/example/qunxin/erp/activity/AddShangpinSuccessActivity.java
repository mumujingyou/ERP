package com.example.qunxin.erp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.example.qunxin.erp.BaseActivity;
import com.example.qunxin.erp.R;

/**
 * Created by qunxin on 2019/8/16.
 */

public class AddShangpinSuccessActivity extends BaseActivity {

    View finishBtn;
    View contniueBtn;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_shangpin_success);
        initView();
    }

    void initView(){
        finishBtn=findViewById(R.id.add_shangpin_success_finish);
        contniueBtn=findViewById(R.id.add_shangpin_success_add);

        finishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AddShangpinSuccessActivity.this,BaseDatusActivity.class);
                startActivity(intent);
                finish();
            }
        });

        contniueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AddShangpinSuccessActivity.this,AddShangpinActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }


}
