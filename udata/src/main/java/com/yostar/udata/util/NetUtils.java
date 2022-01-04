package com.yostar.udata.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.webkit.WebSettings;

import com.yostar.udata.Interface.CallbackValue;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;

public class NetUtils {
    //--ip
    public static String getIpAddress(Context context){
        String localIP = "";
        NetworkInfo info = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (info != null && info.isConnected()) {
            // 3/4g网络
            if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
                try {
                    for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                        NetworkInterface intf = en.nextElement();
                        for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                            InetAddress inetAddress = enumIpAddr.nextElement();
                            if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                                localIP = inetAddress.getHostAddress();
                                LogUtil.i("getLocalIp 3/4g网络 " + localIP);
                                return localIP;
                            }
                        }
                    }
                } catch (SocketException e) {
                    e.printStackTrace();
                }

            } else if (info.getType() == ConnectivityManager.TYPE_WIFI) {
                //  wifi网络
                WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                localIP = intIP2StringIP(wifiInfo.getIpAddress());
                LogUtil.i("getLocalIp wifi网络 " + localIP);
                return localIP;
            }  else if (info.getType() == ConnectivityManager.TYPE_ETHERNET){
                // 有限网络
                localIP = getLocalIp();
                LogUtil.i("getLocalIp 有限网络 " + localIP);
                return localIP;
            }
        }
        return null;
    }

    private static String intIP2StringIP(int ip) {
        return (ip & 0xFF) + "." +
                ((ip >> 8) & 0xFF) + "." +
                ((ip >> 16) & 0xFF) + "." +
                (ip >> 24 & 0xFF);
    }


    // 获取有限网IP
    private static String getLocalIp() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface
                    .getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf
                        .getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()
                            && inetAddress instanceof Inet4Address) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException ex) {

        }
        return "0.0.0.0";

    }

    /**
     * 获取外网ip地址（非本地局域网地址）的方法
     */
    public static void getOutNetIP(CallbackValue callbackValue) {
        new Thread(){
            @Override
            public void run() {
                URL infoUrl = null;
                InputStream inStream = null;
                try {
                    infoUrl = new URL("https://ifconfig.co/json");
                    URLConnection connection = infoUrl.openConnection();
                    HttpURLConnection httpConnection = (HttpURLConnection) connection;
                    connection.setConnectTimeout(2000);
                    connection.setReadTimeout(2000);
                    int responseCode = httpConnection.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        inStream = httpConnection.getInputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(inStream, "utf-8"));
                        StringBuilder strber = new StringBuilder();
                        String line = null;
                        while ((line = reader.readLine()) != null)
                            strber.append(line + "\n");
                        inStream.close();
                        JSONObject jsonObject = new JSONObject(strber.toString());
                        //从反馈的结果中提取出IP地址
                        String ip = jsonObject.getString("ip");
                        LogUtil.i("getOutNetIP " + ip);
                        callbackValue.onCallback(ip);
                    }
                } catch (Exception e) {
                    callbackValue.onCallback("");
                }
            }
        }.start();

    }

    //--ua
    public static String getUserAgent(Context context) {
        String userAgent = "";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            try {
                userAgent = WebSettings.getDefaultUserAgent(context);
            } catch (Exception e) {
                userAgent = System.getProperty("http.agent");
            }
        } else {
            userAgent = System.getProperty("http.agent");
        }
        StringBuffer sb = new StringBuffer();
        for (int i = 0, length = userAgent.length(); i < length; i++) {
            char c = userAgent.charAt(i);
            if (c <= '\u001f' || c >= '\u007f') {
                sb.append(String.format("\\u%04x", (int) c));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }
}
