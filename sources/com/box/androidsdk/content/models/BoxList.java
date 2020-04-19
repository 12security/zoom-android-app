package com.box.androidsdk.content.models;

import com.box.androidsdk.content.models.BoxJsonObject;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject.Member;
import com.eclipsesource.json.JsonValue;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class BoxList<E extends BoxJsonObject> extends BoxJsonObject implements Collection<E> {
    public static final String FIELD_ENTRIES = "entries";
    public static final String FIELD_LIMIT = "limit";
    public static final String FIELD_OFFSET = "offset";
    public static final String FIELD_ORDER = "order";
    public static final String FIELD_TOTAL_COUNT = "total_count";
    private static final long serialVersionUID = 8036181424029520417L;
    protected final Collection<E> collection = new ArrayList<E>() {
        public boolean add(E e) {
            BoxList.this.addCollectionToProperties();
            return super.add(e);
        }

        public void add(int i, E e) {
            BoxList.this.addCollectionToProperties();
            super.add(i, e);
        }

        public boolean addAll(Collection<? extends E> collection) {
            BoxList.this.addCollectionToProperties();
            return super.addAll(collection);
        }

        public boolean addAll(int i, Collection<? extends E> collection) {
            BoxList.this.addCollectionToProperties();
            return super.addAll(i, collection);
        }
    };
    protected transient boolean collectionInProperties = false;

    /* access modifiers changed from: protected */
    public void addCollectionToProperties() {
        if (!this.collectionInProperties) {
            this.mProperties.put(FIELD_ENTRIES, this.collection);
            this.collectionInProperties = true;
        }
    }

    public BoxList() {
    }

    public BoxList(Map<String, Object> map) {
        super(map);
    }

    public Long offset() {
        return (Long) this.mProperties.get("offset");
    }

    public Long limit() {
        return (Long) this.mProperties.get(FIELD_LIMIT);
    }

    public Long fullSize() {
        return (Long) this.mProperties.get(FIELD_TOTAL_COUNT);
    }

    /* access modifiers changed from: protected */
    public void parseJSONMember(Member member) {
        String name = member.getName();
        JsonValue value = member.getValue();
        if (name.equals(FIELD_ORDER)) {
            this.mProperties.put(FIELD_ORDER, parseOrder(value));
        } else if (name.equals(FIELD_TOTAL_COUNT)) {
            this.mProperties.put(FIELD_TOTAL_COUNT, Long.valueOf(value.asLong()));
        } else if (name.equals("offset")) {
            this.mProperties.put("offset", Long.valueOf(value.asLong()));
        } else if (name.equals(FIELD_LIMIT)) {
            this.mProperties.put(FIELD_LIMIT, Long.valueOf(value.asLong()));
        } else if (name.equals(FIELD_ENTRIES)) {
            addCollectionToProperties();
            Iterator it = value.asArray().iterator();
            while (it.hasNext()) {
                this.collection.add(BoxEntity.createEntityFromJson(((JsonValue) it.next()).asObject()));
            }
        } else {
            super.parseJSONMember(member);
        }
    }

    private ArrayList<BoxOrder> parseOrder(JsonValue jsonValue) {
        JsonArray asArray = jsonValue.asArray();
        ArrayList<BoxOrder> arrayList = new ArrayList<>(asArray.size());
        Iterator it = asArray.iterator();
        while (it.hasNext()) {
            JsonValue jsonValue2 = (JsonValue) it.next();
            BoxOrder boxOrder = new BoxOrder();
            boxOrder.createFromJson(jsonValue2.asObject());
            arrayList.add(boxOrder);
        }
        return arrayList;
    }

    /* access modifiers changed from: protected */
    public JsonValue parseJsonObject(Entry<String, Object> entry) {
        if (!((String) entry.getKey()).equals(FIELD_ENTRIES)) {
            return super.parseJsonObject(entry);
        }
        JsonArray jsonArray = new JsonArray();
        for (BoxJsonObject jsonObject : (Collection) entry.getValue()) {
            jsonArray.add((JsonValue) jsonObject.toJsonObject());
        }
        return jsonArray;
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

    public ArrayList<BoxOrder> getSortOrders() {
        return (ArrayList) this.mProperties.get(FIELD_ORDER);
    }
}
