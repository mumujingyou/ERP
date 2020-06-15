package com.example.qunxin.erp.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qunxin.erp.R;

public class AddjinhuoActivity extends AppCompatActivity implements View.OnClickListener {

    private View backbtn;
    private TextView gongyingshangText;
    private TextView cangkuText;
    private TextView shangpin;







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addjinhuo);

        initView();
    }

    void initView(){
        backbtn=findViewById(R.id.add_jinhuo_back);
        gongyingshangText=findViewById(R.id.add_jinhuo_gongyingshang);
        cangkuText=findViewById(R.id.add_jinhuo_cangku);
        shangpin=findViewById(R.id.add_jinhuo_shangpin);



        backbtn.setOnClickListener(this);
        gongyingshangText.setOnClickListener(this);
        cangkuText.setOnClickListener(this);
        shangpin.setOnClickListener(this);
        backbtn.setOnClickListener(this);




    }




    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.add_jinhuo_back:
                finish();
                break;
            case R.id.add_jinhuo_gongyingshang:
                Intent intent=new Intent(AddjinhuoActivity.this,XuanzegongyingshangActivity.class);
                startActivityForResult(intent,1);
                break;
            case R.id.add_jinhuo_cangku:
                Intent intent1=new Intent(AddjinhuoActivity.this,XuanzeCangkuActivity.class);
                startActivityForResult(intent1,1);
                break;
            case R.id.add_jinhuo_shangpin:

                if("".equals(supplier)){
                    Toast.makeText(AddjinhuoActivity.this, "请选择供应商", Toast.LENGTH_SHORT).show();
                    return;
                }
                if("".equals(depot)){
                    Toast.makeText(AddjinhuoActivity.this, "请选择供仓库", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent2=new Intent(AddjinhuoActivity.this,AddJinhuodanXuanzeshangpinActivity.class);
                intent2.putExtra("supplier",supplier);
                intent2.putExtra("depot",depot);
                intent2.putExtra("supplierName",gongyingshangText.getText().toString());
                intent2.putExtra("depotName",cangkuText.getText().toString());
                startActivity(intent2);
                finish();
                break;
        }
    }

    String supplier="";
    String depot="";
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 1:
                if(resultCode==6){
                    String s=data.getStringExtra("gongyingshangName");
                    gongyingshangText.setText(s);
                    supplier=data.getStringExtra("gongyingshangNameID");
                    String type=data.getStringExtra("type");
                    Log.d("type", type);
                }

                if(resultCode==1){
                    String s=data.getStringExtra("name");
                    cangkuText.setText(s);
                    depot=data.getStringExtra("id");
                    Log.d("onActivityResult", depot);
                }
        }
    }
}
