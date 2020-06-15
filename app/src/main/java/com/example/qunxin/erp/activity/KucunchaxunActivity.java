package com.example.qunxin.erp.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.qunxin.erp.R;
import com.example.qunxin.erp.UserBaseDatus;
import com.example.qunxin.erp.modle.JinhuoBaseDatus;
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

public class KucunchaxunActivity extends AppCompatActivity implements View.OnClickListener, AbsListView.OnScrollListener {


    View paixuView;
    View shaixuanView;
    View backBtn;
    ListView listView;
    View bantouming;
    ImageView nodatusImage;
    private RelativeLayout footView;
    private RelativeLayout bottomView;
    private RelativeLayout loading;
    boolean isSearch=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kucunchaxun);

        initView();
    }

    void initView(){
        nodatusImage=findViewById(R.id.nodatusImage);
        loading=findViewById(R.id.loading);

        paixuView=findViewById(R.id.paixu);
        shaixuanView=findViewById(R.id.shaixuan);
        backBtn=findViewById(R.id.back);
        listView=findViewById(R.id.listView);
        bantouming=findViewById(R.id.bantouming);

        paixuView.setOnClickListener(this);
        shaixuanView.setOnClickListener(this);
        listView.setOnScrollListener(this);

        backBtn.setOnClickListener(this);
        addLoading();



        new Thread(new Runnable() {
            @Override
            public void run() {
                loadModel(index);
            }
        }).start();

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.paixu:

                break;
            case R.id.shaixuan:
                showPopupWindow1(shaixuanView);
                break;
            case R.id.back:
                finish();
                break;
        }
    }


    private void showPopupWindow1(View view1) {

        //自定义布局，显示内容

        final View view = LayoutInflater.from(KucunchaxunActivity.this).inflate(R.layout.shaixuan_layout, null);

        final PopupWindow window = new PopupWindow(view, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT, true);

        window.setTouchable(true);

        window.setTouchInterceptor(new View.OnTouchListener() {

            @Override

            public boolean onTouch(View v, MotionEvent event) {

                //这里如果返回true的话，touch事件将被拦截
                //拦截后 PoppWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
                return false;
            }

        });

         depotNameText=view.findViewById(R.id.depotName);
         shangpinText=view.findViewById(R.id.shengpin);
         View finishBtn=view.findViewById(R.id.finish);
         View clearBtn=view.findViewById(R.id.clear);

         depotNameText.setText(depotName);
         shangpinText.setText(proName);

        depotNameText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(KucunchaxunActivity.this,XuanzeCangkuActivity.class);
                startActivityForResult(intent,1);
            }
        });



        shangpinText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(KucunchaxunActivity.this,KucunchanxunXuanzeshangpinActivity.class);
                intent.putExtra("depot",depotId);
                startActivityForResult(intent,1);
            }
        });

        finishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lists.clear();
                index=1;
                isLoad=false;
                isSearch=true;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        loadModel(index);
                    }
                }).start();
                if (adapter != null) {
                    Log.d("hhhhhh", "onClick: hhhhhh");
                    adapter.notifyDataSetChanged();
                }
                window.dismiss();

            }
        });

        clearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                depotId="";
                proId="";
                depotName="";
                proName="";
                depotNameText.setText("全部");
                shangpinText.setText("全部");
            }
        });


        //（注意一下！！）如果不设置popupWindow的背景，无论是点击外部区域还是Back键都无法弹框
        window.showAsDropDown(view1);
        window.setBackgroundDrawable(new BitmapDrawable());
        //listView.setBackgroundResource(R.drawable.bantouming);



        //window = new PopupWindow(popAddNoteType, mScreenWidth *8 /10, ViewGroup.LayoutParams.WRAP_CONTENT);

        //在PopupWindow里面就加上下面代码，让键盘弹出时，不会挡住pop窗口。

        window.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);

        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        //点击空白处时，隐藏掉pop窗口

        window.setFocusable(true);


        //backgroundAlpha(0.7f);

        bantouming.setVisibility(View.VISIBLE);






        //添加pop窗口关闭事件

        window.setOnDismissListener(new poponDismissListener());

    }


    /**

     * 添加新笔记时弹出的popWin关闭的事件，主要是为了将背景透明度改回来

     * @author cg

     *

     */

    class poponDismissListener implements PopupWindow.OnDismissListener{



        @Override

        public void onDismiss() {

            // TODO Auto-generated method stub

            //Log.v("List_noteTypeActivity:", "我是关闭事件");

            //backgroundAlpha(1f);
           bantouming.setVisibility(View.GONE);



        }



    }



    /**

     * 设置添加屏幕的背景透明度

     * @param bgAlpha

     */

    public void backgroundAlpha(float bgAlpha)

    {

        WindowManager.LayoutParams lp = getWindow().getAttributes();

        lp.alpha = bgAlpha; //0.0-1.0

        getWindow().setAttributes(lp);

    }




    TextView depotNameText=null;
    TextView shangpinText=null;




    List<ShangpinBaseDatus> lists=new ArrayList<>();
    void loadModel(int index){

        final String contentTypeList = "application/json";
        final String url= UserBaseDatus.getInstance().url+"api/app/stock/pageList";
        Map<String, String> jsonMap = new HashMap<>();
        jsonMap.put("proId",proId);
        jsonMap.put("userId",UserBaseDatus.getInstance().userId);
        jsonMap.put("pageNum", index+"");
        jsonMap.put("pageSize", "10");
        jsonMap.put("signa", UserBaseDatus.getInstance().getSign());
        jsonMap.put("depot",depotId);
        String jsonString = JSON.toJSONString(jsonMap);

        Log.d("jsonString", jsonString);
        Map map=UserBaseDatus.getInstance().isSuccessPost(url, jsonString, contentTypeList);
        if((boolean)(map.get("isSuccess"))){

            JSONObject json=(JSONObject) map.get("json");
            try {
                JSONObject jsonData=json.getJSONObject("data");
                final JSONArray jsonArray=jsonData.getJSONArray("rows");
                for (int i=0;i<jsonArray.length();i++){
                    JSONObject jsonObject=jsonArray.getJSONObject(i);

                    String proName=jsonObject.getString("proName");

                    String norms=jsonObject.getString("norms");

                    String property=jsonObject.getString("property");

                    String unit=jsonObject.getString("unit");


                    String depot=jsonObject.getString("depot");


                    String beginTotal=jsonObject.getString("beginTotal");

                    String stockTotal=jsonObject.getString("stockTotal");

                    String stockAmount=jsonObject.getString("stockAmount");


                    String price=jsonObject.getString("avgFinalcost");

                    String id=jsonObject.getString("id");

                    ShangpinBaseDatus datus = new ShangpinBaseDatus();
                    datus.setProName(proName);
                    datus.setStockAmount(Float.parseFloat(stockAmount));
                    datus.setStockTotal(Integer.parseInt(stockTotal));
                    datus.setId(id);
                    datus.setProperty(property);
                    datus.setNorms(norms);
                    datus.setUnit(unit);
                    datus.setPrice(Float.parseFloat(price));
                    datus.setDeportName(depot);
                    lists.add(datus);

                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        nodatusImage();
                        removeLoading();
                        if (jsonArray.length() > 0) {
                            loadView();
                        } else {
                            if (isSearch) {//搜索情况下
                                removeFootView();
                                removeBottomView();
                            } else {
                                removeFootView();
                                addBottomView();
                            }

                        }
                    }
                });

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    CommonAdapter<ShangpinBaseDatus> adapter=null;

    private void loadView() {
        if (isSearch) {
            removeFootView();
            removeBottomView();
        } else {
            addFootView();
        }

        if(adapter==null){
            adapter=new CommonAdapter<ShangpinBaseDatus>(KucunchaxunActivity.this, lists, R.layout.item_kucunchaxun) {

                @Override
                public void convert(final ViewHolder holder, ShangpinBaseDatus datus, final int position, View convertView) {

                    if (datus == null) return;
                    holder.setText(R.id.name, datus.getProName());
                    holder.setText(R.id.count, datus.getStockTotal() + "");
                    holder.setText(R.id.totalAmount, datus.getStockAmount() + "");

                    holder.setOnClickListener(R.id.ll_content, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //Toast.makeText(ShangpinliebiaoActivity.this,"点击了："+position, Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(KucunchaxunActivity.this, ShangpinkucunActivity.class);
                            ShangpinBaseDatus datus = lists.get(position);

                            isLoad=true;
                            intent.putExtra("proName",datus.getProName());
                            intent.putExtra("norms",datus.getNorms());
                            intent.putExtra("property",datus.getProperty());
                            intent.putExtra("unit",datus.getUnit());
                            intent.putExtra("depotName",datus.getDeportName());
                            intent.putExtra("stockTotal",datus.getStockTotal());
                            intent.putExtra("stockAmount",datus.getStockAmount());
                            intent.putExtra("price",datus.getPrice());

                            startActivity(intent);
                        }
                    });

                }

            };

            listView.setAdapter(adapter);

        }

        if(adapter!=null){
            adapter.notifyDataSetChanged();
        }


    }

    String depotName;
    String depotId;
    String proName;
    String proId;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 1:
                if(resultCode==1){
                    depotName=data.getStringExtra("name");
                    depotId=data.getStringExtra("id");
                    depotNameText.setText(depotName);
                }else if(resultCode==2){
                    proName=data.getStringExtra("proName");
                    proId=data.getStringExtra("proId");
                    Log.d("proId", proId);
                    shangpinText.setText(proName);
                }
                break;
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
            if (view.getLastVisiblePosition() == view.getCount() - 1&&view.getLastVisiblePosition()==9) {
              index++;
                pageLoad();
            }

        }
    }
    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }

    @Override
    public void onResume() {
        super.onResume();
        removeFootView();
        removeBottomView();
        isSearch=false;
        //addLoading();
        if (isLoad == false) {
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                initData();
                loadModel(index);
            }
        }).start();
        if (adapter != null) {
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


    private void nodatusImage(){
        if(lists.size()==0){
            nodatusImage.setVisibility(View.VISIBLE);
        }else {
            nodatusImage.setVisibility(View.GONE);

        }
    }

    boolean isLoad=false;
    int index=1;
    void initData() {
        lists.clear();
        index = 1;
        //adapter=null;
        isLoad = false;
    }


    void removeFootView() {
        if (footView != null) {
            listView.removeFooterView(footView);
            footView = null;
        }
    }

    void addFootView() {
        if (footView == null) {
            footView = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.foot_view, null);
            listView.addFooterView(footView);
        }
    }

    void removeBottomView() {
        if (bottomView != null) {
            listView.removeFooterView(bottomView);
            bottomView = null;
        }
    }

    void addBottomView() {
        if (bottomView == null) {
            bottomView = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.foot_bottom_view, null);
            listView.addFooterView(bottomView);
        }
    }


    void addLoading() {
        loading.setVisibility(View.VISIBLE);
    }

    void removeLoading() {
        loading.setVisibility(View.GONE);


    }
}

