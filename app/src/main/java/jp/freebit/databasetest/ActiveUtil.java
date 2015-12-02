package jp.freebit.databasetest;

import java.util.List;

import jp.freebit.databasetest.ActiveAndroid.Item;

import com.activeandroid.query.Select;

/**
 * Created by akihiroaida on 15/11/25.
 */
public class ActiveUtil {


    public void testActiveAndroid() {
        // Insert
        Item item = new Item();
        item.name = "bebe";
        item.age = 6;
        item.save();

        // Update
        item.name = "nagisa";
        item.save();

        // Delete
        item.delete();

        // Query
        Item item2 = new Select().from(Item.class).where("name = ?", "bebe").executeSingle();

        List<Item> items = new Select().from(Item.class).where("age > ?", 0).execute();

    }
}
