package com.smsdemo;

import com.alibaba.fastjson.JSON;
import com.smsdemo.util.SignUtil;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ganchangjian on 2018/9/27.
 */
public class SmsDemoServlet extends HttpServlet {
    //行云管家管理控制台 OpenAPI中的AccessKey信息
    private String accessKeySecret = "Vs9rmLAObPRwLcrEU42UjJ0Bmi6VJh";

        public void doGet(HttpServletRequest request, HttpServletResponse response)
                throws ServletException, IOException {
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            out.println("hello world");
            out.flush();
            out.close();
        }

        public void doPost(HttpServletRequest request, HttpServletResponse response)
                throws ServletException, IOException {
            response.setContentType("application/json; charset=UTF-8");
            PrintWriter out = response.getWriter();

            //时间戳
            String timestamp = request.getParameter("timestamp");
            //接口版本
            String version = request.getParameter("version");
            //随机数
            String nonce = request.getParameter("nonce");
            //签名工具签名后的结果
            String signature = request.getParameter("signature");
            //行云管家管理控制台 OpenAPI中的AccessKey信息
            String accessKeyId = request.getParameter("accessKeyId");

            Map<String, Object> signMap = new HashMap<>();
            signMap.put("timestamp", timestamp);
            signMap.put("version", version);
            signMap.put("nonce", nonce);
            signMap.put("accessKeyId", accessKeyId);

            //通过工具类生成签名，与请求中携带的签名信息比较是否相同
            String signature0 = SignUtil.sign(null, signMap, "POST", accessKeySecret);

            Map<String,Object> result = new HashMap<>();
            if (signature.equals(signature0)) {
                // body为短信模板参数，拿到参数去自有短信平台发送短信
                SmsRequest body = JSON.parseObject(request.getInputStream(), SmsRequest.class, null);
                result.put("success",true);
                result.put("msg","签名成功");
                System.out.println("签名成功");
            }else {
                //签名失败，非行云管家平台发送请求
                result.put("success",false);
                result.put("msg","签名失败");
                System.out.println("签名失败");
            }
            out.println(JSON.toJSONString(result));
            out.flush();
            out.close();
        }
}
