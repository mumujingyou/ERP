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

public class AddDiaobodanAfterActivity extends AppCompatActivity implements View.OnClickListener {

    TextView refundDepotNameText;
    TextView buyDepotNameText;
    TextView countText;
    TextView danhaoText;
    TextView jingshourenText;
    TextView dateText;
    EditText qitafeiyongText;

    EditText remarksText;
    TextView jiesuanzhanghaoText;
    View shangpinText;



    ImageButton addImage;
    View backBtn;
    View saveBtn;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_diaobodan_after);
        initView();
        getDanHao();
        intiModel();
    }


    int count;
    String refundDepotName="";
    String refundDepot="";
    String buyDepotName="";
    String buyDepot="";
    String aList="";


    void initView(){
        danhaoText=findViewById(R.id.busNo);
        refundDepotNameText=findViewById(R.id.refundDepotName);
        buyDepotNameText=findViewById(R.id.buyDepotName);
        countText=findViewById(R.id.total);
        jingshourenText=findViewById(R.id.leader);
        shangpinText=findViewById(R.id.shangpin);

        dateText=findViewById(R.id.dateText);
        qitafeiyongText =findViewById(R.id.otherCase);
        addImage=findViewById(R.id.addImage);
        remarksText=findViewById(R.id.remarks);
        jiesuanzhanghaoText=findViewById(R.id.jiesuanzhanghu);

        backBtn=findViewById(R.id.back);
        saveBtn=findViewById(R.id.save);


        shangpinText.setOnClickListener(this);
        addImage.setOnClickListener(this);
        dateText.setOnClickListener(this);
        jiesuanzhanghaoText.setOnClickListener(this);
        backBtn.setOnClickListener(this);
        saveBtn.setOnClickListener(this);

    }


    void intiModel(){

        Intent intent=getIntent();

        count=intent.getIntExtra("count",0);
        refundDepot=intent.getStringExtra("refundDepot");
        refundDepotName=intent.getStringExtra("refundDepotName");
        buyDepot=intent.getStringExtra("buyDepot");
        buyDepotName=intent.getStringExtra("buyDepotName");
        aList=intent.getStringExtra("aList");

        countText.setText(count+"件");
        refundDepotNameText.setText(refundDepotName);
        buyDepotNameText.setText(buyDepotName);
        dateText.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
    }
    public static final int EXTERNAL_STORAGE_REQ_CODE = 10 ;

    void getDanHao(){
        UserBaseDatus.getInstance().getShangpinbianhao("10",AddDiaobodanAfterActivity.this,danhaoText);
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
            case R.id.dateText:
                final Calendar ca = Calendar.getInstance();
                mYear = ca.get(Calendar.YEAR);
                mMonth = ca.get(Calendar.MONTH);
                mDay = ca.get(Calendar.DAY_OF_MONTH);

                showDialog(DATE_DIALOG);
                break;
            case R.id.jiesuanzhanghu:
                Intent intent1=new Intent(AddDiaobodanAfterActivity.this,XuanZeZhanghuActivity.class);
                startActivityForResult(intent1,1);
                break;
            case R.id.back:
                finish();
                break;
            case R.id.save:
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
        String type="10";
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
  "aList": [
    {
      "proId": "28c849ba0d2745bbb595def92df50bbf",
      "remarks": "说明",
      "total": 1
    }
  ],
  "allocationAppSaveVo": {
    "allocationNo": "DB20190530568940001",
    "busScope": "1",
    "busTime": "2019-05-29 14:03:10",
    "buyDepot": "89b8f79d7ae344809bec8a45acce0629",
    "leader": "经手人",
    "otherAmount": 1,
    "refundDepot": "2efa30c5b2464dee904f6720d54c2582",
    "remarks": "备注"
  },
  "signa": "184fc66d751f5b3063cbee511c1171bad77f723ba8818768cbe29bc77a1211a7",
  "userId": "1"
}





     */




    String data="";
    void enter() throws JSONException {


        JSONObject jsonObject=new JSONObject();
        jsonObject.put("allocationNo",danhaoText.getText().toString());
        jsonObject.put("busScope",zhanghuID);
        jsonObject.put("busTime",dateText.getText().toString());
        jsonObject.put("buyDepot",buyDepot);
        jsonObject.put("leader",jingshourenText.getText().toString());
        jsonObject.put("otherAmount",qitafeiyongText.getText().toString());
        jsonObject.put("refundDepot",refundDepot);
        jsonObject.put("remarks",remarksText.getText().toString());


        JSONArray jsonArray=new JSONArray(aList);
        JSONObject json=new JSONObject();
        json.put("aList",jsonArray);
        json.put("allocationAppSaveVo",jsonObject);
        json.put("signa",UserBaseDatus.getInstance().getSign());
        json.put("userId",UserBaseDatus.getInstance().userId);


        final String jsonString = json.toString();
        Log.d("jsonString", jsonString);


        final String contentTypeList = "application/json";
        final String url=UserBaseDatus.getInstance().url+"api/app/allocation/addAppAllocation";

        new Thread(new Runnable() {
            @Override
            public void run() {
                Map map=UserBaseDatus.getInstance().isSuccessPost(url, jsonString, contentTypeList);
                if((boolean)(map.get("isSuccess"))){
                    Intent intent=new Intent(AddDiaobodanAfterActivity.this,AddDiaoboSuccessActivity.class);
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
                            Toast.makeText(AddDiaobodanAfterActivity.this, data, Toast.LENGTH_SHORT).show();
                            data="";
                        }
                    });
                }


            }
        }).start();

    }
}
