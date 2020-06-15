package com.example.qunxin.erp.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qunxin.erp.R;
import com.example.qunxin.erp.activity.AddShangpinActivity;
import com.example.qunxin.erp.activity.ShangpinliebiaoActivity;
import com.example.qunxin.erp.util.Constant;
import com.google.zxing.activity.CaptureActivity;

import static android.app.Activity.RESULT_OK;

/**
 * Created by qunxin on 2019/8/15.
 */

public class ShangpinliebiaoSearchFragment extends Fragment {


    EditText editText;
    View view;
    View shangpinliebiao_search_back;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         view=inflater.inflate(R.layout.fragment_shangpinliebiao_search,container,false);
        initView();
        return view;
    }

    void initView(){
        shangpinliebiao_search_back=view.findViewById(R.id.shangpinliebiao_search_back);
        shangpinliebiao_search_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ((ShangpinliebiaoActivity)(getActivity())).addShangpinliebiaoPaixuFragmentt();
//                ((ShangpinliebiaoActivity)(getActivity())).search("");
            }
        });

        editText=view.findViewById(R.id.shangpinliebiao_search_serch);

        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override

            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_SEARCH){

                    Log.d("search", "onEditorAction: search");
                    ((ShangpinliebiaoActivity)(getActivity())).search(editText.getText().toString());
                    return true;
                }
                return false;

            }
        });

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                ((ShangpinliebiaoActivity)(getActivity())).search(editText.getText().toString());
            }
        });

        //点击editText右边图标
        editText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // getCompoundDrawables获取是一个数组，数组0,1,2,3,对应着左，上，右，下 这4个位置的图片，如果没有就为null
                Drawable drawable = editText.getCompoundDrawables()[2];
                //如果右边没有图片，不再处理
                if (drawable == null){
                    return false;
                }
                //如果不是按下事件，不再处理
                if (event.getAction() != MotionEvent.ACTION_DOWN) {
                    return false;
                }
                if (event.getX() > editText.getWidth()
                        - editText.getPaddingRight()
                        - drawable.getIntrinsicWidth()){
                    //具体操作
                    startQrCode();
                    Log.d("touch", "onTouch: touch");
                }
                return false;
            }
        });
    }

    // 开始扫码
    private void startQrCode() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            // 申请权限
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, Constant.REQ_PERM_CAMERA);
            return;
        }
        // 二维码扫码
        Intent intent = new Intent(((ShangpinliebiaoActivity)(getActivity())), CaptureActivity.class);
        startActivityForResult(intent, Constant.REQ_QR_CODE);
    }



    //申请访问权限回调结果
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case Constant.REQ_PERM_CAMERA:
                // 摄像头权限申请
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 获得授权
                    startQrCode();
                } else {
                    // 被禁止授权
                    Toast.makeText(getActivity(), "请至权限中心打开本应用的相机访问权限", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constant.REQ_QR_CODE && resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            String scanResult = bundle.getString(Constant.INTENT_EXTRA_KEY_QR_SCAN);
            //将扫描出的信息显示出来
            editText.setText(scanResult);

        }
    }
}
