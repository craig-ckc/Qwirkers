package com.example.qwirkers.Utility;

import android.app.Activity;

import androidx.appcompat.app.AppCompatActivity;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class CommunicationThread extends Thread {
    private Activity activity;
    private String serverAddress;
    private Runnable action;

    public CommunicationThread(Activity activity, Runnable action, String serverAddress) {
        super();
        this.activity = activity;
        this.action = action;
        this.serverAddress = serverAddress;
    }

    protected void command(DataInputStream dis, DataOutputStream dos){
        activity.runOnUiThread(action);
    }

    @Override
    public void run() {
        Socket client;
        DataInputStream dis;
        DataOutputStream dos;

        try {
            client = new Socket(serverAddress, 500);

            dis = new DataInputStream(client.getInputStream());
            dos = new DataOutputStream(client.getOutputStream());

            command(dis, dos);

            client.close();
        } catch (IOException e) {

        }

    }

}