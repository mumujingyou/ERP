package com.example.qunxin.erp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.qunxin.erp.R;
import com.example.qunxin.erp.activity.AddCaigoudingdanActivity;
import com.example.qunxin.erp.activity.AddCaigoudingdanAfterActivity;
import com.example.qunxin.erp.activity.AddDiaobodanActivity;
import com.example.qunxin.erp.activity.AddJinhuotuihuodanActivity;
import com.example.qunxin.erp.activity.AddXiaoshoudanActivity;
import com.example.qunxin.erp.activity.AddXiaoshoudingdanActivity;
import com.example.qunxin.erp.activity.AddXiaoshoutuihuodanActivity;
import com.example.qunxin.erp.activity.AddjinhuoActivity;
import com.example.qunxin.erp.activity.AddpandianActivity;
import com.example.qunxin.erp.activity.CaigoudingdanActivity;
import com.example.qunxin.erp.activity.DiaobolishiActivity;
import com.example.qunxin.erp.activity.JinhuolishiActivity;
import com.example.qunxin.erp.activity.JinhuotuihuolishiActivity;
import com.example.qunxin.erp.activity.KucunchaxunActivity;
import com.example.qunxin.erp.activity.PandianlishiActivity;
import com.example.qunxin.erp.activity.RukulishiActivity;
import com.example.qunxin.erp.activity.XiaoshoudingdanlishiActivity;
import com.example.qunxin.erp.activity.XiaoshoulishiActivity;
import com.example.qunxin.erp.activity.XiaoshoutuihuolishiActivity;

/**
 * Created by qunxin on 2019/7/31.
 */

public class GuanhuoFragment extends Fragment {


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_guanhuo,container,false);

        View caigoulishiItem=view.findViewById(R.id.caigoulishi_item);
        View caigoulishiAdd=view.findViewById(R.id.caigoulishi_add);

        caigoulishiItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), CaigoudingdanActivity.class);
                startActivity(intent);
            }
        });


        caigoulishiAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), AddCaigoudingdanActivity.class);
                startActivity(intent);
            }
        });


        View jinhuolishiItem=view.findViewById(R.id.jinhuolishi_item);
        View jinhuolishiAdd=view.findViewById(R.id.jinhuolishi_add);

        jinhuolishiItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), JinhuolishiActivity.class);
                startActivity(intent);
            }
        });


        jinhuolishiAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), AddjinhuoActivity.class);
                startActivity(intent);
            }
        });



        View jinhuotuihuolishiItem=view.findViewById(R.id.jinhuotuihuolishi_item);
        View jinhuotuihuolishiAdd=view.findViewById(R.id.jinhuotuihuolishi_add);

        jinhuotuihuolishiItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), JinhuotuihuolishiActivity.class);
                startActivity(intent);
            }
        });


        jinhuotuihuolishiAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), AddJinhuotuihuodanActivity.class);
                startActivity(intent);
            }
        });


        View rukulishi_item=view.findViewById(R.id.rukulishi_item);

        rukulishi_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), RukulishiActivity.class);
                startActivity(intent);
            }
        });





        View xiaoshoulishi_item=view.findViewById(R.id.xiaoshoulishi_item);
        View xiaoshoulishi_add=view.findViewById(R.id.xiaoshoulishi_add);

        xiaoshoulishi_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), XiaoshoulishiActivity.class);
                startActivity(intent);
            }
        });


        xiaoshoulishi_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), AddXiaoshoudanActivity.class);
                startActivity(intent);
            }
        });


        View xiaoshoutuihuolishi_item=view.findViewById(R.id.xiaoshoutuihuolishi_item);
        View xiaoshoutuihuolishi_add=view.findViewById(R.id.xiaoshoutuihuolishi_add);

        xiaoshoutuihuolishi_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), XiaoshoutuihuolishiActivity.class);
                startActivity(intent);
            }
        });


        xiaoshoutuihuolishi_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), AddXiaoshoutuihuodanActivity.class);
                startActivity(intent);
            }
        });



        View xiaoshoudingdan_item=view.findViewById(R.id.xiaoshoudingdan_item);
        View xiaoshoudingdan_add=view.findViewById(R.id.xiaoshoudingdan_add);

        xiaoshoudingdan_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), XiaoshoudingdanlishiActivity.class);
                startActivity(intent);
            }
        });


        xiaoshoudingdan_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), AddXiaoshoudingdanActivity.class);
                startActivity(intent);
            }
        });


        View kucunpandian_item=view.findViewById(R.id.kucunpandian_item);
        View kucunpandian_add=view.findViewById(R.id.kucunpandian_add);

        kucunpandian_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), PandianlishiActivity.class);
                startActivity(intent);
            }
        });

        kucunpandian_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), AddpandianActivity.class);
                startActivity(intent);
            }
        });



        View diaobodan_item=view.findViewById(R.id.diaobodan_item);
        View diaobodan_add=view.findViewById(R.id.diaobodan_add);

        diaobodan_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), DiaobolishiActivity.class);
                startActivity(intent);
            }
        });

        diaobodan_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), AddDiaobodanActivity.class);
                startActivity(intent);
            }
        });

        View kucunchaxun_item=view.findViewById(R.id.kucunchaxun_item);

        kucunchaxun_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), KucunchaxunActivity.class);
                startActivity(intent);
            }
        });



        return  view;
    }
}

