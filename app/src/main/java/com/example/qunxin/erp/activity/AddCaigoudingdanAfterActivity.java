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

public class AddCaigoudingdanAfterActivity extends AppCompatActivity implements View.OnClickListener {



    TextView gongyingshangNameText;
    TextView countText;
    TextView priceText;
    TextView danhaoText;
    TextView dateText;

    EditText remarksText;
    View shangpinText;



    ImageButton addImage;
    View backBtn;
    View enterBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_caigoudingdan_after);
        initView();
        getDanHao();
        intiModel();
    }

    String supplier;
    String supplierName;
    String depotName;
    float price;
    int count;
    String purchaseDetailVoList;
    void initView(){
        danhaoText=findViewById(R.id.add_caigoudingdan_danhao);
        gongyingshangNameText=findViewById(R.id.add_caigoudingdan_gongyingshang);
        countText=findViewById(R.id.add_caigoudingdan_count);
        priceText=findViewById(R.id.add_caigoudingdan_price);
        shangpinText=findViewById(R.id.add_caigoudingdan_shangpin);

        dateText=findViewById(R.id.add_caigoudingdan_daohuoriqi);
        addImage=findViewById(R.id.add_caigoudingdan_addImage);
        remarksText=findViewById(R.id.add_caigoudingdan_remarks);

        backBtn=findViewById(R.id.add_caigoudingdan_back);
        enterBtn=findViewById(R.id.enter);


        shangpinText.setOnClickListener(this);
        addImage.setOnClickListener(this);
        dateText.setOnClickListener(this);
        backBtn.setOnClickListener(this);
        enterBtn.setOnClickListener(this);






    }

    String busNo;
    void intiModel(){

        Intent intent=getIntent();
        supplier=intent.getStringExtra("supplier");
        supplierName=intent.getStringExtra("supplierName");
        price=intent.getIntExtra("price",0);
        count=intent.getIntExtra("count",0);
        purchaseDetailVoList=intent.getStringExtra("lists");
        busNo=intent.getStringExtra("busNo");

        gongyingshangNameText.setText(supplierName);
        priceText.setText("￥"+price);
        countText.setText(count+"件");

        dateText.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        danhaoText.setText(busNo);
    }
    public static final int EXTERNAL_STORAGE_REQ_CODE = 10 ;

    void getDanHao(){



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
            case R.id.add_caigoudingdan_shangpin:
                Intent intent1=new Intent(AddCaigoudingdanAfterActivity.this,AddcaigouYixuanshangpinActivity.class);
                intent1.putExtra("supplier",supplier);
                intent1.putExtra("dateList",purchaseDetailVoList);
                startActivityForResult(intent1,1);
                break;
            case R.id.add_caigoudingdan_addImage:
                Intent intent = new Intent();

                intent.setType("image/*");

                intent.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(intent, 1);
                break;
            case R.id.add_caigoudingdan_daohuoriqi:
                final Calendar ca = Calendar.getInstance();
                mYear = ca.get(Calendar.YEAR);
                mMonth = ca.get(Calendar.MONTH);
                mDay = ca.get(Calendar.DAY_OF_MONTH);

                showDialog(DATE_DIALOG);
                break;

            case R.id.add_caigoudingdan_back:
                finish();
                break;
            case R.id.enter:
                try {
                    enter();
                } catch (JSONException e) {
                    e.printStackTrace();
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
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case 1:

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
                if(resultCode==3){
                    purchaseDetailVoList=data.getStringExtra("dateList");
                    int count=data.getIntExtra("count",0);
                    float totalAmount=data.getFloatExtra("totalAmount",0);
                    countText.setText(count+"件");
                    priceText.setText("￥"+totalAmount);
                }

                break;
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



    void addImage(){

        String busNo=danhaoText.getText().toString();
        String type="22";
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

    /*

    {
        "purchaseDetailVoList": [
        {
                "actualPurchaseQuantity": 5,
                "applicationAllowance": 0,
                "createBy": "1",
                "createTime": "2019-06-06 14:48:34",
                "delFlag": 1,
                "deliveryDate": "2019-06-06 14:48:39",
                "id": "3bdfe1dbda9240cf9c07506602d9babc",
                "marketAmount": 2,
                "norms": "002",
                "orderQuantity": 5,
                "params": {},
                "proId": "01b1314e49c44cd991c57570947033e7",
                "proName": "PP",
                "purchaseAmount": 2,
                "purchaseId": "712536166e0442db85b37c9565d5b933",
                "remark": "string",
                "remarks": "哈哈",
                "searchValue": "string",
                "supplier": "d858012ced4b4cc7a686ecd9b54fd3fb",
                "unit": "斤",
                "updateBy": "1",
                "updateTime": "2019-06-06 14:49:11"
        }
  ],
        "purchaseVo": {
                "buyer": "等等",
                "createBy": "1",
                "createTime": "2019-06-06 14:50:01",
                "delFlag": 0,
                "deliveryDate": "2019-06-06 14:49:49",
                "id": "460fe70b3c5042ac9790d47184c6a8af",
                "marketAmount": 0,
                "nowTime": "2019-06-06 14:50:01",
                "pageNum": 1,
                "pageSize": 10,
                "params": {},
                "proName": "PP",
                "purchaseAmount": 0,
                "purchaseNo": "PC20190606770361001",
                "remark": "string",
                "searchValue": "string",
                "signa": "184fc66d751f5b3063cbee511c1171bad77f723ba8818768cbe29bc77a1211a7",
                "status": "0",
                "supplier": "d858012ced4b4cc7a686ecd9b54fd3fb",
                "total": 2,
                "unit": "哈哈",
                "updateBy": "1",
                "updateTime": "2019-06-06 14:50:05"
             },
                "signa": "5b008d46e0d3ed1b8f88239044a053c74d8ac2b562b1bcd0877f8f5817a82ea0",
                "userId": "1"
            }

    */




    String data="";
   void enter() throws JSONException {





       Map<String,String> map=new HashMap<>();
       map.put("buyer","");
       map.put("createBy",UserBaseDatus.getInstance().userId);
       map.put("createTime",dateText.getText().toString());
       map.put("delFlag","0");
       map.put("deliveryDate",dateText.getText().toString());
       map.put("id","");
       map.put("marketAmount","");
       map.put("nowTime",dateText.getText().toString());
       map.put("pageNum","1");
       map.put("pageSize","10");
       map.put("proName","PP");
       String price=priceText.getText().toString();
       map.put("purchaseAmount",price.substring(1));
       map.put("purchaseNo",danhaoText.getText().toString());
       map.put("remark",remarksText.getText().toString());
       map.put("searchValue","");
       map.put("signa",UserBaseDatus.getInstance().getSign());
       map.put("supplier",supplier);
       String total=countText.getText().toString();
       map.put("total",total.substring(0,total.length()-1));

       map.put("unit","");
       map.put("status","0");
       map.put("updateBy","1");
       map.put("updateTime",dateText.getText().toString());
       JSONObject jsonObject1=new JSONObject(map);

       JSONArray jsonArray=new JSONArray(purchaseDetailVoList);



       JSONObject jsonObject=new JSONObject();
       jsonObject.put("signa",UserBaseDatus.getInstance().getSign());
       jsonObject.put("userId",UserBaseDatus.getInstance().userId);
       jsonObject.put("purchaseVo",jsonObject1);
       jsonObject.put("purchaseDetailVoList",jsonArray);

        final String jsonString = jsonObject.toString();

       Log.d("jsonString", jsonString);

        final String contentTypeList = "application/json";
        final String url=UserBaseDatus.getInstance().url+"api/app/purchases/addPurchaseApp";

        new Thread(new Runnable() {
            @Override
            public void run() {
                Map map=UserBaseDatus.getInstance().isSuccessPost(url, jsonString, contentTypeList);
                if((boolean)(map.get("isSuccess"))){
                    Intent intent=new Intent(AddCaigoudingdanAfterActivity.this,AddCaigoudingdanSuccessActivity.class);
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
                            Toast.makeText(AddCaigoudingdanAfterActivity.this, data, Toast.LENGTH_SHORT).show();
                            data="";
                        }
                    });
                }
            }
        }).start();

    }

}
