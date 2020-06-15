package com.example.qunxin.erp.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.qunxin.erp.R;
import com.example.qunxin.erp.UserBaseDatus;
import com.example.qunxin.erp.modle.ShangpinBaseDatus;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import swipedelete.utils.CommonAdapter;
import swipedelete.utils.ViewHolder;

public class XuanzeXiaoshou_tuihuoshangpinActivity extends AppCompatActivity implements View.OnClickListener {

    View backBtn;
    View jiesuanBtn;
    TextView sellNoText;
    TextView depotNameText;
    ListView listView;
    TextView busNoText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xuanze_xiaoshou_tuihuoshangpin);

        initView();
    }


    void initView(){
        backBtn=findViewById(R.id.back);
        jiesuanBtn=findViewById(R.id.jiesuan);
        sellNoText=findViewById(R.id.sellNo);
        depotNameText=findViewById(R.id.depotName);
        listView=findViewById(R.id.listView);
        busNoText=findViewById(R.id.busNo);

        backBtn.setOnClickListener(this);
        jiesuanBtn.setOnClickListener(this);


        getBusNoAndInitModel();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    loadModle();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();


    }




    void getBusNoAndInitModel(){
        UserBaseDatus.getInstance().getShangpinbianhao("14",XuanzeXiaoshou_tuihuoshangpinActivity.this,busNoText);

        Intent intent=getIntent();
        id=intent.getStringExtra("id");
    }



    String id="";
    String sellNo="";
    String busTime="";
    String leader="";
    String custName="";
    String otherAmount="";
    String totalAmount="";
    String busScope="";
    String recAmount="";
    String remarks="";
    String createTime="";
    String createBy="";
    String depotName="";

    String cust;
    String depot;
    String sellId;


    List<ShangpinBaseDatus> lists=new ArrayList<>();
    void loadModle() throws JSONException {
        final String contentType = "application/x-www-form-urlencoded";
        final String url = UserBaseDatus.getInstance().url + "api/sells/select";
        final String data = "signa=" + UserBaseDatus.getInstance().getSign() + "&&sellId=" + id;
        Map map = UserBaseDatus.getInstance().isSuccessPost(url, data, contentType);
        if ((boolean) (map.get("isSuccess"))) {
            JSONObject jsonObject=(JSONObject) map.get("json");

            JSONObject json1=jsonObject.getJSONObject("data");




            JSONObject json=json1.getJSONObject("sell");
            sellNo=json.getString("sellNo");
            busTime=json.getString("busTime");
            leader=json.getString("leader");
            custName=json.getString("custName");
            otherAmount=json.getString("otherAmount");
            remarks=json.getString("remarks");
            totalAmount=json.getString("amount");
            busScope=json.getString("busScope");
            recAmount=json.getString("recAmount");
            createTime=json.getString("createTime");
            createBy=json.getString("createBy");
            depotName=json.getString("outDepotName");
            cust=json.getString("cust");
            depot=json.getString("outDepot");
            sellId=json.getString("id");


            JSONArray jsonArray= json1.getJSONArray("sellDetailList");
            for (int i=0;i<jsonArray.length();i++){
                JSONObject object=jsonArray.getJSONObject(i);
                String proName=object.getString("proName");
                int price=object.getInt("sellPrice");
                int total=object.getInt("total");
                String unit=object.getString("unit");
                String proId=object.getString("proId");
                String property=object.getString("property");
                String norms=object.getString("norms");
                String proNo=object.getString("proNo");
                String totalAmount=object.getString("totalAmount");
                String sellPrice=object.getString("sellPrice");
                String id=object.getString("id");
                ShangpinBaseDatus datus=new ShangpinBaseDatus();
                datus.setProName(proName);
                datus.setPrice(price);
                datus.setTotal(total);
                datus.setTotalAmount(Float.parseFloat(totalAmount));
                datus.setKucunCount(total);
                datus.setTuihuoCount(total);
                datus.setUnit(unit);
                datus.setProId(proId);
                datus.setSellPrice(Float.parseFloat(sellPrice));
                datus.setProperty(property);
                datus.setProNo(proNo);
                datus.setNorms(norms);
                datus.setId(id);
                lists.add(datus);
            }

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    depotNameText.setText(depotName);
                    sellNoText.setText(sellNo);
                    loadListView();
                }
            });
        }

    }
            /*
            "buyPrice": 25,
             "createBy": "1",
             "createTime": "2019-05-28 16:25:55",
             "id": "19be0ff691e047cf89caa100b3e27354",
             "norms": "杯型",
             "params": {},
            "proId": "c5684753161047db8e68e6bf09e395ad",
            "proName": "杯子",
            "proNo": "SP20190101000",
            "property": "陶瓷",
            "rejectId": "eda1b1b2733d46c7ba3ffa727f6d695f",
            "rejectNo": "SP20190101000",
            "remark": "string",
            "remarks": "嘎嘎嘎",
            "searchValue": "string",
            "sellPrice": 25,
            "total": 1,
            "totalAmount": 25,
            "unit": "个",
            "updateBy": "1",
            "updateTime": "2019-05-28 16:25:55"
            */
    String listToJson(){
        StringBuffer buffer=new StringBuffer();
        for (int i=0;i<lists.size();i++){
            JSONObject jsonObject=new JSONObject();
            try {
                if(lists.get(i).getTotal()==0) break;

                jsonObject.put("buyPrice",lists.get(i).getPrice());
                jsonObject.put("createBy",UserBaseDatus.getInstance().userId);
                jsonObject.put("id",lists.get(i).getId());
                jsonObject.put("proName",lists.get(i).getProName());
                jsonObject.put("proNo",lists.get(i).getProNo());
                jsonObject.put("property",lists.get(i).getProperty());
                jsonObject.put("norms",lists.get(i).getNorms());
                jsonObject.put("rejectId","");
                //jsonObject.put("rejectNo",busNoText.getText().toString());
                jsonObject.put("remarks","");
                jsonObject.put("sellPrice",lists.get(i).getSellPrice());
                jsonObject.put("total",lists.get(i).getTotal());
                jsonObject.put("totalAmount",lists.get(i).getTotalAmount());
                jsonObject.put("unit",lists.get(i).getUnit());
                jsonObject.put("updateBy",UserBaseDatus.getInstance().userId);
                jsonObject.put("proId",lists.get(i).getProId());

                String content=String.valueOf(jsonObject);
                buffer.append(content).append(",");
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        String s= buffer.toString();
        return "["+s.substring(0,s.length()-1)+"]";
    }


    void loadListView(){

        BaseAdapter adapter=new CommonAdapter<ShangpinBaseDatus>(XuanzeXiaoshou_tuihuoshangpinActivity.this,lists, R.layout.item_xuanzetuihuoshangpin) {

            @Override
            public void convert(final ViewHolder holder, final ShangpinBaseDatus shangpinDatus, final int position, View convertView) {
                holder.setText(R.id.name,shangpinDatus.getProName());
                holder.setText(R.id.kucunCount,shangpinDatus.getKucunCount()+shangpinDatus.getUnit());

                holder.setText(R.id.tuihuoCount,shangpinDatus.getKucunCount()+shangpinDatus.getUnit());

            }
        };

        listView.setAdapter(adapter);



    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();
                break;

            case R.id.jiesuan:
                Intent intent=new Intent(XuanzeXiaoshou_tuihuoshangpinActivity.this,AddXiaoshoutuihuodanAfterActivity.class);
                intent.putExtra("cust",cust);
                intent.putExtra("depot",depot);
                intent.putExtra("custName",custName);
                intent.putExtra("depotName",depotName);
                intent.putExtra("price",getTotalAmount());
                intent.putExtra("count",getTotal());
                intent.putExtra("lists",listToJson());
                intent.putExtra("busNo",busNoText.getText().toString());
                intent.putExtra("leader",leader);
                intent.putExtra("id",sellId);
                intent.putExtra("isGuanLianJinhuodan",true);
                intent.putExtra("sellNo",sellNoText.getText().toString());
                startActivity(intent);
                finish();
                break;
        }
    }


    int getTotal(){
        int total=0;
        for (int i=0;i<lists.size();i++){
            total+=lists.get(i).getTotal();
        }
        return total;
    }




    float getTotalAmount(){
        float totalAmount=0;
        for (int i=0;i<lists.size();i++){
            totalAmount+=lists.get(i).getTotal()*lists.get(i).getPrice();
        }
        return totalAmount;
    }


}
