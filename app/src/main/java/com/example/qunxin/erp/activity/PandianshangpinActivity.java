package com.example.qunxin.erp.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.qunxin.erp.R;

public class PandianshangpinActivity extends AppCompatActivity {

    TextView proNameText;
    TextView proNoText;
    TextView normsText;
    TextView propertyText;
    TextView bookTotalText;
    TextView actualTotalText;
    TextView lessTotalText;
    TextView moreTotalText;
    TextView remarksText;


    View bakcBtn;







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pandianshangpin);

        initView();
    }


    void initView(){
        proNameText=findViewById(R.id.name);
        proNoText=findViewById(R.id.proNo);
        normsText=findViewById(R.id.norms);
        propertyText=findViewById(R.id.property);
        bookTotalText=findViewById(R.id.zhangmianCount);
        actualTotalText=findViewById(R.id.shijiCount);
        lessTotalText=findViewById(R.id.kuishunCount);
        moreTotalText=findViewById(R.id.panyingCount);
        remarksText=findViewById(R.id.remarksText);
        bakcBtn=findViewById(R.id.back);

        Intent intent=getIntent();
        proNameText.setText(intent.getStringExtra("proName"));
        proNoText.setText(intent.getStringExtra("proNo"));
        normsText.setText(intent.getStringExtra("norms"));
        propertyText.setText(intent.getStringExtra("property"));
        bookTotalText.setText(intent.getIntExtra("bookTotal",0)+"");
        actualTotalText.setText(intent.getIntExtra("actualTotal",0)+"");
        lessTotalText.setText(intent.getIntExtra("lessTotal",0)+"");
        moreTotalText.setText(intent.getIntExtra("moreTotal",0)+"");
        remarksText.setText(intent.getStringExtra("remarks"));


        bakcBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}
