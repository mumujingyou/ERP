package com.example.qunxin.erp.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.qunxin.erp.R;
import com.example.qunxin.erp.UserBaseDatus;
import com.example.qunxin.erp.modle.KehuDatus;
import com.example.qunxin.erp.modle.ShangpinBaseDatus;
import com.example.qunxin.erp.modle.ShangpinDatus;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import swipedelete.utils.CommonAdapter;
import swipedelete.utils.ViewHolder;
import swipedelete.view.SwipeMenuLayout;

public class KucunchanxunXuanzeshangpinActivity extends AppCompatActivity implements AbsListView.OnScrollListener {

    ListView listView;

    TextView searchText;
    View backBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kucunchanxun_xuanzeshangpin);


        final Intent intent=getIntent();
        depot=intent.getStringExtra("depot");


        nodatusImage=findViewById(R.id.nodatusImage);

        listView=findViewById(R.id.listView);
        new Thread(new Runnable() {
            @Override
            public void run() {
                loadModel(index);
            }
        }).start();


        searchText=findViewById(R.id.search);
        backBtn=findViewById(R.id.back);
        listView.setOnScrollListener(this);


        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        searchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override

            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_SEARCH){

                    search(searchText.getText().toString());
                    return true;
                }
                return false;

            }
        });

        searchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                search(searchText.getText().toString());
            }
        });

    }



    String searchValue="";
    String kehuName="";
    String depotName="";
    String depot;
    String pageSize="10";

    String type="";

    List<ShangpinBaseDatus> lists=new ArrayList<>();

    void loadModel(int index){
        String signa=UserBaseDatus.getInstance().getSign();

        final String contentTypeList = "application/x-www-form-urlencoded";
        final String url= UserBaseDatus.getInstance().url+"api/app/proProduct/getBusinessProProductList";


        String jsonString="depot="+depot+"&&signa="+signa+"&&pageSize="+pageSize+"&&pageNum="+index+
                "&&type="+type+"&&searchValue="+searchValue;

        Log.d("jsonString", jsonString);
        Map map=UserBaseDatus.getInstance().isSuccessPost(url, jsonString, contentTypeList);
        if((boolean)(map.get("isSuccess"))){

            JSONObject json=(JSONObject) map.get("json");
            try {
                JSONObject jsonData=json.getJSONObject("data");
                final JSONArray jsonArray=jsonData.getJSONArray("rows");
                for (int i=0;i<jsonArray.length();i++){
                    JSONObject jsonObject=jsonArray.getJSONObject(i);
                    String typeName=jsonObject.getString("proName");

                    String price=jsonObject.getString("price");

                    String id=jsonObject.getString("id");

                    String proNo=jsonObject.getString("proNo");

                    String norms=jsonObject.getString("norms");
                    String property=jsonObject.getString("property");
                    String unit=jsonObject.getString("unit");

                    String depot=jsonObject.getString("depot");

                    String maxTotalStr=jsonObject.getString("stockTotal");

                    if(maxTotalStr=="null") continue;

                    int maxTotal=Integer.parseInt(maxTotalStr);
                    if(depot.equals("null")){
                        continue;
                    }

                    if(maxTotal==0){
                        continue;
                    }

                    ShangpinBaseDatus datus = new ShangpinBaseDatus();
                    datus.setPrice(Float.parseFloat(price));
                    datus.setProName(typeName);
                    datus.setProId(id);
                    datus.setImgRes(R.mipmap.shop_icon);
                    datus.setProNo(proNo);
                    datus.setNorms(norms);
                    datus.setProperty(property);
                    datus.setUnit(unit);
                    datus.setMaxTotal(maxTotal);
                    datus.setSellPrice(Float.parseFloat(price));

                    lists.add(datus);


                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        nodatusImage();
                        if(jsonArray.length()==0) return;
                        loadView();
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    int index=1;
    ImageView nodatusImage;
    CommonAdapter<ShangpinBaseDatus> adapter=null;

    void loadView(){

        if(adapter==null){
            adapter=new CommonAdapter<ShangpinBaseDatus>(KucunchanxunXuanzeshangpinActivity.this,lists, R.layout.item_shangpin) {

                @Override
                public void convert(final ViewHolder holder, ShangpinBaseDatus shangpinDatus,final int position, View convertView) {
                    holder.setText(R.id.listview_tv,shangpinDatus.getProName());
                    holder.setText(R.id.shangpinliebiao_price,shangpinDatus.getPrice()+"");


                    //可以根据自己需求设置一些选项(这里设置了IOS阻塞效果以及item的依次左滑、右滑菜单)
                    ((SwipeMenuLayout)holder.getConvertView()).setIos(true).setLeftSwipe(true);
                    //监听事件
                    holder.setOnClickListener(R.id.ll_content, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //Toast.makeText(ShangpinliebiaoActivity.this,"点击了："+position, Toast.LENGTH_SHORT).show();

                            Intent intent=new Intent();

                            ShangpinBaseDatus datus=lists.get(position);
                            String id=datus.getProId();
                            intent.putExtra("proId",id);
                            intent.putExtra("proName",datus.getProName());

                            setResult(2,intent);
                            finish();

                        }
                    });



                }
            };
            listView.setAdapter(adapter);

        }

    }


    void search(String searchValue){
        this.searchValue=searchValue;
        index=1;
        searchLoad();
    }

    boolean isLoad=false;

    void searchLoad(){
        lists.clear();
        new Thread(new Runnable() {
            @Override
            public void run() {
                loadModel(index);
            }
        }).start();

    }


    void pageLoad(){

        new Thread(new Runnable() {
            @Override
            public void run() {
                loadModel(index);
            }
        }).start();

        adapter.notifyDataSetChanged();
    }


    private void nodatusImage(){
        if(lists.size()==0){
            nodatusImage.setVisibility(View.VISIBLE);
        }else {
            nodatusImage.setVisibility(View.GONE);

        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
            if (view.getLastVisiblePosition() == view.getCount() - 1) {
                index++;
                pageLoad();
            }

        }
    }

    @Override
    public void onScroll(AbsListView absListView, int i, int i1, int i2) {

    }
}
