package com.example.qunxin.erp.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.qunxin.erp.R;
import com.example.qunxin.erp.UserBaseDatus;
import com.example.qunxin.erp.activity.KehuxiangqingActivity;
import com.example.qunxin.erp.activity.RukulishiluruActivity;
import com.example.qunxin.erp.modle.KehuDatus;
import com.example.qunxin.erp.modle.ShangpinBaseDatus;

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

/**
 * Created by qunxin on 2019/8/28.
 */

public class DairukudanFragment extends Fragment implements AbsListView.OnScrollListener {

    ListView listView;

    TextView searchText;

    private RelativeLayout loading;
    private RelativeLayout footView;
    private RelativeLayout bottomView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_kehu, container, false);

        listView=view.findViewById(R.id.listView);
        nodatusImage=view.findViewById(R.id.nodatusImage);

        listView.setOnScrollListener(this);
        loading=view.findViewById(R.id.loading);
        addLoading();


        new Thread(new Runnable() {
            @Override
            public void run() {

                loadModel(index);
            }
        }).start();


        searchText=view.findViewById(R.id.search);

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
                index=1;
            }
        });

        return view;
    }


    List<ShangpinBaseDatus> lists=new ArrayList<>();

    String searchValue="";
    int count=0;
    void loadModel(int index){

        final String pageNum=index+"";
        final String pageSize="10";
        final String signa=UserBaseDatus.getInstance().getSign();
        final String contentType = "application/json";
        final String url= UserBaseDatus.getInstance().url+"api/app/warehouse/pageList";


       JSONObject jsonObject1=new JSONObject();
        try {
            jsonObject1.put("signa",signa);
            jsonObject1.put("pageSize",pageSize);
            jsonObject1.put("pageNum",pageNum);
            jsonObject1.put("warehouseNo",searchValue);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        String jsonString=jsonObject1.toString();

        Map map=UserBaseDatus.getInstance().isSuccessPost(url, jsonString, contentType);

        if((boolean)(map.get("isSuccess"))){

            JSONObject json=(JSONObject) map.get("json");
            try {
                JSONObject jsonData=json.getJSONObject("data");
                final JSONArray jsonArray=jsonData.getJSONArray("rows");
                for (int i=0;i<jsonArray.length();i++){


                        JSONObject jsonObject=jsonArray.getJSONObject(i);
                        String warehouseNo=jsonObject.getString("warehouseNo");

                        String depotName=jsonObject.getString("depotId");

                        String id=jsonObject.getString("id");

                        String status=jsonObject.getString("status");

                        String supplierId=jsonObject.getString("supplierId");


                        //1表示已经入库
                        if(status.equals("0")){
                            String supplierName= getSupplierName(supplierId);
                            ShangpinBaseDatus datus = new ShangpinBaseDatus();
                            datus.setProNo(warehouseNo);
                            datus.setDeportName(depotName);
                            datus.setId(id);
                            datus.setSupplierName(supplierName);
                            lists.add(datus);
                        }else {
                            count++;
                        }



                }

                FragmentActivity fragmentActivity=getActivity();
                if (fragmentActivity != null) {
                    fragmentActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            nodatusImage();
                            removeLoading();
                            if(jsonArray.length()>0){
                                loadView();
                            }else {
                                if(searchValue.equals("")==false){//搜索情况下
                                    removeFootView();
                                    removeBottomView();
                                }else {
                                    removeFootView();
                                    addBottomView();
                                }

                            }
                        }
                    });
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    String getSupplierName(String supplierId){
        final String contentType = "application/x-www-form-urlencoded";
        final String url = UserBaseDatus.getInstance().url + "api/suppliers/getSupplierById";
        final String data = "signa=" + UserBaseDatus.getInstance().getSign() + "&&id=" + supplierId;
        Map map = UserBaseDatus.getInstance().isSuccessPost(url, data, contentType);
        if ((boolean) (map.get("isSuccess"))) {
            JSONObject jsonObject=(JSONObject) map.get("json");

            JSONObject json= null;
            try {
                json = jsonObject.getJSONObject("data");
                String supName=json.getString("supName");
                return supName;

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return "";
    }




    int index=1;

    ImageView nodatusImage;

    CommonAdapter<ShangpinBaseDatus> adapter=null;
    void loadView(){
        if(searchValue.equals("")==false){
            removeFootView();
            removeBottomView();
        }else {
            addFootView();
        }
        if(adapter==null){
            adapter=new CommonAdapter<ShangpinBaseDatus>(getContext(),lists, R.layout.item_dairukudan) {

                @Override
                public void convert(final ViewHolder holder, ShangpinBaseDatus shangpingDatus, final int position, View convertView) {
                    if(shangpingDatus==null) return;

                    holder.setText(R.id.danhao,shangpingDatus.getProNo());
                    holder.setText(R.id.supplierName,shangpingDatus.getSupplierName());
                    //holder.setImageResource(R.id.listview_iv,zhanghuDatus.getImgRes());

                    //可以根据自己需求设置一些选项(这里设置了IOS阻塞效果以及item的依次左滑、右滑菜单)
                    //((SwipeMenuLayout)holder.getConvertView()).setIos(true).setLeftSwipe(true);
                    //监听事件
                    holder.setOnClickListener(R.id.ll_content, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //Toast.makeText(ShangpinliebiaoActivity.this,"点击了："+position, Toast.LENGTH_SHORT).show();
                            isLoad=true;
                            Intent intent=new Intent(getContext(),RukulishiluruActivity.class);
                            ShangpinBaseDatus datus=lists.get(position);
                            String id=datus.getId();
                            intent.putExtra("id",id);

                            startActivityForResult(intent,1);
                        }
                    });



                }
            };
            listView.setAdapter(adapter);
        }
//        if(listView.getCount()<=6){
//            index++;
//            pageLoad();
//        }

    }


     void search(String searchValue){
         this.searchValue=searchValue;
         index=1;
         searchLoad();
    }

    boolean isLoad=false;

    @Override
    public void onResume() {
        super.onResume();
        removeFootView();
        removeBottomView();
        addLoading();
        if (isLoad == false) return;
        new Thread(new Runnable() {
            @Override
            public void run() {
                initData();
                loadModel(index);
            }
        }).start();

        if(adapter!=null){
            adapter.notifyDataSetChanged();
        }
    }




    void pageLoad(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                loadModel(index);
            }
        }).start();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                adapter.notifyDataSetChanged();
            }
        }, 1000);//3秒后执行Runnable中的run方法
    }

    void searchLoad(){
        initData();
        new Thread(new Runnable() {
            @Override
            public void run() {
                loadModel(index);
            }
        }).start();
        if(adapter!=null){
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
            if (view.getLastVisiblePosition() == view.getCount() -1) {
                index++;
                pageLoad();
            }
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }

    private void nodatusImage(){
        if(lists.size()==0){
            nodatusImage.setVisibility(View.VISIBLE);
        }else {
            nodatusImage.setVisibility(View.GONE);

        }
    }
    void initData(){
        lists.clear();
        index=1;
        //adapter=null;
        isLoad=false;
    }


    void removeFootView(){
        if(footView!=null){
            listView.removeFooterView(footView);
            footView=null;
        }
    }

    void addFootView(){
        if(footView==null){
            footView= (RelativeLayout) LayoutInflater.from(getContext()).inflate(R.layout.foot_view,null);
            listView.addFooterView(footView);
        }
    }

    void removeBottomView(){
        if(bottomView!=null){
            listView.removeFooterView(bottomView);
            bottomView=null;
        }
    }

    void addBottomView(){
        if(bottomView==null){
            bottomView= (RelativeLayout) LayoutInflater.from(getContext()).inflate(R.layout.foot_bottom_view,null);
            listView.addFooterView(bottomView);
        }
    }


    void addLoading(){
        loading.setVisibility(View.VISIBLE);
    }

    void removeLoading(){
        loading.setVisibility(View.GONE);
    }
}