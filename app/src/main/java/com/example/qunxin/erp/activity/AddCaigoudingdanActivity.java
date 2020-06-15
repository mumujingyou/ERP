package com.example.qunxin.erp.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qunxin.erp.R;
import com.example.qunxin.erp.UserBaseDatus;

public class AddCaigoudingdanActivity extends AppCompatActivity implements View.OnClickListener {

    TextView danhaoText;
    TextView gongyingshangText;
    TextView shangpin;
    View backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_caigoudingdan);
        initView();

    }

    void initView(){
        danhaoText=findViewById(R.id.add_caigoudingdan_danhao);
        gongyingshangText=findViewById(R.id.add_caigoudingdan_gongyingshang);
        shangpin=findViewById(R.id.add_caigoudingdan_shangpin);
        backBtn=findViewById(R.id.add_caigoudingdan_back);


        getDanHao();
        backBtn.setOnClickListener(this);
        gongyingshangText.setOnClickListener(this);
        shangpin.setOnClickListener(this);
    }

    void getDanHao(){
        UserBaseDatus.getInstance().getShangpinbianhao("22",AddCaigoudingdanActivity.this,danhaoText);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.add_caigoudingdan_gongyingshang:
                Intent intent=new Intent(AddCaigoudingdanActivity.this,XuanzegongyingshangActivity.class);
                startActivityForResult(intent,1);
                break;
            case R.id.add_caigoudingdan_shangpin:
                if(supplier.equals("")){
                    Toast.makeText(AddCaigoudingdanActivity.this, "供应商不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent1=new Intent(AddCaigoudingdanActivity.this,AddCaigoudingdanXuanzeshangpinActivity.class);
                intent1.putExtra("supplier",supplier);
                intent1.putExtra("supplierName",supplierName);
                intent1.putExtra("busNo",danhaoText.getText().toString());
                startActivity(intent1);
                finish();
                break;
            case R.id.add_caigoudingdan_back:
                finish();
                break;
        }
    }



    String supplier="";
    String supplierName="";

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 1:
                if(resultCode==6){
                    supplierName=data.getStringExtra("gongyingshangName");
                    supplier=data.getStringExtra("gongyingshangNameID");
                    gongyingshangText.setText(supplierName);
                }

        }
    }
}
