package com.example.qunxin.erp.activity;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.example.qunxin.erp.R;
import com.example.qunxin.erp.UserBaseDatus;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import swipedelete.utils.CommonAdapter;
import swipedelete.utils.ViewHolder;

public class CangkujiesuoActivity extends AppCompatActivity {

    View backBtn;
    ListView listView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cangkujiesuo);

        initView();
    }

    void initView(){
        backBtn=findViewById(R.id.back);
        listView=findViewById(R.id.listView);

        new Thread(new Runnable() {
            @Override
            public void run() {
                loadModel();
            }
        }).start();

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    class CangkuClass{
        String name="";
        String id="";
        String status="";
    }

    List<CangkujiesuoActivity.CangkuClass> lists=new ArrayList<>();
    void loadModel(){
        final String contentTypeList = "application/json";
        final String url= UserBaseDatus.getInstance().url+"api/app/depot/pageList";
        Map<String, String> jsonMap = new HashMap<>();

        jsonMap.put("pageNum", "1");
        jsonMap.put("pageSize", "10");
        jsonMap.put("signa", UserBaseDatus.getInstance().getSign());
        String jsonString = JSON.toJSONString(jsonMap);

        Map map=UserBaseDatus.getInstance().isSuccessPost(url, jsonString, contentTypeList);
        if((boolean)(map.get("isSuccess"))){

            JSONObject json=(JSONObject) map.get("json");
            try {
                JSONObject jsonData=json.getJSONObject("data");
                JSONArray jsonArray=jsonData.getJSONArray("rows");
                for (int i=0;i<jsonArray.length();i++){
                    JSONObject jsonObject=jsonArray.getJSONObject(i);
                    String name=jsonObject.getString("name");
                    String id=jsonObject.getString("id");
                    String status=jsonObject.getString("status");

                    CangkujiesuoActivity.CangkuClass cangku=new CangkujiesuoActivity.CangkuClass();
                    cangku.id=id;
                    cangku.name=name;
                    cangku.status=status;
                    lists.add(cangku);
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
        listView.setAdapter(new CommonAdapter<CangkujiesuoActivity.CangkuClass>(CangkujiesuoActivity.this,lists, R.layout.item_pandiancangkujiesuo) {


            @Override
            public void convert(final ViewHolder holder, final CangkujiesuoActivity.CangkuClass cangkuClass, final int position, View convertView) {
                holder.setText(R.id.depotName,cangkuClass.name);
                Switch switchBtn=holder.getView(R.id.switchBtn);
                if(cangkuClass.status.equals("1")){
                    switchBtn.setChecked(true);
                }

                switchBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                lock(cangkuClass.id);
                            }
                        }).start();
                    }
                });


            }


        });

    }


    Boolean lock(String id){

        String signa= UserBaseDatus.getInstance().getSign();
        final String stringData = "id="+id+"&&signa="+signa;


        final String contentType = "application/x-www-form-urlencoded";

        final String url=UserBaseDatus.getInstance().url+"api/app/depot/updateStatus";

        Map map=UserBaseDatus.getInstance().isSuccessPost(url, stringData, contentType);
        return  (boolean)(map.get("isSuccess"));
    }


}
