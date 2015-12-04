package net.wasnot.android.databasetest.activity;

import com.squareup.otto.Subscribe;

import net.wasnot.android.databasetest.R;
import net.wasnot.android.databasetest.bus.BusHolder;
import net.wasnot.android.databasetest.bus.SwitchEvent;
import net.wasnot.android.databasetest.fragment.ItemFragment;
import net.wasnot.android.databasetest.fragment.TestActivityFragment;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test2);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
        getSupportFragmentManager().beginTransaction().replace(R.id.content, new TestActivityFragment()).commit();
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

    @Subscribe
    public void onSwitch(SwitchEvent event) {
        switch (event.next) {
            case R.id.buttonListRealm:
                setTitle("Realm");
                getSupportFragmentManager().beginTransaction().replace(R.id.content, ItemFragment.newInstance(5, 0))
                        .addToBackStack(null).commit();
                break;
            case R.id.buttonListSQLite:
                setTitle("SQLite");
                getSupportFragmentManager().beginTransaction().replace(R.id.content, ItemFragment.newInstance(5, 1))
                        .addToBackStack(null).commit();
                break;
        }
    }
}
