package com.x6.arcade.analysis;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpUtils {

    public static String sendPostRequest(String url, String postData) throws IOException {
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        try {
            // 创建URL对象
            URL requestUrl = new URL(url);

            // 打开连接
            connection = (HttpURLConnection) requestUrl.openConnection();

            // 设置请求方法为POST
            connection.setRequestMethod("POST");

            // 设置请求头部信息
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("charset", "utf-8");

            // 允许输出数据
            connection.setDoOutput(true);

            // 获取输出流并写入请求体数据
            DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
            outputStream.writeBytes(postData);
            outputStream.flush();
            outputStream.close();

            // 获取响应状态码
            int responseCode = connection.getResponseCode();

            // 读取响应数据
            StringBuilder response = new StringBuilder();
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }

            // 返回响应数据
            return response.toString();
        } finally {
            // 关闭连接和输入流
            if (reader != null) {
                reader.close();
            }
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

}

