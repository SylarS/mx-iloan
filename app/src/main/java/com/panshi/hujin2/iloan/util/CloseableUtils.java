package com.panshi.hujin2.iloan.util;

import java.io.Closeable;
import java.io.IOException;

/**
 * Created by Administrator on 2018/6/14 0014.
 */

public class CloseableUtils {

    public static void close(Closeable closeable)  {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
