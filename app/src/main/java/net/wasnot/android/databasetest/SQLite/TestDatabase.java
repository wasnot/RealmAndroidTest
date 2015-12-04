package net.wasnot.android.databasetest.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by akihiroaida on 15/11/25.
 */
public class TestDatabase {

    private final static String TAG = TestDatabase.class.getSimpleName();

    protected final static String TABLE = "users";

    private final static String DB_FILENAME = "testdpb";

    private final static boolean DUMPING = false;

    private Context mContext;

    private TestSQLiteOpenHelper mHelper;

    private SQLiteDatabase mReadDb;

    private SQLiteDatabase mWriteDb;

    public TestDatabase(Context context) {
        this.mContext = context;
        this.mHelper = new TestSQLiteOpenHelper(mContext, DB_FILENAME);
        this.mReadDb = mHelper.getReadableDatabase();
        this.mWriteDb = mHelper.getWritableDatabase();
    }

    public void closeDatabase() {
        this.mReadDb.close();
        this.mWriteDb.close();
        this.mHelper.close();
    }

    public boolean putList(List<UserItem> users) {
        boolean isSuccess = false;
        List<Long> results = new ArrayList<Long>();
        Log.e(TAG, "putList:" + users.size());
        long start = System.currentTimeMillis();
        try {
            mWriteDb.beginTransaction();
            try {
                for (UserItem item : users) {
                    results.add(put(item));
                }
                mWriteDb.setTransactionSuccessful();
                isSuccess = true;
            } catch (Exception e) {
            } finally {
                mWriteDb.endTransaction();
            }
            Log.e(TAG, "putList ended!:" + (System.currentTimeMillis() - start));
        } catch (Exception e) {
            // } catch (SQLiteDatabaseLockedException e) {
            e.printStackTrace();
            Log.e(TAG, "Database is Locked!");
        }
        return isSuccess;
    }

    public long put(UserItem user) {
        ContentValues values = new ContentValues();
        values.put(UserColumns.NAME, user.getName());
        values.put(UserColumns.AGE, user.getAge());
        values.put(UserColumns.EMAIL, user.getEmail());
        long re = putContentValue(values);
        return re;
    }

    private long putContentValue(ContentValues values) {
        return mWriteDb.replace(TABLE, null, values);
    }

    private boolean updateContentValueList(List<ContentValues> valuesList) {
        boolean isSuccess = false;
        List<Long> results = new ArrayList<Long>();
        Log.e(TAG, "putList:" + valuesList.size());
        long start = System.currentTimeMillis();
        try {
            mWriteDb.beginTransaction();
            try {
                for (ContentValues values : valuesList) {
                    // results.add(put(updateContentValue(values)));
                }
                mWriteDb.setTransactionSuccessful();
                isSuccess = true;
            } catch (Exception e) {
            } finally {
                mWriteDb.endTransaction();
            }
            Log.e(TAG, "putList ended!:" + (System.currentTimeMillis() - start));
        } catch (Exception e) {
            // } catch (SQLiteDatabaseLockedException e) {
            e.printStackTrace();
            Log.e(TAG, "Database is Locked!");
        }
        return isSuccess;
    }

    private int updateField(String targetField, String targetValue, String fieldName,
            String fieldValue) {
        if (!isExistField(fieldName) || !isExistField(targetField)) {
            return -1;
        }
        ContentValues values = new ContentValues();
        values.put(targetField, targetValue);
        return updateContentValue(values, fieldName, fieldValue);
    }

    protected int updateContentValue(ContentValues values, String fieldName, String fieldValue) {
        return mWriteDb.update(TABLE, values, fieldName + " = ?", new String[]{
                fieldValue
        });
    }

    protected String getStringByField(String targetField, String fieldName, String fieldValue) {
        if (!isExistField(fieldName) || !isExistField(targetField)) {
            return null;
        }
        String result;
        Cursor c = mReadDb.query(TABLE, null, fieldName + " = ?", new String[]{
                fieldValue
        }, null, null, null);
        c.moveToFirst();
        if (DUMPING) {
            Log.d(TAG, "id:" + fieldValue + ", count:" + c.getCount());
        }
        if (c.getCount() > 0) {
            result = c.getString(c.getColumnIndex(targetField));
        } else {
            result = null;
        }
        log(c.getCount(), result);
        c.close();
        return result;
    }

    protected int getIntByField(String targetField, String fieldName, String fieldValue) {
        if (!isExistField(fieldName) || !isExistField(targetField)) {
            return 0;
        }
        int result;
        Cursor c = mReadDb.query(TABLE, null, fieldName + " = ?", new String[]{
                fieldValue
        }, null, null, null);
        c.moveToFirst();
        if (DUMPING) {
            Log.d(TAG, "count:" + c.getCount());
        }
        if (c.getCount() > 0) {
            result = c.getInt(c.getColumnIndexOrThrow(targetField));
        } else {
            result = 0;
        }
        log(c.getCount(), result + "");
        c.close();
        return result;
    }

    public List<UserItem> getAllItemList() {
        List<UserItem> list = new ArrayList<UserItem>();
        Cursor c = mReadDb.query(TABLE, null, null, null, null, null,
                null);
        boolean isEof = c.moveToFirst();
        while (isEof) {
            list.add(setUserItem(c));
            isEof = c.moveToNext();
        }
        c.close();
        return list;
    }

    public Cursor getCursor(String[] projection, String selection, String[] selectionArgs,
            String sortOrder) {
        return mReadDb.query(TABLE, projection, selection, selectionArgs, null, null, sortOrder);
    }

    public Cursor getCursor(String[] projection, String selection, String[] selectionArgs,
            String sortOrder, String limit) {
        return mReadDb.query(TABLE, projection, selection, selectionArgs, null, null, sortOrder, limit);
    }

    public Cursor getCursor(String sql, String[] selectionArgs) {
        return mReadDb.rawQuery(sql, selectionArgs);
    }

    public boolean isExistByField(String fieldName, String fieldValue) {
        boolean result = false;
        if (!isExistField(fieldName)) {
            return false;
        }
        Cursor c = mReadDb.query(TABLE, null, fieldName + " = ?", new String[]{
                fieldValue
        }, null, null, null);
        result = (c.getCount() > 0);
        if (DUMPING) {
            log(c.getCount(), result + " by " + fieldValue);
        }
        c.close();
        return result;
    }

    /**
     * すべて削除する
     */
    public boolean deleteAll() {
        mWriteDb.delete(TABLE, UserColumns._ID + " like '%'", null);
        return true;
    }

    /**
     * 最後を削除する
     */
    public boolean deleteLast() {
        mWriteDb.execSQL(
                "DELETE FROM " + TABLE + " WHERE " + UserColumns._ID + " = (SELECT MAX(" + UserColumns._ID + ") FROM " + TABLE
                        + ")");
        return true;
    }

    /**
     * すべて削除する
     */
    public boolean drop() {
        mWriteDb.execSQL("DROP " + TABLE);
        return true;
    }

    /**
     * 指定された行を削除する
     */
    private boolean deleteByField(String fieldName, String fieldValue) {
        if (!isExistField(fieldName)) {
            return false;
        }
        int i = -1;
        i = mWriteDb.delete(TABLE, fieldName + " = ?", new String[]{
                fieldValue
        });
        Log.d(TAG, "delete:" + i);
        // mWriteDb.close();
        return true;
    }

    private UserItem setUserItem(Cursor c) {
        UserItem item = null;
        try {
            item = new UserItem();
            item.setName(c.getString(c.getColumnIndexOrThrow(UserColumns.NAME)));
            item.setAge(c.getInt(c.getColumnIndexOrThrow(UserColumns.AGE)));
            item.setEmail(c.getString(c.getColumnIndexOrThrow(UserColumns.EMAIL)));
            // Log.d(TAG, bundle.toString());
        } catch (IllegalArgumentException e) {
            Log.e(TAG, e.toString());
        }
        return item;
    }

    private boolean isExistField(String fieldName) {
        boolean isExistField = false;
        for (int i = 0; i < UserColumns.all_fields.length; i++) {
            if (fieldName.equals(UserColumns.all_fields[i])) {
                isExistField = true;
            }
        }
        return isExistField;
    }

    private void log(int count, String result) {
        // Log.d(TAG,"count:"+count + " result:"+result);
    }

    private static class TestSQLiteOpenHelper extends SQLiteOpenHelper {

        // static final String DB = "sqlite_sample.db";//filename
        // static final String CREATE_TABLE =
        // "create table mytable ( _id integer primary key autoincrement, data integer not null );";
        // static final String DROP_TABLE = "drop table mytable;";// drop db if
        // different db ver called.
        final static private int DB_VERSION = 1;

        // private Context mContext;

        public TestSQLiteOpenHelper(Context context, String filename) {
            super(context, filename, null, DB_VERSION);
            // mContext = context;
            // super(context, DB, null, DB_VERSION); // context, filename,cursor
            // factory, db ver
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            // table create
            // db.execSQL(CREATE_TABLE);
            // db.execSQL("create table person_table(" +
            // "   name text not null,"
            // + "   age text" + ");");
            db.execSQL("create table " + TABLE + "(" +
                    UserColumns._ID + " integer primary key autoincrement, "
                    + UserColumns.NAME + " text not null,"
                    + UserColumns.AGE + " integer, "
                    + UserColumns.EMAIL + " integer "
                    + ");");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // データベースの変更が生じた場合は、ここに処理を記述する。
            // db.execSQL(DROP_TABLE);
        }
    }
}
