package com.library.luo.android;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URLEncoder;

public class Main4Activity extends AppCompatActivity {

    private Button MoreButton=null;
    private Button sureButton=null;
    private Button cancelButton=null;

    private String bookname;
    private EditText editText=null;
    private String url="http://47.94.101.75/PhoneLibraryM/lookbook.do";

    @SuppressLint("HandlerLeak")
    Handler handler=new Handler(){//消息机制，用来在子线程中更新UI
        @Override
        public void handleMessage(Message msg) {
            //进行消息提示
            Toast.makeText(getApplicationContext(),(String)msg.obj,Toast.LENGTH_LONG).show();


        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);



        MoreButton=(Button) findViewById(R.id.button8);
        sureButton=(Button) findViewById(R.id.button10);
        cancelButton=(Button) findViewById(R.id.button9);

       editText=(EditText)findViewById(R.id.editText6);




       //进去图书馆
       MoreButton.setOnClickListener(new Button.OnClickListener()
       {
           @Override
           public void onClick(View v)
           {

               startActivity(new Intent(Main4Activity.this, Main3Activity.class));

           }


       });

       //取消
       cancelButton.setOnClickListener(new Button.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

       editText.setText("");

            }


        });

       //进行查询
       sureButton.setOnClickListener(new Button.OnClickListener()
       {
           @Override
           public void onClick(View v)
           {


               bookname=editText.getText().toString().trim();

               if(bookname.equals(""))
               {
                   Message message = Message.obtain();
                   message.what = 3;
                   message.obj = "查询输入不得为空";
                   handler.sendMessage(message);
               }

               else {
                   new Thread() {

                       private HttpURLConnection connection;

                       //直接查询前四本书
                       @Override
                       public void run() {

                           try {
                               //封装成传输数据的键值对,无论get还是post,传输中文时都要进行url编码（RULEncoder）
                               // 如果是在浏览器端的话，它会自动进行帮我们转码，不用我们进行手动设置
                               //第三个参数是对操作的形式做了个标记
                               String data2 = "&index=" + URLEncoder.encode(String.valueOf("1"), "utf-8") + "&sign=" + URLEncoder.encode("2", "utf-8") + "&bookname=" + URLEncoder.encode(bookname, "utf-8");

                               //利用HttpConnectionUtils类进行互联网连接
                               connection = HttpConnectionUtils.getConnection(data2, url, 7000);
                               int code = connection.getResponseCode();
                               if (code == 200) {

                                   String str = StreamChangeStrUtils.toChange(connection.getInputStream());//写个工具类流转换成字符串


                                   JSONObject jsonObject = JSONObject.fromObject(str);


                                   String sign = jsonObject.getString("sign");


                                   Message message2 = Message.obtain();
                                   message2.what = 3;
                                   message2.obj = sign;
                                   handler.sendMessage(message2);

                                   if (sign.equals("查询成功")) {
                                       //获取图片的资源的函数

                                       String bookname = jsonObject.getString("bookname");
                                       String bookid = jsonObject.getString("bookid");
                                       String bookwriter = jsonObject.getString("bookwriter");
                                       Double bookprice = new Double(jsonObject.getString("bookprice"));


                                        /*
                                        Book b = new Book();
                                        b.setBookid(bookid);
                                        b.setBookname(bookname);
                                        b.setBookprice(bookprice);
                                        b.setBookwriter(bookwriter);
                                          */
                                       // 返回数据的获取的操作
                                       Intent intent = new Intent(Main4Activity.this, Main5Activity.class);
                                       intent.putExtra("bookname", bookname);
                                       intent.putExtra("bookid", bookid);
                                       intent.putExtra("bookwriter", bookwriter);
                                       intent.putExtra("bookprice", String.valueOf(bookprice));
                                       // 新打开的activity返回的数据
                                       Main4Activity.this.startActivity(intent);


                                   }


                               } else {
                                   Message message = Message.obtain();
                                   message.what = 3;
                                   message.obj = "连接超时";
                                   handler.sendMessage(message);
                               }


                           } catch (Exception e) {//会抛出很多个异常，这里抓一个大的异常
                               e.printStackTrace();
                               Message message = Message.obtain();
                               message.what = 3;
                               message.obj = "服务器异常...请稍后再试";
                               handler.sendMessage(message);

                           }
                       }
                   }.start();//不要忘记开线程


                   editText.setText("");
               }





           }


       });




    }

}
