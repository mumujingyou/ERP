package com.example.qunxin.erp.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.qunxin.erp.R;
import com.example.qunxin.erp.modle.ShangpinBaseDatus;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import swipedelete.utils.CommonAdapter;
import swipedelete.utils.ViewHolder;

public class EditYixuanshangpinActivity extends AppCompatActivity implements View.OnClickListener {


    View backBtn;
    ListView listView;
    Button selectAllBtn;
    View deleteBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_yixuanshangpin);

        initView();
        initModel();
    }

    void initView(){
        backBtn=findViewById(R.id.back);
        listView=findViewById(R.id.listView);
        selectAllBtn=findViewById(R.id.selectAll);
        deleteBtn=findViewById(R.id.btn_delete);

        backBtn.setOnClickListener(this);
        selectAllBtn.setOnClickListener(this);
        deleteBtn.setOnClickListener(this);

    }


    void initModel(){

        Intent intent=getIntent();
        dateList=intent.getStringExtra("dateList");
        jsonParse();
        loadView(lists);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:

                Intent intent=new Intent();
                dateList=jsonToString();
                intent.putExtra("dateList",dateList);

                setResult(2,intent);

                finish();
                break;
            case R.id.selectAll:
                selectAll();
                break;
            case R.id.btn_delete:
                delete();
                break;
        }
    }




    boolean isAll;
    void selectAll(){
        isAll=!isAll;
        if(isAll){
            selectAllBtn.setText("全选");
            for(int i=0;i<lists.size();i++){
                lists.get(i).getCheckBox().setChecked(true);
            }
        }else {
            selectAllBtn.setText("全不选");
            for(int i=0;i<lists.size();i++){
                lists.get(i).getCheckBox().setChecked(false);
            }
        }
    }
    List<ShangpinBaseDatus> datuses;
    void delete(){

        Iterator it = lists.iterator();
        while (it.hasNext()) {
            // 得到对应集合元素
            ShangpinBaseDatus datus = (ShangpinBaseDatus) it.next();
            // 判断
            if (datus.getCheckBox().isChecked()) {
                // 从集合中删除上一次next方法返回的元素
                it.remove();
            }
        }
        loadView(lists);
    }


    BaseAdapter adapter=null;


    void loadView(List<ShangpinBaseDatus> lists){


        adapter=new CommonAdapter<ShangpinBaseDatus>(EditYixuanshangpinActivity.this,lists, R.layout.item_edit_yixuanshangpin) {



            @Override
            public void convert(final ViewHolder holder, final ShangpinBaseDatus shangpinDatus, final int position, View convertView) {
                holder.setText(R.id.name,shangpinDatus.getProName());
                holder.setText(R.id.totalAmount,"￥"+shangpinDatus.getTotalAmount());

                final CheckBox checkBox=holder.getView(R.id.checkbox);

                shangpinDatus.setCheckBox(checkBox);

                holder.setText(R.id.detail,"￥"+shangpinDatus.getPrice()+"*"+shangpinDatus.getTotal()+shangpinDatus.getUnit());

                holder.setOnClickListener(R.id.item, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                       if(shangpinDatus.getCheckBox().isChecked()){
                           shangpinDatus.getCheckBox().setChecked(false);
                       }else {
                           shangpinDatus.getCheckBox().setChecked(true);

                       }

                    }
                });
            }
        };

        listView.setAdapter(adapter);


    }

    String dateList="";
    List<ShangpinBaseDatus> lists=new ArrayList<>();
    void jsonParse(){

        try {
            JSONArray jsonArray=new JSONArray(dateList);
            for (int i=0;i<jsonArray.length();i++){
                JSONObject jsonObject=jsonArray.getJSONObject(i);
                String proName=jsonObject.getString("proName");
                String price=jsonObject.getString("price");
                String proId=jsonObject.getString("proId");
                String norms=jsonObject.getString("norms");
                String property=jsonObject.getString("property");
                String remarks=jsonObject.getString("remarks");
                String total=jsonObject.getString("total");
                String totalAmount=jsonObject.getString("totalAmount");
                String unit=jsonObject.getString("unit");


                ShangpinBaseDatus datus = new ShangpinBaseDatus();
                datus.setPrice(Float.parseFloat(price));
                datus.setProName(proName);
                datus.setProId(proId);
                datus.setNorms(norms);
                datus.setProperty(property);
                datus.setRemarks(remarks);
                datus.setTotal(Integer.parseInt(total));
                datus.setTotalAmount(Float.parseFloat(totalAmount));
                datus.setUnit(unit);
                datus.setImgRes(R.mipmap.shop_icon);
                lists.add(datus);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    String jsonToString()  {
        StringBuffer buffer=new StringBuffer();

        for (int i=0;i<lists.size();i++){
            JSONObject jsonObject=new JSONObject();
            try {
                if(lists.get(i).getTotal()==0) break;
                jsonObject.put("norms",lists.get(i).getNorms());
                jsonObject.put("property",lists.get(i).getProperty());
                jsonObject.put("price",lists.get(i).getPrice());
                jsonObject.put("proId",lists.get(i).getProId());
                jsonObject.put("proName",lists.get(i).getProName());
                jsonObject.put("remarks",lists.get(i).getRemarks());
                jsonObject.put("total",lists.get(i).getTotal());
                jsonObject.put("totalAmount",lists.get(i).getTotalAmount());
                jsonObject.put("unit",lists.get(i).getUnit());
                jsonObject.put("proNo",lists.get(i).getProNo());

                String content=String.valueOf(jsonObject);
                buffer.append(content).append(",");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        String s= buffer.toString();
        if("".equals(s)){
            return "";
        }
        return "["+s.substring(0,s.length()-1)+"]";
    }
}
