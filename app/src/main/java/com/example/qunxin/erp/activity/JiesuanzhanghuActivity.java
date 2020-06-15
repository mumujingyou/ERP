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
import com.example.qunxin.erp.modle.ShangpinDatus;
import com.example.qunxin.erp.modle.ZhanghuDatus;

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

public class JiesuanzhanghuActivity extends AppCompatActivity implements View.OnClickListener {

    View backBtn;
    View addBtn;
    ListView listView;
    TextView searchText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jiesuanzhanghu);

        initView();
    }

    void initView(){
        nodatusImage=findViewById(R.id.nodatusImage);

        backBtn=findViewById(R.id.jiesuanzhanghu_back);
        addBtn=findViewById(R.id.jiesuanzhanghu_add);
        listView=findViewById(R.id.listView);

        backBtn.setOnClickListener(this);
        addBtn.setOnClickListener(this);


        new Thread(new Runnable() {
            @Override
            public void run() {
                loadModel();
            }
        }).start();


        searchText=findViewById(R.id.jiesuanzhanghu_search);

        searchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override

            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_SEARCH){

                    Log.d("search", "onEditorAction: search");
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.jiesuanzhanghu_back:
                finish();
                break;
            case R.id.jiesuanzhanghu_add:
                isLoad=true;
                Intent intent=new Intent(JiesuanzhanghuActivity.this,AddZhanghuActivity.class);
                startActivity(intent);
                break;
        }
    }

    List<ZhanghuDatus> lists=new ArrayList<>();
    String searchValue="";
    void loadModel(){

        final String contentTypeList = "application/json";
        final String url= UserBaseDatus.getInstance().url+"api/app/settleAccount/list";
        Map<String, String> jsonMap = new HashMap<>();
        jsonMap.put("searchValue",searchValue);
        jsonMap.put("pageNum", "1");
        jsonMap.put("pageSize", "1000");
        jsonMap.put("signa", UserBaseDatus.getInstance().getSign());
        String jsonString = JSON.toJSONString(jsonMap);

        Map map=UserBaseDatus.getInstance().isSuccessPost(url, jsonString, contentTypeList);
        if((boolean)(map.get("isSuccess"))){

            JSONObject json=(JSONObject) map.get("json");
            try {
                JSONObject jsonData=json.getJSONObject("data");
                JSONArray jsonArray=jsonData.getJSONArray("rows");
                for (int i=0;i<jsonArray.length();i++){
                    JSONObject jsonObject=jsonArray.getJSONObject(i);
                    String acName=jsonObject.getString("acName");

                    String amount=jsonObject.getString("amount");

                    String bankName=jsonObject.getString("bankName");

                    String id=jsonObject.getString("id");

                    ZhanghuDatus datus = new ZhanghuDatus();
                    datus.setAcName(acName);
                    datus.setAmount(amount);
                    datus.setBankName(bankName);
                    datus.setImgRes(R.mipmap.shop_icon);
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

    int index=1;
    ImageView nodatusImage;

    void loadView(){


        if(lists.size()==0){
            nodatusImage.setVisibility(View.VISIBLE);
        }else {
            nodatusImage.setVisibility(View.GONE);

        }


        listView.setAdapter(new CommonAdapter<ZhanghuDatus>(JiesuanzhanghuActivity.this,lists, R.layout.item_jiesuanzhanghu) {

            @Override
            public void convert(final ViewHolder holder, ZhanghuDatus zhanghuDatus, final int position, View convertView) {
                holder.setText(R.id.item_jiesuanzhanghu_name,zhanghuDatus.getBankName());
                holder.setText(R.id.item_jiesuanzhanghu_count,zhanghuDatus.getAmount());
                holder.setText(R.id.listview_tv,zhanghuDatus.getAcName());
                //holder.setImageResource(R.id.listview_iv,zhanghuDatus.getImgRes());

                //可以根据自己需求设置一些选项(这里设置了IOS阻塞效果以及item的依次左滑、右滑菜单)
                ((SwipeMenuLayout)holder.getConvertView()).setIos(true).setLeftSwipe(true);
                //监听事件
                holder.setOnClickListener(R.id.ll_content, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Toast.makeText(ShangpinliebiaoActivity.this,"点击了："+position, Toast.LENGTH_SHORT).show();
                        isLoad=true;
                        Intent intent=new Intent(JiesuanzhanghuActivity.this,ZhanghuxiangqingActivity.class);
                        ZhanghuDatus datus=lists.get(position);
                        String id=datus.getId();
                        intent.putExtra("id",id);
                        index=position;
                        startActivityForResult(intent,1);
                    }
                });
                holder.setOnClickListener(R.id.btn_zd, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Toast.makeText(MainActivity.this,"点击了置顶选项", Toast.LENGTH_SHORT).show();
                        //在ListView里，点击侧滑菜单上的选项时，如果想让侧滑菜单同时0关闭，调用这句话
                        ((SwipeMenuLayout) holder.getConvertView()).quickClose();
                        ZhanghuDatus datus=lists.get(position);
                        lists.add(0,datus);
                        lists.remove(position+1);
                        notifyDataSetChanged();
                    }
                });
                holder.setOnClickListener(R.id.btn_delete, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Toast.makeText(MainActivity.this,"点击了删除选项", Toast.LENGTH_SHORT).show();
                        //在ListView里，点击侧滑菜单上的选项时，如果想让侧滑菜单同时关闭，调用这句话
                        ((SwipeMenuLayout) holder.getConvertView()).quickClose();
                        //删除操作
                        delete(position);
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                /**
                                 *要执行的操作
                                 */
                                if("".equals(data)){
                                    lists.remove(position);
                                    notifyDataSetChanged();
                                }else {
                                    Toast.makeText(mContext, data, Toast.LENGTH_SHORT).show();
                                }
                                data="";

                            }
                        }, 500);//3秒后执行Runnable中的run方法
                    }
                });
                //长按监听
                holder.setOnLongClickListener(R.id.ll_content, new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        // Toast.makeText(MainActivity.this,"正在进行长按操作", Toast.LENGTH_SHORT).show();
                        return true;
                    }
                });
            }
        });



    }

    String data="";
    void delete(final int position){
        final ZhanghuDatus datus=lists.get(position);
        new Thread(new Runnable() {
            @Override
            public void run() {
                final String strUrl = UserBaseDatus.getInstance().url+"api/app/settleAccount/remove";
                final String strData = "ids="+datus.getId()+"&&signa="+UserBaseDatus.getInstance().getSign();
                final String contentType = "application/x-www-form-urlencoded";
                Map map=UserBaseDatus.getInstance().isSuccessPost(strUrl, strData, contentType);
                if ((boolean)(map.get("isSuccess"))) {
                    Log.d("delete", "run: 删除成功");
                }else {
                    JSONObject jsonObject= (JSONObject) map.get("json");
                    try {
                        data =jsonObject.getString("data");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    public void search(String searchValue){
        lists.clear();
        this.searchValue=searchValue;
        new Thread(new Runnable() {
            @Override
            public void run() {
                loadModel();
            }
        }).start();
    }

    boolean isLoad=false;
    @Override
    protected void onResume() {
        super.onResume();
        if(isLoad==false) return;;
        lists.clear();
        new Thread(new Runnable() {
            @Override
            public void run() {
                loadModel();
            }
        }).start();
    }






}
