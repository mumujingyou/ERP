package com.example.qunxin.erp.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qunxin.erp.R;

public class AddDiaobodanActivity extends AppCompatActivity implements View.OnClickListener {

    View backBtn;
    TextView refundDepotNameText;
    TextView buyDepotNameText;
    TextView shangpinText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_diaobodan);

        initView();
    }

    void initView(){
        backBtn=findViewById(R.id.back);
        refundDepotNameText=findViewById(R.id.refundDepotName);
        buyDepotNameText=findViewById(R.id.buyDepotName);
        shangpinText=findViewById(R.id.shangpin);

        backBtn.setOnClickListener(this);
        refundDepotNameText.setOnClickListener(this);
        buyDepotNameText.setOnClickListener(this);
        shangpinText.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.refundDepotName:
                Intent intent=new Intent(AddDiaobodanActivity.this,XuanzeCangkuActivity.class);
                startActivityForResult(intent,1);
                break;
            case R.id.buyDepotName:
                Intent inten2=new Intent(AddDiaobodanActivity.this,XuanzeCangkuActivity.class);
                startActivityForResult(inten2,2);
                break;
            case R.id.shangpin:

                if(refundDepotName.equals("")||buyDepotName.equals("")){
                    Toast.makeText(this, "出库仓库和入库仓库不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(refundDepotName.equals(buyDepotName)){
                    Toast.makeText(this, "出库仓库和入库仓库不能相同", Toast.LENGTH_SHORT).show();
                    return;
                }


                Intent inten3=new Intent(AddDiaobodanActivity.this,AdddiaoboxuanzeshangpinActivity.class);
                inten3.putExtra("refundDepotName",refundDepotName);
                inten3.putExtra("refundDepot",refundDepot);
                inten3.putExtra("buyDepot",buyDepot);
                inten3.putExtra("buyDepotName",buyDepotName);

                startActivity(inten3);
                finish();
                break;
        }
    }

    String refundDepotName="";
    String refundDepot="";
    String buyDepotName="";
    String buyDepot="";
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 1:
                if(resultCode==1){
                    refundDepotName=data.getStringExtra("name");
                    refundDepot=data.getStringExtra("id");
                    refundDepotNameText.setText(refundDepotName);
                }
                break;

            case 2:
                if(resultCode==1){
                    buyDepotName=data.getStringExtra("name");
                    buyDepot=data.getStringExtra("id");
                    buyDepotNameText.setText(buyDepotName);
                }
                break;
        }

    }
}
