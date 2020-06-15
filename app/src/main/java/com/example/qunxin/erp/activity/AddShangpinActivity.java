package com.example.qunxin.erp.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.qunxin.erp.R;
import com.example.qunxin.erp.UserBaseDatus;
import com.example.qunxin.erp.util.Constant;
import com.google.zxing.activity.CaptureActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AddShangpinActivity extends AppCompatActivity implements View.OnClickListener {

    View xuanzefenlei;
    View gongyingshangfeilei;
    View guigeshuxing;
    View kucunyujing;
    View unit;
    View backBtn;
    View saveBtn;
    View tiaoxingmaBtn;
    View gongyingshangName;

    TextView typeNameText;
    TextView unitText;
    TextView guigeshuxingText;
    TextView kucunyujingText;
    TextView tiaoxingmaText;
    TextView gongyingshangTypeText;
    TextView shangpinbianhaoText;
    TextView priceText;
    TextView nameText;
    TextView remarkText;
    TextView gongyingshangNameText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_shangpin);
        initView();

    }

    void initView(){
         xuanzefenlei=findViewById(R.id.add_shangpin_fenlei);
        gongyingshangfeilei=findViewById(R.id.add_shangpin_gongyingshangfenlei);
        backBtn=findViewById(R.id.add_shangpin_back);
        saveBtn=findViewById(R.id.add_shangpin_save);
        unit=findViewById(R.id.add_shangpin_unit);
        guigeshuxing=findViewById(R.id.add_shangpin_guigeshuxing);
        kucunyujing=findViewById(R.id.add_shangpin_kucunyujing);
        tiaoxingmaBtn=findViewById(R.id.add_shangpin_tiaoxingma);
        gongyingshangName=findViewById(R.id.add_shangpin_gongyingshangName);

        typeNameText=findViewById(R.id.add_shangpin_typeName);
        unitText=findViewById(R.id.add_shangpin_unitText);
        guigeshuxingText=findViewById(R.id.add_shangpin_guigeshuxingText);
        kucunyujingText=findViewById(R.id.add_shangpin_kucunyujingText);
        tiaoxingmaText=findViewById(R.id.add_shangpin_tiaoxingmaText);
        gongyingshangTypeText =findViewById(R.id.add_shangpin_gongyingshangTypeText);
        shangpinbianhaoText=findViewById(R.id.add_shangpin_shangpinbianhao);
        priceText=findViewById(R.id.add_shangpin_price);
        nameText=findViewById(R.id.add_shangpin_name);
        remarkText=findViewById(R.id.remarksText);
        gongyingshangNameText =findViewById(R.id.add_shangpin_gongyingshangNameText);

        xuanzefenlei.setOnClickListener(this);
        gongyingshangfeilei.setOnClickListener(this);
        backBtn.setOnClickListener(this);
        unit.setOnClickListener(this);
        guigeshuxing.setOnClickListener(this);
        kucunyujing.setOnClickListener(this);
        tiaoxingmaBtn.setOnClickListener(this);
        saveBtn.setOnClickListener(this);
        gongyingshangName.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.add_shangpin_tiaoxingma:
                startQrCode();
                break;
            case R.id.add_shangpin_fenlei:
                Intent intent=new Intent(AddShangpinActivity.this,XuanzefengleiActivity.class);

                startActivityForResult(intent,1);
                break;
            case R.id.add_shangpin_gongyingshangfenlei:
                Intent intent1=new Intent(AddShangpinActivity.this,XuanzegongyingshangfengleiActivity.class);
                startActivityForResult(intent1,1);
                break;
            case R.id.add_shangpin_unit:
                Intent intent2=new Intent(AddShangpinActivity.this,UnitActivity.class);
                startActivityForResult(intent2,1);
                break;
            case R.id.add_shangpin_guigeshuxing:
                Intent intent3=new Intent(AddShangpinActivity.this,GuigeshuxingActivity.class);
                startActivityForResult(intent3,1);
                break;
            case R.id.add_shangpin_kucunyujing:
                Intent intent4=new Intent(AddShangpinActivity.this,KucunyujingActivity.class);
                startActivityForResult(intent4,1);
                break;
            case R.id.add_shangpin_back:
                finish();
                break;
            case R.id.add_shangpin_save:
                saveShangpin();
                break;
            case R.id.add_shangpin_gongyingshangName:
                Intent intent6=new Intent(AddShangpinActivity.this,XuanzegongyingshangActivity.class);
                startActivityForResult(intent6,1);
                break;
        }
    }

    String id;
    String gongyingshangNameID;
    String gongyingshgnID;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 1:
                if(resultCode==1){
                    String s=data.getStringExtra("typeName");
                    id=data.getStringExtra("id");
                    Log.d("id", id);
                    typeNameText.setText(s);
                }
                if(resultCode==2){
                    String s=data.getStringExtra("unit");
                    unitText.setText(s);
                }
                if(resultCode==3){
                    String s=data.getStringExtra("guigeshuxing");
                    guigeshuxingText.setText(s);
                }
                if(resultCode==4){
                    String s=data.getStringExtra("number");
                    kucunyujingText.setText(s);
                }
                if(resultCode==5){
                    String s=data.getStringExtra("gongyingshangYypeName");
                    gongyingshangTypeText.setText(s);
                    gongyingshgnID=data.getStringExtra("gongyingshangID");
                }

                if(resultCode==6){
                    String s=data.getStringExtra("gongyingshangName");
                    gongyingshangNameID=data.getStringExtra("gongyingshangNameID");
                    gongyingshangNameText.setText(s);
                }
        }
        if (requestCode == Constant.REQ_QR_CODE && resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            String scanResult = bundle.getString(Constant.INTENT_EXTRA_KEY_QR_SCAN);
            //将扫描出的信息显示出来
            tiaoxingmaText.setText(scanResult);
            UserBaseDatus.getInstance().getShangpinbianhao("5",AddShangpinActivity.this,shangpinbianhaoText);
        }
    }





    // 开始扫码
    private void startQrCode() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            // 申请权限
            ActivityCompat.requestPermissions(AddShangpinActivity.this, new String[]{Manifest.permission.CAMERA}, Constant.REQ_PERM_CAMERA);
            return;
        }
        // 二维码扫码
        Intent intent = new Intent(AddShangpinActivity.this, CaptureActivity.class);
        startActivityForResult(intent, Constant.REQ_QR_CODE);
    }



    //申请访问权限回调结果
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case Constant.REQ_PERM_CAMERA:
                // 摄像头权限申请
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 获得授权
                    startQrCode();
                } else {
                    // 被禁止授权
                    Toast.makeText(AddShangpinActivity.this, "请至权限中心打开本应用的相机访问权限", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    /*{
  "barCode": "955652326544",
          "createBy": "1",
          "norms": "002",
          "price": 100,
          "proName": "PP",
          "property": "塑胶",
          "remarks": "sdsdsdsd",
          "signa": "baa1a4b81b03a95652d4c19542da8b940ab5200bdf727741fceadc4741854b6e",
          "supplier": "d858012ced4b4cc7a686ecd9b54fd3fb",
          "supplierType": "097a8e3438a5468cac371914a5499286",
          "type": "65e96790744c4f7585b17b4a9c3b69ad",
          "unit": "斤"
}

*/
    String data="";
    void saveShangpin(){
        String barCode=tiaoxingmaText.getText().toString();
        String createBy=UserBaseDatus.getInstance().userId;

        String price=priceText.getText().toString();
        String proName=nameText.getText().toString();
        String signa=UserBaseDatus.getInstance().getSign();
        String supplierType= gongyingshgnID;
        String supplier=gongyingshangNameID;
        String type=id;
        String unit=unitText.getText().toString();
        String remarks=remarkText.getText().toString();
        String norms="";
        String property="";
        try{
             norms=guigeshuxingText.getText().toString().split(" ")[0];
             property=guigeshuxingText.getText().toString().split(" ")[1];
        }catch (ArrayIndexOutOfBoundsException e){

        }
        if(barCode.length()==0||norms.length()==0||property.length()==0||price.length()==0||proName.length()==0||signa.length()==0||
                supplierType.length()==0||supplier.length()==0||unit.length()==0||remarks.length()==0||type.length()==0){
            Toast.makeText(this, "请完善商品资料信息", Toast.LENGTH_SHORT).show();
            return;
        }
        Map <String, String> jsonMap = new HashMap<>();
        jsonMap.put("barCode", barCode);
        jsonMap.put("createBy", createBy);
        jsonMap.put("norms",norms);
        jsonMap.put("property",property);
        jsonMap.put("price",price);
        jsonMap.put("proName",proName);
        jsonMap.put("signa",signa);
        jsonMap.put("supplierType",supplierType);
        jsonMap.put("supplier",supplier);
        jsonMap.put("type",type);
        jsonMap.put("unit",unit);
        jsonMap.put("remarks",remarks);



        final String jsonString = JSON.toJSONString(jsonMap);

        //http://119.23.219.127:8094/api/app/proProduct/add

        final String contentTypeList = "application/json";
        final String url="http://119.23.219.127:8094/api/app/proProduct/add";

        new Thread(new Runnable() {
            @Override
            public void run() {
                Map map=UserBaseDatus.getInstance().isSuccessPost(url, jsonString, contentTypeList);
                if((boolean)(map.get("isSuccess"))){
                    Intent intent=new Intent(AddShangpinActivity.this,AddShangpinSuccessActivity.class);
                    startActivity(intent);
                    finish();
                }else {
                    JSONObject jsonObject= (JSONObject) map.get("json");
                    try {
                        data =jsonObject.getString("data");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(AddShangpinActivity.this, data, Toast.LENGTH_SHORT).show();
                            data="";
                        }
                    });
                }
            }
        }).start();

    }



}
