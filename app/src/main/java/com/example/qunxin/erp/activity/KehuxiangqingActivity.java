package com.example.qunxin.erp.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.qunxin.erp.R;
import com.example.qunxin.erp.UserBaseDatus;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class KehuxiangqingActivity extends AppCompatActivity implements View.OnClickListener {

    TextView kehumingchengText;
    TextView lianxirenText;
    TextView lianxidianhuaText;
    TextView bianhaoText;
    TextView youxiangText;
    TextView chuanzhenText;
    TextView lianxidizhiText;
    TextView fuzerenText;
    //TextView qichuqinakuanText;
    TextView remarksText;


    View backBtn;
    View editBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kehuxiangqing);

        initView();
    }

    String id;

    void initView(){
        kehumingchengText=findViewById(R.id.kehuxiangqing_kehumingcheng);
        lianxirenText=findViewById(R.id.kehuxiangqing_lianxiren);
        lianxidianhuaText=findViewById(R.id.kehuxiangqing_lianxidianhua);
        bianhaoText=findViewById(R.id.kehuxiangqing_bianhao);
        youxiangText=findViewById(R.id.kehuxiangqing_youxiang);
        chuanzhenText=findViewById(R.id.kehuxiangqing_chuanzhen);
        lianxidizhiText=findViewById(R.id.kehuxiangqing_lianxidizhi);
        fuzerenText=findViewById(R.id.kehuxiangqing_fuzeren);
        //qichuqinakuanText=findViewById(R.id.kehuxiangqing_chuqiqiankuan);
        remarksText=findViewById(R.id.remarksText);

        backBtn=findViewById(R.id.kehuxiangqing_back);
        editBtn=findViewById(R.id.kehuxiangqing_edit);

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
            case R.id.kehuxiangqing_back:
                finish();
                break;
            case R.id.kehuxiangqing_edit:
                Intent intent=new Intent(KehuxiangqingActivity.this,EditKehuActivity.class);
                intent.putExtra("kehumingcheng",kehumingcheng);
                intent.putExtra("lianxiren",lianxiren);
                intent.putExtra("lianxidianhua",lianxidianhua);
                intent.putExtra("bianhao",bianhao);
                intent.putExtra("youxiang",youxiang);
                intent.putExtra("lianxidizhi",lianxidizhi);
                intent.putExtra("fuzeren",fuzeren);
                intent.putExtra("qichuqiankuan",qichuqiankuan);
                intent.putExtra("remarks",remarks);
                intent.putExtra("id",id);


                startActivity(intent);
                break;
        }
    }


    String kehumingcheng;
    String lianxiren;
    String lianxidianhua;
    String lianxidizhi;
    String bianhao;
    String remarks;
    String youxiang;
    String chuanzhen;
    String qichuqiankuan;
    String fuzeren;
    void loadModle() throws JSONException {


        final String contentType = "application/x-www-form-urlencoded";
        final String url = UserBaseDatus.getInstance().url + "api/custs/getCustById";
        final String data = "signa=" + UserBaseDatus.getInstance().getSign() + "&&id=" + id;
        Map map = UserBaseDatus.getInstance().isSuccessPost(url, data, contentType);
        if ((boolean) (map.get("isSuccess"))) {
            JSONObject jsonObject=(JSONObject) map.get("json");

            JSONObject json=jsonObject.getJSONObject("data");

            kehumingcheng=json.getString("custName");
            lianxiren=json.getString("contactPeople");
            lianxidianhua=json.getString("phone");
            lianxidizhi=json.getString("localtion");
            remarks=json.getString("remarks");
            bianhao=json.getString("custNo");
            youxiang=json.getString("email");
            chuanzhen=json.getString("fax");
            qichuqiankuan="0￥";
            fuzeren=json.getString("qxLeader");
            loadView(kehumingcheng,lianxiren,lianxidianhua,lianxidizhi,remarks,bianhao,youxiang,chuanzhen,qichuqiankuan,fuzeren);
        }
    }

    private void loadView(final String kehumingcheng, final String lianxiren, final String lianxidianhua, final String lianxidizhi, final String remarks,
                          final String bianhao, final String youxiang, final String chuanzhen, final String qichuqiankuan, final String fuzeren) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                kehumingchengText.setText(kehumingcheng);
                lianxirenText.setText(lianxiren);
                lianxidianhuaText.setText(lianxidianhua);
                lianxidizhiText.setText(lianxidizhi);
                remarksText.setText(remarks);
                bianhaoText.setText(bianhao);
                youxiangText.setText(youxiang);
                chuanzhenText.setText(chuanzhen);
                //qichuqinakuanText.setText(qichuqiankuan);
                fuzerenText.setText(fuzeren);

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
