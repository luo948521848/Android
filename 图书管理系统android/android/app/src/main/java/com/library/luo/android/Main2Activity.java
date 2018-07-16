package com.library.luo.android;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;


import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URLEncoder;

/*
*该类的功能以及特点描述：这是注册界面


*该类是否被编译测试：是
*@see(与之相关联的类)：  来源：MainActivity
*                     中间：StreamChangeStrUtils  HttpConnectionUtils
*                     去处：MainActivity
*开发公司或单位：成007个人
*版权：成007

*@author(作者)：成007
*@since（该文件使用的jdk）：
*@version（版本）：

*@date(开发日期)：2018/4/23
*改进：
*最后更改日期：
*/

public class Main2Activity extends AppCompatActivity {

    private Button cancelButton=null;
    private Button sureButton=null;

    private EditText nameEditText=null;
    private EditText passwordEditText=null;
    private EditText locationEditText=null;
    private String name=null;
    private String password=null;
    private String location=null;
    private String url="http://47.94.101.75/PhoneLibraryM/login.do";
    private Integer time=5000;
    @SuppressLint("HandlerLeak")

    Handler handler=new Handler(){//消息机制，用来在子线程中更新UI
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){//具体消息，具体显示
                case 1:  //注册成功
                    Toast.makeText(getApplicationContext(),(String)msg.obj,Toast.LENGTH_LONG).show();
                    break;
                case 2:  //注册失败
                    Toast.makeText(getApplicationContext(),(String)msg.obj,Toast.LENGTH_LONG).show();
                    break;
                case 3:  //服务器异常   可以使用带图片提醒的Toast
                     Toast toast= Toast.makeText(getApplicationContext(),(String)msg.obj,Toast.LENGTH_LONG);
                     toast.setGravity(Gravity.CENTER,0,0);
                    LinearLayout toastView=(LinearLayout)toast.getView();
                    ImageView imageCode=new ImageView((getApplicationContext()));
                    imageCode.setImageResource(R.drawable.icon);
                    toastView.addView(imageCode);
                     toast.show();
                    break;

            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        //分别获取name，password  取消确认按钮

        //通过id获取button和textview
        cancelButton=(Button) findViewById(R.id.button2);
        sureButton=(Button) findViewById(R.id.button3);


        //用户名
        nameEditText=(EditText)findViewById(R.id.editText);
        //密码
        passwordEditText=(EditText)findViewById(R.id.editText2);
        //收货地址
        locationEditText=(EditText)findViewById(R.id.editText5);


        //取消键的响应事件
        cancelButton.setOnClickListener(new Button.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
         //跳转到登录界面

                startActivity(new Intent(Main2Activity.this, MainActivity.class));

            }


        });

        //确认键的响应事件

        sureButton.setOnClickListener(new Button.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //获取用户名和密码进行用户注册
                name=nameEditText.getText().toString();
                password=passwordEditText.getText().toString();
                location=locationEditText.getText().toString();


                //联网操作要开子线程，在主线程不能更新UI
                new Thread(){

                    private HttpURLConnection connection;


                    @Override
                    public void run() {

                        try {
                            //封装成传输数据的键值对,无论get还是post,传输中文时都要进行url编码（RULEncoder）
                            // 如果是在浏览器端的话，它会自动进行帮我们转码，不用我们进行手动设置
                            //第三个参数是对操作的形式做了个标记
                            String data2= "&location="+ URLEncoder.encode(location,"utf-8")+"&name="+ URLEncoder.encode(name,"utf-8")+"&password="+ URLEncoder.encode(password,"utf-8")+"&sign="+URLEncoder.encode("2","utf-8");

                            //利用HttpConnectionUtils类进行互联网连接
                            connection= HttpConnectionUtils.getConnection(data2,url,time);
                            int code = connection.getResponseCode();
                            if(code==200){


                                InputStream inputStream = connection.getInputStream();
                                String str = StreamChangeStrUtils.toChange(inputStream);//写个工具类流转换成字符串

                                if (str.equals("注册成功")) {
                                    Message message = Message.obtain();//更新UI就要向消息机制发送消息
                                    message.what = 1;//用来标志是哪个消息
                                    message.obj = "注册成功，请登录";//消息主体
                                    handler.sendMessage(message);


                                    //利用数据回调机制回到登录Activity进行登录

                                    Intent intent=new Intent();
                                    intent.putExtra("name",name);
                                    intent.putExtra("password",password);
                                    intent.setClass(Main2Activity.this, MainActivity.class);

                                    setResult(0, intent);

                                    //关闭本Activity
                                    Main2Activity.this.finish();

                                } else {
                                    Message message = Message.obtain();
                                    message.what = 2;
                                    message.obj = "用户已存在";
                                    handler.sendMessage(message);
                                }
                            }
                            else
                            {
                                Message message = Message.obtain();
                                message.what=3;
                                message.obj="连接超时";
                                handler.sendMessage(message);
                            }
                        } catch (Exception e) {//会抛出很多个异常，这里抓一个大的异常
                            e.printStackTrace();

                        }
                    }
                }.start();//不要忘记开线程



            }


        });




    }
}
