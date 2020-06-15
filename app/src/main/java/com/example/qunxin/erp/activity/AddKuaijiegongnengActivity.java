package com.example.qunxin.erp.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewParent;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qunxin.erp.R;
import com.example.qunxin.erp.fragment.IndexFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class AddKuaijiegongnengActivity extends AppCompatActivity implements View.OnClickListener {


    View backBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_kuaijiegongneng);

        initView();
    }



    List<CheckBox> lists=new ArrayList<>();
    void initView(){
        backBtn=findViewById(R.id.back);
        backBtn.setOnClickListener(this);


        CheckBox checkBox0=findViewById(R.id.checkbox0);
        CheckBox checkBox1=findViewById(R.id.checkbox1);
        CheckBox checkBox2=findViewById(R.id.checkbox2);
        CheckBox checkBox3=findViewById(R.id.checkbox3);
        CheckBox checkBox4=findViewById(R.id.checkbox4);
        CheckBox checkBox5=findViewById(R.id.checkbox5);
        CheckBox checkBox6=findViewById(R.id.checkbox6);
        CheckBox checkBox7=findViewById(R.id.checkbox7);
        CheckBox checkBox8=findViewById(R.id.checkbox8);
        CheckBox checkBox9=findViewById(R.id.checkbox9);
        CheckBox checkBox10=findViewById(R.id.checkbox10);
        CheckBox checkBox11=findViewById(R.id.checkbox11);
        CheckBox checkBox12=findViewById(R.id.checkbox12);
        CheckBox checkBox13=findViewById(R.id.checkbox13);
        CheckBox checkBox14=findViewById(R.id.checkbox14);
        //CheckBox checkBox15=findViewById(R.id.checkbox15);


        lists.add(checkBox0);
        lists.add(checkBox1);
        lists.add(checkBox2);
        lists.add(checkBox3);
        lists.add(checkBox4);
        lists.add(checkBox5);
        lists.add(checkBox6);
        lists.add(checkBox7);
        lists.add(checkBox8);
        lists.add(checkBox9);
        lists.add(checkBox10);
        lists.add(checkBox11);
        lists.add(checkBox12);
        lists.add(checkBox13);
        lists.add(checkBox14);
        //lists.add(checkBox15);



        loadModel();
    }





    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:

                if( saveTxt()==false){
                    Toast.makeText(this, "至少需要一个快捷功能", Toast.LENGTH_SHORT).show();
                    return;
                }
                finish();
                break;
        }
    }





    String back(){
        StringBuffer buffer=new StringBuffer();
        for (int i=0;i<lists.size();i++){
            CheckBox checkBox=lists.get(i);

            if(checkBox.isChecked()){
                TextView nameText=null;
                TextView infoText=null;
                View viewParent=(View)checkBox.getParent();

                switch (i){
                    case 0:
                        nameText=viewParent.findViewById(R.id.listview_tv1);
                        infoText=viewParent.findViewById(R.id.listview_tvInfo1);
                        break;
                    case 1:
                        nameText=viewParent.findViewById(R.id.listview_tv2);
                        infoText=viewParent.findViewById(R.id.listview_tvInfo2);
                        break;
                    case 2:
                        nameText=viewParent.findViewById(R.id.listview_tv3);
                        infoText=viewParent.findViewById(R.id.listview_tvInfo3);
                        break;
                    case 3:
                        nameText=viewParent.findViewById(R.id.listview_tv4);
                        infoText=viewParent.findViewById(R.id.listview_tvInfo4);
                        break;
                    case 4:
                        nameText=viewParent.findViewById(R.id.listview_tv5);
                        infoText=viewParent.findViewById(R.id.listview_tvInfo5);
                        break;
                    case 5:
                        nameText=viewParent.findViewById(R.id.listview_tv6);
                        infoText=viewParent.findViewById(R.id.listview_tvInfo6);
                        break;
                    case 6:
                        nameText=viewParent.findViewById(R.id.listview_tv7);
                        infoText=viewParent.findViewById(R.id.listview_tvInfo7);
                        break;
                    case 7:
                        nameText=viewParent.findViewById(R.id.listview_tv8);
                        infoText=viewParent.findViewById(R.id.listview_tvInfo8);
                        break;
                    case 8:
                        nameText=viewParent.findViewById(R.id.listview_tv9);
                        infoText=viewParent.findViewById(R.id.listview_tvInfo9);
                        break;
                    case 9:
                        nameText=viewParent.findViewById(R.id.listview_tv10);
                        infoText=viewParent.findViewById(R.id.listview_tvInfo10);
                        break;
                    case 10:
                        nameText=viewParent.findViewById(R.id.listview_tv11);
                        infoText=viewParent.findViewById(R.id.listview_tvInfo11);
                        break;
                    case 11:
                        nameText=viewParent.findViewById(R.id.listview_tv12);
                        infoText=viewParent.findViewById(R.id.listview_tvInfo12);
                        break;
                    case 12:
                        nameText=viewParent.findViewById(R.id.listview_tv13);
                        infoText=viewParent.findViewById(R.id.listview_tvInfo13);
                        break;
                    case 13:
                        nameText=viewParent.findViewById(R.id.listview_tv14);
                        infoText=viewParent.findViewById(R.id.listview_tvInfo14);
                        break;
                    case 14:
                        nameText=viewParent.findViewById(R.id.listview_tv15);
                        infoText=viewParent.findViewById(R.id.listview_tvInfo15);
                        break;

                }


                JSONObject jsonObject=new JSONObject();
                try {
                    jsonObject.put("name",nameText.getText().toString());
                    jsonObject.put("info",infoText.getText().toString());
                    jsonObject.put("image",i);
                    String jsonStr=jsonObject.toString();
                    buffer.append(jsonStr).append(",");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        if(buffer.toString().equals("")) {
            return "";
        }

        return "["+buffer.toString().substring(0,buffer.toString().length()-1)+"]";

    }



    boolean saveTxt(){

        String saveInfo=back();

        if(saveInfo.equals("")){
          return false;
        }
        Log.d("saveInfo", saveInfo);
        FileOutputStream fos;

        try {

            fos=openFileOutput("data.txt",MODE_PRIVATE);//把文件输出到data中

            fos.write(saveInfo.getBytes());//将我们写入的字符串变成字符数组）

            fos.close();

            return true;

        } catch (Exception e) {

            e.printStackTrace();
            return false;
        }
    }


    String readTxt(){
        String content="";

        try {

            FileInputStream fis=openFileInput("data.txt");

            byte [] buffer=new  byte[fis.available()];

            fis.read(buffer);

            content=new String(buffer);

            fis.close();

        } catch (Exception e) {

            e.printStackTrace();

        }
        return content;
    }


    List<Integer> images=new ArrayList<>();
    String dataStr="";
    void loadModel(){
        dataStr=readTxt();
        if("".equals(dataStr)){
            return;
        }
        try {
            JSONArray jsonArray=new JSONArray(dataStr);
            for (int i=0;i<jsonArray.length();i++){
                JSONObject jsonObject=jsonArray.getJSONObject(i);
                int image=jsonObject.getInt("image");
                images.add(image);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        loadView();

    }

    void loadView(){
        for(int i=0;i<images.size();i++){
            int j=images.get(i);
            lists.get(j).setChecked(true);
        }
    }










}
