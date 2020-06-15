package com.example.qunxin.erp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import com.example.qunxin.erp.R;
import com.example.qunxin.erp.UserBaseDatus;
import com.example.qunxin.erp.activity.AddCaigoudingdanActivity;
import com.example.qunxin.erp.activity.AddCangkuActivity;
import com.example.qunxin.erp.activity.AddDiaobodanActivity;
import com.example.qunxin.erp.activity.AddGongyingshangActivity;
import com.example.qunxin.erp.activity.AddJinhuotuihuodanActivity;
import com.example.qunxin.erp.activity.AddKuaijiegongnengActivity;
import com.example.qunxin.erp.activity.AddKehuActivity;
import com.example.qunxin.erp.activity.AddShangpinActivity;
import com.example.qunxin.erp.activity.AddXiaoshoudanActivity;
import com.example.qunxin.erp.activity.AddXiaoshoudingdanActivity;
import com.example.qunxin.erp.activity.AddXiaoshoutuihuodanActivity;
import com.example.qunxin.erp.activity.AddZhanghuActivity;
import com.example.qunxin.erp.activity.AddjinhuoActivity;
import com.example.qunxin.erp.activity.AddpandianActivity;
import com.example.qunxin.erp.activity.CaigoudingdanActivity;
import com.example.qunxin.erp.activity.CangkuliebiaoActivity;
import com.example.qunxin.erp.activity.DiaobolishiActivity;
import com.example.qunxin.erp.activity.JiesuanzhanghuActivity;
import com.example.qunxin.erp.activity.JinhuolishiActivity;
import com.example.qunxin.erp.activity.JinhuotuihuolishiActivity;
import com.example.qunxin.erp.activity.KucunchaxunActivity;
import com.example.qunxin.erp.activity.MainActivity;
import com.example.qunxin.erp.activity.PandianlishiActivity;
import com.example.qunxin.erp.activity.QichuInfoActivity;
import com.example.qunxin.erp.activity.RukulishiActivity;
import com.example.qunxin.erp.activity.ShangpinliebiaoActivity;
import com.example.qunxin.erp.activity.XiaoshoudingdanlishiActivity;
import com.example.qunxin.erp.activity.XiaoshoulishiActivity;
import com.example.qunxin.erp.activity.XiaoshoutuihuolishiActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import swipedelete.utils.CommonAdapter;
import swipedelete.utils.ViewHolder;
import swipedelete.view.SwipeMenuLayout;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by qunxin on 2019/7/31.
 */

public class IndexFragment extends Fragment {


    TextView shishoujineText;
    TextView xiaoshoubishuText;
    TextView xiaoshoueText;
    View addkuaijiegongneng;
    ListView listView;

    Switch switchBtn;

    int[] images;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_index, container, false);

        shishoujineText = view.findViewById(R.id.shishoue);
        xiaoshoubishuText = view.findViewById(R.id.xiaoshoubishu);
        xiaoshoueText=view.findViewById(R.id.xiaoshoue);
        listView=view.findViewById(R.id.listView);

        images=new int[]{R.drawable.caigou,R.drawable.jinhuotuihuo,R.drawable.jinhuolishi,R.drawable.ruku,
        R.drawable.xiaoshoudingdan,R.drawable.xiaoshoulishi,R.drawable.xiaoshoutuihuolishi,R.drawable.kucunpandian,
        R.drawable.tiaobodan,R.drawable.kucunchaxun,R.drawable.kehuicon,R.drawable.gongyingshangicon,
                R.mipmap.shangpinliebiao, R.mipmap.jiesuanzhanghu,R.mipmap.cangkuicon,R.mipmap.qicuxinxi};





        switchBtn=view.findViewById(R.id.switchBtn);
        switchBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                    shishoujineText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());//显示密码
                    xiaoshoubishuText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());//显示密码

                    xiaoshoueText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());//显示密码


                } else {

                    shishoujineText.setTransformationMethod(PasswordTransformationMethod.getInstance());//隐藏密码
                    xiaoshoubishuText.setTransformationMethod(PasswordTransformationMethod.getInstance());//隐藏密码
                    xiaoshoueText.setTransformationMethod(PasswordTransformationMethod.getInstance());//隐藏密码

                }

            }
        });

        addkuaijiegongneng=view.findViewById(R.id.addkuaijiegongneng);
        addkuaijiegongneng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(),AddKuaijiegongnengActivity.class);
                startActivityForResult(intent,1);
            }
        });

        dataStr=readTxt();


        new Thread(new Runnable() {
            @Override
            public void run() {
                caculate();

            }
        }).start();


        return view;

    }




    String readTxt(){
        String content="";

        try {

            FileInputStream fis=getActivity().openFileInput("data.txt");

            byte [] buffer=new  byte[fis.available()];

            fis.read(buffer);

            content=new String(buffer);

            fis.close();

        } catch (Exception e) {

            e.printStackTrace();

        }
        return content;
    }

    String dataStr="";


    @Override
    public void onResume() {
        super.onResume();

        loadModel();
        new Thread(new Runnable() {
            @Override
            public void run() {
                caculate();
            }
        }).start();
    }


    class DataModel{
        String name="";
        String info="";
        int image=-1;
    }

    List<DataModel> lists=new ArrayList<>();
    void loadModel(){
        lists.clear();
        dataStr=readTxt();
        if("".equals(dataStr)){
            return;
        }
        try {
            JSONArray jsonArray=new JSONArray(dataStr);
            for (int i=0;i<jsonArray.length();i++){
                JSONObject jsonObject=jsonArray.getJSONObject(i);
                String name=jsonObject.getString("name");
                String info=jsonObject.getString("info");
                int image=jsonObject.getInt("image");
                DataModel dataModel=new DataModel();
                dataModel.image=image;
                dataModel.name=name;
                dataModel.info=info;
                lists.add(dataModel);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }



        loadView();
    }

    void loadView(){

        listView.setAdapter(new CommonAdapter<DataModel>(getContext(),lists) {
            @Override
            public int getItemViewType(int position) {
                if(lists.get(position).image==3||lists.get(position).image==9){
                    return NO_ADD;
                }else {
                    return ADD;
                }
            }

            @Override
            public int getViewTypeCount() {
                return 2;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                int type=getItemViewType(position);

                if(type==0){
                    ViewHolder holder = ViewHolder.get(mContext, convertView, parent,
                            R.layout.item_index, position);
                    convert(holder, getItem(position), position, convertView);
                    return holder.getConvertView();

                }else {
                    ViewHolder holder = ViewHolder.get(mContext, convertView, parent,
                            R.layout.item_index_no_add, position);
                    convert(holder, getItem(position), position, convertView);
                    return holder.getConvertView();
                }

            }

            @Override
            public void convert(final ViewHolder holder, DataModel dataModel, final int position, View convertView) {
                holder.setText(R.id.listview_tv,dataModel.name);
                holder.setImageResource(R.id.listview_iv,images[dataModel.image]);
                holder.setText(R.id.listview_tvInfo,dataModel.info);
                //可以根据自己需求设置一些选项(这里设置了IOS阻塞效果以及item的依次左滑、右滑菜单)
                ((SwipeMenuLayout)holder.getConvertView()).setIos(true).setLeftSwipe(true);
                //监听事件
                holder.setOnClickListener(R.id.ll_content, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DataModel dataModel1=lists.get(position);
                        tiaozhuanIntent(dataModel1.image);
                    }
                });


                int type=getItemViewType(position);
                if(type==0){
                    holder.setOnClickListener(R.id.add, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DataModel dataModel1=lists.get(position);
                            tiaozhuanToAddIntent(dataModel1.image);
                        }
                    });
                }

                holder.setOnClickListener(R.id.btn_zd, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Toast.makeText(MainActivity.this,"点击了置顶选项", Toast.LENGTH_SHORT).show();
                        //在ListView里，点击侧滑菜单上的选项时，如果想让侧滑菜单同时0关闭，调用这句话
                        ((SwipeMenuLayout) holder.getConvertView()).quickClose();
                        DataModel dataModel1=lists.get(position);
                        lists.add(0,dataModel1);
                        lists.remove(position+1);
                        notifyDataSetChanged();
                        saveTxt();
                    }
                });
                holder.setOnClickListener(R.id.btn_delete, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Toast.makeText(MainActivity.this,"点击了删除选项", Toast.LENGTH_SHORT).show();
                        //在ListView里，点击侧滑菜单上的选项时，如果想让侧滑菜单同时关闭，调用这句话
                        ((SwipeMenuLayout) holder.getConvertView()).quickClose();
                        //删除操作
                        lists.remove(position);

                        notifyDataSetChanged();
                        saveTxt();
                    }
                });
            }
        });

    }


    final int ADD=0;
    final int NO_ADD=1;



    String getDataStr(){
        StringBuffer buffer=new StringBuffer();
        for (int i=0;i<lists.size();i++){
            DataModel dataModel=lists.get(i);
            int image=dataModel.image;
            String name=dataModel.name;
            String info=dataModel.info;

            JSONObject jsonObject=new JSONObject();
            try {
                jsonObject.put("name",name);
                jsonObject.put("info",info);
                jsonObject.put("image",image);
                String jsonStr=jsonObject.toString();
                buffer.append(jsonStr).append(",");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return "["+buffer.toString().substring(0,buffer.toString().length()-1)+"]";

    }



    void saveTxt(){
        String saveInfo=getDataStr();

        FileOutputStream fos;

        try {

            fos=getActivity().openFileOutput("data.txt",MODE_PRIVATE);//把文件输出到data中

            fos.write(saveInfo.getBytes());//将我们写入的字符串变成字符数组）

            fos.close();

        } catch (Exception e) {

            e.printStackTrace();

        }
    }


    private void tiaozhuanIntent(int image) {
        switch (image){
            case 0:
                Intent intent0=new Intent(getContext(),CaigoudingdanActivity.class);
                startActivity(intent0);
                break;
            case 1:
                Intent intent1=new Intent(getContext(),JinhuotuihuolishiActivity.class);
                startActivity(intent1);
                break;
            case 2:
                Intent intent2=new Intent(getContext(),JinhuolishiActivity.class);
                startActivity(intent2);
                break;
            case 3:
                Intent intent3=new Intent(getContext(),RukulishiActivity.class);
                startActivity(intent3);
                break;
            case 4:
                Intent intent4=new Intent(getContext(),XiaoshoudingdanlishiActivity.class);
                startActivity(intent4);
                break;
            case 5:
                Intent intent5=new Intent(getContext(),XiaoshoulishiActivity.class);
                startActivity(intent5);
                break;
            case 6:
                Intent intent6=new Intent(getContext(),XiaoshoutuihuolishiActivity.class);
                startActivity(intent6);
                break;
            case 7:
                Intent intent7=new Intent(getContext(),PandianlishiActivity.class);
                startActivity(intent7);
                break;
            case 8:
                Intent intent8=new Intent(getContext(),DiaobolishiActivity.class);
                startActivity(intent8);
                break;
            case 9:
                Intent intent9=new Intent(getContext(),KucunchaxunActivity.class);
                startActivity(intent9);
                break;
            case 10:
                MainActivity.instance.guankeFun();
                break;
            case 11:
                MainActivity.instance.gongyingshangFun();

                break;
            case 12:
                Intent intent12=new Intent(getContext(),ShangpinliebiaoActivity.class);
                startActivity(intent12);
                break;
            case 13:
                Intent intent13=new Intent(getContext(),JiesuanzhanghuActivity.class);
                startActivity(intent13);
                break;
            case 14:
                Intent intent14=new Intent(getContext(),CangkuliebiaoActivity.class);
                startActivity(intent14);
                break;
            case 15:
                Intent intent15=new Intent(getContext(),QichuInfoActivity.class);
                startActivity(intent15);
                break;

        }
    }



    private void tiaozhuanToAddIntent(int image) {
        switch (image){
            case 0:
                Intent intent0=new Intent(getContext(),AddCaigoudingdanActivity.class);
                startActivity(intent0);
                break;
            case 1:
                Intent intent1=new Intent(getContext(),AddJinhuotuihuodanActivity.class);
                startActivity(intent1);
                break;
            case 2:
                Intent intent2=new Intent(getContext(),AddjinhuoActivity.class);
                startActivity(intent2);
                break;
            case 3:

                break;
            case 4:
                Intent intent4=new Intent(getContext(),AddXiaoshoudingdanActivity.class);
                startActivity(intent4);
                break;
            case 5:
                Intent intent5=new Intent(getContext(),AddXiaoshoudanActivity.class);
                startActivity(intent5);
                break;
            case 6:
                Intent intent6=new Intent(getContext(),AddXiaoshoutuihuodanActivity.class);
                startActivity(intent6);
                break;
            case 7:
                Intent intent7=new Intent(getContext(),AddpandianActivity.class);
                startActivity(intent7);
                break;
            case 8:
                Intent intent8=new Intent(getContext(),AddDiaobodanActivity.class);
                startActivity(intent8);
                break;
            case 9:

                break;
            case 10:
                Intent intent10=new Intent(getContext(),AddKehuActivity.class);
                startActivity(intent10);
                break;
            case 11:
                Intent intent11=new Intent(getContext(),AddGongyingshangActivity.class);
                startActivity(intent11);
                break;
            case 12:
                Intent intent12=new Intent(getContext(),AddShangpinActivity.class);
                startActivity(intent12);
                break;
            case 13:
                Intent intent13=new Intent(getContext(),AddZhanghuActivity.class);
                startActivity(intent13);
                break;
            case 14:
                Intent intent14=new Intent(getContext(),AddCangkuActivity.class);
                startActivity(intent14);
                break;
            case 15:

                break;

        }
    }



    String valueOfSales="";
    String actualPayment="";
    String salesPens="";
    void caculate(){
        final String strUrl = UserBaseDatus.getInstance().url+"api/sells/selectSellReport";

        final String strData = "sign="+UserBaseDatus.getInstance().getSign();
        Log.d("strData", strData);
        final String contentType = "application/x-www-form-urlencoded";
        //final String contentType = "application/json";

        Map map=UserBaseDatus.getInstance().isSuccessPost(strUrl, strData, contentType);
        if ((boolean)(map.get("isSuccess"))) {
            JSONObject jsonObject= (JSONObject) map.get("json");
            try {
                JSONObject json=jsonObject.getJSONObject("data");

                valueOfSales=json.getString("valueOfSales");
                actualPayment=json.getString("actualPayment");
                salesPens=json.getString("salesPens");

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        xiaoshoubishuText.setText(salesPens);
                        shishoujineText.setText(actualPayment);
                        xiaoshoueText.setText(valueOfSales);
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


}

