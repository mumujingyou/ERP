package com.example.qunxin.erp.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qunxin.erp.R;
import com.example.qunxin.erp.UserBaseDatus;

public class AddXiaoshoutuihuodanActivity extends AppCompatActivity implements View.OnClickListener {

    LinearLayout linearLayout;
    Switch switchBtn;
    LinearLayout root_LinerLayout;
    TextView busNoText;
    TextView kehuNameText;
    TextView depotNameText;
    TextView addText;

    View addShangpinBtn;
    View backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_xiaoshoutuihuodan);

        initView();
    }

    void initView(){
        linearLayout=findViewById(R.id.linerLayout);
        switchBtn=findViewById(R.id.switchBtn);
        root_LinerLayout=findViewById(R.id.root_LinerLayout);
        busNoText=findViewById(R.id.busNo);
        kehuNameText=findViewById(R.id.kehuName);
        depotNameText=findViewById(R.id.depotName);
        addShangpinBtn=findViewById(R.id.add_shangpin);
        backBtn=findViewById(R.id.back);
        addText=findViewById(R.id.addText);


        kehuNameText.setOnClickListener(this);
        depotNameText.setOnClickListener(this);
        backBtn.setOnClickListener(this);
        addShangpinBtn.setOnClickListener(this);


        switchBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    root_LinerLayout.removeView(linearLayout);
                    addText.setText("添加销售单");
                }else {
                    root_LinerLayout.addView(linearLayout,2);
                    addText.setText("添加商品");

                }
            }
        });


        getBusNo();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.kehuName:
                Intent intent=new Intent(AddXiaoshoutuihuodanActivity.this,XuanzeKehuActivity.class);
                startActivityForResult(intent,1);
                break;
            case R.id.depotName:
                Intent intent1=new Intent(AddXiaoshoutuihuodanActivity.this,XuanzeCangkuActivity.class);
                startActivityForResult(intent1,1);
                break;
            case R.id.add_shangpin:
                if(switchBtn.isChecked()){
                    Intent intent2=new Intent(AddXiaoshoutuihuodanActivity.this,XuanzexiaoshoudanActivity.class);
                    startActivity(intent2);
                    finish();

                }else {

                    if("".equals(depot)||"".equals(kehu)){
                        Toast.makeText(AddXiaoshoutuihuodanActivity.this, "请选择供应商和仓库", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    Intent intent2=new Intent(AddXiaoshoutuihuodanActivity.this,AddXiaoshoutuihuoxuznzeshangpinActivity.class);
                    intent2.putExtra("depotName",depotNameText.getText().toString());
                    intent2.putExtra("depot",depot);
                    intent2.putExtra("custName",kehuNameText.getText().toString());
                    intent2.putExtra("cust",kehu);
                    intent2.putExtra("busNo",busNoText.getText().toString());

                    startActivity(intent2);
                    finish();
                }

                break;
            case R.id.back:
                finish();
                break;
        }
    }

    String depot="";
    String kehu="";

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case 1:
                if(resultCode==2){

                    kehu=data.getStringExtra("id");
                    kehuNameText.setText(data.getStringExtra("kehu"));
                }


                if(resultCode==1){
                    depot=data.getStringExtra("id");
                    depotNameText.setText(data.getStringExtra("name"));
                }
                break;

        }
    }


    void getBusNo(){
        UserBaseDatus.getInstance().getShangpinbianhao("14",AddXiaoshoutuihuodanActivity.this,busNoText);
    }
}
