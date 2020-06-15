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

public class RukulishixiangqingActivity extends AppCompatActivity {

    View backBtn;

    TextView danhaoText;
    TextView yewuriqiText;
    TextView supplierNameText;


    ImageView imageView;
    TextView remarksText;
    TextView zhidanriqiText;
    TextView createByText;
    TextView totalAmountText;
    //TextView caigouyuanText;
    TextView jingshourenText;
    TextView depotNameText;


    ListView listView;
    View closeBtn;
    View editBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rukulishixiangqing);
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
        closeBtn=findViewById(R.id.closeBtn);

        //caigouyuanText=findViewById(R.id.caigouyuanText);
        jingshourenText=findViewById(R.id.jingshourenText);
        depotNameText=findViewById(R.id.depotName);
        totalAmountText=findViewById(R.id.totalAmount);

        listView=findViewById(R.id.listView);
        backBtn=findViewById(R.id.back);
        editBtn=findViewById(R.id.edit);
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

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//
                showPopupWindow(editBtn);
            }
        });

        initModel();
    }


    //控件下方弹出窗口

    private void showPopupWindow(View view1) {

        //自定义布局，显示内容

        final View view = LayoutInflater.from(RukulishixiangqingActivity.this).inflate(R.layout.print_popwindow, null);

        View printBtn =  view.findViewById(R.id.print);

        final PopupWindow window = new PopupWindow(view, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT, true);


        printBtn.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View v) {
                window.dismiss();
            }

        });

        window.setTouchable(true);

        window.setTouchInterceptor(new View.OnTouchListener() {

            @Override

            public boolean onTouch(View v, MotionEvent event) {

                return false;

                //这里如果返回true的话，touch事件将被拦截

                //拦截后 PoppWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss

            }

        });

        //（注意一下！！）如果不设置popupWindow的背景，无论是点击外部区域还是Back键都无法弹框
        window.showAsDropDown(view1);
        window.setBackgroundDrawable(new BitmapDrawable());

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
    String warehouseNo="";
    String busTime="";
    String supplierName="";
    String remarks="";
    String createTime="";
    String createBy="";
    String supplier="";
    int total=0;
    String depotName="";
    String leader="";



    List<ShangpinBaseDatus> lists=new ArrayList<>();
    void loadModle() throws JSONException {
        final String contentType = "application/x-www-form-urlencoded";
        final String url = UserBaseDatus.getInstance().url + "api/app/warehouse/selectWarehouseDetail";
        final String data = "signa=" + UserBaseDatus.getInstance().getSign() + "&&id=" + id;
        Log.d("purchaseId", id);
        Map map = UserBaseDatus.getInstance().isSuccessPost(url, data, contentType);
        if ((boolean) (map.get("isSuccess"))) {
            JSONObject jsonObject=(JSONObject) map.get("json");

            JSONObject json1=jsonObject.getJSONObject("data");


            JSONObject json=json1.getJSONObject("warehouse");
            warehouseNo=json.getString("warehouseNo");
            busTime=json.getString("busTime");
            remarks=json.getString("remark");
            createTime=json.getString("createTime");
            supplier=json.getString("supplierId");
            depotName=json.getString("depotId");
            leader=json.getString("leader");
            createBy="管理员";

            JSONArray jsonArray= json1.getJSONArray("warehouseDetailList");
            for (int i=0;i<jsonArray.length();i++){
                JSONObject object=jsonArray.getJSONObject(i);
                String proName=object.getString("proName");

                String unit=object.getString("unit");
                String proId=object.getString("proId");
                String remarks=object.getString("remarks");
                String norms=object.getString("norms");
                String id=object.getString("id");
                int total=object.getInt("total");
                String proPrice=object.getString("proPrice");
                int realTotal=object.getInt("realTotal");
                String realPrice=object.getString("realPrice");



                ShangpinBaseDatus datus=new ShangpinBaseDatus();

                datus.setProName(proName);
                datus.setUnit(unit);
                datus.setProId(proId);
                datus.setRemarks(remarks);
                datus.setNorms(norms);
                datus.setId(id);
                datus.setRealPrice(Float.parseFloat(realPrice));
                datus.setPrice(Float.parseFloat(proPrice));
                datus.setTotal(total);
                datus.setRealTotal(realTotal);

                lists.add(datus);
            }


            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    float totalAmount=getTotalAmount();
                    loadView(warehouseNo,busTime,supplierName,remarks,createTime,createBy,leader,totalAmount,depotName);
                    loadListView();
                }
            });
            getJsonIds();

        }

    }

    private void loadView(String purchaseNo, String busTime, String supplierName, String remarks,
                          String createTime,String createBy,String leader,float totalAmount,String depotName) {
        danhaoText.setText(purchaseNo);
        yewuriqiText.setText(busTime);
        supplierNameText.setText(supplierName);
        remarksText.setText(remarks);

        zhidanriqiText.setText(createTime);
        createByText.setText(createBy);
        jingshourenText.setText(leader);
        totalAmountText.setText(totalAmount+"￥");
        depotNameText.setText(depotName);
    }



    List<String> jsonIds=new ArrayList<>();
    void getJsonIds(){
        Log.d("buyNo", warehouseNo);
        final String contentType = "application/x-www-form-urlencoded";
        final String url = UserBaseDatus.getInstance().url + "api/app/upload/list";
        final String data = "signa=" + UserBaseDatus.getInstance().getSign() + "&&busNo=" + warehouseNo;
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

        adapter=new CommonAdapter<ShangpinBaseDatus>(RukulishixiangqingActivity.this,lists, R.layout.item_caigou_enter_shangpin) {

            @Override
            public void convert(final ViewHolder holder, final ShangpinBaseDatus shangpinDatus, final int position, View convertView) {
                holder.setText(R.id.name,shangpinDatus.getProName());

                holder.setText(R.id.detail,"￥"+shangpinDatus.getRealPrice()+"*"+shangpinDatus.getRealTotal()+shangpinDatus.getUnit());

                holder.setText(R.id.totalAmount,shangpinDatus.getRealPrice()*shangpinDatus.getRealTotal()+"￥");


                holder.setOnClickListener(R.id.shangpin, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ShangpinBaseDatus shangpinDatus=lists.get(position);
                        Intent intent=new Intent(RukulishixiangqingActivity.this,RukushangpinxiangqingActivity.class);
                        intent.putExtra("name",shangpinDatus.getProName());
                        intent.putExtra("unit",shangpinDatus.getUnit());
                        intent.putExtra("shangpinjia",shangpinDatus.getPrice());
                        intent.putExtra("shijijia",shangpinDatus.getRealPrice());
                        intent.putExtra("caigoushuliang",shangpinDatus.getTotal());
                        intent.putExtra("shijicaigoushuliang",shangpinDatus.getRealTotal());

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
        dialog = new Dialog(RukulishixiangqingActivity.this, R.style.AlertDialog_AppCompat_Light);
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

    float getTotalAmount(){
        float total=0;
        for (int i=0;i<lists.size();i++){
            total+=lists.get(i).getRealPrice()*lists.get(i).getRealTotal();
        }
        return total;
    }

}
