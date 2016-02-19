/**
 *    Copyright 2016 Keith Wannamaker
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.nissano;

import java.io.IOException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.nissano.Proto.Credentials;

import android.os.AsyncTask;
import android.util.Log;

public class WebClientAsyncTask extends AsyncTask<Proto.Credentials, Void, Boolean> {

  private static int TIMEOUT_MILLIS = 5 * 60 * 1000;  // five minutes
  private static String CHROME_USER_AGENT =
      "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36";

  private final ViewManager viewManager;
  private String extendedStatus;

  public WebClientAsyncTask(ViewManager viewManager) {
    this.viewManager = viewManager;
    this.extendedStatus = "";
  }

  @Override
  protected Boolean doInBackground(Credentials... credentials) {
    boolean success = false;
    try {
      success = activateHvac(credentials[0]);
    } catch (IOException e) {
      Log.e("nissano", e.toString());
      extendedStatus = e.getMessage();
      success = false;
    }
    return success;
  }

  @Override
  protected void onPostExecute(Boolean success) {
    viewManager.setStatus(success ? "Success" : "Failed " + extendedStatus);
  }

  private boolean activateHvac(Proto.Credentials credentials) throws IOException {
    HttpClient httpClient = new HttpClient();
    httpClient.getParams().setParameter(
        HttpMethodParams.USER_AGENT,
        CHROME_USER_AGENT);
    httpClient.getParams().setParameter("http.socket.timeout", Integer.valueOf(TIMEOUT_MILLIS));
    PostMethod post = new PostMethod("https://www.nissanusa.com/nowners/user/login") {
      @Override
      public boolean getFollowRedirects() {
        return true;
      }
    };
    post.addParameter("username", credentials.getUsername());
    post.addParameter("password", credentials.getPassword());
    httpClient.executeMethod(post);

    post = new PostMethod("https://www.nissanusa.com/nowners/EV/setHvac");
    post.addParameter("vin", credentials.getVin());
    post.addParameter("fan", "on");
    httpClient.executeMethod(post);

    if (post.getStatusCode() != 200) {
      extendedStatus = String.format("Activate hvac returned %d.", post.getStatusCode());
      return false;
    }
    String result = post.getResponseBodyAsString();
    if (result.equals("true")) {
      return true;
    }
    extendedStatus = "Nissan responded with an error.";
    return false;
  }
}
