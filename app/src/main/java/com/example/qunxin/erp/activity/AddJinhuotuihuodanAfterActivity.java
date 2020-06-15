package com.example.qunxin.erp.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
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

public class AddJinhuotuihuodanAfterActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher {



    TextView gongyingshangNameText;
    TextView cangkuNameText;
    TextView totalText;
    TextView totalAmountText;
    TextView danhaoText;
    TextView jingshourenText;
    TextView dateText;
    EditText qitafeiyongText;

    TextView priceText;


    EditText remarksText;
    TextView jiesuanzhanghaoText;
    TextView tuikuanText;
    View shangpinText;



    ImageButton addImage;
    View backBtn;
    View enterBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_jinhuotuihuodan_after);


        initView();
        intiModel();
    }



    void initView() {
        danhaoText = findViewById(R.id.add_jinhuo_danhao);
        gongyingshangNameText = findViewById(R.id.add_jinhuo_gongyingshang);
        cangkuNameText = findViewById(R.id.depotName);
        totalText = findViewById(R.id.count);
        priceText = findViewById(R.id.price);
        jingshourenText = findViewById(R.id.name);
        shangpinText = findViewById(R.id.add_jinhuo_shangpin);

        dateText = findViewById(R.id.date);
        qitafeiyongText = findViewById(R.id.otherCase);
        totalAmountText = findViewById(R.id.sumAmount);
        addImage = findViewById(R.id.addImage);
        remarksText = findViewById(R.id.remarks);
        jiesuanzhanghaoText = findViewById(R.id.jiesuanzhanghu);
        tuikuanText = findViewById(R.id.tuikuanAmount);

        backBtn = findViewById(R.id.add_jinhuo_back);
        enterBtn = findViewById(R.id.enter);


        shangpinText.setOnClickListener(this);
        addImage.setOnClickListener(this);
        dateText.setOnClickListener(this);
        jiesuanzhanghaoText.setOnClickListener(this);
        tuikuanText.setOnClickListener(this);
        backBtn.setOnClickListener(this);
        enterBtn.setOnClickListener(this);

        qitafeiyongText.addTextChangedListener(this);

    }

        String supplier;
        String depot;
        String supplierName;
        String depotName;
        float price;
        int count;
        String rList;
        String leader;
        String busNo;
        String buyId;

        boolean isGuanLianJinhuodan=false;
    void intiModel(){

        Intent intent=getIntent();
        supplier=intent.getStringExtra("supplier");
        depot=intent.getStringExtra("depot");
        supplierName=intent.getStringExtra("supplierName");
        depotName=intent.getStringExtra("depotName");
        price=intent.getFloatExtra("price",0.0f);
        count=intent.getIntExtra("count",0);
        rList=intent.getStringExtra("lists");
        dateList=intent.getStringExtra("lists");
        leader=intent.getStringExtra("leader");
        busNo=intent.getStringExtra("busNo");
        buyId=intent.getStringExtra("id");
        isGuanLianJinhuodan=intent.getBooleanExtra("isGuanLianJinhuodan",false);


        jingshourenText.setText(leader);
        danhaoText.setText(busNo);
        gongyingshangNameText.setText(supplierName);
        cangkuNameText.setText(depotName);
        priceText.setText("￥"+price);
        totalText.setText(count+"件");
        totalAmountText.setText("￥"+price);
        tuikuanText.setText(""+price);
        dateText.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
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
                Intent intent1=new Intent(AddJinhuotuihuodanAfterActivity.this,XuanZeZhanghuActivity.class);
                startActivityForResult(intent1,1);
                break;
            case R.id.add_jinhuo_back:
                finish();
                break;
            case R.id.enter:
                if(isGuanLianJinhuodan){
                    enterGuanlian();
                }else {
                    enterNoguanlian();
                }
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
                    rList=data.getStringExtra("dateList");
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

        totalText.setText(count+"件");



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
        totalAmountText.setText(""+sumAmount);
        tuikuanText.setText(""+sumAmount);
    }

    void addImage(){

        String busNo=danhaoText.getText().toString();
        String type="2";
        String signa= UserBaseDatus.getInstance().getSign();

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


    //不关联
    void enterNoguanlian(){

/*
        {
            "rList": [
            {
                "price": 100,
                    "proId": "d2ce8916c42449e6ac330bc7f6bbb6dd",
                    "remarks": "这个是说明",
                    "total": 1
            }
  ],
            "refundAppSaveNoVo": {
            "busScope": "1",
                    "busTime": "2019-05-24 17:17:06",
                    "depot": "f828a4adad1749caac4387406a69fe91",
                    "leader": "seven",
                    "otherAmount": 1,
                    "payAmount": 101,
                    "refundNo": "JTD20190912847044022",
                    "remarks": "这个是备注",
                    "supplier": "d858012ced4b4cc7a686ecd9b54fd3fb"
        },
            "signa": "450b6ea3a8ac69588df143de38fbe479c4750bf8d723e9114df454f5d5f9ba36",
                "userId": "1"
        }


*/
        String signa=UserBaseDatus.getInstance().getSign();
        String userId=UserBaseDatus.getInstance().userId;

        Map<String,String> map=new HashMap<>();
        map.put("busScope",zhanghuID);
        map.put("busTime",dateText.getText().toString());
        map.put("otherAmount",qitafeiyongText.getText().toString());
        map.put("leader",jingshourenText.getText().toString());
        map.put("payAmount",tuikuanText.getText().toString());
        map.put("refundNo",danhaoText.getText().toString());
        map.put("remarks",remarksText.getText().toString());
        map.put("depot",depot);
        map.put("supplier",supplier);
        JSONObject jsonObject1=new JSONObject(map);

        JSONObject jsonObject=new JSONObject();
        try {


            JSONArray jsonArray=new JSONArray(rList);

            jsonObject.put("signa",signa);
            jsonObject.put("userId",userId);
            jsonObject.put("refundAppSaveNoVo",jsonObject1);
            jsonObject.put("rList",jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        final String jsonString = jsonObject.toString();

        final String contentTypeList = "application/json";
        //final String url=UserBaseDatus.getInstance().url+"api/app/refund/addAppRefundNoBuy";
        final String url=UserBaseDatus.getInstance().url+"api/app/refund/addAppRefundNoBuy";

        Log.d("jsonString", jsonString);
        new Thread(new Runnable() {
            @Override
            public void run() {
                Map map=UserBaseDatus.getInstance().isSuccessPost(url, jsonString, contentTypeList);
                if((boolean)(map.get("isSuccess"))){
                    Intent intent=new Intent(AddJinhuotuihuodanAfterActivity.this,AddjinhuotuihuodanSuccessActivity.class);
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
                            Toast.makeText(AddJinhuotuihuodanAfterActivity.this, data, Toast.LENGTH_SHORT).show();
                            data="";
                        }
                    });
                }
            }
        }).start();

    }





    //关联

    String data="";
    String dateList="";
    void enterGuanlian(){
        /*
        {
            "rufundAppSaveDetailVoList": [
            {
                "proId": "28c849ba0d2745bbb595def92df50bbf",
                    "remarks": "这个是备注"
            }
  ],
            "rufundAppSaveVo": {
            "busScope": "1",
                    "busTime": "2019-05-24 11:14:49",
                    "buyId": "20d5019845ae4705939040aa44bbe5d2",
                    "leader": "seven",
                    "payAmount": 100,
                    "refundNo": "JTD20190524658097000",
                    "remarks": "这是备注"
        },
            "signa": "5b008d46e0d3ed1b8f88239044a053c74d8ac2b562b1bcd0877f8f5817a82ea0",
                "userId": "1"
        }

        */
        String signa=UserBaseDatus.getInstance().getSign();
        String userId="1";

        Map<String,String> map=new HashMap<>();
        map.put("busScope",zhanghuID);
        map.put("busTime",dateText.getText().toString());
        map.put("buyId",buyId);
        map.put("leader",jingshourenText.getText().toString());
        map.put("payAmount",tuikuanText.getText().toString());
        map.put("refundNo",danhaoText.getText().toString());
        map.put("remarks",remarksText.getText().toString());
        JSONObject jsonObject1=new JSONObject(map);

        JSONObject jsonObject=new JSONObject();
        try {

            JSONArray jsonArray=new JSONArray(rList);

            jsonObject.put("signa",signa);
            jsonObject.put("userId",userId);
            jsonObject.put("rufundAppSaveVo",jsonObject1);
            jsonObject.put("rufundAppSaveDetailVoList",jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        final String jsonString = jsonObject.toString();

        final String contentTypeList = "application/json";
        final String url=UserBaseDatus.getInstance().url+"api/app/refund/addAppRefundBuy";

        Log.d("jsonString", jsonString);
        new Thread(new Runnable() {
            @Override
            public void run() {
                Map map=UserBaseDatus.getInstance().isSuccessPost(url, jsonString, contentTypeList);
                if((boolean)(map.get("isSuccess"))){
                    Intent intent=new Intent(AddJinhuotuihuodanAfterActivity.this,AddjinhuotuihuodanSuccessActivity.class);
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
                            Toast.makeText(AddJinhuotuihuodanAfterActivity.this, data, Toast.LENGTH_SHORT).show();
                            data="";
                        }
                    });
                }
            }
        }).start();
    }

}
