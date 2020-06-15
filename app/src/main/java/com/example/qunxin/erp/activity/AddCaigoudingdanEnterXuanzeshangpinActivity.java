package com.example.qunxin.erp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.example.qunxin.erp.R;
import com.example.qunxin.erp.UserBaseDatus;
import com.example.qunxin.erp.modle.ShangpinBaseDatus;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import swipedelete.utils.CommonAdapter;
import swipedelete.utils.ViewHolder;

public class AddCaigoudingdanEnterXuanzeshangpinActivity extends AppCompatActivity {


    View backBtn;
    View finishBtn;
    ListView listView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_caigoudingdan_enter_xuanzeshangpin);

        initView();
    }

    void initView() {
        backBtn = findViewById(R.id.back);
        finishBtn = findViewById(R.id.finish);
        listView = findViewById(R.id.listView);


        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        finishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishBtn();
                finish();
            }
        });


        Intent intent = getIntent();
        purchaseDetailVoList = intent.getStringExtra("purchaseDetailVoList");
        Log.d("dateList", purchaseDetailVoList);
        jsonParse();
        loadView();
    }

    String purchaseDetailVoList = "";

    List<ShangpinBaseDatus> lists = new ArrayList<>();

    void jsonParse() {

        if ("".equals(purchaseDetailVoList)) {
            lists.clear();
            return;
        }

        try {
            JSONArray jsonArray = new JSONArray(purchaseDetailVoList);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String proName = jsonObject.getString("proName");
                String proId = jsonObject.getString("proId");
                String norms = jsonObject.getString("norms");
                String id = jsonObject.getString("id");
                String purchaseId = jsonObject.getString("purchaseId");
                String remarks = jsonObject.getString("remarks");
                String unit = jsonObject.getString("unit");
                int total = jsonObject.getInt("total");

                ShangpinBaseDatus datus = new ShangpinBaseDatus();
                datus.setProName(proName);
                datus.setProId(proId);
                datus.setNorms(norms);
                datus.setId(id);
                datus.setPurchaseId(purchaseId);
                datus.setRemarks(remarks);
                datus.setTotal(total);
                datus.setUnit(unit);
                datus.setImgRes(R.mipmap.shop_icon);
                lists.add(datus);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    String supplier;

    String jsonToString() {
        StringBuffer buffer = new StringBuffer();

        for (int i = 0; i < lists.size(); i++) {
            JSONObject jsonObject = new JSONObject();
            try {
                if (lists.get(i).getTotal() == 0) continue;

                jsonObject.put("actualPurchaseQuantity", lists.get(i).getActualPurchaseQuantity());
                jsonObject.put("applicationAllowance", 0);
                jsonObject.put("createBy", UserBaseDatus.getInstance().userId);
                jsonObject.put("createTime", "");
                jsonObject.put("delFlag", 0);
                jsonObject.put("deliveryDate", "");
                jsonObject.put("id", lists.get(i).getId());
                jsonObject.put("marketAmount", lists.get(i).getMarketAmount());
                jsonObject.put("orderQuantity", lists.get(i).getTotal());
                jsonObject.put("proId", lists.get(i).getProId());
                jsonObject.put("purchaseId", lists.get(i).getPurchaseId());
                jsonObject.put("purchaseAmount", lists.get(i).getPurchaseAmount());

                jsonObject.put("remark", "");
                jsonObject.put("searchValue", "");
                jsonObject.put("supplier", supplier);

                jsonObject.put("updateBy", UserBaseDatus.getInstance().userId);
                jsonObject.put("updateTime", "");

                jsonObject.put("norms", lists.get(i).getNorms());
                jsonObject.put("proName", lists.get(i).getProName());
                jsonObject.put("remarks", lists.get(i).getRemarks());
                jsonObject.put("unit", lists.get(i).getUnit());

                jsonObject.put("price", lists.get(i).getPrice());

                jsonObject.put("totalAmount", getTotalAmount());
                jsonObject.put("property", lists.get(i).getProperty());

                String content = String.valueOf(jsonObject);
                buffer.append(content).append(",");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        String s = buffer.toString();
        if ("".equals(s)) {
            return "";
        }
        return "[" + s.substring(0, s.length() - 1) + "]";
    }


    void loadView() {
        listView.setAdapter(new CommonAdapter<ShangpinBaseDatus>(AddCaigoudingdanEnterXuanzeshangpinActivity.this, lists, R.layout.item_caigoudingdan_yixuanshangpin) {
            @Override
            public void convert(final ViewHolder holder, ShangpinBaseDatus shangpinDatus, final int position, View convertView) {
                holder.setText(R.id.name, shangpinDatus.getProName());
                holder.setText(R.id.caigouCount, shangpinDatus.getTotal() + "");

                final TextView shichangjiaText = holder.getView(R.id.shichangjiaText);
                final TextView caigoujiaText = holder.getView(R.id.caigoujiaText);
                final TextView shijicaigouCountText = holder.getView(R.id.shijicaigouCount);
                ShangpinBaseDatus datus = lists.get(position);
                shichangjiaText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        String string = shichangjiaText.getText().toString();
                        if ("".equals(string)) return;
                        ShangpinBaseDatus datus = lists.get(position);
                        datus.setMarketAmount(Float.parseFloat(string));
                    }
                });


                caigoujiaText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        String string = caigoujiaText.getText().toString();
                        if ("".equals(string)) return;
                        ShangpinBaseDatus datus = lists.get(position);
                        datus.setPurchaseAmount(Float.parseFloat(string));
                    }
                });

                shijicaigouCountText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        String string = shijicaigouCountText.getText().toString();
                        if ("".equals(string)) return;
                        ShangpinBaseDatus datus = lists.get(position);
                        datus.setActualPurchaseQuantity(Integer.parseInt(string));
                    }
                });

                datus.setActualPurchaseQuantity(Integer.parseInt(shijicaigouCountText.getText().toString()));
            }
        });
    }


    int getTotal() {
        int total = 0;
        for (int i = 0; i < lists.size(); i++) {
            total += lists.get(i).getTotal();
        }
        return total;
    }

    int getActualTotal() {
        int total = 0;
        for (int i = 0; i < lists.size(); i++) {
            total += lists.get(i).getActualPurchaseQuantity();
        }
        return total;
    }


    float getTotalAmount() {
        int totalAmount = 0;
        for (int i = 0; i < lists.size(); i++) {
            totalAmount += lists.get(i).getActualPurchaseQuantity() * lists.get(i).getPurchaseAmount();
        }
        return totalAmount;
    }


    void finishBtn() {
        Intent intent = new Intent();
        purchaseDetailVoList = jsonToString();
        intent.putExtra("purchaseDetailVoList", purchaseDetailVoList);
        intent.putExtra("realTotal", getActualTotal() + "");
        intent.putExtra("totalAmount", getTotalAmount() + "");
        Log.d("purchaseDetailVoList", purchaseDetailVoList);

        setResult(1, intent);
        finish();
    }


}
