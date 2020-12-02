package ca.responsivetech.pebblewizlights;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.getpebble.android.kit.PebbleKit;
import com.getpebble.android.kit.util.PebbleDictionary;

import java.util.Random;
import java.util.UUID;

public class LightService extends Service {
    final int AppKeyContactName = 0;
    final int AppKeyAge = 1;
    private PebbleKit.PebbleDataReceiver dataReceiver;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        Context ctx = getApplicationContext();
        SharedPreferences prefs = ctx.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        // appUuid = UUID.fromString(prefs.getString(getString(R.string.preference_uuid), null));

        dataReceiver = new PebbleKit.PebbleDataReceiver(Constants.WATCH_UUID) {
            @Override
            public void receiveData(Context context, int transactionId, PebbleDictionary data) {
                // Always ACK
                PebbleKit.sendAckToPebble(context, transactionId);

                LightControl lc = new LightControl("10.0.0.70", 38899);

                lc.setLedBlue(new Random().nextInt(255-0) + 0);
                lc.setLedRed(new Random().nextInt(255-0) + 0);
                lc.setLedGreen(new Random().nextInt(255-0) + 0);
                lc.setLedDimming(100);

                Thread thread = new Thread(lc);
                thread.start();
            }
        };

        PebbleKit.registerReceivedDataHandler(this, dataReceiver);

        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
