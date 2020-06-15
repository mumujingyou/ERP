package com.example.qunxin.erp.activity;

import android.content.Intent;
import android.content.Loader;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.qunxin.erp.R;
import com.example.qunxin.erp.UserBaseDatus;
import com.hb.dialog.myDialog.MyAlertInputDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class XuanzefengleiActivity extends AppCompatActivity implements View.OnClickListener {
    View add;
    View backBtn;
    RadioGroup radioGroup;

    JSONObject jsonObject;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xuanzefenglei);
        initView();

        new Thread(new Runnable() {
            @Override
            public void run() {
                loadModel();
            }
        }).start();
    }


    void initView() {
        add = findViewById(R.id.xuanzefenlei_add);
        backBtn = findViewById(R.id.xuanzefenlei_back);
        radioGroup = findViewById(R.id.xuanzefenlei_radioGroup);

        backBtn.setOnClickListener(this);
        add.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.xuanzefenlei_add:
                final MyAlertInputDialog myAlertInputDialog = new MyAlertInputDialog(this).builder()
                        .setTitle("新增分类")
                        .setEditText("请输入新增分类名称");
                final EditText contentText = myAlertInputDialog.getContentEditText();
                myAlertInputDialog.setPositiveButton("确认", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Enter(contentText);
                        myAlertInputDialog.dismiss();
                    }
                }).setNegativeButton("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //showMsg("取消");
                        myAlertInputDialog.dismiss();
                    }
                });
                myAlertInputDialog.show();

                break;
            case R.id.xuanzefenlei_back:


                finish();
                break;
        }
    }


    Map map=new HashMap();
    List<String> typeNames=new ArrayList<>();
    List<String> ids=new ArrayList<>();

    //点击确认建
    void Enter(EditText contentText) {
        //http://119.23.219.127:8094/api/app/proType/add?userId=1&signa=6b4d45e1980f12dda10194f818e158fe2aa6b500531afe5d11b052605f6e1181&typeName=fsadfasdfas
        final String typeName = contentText.getText().toString();
        final String strUrl = UserBaseDatus.getInstance().url+"api/app/proType/add";
        final String strData = "userId=" + 1 + "&&signa=" + UserBaseDatus.getInstance().getSign() + "&&typeName=" + typeName;

        new Thread(new Runnable() {
            @Override
            public void run() {
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



    final String contentType = "application/x-www-form-urlencoded";

    //加载数据
    void loadModel(){
        //http://119.23.219.127:8094/api/app/proType/list?signa=170cbeda998c4a360128074fab8dccb5782c79bfed2f06680299d52f3aa288d9;

        final String url=UserBaseDatus.getInstance().url+"api/app/proType/list";
        final String data="signa="+UserBaseDatus.getInstance().getSign();
        map=UserBaseDatus.getInstance().isSuccessPost(url, data, contentType);
        if((boolean)(map.get("isSuccess"))){
            JSONObject json=(JSONObject) map.get("json");
            try {
                JSONArray jsonArray=json.getJSONArray("data");
                for (int i=0;i<jsonArray.length();i++){
                    JSONObject jsonObject=jsonArray.getJSONObject(i);
                    String typeName=jsonObject.getString("typeName");
                    typeNames.add(typeName);

                    String id=jsonObject.getString("id");
                    ids.add(id);
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
                    String id=ids.get(finalI);
                    intent.putExtra("typeName", typeName);
                    intent.putExtra("id",id);
                    setResult(1,intent);
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

        radioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();

                intent.putExtra("typeName", typeNames.get(0));
                intent.putExtra("id", ids.get(0));


                setResult(1,intent);

                Log.d("hahaha", ids.get(0));
                finish();
            }
        });
        radioGroup.addView(radioButton,0);
    }


    void addSingleRadioButtonToRadioGroupLoadModel(){
        final String url=UserBaseDatus.getInstance().url+"api/app/proType/list";
        final String data="signa="+UserBaseDatus.getInstance().getSign();
        map=UserBaseDatus.getInstance().isSuccessPost(url, data, contentType);
        if((boolean)(map.get("isSuccess"))){
            JSONObject json=(JSONObject) map.get("json");
            try {
                JSONArray jsonArray=json.getJSONArray("data");

                JSONObject jsonObject=jsonArray.getJSONObject(0);

                String typeName=jsonObject.getString("typeName");
                String id=jsonObject.getString("id");

                typeNames.add(0,typeName);
                ids.add(0,id);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}