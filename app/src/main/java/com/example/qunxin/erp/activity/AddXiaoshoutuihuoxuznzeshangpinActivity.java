package com.example.qunxin.erp.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

public class AddXiaoshoutuihuoxuznzeshangpinActivity extends AppCompatActivity {

    ListView shangpinTypeListView;
    ListView shangpinsListView;

    Button selectBtn;
    Button enterBtn;
    View backBtn;


    RelativeLayout container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_xuanzeshangpin);
        initView();
    }


    boolean isAuto;
    void initView(){

        shangpinTypeListView=findViewById(R.id.add_caigoudingdan_shagnpinType);
        shangpinsListView=findViewById(R.id.add_caigoudingdan_shangpins_listView);
        container=findViewById(R.id.container);

        selectBtn=findViewById(R.id.select);
        enterBtn=findViewById(R.id.enter);
        backBtn=findViewById(R.id.back);


        Intent intent=getIntent();
        cust=intent.getStringExtra("cust");
        depot=intent.getStringExtra("depot");


        custName=intent.getStringExtra("custName");
        depotName=intent.getStringExtra("depotName");
        busNo=intent.getStringExtra("busNo");

        Log.d("depotName", depotName);


        isAuto=intent.getBooleanExtra("isAuto",false);


        new Thread(new Runnable() {
            @Override
            public void run() {
                loadModelType();
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                loadModelShangpin();
            }
        }).start();


        enterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isAuto==false){
                    float sum=enter();

                    Intent intent=new Intent(AddXiaoshoutuihuoxuznzeshangpinActivity.this,AddXiaoshoutuihuodanAfterActivity.class);

                    intent.putExtra("cust",cust);
                    intent.putExtra("depot",depot);
                    intent.putExtra("custName",custName);
                    intent.putExtra("depotName",depotName);
                    intent.putExtra("price",sum);
                    intent.putExtra("count",caculateCount());
                    intent.putExtra("lists",listToJson());
                    intent.putExtra("busNo",busNo);
                    intent.putExtra("isGuanLianJinhuodan",false);
                    startActivity(intent);
                    finish();
                }else {
                    Intent intent=new Intent();
                    float sum=enter();
                    intent.putExtra("price",sum);
                    intent.putExtra("count",caculateCount());
                    intent.putExtra("lists",listToJson());

                    setResult(1,intent);
                    finish();
                }


            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                /**
                 *要执行的操作
                 */
                typeNameLists.get(0).getTextView().setTextColor(Color.parseColor("#FF5FC6FD"));

            }
        }, 200);//3秒后执行Runnable中的run方法
    }

    float sum=0;
    float enter(){
        for(int i=0;i<lists.size();i++){
            sum+=lists.get(i).getTotal()*lists.get(i).getPrice();
        }
        return sum;
    }


    String toStringJson()  {

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



    String listToJson(){
        StringBuffer buffer=new StringBuffer();
        for (int i=0;i<lists.size();i++){
            JSONObject jsonObject=new JSONObject();
            try {
                if(lists.get(i).getTotal()==0) break;

//                jsonObject.put("proId",lists.get(i).getProId());
//                jsonObject.put("remarks",lists.get(i).getRemarks());
//                jsonObject.put("price",lists.get(i).getPrice());
//                jsonObject.put("total",lists.get(i).getTotal());

                jsonObject.put("buyPrice",lists.get(i).getPrice());
                jsonObject.put("createBy",UserBaseDatus.getInstance().userId);
                jsonObject.put("id",lists.get(i).getId());
                jsonObject.put("proName",lists.get(i).getProName());
                jsonObject.put("proNo",lists.get(i).getProNo());
                jsonObject.put("property",lists.get(i).getProperty());
                jsonObject.put("norms",lists.get(i).getNorms());
                jsonObject.put("rejectId","");
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



    void loadModelType(){

        AddXiaoshoutuihuoxuznzeshangpinActivity.TypeNameClass typeNameClass=new AddXiaoshoutuihuoxuznzeshangpinActivity.TypeNameClass();
        typeNameClass.setTypeName("所有");
        typeNameClass.setId("");
        typeNameLists.add(typeNameClass);
        final String contentType = "application/x-www-form-urlencoded";

        final String url= UserBaseDatus.getInstance().url+"api/app/proType/list";
        final String data="signa="+UserBaseDatus.getInstance().getSign();
        Map map=UserBaseDatus.getInstance().isSuccessPost(url, data, contentType);
        if((boolean)(map.get("isSuccess"))){
            JSONObject json=(JSONObject) map.get("json");
            try {
                JSONArray jsonArray=json.getJSONArray("data");
                for (int i=0;i<jsonArray.length();i++){
                    JSONObject jsonObject=jsonArray.getJSONObject(i);
                    String typeName=jsonObject.getString("typeName");
                    String id=jsonObject.getString("id");

                    AddXiaoshoutuihuoxuznzeshangpinActivity.TypeNameClass typeNameClass1=new AddXiaoshoutuihuoxuznzeshangpinActivity.TypeNameClass();
                    typeNameClass1.setTypeName(typeName);
                    typeNameClass1.setId(id);
                    typeNameLists.add(typeNameClass1);



                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        loadViewType();
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    //左边分类
    List<AddXiaoshoutuihuoxuznzeshangpinActivity.TypeNameClass> typeNameLists=new ArrayList<>();
    class TypeNameClass{

        String typeName;
        String id;
        TextView textView;

        public TextView getTextView() {
            return textView;
        }

        public void setTextView(TextView textView) {
            this.textView = textView;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTypeName() {
            return typeName;
        }

        public void setTypeName(String typeName) {
            this.typeName = typeName;
        }
    }
    boolean isClick=true;
    void loadViewType(){




        shangpinTypeListView.setAdapter(new CommonAdapter<AddXiaoshoutuihuoxuznzeshangpinActivity.TypeNameClass>(AddXiaoshoutuihuoxuznzeshangpinActivity.this,typeNameLists, R.layout.item_shangpintype) {

            @Override
            public void convert(final ViewHolder holder, final AddXiaoshoutuihuoxuznzeshangpinActivity.TypeNameClass typeName, final int position, View convertView) {

                final TextView textView=holder.getView(R.id.txt);
                textView.setText(typeName.getTypeName());
                typeName.setTextView(textView);

                holder.setOnClickListener(R.id.txt, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        type=typeNameLists.get(position).getId();
                        lists.clear();
                        if(isClick==false) return;
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                isClick=false;
                                loadModelShangpin();
                            }
                        }).start();

                        for(int i=0;i<typeNameLists.size();i++){
                            if(i==position){
                                typeNameLists.get(i).getTextView().setTextColor(Color.parseColor("#FF5FC6FD"));
                            }else {
                                typeNameLists.get(i).getTextView().setTextColor(Color.parseColor("#FFD2D2D2"));
                            }
                        }
                    }
                });

            }
        });



    }




    String custName="";
    String depotName="";
    String cust;
    String depot;
    String pageSize="10";
    String pageNum="1";
    String type="";
    String busNo;

    List<ShangpinBaseDatus> lists=new ArrayList<>();
    void loadModelShangpin(){
        String signa=UserBaseDatus.getInstance().getSign();

        final String contentTypeList = "application/x-www-form-urlencoded";
        final String url= UserBaseDatus.getInstance().url+"api/app/sellRecord/getSellRecordList";


        String jsonString="custId="+cust+"&&depot="+depot+"&&signa="+signa;

        Log.d("jsonString", jsonString);
        Map map=UserBaseDatus.getInstance().isSuccessPost(url, jsonString, contentTypeList);
        if((boolean)(map.get("isSuccess"))){

            JSONObject json=(JSONObject) map.get("json");
            try {
                JSONArray jsonArray=json.getJSONArray("data");
                for (int i=0;i<jsonArray.length();i++){
                   JSONObject object=jsonArray.getJSONObject(i);

                    String proName=object.getString("proName");

                    int total=object.getInt("total");
                    int rejectTotal=object.getInt("rejectTotal");
                    int maxTotal=total-rejectTotal;
                    String unit=object.getString("unit");
                    String proId=object.getString("proId");
                    String property=object.getString("property");
                    String norms=object.getString("norms");
                    String proNo=object.getString("proNo");
                    String totalAmount=object.getString("totalAmount");

                    String id=object.getString("id");
                    ShangpinBaseDatus datus=new ShangpinBaseDatus();
                    datus.setProName(proName);
                    //datus.setTotal(total);
                    datus.setTotalAmount(Float.parseFloat(totalAmount));
                    datus.setKucunCount(total);
                    datus.setTuihuoCount(total);
                    datus.setUnit(unit);
                    datus.setProId(proId);
                    datus.setProperty(property);
                    datus.setProNo(proNo);
                    datus.setNorms(norms);
                    datus.setId(id);
                    datus.setMaxTotal(maxTotal);
                    lists.add(datus);
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        loadViewShangpin();
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }




    ImageView nodatusImage;
    void loadViewShangpin(){

        if(lists.size()==0){
            if(nodatusImage==null){
                nodatusImage=new ImageView(this);
            }
            nodatusImage.setImageResource(R.drawable.nodatus);
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(300, 300);

            layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
            layoutParams.topMargin=30;
            nodatusImage.setLayoutParams(layoutParams);
            if(nodatusImage.getParent()==null){
                container.addView(nodatusImage);
            }
        }else {
            container.removeView(nodatusImage);
        }

        shangpinsListView.setAdapter(new CommonAdapter<ShangpinBaseDatus>(AddXiaoshoutuihuoxuznzeshangpinActivity.this,lists, R.layout.item_addcaigoudingdan_xuanzeshangpin) {

            @Override
            public void convert(final ViewHolder holder, ShangpinBaseDatus shangpinDatus, final int position, View convertView) {

                final int[] amount = {0};
                holder.setText(R.id.name,shangpinDatus.getProName());
                holder.setText(R.id.price,shangpinDatus.getPrice()+"");
                final TextView etAmount=holder.getView(R.id.etAmount);
                holder.setOnClickListener(R.id.ll_content, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        ShangpinBaseDatus datus=lists.get(position);
                        String id=datus.getProId();
                        TextView etAmount=holder.getView(R.id.etAmount);
                        TextView price=holder.getView(R.id.price);

                        datus.setTotal(Integer.parseInt(etAmount.getText().toString()));

                        show(datus.getProName(),datus.getPrice()+"", position,etAmount,price);
                    }
                });

                holder.setOnClickListener(R.id.btnIncrease, new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        amount[0]=Integer.parseInt(etAmount.getText().toString());

                        amount[0]++;
                        holder.setText(R.id.etAmount,  amount[0] +"");
                        ShangpinBaseDatus datus=lists.get(position);
                        String id=datus.getProId();


                        datus.setTotal(Integer.parseInt(etAmount.getText().toString()));
                        datus.setTotalAmount(datus.getPrice()*datus.getTotal());
                        TextChange();
                        etAmount.clearFocus();

                    }
                });

                holder.setOnClickListener(R.id.btnDecrease, new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        amount[0]=Integer.parseInt(etAmount.getText().toString());

                        if(amount[0] <=0){
                            return;
                        }
                        amount[0]--;
                        holder.setText(R.id.etAmount, amount[0] +"");
                        ShangpinBaseDatus datus=lists.get(position);

                        datus.setTotal(Integer.parseInt(etAmount.getText().toString()));
                        datus.setTotalAmount(datus.getTotal()*datus.getPrice());
                        TextChange();
                        etAmount.clearFocus();


                    }
                });

                etAmount.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        String string=etAmount.getText().toString();
                        if("".equals(string)) return;
                        amount[0]=Integer.parseInt(etAmount.getText().toString());

                        ShangpinBaseDatus datus=lists.get(position);
                        if(amount[0]>datus.getMaxTotal()){
                            amount[0]=datus.getMaxTotal();
                            holder.setText(R.id.etAmount, amount[0] +"");
                            Toast.makeText(mContext, "已经设置成最大退货数量", Toast.LENGTH_SHORT).show();
                        }
                        datus.setTotal(Integer.parseInt(etAmount.getText().toString()));
                        TextChange();


                    }
                });


            }
        });
        isClick=true;
    }

    Dialog dialog;




    public void show(String name, final String price, final int position,final TextView countText, final TextView danjia){
        dialog = new Dialog(this,R.style.ActionSheetDialogStyle);
        //填充对话框的布局
        LinearLayout inflate =(LinearLayout) LayoutInflater.from(this).inflate(R.layout.bottom_dialog_shangpin, null);
        //初始化控件
        TextView nameText = (TextView) inflate.findViewById(R.id.name);
        TextView priceText = (TextView) inflate.findViewById(R.id.price);
        final TextView danjiaText = (TextView) inflate.findViewById(R.id.danjia);
        final TextView danjiaText_qianmian=inflate.findViewById(R.id.danjiaText_qianmian);
        danjiaText_qianmian.setText("销售价");
        final TextView countTextView = (TextView) inflate.findViewById(R.id.count);

        View finishBtn=inflate.findViewById(R.id.finish);
        nameText.setText(name);
        priceText.setText(price);

        //将布局设置给Dialog
        dialog.setContentView(inflate);
        //获取当前Activity所在的窗体
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
        lp.height = inflate.getMeasuredHeight();

        lp.alpha = 9f; // 透明度
        dialogWindow.setAttributes(lp);
        dialogWindow.setAttributes(lp);


        finishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if("".equals(countTextView.getText().toString())){
                    return;
                }
                if("".equals(danjiaText.getText().toString())){
                    return;
                }

                int count=Integer.parseInt(countTextView.getText().toString());



                danjia.setText(danjiaText.getText().toString());
                int priceDanjia=Integer.parseInt(danjiaText.getText().toString());
                ShangpinBaseDatus datus=lists.get(position);


                if(count>datus.getMaxTotal()){
                    count=datus.getMaxTotal();
                    Toast.makeText(AddXiaoshoutuihuoxuznzeshangpinActivity.this, "已经设置成最大退货数量", Toast.LENGTH_SHORT).show();
                }


                datus.setPrice(priceDanjia);
                datus.setTotal(count);
                datus.setTotalAmount(datus.getPrice()*datus.getTotal());

                countText.setText(count+"");

                TextChange();
                dialog.dismiss();
            }
        });
        dialog.show();//显示对话框
    }


    void TextChange(){
        int sumCount= caculateCount();
        selectBtn.setText("已选商品（"+sumCount+"）件");
        if(sumCount<=0){
            enterBtn.setEnabled(false);
        }else {
            enterBtn.setEnabled(true);
        }
    }

    int caculateCount(){
        int sumCount=0;
        for (int i=0;i<lists.size();i++){
            sumCount+=lists.get(i).getTotal();
        }
        return sumCount;
    }
}
