package com.yostar.uniqueid.net;

import com.yostar.uniqueid.model.BaseRsp;
import com.yostar.uniqueid.model.InitReq;
import com.yostar.uniqueid.model.InitRsp;
import com.yostar.uniqueid.model.LoginReq;
import com.yostar.uniqueid.model.LoginRsp;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface ServiceApi {
  @Headers({"Content-Type: application/json"})
  @POST("/device/match")
  Call<BaseRsp<InitRsp>> callInit(
          @Header ("Authorization") String authorization,
          @Body InitReq initReq);

  @Headers({"Content-Type: application/json"})
  @PUT("/device/account")
  Call<BaseRsp<LoginRsp>> callLogin(
          @Header ("Authorization") String authorization,
          @Body LoginReq loginReq);
}

