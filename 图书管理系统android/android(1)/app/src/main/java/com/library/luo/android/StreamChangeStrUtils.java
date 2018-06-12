package com.library.luo.android;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by luo on 2018/5/2.
 */

class /*
*该类的功能以及特点描述：这是将流转化成字符串的工具类


*该类是否被编译测试：否
*@see(与之相关联的类)：  来源：
*                     中间：
*                     去处：
*开发公司或单位：成007个人
*版权：成007

*@author(作者)：成007
*@since（该文件使用的jdk）：
*@version（版本）：

*@date(开发日期)：2018/4/9
*改进：
*最后更改日期：
*/StreamChangeStrUtils {
    public static String toChange(InputStream inputStream) throws Exception {
        BufferedReader in = new BufferedReader(new InputStreamReader(inputStream, "utf-8"));
        StringBuffer buffer = new StringBuffer();
        String line = "";
        while ((line = in.readLine()) != null){
            buffer.append(line);
        }
        String str = buffer.toString();


        return str;
    }

}
