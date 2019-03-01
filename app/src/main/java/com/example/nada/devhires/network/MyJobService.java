package com.example.nada.devhires.network;

import android.util.Log;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

public class MyJobService extends JobService {

    public static final String TAG = MyJobService.class.getName();
    private boolean jobCancelled = false;

    @Override
    public boolean onStartJob(JobParameters job) {
        doBackgroundWork(job);
        return true; // Answers the question: "Is there still work going on?"
    }

    private void doBackgroundWork(final JobParameters params) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (jobCancelled) {
                    Log.d(TAG, "run: Cancel");
                    return;
                }

                try {
                    Thread.sleep(30000000);
                    Log.d(TAG, "run: Start");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                jobFinished(params, false);
            }
        }).start();
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        jobCancelled = true;
        return true; // Answers the question: "Should this job be retried?"
    }
}
