package com.box.androidsdk.content.models;

import com.box.androidsdk.content.utils.IStreamPosition;
import com.eclipsesource.json.JsonObject.Member;
import com.eclipsesource.json.JsonValue;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

public class BoxListEvents extends BoxList<BoxEvent> implements IStreamPosition {
    public static final String FIELD_CHUNK_SIZE = "chunk_size";
    public static final String FIELD_NEXT_STREAM_POSITION = "next_stream_position";
    private static final long serialVersionUID = 2397451459829964208L;
    private final HashSet<String> mEventIds = new HashSet<>();
    private boolean mFilterDuplicates = true;

    /* access modifiers changed from: protected */
    public void parseJSONMember(Member member) {
        String name = member.getName();
        JsonValue value = member.getValue();
        if (name.equals("next_stream_position")) {
            this.mProperties.put("next_stream_position", Long.valueOf(value.asLong()));
        } else if (name.equals("chunk_size")) {
            this.mProperties.put("chunk_size", Long.valueOf(value.asLong()));
        } else if (name.equals(BoxList.FIELD_ENTRIES)) {
            Iterator it = value.asArray().iterator();
            while (it.hasNext()) {
                JsonValue jsonValue = (JsonValue) it.next();
                BoxEvent boxEvent = new BoxEvent();
                boxEvent.createFromJson(jsonValue.asObject());
                add(boxEvent);
            }
            this.mProperties.put(BoxList.FIELD_ENTRIES, this.collection);
        } else {
            super.parseJSONMember(member);
        }
    }

    public boolean add(BoxEvent boxEvent) {
        if (this.mFilterDuplicates && this.mEventIds.contains(boxEvent.getEventId())) {
            return false;
        }
        this.mEventIds.add(boxEvent.getEventId());
        return super.add(boxEvent);
    }

    public boolean remove(Object obj) {
        if (obj instanceof BoxEvent) {
            this.mEventIds.remove(((BoxEvent) obj).getEventId());
        }
        return super.remove(obj);
    }

    public boolean removeAll(Collection<?> collection) {
        for (Object next : collection) {
            if (next instanceof BoxEvent) {
                this.mEventIds.remove(((BoxEvent) next).getEventId());
            }
        }
        return super.removeAll(collection);
    }

    public boolean addAll(Collection<? extends BoxEvent> collection) {
        boolean z = true;
        for (BoxEvent add : collection) {
            z &= add(add);
        }
        return z;
    }

    public void clear() {
        this.mEventIds.clear();
        super.clear();
    }

    public Long getChunkSize() {
        return (Long) this.mProperties.get("chunk_size");
    }

    public Long getNextStreamPosition() {
        return (Long) this.mProperties.get("next_stream_position");
    }

    public void setFilterDuplicates(boolean z) {
        this.mFilterDuplicates = z;
    }
}
