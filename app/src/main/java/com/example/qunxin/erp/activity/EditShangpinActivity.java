package com.example.qunxin.erp.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.qunxin.erp.R;
import com.example.qunxin.erp.UserBaseDatus;

import java.util.HashMap;
import java.util.Map;

public class EditShangpinActivity extends AppCompatActivity implements View.OnClickListener {


    View fenlei;
    View unit;
    View guigeshuxing;
    View backBtn;
    View saveBtn;


    TextView fenleiText;
    TextView unitText;
    TextView guigeshuxingText;
    TextView priceText;
    TextView remarkText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_shangpin);
        initView();
    }


    void initView(){
        fenlei=findViewById(R.id.edit_shangpin_fenlei);
        unit=findViewById(R.id.edit_shangpin_unit);
        guigeshuxing=findViewById(R.id.edit_shangpin_guigeshuxing);
        saveBtn=findViewById(R.id.edit_shangpin_save);
        backBtn=findViewById(R.id.edit_shangpin_back);

        fenleiText=findViewById(R.id.edit_shangpin_typeName);
        unitText=findViewById(R.id.edit_shangpin_unitText);
        guigeshuxingText=findViewById(R.id.edit_shangpin_guigeshuxingText);
        priceText=findViewById(R.id.edit_shangpin_priceText);
        remarkText=findViewById(R.id.edit_shangpin_remarksText);



        saveBtn.setOnClickListener(this);
        backBtn.setOnClickListener(this);
        fenlei.setOnClickListener(this);
        unit.setOnClickListener(this);
        guigeshuxing.setOnClickListener(this);


        loadModelAndLoadView();

    }

    String fenleiStr;
    String guigeshuxingStr;
    String unitStr;
    String priceStr;
    String remarksStr;
    String type;
    String normsStr;
    String propertyStr;
    String supplierTypeStr;
    String supplierStr;
    String proNameStr;
    String barCodeStr;
    String idStr;
    void loadModelAndLoadView(){

        Intent intent=getIntent();
        fenleiStr=intent.getStringExtra("fenlei");
        guigeshuxingStr=intent.getStringExtra("norms")+" "+intent.getStringExtra("property");
        unitStr=intent.getStringExtra("unit");
        priceStr=intent.getStringExtra("price");
        remarksStr=intent.getStringExtra("remarks");
        type=intent.getStringExtra("type");
        normsStr=intent.getStringExtra("norms");
        propertyStr=intent.getStringExtra("property");
        supplierTypeStr=intent.getStringExtra("supplierType");
        supplierStr=intent.getStringExtra("supplier");
        proNameStr=intent.getStringExtra("proName");
        barCodeStr=intent.getStringExtra("barCode");
        idStr=intent.getStringExtra("id");



        fenleiText.setText(fenleiStr);
        guigeshuxingText.setText(guigeshuxingStr);
        unitText.setText(unitStr);
        priceText.setText(priceStr);
        remarkText.setText(remarksStr);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.edit_shangpin_fenlei:
                Intent intent1=new Intent(EditShangpinActivity.this,EditShangpinTypeActivity.class);
                intent1.putExtra("type",type);
                startActivityForResult(intent1,1);
                break;
            case R.id.edit_shangpin_unit:
                Intent intent2=new Intent(EditShangpinActivity.this,UnitActivity.class);
                startActivityForResult(intent2,1);
                break;
            case R.id.edit_shangpin_guigeshuxing:
                Intent intent3=new Intent(EditShangpinActivity.this,GuigeshuxingActivity.class);
                startActivityForResult(intent3,1);
                break;
            case R.id.edit_shangpin_save:
                save();
                break;
            case R.id.edit_shangpin_back:
                finish();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case 1:
                if(resultCode==1){
                    fenleiStr=data.getStringExtra("typeName");
                    fenleiText.setText(fenleiStr);
                }
                if(resultCode==2){
                    unitStr=data.getStringExtra("unit");
                    unitText.setText(unitStr);
                }
                if(resultCode==3){
                    guigeshuxingStr=data.getStringExtra("guigeshuxing");
                    guigeshuxingText.setText(guigeshuxingStr);
                }

        }

    }

    void save(){
       new Thread(new Runnable() {
           @Override
           public void run() {
               saveChangeShangpin();
           }
       }).start();
    }

    void saveChangeShangpin(){
        String signa=UserBaseDatus.getInstance().getSign();
        Map<String, String> jsonMap = new HashMap<>();
        priceStr=priceText.getText().toString();
        unitStr=unitText.getText().toString();
        remarksStr=remarkText.getText().toString();

         normsStr="";
         propertyStr="";
        try{
            normsStr=guigeshuxingText.getText().toString().split(" ")[0];
            propertyStr=guigeshuxingText.getText().toString().split(" ")[1];
        }catch (ArrayIndexOutOfBoundsException e){

        }
        if(normsStr.length()==0||propertyStr.length()==0){
            Toast.makeText(this, "请完善商品资料信息", Toast.LENGTH_SHORT).show();
            return;
        }

        jsonMap.put("barCode", barCodeStr);
        jsonMap.put("updateBy", "1");
        jsonMap.put("norms",normsStr);
        jsonMap.put("property",propertyStr);
        jsonMap.put("price",priceStr);
        jsonMap.put("proName",proNameStr);
        jsonMap.put("signa",signa);
        jsonMap.put("supplierType",supplierTypeStr);
        jsonMap.put("supplier",supplierStr);
        jsonMap.put("type",type);
        jsonMap.put("unit",unitStr);
        jsonMap.put("remarks",remarksStr);
        jsonMap.put("id",idStr);



        final String jsonString = JSON.toJSONString(jsonMap);

        final String contentType = "application/json";
        final String url=UserBaseDatus.getInstance().url+"api/app/proProduct/edit";

        Map map=UserBaseDatus.getInstance().isSuccessPost(url, jsonString, contentType);
        if((boolean)(map.get("isSuccess"))){
            Intent intent=new Intent();
            intent.putExtra("fenlei",fenleiStr);

            intent.putExtra("unit",unitStr);
            intent.putExtra("price",priceStr);
            intent.putExtra("remarks",remarksStr);
            setResult(1,intent);
            finish();
        }
    }
}
