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

public class XuanzeJinhuo_tuihuoshangpinActivity extends AppCompatActivity implements View.OnClickListener {



    View backBtn;
    View jiesuanBtn;
    TextView busNoText;
    TextView depotNameText;
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xuanzetuihuoshangpin);

        initView();
    }


    void initView(){
        backBtn=findViewById(R.id.back);
        jiesuanBtn=findViewById(R.id.jiesuan);
        busNoText=findViewById(R.id.busNo);
        depotNameText=findViewById(R.id.depotName);
        listView=findViewById(R.id.listView);


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
        UserBaseDatus.getInstance().getShangpinbianhao("2",XuanzeJinhuo_tuihuoshangpinActivity.this,busNoText);

        Intent intent=getIntent();
        id=intent.getStringExtra("id");
    }



    String id="";
    String buyNo="";
    String busTime="";
    String leader="";
    String supplierName="";
    String otherAmount="";
    String totalAmount="";
    String busScope="";
    String fccAmount="";
    String remarks="";
    String createTime="";
    String createBy="";
    String depotName="";

    String supplier;
    String depot;


    List<ShangpinBaseDatus> lists=new ArrayList<>();
    void loadModle() throws JSONException {
        final String contentType = "application/x-www-form-urlencoded";
        final String url = UserBaseDatus.getInstance().url + "api/app/buy/lookDetail";
        final String data = "signa=" + UserBaseDatus.getInstance().getSign() + "&&id=" + id;
        Map map = UserBaseDatus.getInstance().isSuccessPost(url, data, contentType);
        if ((boolean) (map.get("isSuccess"))) {
            JSONObject jsonObject=(JSONObject) map.get("json");

            JSONObject json1=jsonObject.getJSONObject("data");


            JSONObject json=json1.getJSONObject("buy");
            buyNo=json.getString("buyNo");
            busTime=json.getString("busTime");
            leader=json.getString("leader");
            supplierName=json.getString("supplierName");
            otherAmount=json.getString("otherAmount");
            remarks=json.getString("remarks");
            totalAmount=json.getString("totalAmount");
            busScope=json.getString("busScope");
            fccAmount=json.getString("payAmount");
            createTime=json.getString("createTime");
            createBy=json.getString("createBy");
            depotName=json.getString("depotName");
            supplier=json.getString("supplier");
            depot=json.getString("depot");


            JSONArray jsonArray= json1.getJSONArray("bList");
            for (int i=0;i<jsonArray.length();i++){
                JSONObject object=jsonArray.getJSONObject(i);
                String proName=object.getString("proName");
                int price=object.getInt("price");
                int total=object.getInt("total");
                String unit=object.getString("unit");
                String proId=object.getString("proId");
                ShangpinBaseDatus datus=new ShangpinBaseDatus();
                datus.setProName(proName);
                datus.setPrice(price);
                datus.setTotal(total);
                datus.setTotalAmount(price*total);
                datus.setKucunCount(total);
                datus.setTuihuoCount(total);
                datus.setUnit(unit);
                datus.setProId(proId);
                lists.add(datus);
            }

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    depotNameText.setText(depotName);
                    loadListView();
                }
            });
        }

    }

    String listToJson(){
        StringBuffer buffer=new StringBuffer();
        for (int i=0;i<lists.size();i++){
            JSONObject jsonObject=new JSONObject();
            try {
                if(lists.get(i).getTotal()==0) break;

                jsonObject.put("proId",lists.get(i).getProId());
                jsonObject.put("remarks",lists.get(i).getRemarks());

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

        BaseAdapter adapter=new CommonAdapter<ShangpinBaseDatus>(XuanzeJinhuo_tuihuoshangpinActivity.this,lists, R.layout.item_xuanzetuihuoshangpin) {

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
                Intent intent=new Intent(XuanzeJinhuo_tuihuoshangpinActivity.this,AddJinhuotuihuodanAfterActivity.class);

                intent.putExtra("supplier",supplier);
                intent.putExtra("depot",depot);
                intent.putExtra("supplierName",supplierName);
                intent.putExtra("depotName",depotName);
                intent.putExtra("price",getTotalAmount());
                intent.putExtra("count",getTotal());
                intent.putExtra("lists",listToJson());
                intent.putExtra("busNo",busNoText.getText().toString());
                intent.putExtra("leader",leader);
                intent.putExtra("id",id);
                intent.putExtra("isGuanLianJinhuodan",true);
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
