package com.example.qunxin.erp.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.qunxin.erp.BaseActivity;
import com.example.qunxin.erp.R;
import com.example.qunxin.erp.UserBaseDatus;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileOutputStream;
import java.util.Map;


public class LoginActivity extends BaseActivity {

    EditText nameText;
    EditText passWoldText;
    ImageButton loginBtn;

    String username ;
    String password ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();
        loginFunction();

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               new Thread(new Runnable() {
                   @Override
                   public void run() {

                       username = nameText.getText().toString();
                       password = passWoldText.getText().toString();
                       final String strUrl = UserBaseDatus.getInstance().url+"api/app/login/login";
                       final String strData = "username=" + username + "&&password=" + password+"&&signa=" +UserBaseDatus.getInstance().getSign();
                       final String contentType="application/x-www-form-urlencoded";


                       Map map=UserBaseDatus.getInstance().isSuccessPost(strUrl, strData, contentType);
                       if((boolean)(map.get("isSuccess"))){


                           try {
                               JSONObject json=(JSONObject) map.get("json");
                               JSONObject jsonData=json.getJSONObject("data");
                               JSONObject jsonObject= jsonData.getJSONObject("user");
                               UserBaseDatus.getInstance().avatar=jsonObject.getString("avatar");
                               UserBaseDatus.getInstance().userId=jsonObject.getString("userId");

                           } catch (JSONException e) {
                               e.printStackTrace();
                           }
                           SharedPreferences.Editor editor = getSharedPreferences("baseData", Activity.MODE_PRIVATE).edit();
                           editor.putBoolean("login",true);
                           editor.putString("userId",UserBaseDatus.getInstance().userId);
                           editor.commit();

                           finish();
                           Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                           startActivity(intent);
                       }else {
                           runOnUiThread(new Runnable() {
                               @Override
                               public void run() {
                                   Toast.makeText(LoginActivity.this, "账号或密码错误，请重新输入", Toast.LENGTH_SHORT).show();
                               }
                           });
                       }
                   }
               }).start();
            }
        });
    }

    public static final int EXTERNAL_STORAGE_REQ_CODE = 10 ;
    void showQuan(){

        int permission = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // 请求权限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    EXTERNAL_STORAGE_REQ_CODE);
        }
    }

    void initView() {
        loginBtn = findViewById(R.id.loginbtn);
        nameText = findViewById(R.id.name);
        passWoldText = findViewById(R.id.passWold);

        showQuan();
    }



    void loginFunction(){
        SharedPreferences read = getSharedPreferences("baseData", Activity.MODE_PRIVATE);
        boolean isLogin=read.getBoolean("login",false);
        if(isLogin){
            Log.d("tiaozhuan", "loginFunction: 跳转");
            Intent intent=new Intent(LoginActivity.this,MainActivity.class);
            startActivity(intent);
            finish();
        }else {
            saveTxt();
        }
    }


    void saveTxt(){
        String saveInfo="[{\n" +
                "\t\"name\": \"采购历史\",\n" +
                "\t\"info\": \"向供应商预定进货商品\",\n" +
                "\t\"image\": 0\n" +
                "}, {\n" +
                "\t\"name\": \"进货退货历史\",\n" +
                "\t\"info\": \"关联进货单退货或直接退货\",\n" +
                "\t\"image\": 1\n" +
                "}, {\n" +
                "\t\"name\": \"进货历史\",\n" +
                "\t\"info\": \"从供应商采购商品\",\n" +
                "\t\"image\": 2\n" +
                "}, {\n" +
                "\t\"name\": \"入库历史\",\n" +
                "\t\"info\": \"货物入库的历史\",\n" +
                "\t\"image\": 3\n" +
                "}, {\n" +
                "\t\"name\": \"销售订单\",\n" +
                "\t\"info\": \"客户预定销售商品\",\n" +
                "\t\"image\": 4\n" +
                "}, {\n" +
                "\t\"name\": \"商品列表\",\n" +
                "\t\"info\": \"商品名称\\/售价\\/规格\\/库存\\/分类\",\n" +
                "\t\"image\": 12\n" +
                "}, {\n" +
                "\t\"name\": \"结算账户\",\n" +
                "\t\"info\": \"结算账户\",\n" +
                "\t\"image\": 13\n" +
                "}]";
        Log.d("saveInfo", saveInfo);
        FileOutputStream fos;

        try {

            fos=openFileOutput("data.txt",MODE_PRIVATE);//把文件输出到data中

            fos.write(saveInfo.getBytes());//将我们写入的字符串变成字符数组）

            fos.close();

        } catch (Exception e) {

            e.printStackTrace();

        }
    }

}
