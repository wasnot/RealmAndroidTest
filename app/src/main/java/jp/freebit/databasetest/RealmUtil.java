package jp.freebit.databasetest;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import jp.freebit.databasetest.Realm.TestData;
import jp.freebit.databasetest.Realm.User;

/**
 * Created by akihiroaida on 15/11/25.
 */
public class RealmUtil {


    public void testRealm() {

        // Write
        // Obtain a Realm instance
        Realm realm = Realm.getDefaultInstance();

        realm.beginTransaction();

        // Create a new object
        User user = realm.createObject(User.class);
        user.setName("Wasabeef");
        user.setEmail("chip@wasabeef.jp");

        realm.commitTransaction();

        // Query
        // Build the query looking at all users:
        RealmQuery<User> query = realm.where(User.class);

        // Add query conditions:
        query.equalTo("name", "Wasabeef");
        query.or().equalTo("name", "Chip");
        // Execute the query:
        RealmResults<User> resultAll = query.findAll();

        // Or alternatively do the same all at once (the "Fluent interface"):
        RealmResults<User> result = realm.where(User.class).equalTo("name", "Wasabeef").or()
                .equalTo("name", "Chip").findAll();

        // Query
        RealmResults<User> result2 = realm.where(User.class).findAll();

        // Asc
//        RealmResults<User> sortedAscending =
        result2.sort("age");

        // Desc
//        RealmResults<User> sortedDescending =
        result2.sort("age", RealmResults.SORT_ORDER_DESCENDING);

        // All changes to data must happen in a transaction
        realm.beginTransaction();

// remove single match
        result.remove(0);
        result.removeLast();

// Delete all matches
        result.clear();

        realm.commitTransaction();


    }

    public static List<User> makeUsers(int count) {
        SessionItentifierGenerator sig = new SessionItentifierGenerator();
        List<User> items = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            items.add(createUser(sig));
        }
        return items;
    }

    public static void add(Realm realm, List<User> items) {
        realm.beginTransaction();
        realm.copyToRealm(items);
        realm.commitTransaction();
    }

    private static User createUser(SessionItentifierGenerator sig) {
        User item = new User();
        item.setName(sig.nextSessionId());
        item.setAge((int) (Math.random() * 100));
        item.setEmail(sig.nextSessionId());
        return item;
    }

    public static int pull(Realm realm) {
        RealmResults<User> results = realm.where(User.class).findAll();
        List<User> users = new ArrayList<>();
        for (User user : results) {
            users.add(clone(user));
        }
        return users.size();
    }

    private static User clone(User user) {
        User newUser = new User();
        newUser.setName(user.getName());
        newUser.setAge(user.getAge());
        newUser.setEmail(user.getEmail());
        return newUser;
    }

    public static void remove(Realm realm) {
        realm.beginTransaction();
        realm.clear(User.class);
        realm.commitTransaction();
    }

    public static void putTestData(Realm realm, TestData data) {
        realm.beginTransaction();
        realm.copyToRealm(data);
        realm.commitTransaction();
    }
}
