package com.example.qunxin.erp.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.alibaba.fastjson.JSON;
import com.example.qunxin.erp.R;
import com.example.qunxin.erp.UserBaseDatus;
import com.hb.dialog.myDialog.MyAlertInputDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class XuanzegongyingshangfengleiActivity extends AppCompatActivity implements View.OnClickListener {
    View add;
    View backBtn;
    RadioGroup radioGroup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xuanzegongyingshangfenglei);
        initView();

        new Thread(new Runnable() {
            @Override
            public void run() {
                loadModel();
            }
        }).start();
    }

    void initView(){
         add=findViewById(R.id.xuanzegongyingshangfenlei_add);
        backBtn=findViewById(R.id.xuanzegongyingshangfenlei_back);
       radioGroup=findViewById(R.id.xuanzegongyingshangfenlei_rg);

        backBtn.setOnClickListener(this);
        add.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.xuanzegongyingshangfenlei_add:{
                final MyAlertInputDialog myAlertInputDialog = new MyAlertInputDialog(this).builder()
                        .setTitle("新增分类")
                        .setEditText("请输入新增分类名称");
                final EditText contentText=myAlertInputDialog.getContentEditText();
                myAlertInputDialog.setPositiveButton("确认", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        enter(contentText);
                        myAlertInputDialog.dismiss();
                    }
                }).setNegativeButton("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        myAlertInputDialog.dismiss();
                    }
                });
                myAlertInputDialog.show();

                break;
            }
            case R.id.xuanzegongyingshangfenlei_back:
                finish();
                break;
        }
    }


    Map map=new HashMap();
    List<String> typeNames=new ArrayList<>();
    List<String> ids=new ArrayList<>();

    //点击确认建
    void enter(EditText contentText) {
        //http://119.23.219.127:8094/api/supplierTypes/add?userId=1&signa=170cbeda998c4a360128074fab8dccb5782c79bfed2f06680299d52f3aa288d9&typeName=234wr

        final String typeName = contentText.getText().toString();
        new Thread(new Runnable() {
            @Override
            public void run() {
                final String strUrl = UserBaseDatus.getInstance().url+"api/supplierTypes/add";
                final String strData = "userId=" + UserBaseDatus.getInstance().userId + "&&signa=" + UserBaseDatus.getInstance().getSign() + "&&typeName=" + typeName;
                final String contentType = "application/x-www-form-urlencoded";
                map=UserBaseDatus.getInstance().isSuccessPost(strUrl, strData, contentType);
                if ((boolean)(map.get("isSuccess"))) {
                    addSingleRadioButtonToRadioGroupLoadModel();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            addSingleRadioButtonToRadioGroup(typeName);

                        }
                    });
                }
            }
        }).start();

    }



    //加载数据
    void loadModel(){
        final String contentTypeList = "application/json";
        final String url=UserBaseDatus.getInstance().url+"api/supplierTypes/list";
        Map <String, String> jsonMap = new HashMap <>();
        jsonMap.put("pageNum", "1");
        jsonMap.put("pageSize", "10");
        jsonMap.put("signa",UserBaseDatus.getInstance().getSign());
        String jsonString = JSON.toJSONString(jsonMap);

        map=UserBaseDatus.getInstance().isSuccessPost(url, jsonString, contentTypeList);
        if((boolean)(map.get("isSuccess"))){
            JSONObject json=(JSONObject) map.get("json");
            try {
                JSONObject jsonData=json.getJSONObject("data");
                JSONArray jsonArray=jsonData.getJSONArray("rows");
                for (int i=0;i<jsonArray.length();i++){
                    JSONObject jsonObject=jsonArray.getJSONObject(i);
                    String typeName=jsonObject.getString("typeName");
                    typeNames.add(typeName);
                    String id=jsonObject.getString("id");
                    ids.add(id);
                    Log.d("typeNames", typeNames.get(i));
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

    //加载视图
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
                    intent.putExtra("gongyingshangYypeName", typeName);
                    String id=ids.get(finalI);
                    intent.putExtra("gongyingshangID",id);
                    setResult(5,intent);

                    finish();
                }
            });
            //将radioButton添加到radioGroup中
            radioGroup.addView(radioButton);
        }
    }


    void addSingleRadioButtonToRadioGroup(String string){
        RadioButton radioButton = new RadioButton(this);
        ViewGroup.LayoutParams layoutParams = new RadioGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 100);
        ((RadioGroup.LayoutParams) layoutParams).setMargins(0, 10, 0, 0);
        radioButton.setLayoutParams(layoutParams);

        radioButton.setTextSize(20);
        radioButton.setTextColor(Color.parseColor("#b4b4b4"));
        radioButton.setBackgroundColor(Color.parseColor("#ffffff"));

        radioButton.setText(string);

        final String stringData=string;
        radioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();

                String typeName=stringData;

                intent.putExtra("gongyingshangYypeName", typeNames.get(0));
                intent.putExtra("gongyingshangID",ids.get(0));
                setResult(5,intent);
                finish();
            }
        });
        radioGroup.addView(radioButton,0);

    }

    void addSingleRadioButtonToRadioGroupLoadModel(){
        final String contentTypeList = "application/json";
        final String url=UserBaseDatus.getInstance().url+"api/supplierTypes/list";
        Map <String, String> jsonMap = new HashMap <>();
        jsonMap.put("pageNum", "1");
        jsonMap.put("pageSize", "10");
        jsonMap.put("signa",UserBaseDatus.getInstance().getSign());
        String jsonString = JSON.toJSONString(jsonMap);

        map=UserBaseDatus.getInstance().isSuccessPost(url, jsonString, contentTypeList);
        if((boolean)(map.get("isSuccess"))){
            JSONObject json=(JSONObject) map.get("json");
            try {
                JSONObject jsonData=json.getJSONObject("data");
                JSONArray jsonArray=jsonData.getJSONArray("rows");

                    JSONObject jsonObject=jsonArray.getJSONObject(0);
                String typeName=jsonObject.getString("typeName");
                typeNames.add(0,typeName);
                String id=jsonObject.getString("id");
                ids.add(0,id);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
