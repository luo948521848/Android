package com.library.luo.android;


import

        android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import android.widget.Toast;


import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;

import java.net.URLEncoder;



/*
*该类的功能以及特点描述：这是android的主界面


*该类是否被编译测试：是
*@see(与之相关联的类)：  来源：
*                     中间：HttpConnectionUtils.class 、StreamChangeStrUtils
*                     去处：Main2Activity 、LibraryActivity
*开发公司或单位：成007个人
*版权：成007

*@author(作者)：成007
*@since（该文件使用的jdk）：
*@version（版本）：

*@date(开发日期)：2018/4/9
*改进：
*最后更改日期：
*/
public class MainActivity extends AppCompatActivity {

    private Button cancelButton=null;
    private Button sureButton=null;
    private Button register=null;
    private EditText nameEditText=null;
    private EditText passwordEditText=null;
    private String url="http://47.94.101.75/PhoneLibraryM/login.do";
    private Integer time=8000;
    String name=null;
    String password=null;
    @SuppressLint("HandlerLeak")
    Handler handler=new Handler(){//消息机制，用来在子线程中更新UI
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){//具体消息，具体显示
                case 1:  //登录成功
                    Toast.makeText(getApplicationContext(),(String)msg.obj,Toast.LENGTH_LONG).show();
                    break;
                case 2:  //登录失败
                    Toast.makeText(getApplicationContext(),(String)msg.obj,Toast.LENGTH_LONG).show();
                    break;
                case 3:  //服务器异常
                    Toast.makeText(getApplicationContext(),(String)msg.obj,Toast.LENGTH_LONG).show();
                    break;

            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //通过id获取button和textview
        cancelButton=(Button) findViewById(R.id.button4);
        sureButton=(Button) findViewById(R.id.button6);
        //注册按钮
        register=(Button)findViewById(R.id.button5);

        //用户名
        nameEditText=(EditText)findViewById(R.id.editText3);
        //密码
        passwordEditText=(EditText)findViewById(R.id.editText4);


        //取消键的响应事件
        cancelButton.setOnClickListener(new Button.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //将用户名和密码的编辑框清空

                nameEditText.setText("");
                passwordEditText.setText("");

            }


        });

        //确认键的响应事件

        sureButton.setOnClickListener(new Button.OnClickListener()
                                {
                                    @Override
                                    public void onClick(View v) {



                                        name = nameEditText.getText().toString().trim();
                                        password = passwordEditText.getText().toString().trim();


                                        //进行远程数据库访问
                                        if(name.equals("") || password.equals("")){
                                            Toast.makeText(MainActivity.this,"用户名或密码不能为空",Toast.LENGTH_SHORT).show();
                                        }//进行登录操作(联网操作要添加权限)
                                        else {
                                            //联网操作要开子线程，在主线程不能更新UI
                                            new Thread(){

                                                private HttpURLConnection connection;


                                                @Override
                                                public void run() {

                                                    try {
                                                        //封装成传输数据的键值对,无论get还是post,传输中文时都要进行url编码（RULEncoder）
                                                        // 如果是在浏览器端的话，它会自动进行帮我们转码，不用我们进行手动设置
                                                        //第三个参数是对操作的形式做了个标记
                                                        String data2= "&location="+ URLEncoder.encode("","utf-8")+"&name="+ URLEncoder.encode(name,"utf-8")+"&password="+ URLEncoder.encode(password,"utf-8")+"&sign="+URLEncoder.encode("1","utf-8");


                                                        //利用HttpConnectionUtils类进行互联网连接
                                                        //设置url、访问时间


                                                        connection= HttpConnectionUtils.getConnection(data2,url,time);
                                                        int code = connection.getResponseCode();
                                                        if(code==200) {

                                                            String str = StreamChangeStrUtils.toChange( connection.getInputStream());//写个工具类流转换成字符串
                                                            JSONObject jsonObject=JSONObject.fromObject(str);
                                                            String str2=jsonObject.getString("sign");

                                                            if (str2.equals("登录成功")) {
                                                                Message message = Message.obtain();//更新UI就要向消息机制发送消息
                                                                message.what = 1;//用来标志是哪个消息
                                                                message.obj = str2;//消息主体
                                                                handler.sendMessage(message);

                                                                String name=jsonObject.getString("name");
                                                               String location=jsonObject.getString("location");

                                                                //利用Bundle将用户基本信息传给LibraryActivity
                                                                Intent intent = new Intent(MainActivity.this,Main4Activity.class);
                                                                intent.putExtra("name",name);
                                                                intent.putExtra(location,location);
                                                                MainActivity.this.startActivity(intent);
                                                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                                                //MainActivity.this.finish();

                                                            } else if(str2.equals("登录失败")){
                                                                Message message = Message.obtain();
                                                                message.what = 2;
                                                                message.obj = "账号或者密码错误";
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
                           }
                                    }

        );


        //注册键的响应事件
        register.setOnClickListener(new Button.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //进行界面跳转  跳转至注册界面   返回数据标识码为0

                Intent intent=new Intent();
                intent.setClass(MainActivity.this, Main2Activity.class);
                startActivityForResult(intent,0);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);

            }


        });



    }

    //对注册成成功的Activity进行数据回调登录
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        {
            if (requestCode == 0) {
                //获取Activity2数据进行登录操作
                Bundle bundle = data.getExtras();
                name= bundle.getString("name");
                password = bundle.getString("password");

                new Thread() {

                    private HttpURLConnection connection;


                    @Override
                    public void run() {

                        try {
                            //封装成传输数据的键值对,无论get还是post,传输中文时都要进行url编码（RULEncoder）
                            // 如果是在浏览器端的话，它会自动进行帮我们转码，不用我们进行手动设置
                            //第三个参数是对操作的形式做了个标记
                            String data2 = "&location="+ URLEncoder.encode("","utf-8")+ "&name=" + URLEncoder.encode(name, "utf-8") + "&password=" + URLEncoder.encode(password, "utf-8") + "&sign=" + URLEncoder.encode("1", "utf-8");


                            //利用HttpConnectionUtils类进行互联网连接
                            connection = HttpConnectionUtils.getConnection(data2,url,time);
                            int code = connection.getResponseCode();
                            if (code == 200) {
                                InputStream inputStream = connection.getInputStream();
                                String str = StreamChangeStrUtils.toChange(inputStream);//写个工具类流转换成字符串

                                JSONObject jsonObject=JSONObject.fromObject(str);
                                String str2=jsonObject.getString("sign");
                                if (str2.equals("登录成功")) {
                                    Message message = Message.obtain();//更新UI就要向消息机制发送消息
                                    message.what = 1;//用来标志是哪个消息
                                    message.obj = str2;//消息主体
                                    handler.sendMessage(message);
                                    String name=jsonObject.getString("name");
                                    String location=jsonObject.getString("location");

                                    //利用Bundle将用户基本信息传给LibraryActivity
                                    Intent intent = new Intent(MainActivity.this,Main4Activity.class);
                                    intent.putExtra("name",name);
                                    intent.putExtra(location,location);
                                    MainActivity.this.startActivity(intent);
                                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                    //MainActivity.this.finish();

                                } else if(str2.equals("登录失败")){
                                    Message message = Message.obtain();
                                    message.what = 2;
                                    message.obj = "账号或者密码错误";
                                    handler.sendMessage(message);
                                }
                            } else {
                                Message message = Message.obtain();
                                message.what = 3;
                                message.obj = "连接超时";
                                handler.sendMessage(message);
                            }
                        } catch (Exception e) {//会抛出很多个异常，这里抓一个大的异常
                            e.printStackTrace();

                        }
                    }
                }.start();//不要忘记开线程


            }


        }
    }
}
