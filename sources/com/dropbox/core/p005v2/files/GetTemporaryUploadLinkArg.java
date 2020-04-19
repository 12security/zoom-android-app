package com.dropbox.core.p005v2.files;

import com.dropbox.core.stone.StoneDeserializerLogger;
import com.dropbox.core.stone.StoneSerializers;
import com.dropbox.core.stone.StructSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import java.util.Arrays;

/* renamed from: com.dropbox.core.v2.files.GetTemporaryUploadLinkArg */
class GetTemporaryUploadLinkArg {
    protected final CommitInfo commitInfo;
    protected final double duration;

    /* renamed from: com.dropbox.core.v2.files.GetTemporaryUploadLinkArg$Serializer */
    static class Serializer extends StructSerializer<GetTemporaryUploadLinkArg> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(GetTemporaryUploadLinkArg getTemporaryUploadLinkArg, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            jsonGenerator.writeFieldName("commit_info");
            Serializer.INSTANCE.serialize(getTemporaryUploadLinkArg.commitInfo, jsonGenerator);
            jsonGenerator.writeFieldName("duration");
            StoneSerializers.float64().serialize(Double.valueOf(getTemporaryUploadLinkArg.duration), jsonGenerator);
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public GetTemporaryUploadLinkArg deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            CommitInfo commitInfo = null;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
            } else {
                str = null;
            }
            if (str == null) {
                Double valueOf = Double.valueOf(14400.0d);
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if ("commit_info".equals(currentName)) {
                        commitInfo = (CommitInfo) Serializer.INSTANCE.deserialize(jsonParser);
                    } else if ("duration".equals(currentName)) {
                        valueOf = (Double) StoneSerializers.float64().deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                if (commitInfo != null) {
                    GetTemporaryUploadLinkArg getTemporaryUploadLinkArg = new GetTemporaryUploadLinkArg(commitInfo, valueOf.doubleValue());
                    if (!z) {
                        expectEndObject(jsonParser);
                    }
                    StoneDeserializerLogger.log(getTemporaryUploadLinkArg, getTemporaryUploadLinkArg.toStringMultiline());
                    return getTemporaryUploadLinkArg;
                }
                throw new JsonParseException(jsonParser, "Required field \"commit_info\" missing.");
            }
            StringBuilder sb = new StringBuilder();
            sb.append("No subtype found that matches tag: \"");
            sb.append(str);
            sb.append("\"");
            throw new JsonParseException(jsonParser, sb.toString());
        }
    }

    public GetTemporaryUploadLinkArg(CommitInfo commitInfo2, double d) {
        if (commitInfo2 != null) {
            this.commitInfo = commitInfo2;
            if (d < 60.0d) {
                throw new IllegalArgumentException("Number 'duration' is smaller than 60.0");
            } else if (d <= 14400.0d) {
                this.duration = d;
            } else {
                throw new IllegalArgumentException("Number 'duration' is larger than 14400.0");
            }
        } else {
            throw new IllegalArgumentException("Required value for 'commitInfo' is null");
        }
    }

    public GetTemporaryUploadLinkArg(CommitInfo commitInfo2) {
        this(commitInfo2, 14400.0d);
    }

    public CommitInfo getCommitInfo() {
        return this.commitInfo;
    }

    public double getDuration() {
        return this.duration;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.commitInfo, Double.valueOf(this.duration)});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !obj.getClass().equals(getClass())) {
            return false;
        }
        GetTemporaryUploadLinkArg getTemporaryUploadLinkArg = (GetTemporaryUploadLinkArg) obj;
        CommitInfo commitInfo2 = this.commitInfo;
        CommitInfo commitInfo3 = getTemporaryUploadLinkArg.commitInfo;
        if ((commitInfo2 != commitInfo3 && !commitInfo2.equals(commitInfo3)) || this.duration != getTemporaryUploadLinkArg.duration) {
            z = false;
        }
        return z;
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
