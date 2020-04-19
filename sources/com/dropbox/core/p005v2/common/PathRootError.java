package com.dropbox.core.p005v2.common;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import java.util.Arrays;

/* renamed from: com.dropbox.core.v2.common.PathRootError */
public final class PathRootError {
    public static final PathRootError NO_PERMISSION = new PathRootError().withTag(Tag.NO_PERMISSION);
    public static final PathRootError OTHER = new PathRootError().withTag(Tag.OTHER);
    private Tag _tag;
    /* access modifiers changed from: private */
    public RootInfo invalidRootValue;

    /* renamed from: com.dropbox.core.v2.common.PathRootError$Serializer */
    public static class Serializer extends UnionSerializer<PathRootError> {
        public static final Serializer INSTANCE = new Serializer();

        public void serialize(PathRootError pathRootError, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (pathRootError.tag()) {
                case INVALID_ROOT:
                    jsonGenerator.writeStartObject();
                    writeTag("invalid_root", jsonGenerator);
                    jsonGenerator.writeFieldName("invalid_root");
                    com.dropbox.core.p005v2.common.RootInfo.Serializer.INSTANCE.serialize(pathRootError.invalidRootValue, jsonGenerator);
                    jsonGenerator.writeEndObject();
                    return;
                case NO_PERMISSION:
                    jsonGenerator.writeString("no_permission");
                    return;
                default:
                    jsonGenerator.writeString("other");
                    return;
            }
        }

        public PathRootError deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            PathRootError pathRootError;
            if (jsonParser.getCurrentToken() == JsonToken.VALUE_STRING) {
                z = true;
                str = getStringValue(jsonParser);
                jsonParser.nextToken();
            } else {
                z = false;
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
            }
            if (str != null) {
                if ("invalid_root".equals(str)) {
                    expectField("invalid_root", jsonParser);
                    pathRootError = PathRootError.invalidRoot((RootInfo) com.dropbox.core.p005v2.common.RootInfo.Serializer.INSTANCE.deserialize(jsonParser));
                } else if ("no_permission".equals(str)) {
                    pathRootError = PathRootError.NO_PERMISSION;
                } else {
                    pathRootError = PathRootError.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return pathRootError;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }

    /* renamed from: com.dropbox.core.v2.common.PathRootError$Tag */
    public enum Tag {
        INVALID_ROOT,
        NO_PERMISSION,
        OTHER
    }

    private PathRootError() {
    }

    private PathRootError withTag(Tag tag) {
        PathRootError pathRootError = new PathRootError();
        pathRootError._tag = tag;
        return pathRootError;
    }

    private PathRootError withTagAndInvalidRoot(Tag tag, RootInfo rootInfo) {
        PathRootError pathRootError = new PathRootError();
        pathRootError._tag = tag;
        pathRootError.invalidRootValue = rootInfo;
        return pathRootError;
    }

    public Tag tag() {
        return this._tag;
    }

    public boolean isInvalidRoot() {
        return this._tag == Tag.INVALID_ROOT;
    }

    public static PathRootError invalidRoot(RootInfo rootInfo) {
        if (rootInfo != null) {
            return new PathRootError().withTagAndInvalidRoot(Tag.INVALID_ROOT, rootInfo);
        }
        throw new IllegalArgumentException("Value is null");
    }

    public RootInfo getInvalidRootValue() {
        if (this._tag == Tag.INVALID_ROOT) {
            return this.invalidRootValue;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Invalid tag: required Tag.INVALID_ROOT, but was Tag.");
        sb.append(this._tag.name());
        throw new IllegalStateException(sb.toString());
    }

    public boolean isNoPermission() {
        return this._tag == Tag.NO_PERMISSION;
    }

    public boolean isOther() {
        return this._tag == Tag.OTHER;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this._tag, this.invalidRootValue});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !(obj instanceof PathRootError)) {
            return false;
        }
        PathRootError pathRootError = (PathRootError) obj;
        if (this._tag != pathRootError._tag) {
            return false;
        }
        switch (this._tag) {
            case INVALID_ROOT:
                RootInfo rootInfo = this.invalidRootValue;
                RootInfo rootInfo2 = pathRootError.invalidRootValue;
                if (rootInfo != rootInfo2 && !rootInfo.equals(rootInfo2)) {
                    z = false;
                }
                return z;
            case NO_PERMISSION:
                return true;
            case OTHER:
                return true;
            default:
                return false;
        }
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
