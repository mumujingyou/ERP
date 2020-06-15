package com.example.qunxin.erp.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.qunxin.erp.R;
import com.example.qunxin.erp.UserBaseDatus;

/**
 * Created by qunxin on 2019/7/31.
 */

public class GuankeFragment extends Fragment implements RadioGroup.OnCheckedChangeListener {

    RadioGroup radioGroup;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_guanke,container,false);
        radioGroup=view.findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(this);


        if(UserBaseDatus.getInstance().isKehu){
            Log.d("heihei", "kehu");
            FragmentTransaction transaction = getFragmentManager().beginTransaction();

            kehuFragment=new KehuFragment();
            transaction.replace(R.id.guankefragment_container,kehuFragment);
            RadioButton radioButton=view.findViewById(R.id.kehu);
            radioButton.setChecked(true);
            transaction.commit();
            return  view;


        }else {
            FragmentTransaction transaction = getFragmentManager().beginTransaction();

            Log.d("heihei", "gongyingshang");
            gongyingshangFragment=new GongyingshangFragment();
            transaction.replace(R.id.guankefragment_container,gongyingshangFragment);
            RadioButton radioButton=view.findViewById(R.id.gongyingshang);
            radioButton.setChecked(true);
            transaction.commit();
            return  view;


        }

    }



    KehuFragment kehuFragment;
    GongyingshangFragment gongyingshangFragment;
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

        FragmentTransaction transaction = getFragmentManager().beginTransaction();

        switch (checkedId){
            case R.id.kehu:
                kehuFragment=new KehuFragment();
                transaction.replace(R.id.guankefragment_container,kehuFragment);
                UserBaseDatus.getInstance().isKehu=true;
                break;
            case R.id.gongyingshang:
                gongyingshangFragment=new GongyingshangFragment();
                transaction.replace(R.id.guankefragment_container,gongyingshangFragment);
                UserBaseDatus.getInstance().isKehu=false;
                break;
        }
        transaction.commit();
    }
}
