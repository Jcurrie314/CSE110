/*
 * Copyright (c) 2015-present, Parse, LLC.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.parse.tuber;

import android.app.Application;

import com.parse.Parse;


public class StarterApplication extends Application {

  @Override
  public void onCreate() {
    super.onCreate();


    // Enable Local Datastore.
    Parse.enableLocalDatastore(this);

    // Add your initialization code here
    Parse.initialize(this, "WkQIirCEXGI9k26ZGjXYZdLKiDiCpFpQpX19bWlo", "VAsXc5A8i9tK5VORdmHfoy8mr3cg2RBQLl7UHWEZ");


  }
}
