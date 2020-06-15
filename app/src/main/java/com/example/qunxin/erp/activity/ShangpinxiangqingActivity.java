package com.example.qunxin.erp.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.qunxin.erp.R;
import com.example.qunxin.erp.UserBaseDatus;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class ShangpinxiangqingActivity extends AppCompatActivity implements View.OnClickListener {


    TextView nameText;
    TextView bianhaoText;
    TextView fenleiText;
    TextView unitText;
    TextView tiaoxingmaText;
    TextView priceText;
    TextView remarkText;

    View backBtn;
    View editBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shangpinxiangqing);

        initView();
    }

    void initView(){
        nameText=findViewById(R.id.shangpinxiangqing_name);
        bianhaoText=findViewById(R.id.shangpinxiangqing_bianhao);
        fenleiText=findViewById(R.id.shangpinxiangqing_fenlei);
        unitText=findViewById(R.id.shangpinxiangqing_unit);
        tiaoxingmaText=findViewById(R.id.shangpinxiangqing_tiaoxingma);
        priceText=findViewById(R.id.shangpinxiangqing_price);
        remarkText=findViewById(R.id.shangpinxiangqing_remark);


        backBtn=findViewById(R.id.shangpinxiangqing_back);
        editBtn=findViewById(R.id.shangpinxiangqing_edit);

        backBtn.setOnClickListener(this);
        editBtn.setOnClickListener(this);

        Intent intent=getIntent();
        id=intent.getStringExtra("id");

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    loadModle();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();


    }
    String id;
    String type;
    String fenlei;
    String unit;
    String price;
    String remarks;
    String norms;
    String property;
    String supplierType;
    String supplier;
    String proName;
    String barCode;
    void loadModle() throws JSONException {
        final String contentType = "application/x-www-form-urlencoded";
        final String url = UserBaseDatus.getInstance().url + "api/app/proProduct/getProProductById";
        final String data = "signa=" + UserBaseDatus.getInstance().getSign() + "&&id=" + id;
        Map map = UserBaseDatus.getInstance().isSuccessPost(url, data, contentType);
        if ((boolean) (map.get("isSuccess"))) {
           JSONObject jsonObject=(JSONObject) map.get("json");

            JSONObject json=jsonObject.getJSONObject("data");
            type=json.getString("type");


           String bianhao=json.getString("proNo");
            proName=json.getString("proName");
            fenlei=getProTypeById(type);
            unit=json.getString("unit");
            barCode=json.getString("barCode");
            price=json.getString("price");
            remarks=json.getString("remarks");
            norms=json.getString("norms");
            property=json.getString("property");
            supplierType=json.getString("supplierType");
            supplier=json.getString("supplier");
            loadView(bianhao,proName,fenlei,unit,barCode,price,remarks);
        }

    }


    void loadView(final String bianhao,final String proName,final String fenlei,final String unit,final String barCode,final String price, final String remarks){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                bianhaoText.setText(bianhao);
                nameText.setText(proName);
                fenleiText.setText(fenlei);
                unitText.setText(unit);
                tiaoxingmaText.setText(barCode);
                priceText.setText(price);
                remarkText.setText(remarks);
            }
        });
    }


//根据id获取商品类型
    String getProTypeById(String type) throws JSONException {
        final String contentType = "application/x-www-form-urlencoded";
        final String url = UserBaseDatus.getInstance().url + "api/app/proType/getProTypeById";
        final String data = "signa=" + UserBaseDatus.getInstance().getSign() + "&&id=" + type;
        Map map = UserBaseDatus.getInstance().isSuccessPost(url, data, contentType);
        if ((boolean) (map.get("isSuccess"))) {
            JSONObject jsonObject=(JSONObject) map.get("json");

            JSONObject json=jsonObject.getJSONObject("data");


            String typeName=json.getString("typeName");
            return typeName;

        }
        return "";
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.shangpinxiangqing_back:
                Intent intent=new Intent();
                intent.putExtra("price",price);
                setResult(1,intent);
                finish();
                break;
            case R.id.shangpinxiangqing_edit:
                goToEditShangpinActivity();
                break;
        }
    }

    void goToEditShangpinActivity(){
        Intent intent=new Intent(ShangpinxiangqingActivity.this,EditShangpinActivity.class);

        intent.putExtra("id",id);
        intent.putExtra("type",type);
        intent.putExtra("fenlei",fenlei);
        intent.putExtra("unit",unit);
        intent.putExtra("price",price);
        intent.putExtra("remarks",remarks);
        intent.putExtra("norms",norms);
        intent.putExtra("property",property);
        intent.putExtra("supplier",supplier);
        intent.putExtra("supplierType",supplierType);
        intent.putExtra("proName",proName);
        intent.putExtra("barCode",barCode);


        startActivityForResult(intent,1);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case 1:
                if(resultCode==1){
                    fenlei=data.getStringExtra("fenlei");
                    fenleiText.setText(fenlei);
                    unit=data.getStringExtra("unit");
                    unitText.setText(unit);
                    price=data.getStringExtra("price");
                    priceText.setText(price);
                    remarks=data.getStringExtra("remarks");
                    remarkText.setText(remarks);
                }

        }

    }
}
