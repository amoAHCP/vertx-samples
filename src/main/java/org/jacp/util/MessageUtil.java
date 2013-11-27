package org.jacp.util;

import java.io.IOException;

import static org.jacp.util.Serializer.*;

/**
 * Created with IntelliJ IDEA.
 * User: amo
 * Date: 27.11.13
 * Time: 10:15
 * To change this template use File | Settings | File Templates.
 */
public class MessageUtil {

    public static <T> T getMessage(byte[] bytes, Class<T> clazz) throws IOException, ClassNotFoundException {
                 return clazz.cast(deserialize(bytes));
    }
}
