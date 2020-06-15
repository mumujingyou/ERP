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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
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

public class AddPandiancangkuAfterActivity extends AppCompatActivity implements View.OnClickListener {

    TextView countText;
    TextView danhaoText;
    TextView dateText;
    TextView depotNameText;



    EditText remarksText;
    EditText leaderText;
    ImageButton addImage;
    View backBtn;
    View enterBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pandiancangku_after);
        initView();
        intiModel();
    }

    String supplier;
    String supplierName;
    String depotName;
    float price;
    int count;
    String sList;
    void initView(){
        danhaoText=findViewById(R.id.busNo);
        countText=findViewById(R.id.count);
        leaderText=findViewById(R.id.leader);
        depotNameText=findViewById(R.id.depotName);
        dateText=findViewById(R.id.busTime);
        addImage=findViewById(R.id.addImage);
        remarksText=findViewById(R.id.remarks);

        backBtn=findViewById(R.id.back);
        enterBtn=findViewById(R.id.enter);


        addImage.setOnClickListener(this);
        dateText.setOnClickListener(this);
        backBtn.setOnClickListener(this);
        enterBtn.setOnClickListener(this);



        UserBaseDatus.getInstance().getShangpinbianhao("4",AddPandiancangkuAfterActivity.this,danhaoText);




    }

    String busNo;
    String depot;
    void intiModel(){

        Intent intent=getIntent();

        count=intent.getIntExtra("count",0);
        depotName=intent.getStringExtra("depotName");
        depot=intent.getStringExtra("depot");
        sList=intent.getStringExtra("lists");


        countText.setText(count+"件");
        depotNameText.setText(depotName);
        dateText.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
    }
    public static final int EXTERNAL_STORAGE_REQ_CODE = 10 ;



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.count:
                Intent intent1=new Intent(AddPandiancangkuAfterActivity.this,AddcaigouYixuanshangpinActivity.class);
                intent1.putExtra("supplier",supplier);
                intent1.putExtra("dateList",sList);
                startActivityForResult(intent1,1);
                break;
            case R.id.addImage:
                Intent intent = new Intent();

                intent.setType("image/*");

                intent.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(intent, 1);
                break;
            case R.id.busTime:
                final Calendar ca = Calendar.getInstance();
                mYear = ca.get(Calendar.YEAR);
                mMonth = ca.get(Calendar.MONTH);
                mDay = ca.get(Calendar.DAY_OF_MONTH);

                showDialog(DATE_DIALOG);
                break;

            case R.id.back:
                finish();
                break;
            case R.id.enter:
                enter();
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
                    sList=data.getStringExtra("dateList");
                    int count=data.getIntExtra("count",0);
                    float totalAmount=data.getFloatExtra("totalAmount",0);
                    countText.setText(count+"件");
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
        String type="4";
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


    String data="";
    void enter()  {

        dialogShow();

    }


    private void dialogShow() {
        final Dialog dialog = new Dialog(this,R.style.ActionSheetDialogStyle);
        //填充对话框的布局
        LinearLayout inflate =(LinearLayout) LayoutInflater.from(this).inflate(R.layout.save_pandian_dialog, null);
        //初始化控件
        View btn_sure = inflate.findViewById(R.id.sure);
        View btn_cancel = inflate.findViewById(R.id.cancel);



        //将布局设置给Dialog
        dialog.setContentView(inflate);

        btn_sure.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
                try {
                    enterSave();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                dialog.dismiss();
            }
        });
        dialog.show();//显示对话框
    }

    void enterSave() throws JSONException {
             /*

        {
  "createBy": "1",
  "sList": [
    {
      "actualTotal": 2,
      "proId": "28c849ba0d2745bbb595def92df50bbf",
      "remarks": "说明"
    }
  ],
  "signa": "2ca4b1ce076005c305950f80327c7e5ab251e93a4ecc052fad0de3e88af22eb5",
  "stockCheckSaveAppVo": {
    "busTime": "2019-05-28 16:16:49",
    "depot": "2efa30c5b2464dee904f6720d54c2582",
    "leader": "第三",
    "remarks": "hello world",
    "stockCheckNo": "PD20190529269729001"
  }
}
         */
        JSONObject jsonObject1=new JSONObject();
        JSONArray jsonArray=new JSONArray(sList);
        jsonObject1.put("busTime",dateText.getText().toString());
        jsonObject1.put("depot",depot);
        jsonObject1.put("leader",leaderText.getText().toString());
        jsonObject1.put("remarks",remarksText.getText().toString());
        jsonObject1.put("stockCheckNo",danhaoText.getText().toString());

        JSONObject jsonObject=new JSONObject();
        jsonObject.put("createBy",UserBaseDatus.getInstance().userId);
        jsonObject.put("signa",UserBaseDatus.getInstance().getSign());
        jsonObject.put("sList",jsonArray);
        jsonObject.put("stockCheckSaveAppVo",jsonObject1);

        final String jsonString =jsonObject.toString();

        Log.d("jsonString", jsonString);

        final String contentTypeList = "application/json";
        final String url=UserBaseDatus.getInstance().url+"api/app/stockCheck/addAppStockCheckSave";

        new Thread(new Runnable() {
            @Override
            public void run() {
                Map map=UserBaseDatus.getInstance().isSuccessPost(url, jsonString, contentTypeList);
                if((boolean)(map.get("isSuccess"))){
                    Intent intent=new Intent(AddPandiancangkuAfterActivity.this,PandianlishiActivity.class);
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
                            Toast.makeText(AddPandiancangkuAfterActivity.this, data, Toast.LENGTH_SHORT).show();
                            data="";
                        }
                    });
                }
            }
        }).start();
    }

}
