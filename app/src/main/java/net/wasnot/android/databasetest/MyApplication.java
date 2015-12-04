
package net.wasnot.android.databasetest;

import net.wasnot.android.databasetest.Realm.Migration;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import jp.wasabeef.takt.Takt;

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

        Takt.stock(this).play();
    }
}
