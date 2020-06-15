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

public class JinhuoTuihuodanxiangqingActivity extends AppCompatActivity {

    View backBtn;
    TextView danhaoText;
    TextView yewuriqiText;
    TextView leaderText;
    TextView supplierNameText;
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



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tuihuodanxiangqing);
        initView();
    }


    void initView(){
        danhaoText=findViewById(R.id.buyNo);
        yewuriqiText=findViewById(R.id.yewuriqi);
        leaderText=findViewById(R.id.leader);
        supplierNameText=findViewById(R.id.gongyingshang);
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


        initModel();
    }

    void initModel(){
        Intent intent=getIntent();
        id=intent.getStringExtra("id");
        Log.d("initModel", id);

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


    List<ShangpinBaseDatus> lists=new ArrayList<>();
    void loadModle() throws JSONException {
        final String contentType = "application/x-www-form-urlencoded";
        final String url = UserBaseDatus.getInstance().url + "api/app/refund/appRefundDetail";
        final String data = "signa=" + UserBaseDatus.getInstance().getSign() + "&&id=" + id;
        Map map = UserBaseDatus.getInstance().isSuccessPost(url, data, contentType);
        if ((boolean) (map.get("isSuccess"))) {
            JSONObject jsonObject=(JSONObject) map.get("json");

            JSONObject json1=jsonObject.getJSONObject("data");


            JSONObject json=json1.getJSONObject("refund");
            buyNo=json.getString("refundNo");
            busTime=json.getString("busTime");
            leader=json.getString("leader");
            supplierName=json.getString("supplier");
            otherAmount=json.getString("otherAmount");
            remarks=json.getString("remarks");
            totalAmount=json.getString("totalAmount");
            busScope=json.getString("busScope");
            fccAmount=json.getString("payAmount");
            createTime=json.getString("createTime");
            createBy=json.getString("createBy");
            depotName=json.getString("depot");


            JSONArray jsonArray= json1.getJSONArray("list");
            for (int i=0;i<jsonArray.length();i++){
                JSONObject object=jsonArray.getJSONObject(i);
                String proName=object.getString("proName");
                int price=object.getInt("price");
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
                    loadView(buyNo,busTime,leader,supplierName,otherAmount,remarks,totalAmount,busScope,fccAmount,createTime,createBy,depotName);
                    loadListView();
                }
            });

            getJsonIds();

        }

    }

    private void loadView(String buyNo, String busTime,
                          String leader, String supplierName, String otherAmount, String remarks, String totalAmount,
                          String busScope, String fccAmount, String createTime,String createBy,String cangku) {
        danhaoText.setText(buyNo);
        yewuriqiText.setText(busTime);
        leaderText.setText(leader);
        supplierNameText.setText(supplierName);
        otherCaseText.setText("￥"+otherAmount);
        remarksText.setText(remarks);
        totalAmountText.setText("￥"+totalAmount);
        jiesuanzhanghuText.setText(busScope);
        shifujineText.setText("￥"+fccAmount);
        zhidanriqiText.setText(createTime);
        createByText.setText(createBy);
        cangkuText.setText(cangku);
    }




    List<String> jsonIds=new ArrayList<>();
    void getJsonIds(){
        Log.d("buyNo", buyNo);
        final String contentType = "application/x-www-form-urlencoded";
        final String url = UserBaseDatus.getInstance().url + "api/app/upload/list";
        final String data = "signa=" + UserBaseDatus.getInstance().getSign() + "&&busNo=" + buyNo;
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

        adapter=new CommonAdapter<ShangpinBaseDatus>(JinhuoTuihuodanxiangqingActivity.this,lists, R.layout.item_jinhuoxiangqing_shangpin) {

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
        System.out.println(jsonIds);
        if(jsonIds.size()==0) return;
        final String contentType = "application/octet-stream";
        final String data = "?signa=" + UserBaseDatus.getInstance().getSign() + "&id=" + jsonIds.get(0);

        final String url = UserBaseDatus.getInstance().url + "api/app/upload/download"+data;
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
        dialog = new Dialog(JinhuoTuihuodanxiangqingActivity.this, R.style.AlertDialog_AppCompat_Light);
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
