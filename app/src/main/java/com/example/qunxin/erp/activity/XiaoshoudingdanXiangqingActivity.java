package com.example.qunxin.erp.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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

public class XiaoshoudingdanXiangqingActivity extends AppCompatActivity {

    View backBtn;

    TextView danhaoText;
    TextView yewuriqiText;
    TextView leaderText;
    TextView kehuNameText;
    TextView depotNameText;

    TextView otherCaseText;
    TextView totalAmountText;
    TextView jiesuanzhanghuText;
    TextView shifujineText;

    ImageView imageView;
    TextView remarksText;
    TextView zhidanriqiText;
    TextView createByText;
    TextView cangkuText;


    ListView listView;

    TextView peihuohuozhuanxiaoshouText;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xiaoshoudingdan_xiangqing);
        initView();
    }


    void initView(){

        danhaoText=findViewById(R.id.buyNo);
        yewuriqiText=findViewById(R.id.yewuriqi);
        leaderText=findViewById(R.id.leader);
        kehuNameText=findViewById(R.id.kehuName);
        depotNameText=findViewById(R.id.cangku);
        otherCaseText=findViewById(R.id.otherCase);
        totalAmountText=findViewById(R.id.totalAmount);
        jiesuanzhanghuText=findViewById(R.id.jiesuanzhanghu);
        shifujineText=findViewById(R.id.shifujine);
        imageView=findViewById(R.id.image);
        remarksText=findViewById(R.id.remarksText);
        zhidanriqiText=findViewById(R.id.zhidanriqi);
        createByText=findViewById(R.id.createBy);
        cangkuText=findViewById(R.id.cangku);
        peihuohuozhuanxiaoshouText=findViewById(R.id.peihuohuozhuanxiaoshou);


        //framLayout=findViewById(R.id.frameLayout);
        listView=findViewById(R.id.listView);
        backBtn=findViewById(R.id.back);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImage();
            }
        });

        peihuohuozhuanxiaoshouText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(status.equals("0")){
                    peiHuo();
                }else if(status.equals("1")){
                    try {
                        zhuanxiaoshou();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }else if(status.equals("2")){

                }

        }
        });


        initModel();
    }


    void peiHuo(){
        final String contentType = "application/x-www-form-urlencoded";
        final String url = UserBaseDatus.getInstance().url + "api/app/sellorders/confirmApp";
        final String data = "signa=" + UserBaseDatus.getInstance().getSign() + "&&userId=" + UserBaseDatus.getInstance().userId+"&&id="+id;
        new Thread(new Runnable() {
            @Override
            public void run() {
                Map map = UserBaseDatus.getInstance().isSuccessPost(url, data, contentType);
                if ((boolean) (map.get("isSuccess"))) {
                    Intent intent=new Intent(XiaoshoudingdanXiangqingActivity.this,AddXiaoshoudingdanPeihuoSuccessActivity.class);
                    startActivity(intent);
                }else{
                }
            }
        }).start();

    }

    void zhuanxiaoshou() throws JSONException {
        /*
        {
  "busTime": "2019-05-21 09:24:20",
  "createBy": "1",
  "id": "28c849ba0d2745bbb595def92df50bbf",
  "leader": "5",
  "sellNo": "SO20190521989970002",
  "signa": "0"
}
         */


        JSONObject jsonObject=new JSONObject();
        jsonObject.put("busTime",yewuriqiText.getText().toString());
        jsonObject.put("createBy",UserBaseDatus.getInstance().userId);
        jsonObject.put("id",id);
        jsonObject.put("leader",leaderText.getText().toString());
        jsonObject.put("sellNo",UserBaseDatus.getInstance().getShangpinbianhao("13"));
        jsonObject.put("signa",UserBaseDatus.getInstance().getSign());


        final String dataStr=jsonObject.toString();
        Log.d("jsonString", dataStr);
        final String contentType = "application/json";;
        final String url = UserBaseDatus.getInstance().url + "api/app/sellorders/addSellApp";
        new Thread(new Runnable() {
            @Override
            public void run() {
                Map map = UserBaseDatus.getInstance().isSuccessPost(url, dataStr, contentType);
                if ((boolean) (map.get("isSuccess"))) {
                    Intent intent=new Intent(XiaoshoudingdanXiangqingActivity.this,AddDingdanzhuanxiaoshouSuccessActivity.class);
                    startActivity(intent);
                }else {
                    JSONObject jsonObject= (JSONObject) map.get("json");
                    try {
                        data =jsonObject.getString("data");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(XiaoshoudingdanXiangqingActivity.this, data, Toast.LENGTH_SHORT).show();
                            data="";
                        }
                    });
                }
            }
        }).start();

    }

    String data="";
    void initModel(){
        Intent intent=getIntent();
        id=intent.getStringExtra("id");

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    loadModle();
                    getImage();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    String id="";
    String sellNo="";
    String busTime="";
    String leader="";
    String custName="";
    String otherAmount="";
    String totalAmount="";
    String busScope="";
    String remarks="";
    String createTime="";
    String createBy="";
    String depotName="";
    String status="";
    String outDepot="";
    String cust="";
    float gop=0;
    float dept=0;
    float amount=0;
    float recAmount=0;
    int total=0;


    List<ShangpinBaseDatus> lists=new ArrayList<>();
    void loadModle() throws JSONException {
        final String contentType = "application/x-www-form-urlencoded";
        final String url = UserBaseDatus.getInstance().url + "api/app/sellorders/selectById";
        final String data = "signa=" + UserBaseDatus.getInstance().getSign() + "&&id=" + id;
        Map map = UserBaseDatus.getInstance().isSuccessPost(url, data, contentType);
        if ((boolean) (map.get("isSuccess"))) {
            JSONObject jsonObject=(JSONObject) map.get("json");

            JSONObject json1=jsonObject.getJSONObject("data");

            JSONObject json=json1.getJSONObject("sellorder");
            sellNo=json.getString("sellNo");
            busTime=json.getString("busTime");
            leader=json.getString("leader");
            custName=json.getString("custName");
            otherAmount=json.getString("otherAmount");
            remarks=json.getString("remarks");
            totalAmount=json.getString("amount");
            busScope=json.getString("busScope");
            createTime=json.getString("createTime");
            createBy=json.getString("creatorName");
            depotName=json.getString("outDepotName");
            outDepot=json.getString("outDepot");
            cust=json.getString("cust");
            status=json.getString("status");
            gop=Float.parseFloat(json.getString("gop"));
            dept=Float.parseFloat(json.getString("dept"));
            amount=Float.parseFloat(json.getString("amount"));
            recAmount=Float.parseFloat(json.getString("recAmount"));
            total=json.getInt("total");


            JSONArray jsonArray= json1.getJSONArray("sellorderDetailList");
            for (int i=0;i<jsonArray.length();i++){
                JSONObject object=jsonArray.getJSONObject(i);
                String proName=object.getString("proName");
                int price=object.getInt("sellPrice");
                int total=object.getInt("total");
                String unit=object.getString("unit");
                ShangpinBaseDatus datus=new ShangpinBaseDatus();
                datus.setProName(proName);
                datus.setPrice(price);
                datus.setTotal(total);
                datus.setTotalAmount(price*total);
                datus.setUnit(unit);
                lists.add(datus);
            }



            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    loadView(sellNo,busTime,leader,custName,otherAmount,remarks,totalAmount,busScope,recAmount+"",createTime,createBy,depotName,status);
                    loadListView();
                }
            });

            getJsonIds();

        }

    }

    private void loadView(String buyNo, String busTime,
                          String leader, String custName, String otherAmount, String remarks, String totalAmount,
                          String busScope, String recAmount, String createTime,String createBy,String cangku,String status) {
        danhaoText.setText(buyNo);
        yewuriqiText.setText(busTime);
        leaderText.setText(leader);
        kehuNameText.setText(custName);
        otherCaseText.setText("￥"+otherAmount);
        remarksText.setText(remarks);
        totalAmountText.setText("￥"+totalAmount);
        jiesuanzhanghuText.setText(busScope);
        shifujineText.setText("￥"+recAmount);
        zhidanriqiText.setText(createTime);
        createByText.setText(createBy);
        cangkuText.setText(cangku);
        if(status.equals("0")){
            peihuohuozhuanxiaoshouText.setText("配货");
        }else if(status.equals("1")){
            peihuohuozhuanxiaoshouText.setText("转销售");

        }else if(status.equals("2")){
            peihuohuozhuanxiaoshouText.setText("");

        }
    }



    List<String> jsonIds=new ArrayList<>();
    void getJsonIds(){
        Log.d("buyNo", sellNo);
        final String contentType = "application/x-www-form-urlencoded";
        final String url = UserBaseDatus.getInstance().url + "api/app/upload/list";
        final String data = "signa=" + UserBaseDatus.getInstance().getSign() + "&&busNo=" + sellNo;
        Map map = UserBaseDatus.getInstance().isSuccessPost(url, data, contentType);
        if ((boolean) (map.get("isSuccess"))) {
            JSONObject jsonObject=(JSONObject) map.get("json");
            try {
                JSONArray jsonArray=jsonObject.getJSONArray("data");
                for(int i=0;i<jsonArray.length();i++){
                    JSONObject object=jsonArray.getJSONObject(i);
                    String id=object.getString("id");
                    jsonIds.add(id);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    BaseAdapter adapter=null;


    void loadListView(){

        adapter=new CommonAdapter<ShangpinBaseDatus>(XiaoshoudingdanXiangqingActivity.this,lists, R.layout.item_jinhuoxiangqing_shangpin) {

            @Override
            public void convert(final ViewHolder holder, final ShangpinBaseDatus shangpinDatus, final int position, View convertView) {
                holder.setText(R.id.name,shangpinDatus.getProName());
                holder.setText(R.id.totalAmount,"￥"+shangpinDatus.getTotalAmount());

                holder.setText(R.id.detail,"￥"+shangpinDatus.getPrice()+"*"+shangpinDatus.getTotal()+shangpinDatus.getUnit());

            }
        };

        listView.setAdapter(adapter);
        setListViewHeightBasedOnChildren(listView);


    }



    void setListViewHeightBasedOnChildren(ListView listView) {



        ListAdapter listAdapter = listView.getAdapter();



        if (listAdapter == null) {

            return;

        }



        int totalHeight = 0;



        for (int i = 0; i < listAdapter.getCount(); i++) {

            View listItem = listAdapter.getView(i, null, listView);

            listItem.measure(0, 0);

            totalHeight += listItem.getMeasuredHeight();

        }



        ViewGroup.LayoutParams params = listView.getLayoutParams();



        params.height = totalHeight

                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));



        // ((ViewGroup.MarginLayoutParams) params).setMargins(10, 10, 10, 10); // 可删除



        listView.setLayoutParams(params);

    }

    Bitmap mBitmap;
    void getImage() {
        if(jsonIds.size()==0) return;
        final String contentType = "application/octet-stream";
        final String data = "?signa=" + UserBaseDatus.getInstance().getSign() + "&id=" + jsonIds.get(0);
        final String url = UserBaseDatus.getInstance().url + "api/app/upload/download"+data;
        Log.d("url", url);
        final Bitmap bitmap = UserBaseDatus.getInstance().isSuccessGet(url, contentType);
        mBitmap=bitmap;
        System.out.println(bitmap);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                imageView.setImageBitmap(mBitmap);

            }
        });
    }


    Dialog dialog;
    void showImage(){
        dialog = new Dialog(XiaoshoudingdanXiangqingActivity.this, R.style.AlertDialog_AppCompat_Light);
        ImageView imageView = getImageView();
        dialog.setContentView(imageView);
        dialog.show();

        //大图的点击事件（点击让他消失）
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    //动态的ImageView
    private ImageView getImageView(){
        ImageView iv = new ImageView(this);
        //宽高
        iv.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        //设置Padding
        iv.setPadding(20,20,20,20);

        if(mBitmap!=null){
            iv.setImageBitmap(mBitmap);
        }else {
            iv.setImageResource(R.mipmap.shop_icon);
        }
        return iv;
    }
}
