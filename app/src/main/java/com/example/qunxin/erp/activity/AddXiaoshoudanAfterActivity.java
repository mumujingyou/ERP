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

public class AddXiaoshoudanAfterActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher {

    TextView kehuNameText;
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
        setContentView(R.layout.activity_add_xiaoshoudan_after);
        initView();

        intiModel();
    }



    String kehuId;
    String depot;
    String kehuName;
    String depotName;
    float price;
    int count;
    String sellDatailList;
    void initView(){
        danhaoText=findViewById(R.id.danhao);
        kehuNameText=findViewById(R.id.custName);
        cangkuNameText=findViewById(R.id.cangku);
        countText=findViewById(R.id.count);
        priceText=findViewById(R.id.price);
        jingshourenText=findViewById(R.id.name);
        shangpinText=findViewById(R.id.shangpin);

        dateText=findViewById(R.id.date);
        qitafeiyongText =findViewById(R.id.otherCase);
        sumAmountText =findViewById(R.id.sumAmount);
        addImage=findViewById(R.id.addImage);
        remarksText=findViewById(R.id.remarks);
        jiesuanzhanghaoText=findViewById(R.id.jiesuanzhanghu);
        fukuanText=findViewById(R.id.fukuanAmount);

        backBtn=findViewById(R.id.back);
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

    String busNo;
    void intiModel(){

        Intent intent=getIntent();
        kehuId=intent.getStringExtra("kehuId");
        depot=intent.getStringExtra("depot");
        kehuName=intent.getStringExtra("kehuName");
        depotName=intent.getStringExtra("depotName");
        price=intent.getIntExtra("price",0);
        count=intent.getIntExtra("count",0);
        sellDatailList=intent.getStringExtra("lists");
        busNo=intent.getStringExtra("busNo");




        kehuNameText.setText(kehuName);
        cangkuNameText.setText(depotName);
        priceText.setText("￥"+price);
        countText.setText(count+"件");
        sumAmountText.setText("￥"+price);
        fukuanText.setText(""+price);

        dateText.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        danhaoText.setText(busNo);
    }
    public static final int EXTERNAL_STORAGE_REQ_CODE = 10 ;



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.shangpin:
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
                Intent intent1=new Intent(AddXiaoshoudanAfterActivity.this,XuanZeZhanghuActivity.class);
                startActivityForResult(intent1,1);
                break;
            case R.id.back:
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
                    sellDatailList=data.getStringExtra("dateList");
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
        String type="13";
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
        "sellDetailList": [
        {
                "buyPrice": 5,
                "createBy": "1",
                "createTime": "2019-05-22 17:41:47",
                "id": "247e7ccafebb419599d6a5f187cb290e",
                "norms": "DP20",
                "params": {},
                "proId": "80cd895c948440eeb03e33022c606c02",
                "proName": "塑料配件",
                "proNo": "SP20190327178175000",
                "property": "塑料",
                "remark": "string",
                "remarks": "哈哈哈",
                "searchValue": "string",
                "sellId": "681c2758fdac414eba7854a05213fa39",
                "sellNo": "XSD20190521747709003",
                "sellPrice": 5,
                "sellTotal": 1,
                "total": 1,
                "totalAmount": 5,
                "unit": "个",
                "updateBy": "1",
                "updateTime": "2019-05-22 17:41:47"
        }
  ],
        "sells": {
                "amount": 5,
                "busScope": "1",
                "busTime": "2019-05-21 09:24:20",
                "createBy": "1",
                "createTime": "2019-05-22 17:49:04",
                "creatorName": "管理员",
                "cust": "bf0b57aab5df40d085ea5d38a240df1f",
                "custName": "哈哈",
                "delFlag": 0,
                "dept": 0,
                "fccAmount": 0,
                "gop": 0,
                "id": "0ec175b1ce284930ba957870d62d5286",
                "leader": "5",
                "leaderName": "哈哈",
                "otherAmount": 0,
                "outDepot": "89b8f79d7ae344809bec8a45acce0629",
                "outDepotName": "哈哈仓库",
                "pageNum": 1,
                "pageSize": 10,
                "params": {},
                "payerAccountName": "哈哈",
                "payerAccountNo": "1",
                "payerBankName": "中国工商银行",
                "proName": "塑料配件",
                "recAmount": 0,
                "rejectFlag": 0,
                "remark": "string",
                "remarks": "哈哈哈哈哈哈哈哈",
                "searchValue": "string",
                "sellNo": "SO20190521989970002",
                "signa": "7a9ae52b9ead9aa58547fc0be3870fa76be93296f148047ad5a7736615abd968",
                "status": "0",
                "total": 1,
                "updateBy": "1",
                "updateName": "哈哈",
                "updateTime": "2019-05-22 17:49:07"
    },
        "signa": "7a9ae52b9ead9aa58547fc0be3870fa76be93296f148047ad5a7736615abd968"
    }

    */

    String data="";
    void enter() throws JSONException {

        String amountStr=sumAmountText.getText().toString();

        String amount=amountStr.substring(1);
        String busScope=zhanghuID;
        String createBy=UserBaseDatus.getInstance().userId;
        String createTime=dateText.getText().toString();
        String creatorName=UserBaseDatus.getInstance().cretorName;
        String cust=kehuId;
        String custName=kehuName;
        String delFlag="0";
        String dept="0";
        String fccAmount="";
        String gop="0";
        String id="";
        String leader=jingshourenText.getText().toString();
        String leaderName=jingshourenText.getText().toString();
        String otherAmount=qitafeiyongText.getText().toString();
        String pageNum="1";
        String pageSize="10";
        String payerAccountName=jiesuanzhanghaoText.getText().toString();
        String payerAccountNo=zhanghuID;
        String payerBankName="";
        String recAmount=fukuanText.getText().toString();
        String rejectFlag="0";
        String remark="";
        String remarks=remarksText.getText().toString();
        String searchValue="";
        String sellNo=danhaoText.getText().toString();
        String signa=UserBaseDatus.getInstance().getSign();
        String status="0";
        String total=count+"";
        String updateBy=UserBaseDatus.getInstance().userId;
        String updateName=UserBaseDatus.getInstance().userId;
        String updateTime=dateText.getText().toString();

        JSONObject jsonObject1=new JSONObject();
        jsonObject1.put("amount",amount);
        jsonObject1.put("busScope",busScope);
        jsonObject1.put("busTime",dateText.getText().toString());
        jsonObject1.put("createBy",createBy);
        jsonObject1.put("createTime",createTime);
        jsonObject1.put("creatorName",creatorName);
        jsonObject1.put("cust",cust);
        jsonObject1.put("custName",custName);
        jsonObject1.put("delFlag",delFlag);
        jsonObject1.put("dept",dept);
        jsonObject1.put("fccAmount",fccAmount);
        jsonObject1.put("gop",gop);
        jsonObject1.put("id",id);
        jsonObject1.put("leaderName",leaderName);
        jsonObject1.put("leader",leader);
        jsonObject1.put("otherAmount",otherAmount);
        jsonObject1.put("pageNum",pageNum);
        jsonObject1.put("pageSize",pageSize);
        jsonObject1.put("payerAccountName",payerAccountName);
        jsonObject1.put("payerAccountNo",payerAccountNo);
        jsonObject1.put("payerBankName",payerBankName);
        jsonObject1.put("proName","");
        jsonObject1.put("recAmount",recAmount);
        jsonObject1.put("rejectFlag",rejectFlag);
        jsonObject1.put("remark",remark);
        jsonObject1.put("remarks",remarks);
        jsonObject1.put("searchValue",searchValue);
        jsonObject1.put("sellNo",sellNo);
        jsonObject1.put("signa",signa);
        jsonObject1.put("status",status);
        jsonObject1.put("total",total);
        jsonObject1.put("updateBy",updateBy);
        jsonObject1.put("updateName",updateName);
        jsonObject1.put("outDepot",depot);
        jsonObject1.put("outDepotName",depotName);

        JSONArray jsonArray=new JSONArray(sellDatailList);

        JSONObject jsonObject=new JSONObject();
        jsonObject.put("sells",jsonObject1);
        jsonObject.put("signa",signa);
        jsonObject.put("sellDetailList",jsonArray);



        final String jsonString = jsonObject.toString();



        final String contentTypeList = "application/json";
        final String url=UserBaseDatus.getInstance().url+"api/sells/add";

        Log.d("jsonString", jsonString);
        Log.d("cust", cust);
        new Thread(new Runnable() {
            @Override
            public void run() {
                Map map=UserBaseDatus.getInstance().isSuccessPost(url, jsonString, contentTypeList);
                if((boolean)(map.get("isSuccess"))){
                    Intent intent=new Intent(AddXiaoshoudanAfterActivity.this,AddXiaoshoudanSuccessActivity.class);
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
                            Toast.makeText(AddXiaoshoudanAfterActivity.this, data, Toast.LENGTH_SHORT).show();
                            data="";
                        }
                    });
                }
            }
        }).start();

    }

}
