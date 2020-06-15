package com.example.qunxin.erp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by qunxin on 2019/8/7.
 */

public class ImageUtils extends AppCompatActivity {

    public static ImageUtils instance=new ImageUtils();

   private ImageUtils(){}

   public static ImageUtils getInstance(){
       if(instance==null){
           instance=new ImageUtils();
       }
       return instance;
   }

    public Bitmap readImage(){
        try {
            FileInputStream fis = openFileInput("head.jpg");
            byte[] bytes=new byte[fis.available()];
            fis.read(bytes);
            fis.close();
            return BitmapFactory.decodeByteArray(bytes,0,bytes.length);
        } catch (FileNotFoundException e) {

            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
