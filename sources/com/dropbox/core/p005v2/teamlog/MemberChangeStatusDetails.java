package com.dropbox.core.p005v2.teamlog;

import com.dropbox.core.stone.StoneDeserializerLogger;
import com.dropbox.core.stone.StoneSerializers;
import com.dropbox.core.stone.StructSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.zipow.videobox.view.p014mm.MMContentFileViewerFragment;
import java.io.IOException;
import java.util.Arrays;

/* renamed from: com.dropbox.core.v2.teamlog.MemberChangeStatusDetails */
public class MemberChangeStatusDetails {
    protected final ActionDetails action;
    protected final MemberStatus newValue;
    protected final MemberStatus previousValue;

    /* renamed from: com.dropbox.core.v2.teamlog.MemberChangeStatusDetails$Builder */
    public static class Builder {
        protected ActionDetails action;
        protected final MemberStatus newValue;
        protected MemberStatus previousValue;

        protected Builder(MemberStatus memberStatus) {
            if (memberStatus != null) {
                this.newValue = memberStatus;
                this.previousValue = null;
                this.action = null;
                return;
            }
            throw new IllegalArgumentException("Required value for 'newValue' is null");
        }

        public Builder withPreviousValue(MemberStatus memberStatus) {
            this.previousValue = memberStatus;
            return this;
        }

        public Builder withAction(ActionDetails actionDetails) {
            this.action = actionDetails;
            return this;
        }

        public MemberChangeStatusDetails build() {
            return new MemberChangeStatusDetails(this.newValue, this.previousValue, this.action);
        }
    }

    /* renamed from: com.dropbox.core.v2.teamlog.MemberChangeStatusDetails$Serializer */
    static class Serializer extends StructSerializer<MemberChangeStatusDetails> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(MemberChangeStatusDetails memberChangeStatusDetails, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("new_value");
            Serializer.INSTANCE.serialize(memberChangeStatusDetails.newValue, jsonGenerator);
            if (memberChangeStatusDetails.previousValue != null) {
                jsonGenerator.writeFieldName("previous_value");
                StoneSerializers.nullable(Serializer.INSTANCE).serialize(memberChangeStatusDetails.previousValue, jsonGenerator);
            }
            if (memberChangeStatusDetails.action != null) {
                jsonGenerator.writeFieldName(MMContentFileViewerFragment.RESULT_ACTION);
                StoneSerializers.nullable(Serializer.INSTANCE).serialize(memberChangeStatusDetails.action, jsonGenerator);
            }
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public MemberChangeStatusDetails deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            MemberStatus memberStatus = null;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
            } else {
                str = null;
            }
            if (str == null) {
                MemberStatus memberStatus2 = null;
                ActionDetails actionDetails = null;
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if ("new_value".equals(currentName)) {
                        memberStatus = Serializer.INSTANCE.deserialize(jsonParser);
                    } else if ("previous_value".equals(currentName)) {
                        memberStatus2 = (MemberStatus) StoneSerializers.nullable(Serializer.INSTANCE).deserialize(jsonParser);
                    } else if (MMContentFileViewerFragment.RESULT_ACTION.equals(currentName)) {
                        actionDetails = (ActionDetails) StoneSerializers.nullable(Serializer.INSTANCE).deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (memberStatus != null) {
                    MemberChangeStatusDetails memberChangeStatusDetails = new MemberChangeStatusDetails(memberStatus, memberStatus2, actionDetails);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(memberChangeStatusDetails, memberChangeStatusDetails.toStringMultiline());
                    return memberChangeStatusDetails;
                }
                throw new JsonParseException(jsonParser, "Required field \"new_value\" missing.");
            }
            StringBuilder sb = new StringBuilder();
            sb.append("No subtype found that matches tag: \"");
            sb.append(str);
            sb.append("\"");
            throw new JsonParseException(jsonParser, sb.toString());
        }
    }

    public MemberChangeStatusDetails(MemberStatus memberStatus, MemberStatus memberStatus2, ActionDetails actionDetails) {
        this.previousValue = memberStatus2;
        if (memberStatus != null) {
            this.newValue = memberStatus;
            this.action = actionDetails;
            return;
        }
        throw new IllegalArgumentException("Required value for 'newValue' is null");
    }

    public MemberChangeStatusDetails(MemberStatus memberStatus) {
        this(memberStatus, null, null);
    }

    public MemberStatus getNewValue() {
        return this.newValue;
    }

    public MemberStatus getPreviousValue() {
        return this.previousValue;
    }

    public ActionDetails getAction() {
        return this.action;
    }

    public static Builder newBuilder(MemberStatus memberStatus) {
        return new Builder(memberStatus);
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.previousValue, this.newValue, this.action});
    }

    /* JADX WARNING: Code restructure failed: missing block: B:21:0x003e, code lost:
        if (r2.equals(r5) == false) goto L_0x0041;
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
            if (r2 == 0) goto L_0x0043
            com.dropbox.core.v2.teamlog.MemberChangeStatusDetails r5 = (com.dropbox.core.p005v2.teamlog.MemberChangeStatusDetails) r5
            com.dropbox.core.v2.teamlog.MemberStatus r2 = r4.newValue
            com.dropbox.core.v2.teamlog.MemberStatus r3 = r5.newValue
            if (r2 == r3) goto L_0x0024
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0041
        L_0x0024:
            com.dropbox.core.v2.teamlog.MemberStatus r2 = r4.previousValue
            com.dropbox.core.v2.teamlog.MemberStatus r3 = r5.previousValue
            if (r2 == r3) goto L_0x0032
            if (r2 == 0) goto L_0x0041
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0041
        L_0x0032:
            com.dropbox.core.v2.teamlog.ActionDetails r2 = r4.action
            com.dropbox.core.v2.teamlog.ActionDetails r5 = r5.action
            if (r2 == r5) goto L_0x0042
            if (r2 == 0) goto L_0x0041
            boolean r5 = r2.equals(r5)
            if (r5 == 0) goto L_0x0041
            goto L_0x0042
        L_0x0041:
            r0 = 0
        L_0x0042:
            return r0
        L_0x0043:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.teamlog.MemberChangeStatusDetails.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
