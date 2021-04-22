/*
 * Copyright (C) 2015 Square, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.yue.base.middle.net.convert;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import cn.yue.base.common.utils.constant.EncryptUtils;
import cn.yue.base.common.utils.debug.LogUtils;
import cn.yue.base.middle.init.InitConstant;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Converter;

/**
 * request请求 body参数解析
 * Created by yue on 2018/7/25.
 */

final class SignGsonRequestBodyConverter<T> implements Converter<T, RequestBody> {
  private static final MediaType MEDIA_TYPE = MediaType.parse("application/json; charset=UTF-8");
  private final Gson gson;
  private final TypeAdapter<T> adapter;

  SignGsonRequestBodyConverter(Gson gson, TypeAdapter<T> adapter) {
    this.gson = gson;
    this.adapter = adapter;
  }

  //body 里的内容直接修改为统一的参数；并且拦截器会判断直接使用该requestBody
  @Override
  public RequestBody convert(T value) throws IOException {
    LogUtils.i("okhttp", "  origin :  " + value);
    String encodeData = gson.toJson(value);
    return RequestBody.create(MEDIA_TYPE, gson.toJson(getBody(encodeData)));
  }


  private Map<String, Object> getBody(String encodeData){
    Map<String, Object> tmp = new HashMap<>();
    long time = System.currentTimeMillis() / 1000;
    try {
//      if(isDataEncode){
//        encodeData = URLEncoder.encode(encodeData.toString(), "utf-8");
//        encodeData = encodeData.replaceAll("\\+", "%20");
//
//      }
      String appVersion = InitConstant.getVersionName();
      String deviceId = InitConstant.getDeviceId();
      String sign = EncryptUtils.encryptMD5ToString((appVersion + InitConstant.APP_CLIENT_TYPE + encodeData +
              deviceId + time + InitConstant.APP_SIGN_KEY).getBytes());
      tmp.put("app_version", appVersion);
      tmp.put("client_type", InitConstant.APP_CLIENT_TYPE + "");
      tmp.put("data", encodeData);
      tmp.put("device_id", deviceId);
      tmp.put("time", time + "");
      tmp.put("sign", sign);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return tmp;
  }

}
