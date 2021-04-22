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
import com.google.gson.GsonBuilder;

import java.io.IOException;

import retrofit2.Converter;

/**
 * request请求参数 解析
 * Created by yue on 2018/7/25.
 */

final class SignRequestStringConverter<T> implements Converter<T, String> {
  private static final Gson gson = new GsonBuilder().disableHtmlEscaping().create();

  SignRequestStringConverter() { }

  //@Query, @QueryMap 会走这里；识别如果不是基本类型；那么为数据类型并构建Bean，传入ClassName和Json，用于拦截器解析
  @Override
  public String convert(T t) throws IOException {
    if(t.getClass().getSuperclass() != Number.class && t.getClass() != String.class && t.getClass() != Boolean.class){
      RequestConverterBean bean = new RequestConverterBean();
      bean.setClassName(t.getClass().getName());
      bean.setJson(gson.toJson(t));
      return gson.toJson(bean);
    }
    return t.toString();
  }
}
