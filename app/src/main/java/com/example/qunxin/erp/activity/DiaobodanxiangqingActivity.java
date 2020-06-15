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

public class DiaobodanxiangqingActivity extends AppCompatActivity {

    View backBtn;
    View editBtn;

    TextView danhaoText;
    TextView yewuriqiText;
    TextView leaderText;

    TextView otherCaseText;
    TextView jiesuanzhanghuText;

    ImageView imageView;
    TextView remarksText;
    TextView zhidanriqiText;
    TextView createByText;
    TextView refundDepotNameText;
    TextView buyDepotNameText;


    ListView listView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diaobodanxiangqing);

        initView();
    }


    void initView(){
        danhaoText=findViewById(R.id.busNo);
        yewuriqiText=findViewById(R.id.yewuriqi);
        leaderText=findViewById(R.id.leader);
        refundDepotNameText=findViewById(R.id.refundDepotName);
        buyDepotNameText=findViewById(R.id.buyDepotName);
        otherCaseText=findViewById(R.id.otherCase);
        jiesuanzhanghuText=findViewById(R.id.jiesuanzhanghu);
        imageView=findViewById(R.id.image);
        remarksText=findViewById(R.id.remarksText);
        zhidanriqiText=findViewById(R.id.zhidanriqi);
        createByText=findViewById(R.id.createBy);
        editBtn=findViewById(R.id.edit);

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

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupWindow(editBtn);
            }
        });

        initModel();
    }

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


    private void showPopupWindow(View view1) {

        //自定义布局，显示内容

        final View view = LayoutInflater.from(DiaobodanxiangqingActivity.this).inflate(R.layout.print_popwindow, null);

        View print =  view.findViewById(R.id.print);

        final PopupWindow window = new PopupWindow(view, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT, true);


        print.setOnClickListener(new View.OnClickListener() {

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



    String id="";
    String busNo="";
    String busTime="";
    String leader="";
    String otherAmount="";
    String busScope="";
    String remarks="";
    String createTime="";
    String createBy="";
    String refunDepotName="";
    String buyDepotName="";


    List<ShangpinBaseDatus> lists=new ArrayList<>();
    void loadModle() throws JSONException {
        final String contentType = "application/x-www-form-urlencoded";
        final String url = UserBaseDatus.getInstance().url + "api/app/allocation/getAllocationDetail";
        final String data = "signa=" + UserBaseDatus.getInstance().getSign() + "&&allocationId=" + id;
        Map map = UserBaseDatus.getInstance().isSuccessPost(url, data, contentType);
        if ((boolean) (map.get("isSuccess"))) {
            JSONObject jsonObject=(JSONObject) map.get("json");

            JSONObject json1=jsonObject.getJSONObject("data");


            JSONObject json=json1.getJSONObject("allocationVo");
            busNo=json.getString("allocationNo");
            busTime=json.getString("busTime");
            leader=json.getString("leader");
            otherAmount=json.getString("otherAmount");
            remarks=json.getString("remarks");
            busScope=json.getString("busScope");
            createTime=json.getString("createTime");
            createBy=json.getString("createBy");
            refunDepotName=json.getString("refundDepotName");
            buyDepotName=json.getString("buyDepotName");




            JSONArray jsonArray= json1.getJSONArray("allocationDetailList");
            for (int i=0;i<jsonArray.length();i++){
                JSONObject object=jsonArray.getJSONObject(i);
                String proName=object.getString("proName");
                String norms =object.getString("norms");
                String property =object.getString("property");

                int total=object.getInt("total");
                String unit=object.getString("unit");
                ShangpinBaseDatus datus=new ShangpinBaseDatus();
                datus.setProName(proName);
                datus.setNorms(norms);
                datus.setProperty(property);
                datus.setTotal(total);
                datus.setUnit(unit);
                lists.add(datus);
            }



            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    loadListView();
                    loadView(busNo,busTime,leader,otherAmount,remarks,busScope,createTime,createBy,refunDepotName,buyDepotName);
                }
            });

            getJsonIds();

        }

    }


    private void loadView(String busNo, String busTime,
                          String leader, String otherAmount, String remarks,
                          String busScope, String createTime,String createBy,String refunDepotName,String buyDepotName) {
        danhaoText.setText(busNo);
        yewuriqiText.setText(busTime);
        leaderText.setText(leader);
        otherCaseText.setText("￥"+otherAmount);
        remarksText.setText(remarks);
        jiesuanzhanghuText.setText(busScope);
        zhidanriqiText.setText(createTime);
        createByText.setText(createBy);
        refundDepotNameText.setText(refunDepotName);
        buyDepotNameText.setText(buyDepotName);
    }



    List<String> jsonIds=new ArrayList<>();
    void getJsonIds(){
        final String contentType = "application/x-www-form-urlencoded";
        final String url = UserBaseDatus.getInstance().url + "api/app/upload/list";
        final String data = "signa=" + UserBaseDatus.getInstance().getSign() + "&&busNo=" + busNo;
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

        adapter=new CommonAdapter<ShangpinBaseDatus>(DiaobodanxiangqingActivity.this,lists, R.layout.item_diaoboxiangqing_shangpin) {

            @Override
            public void convert(final ViewHolder holder, final ShangpinBaseDatus shangpinDatus, final int position, View convertView) {
                holder.setText(R.id.proName,shangpinDatus.getProName()+" "+shangpinDatus.getNorms()+" "+shangpinDatus.getProperty());
                holder.setText(R.id.diaoboCount,shangpinDatus.getTotal()+shangpinDatus.getUnit());


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
        dialog = new Dialog(DiaobodanxiangqingActivity.this, R.style.AlertDialog_AppCompat_Light);
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
