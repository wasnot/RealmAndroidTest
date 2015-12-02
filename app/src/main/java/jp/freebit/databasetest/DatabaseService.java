package jp.freebit.databasetest;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.SystemClock;

import io.realm.Realm;
import jp.freebit.databasetest.Realm.TestData;
import jp.freebit.databasetest.bus.BusHolder;
import jp.freebit.databasetest.bus.WriteEvent;
import jp.freebit.databasetest.util.LogUtil;

public class DatabaseService extends IntentService {

    private static final String TAG = DatabaseService.class.getSimpleName();

    private Handler mHandler = new Handler();

    public DatabaseService() {
        super(TAG);
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        LogUtil.d(TAG, "onHandleIntent");
//
//        Realm realm = Realm.getDefaultInstance();
//        realm.beginTransaction();
//        realm.copyToRealm(createData());
//        realm.commitTransaction();
//        realm.close();
//        setRepeat();
//        mHandler.post(mRunnable);

        Realm realm = Realm.getDefaultInstance();
        int count = 0;
        while (PreferenceUtil.isRepeatTestData(getApplicationContext())) {
            LogUtil.d(TAG, "doInBackground " + count);
            realm.beginTransaction();
            realm.copyToRealm(createData());
            realm.commitTransaction();
            if (count % 300 == 0) {
                mHandler.post(mRunnable);
            }
//                try {
//                    Thread.sleep(10);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
            count++;
        }
        realm.close();

    }

    private void setRepeat() {
        Context con = getApplicationContext();
        if (!PreferenceUtil.isRepeatTestData(con)) {
            return;
        }
        AlarmManager am = (AlarmManager) con.getSystemService(Context.ALARM_SERVICE);
        am.set(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime() + 200,
                PendingIntent.getService(con, 0, new Intent(con, DatabaseService.class), PendingIntent.FLAG_UPDATE_CURRENT));
    }

    private TestData createData() {
        int seed = (int) Math.round(Math.random() * 10);
        TestData data = new TestData();
        data.setName("name" + seed);
        data.setId(seed);
        data.setTimestamp(System.currentTimeMillis());
        return data;
    }

    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            BusHolder.get().post(new WriteEvent());
        }
    };
}
