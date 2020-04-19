package com.dropbox.core.p005v2.common;

import com.dropbox.core.stone.StoneSerializers;
import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import java.util.Arrays;
import java.util.regex.Pattern;

/* renamed from: com.dropbox.core.v2.common.PathRoot */
public final class PathRoot {
    public static final PathRoot HOME = new PathRoot().withTag(Tag.HOME);
    public static final PathRoot OTHER = new PathRoot().withTag(Tag.OTHER);
    private Tag _tag;
    /* access modifiers changed from: private */
    public String namespaceIdValue;
    /* access modifiers changed from: private */
    public String rootValue;

    /* renamed from: com.dropbox.core.v2.common.PathRoot$Serializer */
    public static class Serializer extends UnionSerializer<PathRoot> {
        public static final Serializer INSTANCE = new Serializer();

        public void serialize(PathRoot pathRoot, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (pathRoot.tag()) {
                case HOME:
                    jsonGenerator.writeString("home");
                    return;
                case ROOT:
                    jsonGenerator.writeStartObject();
                    writeTag("root", jsonGenerator);
                    jsonGenerator.writeFieldName("root");
                    StoneSerializers.string().serialize(pathRoot.rootValue, jsonGenerator);
                    jsonGenerator.writeEndObject();
                    return;
                case NAMESPACE_ID:
                    jsonGenerator.writeStartObject();
                    writeTag("namespace_id", jsonGenerator);
                    jsonGenerator.writeFieldName("namespace_id");
                    StoneSerializers.string().serialize(pathRoot.namespaceIdValue, jsonGenerator);
                    jsonGenerator.writeEndObject();
                    return;
                default:
                    jsonGenerator.writeString("other");
                    return;
            }
        }

        public PathRoot deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            PathRoot pathRoot;
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
                if ("home".equals(str)) {
                    pathRoot = PathRoot.HOME;
                } else if ("root".equals(str)) {
                    expectField("root", jsonParser);
                    pathRoot = PathRoot.root((String) StoneSerializers.string().deserialize(jsonParser));
                } else if ("namespace_id".equals(str)) {
                    expectField("namespace_id", jsonParser);
                    pathRoot = PathRoot.namespaceId((String) StoneSerializers.string().deserialize(jsonParser));
                } else {
                    pathRoot = PathRoot.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return pathRoot;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }

    /* renamed from: com.dropbox.core.v2.common.PathRoot$Tag */
    public enum Tag {
        HOME,
        ROOT,
        NAMESPACE_ID,
        OTHER
    }

    private PathRoot() {
    }

    private PathRoot withTag(Tag tag) {
        PathRoot pathRoot = new PathRoot();
        pathRoot._tag = tag;
        return pathRoot;
    }

    private PathRoot withTagAndRoot(Tag tag, String str) {
        PathRoot pathRoot = new PathRoot();
        pathRoot._tag = tag;
        pathRoot.rootValue = str;
        return pathRoot;
    }

    private PathRoot withTagAndNamespaceId(Tag tag, String str) {
        PathRoot pathRoot = new PathRoot();
        pathRoot._tag = tag;
        pathRoot.namespaceIdValue = str;
        return pathRoot;
    }

    public Tag tag() {
        return this._tag;
    }

    public boolean isHome() {
        return this._tag == Tag.HOME;
    }

    public boolean isRoot() {
        return this._tag == Tag.ROOT;
    }

    public static PathRoot root(String str) {
        if (str == null) {
            throw new IllegalArgumentException("Value is null");
        } else if (Pattern.matches("[-_0-9a-zA-Z:]+", str)) {
            return new PathRoot().withTagAndRoot(Tag.ROOT, str);
        } else {
            throw new IllegalArgumentException("String does not match pattern");
        }
    }

    public String getRootValue() {
        if (this._tag == Tag.ROOT) {
            return this.rootValue;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Invalid tag: required Tag.ROOT, but was Tag.");
        sb.append(this._tag.name());
        throw new IllegalStateException(sb.toString());
    }

    public boolean isNamespaceId() {
        return this._tag == Tag.NAMESPACE_ID;
    }

    public static PathRoot namespaceId(String str) {
        if (str == null) {
            throw new IllegalArgumentException("Value is null");
        } else if (Pattern.matches("[-_0-9a-zA-Z:]+", str)) {
            return new PathRoot().withTagAndNamespaceId(Tag.NAMESPACE_ID, str);
        } else {
            throw new IllegalArgumentException("String does not match pattern");
        }
    }

    public String getNamespaceIdValue() {
        if (this._tag == Tag.NAMESPACE_ID) {
            return this.namespaceIdValue;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Invalid tag: required Tag.NAMESPACE_ID, but was Tag.");
        sb.append(this._tag.name());
        throw new IllegalStateException(sb.toString());
    }

    public boolean isOther() {
        return this._tag == Tag.OTHER;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this._tag, this.rootValue, this.namespaceIdValue});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !(obj instanceof PathRoot)) {
            return false;
        }
        PathRoot pathRoot = (PathRoot) obj;
        if (this._tag != pathRoot._tag) {
            return false;
        }
        switch (this._tag) {
            case HOME:
                return true;
            case ROOT:
                String str = this.rootValue;
                String str2 = pathRoot.rootValue;
                if (str != str2 && !str.equals(str2)) {
                    z = false;
                }
                return z;
            case NAMESPACE_ID:
                String str3 = this.namespaceIdValue;
                String str4 = pathRoot.namespaceIdValue;
                if (str3 != str4 && !str3.equals(str4)) {
                    z = false;
                }
                return z;
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
