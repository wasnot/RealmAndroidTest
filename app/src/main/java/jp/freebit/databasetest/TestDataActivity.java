package jp.freebit.databasetest;

import com.squareup.otto.Subscribe;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmResults;
import jp.freebit.databasetest.Realm.TestData;
import jp.freebit.databasetest.bus.BusHolder;
import jp.freebit.databasetest.bus.WriteEvent;
import jp.freebit.databasetest.util.LogUtil;

/**
 * Created by akihiroaida on 15/12/02.
 */
public class TestDataActivity extends Activity {

    private static final String TAG = TestDataActivity.class.getSimpleName();

    @Bind(R.id.textView)
    TextView textView;

    @Bind(R.id.buttonTestData)
    Button buttonTestDataTask;

    @Bind(R.id.buttonTestDataService)
    Button buttonTestDataService;

    private TestTask mTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtil.d(TAG, "onCreate");
        setContentView(R.layout.activity_test_data);
        ButterKnife.bind(this);
        buttonTestDataTask.setText("startTask");
        if (PreferenceUtil.isRepeatTestData(this)) {
            buttonTestDataService.setText("stopService");
        } else {
            buttonTestDataService.setText("startService");
        }
        updateText();
    }

    @Override
    protected void onResume() {
        super.onResume();
        BusHolder.get().register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        BusHolder.get().unregister(this);
    }


    @OnClick({R.id.buttonTestData, R.id.buttonTestDataService})
    public void onStartTestData(View v) {
        LogUtil.d(TAG, "onStartTestData");
        if (v.equals(buttonTestDataTask)) {
            if (mTask != null) {
                mTask.cancel(false);
                mTask = null;
                buttonTestDataTask.setText("startTask");
            } else {
                mTask = new TestTask();
                mTask.execute();
                buttonTestDataTask.setText("stopTask");
            }
        } else if (v.equals(buttonTestDataService)) {
            if (!PreferenceUtil.isRepeatTestData(this)) {
                PreferenceUtil.setRepeatTestData(this, true);
                startService(new Intent(this, DatabaseService.class));
                buttonTestDataService.setText("stopService");
            } else {
                PreferenceUtil.setRepeatTestData(this, false);
                buttonTestDataService.setText("startService");
            }
        }
    }

    @Subscribe
    public void onWrite(WriteEvent event) {
        LogUtil.d(TAG, "onWrite");
        updateText();
    }

    private class TestTask extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] params) {
            LogUtil.d(TAG, "doInBackground");
            Realm realm = Realm.getDefaultInstance();
            int count = 0;
            while (!isCancelled()) {
                LogUtil.d(TAG, "doInBackground " + count);
                realm.beginTransaction();
                realm.copyToRealm(createData());
                realm.commitTransaction();
                if (count % 300 == 0) {
                    publishProgress();
                }
//                try {
//                    Thread.sleep(10);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
                count++;
            }
            realm.close();
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
        }

        @Override
        protected void onProgressUpdate(Object[] values) {
            super.onProgressUpdate(values);
            LogUtil.d(TAG, "onProgressUpdate");
            updateText();
        }
    }

    private TestData createData() {
        int seed = (int) Math.round(Math.random() * 10);
        TestData data = new TestData();
        data.setName("name" + seed);
        data.setId(seed);
        data.setTimestamp(System.currentTimeMillis());
        return data;
    }

    private void updateText() {
        Realm realm = Realm.getDefaultInstance();
        long count = realm.where(TestData.class).count();
        String time = "-";
        RealmResults<TestData> result = realm.where(TestData.class).findAllSorted("timestamp", false);
        if (result.size() > 0) {
            time = result.first().getTimestamp() + "";
        }
        realm.close();
        textView.setText("count:" + count + "\ntime:" + time + "\nnow:" + System.currentTimeMillis());
    }
}
