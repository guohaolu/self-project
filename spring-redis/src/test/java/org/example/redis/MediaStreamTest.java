package org.example.redis;

import com.google.common.collect.Tables;
import org.junit.jupiter.api.Test;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * MediaStreamTest
 */
public class MediaStreamTest {
    private final String audioUrl = "https://api.weixin.qq.com/cgi-bin/media/get?access_token=87_PQ872JkeP4NUb6bkAW9ibgpV0wVmDTuN7wDPtiHvPhNQIt4EyoRbZ1y2GianuQaTu28y0c32zm5e7UO7YXJ9z_kEIO6dzqDaei0uI7QU0mhqBRlhZRVyuvf91bABAAhAHAVUU&media_id=X7hqT99X6dKQsq3eOngp4ghXZxfKFDPgz1CSZXrv9ZEkBw9jrCqVlo8XEy3SIS85";

    @Test
    public void testStream(HttpServletResponse response) {
        HttpURLConnection connection = null;
        InputStream inputStream = null;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        try {
            // 建立URL连接
            URL url = new URL(audioUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            // 获取输入流
            inputStream = connection.getInputStream();

            // 将输入流读入字节数组
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, bytesRead);
            }

            // 设置响应头
            response.setContentType("audio/amr");
            response.setContentLength(byteArrayOutputStream.size());

            // 将字节数组内容写入响应输出流
            OutputStream out = response.getOutputStream();
            byteArrayOutputStream.writeTo(out);
            out.flush();

        } catch (Exception e) {
            // 处理异常
            e.printStackTrace();
        } finally {
            // 关闭连接和流
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (connection != null) {
                    connection.disconnect();
                }
                byteArrayOutputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Tables.toTable()
    }
}
