package com.example.qunxin.erp.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.qunxin.erp.R;
import com.example.qunxin.erp.UserBaseDatus;
import com.example.qunxin.erp.modle.ShangpinBaseDatus;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import swipedelete.utils.CommonAdapter;
import swipedelete.utils.ViewHolder;

public class PandianCangkuXuanzeActivity extends AppCompatActivity {

    ListView listView;
    View backBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pandian_cangku_xuanze);

        initView();
    }

    void initView(){
        listView=findViewById(R.id.listView);
        backBtn=findViewById(R.id.back);

        new Thread(new Runnable() {
            @Override
            public void run() {
                loadModel();
            }
        }).start();

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });
    }

    boolean isFinish=false;
    void back(){
        for(int i=0;i<lists.size();i++){
            CangkuClass cangkuClass=lists.get(i);

            if(cangkuClass.checkBox.isChecked()&&cangkuClass.status.equals("1")){
                Intent intent=new Intent();
                intent.putExtra("depotName",cangkuClass.name);
                intent.putExtra("depot",cangkuClass.id);
                setResult(1,intent);
                isFinish=true;
                finish();
            }
        }
        if(isFinish==false){
            Toast.makeText(this, "请选择锁定的仓库", Toast.LENGTH_SHORT).show();
        }

    }


    class CangkuClass{
        String name="";
        String id="";
        String status="";
        CheckBox checkBox;
    }

    List<CangkuClass> lists=new ArrayList<>();
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

                    CangkuClass cangku=new CangkuClass();
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

    List<CheckBox> checkBoxes=new ArrayList<>();
    private void loadView() {
        listView.setAdapter(new CommonAdapter<CangkuClass>(PandianCangkuXuanzeActivity.this,lists, R.layout.item_pandiancangkuxuanze) {


            @Override
            public void convert(final ViewHolder holder, final CangkuClass cangkuClass, final int position, View convertView) {
                holder.setText(R.id.name,cangkuClass.name);
                TextView statusText=holder.getView(R.id.status);
                if(cangkuClass.status.equals("1")){
                    statusText.setText("已锁定");
                    statusText.setBackgroundColor(Color.parseColor("#F37C4D"));
                }

                final CheckBox checkBox=holder.getView(R.id.checkbox);
                View contentView=holder.getView(R.id.content);
                cangkuClass.checkBox=checkBox;
                contentView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        for(int i=0;i<lists.size();i++){
                            if(i==position){
                                lists.get(i).checkBox.setChecked(true);
                            }else {
                                lists.get(i).checkBox.setChecked(false);
                            }
                        }

                        dialogShow(cangkuClass.id,cangkuClass.status);

                    }
                });
            }
        });

    }



    private void dialogShow(final String id, final String status) {
        final Dialog dialog = new Dialog(this,R.style.ActionSheetDialogStyle);
        //填充对话框的布局
        LinearLayout inflate =(LinearLayout) LayoutInflater.from(this).inflate(R.layout.xuanzecanglu_dialog, null);
        //初始化控件
        View btn_sure = inflate.findViewById(R.id.sure);
        View btn_cancel = inflate.findViewById(R.id.cancel);



        //将布局设置给Dialog
        dialog.setContentView(inflate);

        btn_sure.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if(status.equals("1")) return;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        changeDepotStatus(id);

                    }
                }).start();
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if(status.equals("1")){
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            changeDepotStatus(id);

                        }
                    }).start();
                }
                dialog.dismiss();
            }
        });
        dialog.show();//显示对话框
    }


    void changeDepotStatus(String id){
        final String contentTypeList = "application/x-www-form-urlencoded";
        final String url= UserBaseDatus.getInstance().url+"api/app/stockCheck/update";

        final String jsonString = "signa="+UserBaseDatus.getInstance().getSign()+"&&id="+id;
        Log.d("jsonString", jsonString);
        Map map=UserBaseDatus.getInstance().isSuccessPost(url, jsonString, contentTypeList);
        if((boolean)(map.get("isSuccess"))){

            JSONObject json=(JSONObject) map.get("json");
            try {
               String data= json.getString("data");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            lists.clear();
            loadModel();
        }
    }

}
