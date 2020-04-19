package p021us.zoom.androidlib.util;

import java.util.Comparator;
import java.util.HashMap;

/* renamed from: us.zoom.androidlib.util.HashMapComparator */
public class HashMapComparator implements Comparator<HashMap<String, ?>> {
    private String mSortingKey;

    public HashMapComparator(String str) {
        this.mSortingKey = str;
    }

    public void setSortingKey(String str) {
        this.mSortingKey = str;
    }

    public int compare(HashMap<String, ?> hashMap, HashMap<String, ?> hashMap2) {
        Object obj = hashMap.get(this.mSortingKey);
        Object obj2 = hashMap2.get(this.mSortingKey);
        if (!isComparable(obj)) {
            return isComparable(obj2) ? 1 : 0;
        }
        if (!isComparable(obj2)) {
            return -1;
        }
        return ((Comparable) obj).compareTo(obj2);
    }

    private boolean isComparable(Object obj) {
        return obj != null && (obj instanceof Comparable);
    }
}
