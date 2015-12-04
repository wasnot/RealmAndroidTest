package net.wasnot.android.databasetest.activity;

import net.wasnot.android.databasetest.R;
import net.wasnot.android.databasetest.fragment.ItemFragment;
import net.wasnot.android.databasetest.fragment.QueryFragment;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content, new QueryFragment()).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = new MenuInflater(this);
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_realm:
                setTitle("Realm");
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content, ItemFragment.newInstance(1, 0))
                        .addToBackStack(null).commit();
                break;
            case R.id.action_sqlite:
                setTitle("SQLite");
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content, ItemFragment.newInstance(1, 1))
                        .addToBackStack(null).commit();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
