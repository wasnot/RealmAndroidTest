package net.wasnot.android.databasetest.SQLite;

import android.provider.BaseColumns;

/**
 * Created by akihiroaida on 15/11/25.
 */
public class UserColumns implements BaseColumns {

    public final static String NAME = "name";

    public final static String AGE = "age";

    public final static String EMAIL = "email";

    public final static String[] all_fields = {
            _ID, NAME, AGE, EMAIL
    };
}
