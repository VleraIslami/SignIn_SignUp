package com.example.signin_signup;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import javax.xml.transform.Result;

public class MyBackgroundWorker extends Worker {

    public MyBackgroundWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        // Your background task logic here
        return Result.success(); // Return success or failure based on the operation
    }
}
