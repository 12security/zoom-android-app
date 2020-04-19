package com.dropbox.core.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class LangUtil {
    public static RuntimeException mkAssert(String str, Throwable th) {
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        sb.append(": ");
        sb.append(th.getMessage());
        RuntimeException runtimeException = new RuntimeException(sb.toString());
        runtimeException.initCause(th);
        return runtimeException;
    }

    public static AssertionError badType(Object obj) {
        String str;
        if (obj == null) {
            str = "bad type: null";
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("bad type: ");
            sb.append(obj.getClass().getName());
            str = sb.toString();
        }
        return new AssertionError(str);
    }

    public static <T> T[] arrayConcat(T[] tArr, T[] tArr2) {
        if (tArr == null) {
            throw new IllegalArgumentException("'a' can't be null");
        } else if (tArr2 != null) {
            T[] copyOf = Arrays.copyOf(tArr, tArr.length + tArr2.length);
            System.arraycopy(tArr2, 0, copyOf, tArr.length, tArr2.length);
            return copyOf;
        } else {
            throw new IllegalArgumentException("'b' can't be null");
        }
    }

    public static <T> boolean nullableEquals(T t, T t2) {
        boolean z = false;
        if (t == null) {
            if (t2 == null) {
                z = true;
            }
            return z;
        } else if (t2 == null) {
            return false;
        } else {
            return t.equals(t2);
        }
    }

    public static int nullableHashCode(Object obj) {
        if (obj == null) {
            return 0;
        }
        return obj.hashCode() + 1;
    }

    public static Date truncateMillis(Date date) {
        if (date == null) {
            return date;
        }
        long time = date.getTime();
        return new Date(time - (time % 1000));
    }

    public static List<Date> truncateMillis(List<Date> list) {
        if (list == null) {
            return list;
        }
        ArrayList arrayList = new ArrayList(list.size());
        for (Date time : list) {
            long time2 = time.getTime();
            arrayList.add(new Date(time2 - (time2 % 1000)));
        }
        return arrayList;
    }
}
