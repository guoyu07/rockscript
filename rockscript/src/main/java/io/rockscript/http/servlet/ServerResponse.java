/*
 * Copyright (c) 2017 RockScript.io.
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package io.rockscript.http.servlet;

import com.google.gson.Gson;
import io.rockscript.http.client.Http;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.Charset;

public class ServerResponse {

  HttpServletResponse response;
  Gson gson;

  public ServerResponse(HttpServletResponse response) {
    this.response = response;
  }

  public ServerResponse(HttpServletResponse response, Gson gson) {
    this.response = response;
    this.gson = gson;
  }

  public ServerResponse statusOk() {
    return status(200);
  }

  public ServerResponse statusNotFound() {
    return status(404);
  }

  public ServerResponse statusInternalServerError() {
    return status(500);
  }

  public ServerResponse status(int status) {
    this.response.setStatus(status);
    return this;
  }

  public ServerResponse bodyString(String responseBody) {
    try {
      ServletOutputStream out = response.getOutputStream();
      byte[] bytes = responseBody.getBytes(Charset.forName("UTF-8"));
      out.write(bytes);
      out.flush();
    } catch (IOException e) {
      throw new RuntimeException("Couldn't send body: "+e.getMessage(), e);
    }
    return this;
  }

  public ServerResponse bodyJson(Object object) {
    return bodyString(gson.toJson(object));
  }

  public ServerResponse header(String name, String value) {
    if (name!=null && value!=null) {
      response.addHeader(name, value);
    }
    return this;
  }

  public ServerResponse headerContentType(String value) {
    return header(Http.Headers.CONTENT_TYPE, value);
  }

  public ServerResponse headerContentTypeTextPlain() {
    return header(Http.Headers.CONTENT_TYPE, Http.ContentTypes.TEXT_PLAIN);
  }

  public ServerResponse headerContentTypeApplicationJson() {
    return header(Http.Headers.CONTENT_TYPE, Http.ContentTypes.APPLICATION_JSON);
  }

  public ServerResponse headerContentTypeTextHtml() {
    return header(Http.Headers.CONTENT_TYPE, Http.ContentTypes.TEXT_HTML);
  }
}