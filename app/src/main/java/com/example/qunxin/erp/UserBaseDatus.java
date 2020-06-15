package com.example.qunxin.erp;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qunxin.erp.activity.PersonalInfoActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by qunxin on 2019/8/9.
 */

public class UserBaseDatus {

    //辨别点击+ 按钮时是客户还是供应商
    public boolean isKehu=true;


    public static UserBaseDatus instance=new UserBaseDatus();

    public final String url="http://119.23.219.127:8094/";

    public String avatar="";

    public String userId="1";

    public String cretorName="管理员";


    private UserBaseDatus(){}

    public static UserBaseDatus getInstance(){
        if(instance==null){
            instance=new UserBaseDatus();
        }
        return instance;
    }
//5f179ee93114e6575cffcbddc9193423a1a767e5f0e469fa50e195e05746712c
    public String getSign(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        String date = dateFormat.format(new Date());
        final String  syspwd = Sha.bytes2Hex(Sha.sha256("ppaQunXin" + date));
        Log.d("signa", syspwd);
        return syspwd;
    }

    String dealResponseResult(InputStream inputStream) {
        String resultData = null;      //存储处理结果
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] data = new byte[1024];
        int len = 0;
        try {
            while ((len = inputStream.read(data)) != -1) {
                byteArrayOutputStream.write(data, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        resultData = new String(byteArrayOutputStream.toByteArray());
        return resultData;
    }


    //从网络上下载图片
    Bitmap mBitmap;
    public Bitmap isSuccessGet(String strUrl, String contentType) {
        //通过Get方法请求获取网络图片

        try {
            URL url=new URL(strUrl);

            HttpURLConnection connection=(HttpURLConnection) url.openConnection();


            //设置请求方式

            connection.setRequestMethod("GET");

            //设置超时时间

            connection.setConnectTimeout(30*1000);

            connection.setRequestProperty("Content-Type",contentType);//设置请求体的类型是文本类型


            //发起连接

            connection.connect();


            //获取状态码

            int requestCode=connection.getResponseCode();



            if (requestCode==HttpURLConnection.HTTP_OK){

                    /*

                    * 1.获得文件长度

                    * 2.通过缓冲输入流

                    * 3.将输入流转换成字节数组

                    * 4.将字节数组转换成位图

                    * */

                int fileLength=connection.getContentLength();

                InputStream is=new BufferedInputStream(connection.getInputStream());


                //获取到字节数组

                byte[] arr=streamToArr(is);


                //将字节数组转换成位图

                mBitmap= BitmapFactory.decodeByteArray(arr,0,arr.length);

                is.close();
                    /*

                    * 下载完成后将消息发送出去

                    * 通知主线程，更新UI

                    * */

            }else {

                Log.e("TAG", "run:error "+requestCode);
            }

        }catch (MalformedURLException e){

            e.printStackTrace();



        }catch (IOException e){

            e.printStackTrace();

        }

        return mBitmap;

    }

    //将输入流转换成字节数组

    public byte[] streamToArr(InputStream inputStream){



        try {

            ByteArrayOutputStream baos=new ByteArrayOutputStream();

            byte[] buffer=new byte[1024];

            int len;



            while ((len=inputStream.read(buffer))!=-1){

                baos.write(buffer,0,len);

            }



            //关闭输出流

            baos.close();

            //关闭输入流

            inputStream.close();

            //返回字节数组

            return baos.toByteArray();

        }catch (IOException e){

            e.printStackTrace();

            //若失败，则返回空

            return null;

        }

    }

    //发送post请求
    public Map isSuccessPost (String strUrl, String strData, String contentType) {

        boolean isSuccess = false;

        String StrResponseBody = null;
        JSONObject json=new JSONObject();
        Map map=new HashMap();
        try {
            byte[] requestBody = strData.getBytes("UTF-8");

            URL url = new URL(strUrl);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setConnectTimeout(3000); //设置连接超时时间
            httpURLConnection.setDoInput(true); //打开输入流，以便从服务器获取数据
            httpURLConnection.setDoOutput(true); //打开输出流，以便向服务器提交数据
            httpURLConnection.setRequestMethod("POST"); //设置以Post方式提交数据
            httpURLConnection.setUseCaches(false);  //使用Post方式不能使用缓存

            httpURLConnection.setRequestProperty("Content-Type",contentType);//设置请求体的类型是文本类型

            httpURLConnection.setRequestProperty("Content-Length", String.valueOf(requestBody.length));//设置请求体的长度
            OutputStream outputStream = httpURLConnection.getOutputStream();
            outputStream.write(requestBody);

            int response = httpURLConnection.getResponseCode();//获得服务器的响应码
            if (response == HttpURLConnection.HTTP_OK) {
                InputStream inptStream = httpURLConnection.getInputStream();
                //获得响应体的字节数组
                StrResponseBody = dealResponseResult(inptStream);
                json=new JSONObject(StrResponseBody);
                Log.d("StrResponseBody",StrResponseBody );
                if(json.getInt("code")==0){
                    isSuccess = true;
                    map.put("isSuccess",isSuccess);
                    map.put("json",json);
                }else {
                    map.put("isSuccess",isSuccess);
                    map.put("json",json);
                }
            }
        } catch (IOException e) {
            map.put("isSuccess",isSuccess);
            map.put("json",json);
        } catch (JSONException e) {
            map.put("isSuccess",isSuccess);
            map.put("json",json);
            e.printStackTrace();
        }
        return map;
    }

    //验证手机号
    public boolean judPhone(Context context, TextView phoneNumberText) {

        if (TextUtils.isEmpty(phoneNumberText.getText().toString().trim())) {
            Toast.makeText(context, "请输入您的电话号码", Toast.LENGTH_SHORT).show();
            phoneNumberText.requestFocus();
            return false;
        } else if (phoneNumberText.getText().toString().trim().length() != 11) {
            Toast.makeText(context, "您的电话号码位数不正确", Toast.LENGTH_SHORT).show();
            phoneNumberText.requestFocus();
            return false;
        } else {
            String phone_number = phoneNumberText.getText().toString().trim();
            String num = "[1][358]\\d{9}";
            if (phone_number.matches(num))
                return true;
            else {
                Toast.makeText(context, "请输入正确的手机号码", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
    }


    /**

     * 判断邮箱是否合法

     * @param email

     * @return

     */

    public  boolean isEmail(String email,Context context){

        if (null==email || "".equals(email)) return false;

        //Pattern p = Pattern.compile("\\w+@(\\w+.)+[a-z]{2,3}"); //简单匹配

        Pattern p =  Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");//复杂匹配

        Matcher m = p.matcher(email);

         if(m.matches()){

             return true;
         }else {
             Toast.makeText(context, "请输入正确的邮箱", Toast.LENGTH_SHORT).show();
             return false;
         }

    }


    public void getShangpinbianhao(final String type, final Activity activity, final TextView textView){

        //http://119.23.219.127:8094/api/numberApp/getNumber?type=5
        new Thread(new Runnable() {
            final String url="http://119.23.219.127:8094/api/numberApp/getNumber";
            final String data="type="+type;
            final String contentType = "application/x-www-form-urlencoded";
            @Override
            public void run() {
                Map map=UserBaseDatus.getInstance().isSuccessPost(url, data, contentType);
                if((boolean)(map.get("isSuccess"))){
                    JSONObject json=(JSONObject) map.get("json");
                    try {
                        final String string=json.getString("data");
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                textView.setText(string);
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    String dataStr="";
    public String getShangpinbianhao(final String type){


        //http://119.23.219.127:8094/api/numberApp/getNumber?type=5
        new Thread(new Runnable() {
            final String url="http://119.23.219.127:8094/api/numberApp/getNumber";
            final String data="type="+type;
            final String contentType = "application/x-www-form-urlencoded";
            @Override
            public void run() {
                Map map=UserBaseDatus.getInstance().isSuccessPost(url, data, contentType);
                if((boolean)(map.get("isSuccess"))){
                    JSONObject json=(JSONObject) map.get("json");
                    try {
                        dataStr=json.getString("data");

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
        return dataStr;
    }



    /**

     * 通过拼接的方式构造请求内容，实现参数传输以及文件传输

     *

     * @param url Service net address

     * @param params text content

     * @param files pictures

     * @return String result of Service response

     * @throws IOException

     */

    public  String post(String url, Map<String, String> params, Map<String, File> files)

    throws IOException {

        String BOUNDARY = java.util.UUID.randomUUID().toString();

        String PREFIX ="--", LINEND = "\r\n";

        String MULTIPART_FROM_DATA ="multipart/form-data";

        String CHARSET ="UTF-8";

        URL uri =new URL(url);

        HttpURLConnection conn = (HttpURLConnection) uri.openConnection();

        conn.setReadTimeout(10* 1000);// 缓存的最长时间

        conn.setDoInput(true);// 允许输入

        conn.setDoOutput(true);// 允许输出

        conn.setUseCaches(false);// 不允许使用缓存

        conn.setRequestMethod("POST");

        conn.setRequestProperty("connection","keep-alive");

        conn.setRequestProperty("Charsert","UTF-8");

        conn.setRequestProperty("Content-Type", MULTIPART_FROM_DATA +";boundary=" + BOUNDARY);

        // 首先组拼文本类型的参数

        StringBuilder sb =new StringBuilder();

        for(Map.Entry<String, String> entry : params.entrySet()) {

            sb.append(PREFIX);

            sb.append(BOUNDARY);

            sb.append(LINEND);

            sb.append("Content-Disposition: form-data; name=\""+ entry.getKey() + "\""+ LINEND);

            sb.append("Content-Type: text/plain; charset="+ CHARSET + LINEND);

            sb.append("Content-Transfer-Encoding: 8bit"+ LINEND);

            sb.append(LINEND);

            sb.append(entry.getValue());

            sb.append(LINEND);

        }

        DataOutputStream outStream =new DataOutputStream(conn.getOutputStream());

        outStream.write(sb.toString().getBytes());

        // 发送文件数据

        if(files != null)

            for(Map.Entry<String, File> file : files.entrySet()) {

                StringBuilder sb1 =new StringBuilder();

                sb1.append(PREFIX);

                sb1.append(BOUNDARY);

                sb1.append(LINEND);

                sb1.append("Content-Disposition: form-data; name=\"file\"; filename=\""

                        + file.getValue().getName() +"\"" + LINEND);

                sb1.append("Content-Type: application/octet-stream; charset="+ CHARSET + LINEND);

                sb1.append(LINEND);

                outStream.write(sb1.toString().getBytes());

                InputStream is =new FileInputStream(file.getValue());

                byte[] buffer =new byte[1024];

                int len = 0;

                while((len = is.read(buffer)) != -1) {

                    outStream.write(buffer,0, len);

                }

                is.close();

                outStream.write(LINEND.getBytes());

            }

        // 请求结束标志

        byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINEND).getBytes();

        outStream.write(end_data);

        outStream.flush();

        // 得到响应码

        int res = conn.getResponseCode();

        InputStream in = conn.getInputStream();

        StringBuilder sb2 =new StringBuilder();

        if(res == 200) {

            int ch;

            while((ch = in.read()) != -1) {

                sb2.append((char) ch);
            }
        }
        outStream.close();







        conn.disconnect();

        String name = new String(sb2.toString().getBytes("iso-8859-1"), "UTF-8");

        return name;

    }



    
}
