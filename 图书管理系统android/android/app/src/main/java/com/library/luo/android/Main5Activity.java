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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

public class Main5Activity extends AppCompatActivity {


    private String bookname=null;
    private String bookid=null;
    private String bookwriter=null;
    private String bookprice=null;

    private Button BackButton=null;
    private TextView textView=null;
    private TextView textView1=null;
    private TextView textView2=null;
    private TextView textView3=null;

    private ImageView imageView=null;

    private Bitmap bitmap;
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
        setContentView(R.layout.activity_main5);


        // 获取数据
        Bundle bundle = this.getIntent().getExtras();

        bookname=bundle.getString("bookname");
        bookid=bundle.getString("bookid");
        bookwriter=bundle.getString("bookwriter");
        bookprice=bundle.getString("bookprice");


        imageView=(ImageView) findViewById(R.id.imageView14);

        //书名
        textView1=(TextView)findViewById(R.id.textView20);
        //作者
        textView2=(TextView)findViewById(R.id.textView21);
        //价格
        textView3=(TextView)findViewById(R.id.textView22);
        //书号
        textView=(TextView)findViewById(R.id.textView19);

        BackButton=(Button) findViewById(R.id.button13);



        textView.setText(bookid);
        textView1.setText(bookname);
        textView2.setText(bookwriter);
        textView3.setText(bookprice);




        // image
      Thread thread=  new Thread(new Runnable(){
            String path="http://47.94.101.75:8080/PhoneLibraryM/img/"+bookid+".jpg";
            @Override
            public void run() {

                try {
                    bitmap = new HttpBitMap().GetBitmap(path);

                    //加载图片资源

                } catch (Exception e) {
                    e.getStackTrace();
                }

            }});
            thread.start();
            try {

                //通过join()等待将资源加载成功后再进行设置
                thread.join();
                imageView.setImageBitmap(bitmap);
            }
            catch (Exception e)
            {
                e.getStackTrace();
            }




        BackButton.setOnClickListener(new Button.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                Main5Activity.this.finish();

            }


        });








    }
}
