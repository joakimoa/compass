package com.ojojoj.compass;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

public class MusicService extends Service {
    MediaPlayer mp;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    public void onCreate() {
        mp = MediaPlayer.create(this, R.raw.dn3d);
        mp.setLooping(false);
    }

    public void onDestroy() {
        mp.stop();
    }
    public void onStart(Intent intent, int startid){
        mp.start();
    }
}