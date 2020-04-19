package com.box.androidsdk.content.models;

import com.box.androidsdk.content.models.BoxJsonObject;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonValue;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class BoxArray<E extends BoxJsonObject> implements Collection<E> {
    protected final Collection<E> collection = new ArrayList();

    public String toJson() {
        JsonArray jsonArray = new JsonArray();
        for (int i = 0; i < size(); i++) {
            jsonArray.add((JsonValue) get(i).toJsonObject());
        }
        return jsonArray.toString();
    }

    public boolean add(E e) {
        return this.collection.add(e);
    }

    public boolean addAll(Collection<? extends E> collection2) {
        return this.collection.addAll(collection2);
    }

    public void clear() {
        this.collection.clear();
    }

    public boolean contains(Object obj) {
        return this.collection.contains(obj);
    }

    public boolean containsAll(Collection<?> collection2) {
        return this.collection.containsAll(collection2);
    }

    public boolean equals(Object obj) {
        return this.collection.equals(obj);
    }

    public int hashCode() {
        return this.collection.hashCode();
    }

    public boolean isEmpty() {
        return this.collection.isEmpty();
    }

    public Iterator<E> iterator() {
        return this.collection.iterator();
    }

    public boolean remove(Object obj) {
        return this.collection.remove(obj);
    }

    public boolean removeAll(Collection<?> collection2) {
        if (collection2 == null || collection2.isEmpty()) {
            return false;
        }
        return this.collection.removeAll(collection2);
    }

    public boolean retainAll(Collection<?> collection2) {
        if (collection2 == null || collection2.isEmpty()) {
            return false;
        }
        return this.collection.retainAll(collection2);
    }

    public int size() {
        return this.collection.size();
    }

    public Object[] toArray() {
        return this.collection.toArray();
    }

    public <T> T[] toArray(T[] tArr) {
        return this.collection.toArray(tArr);
    }

    public E get(int i) {
        Collection<E> collection2 = this.collection;
        if (collection2 instanceof List) {
            return (BoxJsonObject) ((List) collection2).get(i);
        }
        if (i >= 0) {
            Iterator it = iterator();
            while (it.hasNext()) {
                if (i == 0) {
                    return (BoxJsonObject) it.next();
                }
                it.next();
            }
            throw new IndexOutOfBoundsException();
        }
        throw new IndexOutOfBoundsException();
    }
}
