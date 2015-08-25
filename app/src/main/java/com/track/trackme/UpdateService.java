package com.track.trackme;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * UpdateService to update the current position of the user at intervals
 */
public class UpdateService extends Service {
    UpdaterThread thread;
    boolean isRunning = false;
    static int delay = 6000;
    @Override
    public void onCreate() {
        super.onCreate();
        thread = new UpdaterThread();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        isRunning = true;
        thread.start();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        isRunning = false;
        thread.interrupt();
        super.onDestroy();

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    class UpdaterThread extends Thread{

        @Override
        public void run() {
            while (isRunning){
                try {
                    Thread.sleep(delay);
                }catch(Exception e){

                }
            }
        }
    }
}
