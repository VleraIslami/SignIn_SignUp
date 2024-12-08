package com.example.signin_signup;
import android.content.Intent;

import android.content.BroadcastReceiver;
import android.content.Context;

import android.util.Log;

public class TimeSetReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("TimeSetReceiver", "Received action: " + intent.getAction());

        // Check if the action is ACTION_TIME_SET
        if (intent.getAction() != null && intent.getAction().equals(Intent.ACTION_TIME_TICK)) {
            // Log the event or handle the necessary task when the time is set
            Log.d("TimeSetReceiver", "Time has been set.");

            // Example of further operations you might want to perform:
            // You could trigger a background service or update certain parts of your UI
            // For example, if you need to update a UI element or start a new activity:
            // Intent updateIntent = new Intent(context, SomeOtherActivity.class);
            // context.startActivity(updateIntent);
        }
    }
}
