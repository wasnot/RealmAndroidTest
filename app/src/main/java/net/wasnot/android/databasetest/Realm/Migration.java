
package net.wasnot.android.databasetest.Realm;

import io.realm.Realm;
import io.realm.RealmMigration;

/**
 * Created by akihiroaida on 15/03/12.
 */
public class Migration implements RealmMigration {

    @Override
    public long execute(Realm realm, long version) {
        // Migrate from version 0 to version 1
        if (version == 0) {
            // 自分で、Tableの再構築を行う必要がある
            // 省略
            version++;
        }
        return version;
    }
}
