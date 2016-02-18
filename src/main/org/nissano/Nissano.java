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
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Nissano extends Activity {

  private PersistentStorage storage;
  private Button heatButton;
  private ViewManager viewManager;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    storage = new PersistentStorage(this);

    setContentView(R.layout.main);
    heatButton = (Button) findViewById(R.id.activate_hvac_button);
    viewManager = new ViewManager(this);

    Proto.Credentials data = storage.read();
    if (data.hasUsername()) viewManager.setUsername(data.getUsername());
    if (data.hasPassword()) viewManager.setPassword(data.getPassword());
    if (data.hasVin()) viewManager.setVin(data.getVin());

    heatButton.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        Proto.Credentials credentials = storage.read();
        if (!credentials.getUsername().equals(viewManager.getUsername())
            || !credentials.getPassword().equals(viewManager.getPassword())
            || !credentials.getVin().equals(viewManager.getVin())) {
          Proto.Credentials.Builder builder = credentials.toBuilder();
          if (!builder.getUsername().equals(viewManager.getUsername())) {
            builder.setUsername(viewManager.getUsername());
          }
          if (!builder.getPassword().equals(viewManager.getPassword())) {
            builder.setPassword(viewManager.getPassword());
          }
          if (!builder.getVin().equals(viewManager.getVin())) {
            builder.setVin(viewManager.getVin());
          }
          credentials = builder.build();
          storage.write(credentials);
        }
        viewManager.setStatus("Sending request...");
        new WebClientAsyncTask(viewManager).execute(credentials);
      }
    });
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    return false;
  }
}
