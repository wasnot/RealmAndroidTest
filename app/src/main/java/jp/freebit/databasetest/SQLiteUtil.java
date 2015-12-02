package jp.freebit.databasetest;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import jp.freebit.databasetest.SQLite.TestDatabase;
import jp.freebit.databasetest.SQLite.UserItem;

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

    public static void add(Context con, List<UserItem> items) {
        TestDatabase db = new TestDatabase(con);
        db.putList(items);
        db.closeDatabase();
    }

    private static UserItem createItem(SessionItentifierGenerator sig) {
        UserItem item = new UserItem();
        item.setName(sig.nextSessionId());
        item.setAge((int) (Math.random() * 100));
        item.setEmail(sig.nextSessionId());
        return item;
    }

    public static int pull(Context con) {
        TestDatabase db = new TestDatabase(con);
        int count = db.getAllItemList().size();
        db.closeDatabase();
        return count;
    }

    public static void remove(Context con) {
        TestDatabase db = new TestDatabase(con);
        db.deleteAll();
        db.closeDatabase();
    }
}
