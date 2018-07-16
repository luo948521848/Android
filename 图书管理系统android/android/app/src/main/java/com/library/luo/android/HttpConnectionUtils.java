package com.library.luo.android;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;


/*
*该类的功能以及特点描述：这是用于进行互联网访问


*该类是否被编译测试：否
*@see(与之相关联的类)：  来源：
*                     中间：
*                     去处：
*开发公司或单位：成007个人
*版权：成007

*@author(作者)：成007
*@since（该文件使用的jdk）：
*@version（版本）：

*@date(开发日期)：2018/5/1
*改进：
*最后更改日期：
*/

class HttpConnectionUtils {


    public static HttpURLConnection getConnection(String data,String url1,int time) throws Exception {
        //通过URL对象获取联网对象
        //可以根据需要替换成其他URL路径
        URL url = new URL(url1);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("Content-type", "text/html");
        connection.setRequestProperty("Content-Type", "application/xml");
        connection.setRequestProperty("Accept-Charset", "utf-8");
        connection.setRequestProperty("contentType", "utf-8");
        connection.setRequestMethod("GET");
        connection.setReadTimeout(time);
        connection.setDoOutput(true);
        connection.setDoInput(true);

        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded ");
        connection.setRequestProperty("Content-Length", data.length() + "");
        OutputStream outputStream = connection.getOutputStream();
        outputStream.write(data.getBytes());

        return connection;
    }
}
