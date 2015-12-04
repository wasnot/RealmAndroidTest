package net.wasnot.android.databasetest;

import com.activeandroid.query.Select;

import net.wasnot.android.databasetest.ActiveAndroid.Item;

import java.util.List;

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
