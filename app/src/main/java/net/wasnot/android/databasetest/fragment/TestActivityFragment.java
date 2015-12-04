package net.wasnot.android.databasetest.fragment;

import net.wasnot.android.databasetest.R;
import net.wasnot.android.databasetest.Realm.User;
import net.wasnot.android.databasetest.RealmUtil;
import net.wasnot.android.databasetest.SQLite.TestDatabase;
import net.wasnot.android.databasetest.SQLite.UserColumns;
import net.wasnot.android.databasetest.SQLiteUtil;
import net.wasnot.android.databasetest.bus.BusHolder;
import net.wasnot.android.databasetest.bus.SwitchEvent;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * A placeholder fragment containing a simple view.
 */
public class TestActivityFragment extends Fragment {

    @Bind(R.id.valueRealm)
    TextView valueRealm;

    @Bind(R.id.valueSQLite)
    TextView valueSQLite;

    @Bind(R.id.editValue)
    EditText editValue;

    private MyTask myTask;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_test, container, false);
        ButterKnife.bind(this, v);
        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.buttonListRealm, R.id.buttonListSQLite})
    public void onNext(View v) {
        switch (v.getId()) {
            case R.id.buttonListRealm:
            case R.id.buttonListSQLite:
                BusHolder.get().post(new SwitchEvent(v.getId()));
                break;
        }
    }

    @OnClick({R.id.buttonInsertRealm, R.id.buttonBulkInsertRealm, R.id.buttonRemoveRealm, R.id.buttonDropRealm,
            R.id.buttonInsertSQLite, R.id.buttonBulkInsertSQLite, R.id.buttonRemoveSQLite, R.id.buttonDropSQLite,
            R.id.buttonSelectRealm, R.id.buttonSelectSQLite, R.id.buttonSelectAllRealm, R.id.buttonSelectAllSQLite
            , R.id.buttonSelectOneRealm, R.id.buttonSelectOneSQLite, R.id.buttonSelectViewSQLite, R.id.buttonSelectViewRealm})
    public void onClick(View v) {
        if (myTask == null) {
            myTask = new MyTask();
            myTask.execute(v.getId(), Integer.parseInt(editValue.getText().toString()));
        }
    }

    private class MyTask extends AsyncTask<Integer, Void, Long> {

        boolean isRealm;

        long time;

        String ope = "unknown";

        @Override
        protected Long doInBackground(Integer[] params) {
            Context con = getActivity();
            long start;
            long result = 0;
            long count = params[1];
            Realm realm;
            TestDatabase db;
            List list;
            Cursor cursor;
            switch (params[0]) {
                case R.id.buttonInsertRealm:
                    isRealm = true;
                    ope = "insert";
                    realm = Realm.getDefaultInstance();
                    list = RealmUtil.makeUsers2((int) count);
                    start = System.currentTimeMillis();
                    RealmUtil.add(realm, list);
                    time = System.currentTimeMillis() - start;
                    result = RealmUtil.pull(realm);
                    realm.close();
                    break;
                case R.id.buttonBulkInsertRealm:
                    isRealm = true;
                    ope = "insertBulk";
                    realm = Realm.getDefaultInstance();
                    list = RealmUtil.makeUsers2((int) count);
                    start = System.currentTimeMillis();
                    RealmUtil.addBulk(realm, list);
                    time = System.currentTimeMillis() - start;
                    result = RealmUtil.pull(realm);
                    realm.close();
                    break;
                case R.id.buttonRemoveRealm:
                    isRealm = true;
                    ope = "remove";
                    realm = Realm.getDefaultInstance();
                    RealmResults results = realm.where(User.class).findAll();
                    start = System.currentTimeMillis();
                    realm.beginTransaction();
                    for (int i = 0; i < count; i++) {
                        results.removeLast();
                    }
                    realm.commitTransaction();
                    time = System.currentTimeMillis() - start;
                    result = RealmUtil.pull(realm);
                    realm.close();
                    break;
                case R.id.buttonDropRealm:
                    isRealm = true;
                    ope = "drop";
                    realm = Realm.getDefaultInstance();
                    start = System.currentTimeMillis();
                    RealmUtil.drop(realm);
                    time = System.currentTimeMillis() - start;
                    result = RealmUtil.pull(realm);
                    realm.close();
                    break;
                case R.id.buttonSelectRealm:
                    isRealm = true;
                    ope = "select";
                    realm = Realm.getDefaultInstance();
                    start = System.currentTimeMillis();
                    result = RealmUtil.pull(realm);
                    time = System.currentTimeMillis() - start;
                    realm.close();
                    break;
                case R.id.buttonSelectAllRealm:
                    isRealm = true;
                    ope = "selectAll";
                    realm = Realm.getDefaultInstance();
                    start = System.currentTimeMillis();
                    result = RealmUtil.pullAll(realm);
                    time = System.currentTimeMillis() - start;
                    realm.close();
                    break;
                case R.id.buttonSelectOneRealm:
                    isRealm = true;
                    ope = "selectOne";
                    realm = Realm.getDefaultInstance();
                    start = System.currentTimeMillis();
                    RealmUtil.pullOne(realm);
                    time = System.currentTimeMillis() - start;
                    result = RealmUtil.pull(realm);
                    realm.close();
                    break;
                case R.id.buttonSelectViewRealm:
                    isRealm = true;
                    ope = "selectView";
                    realm = Realm.getDefaultInstance();
                    start = System.currentTimeMillis();
                    RealmResults<User> results1 = realm.where(User.class).findAll();
                    List<Integer> list1 = new ArrayList<>();
                    for (User o : results1) {
                        int age = o.getAge();
                        if (!list1.contains(age)) {
                            list1.add(age);
                        }
                    }
                    time = System.currentTimeMillis() - start;
                    result = list1.size();
                    realm.close();
                    break;

                case R.id.buttonInsertSQLite:
                    isRealm = false;
                    ope = "insert";
                    db = new TestDatabase(con);
                    list = SQLiteUtil.makeUsers2((int) count);
                    start = System.currentTimeMillis();
                    SQLiteUtil.add(db, list);
                    time = System.currentTimeMillis() - start;
                    result = SQLiteUtil.count(db);
                    db.closeDatabase();
                    break;
                case R.id.buttonBulkInsertSQLite:
                    isRealm = false;
                    ope = "insertBulk";
                    db = new TestDatabase(con);
                    list = SQLiteUtil.makeUsers2((int) count);
                    start = System.currentTimeMillis();
                    SQLiteUtil.addBulk(db, list);
                    time = System.currentTimeMillis() - start;
                    result = SQLiteUtil.count(db);
                    db.closeDatabase();
                    break;
                case R.id.buttonRemoveSQLite:
                    isRealm = false;
                    ope = "remove";
                    db = new TestDatabase(con);
                    start = System.currentTimeMillis();
                    for (int i = 0; i < count; i++) {
                        db.deleteLast();
                    }
                    time = System.currentTimeMillis() - start;
                    result = SQLiteUtil.count(db);
                    db.closeDatabase();
                    break;
                case R.id.buttonDropSQLite:
                    isRealm = false;
                    ope = "drop";
                    db = new TestDatabase(con);
                    start = System.currentTimeMillis();
                    SQLiteUtil.remove(db);
                    time = System.currentTimeMillis() - start;
                    result = SQLiteUtil.count(db);
                    db.closeDatabase();
                    break;
                case R.id.buttonSelectSQLite:
                    isRealm = false;
                    ope = "select";
                    db = new TestDatabase(con);
                    start = System.currentTimeMillis();
                    cursor = db.getCursor(null, null, null, null);
                    result = cursor.getCount();
                    cursor.close();
                    time = System.currentTimeMillis() - start;
                    db.closeDatabase();
                    break;
                case R.id.buttonSelectAllSQLite:
                    isRealm = false;
                    ope = "selectAll";
                    db = new TestDatabase(con);
                    start = System.currentTimeMillis();
                    cursor = db.getCursor(null, null, null, null);
                    result = cursor.getCount();
                    cursor.moveToFirst();
                    int indexAge = cursor.getColumnIndex(UserColumns.AGE);
                    int indexName = cursor.getColumnIndex(UserColumns.NAME);
                    int indexEmail = cursor.getColumnIndex(UserColumns.EMAIL);
                    for (int i = 0; i < result; i++) {
                        cursor.getInt(indexAge);
                        cursor.getString(indexName);
                        cursor.getString(indexEmail);
                        cursor.moveToNext();
                    }
                    cursor.close();
                    time = System.currentTimeMillis() - start;
                    db.closeDatabase();
                    break;
                case R.id.buttonSelectOneSQLite:
                    isRealm = false;
                    ope = "selectOne";
                    db = new TestDatabase(con);
                    start = System.currentTimeMillis();
                    cursor = db.getCursor(null, null, null, null, "1");
                    cursor.close();
                    time = System.currentTimeMillis() - start;
                    result = SQLiteUtil.count(db);
                    db.closeDatabase();
                    break;
                case R.id.buttonSelectViewSQLite:
                    isRealm = false;
                    ope = "selectView";
                    db = new TestDatabase(con);
                    start = System.currentTimeMillis();
                    cursor = db.getCursor("select * from users group by age", null);
                    result = cursor.getCount();
                    cursor.close();
                    time = System.currentTimeMillis() - start;
                    db.closeDatabase();
                    break;
            }
            return result;
        }

        @Override
        protected void onPostExecute(Long o) {
            super.onPostExecute(o);
            if (isRealm) {
                valueRealm.setText(ope + ":" + o + " rows, " + time + "ms");
            } else {
                valueSQLite.setText(ope + ":" + o + " rows, " + time + "ms");
            }
            myTask = null;
        }
    }

}
