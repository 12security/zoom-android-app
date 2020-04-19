package com.dropbox.core.p005v2.paper;

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
import java.util.List;

/* renamed from: com.dropbox.core.v2.paper.FoldersContainingPaperDoc */
public class FoldersContainingPaperDoc {
    protected final FolderSharingPolicyType folderSharingPolicyType;
    protected final List<Folder> folders;

    /* renamed from: com.dropbox.core.v2.paper.FoldersContainingPaperDoc$Builder */
    public static class Builder {
        protected FolderSharingPolicyType folderSharingPolicyType = null;
        protected List<Folder> folders = null;

        protected Builder() {
        }

        public Builder withFolderSharingPolicyType(FolderSharingPolicyType folderSharingPolicyType2) {
            this.folderSharingPolicyType = folderSharingPolicyType2;
            return this;
        }

        public Builder withFolders(List<Folder> list) {
            if (list != null) {
                for (Folder folder : list) {
                    if (folder == null) {
                        throw new IllegalArgumentException("An item in list 'folders' is null");
                    }
                }
            }
            this.folders = list;
            return this;
        }

        public FoldersContainingPaperDoc build() {
            return new FoldersContainingPaperDoc(this.folderSharingPolicyType, this.folders);
        }
    }

    /* renamed from: com.dropbox.core.v2.paper.FoldersContainingPaperDoc$Serializer */
    static class Serializer extends StructSerializer<FoldersContainingPaperDoc> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(FoldersContainingPaperDoc foldersContainingPaperDoc, JsonGenerator jsonGenerator, boolean z) throws IOException, JsonGenerationException {
            if (!z) {
                jsonGenerator.writeStartObject();
            }
            if (foldersContainingPaperDoc.folderSharingPolicyType != null) {
                jsonGenerator.writeFieldName("folder_sharing_policy_type");
                StoneSerializers.nullable(Serializer.INSTANCE).serialize(foldersContainingPaperDoc.folderSharingPolicyType, jsonGenerator);
            }
            if (foldersContainingPaperDoc.folders != null) {
                jsonGenerator.writeFieldName("folders");
                StoneSerializers.nullable(StoneSerializers.list(Serializer.INSTANCE)).serialize(foldersContainingPaperDoc.folders, jsonGenerator);
            }
            if (!z) {
                jsonGenerator.writeEndObject();
            }
        }

        public FoldersContainingPaperDoc deserialize(JsonParser jsonParser, boolean z) throws IOException, JsonParseException {
            String str;
            FolderSharingPolicyType folderSharingPolicyType = null;
            if (!z) {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
            } else {
                str = null;
            }
            if (str == null) {
                List list = null;
                while (jsonParser.getCurrentToken() == JsonToken.FIELD_NAME) {
                    String currentName = jsonParser.getCurrentName();
                    jsonParser.nextToken();
                    if ("folder_sharing_policy_type".equals(currentName)) {
                        folderSharingPolicyType = (FolderSharingPolicyType) StoneSerializers.nullable(Serializer.INSTANCE).deserialize(jsonParser);
                    } else if ("folders".equals(currentName)) {
                        list = (List) StoneSerializers.nullable(StoneSerializers.list(Serializer.INSTANCE)).deserialize(jsonParser);
                    } else {
                        skipValue(jsonParser);
                    }
                }
                FoldersContainingPaperDoc foldersContainingPaperDoc = new FoldersContainingPaperDoc(folderSharingPolicyType, list);
                if (!z) {
                    expectEndObject(jsonParser);
                }
                StoneDeserializerLogger.log(foldersContainingPaperDoc, foldersContainingPaperDoc.toStringMultiline());
                return foldersContainingPaperDoc;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("No subtype found that matches tag: \"");
            sb.append(str);
            sb.append("\"");
            throw new JsonParseException(jsonParser, sb.toString());
        }
    }

    public FoldersContainingPaperDoc(FolderSharingPolicyType folderSharingPolicyType2, List<Folder> list) {
        this.folderSharingPolicyType = folderSharingPolicyType2;
        if (list != null) {
            for (Folder folder : list) {
                if (folder == null) {
                    throw new IllegalArgumentException("An item in list 'folders' is null");
                }
            }
        }
        this.folders = list;
    }

    public FoldersContainingPaperDoc() {
        this(null, null);
    }

    public FolderSharingPolicyType getFolderSharingPolicyType() {
        return this.folderSharingPolicyType;
    }

    public List<Folder> getFolders() {
        return this.folders;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.folderSharingPolicyType, this.folders});
    }

    /* JADX WARNING: Code restructure failed: missing block: B:17:0x0032, code lost:
        if (r2.equals(r5) == false) goto L_0x0035;
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
            if (r2 == 0) goto L_0x0037
            com.dropbox.core.v2.paper.FoldersContainingPaperDoc r5 = (com.dropbox.core.p005v2.paper.FoldersContainingPaperDoc) r5
            com.dropbox.core.v2.paper.FolderSharingPolicyType r2 = r4.folderSharingPolicyType
            com.dropbox.core.v2.paper.FolderSharingPolicyType r3 = r5.folderSharingPolicyType
            if (r2 == r3) goto L_0x0026
            if (r2 == 0) goto L_0x0035
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L_0x0035
        L_0x0026:
            java.util.List<com.dropbox.core.v2.paper.Folder> r2 = r4.folders
            java.util.List<com.dropbox.core.v2.paper.Folder> r5 = r5.folders
            if (r2 == r5) goto L_0x0036
            if (r2 == 0) goto L_0x0035
            boolean r5 = r2.equals(r5)
            if (r5 == 0) goto L_0x0035
            goto L_0x0036
        L_0x0035:
            r0 = 0
        L_0x0036:
            return r0
        L_0x0037:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.dropbox.core.p005v2.paper.FoldersContainingPaperDoc.equals(java.lang.Object):boolean");
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
