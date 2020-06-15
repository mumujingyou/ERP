package com.example.qunxin.erp.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.qunxin.erp.R;
import com.example.qunxin.erp.activity.ShangpinliebiaoActivity;

/**
 * Created by qunxin on 2019/8/15.
 */

public class ShangpinliebiaoPaixuFragment extends Fragment {

    View shangpinliebiao_paixu_search;
    View view;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         view=inflater.inflate(R.layout.fragement_shangpinliebiao_paixu,container,false);

        initView();

        return view;
    }


    void initView(){
        shangpinliebiao_paixu_search=view.findViewById(R.id.shangpinliebiao_paixu_serch);
        shangpinliebiao_paixu_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //((ShangpinliebiaoActivity)(getActivity())).addShangpinliebiaoSearchFragment();
            }
        });
    }

}
