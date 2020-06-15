package com.example.qunxin.erp.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
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

public class CaigoudingdanxiangqingWeicaigouActivity extends AppCompatActivity {


    View backBtn;

    TextView danhaoText;
    TextView yewuriqiText;
    TextView supplierNameText;


    ImageView imageView;
    TextView remarksText;
    TextView zhidanriqiText;
    TextView createByText;


    ListView listView;
    View closeBtn;
    View editBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caigoudingdanxiangqing_weicaigou);
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

        final View view = LayoutInflater.from(CaigoudingdanxiangqingWeicaigouActivity.this).inflate(R.layout.edit_popwindow, null);

        View enterBtn =  view.findViewById(R.id.enter);

        final PopupWindow window = new PopupWindow(view, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT, true);


        enterBtn.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View v) {
                purchaseEnter();
                window.dismiss();
                finish();


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


    void purchaseEnter(){
        Intent intent=new Intent(CaigoudingdanxiangqingWeicaigouActivity.this,AddCaigoudanEnterActivity.class);

        intent.putExtra("supplierName",supplierName);
        intent.putExtra("supplier",supplier);
        intent.putExtra("busNo",danhaoText.getText().toString());
        intent.putExtra("count",total);
        intent.putExtra("zhidanriqi",zhidanriqiText.getText().toString());
        intent.putExtra("createBy",createBy);
        intent.putExtra("purchaseDetailVoList",jsonToString());
        intent.putExtra("id",id1);
        if(jsonIds.size()>0) {
            intent.putExtra("jsonId",jsonIds.get(jsonIds.size()-1));
        }

        startActivity(intent);
    }


    void initModel(){
        Intent intent=getIntent();
        id=intent.getStringExtra("id");
        supplierName=intent.getStringExtra("supplierName");
        Log.d("id111", id);

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


    String id1;

    List<ShangpinBaseDatus> lists=new ArrayList<>();
    void loadModle() throws JSONException {
        final String contentType = "application/x-www-form-urlencoded";
        final String url = UserBaseDatus.getInstance().url + "api/app/purchases/selectDetail";
        final String data = "signa=" + UserBaseDatus.getInstance().getSign() + "&&purchaseId=" + id;
        Log.d("purchaseId", id);
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
            id1=json.getString("id");

            JSONArray jsonArray= json1.getJSONArray("purchaseDetails");
            for (int i=0;i<jsonArray.length();i++){
                JSONObject object=jsonArray.getJSONObject(i);
                String proName=object.getString("proName");
                int total=object.getInt("orderQuantity");
                String unit=object.getString("unit");
                String proId=object.getString("proId");
                String remarks=object.getString("remarks");
                String norms=object.getString("norms");
                String id=object.getString("id");
                String purchaseId=object.getString("purchaseId");
                ShangpinBaseDatus datus=new ShangpinBaseDatus();

                datus.setProName(proName);
                datus.setTotal(total);
                datus.setUnit(unit);
                datus.setProId(proId);
                datus.setRemarks(remarks);
                datus.setNorms(norms);
                datus.setPurchaseId(purchaseId);
                datus.setId(id);

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
        Log.d("buyNo", purchaseNo);
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

        adapter=new CommonAdapter<ShangpinBaseDatus>(CaigoudingdanxiangqingWeicaigouActivity.this,lists, R.layout.item_jinhuoxiangqing_shangpin) {

            @Override
            public void convert(final ViewHolder holder, final ShangpinBaseDatus shangpinDatus, final int position, View convertView) {
                holder.setText(R.id.name,shangpinDatus.getProName());

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
        dialog = new Dialog(CaigoudingdanxiangqingWeicaigouActivity.this, R.style.AlertDialog_AppCompat_Light);
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




    String jsonToString()  {
        StringBuffer buffer=new StringBuffer();

        for (int i=0;i<lists.size();i++){
            JSONObject jsonObject=new JSONObject();
            try {
                if(lists.get(i).getTotal()==0) break;

                jsonObject.put("proName",lists.get(i).getProName());
                jsonObject.put("proId",lists.get(i).getProId());
                jsonObject.put("norms", lists.get(i).getNorms());
                jsonObject.put("property",lists.get(i).getProperty());
                jsonObject.put("remarks",lists.get(i).getRemarks());
                jsonObject.put("unit",lists.get(i).getUnit());
                jsonObject.put("total",lists.get(i).getTotal());
                jsonObject.put("id",lists.get(i).getId());
                jsonObject.put("purchaseId",lists.get(i).getPurchaseId());

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
