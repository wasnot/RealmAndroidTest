package net.wasnot.android.databasetest.bus;

import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

/**
 * Created by akihiroaida on 15/12/02.
 */
public class BusHolder {

    private static Bus sBus = new Bus(ThreadEnforcer.ANY);

    public static Bus get() {
        return sBus;
    }
}
