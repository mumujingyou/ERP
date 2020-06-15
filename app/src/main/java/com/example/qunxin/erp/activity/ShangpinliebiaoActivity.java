package com.example.qunxin.erp.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.qunxin.erp.R;
import com.example.qunxin.erp.UserBaseDatus;
import com.example.qunxin.erp.modle.ShangpinDatus;
import com.example.qunxin.erp.util.Constant;
import com.google.zxing.activity.CaptureActivity;

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

public class ShangpinliebiaoActivity extends AppCompatActivity implements View.OnClickListener, AbsListView.OnScrollListener, TextWatcher, View.OnTouchListener {


    ListView listView;
    public List<ShangpinDatus> lists = new ArrayList<>();
    EditText searchText;

    View backBtn;
    View addBtn;
    View printText;
    ImageView nodatusImage;

    public List<ShangpinDatus> getLists() {
        return lists;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shangpinliebiao);
        initView();


    }

    void initView() {

        nodatusImage = findViewById(R.id.nodatusImage);
        backBtn = findViewById(R.id.shangpinliebiao_back);
        addBtn = findViewById(R.id.shangpinliebiao_add);
        printText = findViewById(R.id.shangpinliebiao_printtag);

        backBtn.setOnClickListener(this);
        addBtn.setOnClickListener(this);
        printText.setOnClickListener(this);


        listView = findViewById(R.id.listView);

        searchText = findViewById(R.id.search);

        listView.setOnScrollListener(this);

        searchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override

            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    search(searchText.getText().toString());
                    return true;
                }
                return false;

            }
        });

        searchText.addTextChangedListener(this);
        searchText.setOnTouchListener(this);


        new Thread(new Runnable() {
            @Override
            public void run() {
                loadModel(1);
            }
        }).start();


    }


    CommonAdapter<ShangpinDatus> adapter = null;
    String searchValue = "";

    void loadModel(int index) {

        final String contentTypeList = "application/json";
        final String url = UserBaseDatus.getInstance().url + "api/app/proProduct/list";
        Map<String, String> jsonMap = new HashMap<>();
        jsonMap.put("searchValue", searchValue);
        jsonMap.put("pageNum", index + "");
        jsonMap.put("pageSize", "10");
        jsonMap.put("signa", UserBaseDatus.getInstance().getSign());
        String jsonString = JSON.toJSONString(jsonMap);

        Map map = UserBaseDatus.getInstance().isSuccessPost(url, jsonString, contentTypeList);
        if ((boolean) (map.get("isSuccess"))) {

            JSONObject json = (JSONObject) map.get("json");
            try {
                JSONObject jsonData = json.getJSONObject("data");
                final JSONArray jsonArray = jsonData.getJSONArray("rows");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String typeName = jsonObject.getString("proName");

                    String price = jsonObject.getString("price");

                    String id = jsonObject.getString("id");

                    ShangpinDatus datus = new ShangpinDatus();
                    datus.setPrice(price);
                    datus.setName(typeName);
                    datus.setId(id);
                    datus.setImgRes(R.mipmap.shop_icon);
                    lists.add(datus);
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        nodatusImage();
                        if (jsonArray.length() == 0) return;
                        loadView();
                    }
                });

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    int index = 1;

    void loadView() {

        if (adapter == null) {
            adapter = new CommonAdapter<ShangpinDatus>(ShangpinliebiaoActivity.this, lists, R.layout.item_shangpin) {

                @Override
                public void convert(final ViewHolder holder, ShangpinDatus shangpinDatus, final int position, View convertView) {
                    holder.setText(R.id.listview_tv, shangpinDatus.getName());
                    holder.setText(R.id.shangpinliebiao_price, shangpinDatus.getPrice());

                    //holder.setImageResource(R.id.listview_iv,shangpinDatus.getImgRes());

                    //可以根据自己需求设置一些选项(这里设置了IOS阻塞效果以及item的依次左滑、右滑菜单)
                    ((SwipeMenuLayout) holder.getConvertView()).setIos(true).setLeftSwipe(true);
                    //监听事件
                    holder.setOnClickListener(R.id.ll_content, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //Toast.makeText(ShangpinliebiaoActivity.this,"点击了："+position, Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(ShangpinliebiaoActivity.this, ShangpinxiangqingActivity.class);

                            ShangpinDatus datus = lists.get(position);
                            String id = datus.getId();
                            intent.putExtra("id", id);
                            index = position;
                            isLoad = true;
                            startActivityForResult(intent, 1);
                        }
                    });
                    holder.setOnClickListener(R.id.btn_zd, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //Toast.makeText(MainActivity.this,"点击了置顶选项", Toast.LENGTH_SHORT).show();
                            //在ListView里，点击侧滑菜单上的选项时，如果想让侧滑菜单同时0关闭，调用这句话
                            ((SwipeMenuLayout) holder.getConvertView()).quickClose();
                            ShangpinDatus bean = lists.get(position);
                            lists.add(0, bean);
                            lists.remove(position + 1);
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
                                    if ("".equals(data)) {
                                        lists.remove(position);
                                        notifyDataSetChanged();
                                    } else {
                                        Toast.makeText(mContext, data, Toast.LENGTH_SHORT).show();
                                    }
                                    data = "";

                                }
                            }, 500);//3秒后执行Runnable中的run方法


                        }
                    });

                }
            };

            listView.setAdapter(adapter);
        }


    }


    String data = "";

    void delete(final int position) {
        final ShangpinDatus datus = lists.get(position);

        new Thread(new Runnable() {
            @Override
            public void run() {
                final String strUrl = UserBaseDatus.getInstance().url + "api/app/proProduct/remove";
                final String strData = "id=" + datus.getId() + "&&signa=" + UserBaseDatus.getInstance().getSign();
                final String contentType = "application/x-www-form-urlencoded";
                Map map = UserBaseDatus.getInstance().isSuccessPost(strUrl, strData, contentType);
                if ((boolean) (map.get("isSuccess"))) {
                    Log.d("delete", "run: 删除成功");
                } else {
                    JSONObject jsonObject = (JSONObject) map.get("json");
                    try {
                        data = jsonObject.getString("data");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.shangpinliebiao_back:
                finish();
                break;
            case R.id.shangpinliebiao_add:
                isLoad = true;
                Intent intent = new Intent(ShangpinliebiaoActivity.this, AddShangpinActivity.class);
                startActivity(intent);
                break;
            case R.id.shangpinliebiao_printtag:
                Intent intent1 = new Intent(ShangpinliebiaoActivity.this, PrintTagActivity.class);
                startActivity(intent1);
        }
    }


    public void search(String searchValue) {
        this.searchValue = searchValue;
        index = 1;
        searchLoad();
    }

    boolean isLoad = false;

    @Override
    public void onResume() {
        super.onResume();

        if (isLoad == false) {
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                lists.clear();
                index = 1;
                adapter = null;
                isLoad = false;
                loadModel(index);
            }
        }).start();
    }

    void pageLoad() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                loadModel(index);
            }
        }).start();
        adapter.notifyDataSetChanged();

    }

    void searchLoad() {
        lists.clear();
        adapter = null;
        new Thread(new Runnable() {
            @Override
            public void run() {
                loadModel(index);
            }
        }).start();
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
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        search(searchText.getText().toString());
        index = 1;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        // getCompoundDrawables获取是一个数组，数组0,1,2,3,对应着左，上，右，下 这4个位置的图片，如果没有就为null
        Drawable drawable = searchText.getCompoundDrawables()[2];
        //如果右边没有图片，不再处理
        if (drawable == null) {
            return false;
        }
        //如果不是按下事件，不再处理
        if (event.getAction() != MotionEvent.ACTION_DOWN) {
            return false;
        }
        if (event.getX() > searchText.getWidth()
                - searchText.getPaddingRight()
                - drawable.getIntrinsicWidth()) {
            //具体操作
            startQrCode();
            Log.d("touch", "onTouch: touch");
        }
        return false;
    }


    // 开始扫码
    private void startQrCode() {
        if (ContextCompat.checkSelfPermission(ShangpinliebiaoActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            // 申请权限
            ActivityCompat.requestPermissions(ShangpinliebiaoActivity.this, new String[]{Manifest.permission.CAMERA}, Constant.REQ_PERM_CAMERA);
            return;
        }
        // 二维码扫码
        Intent intent = new Intent(ShangpinliebiaoActivity.this, CaptureActivity.class);
        startActivityForResult(intent, Constant.REQ_QR_CODE);
    }


    //申请访问权限回调结果
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case Constant.REQ_PERM_CAMERA:
                // 摄像头权限申请
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 获得授权
                    startQrCode();
                } else {
                    // 被禁止授权
                    Toast.makeText(ShangpinliebiaoActivity.this, "请至权限中心打开本应用的相机访问权限", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constant.REQ_QR_CODE && resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            String scanResult = bundle.getString(Constant.INTENT_EXTRA_KEY_QR_SCAN);
            //将扫描出的信息显示出来
            searchText.setText(scanResult);

        }
    }

    void nodatusImage() {
        if (lists.size() == 0) {
            nodatusImage.setVisibility(View.VISIBLE);
        } else {
            nodatusImage.setVisibility(View.GONE);

        }
    }
}
