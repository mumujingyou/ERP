package com.example.qunxin.erp.activity;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qunxin.erp.R;
import com.example.qunxin.erp.UserBaseDatus;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AddJinhuoAfterActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher {


    TextView gongyingshangNameText;
    TextView cangkuNameText;
    TextView countText;
    TextView priceText;
    TextView danhaoText;
    TextView jingshourenText;
    TextView dateText;
    EditText qitafeiyongText;
    TextView sumAmountText;

    EditText remarksText;
    TextView jiesuanzhanghaoText;
    TextView fukuanText;
    View shangpinText;



    ImageButton addImage;
    View backBtn;
    View enterBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_jinhuo_after);
        initView();
        getDanHao();
        intiModel();
    }

    String supplier;
    String depot;
    String supplierName;
    String depotName;
    float price;
    int count;
    String dateList;
    void initView(){
        danhaoText=findViewById(R.id.add_jinhuo_danhao);
        gongyingshangNameText=findViewById(R.id.add_jinhuo_gongyingshang);
        cangkuNameText=findViewById(R.id.add_jinhuo_cangku);
        countText=findViewById(R.id.count);
        priceText=findViewById(R.id.price);
        jingshourenText=findViewById(R.id.name);
        shangpinText=findViewById(R.id.add_jinhuo_shangpin);

        dateText=findViewById(R.id.date);
        qitafeiyongText =findViewById(R.id.otherCase);
        sumAmountText =findViewById(R.id.sumAmount);
        addImage=findViewById(R.id.addImage);
        remarksText=findViewById(R.id.remarks);
        jiesuanzhanghaoText=findViewById(R.id.jiesuanzhanghu);
        fukuanText=findViewById(R.id.fukuanAmount);

        backBtn=findViewById(R.id.add_jinhuo_back);
        enterBtn=findViewById(R.id.enter);


        shangpinText.setOnClickListener(this);
        addImage.setOnClickListener(this);
        dateText.setOnClickListener(this);
        jiesuanzhanghaoText.setOnClickListener(this);
        fukuanText.setOnClickListener(this);
        backBtn.setOnClickListener(this);
        enterBtn.setOnClickListener(this);
        cangkuNameText.setOnClickListener(this);
        qitafeiyongText.addTextChangedListener(this);






    }

    void intiModel(){

        Intent intent=getIntent();
        supplier=intent.getStringExtra("supplier");
        depot=intent.getStringExtra("depot");
        supplierName=intent.getStringExtra("supplierName");
        depotName=intent.getStringExtra("depotName");
        price=intent.getIntExtra("price",0);
        count=intent.getIntExtra("count",0);
        dateList=intent.getStringExtra("lists");


        gongyingshangNameText.setText(supplierName);
        cangkuNameText.setText(depotName);
        priceText.setText("￥"+price);
        countText.setText(count+"件");
        sumAmountText.setText("￥"+price);
        fukuanText.setText(""+price);

        dateText.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
    }
    public static final int EXTERNAL_STORAGE_REQ_CODE = 10 ;

    void getDanHao(){
        UserBaseDatus.getInstance().getShangpinbianhao("1",AddJinhuoAfterActivity.this,danhaoText);


        int permission = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // 请求权限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    EXTERNAL_STORAGE_REQ_CODE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.add_jinhuo_shangpin:
                break;
            case R.id.addImage:
                Intent intent = new Intent();

                intent.setType("image/*");

                intent.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(intent, 1);
                break;
            case R.id.date:
                final Calendar ca = Calendar.getInstance();
                mYear = ca.get(Calendar.YEAR);
                mMonth = ca.get(Calendar.MONTH);
                mDay = ca.get(Calendar.DAY_OF_MONTH);

                showDialog(DATE_DIALOG);
                break;
            case R.id.jiesuanzhanghu:
                Intent intent1=new Intent(AddJinhuoAfterActivity.this,XuanZeZhanghuActivity.class);
                startActivityForResult(intent1,1);
                break;
            case R.id.add_jinhuo_back:
                finish();
                break;
            case R.id.enter:
                try {
                    enter();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.add_jinhuo_cangku:
                Intent intent2=new Intent(AddJinhuoAfterActivity.this,AddjinhuoYixuanshangpinActivity.class);
                String dateList=this.dateList;
                intent2.putExtra("dateList",dateList);
                intent2.putExtra("depot",depot);
                intent2.putExtra("depotName",depotName);
                intent2.putExtra("supplier",supplier);

                startActivityForResult(intent2,1);

                break;
        }
    }




    int mYear, mMonth, mDay;


    final int DATE_DIALOG = 1;

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG:
                return new DatePickerDialog(this, mdateListener, mYear, mMonth, mDay);
        }
        return null;
    }

    /**
     * 设置日期 利用StringBuffer追加
     */
    public void display() {

        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println("当前时间：" + sdf.format(d));
        String string=sdf.format(d);

        dateText.setText(new StringBuffer().append(mYear).append("-").append(mMonth+1).append("-").append(mDay).append(" ").append(string.split(" ")[1]));
    }

    private DatePickerDialog.OnDateSetListener mdateListener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            mYear = year;
            mMonth = monthOfYear;
            mDay = dayOfMonth;
            display();
        }
    };

    String picPath;
    String zhanghuID;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case 1:
                if(resultCode==1){
                    zhanghuID=data.getStringExtra("id");
                    jiesuanzhanghaoText.setText(data.getStringExtra("name"));
                }

                if(resultCode==3){
                    dateList=data.getStringExtra("dateList");
                    count=data.getIntExtra("count",count);
                    price=data.getFloatExtra("totalAmount",price);
                    depot=data.getStringExtra("depot");
                    depotName=data.getStringExtra("depotName");
                    textChange();
                }


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

                            if (path.endsWith("jpg") || path.endsWith("png")|| path.endsWith("jpeg")) {

                                picPath = path;

                                Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));

                                addImage.setImageBitmap(bitmap);

                                addImage();

                            } else {

                                alert();

                            }

                        } else {
                            alert();
                        }
                    } catch (Exception e) {

                    }

                }
            break;
        }

    }

    void textChange(){
        priceText.setText(price+"");

        countText.setText(count+"件");

        cangkuNameText.setText(depotName);

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

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        int cases=0;
        if("".equals(qitafeiyongText.getText().toString())){

        }else {
            cases=Integer.parseInt(qitafeiyongText.getText().toString());
        }

        float sum=price;
        float sumAmount=cases+sum;
        sumAmountText.setText("￥"+sumAmount);
        fukuanText.setText(""+sumAmount);
    }

    void addImage(){

        String busNo=danhaoText.getText().toString();
        String type="1";
        String signa=UserBaseDatus.getInstance().getSign();

        final Map<String, String> params = new HashMap<String, String>();

        params.put("busNo", busNo);

        params.put("type", type);

        params.put("signa", signa);

        final Map<String, File> files = new HashMap<String, File>();

        File file=new File(picPath);

        files.put("file", file);

        final String strUrl = UserBaseDatus.getInstance().url+"api/app/upload/add";

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final String request =UserBaseDatus.getInstance().post(strUrl, params, files);
                    Log.d("requestaaa", request);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    String data="";
    void enter() throws JSONException {

        String busScope=zhanghuID;
        String busTime=dateText.getText().toString();
        String buyAmount=price+"";
        String buyNo=danhaoText.getText().toString();
        float buyTotal=count;
        String ccy="";
        String createBy=UserBaseDatus.getInstance().userId;
        String createTime="";
        String dateList=this.dateList;
        JSONArray jsonArray=new JSONArray(dateList);
        int delFlag=0;
        String depot=this.depot;
        float fccAmount=0;
        String leader=jingshourenText.getText().toString();
        String otherAmount=qitafeiyongText.getText().toString();
        String payAmount=fukuanText.getText().toString();
        String proName="";
        int refundFlag=0;
        String remarks=remarksText.getText().toString();
        String signa=UserBaseDatus.getInstance().getSign();
        String supplier=this.supplier;
        String supplierName=this.supplierName;
        String sumAmount=sumAmountText.getText().toString();
        String totalAmount=sumAmount.substring(1,sumAmount.length());
        String updateBy="1";
        String updateTime="";


        JSONObject jsonObject=new JSONObject();
        jsonObject.put("busScope",busScope);
        jsonObject.put("busTime",busTime);
        jsonObject.put("buyAmount",buyAmount);
        jsonObject.put("buyNo",buyNo);
        jsonObject.put("buyTotal",buyTotal);
        jsonObject.put("ccy",ccy);
        jsonObject.put("createTime",createTime);
        jsonObject.put("dateList",jsonArray);
        jsonObject.put("createBy",createBy);
        jsonObject.put("delFlag",delFlag);
        jsonObject.put("depot",depot);
        jsonObject.put("fccAmount",fccAmount);
        jsonObject.put("leader",leader);
        jsonObject.put("otherAmount",otherAmount);
        jsonObject.put("proName",proName);
        jsonObject.put("refundFlag",refundFlag);
        jsonObject.put("remarks",remarks);
        jsonObject.put("supplier",supplier);
        jsonObject.put("supplierName",supplierName);
        jsonObject.put("totalAmount",totalAmount);
        jsonObject.put("updateBy",updateBy);
        jsonObject.put("updateTime",updateTime);
        jsonObject.put("signa",signa);
        jsonObject.put("payAmount",payAmount);


        final String jsonString = jsonObject.toString();



        final String contentTypeList = "application/json";
        final String url=UserBaseDatus.getInstance().url+"api/app/buy/add";

        new Thread(new Runnable() {
            @Override
            public void run() {
                Map map=UserBaseDatus.getInstance().isSuccessPost(url, jsonString, contentTypeList);
                if((boolean)(map.get("isSuccess"))){
                    Intent intent=new Intent(AddJinhuoAfterActivity.this,AddjinhuodanSuccessActivity.class);
                    startActivity(intent);
                    finish();
                }else {
                    JSONObject jsonObject= (JSONObject) map.get("json");
                    try {
                        data =jsonObject.getString("data");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(AddJinhuoAfterActivity.this, data, Toast.LENGTH_SHORT).show();
                            data="";
                        }
                    });
                }


            }
        }).start();

    }

}
