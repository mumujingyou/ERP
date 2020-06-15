package com.example.qunxin.erp.activity;

import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.example.qunxin.erp.R;
import com.example.qunxin.erp.UserBaseDatus;
import com.example.qunxin.erp.fragment.DairukudanFragment;
import com.example.qunxin.erp.fragment.GongyingshangFragment;
import com.example.qunxin.erp.fragment.KehuFragment;
import com.example.qunxin.erp.fragment.RukudanFragment;

public class RukulishiActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rukulishi);

        initView();
    }


    RadioGroup radioGroup;

    void initView(){
        radioGroup=findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(this);

        DairukudanFragment dairukuFagment=new DairukudanFragment();
        transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container,dairukuFagment);
        transaction.commit();


        View backBtn=findViewById(R.id.back);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }





    FragmentTransaction transaction;
    DairukudanFragment dairukuFagment;
    RukudanFragment rukudanFragment;
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

        transaction = getSupportFragmentManager().beginTransaction();

        switch (checkedId){
            case R.id.kehu:
                dairukuFagment=new DairukudanFragment();
                transaction.replace(R.id.container,dairukuFagment);
                break;
            case R.id.gongyingshang:
                rukudanFragment=new RukudanFragment();
                transaction.replace(R.id.container,rukudanFragment);
                break;
        }
        transaction.commit();
    }
}
