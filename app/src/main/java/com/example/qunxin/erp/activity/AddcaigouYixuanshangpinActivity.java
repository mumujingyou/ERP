package com.example.qunxin.erp.activity;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

public class AddcaigouYixuanshangpinActivity extends AppCompatActivity implements View.OnClickListener {
    View backBtn;

    ListView listView;
    Button finishBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addcaigou_yixuanshangpin);

        initView();
    }

    String dateList;
    String supplier;
    void initView(){
        backBtn=findViewById(R.id.back);

        listView=findViewById(R.id.listview);
        finishBtn=findViewById(R.id.finish);

        backBtn.setOnClickListener(this);
        finishBtn.setOnClickListener(this);



        Intent intent=getIntent();
        dateList=intent.getStringExtra("dateList");
        supplier=intent.getStringExtra("supplier");
        Log.d("dateList", dateList);

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
                String total=jsonObject.getString("purchaseAmount");
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
//                jsonObject.put("norms",lists.get(i).getNorms());
//                jsonObject.put("property",lists.get(i).getProperty());
//                jsonObject.put("price",lists.get(i).getPrice());
//                jsonObject.put("proId",lists.get(i).getProId());
//                jsonObject.put("proName",lists.get(i).getProName());
//                jsonObject.put("remarks",lists.get(i).getRemarks());
//                jsonObject.put("total",lists.get(i).getTotal());
//                jsonObject.put("totalAmount",lists.get(i).getTotalAmount());
//                jsonObject.put("unit",lists.get(i).getUnit());
//                jsonObject.put("proNo",lists.get(i).getProNo());


                jsonObject.put("actualPurchaseQuantity",lists.get(i).getTotal());
                jsonObject.put("applicationAllowance",0);
                jsonObject.put("createBy",UserBaseDatus.getInstance().userId);
                jsonObject.put("createTime","");
                jsonObject.put("delFlag",0);
                jsonObject.put("deliveryDate","");
                jsonObject.put("id","");
                jsonObject.put("marketAmount",lists.get(i).getPrice());
                jsonObject.put("orderQuantity",lists.get(i).getTotal());
                jsonObject.put("proId",lists.get(i).getProId());
                jsonObject.put("purchaseId","");
                jsonObject.put("purchaseAmount",lists.get(i).getTotal());

                jsonObject.put("remark","");
                jsonObject.put("searchValue","");
                jsonObject.put("supplier",supplier);

                jsonObject.put("updateBy",UserBaseDatus.getInstance().userId);
                jsonObject.put("updateTime","");

                jsonObject.put("norms",lists.get(i).getNorms());
                jsonObject.put("proName",lists.get(i).getProName());
                jsonObject.put("remarks",lists.get(i).getRemarks());
                jsonObject.put("unit",lists.get(i).getUnit());

                jsonObject.put("price",lists.get(i).getPrice());

                jsonObject.put("totalAmount",lists.get(i).getPrice()*lists.get(i).getTotal());
                jsonObject.put("property",lists.get(i).getProperty());

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
            case R.id.finish:
                finishBtn();
                break;

        }
    }


    void loadView(){
        listView.setAdapter(new CommonAdapter<ShangpinBaseDatus>(AddcaigouYixuanshangpinActivity.this,lists, R.layout.item_yixuanshangpin) {

            @Override
            public void convert(final ViewHolder holder, ShangpinBaseDatus shangpinDatus, final int position, View convertView) {
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
                        holder.setText(R.id.totalAmount,datus.getTotalAmount()+"￥");
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
        setResult(3,intent);
        finish();
    }

}
