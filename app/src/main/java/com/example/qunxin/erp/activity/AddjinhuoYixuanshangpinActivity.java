package com.example.qunxin.erp.activity;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.qunxin.erp.R;
import com.example.qunxin.erp.UserBaseDatus;
import com.example.qunxin.erp.modle.ShangpinBaseDatus;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import swipedelete.utils.CommonAdapter;
import swipedelete.utils.ViewHolder;

public class AddjinhuoYixuanshangpinActivity extends AppCompatActivity implements View.OnClickListener {


    View backBtn;
    View editBtn;
    TextView cangkuText;
    View shangpinText;
    ListView listView;
    Button finishBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addjinhuo_yixuanshangpin);


        initView();
    }

    String dateList;
    String depot;
    String depotName;
    String supplier;
    String supplierName;
    void initView(){
        backBtn=findViewById(R.id.back);
        editBtn=findViewById(R.id.edit_pen);
        cangkuText=findViewById(R.id.cangku);
        shangpinText=findViewById(R.id.shangpin);
        listView=findViewById(R.id.listview);
        finishBtn=findViewById(R.id.finish);

        backBtn.setOnClickListener(this);
        editBtn.setOnClickListener(this);
        cangkuText.setOnClickListener(this);
        shangpinText.setOnClickListener(this);
        finishBtn.setOnClickListener(this);



        Intent intent=getIntent();
        dateList=intent.getStringExtra("dateList");
        depotName=intent.getStringExtra("depotName");
        depot=intent.getStringExtra("depot");
        supplier=intent.getStringExtra("supplier");
        supplierName=intent.getStringExtra("supplierName");
        cangkuText.setText(depotName);


        jsonParse();

        loadView();

        TextChange();
    }

    List<ShangpinBaseDatus> lists=new ArrayList<>();
    void jsonParse(){

        if("".equals(dateList)){
            lists.clear();
            return;
        }

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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.edit_pen:
                Intent intent1=new Intent(AddjinhuoYixuanshangpinActivity.this,EditYixuanshangpinActivity.class);
                dateList=jsonToString();
                intent1.putExtra("dateList",dateList);
                startActivityForResult(intent1,1);


                break;
            case R.id.cangku:
                show();
                break;
            case R.id.shangpin:
                Intent intent=new Intent(AddjinhuoYixuanshangpinActivity.this,AddJinhuodanXuanzeshangpinActivity.class);

                intent.putExtra("supplier",supplier);
                intent.putExtra("depot",depot);
                intent.putExtra("depotName",depotName);
                intent.putExtra("supplierName",supplierName);
                intent.putExtra("isAuto",true);


                startActivityForResult(intent,1);
                break;
            case R.id.finish:
                finishBtn();
                break;

        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case 1:

                if(resultCode==1){
                    lists.clear();
                    dateList=data.getStringExtra("lists");

                    jsonParse();


                    loadView();

                }

                if(resultCode==2){
                    lists.clear();
                    dateList=data.getStringExtra("dateList");

                    jsonParse();


                    loadView();

                    TextChange();

                }
                break;
        }
    }

    Dialog dialog;
    public void show(){

        dialog = new Dialog(this,R.style.ActionSheetDialogStyle);
        //填充对话框的布局
        LinearLayout inflate = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.bottom_cangku, null);
        //初始化控件

        Button closeBtn=inflate.findViewById(R.id.closeBtn);
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });



         final ListView listView=inflate.findViewById(R.id.listview);


        //宽高
        listView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        //设置Padding
        listView.setPadding(0,00,0,0);


        //将布局设置给Dialog
        dialog.setContentView(inflate);
        Window dialogWindow = dialog.getWindow();
        //设置Dialog从窗体底部弹出
        dialogWindow.setGravity( Gravity.BOTTOM);
        //获得窗体的属性
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        //lp.y = 20;//设置Dialog距离底部的距离
//       将属性设置给窗体
        lp.x = 0; // 新位置X坐标
        lp.y = 0; // 新位置Y坐标
        lp.width = (int) getResources().getDisplayMetrics().widthPixels; // 宽度
        inflate.measure(0, 0);
        //lp.height = inflate.getMeasuredHeight();

        lp.alpha = 9f; // 透明度
        dialogWindow.setAttributes(lp);


        dialog.show();//显示对话框



        new Thread(new Runnable() {
            @Override
            public void run() {
                loadModel(listView);

            }
        }).start();
    }


    List<Map<String, String>> listitem = new ArrayList<Map<String, String>>();
    void loadModel(final ListView listView){
        listitem.clear();
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

                    Map<String,String> map1=new HashMap<>();
                    map1.put("name",name);
                    map1.put("id",id);
                    listitem.add(map1);

                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        loadBottomCangkuListView(listView);
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();

            }
        }
    }


    void loadBottomCangkuListView(final ListView listView) {
        SimpleAdapter myAdapter = new SimpleAdapter(getApplicationContext(),listitem,

                R.layout.item_bottom_cangku,new String[]{"name"},

                new int[]{R.id.name});

        listView.setAdapter(myAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dialog.dismiss();

                cangkuText.setText(listitem.get(position).get("name"));


                if(listitem.get(position).get("name")!=depotName){
                    depotName=listitem.get(position).get("name");
                    depot=listitem.get(position).get("id");
                    lists.clear();
                    loadView();
                    TextChange();

                }

            }
        });
    }



    void loadView(){
        listView.setAdapter(new CommonAdapter<ShangpinBaseDatus>(AddjinhuoYixuanshangpinActivity.this,lists, R.layout.item_yixuanshangpin) {

            @Override
            public void convert(final ViewHolder holder, ShangpinBaseDatus shangpinDatus,final int position, View convertView) {
                holder.setText(R.id.name,shangpinDatus.getProName());
                holder.setText(R.id.totalAmount,shangpinDatus.getTotalAmount()+"");
                holder.setText(R.id.unit,shangpinDatus.getUnit());

                holder.setText(R.id.count,shangpinDatus.getTotal()+"");
                holder.setText(R.id.detail,"￥"+shangpinDatus.getPrice()+"*"+shangpinDatus.getTotal()+shangpinDatus.getUnit());

                holder.setOnClickListener(R.id.ll_content, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                    }
                });

                final int[] amount = {shangpinDatus.getTotal()};
                final TextView etAmount=holder.getView(R.id.count);
                holder.setOnClickListener(R.id.btnIncrease, new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        amount[0]++;
                        etAmount.setText(amount[0]+"");
                        ShangpinBaseDatus datus=lists.get(position);
                        datus.setTotal(Integer.parseInt(etAmount.getText().toString()));
                        datus.setTotalAmount(datus.getPrice()*datus.getTotal());
                        holder.setText(R.id.totalAmount,datus.getTotalAmount()+"");
                        holder.setText(R.id.detail,"￥"+datus.getPrice()+"*"+datus.getTotal()+datus.getUnit());
                        TextChange();


                    }
                });

                holder.setOnClickListener(R.id.btnDecrease, new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if(amount[0] <=0){
                            return;
                        }
                        amount[0]--;
                        etAmount.setText(amount[0]+"");

                        ShangpinBaseDatus datus=lists.get(position);
                        datus.setTotal(Integer.parseInt(etAmount.getText().toString()));
                        datus.setTotalAmount(datus.getTotal()*datus.getPrice());
                        holder.setText(R.id.totalAmount,datus.getTotalAmount()+"");
                        holder.setText(R.id.detail,"￥"+datus.getPrice()+"*"+datus.getTotal()+datus.getUnit());
                        TextChange();

                    }
                });

            }
        });
    }


    int getTotal(){
        int total=0;
        for(int i=0;i<lists.size();i++){
            total+=lists.get(i).getTotal();
        }
        return total;
    }

    float getTotalAmount(){
        int totalAmount=0;
        for(int i=0;i<lists.size();i++){
            totalAmount+=lists.get(i).getTotal()*lists.get(i).getPrice();
        }
        return totalAmount;
    }

    void TextChange(){
        int size=lists.size();
        int total=getTotal();
        float totalAmount=getTotalAmount();
        String s="结算（{0}种共{1}件，￥{2}）";
        finishBtn.setText(MessageFormat.format(s,size,total,totalAmount));
    }


    @Override
    protected void onResume() {
        super.onResume();
        TextChange();
    }

    void finishBtn(){

        if(lists.size()==0){
            Toast.makeText(this, "请添加商品", Toast.LENGTH_SHORT).show();
            String s="结算（{0}种共{1}件，￥{2}）";
            finishBtn.setText(MessageFormat.format(s,lists.size(),0,0));
            return;
        }

        if(getTotal()==0){
            Toast.makeText(this, "请添加商品", Toast.LENGTH_SHORT).show();
            String s="结算（{0}种共{1}件，￥{2}）";
            finishBtn.setText(MessageFormat.format(s,0,0,0));
            return;
        }
        Intent intent=new Intent();
        dateList=jsonToString();
        intent.putExtra("dateList",dateList);
        intent.putExtra("count",getTotal());
        intent.putExtra("totalAmount",getTotalAmount());
        intent.putExtra("depot",depot);
        intent.putExtra("depotName",depotName);
        setResult(3,intent);
        finish();
    }

}
