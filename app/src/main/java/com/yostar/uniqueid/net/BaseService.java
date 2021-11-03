package com.yostar.uniqueid.net;

import com.yostar.uniqueid.model.BaseRsp;
import com.yostar.uniqueid.model.InitReq;
import com.yostar.uniqueid.model.InitRsp;
import com.yostar.uniqueid.model.LoginReq;
import com.yostar.uniqueid.model.LoginRsp;
import com.yostar.uniqueid.util.GsonUtils;
import com.yostar.uniqueid.util.LogUtil;
import com.yostar.uniqueid.util.MessageDigestUtils;
import com.yostar.uniqueid.SDKConst;
import com.yostar.uniqueid.util.SPUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class BaseService {

  private static final String BASE_URL = "https://test-ad-api.yostar.net";
  private static final String SIGN_TAG = "4aeb195cd69ed93520b9b4129636264e0cdc0153";

  protected Retrofit retrofit2;
  protected ServiceApi mBaseServiceApi;

  public BaseService() {
    super();

    if (retrofit2 == null) {
      retrofit2 = NetClientManage.getInstance().getRetrofit(BASE_URL);
    }

    mBaseServiceApi = retrofit2.create(ServiceApi.class);
  }

  protected String getHeaderAuth(String jsonBody) {
    String deviceID = SPUtils.getInstance().getString(SDKConst.SP_DEVICE_ID, "");
    return getHeaderAuth(deviceID, jsonBody);
  }

  protected String getHeaderAuth(String deviceID, String jsonBody) {
    Map<String, Object> mapHead = new LinkedHashMap<>();
    mapHead.put("DeviceID", deviceID);
    mapHead.put("Time", Math.round(new Date().getTime() / 1000));
    String jsonHead = GsonUtils.gsonToString(mapHead);
    String jsonSign = MessageDigestUtils.md5(jsonHead + jsonBody + SIGN_TAG);
    Map<String, Object> mapAuth = new HashMap();
    mapAuth.put("Head", mapHead);
    mapAuth.put("Sign", jsonSign);
    String authStr = GsonUtils.gsonToString(mapAuth);
    return authStr;
  }

  //用户公告
  public void netInit(List<InitReq.TypeData> typeData) {
    InitReq initReq = new InitReq();
    initReq.setType_data(typeData);

    String jsonBody = GsonUtils.gsonToString(initReq);
    Call<BaseRsp<InitRsp>> call = mBaseServiceApi.callInit(getHeaderAuth("", jsonBody), initReq);
    call.enqueue(new Callback<BaseRsp<InitRsp>>() {
      @Override
      public void onResponse(Call<BaseRsp<InitRsp>> call, Response<BaseRsp<InitRsp>> response) {
        if (response.code() == 200) {
          String deviceID = response.body().data.getDevice_id();
          String UDID = response.body().data.getUD_id();
          SPUtils.getInstance().put(SDKConst.SP_DEVICE_ID, deviceID);
          SPUtils.getInstance().put(SDKConst.SP_UD_ID, UDID);
          LogUtil.i(deviceID);
          LogUtil.i(UDID);
        }
      }

      @Override
      public void onFailure(Call<BaseRsp<InitRsp>> call, Throwable t) {
        LogUtil.i(t.getMessage());
      }
    });
  }

  //用户公告
  public void netLogin(String accountID) {
    LoginReq loginReq = new LoginReq();
    loginReq.setAccount_id(accountID);

    String jsonBody = GsonUtils.gsonToString(loginReq);
    Call<BaseRsp<LoginRsp>> call = mBaseServiceApi.callLogin(getHeaderAuth(jsonBody), loginReq);
    call.enqueue(new Callback<BaseRsp<LoginRsp>>() {
      @Override
      public void onResponse(Call<BaseRsp<LoginRsp>> call, Response<BaseRsp<LoginRsp>> response) {
        LogUtil.i(response.code());
      }

      @Override
      public void onFailure(Call<BaseRsp<LoginRsp>> call, Throwable t) {
        LogUtil.i(t.getMessage());
      }
    });
  }

}
