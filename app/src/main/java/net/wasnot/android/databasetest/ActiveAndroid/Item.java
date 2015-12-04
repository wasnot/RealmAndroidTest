
package net.wasnot.android.databasetest.ActiveAndroid;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by akihiroaida on 15/03/12.
 */
@Table(name = "Items")
public class Item extends Model {

    @Column(name = "Name")
    public String name;

    @Column(name = "Age")
    public int age;

    public Item() {
        super();
    }
}
