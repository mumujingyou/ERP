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

public class AddJinhuotuihuodanActivity extends AppCompatActivity implements View.OnClickListener {


    LinearLayout linearLayout;
    Switch switchBtn;
    LinearLayout root_LinerLayout;
    TextView busNoText;
    TextView supplierNameText;
    TextView depotNameText;
    TextView addText;

    View addShangpinBtn;
    View backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_jinhuotuihuodan);

        initView();
    }

    void initView(){
        linearLayout=findViewById(R.id.linerLayout);
        switchBtn=findViewById(R.id.switchBtn);
        root_LinerLayout=findViewById(R.id.root_LinerLayout);
        busNoText=findViewById(R.id.busNo);
        supplierNameText=findViewById(R.id.supplierName);
        depotNameText=findViewById(R.id.depotName);
        addShangpinBtn=findViewById(R.id.add_shangpin);
        backBtn=findViewById(R.id.back);
        addText=findViewById(R.id.addText);


        supplierNameText.setOnClickListener(this);
        depotNameText.setOnClickListener(this);
        backBtn.setOnClickListener(this);
        addShangpinBtn.setOnClickListener(this);


        switchBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    root_LinerLayout.removeView(linearLayout);
                    addText.setText("添加进货单");
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
            case R.id.supplierName:
                Intent intent=new Intent(AddJinhuotuihuodanActivity.this,XuanzegongyingshangActivity.class);
                startActivityForResult(intent,1);
                break;
            case R.id.depotName:
                Intent intent1=new Intent(AddJinhuotuihuodanActivity.this,XuanzeCangkuActivity.class);
                startActivityForResult(intent1,1);
                break;
            case R.id.add_shangpin:
                if(switchBtn.isChecked()){
                    Intent intent2=new Intent(AddJinhuotuihuodanActivity.this,XuanzejinhuodanActivity.class);
                    startActivity(intent2);
                    finish();
                }else {

                    if("".equals(depot)){
                        Toast.makeText(AddJinhuotuihuodanActivity.this, "请选择供仓库", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if("".equals(supplier)){
                        Toast.makeText(AddJinhuotuihuodanActivity.this, "请选择供应商", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Intent intent2=new Intent(AddJinhuotuihuodanActivity.this,AddJinhuotuihuoXuanzeshangpinActivity.class);
                    intent2.putExtra("depotName",depotNameText.getText().toString());
                    intent2.putExtra("depot",depot);
                    intent2.putExtra("supplierName",supplierNameText.getText().toString());
                    intent2.putExtra("supplier",supplier);
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
    String supplier="";

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case 1:
                if(resultCode==6){
                    supplier=data.getStringExtra("gongyingshangNameID");
                    supplierNameText.setText(data.getStringExtra("gongyingshangName"));
                }


                if(resultCode==1){
                    depot=data.getStringExtra("id");
                    depotNameText.setText(data.getStringExtra("name"));
                }
                break;

        }
    }


    void getBusNo(){
        UserBaseDatus.getInstance().getShangpinbianhao("2",AddJinhuotuihuodanActivity.this,busNoText);
    }
}
