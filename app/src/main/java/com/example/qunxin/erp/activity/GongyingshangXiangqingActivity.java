package com.example.qunxin.erp.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.qunxin.erp.R;
import com.example.qunxin.erp.UserBaseDatus;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class GongyingshangXiangqingActivity extends AppCompatActivity implements View.OnClickListener {

    TextView supNameText;
    TextView lianxirenText;
    TextView lianxidianhuaText;
    TextView bianhaoText;
    TextView youxiangText;
    TextView supTypeText;
    TextView lianxidizhiText;
    TextView fuzerenText;
   // TextView qichuqinakuanText;
    TextView remarksText;
    TextView chuanzhenText;


    View backBtn;
    View editBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gongyingshang_xiangqing);

        initView();
    }

    String id;

    void initView(){
        supNameText =findViewById(R.id.gongyingshangxiangqing_name);
        lianxirenText=findViewById(R.id.gongyingshangxiangqing_lianxiren);
        lianxidianhuaText=findViewById(R.id.gongyingshangxiangqing_lianxidianhua);
        bianhaoText=findViewById(R.id.gongyingshangxiangqing_bianhao);
        youxiangText=findViewById(R.id.gongyingshangxiangqing_youxiang);
        supTypeText =findViewById(R.id.gongyingshangxiangqing_fenlei);
        lianxidizhiText=findViewById(R.id.gongyingshangxiangqing_lianxidizhi);
        fuzerenText=findViewById(R.id.gongyingshangxiangqing_fuzeren);
        //qichuqinakuanText=findViewById(R.id.gongyingshangxiangqing_chuqiqiankuan);
        remarksText=findViewById(R.id.gongyingshangxiangqing_remarksText);
        chuanzhenText=findViewById(R.id.gongyingshangxiangqing_chuanzhen);

        backBtn=findViewById(R.id.gongyingshangxiangqing_back);
        editBtn=findViewById(R.id.gongyingshangxiangqing_edit);

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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.gongyingshangxiangqing_back:
                finish();
                break;
            case R.id.gongyingshangxiangqing_edit:
                Intent intent=new Intent(GongyingshangXiangqingActivity.this,EditGongyingshangActivity.class);
                intent.putExtra("supName",supName);
                intent.putExtra("lianxiren",lianxiren);
                intent.putExtra("lianxidianhua",lianxidianhua);
                intent.putExtra("bianhao",bianhao);
                intent.putExtra("youxiang",youxiang);
                intent.putExtra("lianxidizhi",lianxidizhi);
                intent.putExtra("fuzeren",fuzeren);
                intent.putExtra("qichuqiankuan",qichuqiankuan);
                intent.putExtra("remarks",remarks);
                intent.putExtra("id",id);
                intent.putExtra("supType",supType);


                startActivity(intent);
                break;
        }
    }


    String supName;
    String lianxiren;
    String lianxidianhua;
    String lianxidizhi;
    String bianhao;
    String remarks;
    String youxiang;
    String supType;
    String qichuqiankuan;
    String fuzeren;
    String chuanzhen;
    void loadModle() throws JSONException {


        final String contentType = "application/x-www-form-urlencoded";
        final String url = UserBaseDatus.getInstance().url + "api/suppliers/getSupplierById";
        final String data = "signa=" + UserBaseDatus.getInstance().getSign() + "&&id=" + id;
        Map map = UserBaseDatus.getInstance().isSuccessPost(url, data, contentType);
        if ((boolean) (map.get("isSuccess"))) {
            JSONObject jsonObject=(JSONObject) map.get("json");

            JSONObject json=jsonObject.getJSONObject("data");

            supName=json.getString("supName");
            lianxiren=json.getString("contactPeople");
            lianxidianhua=json.getString("phone");
            lianxidizhi=json.getString("localtion");
            remarks=json.getString("remarks");
            bianhao=json.getString("supNo");
            youxiang=json.getString("email");
            chuanzhen=json.getString("fax");
            qichuqiankuan="0￥";
            fuzeren=json.getString("qxLeader");
            supType=json.getString("supTypeName");
            loadView(supName,lianxiren,lianxidianhua,lianxidizhi,remarks,bianhao,youxiang,supType,qichuqiankuan,fuzeren,chuanzhen);
        }
    }

    private void loadView(final String supName, final String lianxiren, final String lianxidianhua, final String lianxidizhi, final String remarks,
                          final String bianhao, final String youxiang, final String supType, final String qichuqiankuan, final String fuzeren,
                          final String chuanzhen) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                supNameText.setText(supName);
                lianxirenText.setText(lianxiren);
                lianxidianhuaText.setText(lianxidianhua);
                lianxidizhiText.setText(lianxidizhi);
                remarksText.setText(remarks);
                bianhaoText.setText(bianhao);
                youxiangText.setText(youxiang);
                supTypeText.setText(supType);
                //qichuqinakuanText.setText(qichuqiankuan);
                fuzerenText.setText(fuzeren);
                chuanzhenText.setText(chuanzhen);

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
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
}
