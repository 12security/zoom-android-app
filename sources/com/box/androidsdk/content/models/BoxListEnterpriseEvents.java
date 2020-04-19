package com.box.androidsdk.content.models;

import com.box.androidsdk.content.utils.IStreamPosition;
import com.eclipsesource.json.JsonObject.Member;
import com.eclipsesource.json.JsonValue;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

public class BoxListEnterpriseEvents extends BoxList<BoxEnterpriseEvent> implements IStreamPosition {
    public static final String FIELD_CHUNK_SIZE = "chunk_size";
    public static final String FIELD_NEXT_STREAM_POSITION = "next_stream_position";
    private static final long serialVersionUID = 940295540206254689L;
    private final HashSet<String> mEventIds = new HashSet<>();
    private boolean mFilterDuplicates = true;

    /* access modifiers changed from: protected */
    public void parseJSONMember(Member member) {
        String name = member.getName();
        JsonValue value = member.getValue();
        if (name.equals("next_stream_position")) {
            this.mProperties.put("next_stream_position", value.asString());
        } else if (name.equals("chunk_size")) {
            this.mProperties.put("chunk_size", Long.valueOf(value.asLong()));
        } else if (name.equals(BoxList.FIELD_ENTRIES)) {
            Iterator it = value.asArray().iterator();
            while (it.hasNext()) {
                JsonValue jsonValue = (JsonValue) it.next();
                BoxEnterpriseEvent boxEnterpriseEvent = new BoxEnterpriseEvent();
                boxEnterpriseEvent.createFromJson(jsonValue.asObject());
                add(boxEnterpriseEvent);
            }
            this.mProperties.put(BoxList.FIELD_ENTRIES, this.collection);
        } else {
            super.parseJSONMember(member);
        }
    }

    public boolean add(BoxEnterpriseEvent boxEnterpriseEvent) {
        if (this.mFilterDuplicates && this.mEventIds.contains(boxEnterpriseEvent.getEventId())) {
            return false;
        }
        this.mEventIds.add(boxEnterpriseEvent.getEventId());
        return super.add(boxEnterpriseEvent);
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

    public boolean addAll(Collection<? extends BoxEnterpriseEvent> collection) {
        boolean z = true;
        for (BoxEnterpriseEvent add : collection) {
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
        return Long.valueOf(Long.parseLong(((String) this.mProperties.get("next_stream_position")).replace("\"", "")));
    }

    public void setFilterDuplicates(boolean z) {
        this.mFilterDuplicates = z;
    }
}
