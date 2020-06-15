package com.example.qunxin.erp.activity;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.qunxin.erp.R;
import com.example.qunxin.erp.UserBaseDatus;
import com.example.qunxin.erp.modle.KehuDatus;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import swipedelete.utils.CommonAdapter;
import swipedelete.utils.ViewHolder;
import swipedelete.view.SwipeMenuLayout;

public class XuanzeKehuActivity extends AppCompatActivity {

    ListView listView;

    TextView searchText;
    View backBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xuanze_kehu);


        listView=findViewById(R.id.listView);

        new Thread(new Runnable() {
            @Override
            public void run() {
                loadModel();
            }
        }).start();


        searchText=findViewById(R.id.search);
        backBtn=findViewById(R.id.back);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        searchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override

            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_SEARCH){

                    search(searchText.getText().toString());
                    return true;
                }
                return false;

            }
        });

        searchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                search(searchText.getText().toString());
            }
        });

    }

    List<KehuDatus> lists=new ArrayList<>();

    String searchValue="";

    void loadModel(){

        final String pageNum="1";
        final String pageSize="200";
        final String contentType = "application/x-www-form-urlencoded";
        final String url= UserBaseDatus.getInstance().url+"api/custs/pageList";

        final String jsonString="signa="+UserBaseDatus.getInstance().getSign()+"&&pageSize="+pageSize+"&&pageNum="+pageNum+
                "&&searchValue="+searchValue;

        Map map=UserBaseDatus.getInstance().isSuccessPost(url, jsonString, contentType);

        if((boolean)(map.get("isSuccess"))){

            JSONObject json=(JSONObject) map.get("json");
            try {
                JSONObject jsonData=json.getJSONObject("data");
                JSONArray jsonArray=jsonData.getJSONArray("rows");
                for (int i=0;i<jsonArray.length();i++){


                    JSONObject jsonObject=jsonArray.getJSONObject(i);
                    String custName=jsonObject.getString("custName");

                    String contactPeople=jsonObject.getString("contactPeople");

                    String id=jsonObject.getString("id");

                    String phone=jsonObject.getString("phone");

                    String dept=getYingshouqiankuna(id);
                    KehuDatus datus = new KehuDatus();
                    datus.setKehu(custName);
                    datus.setLianxiren(contactPeople);
                    datus.setId(id);
                    datus.setPhone(phone);
                    datus.setCount(dept);
                    lists.add(datus);


                }


               runOnUiThread(new Runnable() {
                   @Override
                   public void run() {
                       loadView();
                   }
               });


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }






    void loadView(){


        listView.setAdapter(new CommonAdapter<KehuDatus>(XuanzeKehuActivity.this,lists, R.layout.item_xuanzekehu) {

            @Override
            public void convert(final ViewHolder holder, KehuDatus kehuModel, final int position, View convertView) {
                if(kehuModel==null) return;

                holder.setText(R.id.name,kehuModel.getKehu());
                holder.setText(R.id.phone,kehuModel.getPhone());
                holder.setText(R.id.count,kehuModel.getCount());



                //监听事件
                holder.setOnClickListener(R.id.ll_content, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        isLoad=true;
                        Intent intent=new Intent();
                        KehuDatus datus=lists.get(position);
                        String id=datus.getId();
                        intent.putExtra("id",id);
                        String kehu=datus.getKehu();
                        intent.putExtra("kehu",kehu);


                        setResult(2,intent);
                        finish();

                    }
                });
            }
        });
    }


    String getYingshouqiankuna(String id){
        String result="";
        final String contentType = "application/x-www-form-urlencoded";
        final String url= UserBaseDatus.getInstance().url+"api/custs/selectCustDeptById";
        final String jsonString="sign="+UserBaseDatus.getInstance().getSign()+"&&id="+id;
        System.out.println(jsonString);
        Map map=UserBaseDatus.getInstance().isSuccessPost(url, jsonString, contentType);
        if((boolean)(map.get("isSuccess"))){
            JSONObject json=(JSONObject) map.get("json");
            try {
                JSONObject jsonObject=json.getJSONObject("data");
                result=jsonObject.getString("dept");
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return result;
    }


    void search(String searchValue){
        this.searchValue=searchValue;
        searchLoad();
    }

    boolean isLoad=false;

    void searchLoad(){
        lists.clear();
        new Thread(new Runnable() {
            @Override
            public void run() {
                loadModel();
            }
        }).start();

    }
}
