
package jp.freebit.databasetest;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import jp.freebit.databasetest.Realm.Migration;

/**
 * Created by akihiroaida on 15/03/12.
 */
//public class MyApplication extends com.activeandroid.app.Application {
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        //        RealmConfiguration config0 = new RealmConfiguration.Builder(this)
        //                .name("default0")
        //                .schemaVersion(3)
        //                .build();
        RealmConfiguration config = new RealmConfiguration.Builder(this).schemaVersion(1)
                .migration(new Migration()).build();
        Realm.setDefaultConfiguration(config);
    }
}
