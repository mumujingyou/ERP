package com.example.qunxin.erp.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

import com.example.qunxin.erp.R;

/**
 * Created by qunxin on 2019/8/30.
 */

public class AddView extends View {
    public AddView(Context context) {
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Paint paint=new Paint();
        Bitmap bitmap= BitmapFactory.decodeResource(getResources(), R.mipmap.add_btn);
        canvas.drawBitmap(bitmap,0,0,paint);



    }
}
