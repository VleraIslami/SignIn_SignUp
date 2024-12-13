package com.example.signin_signup;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

public class MainActivity extends AppCompatActivity {

    private TimeSetReceiver timeSetReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize the TimeSetReceiver
        timeSetReceiver = new TimeSetReceiver();

        // Register the receiver to listen for TIME_SET action
        IntentFilter filter = new IntentFilter(Intent.ACTION_TIME_TICK);
        registerReceiver(timeSetReceiver, filter);

        // Enqueue the background worker for handling long operations
        OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder(MyBackgroundWorker.class)
                .build();
        WorkManager.getInstance(this).enqueue(workRequest);  // Corrected API usage

        // Redirect to HomeActivity instead of LoginActivity
        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();  // Ensures MainActivity is not kept in the back stack
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Unregister the receiver to avoid memory leaks
        if (timeSetReceiver != null) {
            unregisterReceiver(timeSetReceiver);
        }
    }
}
