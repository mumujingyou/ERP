package com.example.qunxin.erp.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.qunxin.erp.R;
import com.example.qunxin.erp.UserBaseDatus;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PandiandanXiangqingActivity extends AppCompatActivity {



    TextView depotNameText;
    TextView busNoText;
    TextView leaderText;
    TextView busTimeText;
    TextView createByText;
    TextView createTimeText;
    TextView remarksText;

    View backBtn;
    ImageView imageView;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pandiandan_xiangqing);

        initView();
    }

    void initView(){
        depotNameText=findViewById(R.id.cangku);
        busNoText=findViewById(R.id.busNo);
        leaderText=findViewById(R.id.jingshourenText);
        busTimeText=findViewById(R.id.dateText);
        createByText=findViewById(R.id.createBy);
        createTimeText=findViewById(R.id.creteTimeText);
        remarksText=findViewById(R.id.remarksText);
        backBtn=findViewById(R.id.back);
        imageView=findViewById(R.id.image);
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



        Intent intent=getIntent();
        String depotName=intent.getStringExtra("depotName");
        String busNo=intent.getStringExtra("busNo");
        String leader=intent.getStringExtra("leader");
        String busTime=intent.getStringExtra("busTime");
        String createBy=intent.getStringExtra("createBy");
        String createTime=intent.getStringExtra("createTime");
        String remarks=intent.getStringExtra("remarks");

        depotNameText.setText(depotName);
        busNoText.setText(busNo);
        leaderText.setText(leader);
        busTimeText.setText(busTime);
        createByText.setText(createBy);
        createTimeText.setText(createTime);
        remarksText.setText(remarks);

        new Thread(new Runnable() {
            @Override
            public void run() {
                getJsonIds();
                getImage();
            }
        }).start();

    }


    List<String> jsonIds=new ArrayList<>();
    void getJsonIds(){
        final String contentType = "application/x-www-form-urlencoded";
        final String url = UserBaseDatus.getInstance().url + "api/app/upload/list";
        final String data = "signa=" + UserBaseDatus.getInstance().getSign() + "&&busNo=" + busNoText.getText().toString();
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
        dialog = new Dialog(PandiandanXiangqingActivity.this, R.style.AlertDialog_AppCompat_Light);
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
