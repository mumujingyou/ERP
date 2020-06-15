package com.example.qunxin.erp.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qunxin.erp.BaseActivity;
import com.example.qunxin.erp.R;
import com.example.qunxin.erp.UserBaseDatus;
import com.example.qunxin.erp.fragment.GuanhuoFragment;
import com.example.qunxin.erp.fragment.GuankeFragment;
import com.example.qunxin.erp.fragment.IndexFragment;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener,
        NavigationView.OnNavigationItemSelectedListener{

     RadioGroup radioGroup;
    RadioButton indexButton;
    RadioButton guankeButton;
    RadioButton guanhuoButton;
    RadioButton guanzhangButton;
    ImageView leftHead;
    TextView leftName;

    public static MainActivity instance;


    private IndexFragment indexFragment;

    private IndexFragment mIndexFragment;
    private GuankeFragment mGuankeFragment;
    private GuanhuoFragment mGuanhuoFragment;
    //private GuanzhangFragment mGuanzhangFragment;
    FragmentTransaction transaction;
    Toolbar toolbar;

    NavigationView navigationView;
    DrawerLayout drawer;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        instance=this;
        intiView();
        setBaseDatus();
    }




    void intiView(){
        ViewGroup viewGroup=findViewById(R.id.nav_view);
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.nav_header_main,viewGroup);
        leftHead=view.findViewById(R.id.left_head);
        leftName=view.findViewById(R.id.left_name);
        tittleView=findViewById(R.id.tittle);


        radioGroup=findViewById(R.id.radioGroup);
       indexButton=findViewById(R.id.index);
       guankeButton=findViewById(R.id.guanke);
       guanhuoButton=findViewById(R.id.guanhuo);
       //guanzhangButton=findViewById(R.id.guanzhang);
       radioGroup.setOnCheckedChangeListener(this);
        transaction = getSupportFragmentManager().beginTransaction();
        if (mIndexFragment == null) {
            mIndexFragment = new IndexFragment();
            transaction.replace(R.id.fragment_container, mIndexFragment);
        } else {
            transaction.show(mIndexFragment);
        }
       transaction.commit();

        if(leftHead!=null){
            if(readImage()!=null){
                leftHead.setImageBitmap(readImage());
            }else {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        getImage(leftHead);
                    }
                }).start();
            }

        }

        if(leftName!=null){
            SharedPreferences read = getSharedPreferences("baseData", Activity.MODE_PRIVATE);
            leftName.setText(read.getString("userName",""));
        }


        SharedPreferences read = getSharedPreferences("baseData", Activity.MODE_PRIVATE);
        UserBaseDatus.getInstance().userId=read.getString("userId","1");

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(leftName!=null){
            SharedPreferences read = getSharedPreferences("baseData", Activity.MODE_PRIVATE);
            leftName.setText(read.getString("userName",""));
        }

        if(leftHead!=null){
            leftHead.setImageBitmap(readImage());

        }
    }

    void setBaseDatus(){
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(Gravity.LEFT);
            }
        });

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

       navigationView.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               SharedPreferences read = getSharedPreferences("baseData", Activity.MODE_PRIVATE);
               leftName.setText(read.getString("userName",""));
           }
       });


    }

    public boolean isAdd=false;

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

         transaction = getSupportFragmentManager().beginTransaction();
       // hideAllFragment(transaction);
        switch (checkedId) {
            case R.id.index:

                if(mIndexFragment==null){
                    mIndexFragment = new IndexFragment();
                }
                transaction.replace(R.id.fragment_container, mIndexFragment);
                isAdd=false;
                if(addView!=null){
                    tittleView.removeView(addView);
                }
                toolbar.setTitle("首页");
                break;
            case R.id.guanke:

                if(mGuankeFragment==null){
                    mGuankeFragment = new GuankeFragment();
                }
                transaction.replace(R.id.fragment_container, mGuankeFragment);
                isAdd=true;


                addView();

                toolbar.setTitle("管客");
                break;
            case R.id.guanhuo:
                if(mGuanhuoFragment==null){
                    mGuanhuoFragment = new GuanhuoFragment();
                }
                transaction.replace(R.id.fragment_container, mGuanhuoFragment);
                isAdd=false;
                if(addView!=null){
                    tittleView.removeView(addView);
                }
                toolbar.setTitle("管货");
                break;
//            case R.id.guanzhang:
//                if(mGuanzhangFragment==null){
//                    mGuanzhangFragment = new GuanzhangFragment();
//                }
//                transaction.replace(R.id.fragment_container, mGuanzhangFragment);
//                isAdd=false;
//                toolbar.setTitle("管账");
//                if(addView!=null){
//                    tittleView.removeView(addView);
//                }
//                break;
        }
        transaction.commit();
    }

    ImageView addView;
    RelativeLayout tittleView;


    public  void guankeFun(){
        UserBaseDatus.instance.isKehu=true;


        mGuankeFragment = new GuankeFragment();

        transaction = getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.fragment_container, mGuankeFragment);
        transaction.commit();
        RadioButton radioButton=(RadioButton) radioGroup.getChildAt(2);
        radioButton.setChecked(true);
        isAdd=true;

        toolbar.setTitle("管客");
    }


    public  void gongyingshangFun(){
        UserBaseDatus.instance.isKehu=false;


        mGuankeFragment = new GuankeFragment();

        transaction = getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.fragment_container, mGuankeFragment);
        transaction.commit();
        RadioButton radioButton=(RadioButton) radioGroup.getChildAt(2);
        radioButton.setChecked(true);
        isAdd=true;

        toolbar.setTitle("管客");
    }

    void addView(){
        //动态添加加号
        addView=new ImageView(this);
        addView.setImageResource(R.mipmap.add_btn);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
        layoutParams.rightMargin=30;
        addView.setLayoutParams(layoutParams);
        tittleView.addView(addView);

        addView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isAdd){

                    if(UserBaseDatus.getInstance().isKehu){
                        Intent intent=new Intent(MainActivity.this,AddKehuActivity.class);

                        startActivity(intent);
                    }else {
                        Intent intent=new Intent(MainActivity.this,AddGongyingshangActivity.class);
                        startActivity(intent);
                    }

                }
            }
        });
    }

    public void hideAllFragment(FragmentTransaction transaction){
        if(mIndexFragment!=null){
            transaction.hide(mIndexFragment);
        }
        if(mGuankeFragment!=null){
            transaction.hide(mGuankeFragment);
        }
        if(mGuanhuoFragment!=null){
            transaction.hide(mGuanhuoFragment);
        }
//        if(mGuanzhangFragment!=null){
//            transaction.hide(mGuanzhangFragment);
//        }
    }


    //按下返回键
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    //创建菜单选项
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    //菜单选项被选中调用
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if(id==R.id.action_add){
            if(isAdd){

                if(UserBaseDatus.getInstance().isKehu){
                    Intent intent=new Intent(MainActivity.this,AddKehuActivity.class);
                    startActivity(intent);
                }else {
                    Intent intent=new Intent(MainActivity.this,AddGongyingshangActivity.class);
                    startActivity(intent);
                }

            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //左边导航栏选项被选中时
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_personalInfo) {
            Intent intent=new Intent(MainActivity.this,PersonalInfoActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_settings) {
            Intent intent=new Intent(MainActivity.this,SettingsActivity.class);
            startActivity(intent);
        }else if (id == R.id.nav_baseDatus) {
            Intent intent=new Intent(MainActivity.this,BaseDatusActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

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



    void getImage(final ImageView imageView) {

        String avatar=UserBaseDatus.getInstance().avatar;
        final String contentType = "application/octet-stream";
        final String data = "?avatar="+avatar ;
        final String url = UserBaseDatus.getInstance().url + "api/app/upload/out"+data;
        final Bitmap bitmap = UserBaseDatus.getInstance().isSuccessGet(url, contentType);

        if(bitmap!=null){

            try{
                ByteArrayOutputStream baos=new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG,100,baos);

                saveImage(baos.toByteArray());

            }catch (Exception e){

            }

        }

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                imageView.setImageBitmap(bitmap);
            }
        });
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




    private long firstTime = 0;



    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        long secondTime = System.currentTimeMillis();



        if (keyCode == KeyEvent.KEYCODE_BACK) {

            if (secondTime - firstTime < 2000) {

                System.exit(0);

            } else {

                Toast.makeText(getApplicationContext(), "再按一次返回键退出", Toast.LENGTH_SHORT).show();

                firstTime = System.currentTimeMillis();

            }



            return true;

        }



        return super.onKeyDown(keyCode, event);

    }





}



