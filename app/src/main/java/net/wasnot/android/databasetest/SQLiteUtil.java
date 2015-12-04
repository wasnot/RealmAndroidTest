package net.wasnot.android.databasetest;

import net.wasnot.android.databasetest.SQLite.TestDatabase;
import net.wasnot.android.databasetest.SQLite.UserItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by akihiroaida on 15/11/25.
 */
public class SQLiteUtil {

    public static List<UserItem> makeUsers(int count) {
        SessionItentifierGenerator sig = new SessionItentifierGenerator();
        List<UserItem> items = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            items.add(createItem(sig));
        }
        return items;
    }

    public static List<UserItem> makeUsers2(int count) {
        List<UserItem> items = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            UserItem item = new UserItem();
            item.setName("testa" + (int) (Math.random() * 1000));
            item.setAge((int) (Math.random() * 100));
            item.setEmail("mail" + (int) (Math.random() * 1000) + "@mail.com");
            items.add(item);
        }
        return items;
    }

    public static void addBulk(TestDatabase db, List<UserItem> items) {
        db.putList(items);
    }

    public static void add(TestDatabase db, List<UserItem> items) {
        for (UserItem item : items) {
            db.put(item);
        }
    }

    private static UserItem createItem(SessionItentifierGenerator sig) {
        UserItem item = new UserItem();
        item.setName(sig.nextSessionId());
        item.setAge((int) (Math.random() * 100));
        item.setEmail(sig.nextSessionId());
        return item;
    }

    public static int count(TestDatabase db) {
        int count = db.getAllItemList().size();
        return count;
    }

    public static void remove(TestDatabase db) {
        db.deleteAll();
    }

    public static void drop(TestDatabase db) {
        db.drop();
    }
}
