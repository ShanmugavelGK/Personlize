package com.augusta.dev.personalize.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.augusta.dev.personalize.PersonalizeActivity;
import com.augusta.dev.personalize.utliz.CommonFunction;
import com.augusta.dev.personalize.utliz.Constants;
import com.augusta.dev.personalize.utliz.Preference;

public class BootCompleted extends BroadcastReceiver {

   @Override
   public void onReceive(Context context, Intent intent) {

      if(intent.getAction().equalsIgnoreCase("android.intent.action.BOOT_COMPLETED")) {

         if(Preference.getSharedPreferenceBoolean(context, "Already", false) == true &&
                 Preference.getSharedPreferenceBoolean(context, Constants.ENABLE_NOTIFICATION, false)) {
            CommonFunction.customNotification(context);
         }
      }
   }
}