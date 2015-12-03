package jp.freebit.databasetest;

import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.freebit.databasetest.util.LogUtil;

public class HandlerTestActivity extends ActionBarActivity {

    private static final String TAG = HandlerTestActivity.class.getSimpleName();

    @Bind(R.id.testText)
    TextView testText;

    @Bind(R.id.testButton)
    Button testButton;

    private HandlerThread mThread;

    Looper mLooper;

    Handler mHandler;

    Handler mMainHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handler_test);
        ButterKnife.bind(this);
        testButton.setText("start");
        start();
    }

    @OnClick(R.id.testButton)
    public void onClick(View v) {
        LogUtil.d(TAG, "onClick " + mHandler.getLooper().getThread().isAlive());
        if (count == 0) {
            mHandler.post(mRunnable);
            testButton.setText("stop");
        } else {
            count = 0;
            mHandler.removeCallbacks(mRunnable);
            testButton.setText("start");
        }
    }

    @OnClick(R.id.testButtonQuit)
    public void onClickQuit() {
        LogUtil.d(TAG, "onClickQuit " + mMainHandler.getLooper().toString() + ", " + mLooper);
        mLooper.quit();
        start();
        LogUtil.d(TAG, "onClickQuit " + mThread.isAlive());
    }

    int count = 0;

    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            mMainHandler.post(mMainRunnable);
            count++;
            boolean a = mHandler.postDelayed(mRunnable, 100);
            if (!a) {
                LogUtil.e(TAG, "message post not success!!");
            }
        }
    };

    private Runnable mMainRunnable = new Runnable() {
        @Override
        public void run() {
            testText.setText(count + "");
        }
    };

    private void start() {
        mThread = new HandlerThread("test") {{
            this.start();
        }};
        mLooper = mThread.getLooper();
        mHandler = new Handler(mLooper);
    }

}
