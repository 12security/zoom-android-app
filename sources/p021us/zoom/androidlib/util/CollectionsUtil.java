package p021us.zoom.androidlib.util;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/* renamed from: us.zoom.androidlib.util.CollectionsUtil */
public class CollectionsUtil {

    /* renamed from: us.zoom.androidlib.util.CollectionsUtil$Filter */
    public interface Filter<T> {
        boolean apply(T t);
    }

    public static <T> boolean isListEmpty(@Nullable List<T> list) {
        return list == null || list.isEmpty();
    }

    public static <T> boolean isCollectionEmpty(Collection<T> collection) {
        return collection == null || collection.isEmpty();
    }

    @NonNull
    public static <T> List<T> filter(List<T> list, @NonNull Filter<T> filter) {
        ArrayList arrayList = new ArrayList(5);
        if (isListEmpty(list)) {
            return arrayList;
        }
        for (Object next : list) {
            if (filter.apply(next)) {
                arrayList.add(next);
            }
        }
        return arrayList;
    }
}
