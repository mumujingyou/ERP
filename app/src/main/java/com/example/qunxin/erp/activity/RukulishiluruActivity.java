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
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qunxin.erp.R;
import com.example.qunxin.erp.UserBaseDatus;
import com.example.qunxin.erp.modle.ShangpinBaseDatus;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import swipedelete.utils.CommonAdapter;
import swipedelete.utils.ViewHolder;

public class RukulishiluruActivity extends AppCompatActivity implements View.OnClickListener {

    View backBtn;

    TextView danhaoText;
    TextView dateText;

    TextView supplierNameText;


    ImageView imageView;
    TextView remarksText;
    TextView zhidanriqiText;
    TextView createByText;
    TextView totalAmountText;
    TextView jingshourenText;
    TextView depotNameText;


    ListView listView;
    View enterBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rukulishiluru);
        initView();

        initModel();
    }

    void initView(){
        danhaoText=findViewById(R.id.busNo);
        dateText=findViewById(R.id.dateText);
        supplierNameText=findViewById(R.id.supplierName);

        imageView=findViewById(R.id.addImage);
        remarksText=findViewById(R.id.remarksText);
        zhidanriqiText=findViewById(R.id.zhidanriqi);
        createByText=findViewById(R.id.createBy);

        jingshourenText=findViewById(R.id.jingshourenText);
        depotNameText=findViewById(R.id.depotName);
        totalAmountText=findViewById(R.id.totalAmount);

        listView=findViewById(R.id.listView);
        backBtn=findViewById(R.id.back);
        enterBtn=findViewById(R.id.enterBtn);


        backBtn.setOnClickListener(this);
        dateText.setOnClickListener(this);
        depotNameText.setOnClickListener(this);
        imageView.setOnClickListener(this);
        enterBtn.setOnClickListener(this);

    }

    String supplierName;
    String supplier;
    String busNo;
    void initModel(){
        Intent intent=getIntent();

        id=intent.getStringExtra("id");

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    loadModle();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();


        dateText.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));

    }


    List<ShangpinBaseDatus> lists=new ArrayList<>();
    void loadModle() throws JSONException {
        final String contentType = "application/x-www-form-urlencoded";
        final String url = UserBaseDatus.getInstance().url + "api/app/warehouse/selectWarehouseDetail";
        final String data = "signa=" + UserBaseDatus.getInstance().getSign() + "&&id=" + id;
        Log.d("purchaseId", id);
        Map map = UserBaseDatus.getInstance().isSuccessPost(url, data, contentType);
        if ((boolean) (map.get("isSuccess"))) {
            JSONObject jsonObject=(JSONObject) map.get("json");

            JSONObject json1=jsonObject.getJSONObject("data");


            JSONObject json=json1.getJSONObject("warehouse");
            supplier=json.getString("supplierId");
            busNo=json.getString("warehouseNo");
            supplierName=getSupplierName(supplier);
            JSONArray jsonArray= json1.getJSONArray("warehouseDetailList");
            for (int i=0;i<jsonArray.length();i++){
                JSONObject object=jsonArray.getJSONObject(i);
                String proName=object.getString("proName");

                String unit=object.getString("unit");
                String proId=object.getString("proId");
                String remarks=object.getString("remarks");
                String norms=object.getString("norms");
                String id=object.getString("id");
                int total=object.getInt("total");
                String proPrice=object.getString("proPrice");
                int realTotal=object.getInt("realTotal");
                String realPrice=object.getString("realPrice");



                ShangpinBaseDatus datus=new ShangpinBaseDatus();

                datus.setProName(proName);
                datus.setUnit(unit);
                datus.setProId(proId);
                datus.setRemarks(remarks);
                datus.setNorms(norms);
                datus.setId(id);
                datus.setRealPrice(Float.parseFloat(realPrice));
                datus.setPrice(Float.parseFloat(proPrice));
                datus.setTotal(total);
                datus.setRealTotal(realTotal);

                lists.add(datus);
            }


            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    danhaoText.setText(busNo);
                    supplierNameText.setText(supplierName);

                    loadView();
                }
            });


        }

    }


    void loadView(){





        listView.setAdapter(new CommonAdapter<ShangpinBaseDatus>(RukulishiluruActivity.this,lists, R.layout.item_ruku_shangpin) {
            @Override
            public void convert(final ViewHolder holder, ShangpinBaseDatus shangpinDatus, final int position, View convertView) {
                holder.setText(R.id.name,shangpinDatus.getProName());

                final TextView shijijiaText=holder.getView(R.id.shijijiaText);
                final TextView shijiCountText=holder.getView(R.id.shijiCountText);

                shijijiaText.setText(shangpinDatus.getRealPrice()+"");
                shijiCountText.setText(shangpinDatus.getRealTotal()+"");

                ShangpinBaseDatus datus=lists.get(position);
                shijijiaText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        String string=shijijiaText.getText().toString();
                        if("".equals(string)) return;
                        ShangpinBaseDatus datus=lists.get(position);
                        datus.setRealPrice(Float.parseFloat(string));
                    }
                });


                shijiCountText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        String string=shijiCountText.getText().toString();
                        if("".equals(string)) return;
                        ShangpinBaseDatus datus=lists.get(position);
                        datus.setRealTotal(Integer.parseInt(string));
                    }
                });
            }
        });

        setListViewHeightBasedOnChildren(listView);
    }


    void setListViewHeightBasedOnChildren(ListView listView) {



        ListAdapter listAdapter = listView.getAdapter();



        if (listAdapter == null) {

            return;

        }



        int totalHeight = 0;



        for (int i = 0; i < listAdapter.getCount(); i++) {

            View listItem = listAdapter.getView(i, null, listView);

            listItem.measure(0, 0);

            totalHeight += listItem.getMeasuredHeight();

        }



        ViewGroup.LayoutParams params = listView.getLayoutParams();



        params.height = totalHeight

                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));



        // ((ViewGroup.MarginLayoutParams) params).setMargins(10, 10, 10, 10); // 可删除

        listView.setLayoutParams(params);

    }


    String getSupplierName(String supplierId){
        final String contentType = "application/x-www-form-urlencoded";
        final String url = UserBaseDatus.getInstance().url + "api/suppliers/getSupplierById";
        final String data = "signa=" + UserBaseDatus.getInstance().getSign() + "&&id=" + supplierId;
        Map map = UserBaseDatus.getInstance().isSuccessPost(url, data, contentType);
        if ((boolean) (map.get("isSuccess"))) {
            JSONObject jsonObject=(JSONObject) map.get("json");

            JSONObject json= null;
            try {
                json = jsonObject.getJSONObject("data");
                String supName=json.getString("supName");
                return supName;

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return "";
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
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.enterBtn:
                enter();
                break;
            case R.id.dateText:
                final Calendar ca = Calendar.getInstance();
                mYear = ca.get(Calendar.YEAR);
                mMonth = ca.get(Calendar.MONTH);
                mDay = ca.get(Calendar.DAY_OF_MONTH);

                showDialog(DATE_DIALOG);
                break;
            case R.id.depotName:
                Intent intent1=new Intent(RukulishiluruActivity.this,XuanzeCangkuActivity.class);
                startActivityForResult(intent1,1);
                break;
            case R.id.addImage:
                Intent intent = new Intent();

                intent.setType("image/*");

                intent.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(intent, 1);
                break;
        }

    }


    String picPath;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case 1:
                if(resultCode==1){
                    depotId=data.getStringExtra("id");
                    depotNameText.setText(data.getStringExtra("name"));
                }

                if(resultCode==3){
                   wdList=data.getStringExtra("wdList");
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

                            if (path.endsWith("jpg") || path.endsWith("png")) {

                                picPath = path;

                                Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));

                                imageView.setImageBitmap(bitmap);

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
        String type="14";
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
    String depotId;
    String id;
    String wdList="";
    void enter(){
        String busTime=dateText.getText().toString();
        String createBy= UserBaseDatus.getInstance().userId;
        String depotId=this.depotId;
        String id=this.id;
        String remarks=remarksText.getText().toString();
        String signa=UserBaseDatus.getInstance().getSign();
        String wdList=jsonToString();
        JSONObject jsonObject=new JSONObject();

        try {
            JSONArray jsonArray=new JSONArray(wdList);

            jsonObject.put("busTime",busTime);
            jsonObject.put("createBy",createBy);
            jsonObject.put("id",id);
            jsonObject.put("depotId",depotId);
            jsonObject.put("remarks",remarks);
            jsonObject.put("signa",signa);
            jsonObject.put("wdList",jsonArray);
            jsonObject.put("leader",jingshourenText.getText().toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }


        final String jsonString = jsonObject.toString();

        Log.d("jsonString", jsonString);

        final String contentTypeList = "application/json";
        final String url=UserBaseDatus.getInstance().url+"api/app/warehouse/confirmWarehouse";

        Log.d("url", url);
        new Thread(new Runnable() {
            @Override
            public void run() {
                Map map=UserBaseDatus.getInstance().isSuccessPost(url, jsonString, contentTypeList);
                if((boolean)(map.get("isSuccess"))){
                    Intent intent=new Intent(RukulishiluruActivity.this,RukulishiluruSuccessActivity.class);
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
                            Toast.makeText(RukulishiluruActivity.this, data, Toast.LENGTH_SHORT).show();
                            data="";
                        }
                    });
                }
            }
        }).start();
    }



    String jsonToString()  {
        StringBuffer buffer=new StringBuffer();

        for (int i=0;i<lists.size();i++){
            JSONObject jsonObject=new JSONObject();
            try {
                if(lists.get(i).getTotal()==0) break;

                jsonObject.put("id",lists.get(i).getId());
                jsonObject.put("realPrice",lists.get(i).getRealPrice());
                jsonObject.put("realTotal",lists.get(i).getRealTotal());
                jsonObject.put("remarks",lists.get(i).getRemarks());

                String content=String.valueOf(jsonObject);
                buffer.append(content).append(",");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        String s= buffer.toString();
        if("".equals(s)){
            return "";
        }
        return "["+s.substring(0,s.length()-1)+"]";
    }




}
