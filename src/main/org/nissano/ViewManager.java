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

import android.app.Activity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class ViewManager {
  private final EditText usernameEditText;
  private final EditText passwordEditText;
  private final EditText vinEditText;
  private final TextView statusText;

  public ViewManager(Activity activity) {
    usernameEditText = (EditText) activity.findViewById(R.id.username);
    passwordEditText = (EditText) activity.findViewById(R.id.password);
    vinEditText = (EditText) activity.findViewById(R.id.vin);
    statusText = (TextView) activity.findViewById(R.id.status);
  }

  public void setUsername(String username) {
    usernameEditText.setText(username);
  };

  public String getUsername() {
    return usernameEditText.getText().toString();
  }

  public void setPassword(String password) {
    passwordEditText.setText(password);
  }

  public String getPassword() {
    return passwordEditText.getText().toString();
  }

  public void setVin(String vin) {
    vinEditText.setText(vin);
  }

  public String getVin() {
    return vinEditText.getText().toString();
  }

  public void setStatus(String status) {
    statusText.setText(status);
    statusText.setVisibility(View.VISIBLE);
  }
}
