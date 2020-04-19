package com.google.api.client.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public final class Lists {
    public static <E> ArrayList<E> newArrayList() {
        return new ArrayList<>();
    }

    public static <E> ArrayList<E> newArrayListWithCapacity(int i) {
        return new ArrayList<>(i);
    }

    public static <E> ArrayList<E> newArrayList(Iterable<? extends E> iterable) {
        return iterable instanceof Collection ? new ArrayList<>(Collections2.cast(iterable)) : newArrayList(iterable.iterator());
    }

    public static <E> ArrayList<E> newArrayList(Iterator<? extends E> it) {
        ArrayList<E> newArrayList = newArrayList();
        while (it.hasNext()) {
            newArrayList.add(it.next());
        }
        return newArrayList;
    }

    private Lists() {
    }
}
