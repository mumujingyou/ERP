package com.example.qunxin.erp.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.example.qunxin.erp.R;
import com.example.qunxin.erp.UserBaseDatus;
import com.example.qunxin.erp.modle.JinhuoBaseDatus;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import swipedelete.utils.CommonAdapter;
import swipedelete.utils.ViewHolder;

public class PandianlishiSearchActivity extends AppCompatActivity {

    EditText searchText;
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pandianlishi_search);

        initView();
    }

    void initView(){
        searchText=findViewById(R.id.searchText);
        listView=findViewById(R.id.listView);
        listView.setBackgroundResource(R.drawable.bantouming);

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
//                if(searchText.getText().toString().equals("")){
//                    lists.clear();
//                    adapter.notifyDataSetChanged();
//                }else {
//                    search("linjujiu");
//                }
                search(searchText.getText().toString());
                //index=1;
            }
        });

    }

    private void search(final String searchValue) {
        lists.clear();
        new Thread(new Runnable() {
            @Override
            public void run() {
                loadModel(searchValue);
            }
        }).start();
    }


    List<JinhuoBaseDatus> lists=new ArrayList<>();
    BaseAdapter adapter=null;
    void loadView(){
        if(lists.size()!=0){
            listView.setBackgroundColor(Color.parseColor("#00ffffff"));
        }else {
            listView.setBackgroundResource(R.drawable.bantouming);

        }

        adapter= new CommonAdapter<JinhuoBaseDatus>(PandianlishiSearchActivity.this,lists, R.layout.item_pandianlishi) {

            @Override
            public void convert(final ViewHolder holder, JinhuoBaseDatus jinhuoBaseDatus, final int position, View convertView) {

                if(jinhuoBaseDatus==null) return;
                holder.setText(R.id.danhao, jinhuoBaseDatus.getBuyNo());
                holder.setText(R.id.creatTime, jinhuoBaseDatus.getCreateTime());
                holder.setText(R.id.cangku, jinhuoBaseDatus.getDepotName());
                holder.setText(R.id.status, jinhuoBaseDatus.getTotalAmount()+"￥");

                TextView statusText=holder.getView(R.id.status);
                /** 0无盈亏 1.有盈亏3盘盈4.盘亏 */
                if(jinhuoBaseDatus.getStatus().equals("0")){
                    statusText.setText("无盈亏");
                }else if(jinhuoBaseDatus.getStatus().equals("1")){
                    statusText.setText("有盈亏");
                }else if(jinhuoBaseDatus.getStatus().equals("3")){
                    statusText.setText("盘盈");
                }else if(jinhuoBaseDatus.getStatus().equals("4")){
                    statusText.setText("盘亏");
                }

                holder.setOnClickListener(R.id.ll_content, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Toast.makeText(ShangpinliebiaoActivity.this,"点击了："+position, Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(PandianlishiSearchActivity.this,PandianxiangqingListActivity.class);
                        JinhuoBaseDatus datus=lists.get(position);
                        String id=datus.getId();
                        String status=datus.getStatus();
                        intent.putExtra("id",id);
                        intent.putExtra("status",status);

                        startActivity(intent);
                    }
                });
            }
        };

        listView.setAdapter(adapter);
    }


    void loadModel(String searchValue){

        final String contentTypeList = "application/json";
        final String url= UserBaseDatus.getInstance().url+"api/app/stockCheck/list";
        Map<String, String> jsonMap = new HashMap<>();
        jsonMap.put("pageNum", "1");
        jsonMap.put("pageSize", "10");
        jsonMap.put("signa", UserBaseDatus.getInstance().getSign());
        jsonMap.put("stockCheckNo",searchValue);
        String jsonString = JSON.toJSONString(jsonMap);

        Map map=UserBaseDatus.getInstance().isSuccessPost(url, jsonString, contentTypeList);
        if((boolean)(map.get("isSuccess"))){

            JSONObject json=(JSONObject) map.get("json");
            try {
                JSONObject jsonData=json.getJSONObject("data");
                JSONArray jsonArray=jsonData.getJSONArray("rows");
                for (int i=0;i<jsonArray.length();i++){
                    JSONObject jsonObject=jsonArray.getJSONObject(i);

                    String no=jsonObject.getString("stockCheckNo");

                    String createTime=jsonObject.getString("createTime");

                    String depot=jsonObject.getString("depot");

                    String depotName=getDepotName(depot);

                    String status=jsonObject.getString("status");

                    String id=jsonObject.getString("id");

                    JinhuoBaseDatus datus = new JinhuoBaseDatus();
                    datus.setBuyNo(no);
                    datus.setCreateTime(createTime);
                    datus.setStatus(status);
                    datus.setDepotName(depotName);
                    datus.setId(id);
                    lists.add(datus);
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        loadView();
                    }
                });

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    String getDepotName(String depot) throws JSONException {
        final String contentType = "application/x-www-form-urlencoded";
        final String url = UserBaseDatus.getInstance().url + "api/app/depot/getDepotById";
        final String data = "signa=" + UserBaseDatus.getInstance().getSign() + "&&id=" + depot;
        Map map = UserBaseDatus.getInstance().isSuccessPost(url, data, contentType);
        if ((boolean) (map.get("isSuccess"))) {
            JSONObject jsonObject=(JSONObject) map.get("json");

            JSONObject json=jsonObject.getJSONObject("data");
            return json.getString("name");

        }else {
            return "";
        }
    }

}
