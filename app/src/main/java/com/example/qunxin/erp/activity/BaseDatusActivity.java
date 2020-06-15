package com.example.qunxin.erp.activity;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.qunxin.erp.BaseActivity;
import com.example.qunxin.erp.R;

public class BaseDatusActivity extends BaseActivity implements View.OnClickListener {

    View backBtn;
    View shangpinliebiao_item;
    View shangpinliebiao_add;

    View jiesuanzhanghu_item;
    View jiesuanzhanghu_add;

    View cangkuliebiao_item;
    View cangkuliebiao_add;

    //View qichuxinxi_item;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_basedatus);
        initView();

    }

    void initView(){
        backBtn=findViewById(R.id.baseDatus_back);

        ViewGroup shangpinliebiao=findViewById(R.id.shangpinliebiao);
        View shangpinliebiao_body= LayoutInflater.from(BaseDatusActivity.this).
                inflate(R.layout.basedatus_shangpinliebiao,shangpinliebiao,true);
        shangpinliebiao_item=shangpinliebiao_body.findViewById(R.id.shangpinliebiao_item);
        shangpinliebiao_add=shangpinliebiao_body.findViewById(R.id.shangpinliebiao_add);

        ViewGroup jiesuanzhanghu=findViewById(R.id.jiesuanzhanghu);
        View jiesuanzhanghu_body= LayoutInflater.from(BaseDatusActivity.this).
                inflate(R.layout.basedatus_jiesuanzhanghu,jiesuanzhanghu,true);
        jiesuanzhanghu_item=jiesuanzhanghu_body.findViewById(R.id.jiesuanzhanghu_item);
        jiesuanzhanghu_add=jiesuanzhanghu_body.findViewById(R.id.jiesuanzhanghu_add);


        ViewGroup cangkuliebiao=findViewById(R.id.cangkuliebiao);
        View cangkuliebiao_body= LayoutInflater.from(BaseDatusActivity.this).
                inflate(R.layout.basedatus_cangkuliebiao,cangkuliebiao,true);
        cangkuliebiao_item=cangkuliebiao_body.findViewById(R.id.cangkuliebiao_item);
        cangkuliebiao_add=cangkuliebiao_body.findViewById(R.id.cangkuliebiao_add);


       // ViewGroup qichuxinxi=findViewById(R.id.qichuxinxi);
//        View qichuxinxi_body= LayoutInflater.from(BaseDatusActivity.this).
//                inflate(R.layout.basedatus_cangkuliebiao,qichuxinxi,true);
        //qichuxinxi_item=qichuxinxi_body.findViewById(R.id.qichuxinxi_item);
        //cangkuliebiao_add=cangkuliebiao_body.findViewById(R.id.cangkuliebiao_add);







        backBtn.setOnClickListener(this);
        shangpinliebiao_item.setOnClickListener(this);
        shangpinliebiao_add.setOnClickListener(this);
        jiesuanzhanghu_add.setOnClickListener(this);
        jiesuanzhanghu_item.setOnClickListener(this);
        cangkuliebiao_item.setOnClickListener(this);
        cangkuliebiao_add.setOnClickListener(this);
        //qichuxinxi_item.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.baseDatus_back:
                finish();
                break;
            case R.id.shangpinliebiao_item:

                Intent intent=new Intent(BaseDatusActivity.this,ShangpinliebiaoActivity.class);
                startActivity(intent);
                break;
            case R.id.shangpinliebiao_add:

                Intent intent1=new Intent(BaseDatusActivity.this,AddShangpinActivity.class);
                startActivity(intent1);
                break;

            case R.id.jiesuanzhanghu_item:

                Intent intent2=new Intent(BaseDatusActivity.this,JiesuanzhanghuActivity.class);
                startActivity(intent2);
                break;
            case R.id.jiesuanzhanghu_add:

                Intent intent3=new Intent(BaseDatusActivity.this,AddZhanghuActivity.class);
                startActivity(intent3);
                break;

            case R.id.cangkuliebiao_item:

                Intent intent4=new Intent(BaseDatusActivity.this,CangkuliebiaoActivity.class);
                startActivity(intent4);
                break;
            case R.id.cangkuliebiao_add:

                Intent intent5=new Intent(BaseDatusActivity.this,AddCangkuActivity.class);
                startActivity(intent5);
                break;
            case R.id.qichuxinxi_item:

                Intent intent6=new Intent(BaseDatusActivity.this,QichuInfoActivity.class);
                startActivity(intent6);
                break;
        }
    }


}
