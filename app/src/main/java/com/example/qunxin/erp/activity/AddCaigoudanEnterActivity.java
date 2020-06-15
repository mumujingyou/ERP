package com.example.qunxin.erp.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qunxin.erp.R;
import com.example.qunxin.erp.UserBaseDatus;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AddCaigoudanEnterActivity extends AppCompatActivity {

    View backBtn;

    TextView danhaoText;
    TextView dateText;
    TextView supplierNameText;


    ImageView imageView;
    TextView remarksText;
    TextView zhidanriqiText;
    TextView createByText;
    TextView totalText;
    TextView buyerText;
    TextView priceText;
    TextView countText;


    ListView listView;
    View saveBtn;
    View shangpinView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_caougoudan_enter);
        initView();
    }


    void initView() {
        danhaoText = findViewById(R.id.busNo);
        dateText = findViewById(R.id.dateText);
        supplierNameText = findViewById(R.id.supplierName);

        imageView = findViewById(R.id.image);
        remarksText = findViewById(R.id.remarksText);
        zhidanriqiText = findViewById(R.id.zhidanriqi);
        createByText = findViewById(R.id.createBy);
        saveBtn = findViewById(R.id.save);
        totalText = findViewById(R.id.count);
        shangpinView = findViewById(R.id.shangpin);

        listView = findViewById(R.id.listView);
        backBtn = findViewById(R.id.back);
        buyerText = findViewById(R.id.buyer);
        priceText = findViewById(R.id.price);
        countText = findViewById(R.id.count);


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

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    save();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });


        dateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar ca = Calendar.getInstance();
                mYear = ca.get(Calendar.YEAR);
                mMonth = ca.get(Calendar.MONTH);
                mDay = ca.get(Calendar.DAY_OF_MONTH);

                showDialog(DATE_DIALOG);
            }
        });

        shangpinView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddCaigoudanEnterActivity.this, AddCaigoudingdanEnterXuanzeshangpinActivity.class);
                intent.putExtra("purchaseDetailVoList", purchaseDetailVoList1);
                Log.d("purchaseDetailVoList1", purchaseDetailVoList1);
                startActivityForResult(intent, 1);
            }
        });


        initModel();

        new Thread(new Runnable() {
            @Override
            public void run() {
                getImage();

            }
        }).start();
    }

    String supplier = "";
    String jsonId = "";
    String id = "";

    void initModel() {
        Intent intent = getIntent();
        String supplierName = intent.getStringExtra("supplierName");
        supplier = intent.getStringExtra("supplier");
        String busNo = intent.getStringExtra("busNo");
        int count = intent.getIntExtra("count", 0);
        String zhidanriqi = intent.getStringExtra("zhidanriqi");
        String createBy = intent.getStringExtra("createBy");
        jsonId = intent.getStringExtra("jsonId");
        purchaseDetailVoList1 = intent.getStringExtra("purchaseDetailVoList");
        id = intent.getStringExtra("id");

        supplierNameText.setText(supplierName);
        danhaoText.setText(busNo);
        zhidanriqiText.setText(zhidanriqi);
        createByText.setText(createBy);
        totalText.setText(count + "件");
        dateText.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));

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
        String string = sdf.format(d);

        dateText.setText(new StringBuffer().append(mYear).append("-").append(mMonth + 1).append("-").append(mDay).append(" ").append(string.split(" ")[1]));
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


    Bitmap mBitmap;

    void getImage() {
        if ("".equals(jsonId)) return;
        final String contentType = "application/octet-stream";
        final String data = "?signa=" + UserBaseDatus.getInstance().getSign() + "&id=" + jsonId;
        final String url = UserBaseDatus.getInstance().url + "api/app/upload/download" + data;
        Log.d("url", url);
        final Bitmap bitmap = UserBaseDatus.getInstance().isSuccessGet(url, contentType);
        mBitmap = bitmap;
        System.out.println(bitmap);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                imageView.setImageBitmap(mBitmap);

            }
        });
    }


    Dialog dialog;

    void showImage() {
        dialog = new Dialog(AddCaigoudanEnterActivity.this, R.style.AlertDialog_AppCompat_Light);
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
    private ImageView getImageView() {
        ImageView iv = new ImageView(this);
        //宽高
        iv.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        //设置Padding
        iv.setPadding(20, 20, 20, 20);

        if (mBitmap != null) {
            iv.setImageBitmap(mBitmap);
        } else {
            iv.setImageResource(R.mipmap.shop_icon);
        }
        return iv;
    }


    String purchaseDetailVoList = "";
    String purchaseDetailVoList1 = "";

    void save() throws JSONException {
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


        Map<String, String> map = new HashMap<>();
        map.put("buyer", buyerText.getText().toString());
        map.put("createBy", UserBaseDatus.getInstance().userId);
        map.put("createTime", dateText.getText().toString());
        map.put("delFlag", "0");
        map.put("deliveryDate", dateText.getText().toString());
        map.put("id", id);
        map.put("marketAmount", "0");
        map.put("nowTime", dateText.getText().toString());
        map.put("pageNum", "1");
        map.put("pageSize", "10");
        map.put("proName", "PP");
        String price = priceText.getText().toString();
        map.put("purchaseAmount", price.substring(1));
        map.put("purchaseNo", danhaoText.getText().toString());
        map.put("remark", remarksText.getText().toString());
        map.put("searchValue", "");
        map.put("signa", UserBaseDatus.getInstance().getSign());
        map.put("supplier", supplier);
        String total = countText.getText().toString();
        map.put("total", total.substring(0, total.length() - 1));

        map.put("unit", "");
        map.put("status", "0");
        map.put("updateBy", UserBaseDatus.getInstance().userId);
        map.put("updateTime", dateText.getText().toString());
        JSONObject jsonObject1 = new JSONObject(map);
        JSONArray jsonArray;
        if ("".equals(purchaseDetailVoList) == false) {
            jsonArray = new JSONArray(purchaseDetailVoList);
        } else {
            Toast.makeText(this, "请选择商品", Toast.LENGTH_SHORT).show();
            jsonArray = new JSONArray();
            return;
        }


        JSONObject jsonObject = new JSONObject();
        jsonObject.put("signa", UserBaseDatus.getInstance().getSign());
        jsonObject.put("userId", UserBaseDatus.getInstance().userId);
        jsonObject.put("purchaseVo", jsonObject1);
        jsonObject.put("purchaseDetailVoList", jsonArray);

        final String jsonString = jsonObject.toString();

        Log.d("jsonString", jsonString);

        final String contentTypeList = "application/json";
        final String url = UserBaseDatus.getInstance().url + "api/app/purchases/affirmPurchase";

        Log.d("url", url);
        new Thread(new Runnable() {
            @Override
            public void run() {
                Map map = UserBaseDatus.getInstance().isSuccessPost(url, jsonString, contentTypeList);
                if ((boolean) (map.get("isSuccess"))) {
                    Intent intent = new Intent(AddCaigoudanEnterActivity.this, AddCaigoudingdanEnterSuccessActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    JSONObject jsonObject = (JSONObject) map.get("json");
                    try {
                        data = jsonObject.getString("data");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(AddCaigoudanEnterActivity.this, data, Toast.LENGTH_SHORT).show();
                            data = "";
                        }
                    });
                }
            }
        }).start();
    }


    String data = "";

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                if (resultCode == 1) {
                    purchaseDetailVoList = data.getStringExtra("purchaseDetailVoList");
                    String realTotal = data.getStringExtra("realTotal");
                    String totalAmount = data.getStringExtra("totalAmount");
                    priceText.setText("￥" + totalAmount);
                    countText.setText(realTotal + "件");
                }
                break;
        }
    }
}
