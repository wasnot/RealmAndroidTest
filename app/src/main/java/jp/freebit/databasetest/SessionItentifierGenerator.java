package jp.freebit.databasetest;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * Created by akihiroaida on 15/11/25.
 */
public class SessionItentifierGenerator {

    private SecureRandom random = new SecureRandom();

    public String nextSessionId() {
        return new BigInteger(130, random).toString(32);
    }
}
