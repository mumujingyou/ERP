package com.example.qunxin.erp.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.alibaba.fastjson.JSON;
import com.example.qunxin.erp.BaseActivity;
import com.example.qunxin.erp.R;
import com.example.qunxin.erp.UserBaseDatus;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by qunxin on 2019/8/16.
 */

public class XuanzegongyingshangActivity extends BaseActivity {

    View backBtn;
    RadioGroup radioGroup;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xuanzegongyingshang);
        initView();

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                loadModel();
            }
        }).start();
    }


    void initView(){
        backBtn=findViewById(R.id.xuanzegongyinshang_back);
        radioGroup=findViewById(R.id.xuanzegongyingshang_radioGroup);

    }
    //http://119.23.219.127:8094/api/suppliers/list

    List<String> typeNames=new ArrayList<>();
    List<String> ids=new ArrayList<>();
    List<String> types=new ArrayList<>();

    void loadModel(){
        final String contentTypeList = "application/json";
        final String url=UserBaseDatus.getInstance().url+"api/suppliers/list";
        Map<String, String> jsonMap = new HashMap<>();
        jsonMap.put("pageNum", "1");
        jsonMap.put("pageSize", "100");
        jsonMap.put("signa", UserBaseDatus.getInstance().getSign());
        String jsonString = JSON.toJSONString(jsonMap);

        Map map=UserBaseDatus.getInstance().isSuccessPost(url, jsonString, contentTypeList);
        if((boolean)(map.get("isSuccess"))){
            Log.d("isSuccess", "loadModel: 供应商成功");
            JSONObject json=(JSONObject) map.get("json");
            try {
                JSONObject jsonData=json.getJSONObject("data");
                JSONArray jsonArray=jsonData.getJSONArray("rows");
                for (int i=0;i<jsonArray.length();i++){
                    JSONObject jsonObject=jsonArray.getJSONObject(i);
                    String typeName=jsonObject.getString("supName");
                    typeNames.add(typeName);

                    String id=jsonObject.getString("id");
                    ids.add(id);

                    String type=jsonObject.getString("type");
                    types.add(type);
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        loadRadioGroup();
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    void loadRadioGroup() {
        for (int i = 0; i < typeNames.size(); i++) {
            RadioButton radioButton = new RadioButton(this);
            ViewGroup.LayoutParams layoutParams = new RadioGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 100);
            ((RadioGroup.LayoutParams) layoutParams).setMargins(0, 10, 0, 0);
            radioButton.setLayoutParams(layoutParams);
            radioButton.setTextSize(20);
            radioButton.setTextColor(Color.parseColor("#b4b4b4"));
            radioButton.setBackgroundColor(Color.parseColor("#ffffff"));

            //设置文字
            radioButton.setText(typeNames.get(i));
            final int finalI = i;
            //设置radioButton的点击事件
            radioButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent();
                    String typeName=typeNames.get(finalI);
                    intent.putExtra("gongyingshangName", typeName);
                    String id=ids.get(finalI);
                    intent.putExtra("gongyingshangNameID",id);

                    String type=types.get(finalI);
                    intent.putExtra("type",type);
                    setResult(6,intent);
                    finish();
                }
            });
            //将radioButton添加到radioGroup中
            radioGroup.addView(radioButton);
        }
    }




}
