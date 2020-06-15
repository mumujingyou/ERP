package com.example.qunxin.erp.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qunxin.erp.R;
import com.example.qunxin.erp.UserBaseDatus;

public class AddXiaoshoudingdanActivity extends AppCompatActivity {

    View backBtn;
    TextView danhaoText;
    TextView kehuText;
    View shangpinText;
    TextView depotNameText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_xiaoshoudingdan);

        initView();
        getBianhao();
    }

    void initView(){
        danhaoText=findViewById(R.id.danhao);
        kehuText=findViewById(R.id.kehu);
        shangpinText=findViewById(R.id.shangpin);
        depotNameText=findViewById(R.id.depotName);

        kehuText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AddXiaoshoudingdanActivity.this,XuanzeKehuActivity.class);
                startActivityForResult(intent,1);
            }
        });

        depotNameText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AddXiaoshoudingdanActivity.this,XuanzeCangkuActivity.class);
                startActivityForResult(intent,1);
            }
        });




        shangpinText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(kehuName.equals("")||depotName.equals("")){
                    Toast.makeText(AddXiaoshoudingdanActivity.this, "客户和仓库不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent=new Intent(AddXiaoshoudingdanActivity.this,AddXiaoshoudingdanXuanzeshangpinActivity.class);
                intent.putExtra("busNo",danhaoText.getText().toString());
                intent.putExtra("kehuName",kehuName);
                intent.putExtra("kehuId",kehuId);
                intent.putExtra("depotName",depotName);
                intent.putExtra("depot",depotId);
                startActivity(intent);
                finish();
            }
        });

        backBtn=findViewById(R.id.back);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    String kehuName="";
    String kehuId="";

    String depotName="";
    String depotId="";
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1){
            if(resultCode==2){
                kehuName=data.getStringExtra("kehu");
                kehuId=data.getStringExtra("id");

                kehuText.setText(kehuName);
            }

            if(resultCode==1){
                depotName=data.getStringExtra("name");
                depotId=data.getStringExtra("id");

                depotNameText.setText(depotName);
            }
        }
    }

    void getBianhao(){
        UserBaseDatus.getInstance().getShangpinbianhao("25",AddXiaoshoudingdanActivity.this,danhaoText);
    }
}
