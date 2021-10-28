package com.yostar.uniqueid.net;

import com.yostar.uniqueid.util.LogUtil;

import java.io.IOException;
import java.nio.charset.Charset;

import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;

public class LoggingInterceptor implements Interceptor {

  @Override public Response intercept(Chain chain) throws IOException {

    Request request = chain.request();

    LogUtil.i(" ");
    LogUtil.i("************** Start ******************");
    LogUtil.i("Request: " + request.url());
    LogUtil.i("Authorization：" + request.header("Authorization"));
    if ("POST".equals(request.method())) {
      StringBuilder sb = new StringBuilder();
      if (request.body() instanceof FormBody) {
        FormBody body = (FormBody) request.body();
        sb.append("FormBody: ");
        for (int i = 0; i < body.size(); i++) {
          sb.append(body.name(i)).append(" = ").append(body.value(i)).append(", ");
        }
        sb.delete(sb.length() - 1, sb.length());
        LogUtil.i(sb.toString());
      } else {
        try {
          Buffer buffer = new Buffer();
          request.body().writeTo(buffer);
          Charset charset = Charset.forName("utf-8");
          LogUtil.i("Body：" + buffer.readString(charset));
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }

    long startTime = System.currentTimeMillis();
    Response response = chain.proceed(request);
    long endTime = System.currentTimeMillis();
    long duration = endTime - startTime;



    ResponseBody responseBody = response.body();
    BufferedSource source = responseBody.source();
    source.request(Long.MAX_VALUE); // Buffer the entire body.
    Buffer buffer = source.buffer();
    Charset UTF8 = Charset.forName("UTF-8");

    LogUtil.i("Response: " + buffer.clone().readString(UTF8));
    LogUtil.i("************** End " + duration + " 毫秒 ******************");
    LogUtil.i(" ");
    return response;
  }
}

