package com.example.qunxin.erp.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qunxin.erp.R;

public class AddpandianActivity extends AppCompatActivity {

    TextView cangkuText;
    View shangpinText;
    View backBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addpandian);

        initView();
    }

    void initView(){
        cangkuText=findViewById(R.id.cangku);
        shangpinText=findViewById(R.id.shangpin);
        backBtn=findViewById(R.id.back);

        cangkuText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AddpandianActivity.this,PandianCangkuXuanzeActivity.class);
                startActivityForResult(intent,1);

            }
        });


        shangpinText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(depot.equals("")){
                    Toast.makeText(AddpandianActivity.this, "仓库不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent=new Intent(AddpandianActivity.this,AddPandianxuanzeshangpinActivity.class);
                intent.putExtra("depotName",depotName);
                intent.putExtra("depot",depot);

                startActivity(intent);
                finish();

            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }
    String depotName="";
    String depot="";
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1){
            if(resultCode==1){
                 depotName=data.getStringExtra("depotName");
                 depot=data.getStringExtra("depot");
                cangkuText.setText(depotName);
            }
        }
    }
}
