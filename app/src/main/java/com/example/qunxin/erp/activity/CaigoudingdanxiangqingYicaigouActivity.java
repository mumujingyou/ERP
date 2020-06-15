package com.example.qunxin.erp.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
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

public class CaigoudingdanxiangqingYicaigouActivity extends AppCompatActivity {

    View backBtn;

    TextView danhaoText;
    TextView yewuriqiText;
    TextView supplierNameText;


    ImageView imageView;
    TextView remarksText;
    TextView zhidanriqiText;
    TextView createByText;


    ListView listView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caigoudingdanxiangqing_yicaigou);
        initView();
    }

    void initView(){
        danhaoText=findViewById(R.id.busNo);
        yewuriqiText=findViewById(R.id.dateText);
        supplierNameText=findViewById(R.id.supplierName);

        imageView=findViewById(R.id.image);
        remarksText=findViewById(R.id.remarksText);
        zhidanriqiText=findViewById(R.id.zhidanriqi);
        createByText=findViewById(R.id.createBy);

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
        supplierName=intent.getStringExtra("supplierName");

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
    String purchaseNo="";
    String busTime="";
    String supplierName="";
    String remarks="";
    String createTime="";
    String createBy="";
    String supplier="";
    int total=0;



    List<ShangpinBaseDatus> lists=new ArrayList<>();
    void loadModle() throws JSONException {
        final String contentType = "application/x-www-form-urlencoded";
        final String url = UserBaseDatus.getInstance().url + "api/app/purchases/affirmDetail";
        final String data = "signa=" + UserBaseDatus.getInstance().getSign() + "&&purchaseId=" + id;
        Map map = UserBaseDatus.getInstance().isSuccessPost(url, data, contentType);
        if ((boolean) (map.get("isSuccess"))) {
            JSONObject jsonObject=(JSONObject) map.get("json");

            JSONObject json1=jsonObject.getJSONObject("data");


            JSONObject json=json1.getJSONObject("purchase");
            purchaseNo=json.getString("purchaseNo");
            busTime=json.getString("deliveryDate");
            remarks=json.getString("remark");
            createTime=json.getString("createTime");
            supplier=json.getString("supplier");
            total=json.getInt("total");
            createBy="管理员";


            JSONArray jsonArray= json1.getJSONArray("purchaseDetails");
            for (int i=0;i<jsonArray.length();i++){
                JSONObject object=jsonArray.getJSONObject(i);
                String proName=object.getString("proName");
                String unit=object.getString("unit");
                String proId=object.getString("proId");
                String remarks=object.getString("remarks");
                String norms=object.getString("norms");
                String marketAmount=object.getString("marketAmount");
                String purchaseAmount=object.getString("purchaseAmount");
                int total=object.getInt("orderQuantity");
                int actualPurchaseQuantity=object.getInt("actualPurchaseQuantity");

                ShangpinBaseDatus datus=new ShangpinBaseDatus();

                datus.setProName(proName);
                datus.setTotal(total);
                datus.setUnit(unit);
                datus.setProId(proId);
                datus.setRemarks(remarks);
                datus.setNorms(norms);
                datus.setPurchaseAmount(Float.parseFloat(purchaseAmount));
                datus.setMarketAmount(Float.parseFloat(marketAmount));
                datus.setActualPurchaseQuantity(actualPurchaseQuantity);
                //datus.setProperty(property);
                lists.add(datus);
            }

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    loadView(purchaseNo,busTime,supplierName,remarks,createTime,createBy);
                    loadListView();
                }
            });
            getJsonIds();

        }

    }

    private void loadView(String purchaseNo, String busTime, String supplierName, String remarks,
                          String createTime,String createBy) {
        danhaoText.setText(purchaseNo);
        yewuriqiText.setText(busTime);
        supplierNameText.setText(supplierName);
        remarksText.setText(remarks);

        zhidanriqiText.setText(createTime);
        createByText.setText(createBy);
    }



    List<String> jsonIds=new ArrayList<>();
    void getJsonIds(){
        final String contentType = "application/x-www-form-urlencoded";
        final String url = UserBaseDatus.getInstance().url + "api/app/upload/list";
        final String data = "signa=" + UserBaseDatus.getInstance().getSign() + "&&busNo=" + purchaseNo;
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

        adapter=new CommonAdapter<ShangpinBaseDatus>(CaigoudingdanxiangqingYicaigouActivity.this,lists, R.layout.item_caigou_enter_shangpin) {

            @Override
            public void convert(final ViewHolder holder, final ShangpinBaseDatus shangpinDatus, final int position, View convertView) {
                holder.setText(R.id.name,shangpinDatus.getProName());

                holder.setText(R.id.detail,"￥"+shangpinDatus.getPurchaseAmount()+"*"+shangpinDatus.getActualPurchaseQuantity()+shangpinDatus.getUnit());

                holder.setText(R.id.totalAmount,"￥"+shangpinDatus.getPurchaseAmount()*shangpinDatus.getActualPurchaseQuantity());


                holder.setOnClickListener(R.id.shangpin, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ShangpinBaseDatus shangpinDatus=lists.get(position);
                        Intent intent=new Intent(CaigoudingdanxiangqingYicaigouActivity.this,CaigouEnterShangpinxiangqingActivity.class);
                        intent.putExtra("name",shangpinDatus.getProName());
                        intent.putExtra("unit",shangpinDatus.getUnit());
                        intent.putExtra("shichangjia",shangpinDatus.getMarketAmount());
                        intent.putExtra("caigoujia",shangpinDatus.getPurchaseAmount());
                        intent.putExtra("caigoushuliang",shangpinDatus.getTotal());
                        intent.putExtra("shijicaigoushuliang",shangpinDatus.getActualPurchaseQuantity());

                        Log.d("name", shangpinDatus.getProName());
                        startActivity(intent);
                    }
                });
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
        final String data = "?signa=" + UserBaseDatus.getInstance().getSign() + "&id=" + jsonIds.get(jsonIds.size()-1);
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
        dialog = new Dialog(CaigoudingdanxiangqingYicaigouActivity.this, R.style.AlertDialog_AppCompat_Light);
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
