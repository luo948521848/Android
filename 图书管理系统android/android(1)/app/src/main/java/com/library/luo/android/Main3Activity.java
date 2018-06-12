package com.library.luo.android;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
*该类的功能以及特点描述：这是用户的操作界面


*该类是否被编译测试：否
*@see(与之相关联的类)：  来源：MainActivity
*                     中间：StreamChangeStrUtils  HttpConnectionUtils
*                     去处：
*开发公司或单位：成007个人
*版权：成007

*@author(作者)：成007
*@since（该文件使用的jdk）：
*@version（版本）：

*@date(开发日期)：2018/5/5
*改进：
*最后更改日期：
*/
public class Main3Activity extends AppCompatActivity {

    private User user;
    private Button lastpageButton=null;
    private Button nextpageButton=null;

    private ImageButton UserImageButton=null;
    private ImageButton ShoppingCarButton=null;

    private ImageView imageView1=null;
    private ImageView imageView2=null;
    private ImageView imageView3=null;
    private ImageView imageView4=null;

    private TextView textView1=null;
    private TextView textView2=null;
    private TextView textView3=null;
    private TextView textView4=null;

    private Map<String,Bitmap>map=new HashMap<>();
    private List<Book> list=new ArrayList<>();

    private String sign=null;

    //用i来记录页面数
    private int page=1;
    private int bookIndex=0;
    private int maxbookIndex=0;
    private String url="http://47.94.101.75:8080//PhoneLibraryM/lookbooks.do";
    @SuppressLint("HandlerLeak")
    Handler handler=new Handler(){//消息机制，用来在子线程中更新UI
        @Override
        public void handleMessage(Message msg) {
            //进行消息提示
            Toast.makeText(getApplicationContext(),(String)msg.obj,Toast.LENGTH_LONG).show();


            }

    };


    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);


        //获取自定的控件
        lastpageButton=(Button) findViewById(R.id.button);
        nextpageButton=(Button) findViewById(R.id.button7);

        imageView1=(ImageView) findViewById(R.id.imageView8);
        imageView2=(ImageView) findViewById(R.id.imageView9);
        imageView3=(ImageView) findViewById(R.id.imageView10);
        imageView4=(ImageView) findViewById(R.id.imageView11);

        textView1=(TextView)findViewById(R.id.textView8);
        textView2=(TextView)findViewById(R.id.textView9);
        textView3=(TextView)findViewById(R.id.textView10);
        textView4=(TextView)findViewById(R.id.textView11);


        UserImageButton=(ImageButton)findViewById(R.id.imageButton);
        ShoppingCarButton=(ImageButton)findViewById(R.id.imageButton3);

         /*获取Intent中的Bundle对象*/
        Intent intent=this.getIntent();
        String name=intent.getStringExtra("name");
        String location=intent.getStringExtra("location");




        //查询获得前多个图书信息  再通过书的id查询图片资源
       Thread thread=  new Thread(new Runnable(){

            private HttpURLConnection connection;

            //直接查询前四本书
            @Override
            public void run() {

                try {
                    //封装成传输数据的键值对,无论get还是post,传输中文时都要进行url编码（RULEncoder）
                    // 如果是在浏览器端的话，它会自动进行帮我们转码，不用我们进行手动设置
                    //第三个参数是对操作的形式做了个标记
                    String data2 = "&index=" + URLEncoder.encode(String.valueOf(bookIndex), "utf-8") + "&sign=" + URLEncoder.encode("1", "utf-8") + "&bookname=" + URLEncoder.encode("", "utf-8");

                    //利用HttpConnectionUtils类进行互联网连接
                    connection = HttpConnectionUtils.getConnection(data2, url, 15000);
                    int code = connection.getResponseCode();
                    if (code == 200) {

                        String str = StreamChangeStrUtils.toChange(connection.getInputStream());//写个工具类流转换成字符串


                        JSONArray array = JSONArray.fromObject(str);
                        for (int i = 0; i < array.size(); i++)

                        {

                            maxbookIndex=array.size()-1;
                            JSONObject jsonObject = (JSONObject) array.getJSONObject(i);
                            if (i == 0) {

                                sign = jsonObject.getString("sign");


                            } else {
                                String bookname=jsonObject.getString("bookname");
                                String bookid=jsonObject.getString("bookid");
                                String bookwriter=jsonObject.getString("bookwriter");
                                Double bookprice=new Double(jsonObject.getString("bookprice"));

                                Book b = new Book();
                                b.setBookid(bookid);
                                b.setBookname(bookname);
                                b.setBookprice(bookprice);
                                b.setBookwriter(bookwriter);
                                list.add(b);
                                String path="http://47.94.101.75:8080/PhoneLibrarya/img/"+b.getBookid()+".jpg";
                                map.put(bookid,new HttpBitMap().GetBitmap(path));
                            }

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
        });//不要忘记开线程*/


        thread.start();

        try {
            //通过join()等待将资源加载成功后再进行设置
            thread.join();
            if(sign.equals("查询成功"))

            {textView1.setText(list.get(bookIndex).getBookname());
            textView2.setText(list.get(bookIndex+1).getBookname());
            textView3.setText(list.get(bookIndex+2).getBookname());
            textView4.setText(list.get(bookIndex+3).getBookname());

            imageView1.setImageBitmap(map.get(list.get(bookIndex).getBookid()));
            imageView2.setImageBitmap(map.get(list.get(bookIndex+1).getBookid()));
            imageView3.setImageBitmap(map.get(list.get(bookIndex+2).getBookid()));
            imageView4.setImageBitmap(map.get(list.get(bookIndex+3).getBookid()));


            }
            else
            {
                Message message = Message.obtain();
                message.what = 3;
                message.obj = sign;
                handler.sendMessage(message);


            }
        }
        catch (Exception e)
        {
            e.getStackTrace();
        }

    //上一页操作
        lastpageButton.setOnClickListener(new Button.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                page--;
                bookIndex=bookIndex-4;
                if(page==1)
                {
                    textView1.setText(list.get(bookIndex).getBookname());
                    textView2.setText(list.get(bookIndex+1).getBookname());
                    textView3.setText(list.get(bookIndex+2).getBookname());
                    textView4.setText(list.get(bookIndex+3).getBookname());

                    imageView1.setImageBitmap(map.get(list.get(bookIndex).getBookid()));
                    imageView2.setImageBitmap(map.get(list.get(bookIndex+1).getBookid()));
                    imageView3.setImageBitmap(map.get(list.get(bookIndex+2).getBookid()));
                    imageView4.setImageBitmap(map.get(list.get(bookIndex+3).getBookid()));

                }
                else
                {
                    page=1;
                    bookIndex=bookIndex+4;
                    Message message = Message.obtain();//更新UI就要向消息机制发送消息
                    message.obj = "已达到首页";//消息主体
                    handler.sendMessage(message);

                }



            }


        });
        //下一页操作
        nextpageButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                page++;
                bookIndex = bookIndex + 4;
                if (bookIndex <8) {


                    textView1.setText(list.get(bookIndex).getBookname());
                    textView2.setText(list.get(bookIndex + 1).getBookname());
                    textView3.setText(list.get(bookIndex + 2).getBookname());
                    textView4.setText(list.get(bookIndex + 3).getBookname());

                    imageView1.setImageBitmap(map.get(list.get(bookIndex).getBookid()));
                    imageView2.setImageBitmap(map.get(list.get(bookIndex + 1).getBookid()));
                    imageView3.setImageBitmap(map.get(list.get(bookIndex + 2).getBookid()));
                    imageView4.setImageBitmap(map.get(list.get(bookIndex + 3).getBookid()));


                }
                else
                {
                    bookIndex=bookIndex-4;
                    page--;

                    Message message = Message.obtain();//更新UI就要向消息机制发送消息
                    message.obj = "已达到最后一页";//消息主体
                    handler.sendMessage(message);



                }

            }
        }
        );


                    //点击查看购物车

                    ShoppingCarButton.setOnClickListener(new Button.OnClickListener() {
                        @Override
                        public void onClick(View v) {


                        }


                    });


                    //点击查看个人信息

                    UserImageButton.setOnClickListener(new Button.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            //方法一 通过弹出对话框的方式


                        }


                    });


                    //点击（或者长按）图书了解图书详情 并且可以进行加入购物车的操作

    }
            }

