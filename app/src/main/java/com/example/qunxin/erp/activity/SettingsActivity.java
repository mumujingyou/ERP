package com.example.qunxin.erp.activity;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.qunxin.erp.ActivityManager;
import com.example.qunxin.erp.BaseActivity;
import com.example.qunxin.erp.R;
import com.example.qunxin.erp.UserBaseDatus;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by qunxin on 2019/8/6.
 */

public class SettingsActivity extends BaseActivity {


    CircleImageView showImageView;
    TextView cancelLoginBtn;
    private View backBtn;
    TextView phoneNumTex;
    TextView nameText;
    TextView passWorldText;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        initView();
        showImageView.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {

                showQuan();
                Intent intent = new Intent();
		        /* 开启Pictures画面Type设定为image */
                intent.setType("image/*");
		        /* 使用Intent.ACTION_GET_CONTENT这个Action */
                intent.setAction(Intent.ACTION_GET_CONTENT);
		        /* 取得相片后返回本画面 */
                startActivityForResult(intent, 2);
            }
        });

        if(readImage()!=null){
            showImageView.setImageBitmap(readImage());
        }



        cancelLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = getSharedPreferences("baseData", Activity.MODE_PRIVATE).edit();
                editor.putBoolean("login",false);
                editor.commit();
                finish();
                Intent intent=new Intent(SettingsActivity.this,LoginActivity.class);
                startActivity(intent);
                ActivityManager.getInstance().finishActivitys();
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        SharedPreferences read = getSharedPreferences("baseData", Activity.MODE_PRIVATE);
        nameText.setText(read.getString("userName",""));
        phoneNumTex.setText(read.getString("phoneNumber",""));


    }


    void initView(){
        showImageView = (CircleImageView)findViewById(R.id.headBitmpa);
        cancelLoginBtn=findViewById(R.id.cancle_login);
        backBtn=findViewById(R.id.settings_back);
        phoneNumTex=findViewById(R.id.settings_phone);
        nameText=findViewById(R.id.settings_name);
        passWorldText=findViewById(R.id.passWold);
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (resultCode == RESULT_OK) {
//            Uri uri = data.getData();
//
//            ContentResolver cr = this.getContentResolver();
//            try {
//                Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
//                CircleImageView imageView = (CircleImageView) findViewById(R.id.headBitmpa);
//				/* 将Bitmap设定到ImageView */
//
//                imageView.setImageBitmap(bitmap);
//
//                ByteArrayOutputStream baos=new ByteArrayOutputStream();
//                bitmap.compress(Bitmap.CompressFormat.JPEG,100,baos);
//                bitmap.recycle();
//
//                if(saveImage(baos.toByteArray())){
//                    Log.d("hhhhh", "onActivityResult: 成功");
//                }else {
//                    Log.d("hhhhh", "onActivityResult: 失败");
//                }
//
//            } catch (IOException e) {
//                Log.e("Exception", e.getMessage(),e);
//            }
//        }
//        super.onActivityResult(requestCode, resultCode, data);
//
//    }

    String picPath;

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        /*
        Bitmap bitmap=null;

        if(requestCode == 2){//相册
            if (resultCode == RESULT_OK) {
                Uri uri = data.getData();
                String pathOfPicture = getAbsoluteImagePath(uri);
                System.out.println("ok============================="
                        + pathOfPicture);
                Log.e("uri", uri.getHost());
                ContentResolver cr = this.getContentResolver();
                InputStream is = null;
                try {
                    is = cr.openInputStream(uri);
                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                bitmap = BitmapFactory.decodeStream(is);
            }

        }
        if(bitmap!=null){
            showImageView.setImageBitmap(bitmap);
            try{
                ByteArrayOutputStream baos=new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG,100,baos);

                saveImage(baos.toByteArray());

            }catch (Exception e){

            }

        }
        */




        if (resultCode == Activity.RESULT_OK) {

            /** * 当选择的图片不为空的话，在获取到图片的途径 */

            Uri uri = data.getData();

            Log.e("tag", "uri = " + uri);

            try {

                String[] pojo = { MediaStore.Images.Media.DATA };

                Cursor cursor = managedQuery(uri, pojo, null, null, null);

                if (cursor != null) {

                    ContentResolver cr = this.getContentResolver();

                    int colunm_index = cursor

                            .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

                    cursor.moveToFirst();

                    String path = cursor.getString(colunm_index);

                    /***

                     * * 这里加这样一个判断主要是为了第三方的软件选择，比如：使用第三方的文件管理器的话，你选择的文件就不一定是图片了，

                     * * 这样的话，我们判断文件的后缀名 如果是图片格式的话，那么才可以

                     */

                    if (path.endsWith("jpg") || path.endsWith("png")) {

                        picPath = path;

                        Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));

                        showImageView.setImageBitmap(bitmap);


                        addImage();

                        if(bitmap!=null){

                            try{
                                ByteArrayOutputStream baos=new ByteArrayOutputStream();
                                bitmap.compress(Bitmap.CompressFormat.JPEG,100,baos);

                                saveImage(baos.toByteArray());

                            }catch (Exception e){

                            }

                        }



                    } else {

                        alert();

                    }

                } else {
                    alert();
                }
            } catch (Exception e) {

            }

        }

    }

    public static final int EXTERNAL_STORAGE_REQ_CODE = 10 ;

    void showQuan(){

        int permission = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // 请求权限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    EXTERNAL_STORAGE_REQ_CODE);
        }
    }



    private void alert() {



        Dialog dialog = new AlertDialog.Builder(this).setTitle("提示")

                .setMessage("您选择的不是有效的图片")

                .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {

                        picPath = null;

                    }

                }).create();

        dialog.show();

    }


    protected String getAbsoluteImagePath(Uri uri) {
        String[] proj = { MediaStore.Images.Media.DATA };
        @SuppressWarnings("deprecation")
        Cursor cursor = this.getContentResolver().query(uri, proj, null, null,
                null);

        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();

        return cursor.getString(column_index);
    }


    public Boolean saveImage(byte[] bytes)  {
        try {
            FileOutputStream outputStream = openFileOutput("head.jpg", MODE_PRIVATE);
            outputStream.write(bytes);
            outputStream.close();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    //读取本地文件
    public Bitmap readImage(){
        try {
            FileInputStream fis = openFileInput("head.jpg");
            byte[] bytes=new byte[fis.available()];
            fis.read(bytes);
            fis.close();
            return BitmapFactory.decodeByteArray(bytes,0,bytes.length);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    //上传图片到服务器上

    String avatar="";
    void addImage(){


        String userId="1";//业务类型
        String signa= UserBaseDatus.getInstance().getSign();

        final Map<String, String> params = new HashMap<String, String>();


        params.put("userId", userId);

        params.put("signa", signa);

        final Map<String, File> files = new HashMap<String, File>();

        File file=new File(picPath);

        files.put("file", file);

        final String strUrl = UserBaseDatus.getInstance().url+"api/app/user/updateAvatar";

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final String request =UserBaseDatus.getInstance().post(strUrl, params, files);

                    Log.d("requestaaa", request);

                    JSONObject jsonObject=new JSONObject(request);
                    avatar=jsonObject.getString("data");


                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }





}
