package com.dropbox.core.p005v2.sharing;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

/* renamed from: com.dropbox.core.v2.sharing.FileAction */
public enum FileAction {
    DISABLE_VIEWER_INFO,
    EDIT_CONTENTS,
    ENABLE_VIEWER_INFO,
    INVITE_VIEWER,
    INVITE_VIEWER_NO_COMMENT,
    INVITE_EDITOR,
    UNSHARE,
    RELINQUISH_MEMBERSHIP,
    SHARE_LINK,
    CREATE_LINK,
    OTHER;

    /* renamed from: com.dropbox.core.v2.sharing.FileAction$Serializer */
    static class Serializer extends UnionSerializer<FileAction> {
        public static final Serializer INSTANCE = null;

        Serializer() {
        }

        static {
            INSTANCE = new Serializer();
        }

        public void serialize(FileAction fileAction, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (fileAction) {
                case DISABLE_VIEWER_INFO:
                    jsonGenerator.writeString("disable_viewer_info");
                    return;
                case EDIT_CONTENTS:
                    jsonGenerator.writeString("edit_contents");
                    return;
                case ENABLE_VIEWER_INFO:
                    jsonGenerator.writeString("enable_viewer_info");
                    return;
                case INVITE_VIEWER:
                    jsonGenerator.writeString("invite_viewer");
                    return;
                case INVITE_VIEWER_NO_COMMENT:
                    jsonGenerator.writeString("invite_viewer_no_comment");
                    return;
                case INVITE_EDITOR:
                    jsonGenerator.writeString("invite_editor");
                    return;
                case UNSHARE:
                    jsonGenerator.writeString("unshare");
                    return;
                case RELINQUISH_MEMBERSHIP:
                    jsonGenerator.writeString("relinquish_membership");
                    return;
                case SHARE_LINK:
                    jsonGenerator.writeString("share_link");
                    return;
                case CREATE_LINK:
                    jsonGenerator.writeString("create_link");
                    return;
                default:
                    jsonGenerator.writeString("other");
                    return;
            }
        }

        public FileAction deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            FileAction fileAction;
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
                if ("disable_viewer_info".equals(str)) {
                    fileAction = FileAction.DISABLE_VIEWER_INFO;
                } else if ("edit_contents".equals(str)) {
                    fileAction = FileAction.EDIT_CONTENTS;
                } else if ("enable_viewer_info".equals(str)) {
                    fileAction = FileAction.ENABLE_VIEWER_INFO;
                } else if ("invite_viewer".equals(str)) {
                    fileAction = FileAction.INVITE_VIEWER;
                } else if ("invite_viewer_no_comment".equals(str)) {
                    fileAction = FileAction.INVITE_VIEWER_NO_COMMENT;
                } else if ("invite_editor".equals(str)) {
                    fileAction = FileAction.INVITE_EDITOR;
                } else if ("unshare".equals(str)) {
                    fileAction = FileAction.UNSHARE;
                } else if ("relinquish_membership".equals(str)) {
                    fileAction = FileAction.RELINQUISH_MEMBERSHIP;
                } else if ("share_link".equals(str)) {
                    fileAction = FileAction.SHARE_LINK;
                } else if ("create_link".equals(str)) {
                    fileAction = FileAction.CREATE_LINK;
                } else {
                    fileAction = FileAction.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return fileAction;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }
}
