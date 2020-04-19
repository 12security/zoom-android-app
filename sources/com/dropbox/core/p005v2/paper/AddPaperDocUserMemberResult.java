package com.dropbox.core.p005v2.paper;

import com.dropbox.core.p005v2.sharing.MemberSelector;
import com.dropbox.core.stone.StoneDeserializerLogger;
import com.dropbox.core.stone.StructSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import java.util.Arrays;

/* renamed from: com.dropbox.core.v2.paper.AddPaperDocUserMemberResult */
public class AddPaperDocUserMemberResult {
    protected final MemberSelector member;
    protected final AddPaperDocUserResult result;

    /* renamed from: com.dropbox.core.v2.paper.AddPaperDocUserMemberResult$Serializer */
    static class Serializer extends StructSerializer<AddPaperDocUserMemberResult> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(AddPaperDocUserMemberResult addPaperDocUserMemberResult, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("member");
            com.dropbox.core.p005v2.sharing.MemberSelector.Serializer.INSTANCE.serialize(addPaperDocUserMemberResult.member, jsonGenerator);
            jsonGenerator.writeFieldName("result");
            Serializer.INSTANCE.serialize(addPaperDocUserMemberResult.result, jsonGenerator);
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public AddPaperDocUserMemberResult deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            MemberSelector memberSelector = null;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
            } else {
                str = null;
            }
            if (str == null) {
                AddPaperDocUserResult addPaperDocUserResult = null;
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if ("member".equals(currentName)) {
                        memberSelector = com.dropbox.core.p005v2.sharing.MemberSelector.Serializer.INSTANCE.deserialize(jsonParser);
                    } else if ("result".equals(currentName)) {
                        addPaperDocUserResult = Serializer.INSTANCE.deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (memberSelector == null) {
                    throw new JsonParseException(jsonParser, "Required field \"member\" missing.");
                } else if (addPaperDocUserResult != null) {
                    AddPaperDocUserMemberResult addPaperDocUserMemberResult = new AddPaperDocUserMemberResult(memberSelector, addPaperDocUserResult);
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(addPaperDocUserMemberResult, addPaperDocUserMemberResult.toStringMultiline());
                    return addPaperDocUserMemberResult;
                } else {
                    throw new JsonParseException(jsonParser, "Required field \"result\" missing.");
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

    public AddPaperDocUserMemberResult(MemberSelector memberSelector, AddPaperDocUserResult addPaperDocUserResult) {
        if (memberSelector != null) {
            this.member = memberSelector;
            if (addPaperDocUserResult != null) {
                this.result = addPaperDocUserResult;
                return;
            }
            throw new IllegalArgumentException("Required value for 'result' is null");
        }
        throw new IllegalArgumentException("Required value for 'member' is null");
    }

    public MemberSelector getMember() {
        return this.member;
    }

    public AddPaperDocUserResult getResult() {
        return this.result;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.member, this.result});
    }

    /* JADX WARNING: Code restructure failed: missing block: B:15:0x002e, code lost:
        if (r2.equals(r5) == false) goto L_0x0031;
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
            if (r2 == 0) goto L_0x0033
            com.dropbox.core.v2.paper.AddPaperDocUserMemberResult r5 = (com.dropbox.core.p005v2.paper.AddPaperDocUserMemberResult) r5
            com.dropbox.core.v2.sharing.MemberSelector r2 = r4.member
            com.dropbox.core.v2.sharing.MemberSelector r3 = r5.member
            if (r2 == r3) goto L_0x0024
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0031
        L_0x0024:
            com.dropbox.core.v2.paper.AddPaperDocUserResult r2 = r4.result
            com.dropbox.core.v2.paper.AddPaperDocUserResult r5 = r5.result
            if (r2 == r5) goto L_0x0032
            boolean r5 = r2.equals(r5)
            if (r5 == 0) goto L_0x0031
            goto L_0x0032
        L_0x0031:
            r0 = 0
        L_0x0032:
            return r0
        L_0x0033:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.paper.AddPaperDocUserMemberResult.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
