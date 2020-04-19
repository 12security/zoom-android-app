package com.dropbox.core.p005v2.teamlog;

import com.box.androidsdk.content.models.BoxEvent;
import com.dropbox.core.stone.StoneDeserializerLogger;
import com.dropbox.core.stone.StoneSerializers;
import com.dropbox.core.stone.StructSerializer;
import com.dropbox.core.util.LangUtil;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/* renamed from: com.dropbox.core.v2.teamlog.TeamEvent */
public class TeamEvent {
    protected final ActorLogInfo actor;
    protected final List<AssetLogInfo> assets;
    protected final ContextLogInfo context;
    protected final EventDetails details;
    protected final EventCategory eventCategory;
    protected final EventType eventType;
    protected final Boolean involveNonTeamMember;
    protected final OriginLogInfo origin;
    protected final List<ParticipantLogInfo> participants;
    protected final Date timestamp;

    /* renamed from: com.dropbox.core.v2.teamlog.TeamEvent$Builder */
    public static class Builder {
        protected ActorLogInfo actor;
        protected List<AssetLogInfo> assets;
        protected ContextLogInfo context;
        protected final EventDetails details;
        protected final EventCategory eventCategory;
        protected final EventType eventType;
        protected Boolean involveNonTeamMember;
        protected OriginLogInfo origin;
        protected List<ParticipantLogInfo> participants;
        protected final Date timestamp;

        protected Builder(Date date, EventCategory eventCategory2, EventType eventType2, EventDetails eventDetails) {
            if (date != null) {
                this.timestamp = LangUtil.truncateMillis(date);
                if (eventCategory2 != null) {
                    this.eventCategory = eventCategory2;
                    if (eventType2 != null) {
                        this.eventType = eventType2;
                        if (eventDetails != null) {
                            this.details = eventDetails;
                            this.actor = null;
                            this.origin = null;
                            this.involveNonTeamMember = null;
                            this.context = null;
                            this.participants = null;
                            this.assets = null;
                            return;
                        }
                        throw new IllegalArgumentException("Required value for 'details' is null");
                    }
                    throw new IllegalArgumentException("Required value for 'eventType' is null");
                }
                throw new IllegalArgumentException("Required value for 'eventCategory' is null");
            }
            throw new IllegalArgumentException("Required value for 'timestamp' is null");
        }

        public Builder withActor(ActorLogInfo actorLogInfo) {
            this.actor = actorLogInfo;
            return this;
        }

        public Builder withOrigin(OriginLogInfo originLogInfo) {
            this.origin = originLogInfo;
            return this;
        }

        public Builder withInvolveNonTeamMember(Boolean bool) {
            this.involveNonTeamMember = bool;
            return this;
        }

        public Builder withContext(ContextLogInfo contextLogInfo) {
            this.context = contextLogInfo;
            return this;
        }

        public Builder withParticipants(List<ParticipantLogInfo> list) {
            if (list != null) {
                for (ParticipantLogInfo participantLogInfo : list) {
                    if (participantLogInfo == null) {
                        throw new IllegalArgumentException("An item in list 'participants' is null");
                    }
                }
            }
            this.participants = list;
            return this;
        }

        public Builder withAssets(List<AssetLogInfo> list) {
            if (list != null) {
                for (AssetLogInfo assetLogInfo : list) {
                    if (assetLogInfo == null) {
                        throw new IllegalArgumentException("An item in list 'assets' is null");
                    }
                }
            }
            this.assets = list;
            return this;
        }

        public TeamEvent build() {
            TeamEvent teamEvent = new TeamEvent(this.timestamp, this.eventCategory, this.eventType, this.details, this.actor, this.origin, this.involveNonTeamMember, this.context, this.participants, this.assets);
            return teamEvent;
        }
    }

    /* renamed from: com.dropbox.core.v2.teamlog.TeamEvent$Serializer */
    static class Serializer extends StructSerializer<TeamEvent> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(TeamEvent teamEvent, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("timestamp");
            StoneSerializers.timestamp().serialize(teamEvent.timestamp, jsonGenerator);
            jsonGenerator.writeFieldName("event_category");
            Serializer.INSTANCE.serialize(teamEvent.eventCategory, jsonGenerator);
            jsonGenerator.writeFieldName(BoxEvent.FIELD_EVENT_TYPE);
            Serializer.INSTANCE.serialize(teamEvent.eventType, jsonGenerator);
            jsonGenerator.writeFieldName("details");
            Serializer.INSTANCE.serialize(teamEvent.details, jsonGenerator);
            if (teamEvent.actor != null) {
                jsonGenerator.writeFieldName("actor");
                StoneSerializers.nullable(Serializer.INSTANCE).serialize(teamEvent.actor, jsonGenerator);
            }
            if (teamEvent.origin != null) {
                jsonGenerator.writeFieldName("origin");
                StoneSerializers.nullableStruct(Serializer.INSTANCE).serialize(teamEvent.origin, jsonGenerator);
            }
            if (teamEvent.involveNonTeamMember != null) {
                jsonGenerator.writeFieldName("involve_non_team_member");
                StoneSerializers.nullable(StoneSerializers.boolean_()).serialize(teamEvent.involveNonTeamMember, jsonGenerator);
            }
            if (teamEvent.context != null) {
                jsonGenerator.writeFieldName("context");
                StoneSerializers.nullable(Serializer.INSTANCE).serialize(teamEvent.context, jsonGenerator);
            }
            if (teamEvent.participants != null) {
                jsonGenerator.writeFieldName("participants");
                StoneSerializers.nullable(StoneSerializers.list(Serializer.INSTANCE)).serialize(teamEvent.participants, jsonGenerator);
            }
            if (teamEvent.assets != null) {
                jsonGenerator.writeFieldName("assets");
                StoneSerializers.nullable(StoneSerializers.list(Serializer.INSTANCE)).serialize(teamEvent.assets, jsonGenerator);
            }
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public TeamEvent deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
            } else {
                str = null;
            }
            if (str == null) {
                Date date = null;
                EventCategory eventCategory = null;
                EventType eventType = null;
                EventDetails eventDetails = null;
                ActorLogInfo actorLogInfo = null;
                OriginLogInfo originLogInfo = null;
                Boolean bool = null;
                ContextLogInfo contextLogInfo = null;
                List list = null;
                List list2 = null;
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if ("timestamp".equals(currentName)) {
                        date = (Date) StoneSerializers.timestamp().deserialize(jsonParser);
                    } else if ("event_category".equals(currentName)) {
                        eventCategory = Serializer.INSTANCE.deserialize(jsonParser);
                    } else if (BoxEvent.FIELD_EVENT_TYPE.equals(currentName)) {
                        eventType = Serializer.INSTANCE.deserialize(jsonParser);
                    } else if ("details".equals(currentName)) {
                        eventDetails = Serializer.INSTANCE.deserialize(jsonParser);
                    } else if ("actor".equals(currentName)) {
                        actorLogInfo = (ActorLogInfo) StoneSerializers.nullable(Serializer.INSTANCE).deserialize(jsonParser);
                    } else if ("origin".equals(currentName)) {
                        originLogInfo = (OriginLogInfo) StoneSerializers.nullableStruct(Serializer.INSTANCE).deserialize(jsonParser);
                    } else if ("involve_non_team_member".equals(currentName)) {
                        bool = (Boolean) StoneSerializers.nullable(StoneSerializers.boolean_()).deserialize(jsonParser);
                    } else if ("context".equals(currentName)) {
                        contextLogInfo = (ContextLogInfo) StoneSerializers.nullable(Serializer.INSTANCE).deserialize(jsonParser);
                    } else if ("participants".equals(currentName)) {
                        list = (List) StoneSerializers.nullable(StoneSerializers.list(Serializer.INSTANCE)).deserialize(jsonParser);
                    } else if ("assets".equals(currentName)) {
                        list2 = (List) StoneSerializers.nullable(StoneSerializers.list(Serializer.INSTANCE)).deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (date == null) {
                    throw new JsonParseException(jsonParser, "Required field \"timestamp\" missing.");
                } else if (eventCategory == null) {
                    throw new JsonParseException(jsonParser, "Required field \"event_category\" missing.");
                } else if (eventType == null) {
                    throw new JsonParseException(jsonParser, "Required field \"event_type\" missing.");
                } else if (eventDetails != null) {
                    TeamEvent teamEvent = new TeamEvent(date, eventCategory, eventType, eventDetails, actorLogInfo, originLogInfo, bool, contextLogInfo, list, list2);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(teamEvent, teamEvent.toStringMultiline());
                    return teamEvent;
                } else {
                    throw new JsonParseException(jsonParser, "Required field \"details\" missing.");
                }
            } else {
                StringBuilder sb = new StringBuilder();
                sb.append("No subtype found that matches tag: \"");
                sb.append(str);
                sb.append("\"");
                throw new JsonParseException(jsonParser, sb.toString());
            }
        }
    }

    public TeamEvent(Date date, EventCategory eventCategory2, EventType eventType2, EventDetails eventDetails, ActorLogInfo actorLogInfo, OriginLogInfo originLogInfo, Boolean bool, ContextLogInfo contextLogInfo, List<ParticipantLogInfo> list, List<AssetLogInfo> list2) {
        if (date != null) {
            this.timestamp = LangUtil.truncateMillis(date);
            if (eventCategory2 != null) {
                this.eventCategory = eventCategory2;
                this.actor = actorLogInfo;
                this.origin = originLogInfo;
                this.involveNonTeamMember = bool;
                this.context = contextLogInfo;
                if (list != null) {
                    for (ParticipantLogInfo participantLogInfo : list) {
                        if (participantLogInfo == null) {
                            throw new IllegalArgumentException("An item in list 'participants' is null");
                        }
                    }
                }
                this.participants = list;
                if (list2 != null) {
                    for (AssetLogInfo assetLogInfo : list2) {
                        if (assetLogInfo == null) {
                            throw new IllegalArgumentException("An item in list 'assets' is null");
                        }
                    }
                }
                this.assets = list2;
                if (eventType2 != null) {
                    this.eventType = eventType2;
                    if (eventDetails != null) {
                        this.details = eventDetails;
                        return;
                    }
                    throw new IllegalArgumentException("Required value for 'details' is null");
                }
                throw new IllegalArgumentException("Required value for 'eventType' is null");
            }
            throw new IllegalArgumentException("Required value for 'eventCategory' is null");
        }
        throw new IllegalArgumentException("Required value for 'timestamp' is null");
    }

    public TeamEvent(Date date, EventCategory eventCategory2, EventType eventType2, EventDetails eventDetails) {
        this(date, eventCategory2, eventType2, eventDetails, null, null, null, null, null, null);
    }

    public Date getTimestamp() {
        return this.timestamp;
    }

    public EventCategory getEventCategory() {
        return this.eventCategory;
    }

    public EventType getEventType() {
        return this.eventType;
    }

    public EventDetails getDetails() {
        return this.details;
    }

    public ActorLogInfo getActor() {
        return this.actor;
    }

    public OriginLogInfo getOrigin() {
        return this.origin;
    }

    public Boolean getInvolveNonTeamMember() {
        return this.involveNonTeamMember;
    }

    public ContextLogInfo getContext() {
        return this.context;
    }

    public List<ParticipantLogInfo> getParticipants() {
        return this.participants;
    }

    public List<AssetLogInfo> getAssets() {
        return this.assets;
    }

    public static Builder newBuilder(Date date, EventCategory eventCategory2, EventType eventType2, EventDetails eventDetails) {
        return new Builder(date, eventCategory2, eventType2, eventDetails);
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.timestamp, this.eventCategory, this.actor, this.origin, this.involveNonTeamMember, this.context, this.participants, this.assets, this.eventType, this.details});
    }

    /* JADX WARNING: Code restructure failed: missing block: B:53:0x009a, code lost:
        if (r2.equals(r5) == false) goto L_0x009d;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean equals(java.lang.Object r5) {
        /*
            r4 = this;
            r0 = 1
            if (r5 != r4) goto L_0x0004
            return r0
        L_0x0004:
            r1 = 0
            if (r5 != 0) goto L_0x0008
            return r1
        L_0x0008:
            java.lang.Class r2 = r5.getClass()
            java.lang.Class r3 = r4.getClass()
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x009f
            com.dropbox.core.v2.teamlog.TeamEvent r5 = (com.dropbox.core.p005v2.teamlog.TeamEvent) r5
            java.util.Date r2 = r4.timestamp
            java.util.Date r3 = r5.timestamp
            if (r2 == r3) goto L_0x0024
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x009d
        L_0x0024:
            com.dropbox.core.v2.teamlog.EventCategory r2 = r4.eventCategory
            com.dropbox.core.v2.teamlog.EventCategory r3 = r5.eventCategory
            if (r2 == r3) goto L_0x0030
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x009d
        L_0x0030:
            com.dropbox.core.v2.teamlog.EventType r2 = r4.eventType
            com.dropbox.core.v2.teamlog.EventType r3 = r5.eventType
            if (r2 == r3) goto L_0x003c
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x009d
        L_0x003c:
            com.dropbox.core.v2.teamlog.EventDetails r2 = r4.details
            com.dropbox.core.v2.teamlog.EventDetails r3 = r5.details
            if (r2 == r3) goto L_0x0048
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x009d
        L_0x0048:
            com.dropbox.core.v2.teamlog.ActorLogInfo r2 = r4.actor
            com.dropbox.core.v2.teamlog.ActorLogInfo r3 = r5.actor
            if (r2 == r3) goto L_0x0056
            if (r2 == 0) goto L_0x009d
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x009d
        L_0x0056:
            com.dropbox.core.v2.teamlog.OriginLogInfo r2 = r4.origin
            com.dropbox.core.v2.teamlog.OriginLogInfo r3 = r5.origin
            if (r2 == r3) goto L_0x0064
            if (r2 == 0) goto L_0x009d
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x009d
        L_0x0064:
            java.lang.Boolean r2 = r4.involveNonTeamMember
            java.lang.Boolean r3 = r5.involveNonTeamMember
            if (r2 == r3) goto L_0x0072
            if (r2 == 0) goto L_0x009d
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x009d
        L_0x0072:
            com.dropbox.core.v2.teamlog.ContextLogInfo r2 = r4.context
            com.dropbox.core.v2.teamlog.ContextLogInfo r3 = r5.context
            if (r2 == r3) goto L_0x0080
            if (r2 == 0) goto L_0x009d
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x009d
        L_0x0080:
            java.util.List<com.dropbox.core.v2.teamlog.ParticipantLogInfo> r2 = r4.participants
            java.util.List<com.dropbox.core.v2.teamlog.ParticipantLogInfo> r3 = r5.participants
            if (r2 == r3) goto L_0x008e
            if (r2 == 0) goto L_0x009d
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x009d
        L_0x008e:
            java.util.List<com.dropbox.core.v2.teamlog.AssetLogInfo> r2 = r4.assets
            java.util.List<com.dropbox.core.v2.teamlog.AssetLogInfo> r5 = r5.assets
            if (r2 == r5) goto L_0x009e
            if (r2 == 0) goto L_0x009d
            boolean r5 = r2.equals(r5)
            if (r5 == 0) goto L_0x009d
            goto L_0x009e
        L_0x009d:
            r0 = 0
        L_0x009e:
            return r0
        L_0x009f:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.teamlog.TeamEvent.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
