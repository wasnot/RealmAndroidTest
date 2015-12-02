package jp.freebit.databasetest.Realm;

import io.realm.RealmObject;

/**
 * Created by akihiroaida on 15/12/02.
 */
public class TestData extends RealmObject {

    private int id;

    private String name;

    private long timestamp;

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
