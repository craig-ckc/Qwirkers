package com.example.qwirkers.Utility;

import android.app.Activity;

public class FunctionThread extends Thread {
    private final Activity activity;
    private final Runnable action;

    public FunctionThread(Activity activity, Runnable action) {
        super();
        this.activity = activity;
        this.action = action;
    }

    @Override
    public void run() {
        activity.runOnUiThread(action);
    }
}
