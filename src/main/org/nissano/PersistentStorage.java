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

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.util.Log;

public class PersistentStorage {
  private static String FILENAME = "nissano";
  private Context context;
  private Proto.Credentials data;

  public PersistentStorage(Context context) {
    this.context = context;
    this.data = null;
  }

  public Proto.Credentials read() {
    if (data == null) {
      data = readInternal();
    }
    if (data == null) {
      data =  Proto.Credentials.getDefaultInstance();
      write(data);
    }
    return data;
  }

  public void write(Proto.Credentials data) {
    this.data = data;
    writeInternal(data);
  }

  private void writeInternal(Proto.Credentials data) {
    FileOutputStream fos = null;
    try {
      try {
        fos = context.openFileOutput(FILENAME, Context.MODE_PRIVATE);
        fos.write(data.toByteArray());
      } finally {
        if (fos != null) {
          fos.close();
        }
      }
    } catch (IOException ioe) {
      Log.e("nissano", ioe.toString());
    }
  }

  private Proto.Credentials readInternal() {
    FileInputStream fis = null;
    try {
      try {
        fis = context.openFileInput(FILENAME);
        return Proto.Credentials.parseFrom(readFully(fis));
      } finally {
        if (fis != null) {
          fis.close();
        }
      }
    } catch (IOException exception) {
      return null;
    }
  }

  private static byte[] readFully(InputStream inputStream) throws IOException {
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    int next = inputStream.read();
    while (next > -1) {
      bos.write(next);
      next = inputStream.read();
    }
    bos.flush();
    return bos.toByteArray();
  }
}
