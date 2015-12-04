
package net.wasnot.android.databasetest.activity;

import net.wasnot.android.databasetest.R;
import net.wasnot.android.databasetest.Realm.User;
import net.wasnot.android.databasetest.RealmUtil;
import net.wasnot.android.databasetest.SQLite.TestDatabase;
import net.wasnot.android.databasetest.SQLite.UserItem;
import net.wasnot.android.databasetest.SQLiteUtil;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import io.realm.Realm;

public class MainActivity extends ActionBarActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    @Bind(R.id.listRealm)
    View listRealm;

    @Bind(R.id.listSqlite)
    View listSqlite;

    @Bind(R.id.editText)
    EditText editText;

    ListHolder realmHolder;

    ListHolder sqliteHolder;

    int count = 10000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        realmHolder = new ListHolder(listRealm);
        realmHolder.textTitle.setText("realm");
        sqliteHolder = new ListHolder(listSqlite);
        sqliteHolder.textTitle.setText("sqlite");
        editText.setText(String.valueOf(count));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        // noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @OnClick({R.id.button, R.id.button2})
    void onClick(View v) {
        switch (v.getId()) {
            case R.id.button:
                new AsyncTask<Void, Void, Void>() {
                    long make, add, pull, delete;

                    @Override
                    protected Void doInBackground(Void... params) {
                        Context con = getApplicationContext();
                        TestDatabase db = new TestDatabase(con);
                        //
                        long start = System.currentTimeMillis();
                        List<UserItem> items = SQLiteUtil.makeUsers(count);
                        make = System.currentTimeMillis() - start;
                        //
                        start = System.currentTimeMillis();
                        SQLiteUtil.addBulk(db, items);
                        add = System.currentTimeMillis() - start;
                        //
                        start = System.currentTimeMillis();
                        SQLiteUtil.count(db);
                        pull = System.currentTimeMillis() - start;
                        //
                        start = System.currentTimeMillis();
                        SQLiteUtil.remove(db);
                        delete = System.currentTimeMillis() - start;
                        //
                        Log.d(TAG, "count:" + SQLiteUtil.count(db));
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void aVoid) {
                        super.onPostExecute(aVoid);
                        sqliteHolder.textValue
                                .setText(make + ", " + add + ", " + pull + ", " + delete);
                    }
                }.execute();
                break;
            case R.id.button2:
                new AsyncTask<Void, Void, Void>() {
                    long make, add, pull, delete;

                    @Override
                    protected Void doInBackground(Void... params) {
                        Realm realm = Realm.getDefaultInstance();
                        //
                        long start = System.currentTimeMillis();
                        List<User> items = RealmUtil.makeUsers(count);
                        make = System.currentTimeMillis() - start;
                        //
                        start = System.currentTimeMillis();
                        RealmUtil.addBulk(realm, items);
                        add = System.currentTimeMillis() - start;
                        //
                        start = System.currentTimeMillis();
                        RealmUtil.pull(realm);
                        pull = System.currentTimeMillis() - start;
                        //
                        start = System.currentTimeMillis();
                        RealmUtil.drop(realm);
                        delete = System.currentTimeMillis() - start;
                        //
                        Log.d(TAG, "count:" + RealmUtil.pull(realm));
                        realm.close();
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void aVoid) {
                        super.onPostExecute(aVoid);
                        realmHolder.textValue
                                .setText(make + ", " + add + ", " + pull + ", " + delete);
                    }
                }.execute();
                break;
        }
    }

    @OnTextChanged(R.id.editText)
    public void onTextChanged(CharSequence text) {
        try {
            count = Integer.parseInt(text.toString());
        } catch (NumberFormatException e) {
            e.printStackTrace();
            editText.setText(String.valueOf(count));
        }
    }

    public class ListHolder {

        @Bind(R.id.textTitle)
        TextView textTitle;

        @Bind(R.id.textValue)
        TextView textValue;

        public ListHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
