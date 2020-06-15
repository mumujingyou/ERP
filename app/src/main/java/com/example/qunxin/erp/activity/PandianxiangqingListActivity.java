package com.example.qunxin.erp.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.example.qunxin.erp.R;
import com.example.qunxin.erp.UserBaseDatus;
import com.example.qunxin.erp.modle.JinhuoBaseDatus;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import swipedelete.utils.CommonAdapter;
import swipedelete.utils.ViewHolder;

public class PandianxiangqingListActivity extends AppCompatActivity implements View.OnClickListener {

    View backBtn;
    ListView listView;
    View gantanhao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pandianxiangqing);

        initView();
    }

    String id = "";
    String status="";

    void initView() {
        backBtn = findViewById(R.id.back);
        gantanhao = findViewById(R.id.gantanhao);
        listView=findViewById(R.id.listView);

        backBtn.setOnClickListener(this);
        gantanhao.setOnClickListener(this);


        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        status=intent.getStringExtra("status");

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    loadModel();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.gantanhao:
                Intent intent = new Intent(PandianxiangqingListActivity.this, PandiandanXiangqingActivity.class);
                intent.putExtra("depotName",depotName);
                intent.putExtra("busNo",busNo);
                intent.putExtra("leader",leader);
                intent.putExtra("busTime",busTime);
                intent.putExtra("createTime",createTime);
                intent.putExtra("createBy",createBy);
                intent.putExtra("remarks",remarks);

                startActivity(intent);
                break;
        }
    }


    List<JinhuoBaseDatus> lists = new ArrayList<>();
    String depotName="";
    String busNo="";
    String leader="";
    String createBy="";
    String createTime="";
    String remarks="";
    String busTime="";



    void loadModel() throws JSONException {
        final String contentType = "application/x-www-form-urlencoded";
        final String url = UserBaseDatus.getInstance().url + "api/app/stockCheck/getAppStockCheckDetail";
        final String data = "signa=" + UserBaseDatus.getInstance().getSign() + "&&stockCheckId=" + id;
        Map map = UserBaseDatus.getInstance().isSuccessPost(url, data, contentType);
        if ((boolean) (map.get("isSuccess"))) {

            JSONObject json = (JSONObject) map.get("json");
            try {
                JSONObject jsonData = json.getJSONObject("data");

                JSONObject jsonObject1=jsonData.getJSONObject("stockCheck");

                createBy=jsonObject1.getString("createBy");
                busNo=jsonObject1.getString("stockCheckNo");
                busTime=jsonObject1.getString("busTime");
                depotName=jsonObject1.getString("depot");
                leader=jsonObject1.getString("leader");
                remarks=jsonObject1.getString("remarks");
                createTime=jsonObject1.getString("createTime");

                JSONArray jsonArray = jsonData.getJSONArray("stockCheckDetailList");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);


                    String proName = jsonObject.getString("proName");

                    int bookTotal = jsonObject.getInt("bookTotal");

                    int actualTotal = jsonObject.getInt("actualTotal");

                    int moreTotal = jsonObject.getInt("moreTotal");

                    int lessTotal=jsonObject.getInt("lessTotal");


                    String proNo=jsonObject.getString("proNo");

                    String norms=jsonObject.getString("norms");

                    String property=jsonObject.getString("property");

                    String remarks=jsonObject.getString("remarks");



                    JinhuoBaseDatus datus = new JinhuoBaseDatus();
                    datus.setProName(proName);
                    datus.setBookTotal(bookTotal);
                    datus.setMoreTotal(moreTotal);
                    datus.setActualTotal(actualTotal);
                    datus.setProNo(proNo);
                    datus.setProperty(property);
                    datus.setLessTotal(lessTotal);
                    datus.setRemarks(remarks);
                    datus.setNorms(norms);
                    lists.add(datus);
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        loadView();
                    }
                });

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void loadView() {
        listView.setAdapter(new CommonAdapter<JinhuoBaseDatus>(PandianxiangqingListActivity.this,lists, R.layout.item_pandianxiangqing) {

            @Override
            public void convert(final ViewHolder holder, JinhuoBaseDatus jinhuo, final int position, View convertView) {

                if(jinhuo==null) return;
                holder.setText(R.id.name, jinhuo.getProName());
                holder.setText(R.id.zhangmianCount, jinhuo.getBookTotal()+"");
                holder.setText(R.id.shijiCount, jinhuo.getActualTotal()+"");


                TextView statusText=holder.getView(R.id.status);
                /** 0无盈亏 1.有盈亏3盘盈4.盘亏 */
                if(status.equals("0")){
                    statusText.setText("无盈亏");
                }else if(status.equals("1")){
                    statusText.setText("有盈亏");
                }else if(status.equals("3")){
                    statusText.setText("盘盈");
                }else if(status.equals("4")){
                    statusText.setText("盘亏");
                }

                holder.setOnClickListener(R.id.ll_content, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Toast.makeText(ShangpinliebiaoActivity.this,"点击了："+position, Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(PandianxiangqingListActivity.this,PandianshangpinActivity.class);
                        JinhuoBaseDatus datus=lists.get(position);

                        intent.putExtra("proName",datus.getProName());
                        intent.putExtra("proNo",datus.getProNo());
                        intent.putExtra("norms",datus.getNorms());
                        intent.putExtra("property",datus.getProperty());
                        intent.putExtra("bookTotal",datus.getBookTotal());
                        intent.putExtra("actualTotal",datus.getActualTotal());
                        intent.putExtra("lessTotal",datus.getLessTotal());
                        intent.putExtra("moreTotal",datus.getMoreTotal());
                        intent.putExtra("remarks",datus.getRemarks());


                        startActivity(intent);
                    }
                });



            }
        });

    }

}
