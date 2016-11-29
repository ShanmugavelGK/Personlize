package com.augusta.dev.personalize.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.augusta.dev.personalize.PersonalizeActivity;

public class BootCompleted extends BroadcastReceiver {

   @Override
   public void onReceive(Context context, Intent intent) {

      if(intent.getAction().equalsIgnoreCase("android.intent.action.BOOT_COMPLETED")) {

         PersonalizeActivity.customNotification(context);
      }
   }
}