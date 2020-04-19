package com.dropbox.core.p005v2.sharing;

import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;

/* renamed from: com.dropbox.core.v2.sharing.FolderAction */
public enum FolderAction {
    CHANGE_OPTIONS,
    DISABLE_VIEWER_INFO,
    EDIT_CONTENTS,
    ENABLE_VIEWER_INFO,
    INVITE_EDITOR,
    INVITE_VIEWER,
    INVITE_VIEWER_NO_COMMENT,
    RELINQUISH_MEMBERSHIP,
    UNMOUNT,
    UNSHARE,
    LEAVE_A_COPY,
    SHARE_LINK,
    CREATE_LINK,
    SET_ACCESS_INHERITANCE,
    OTHER;

    /* renamed from: com.dropbox.core.v2.sharing.FolderAction$Serializer */
    static class Serializer extends UnionSerializer<FolderAction> {
        public static final Serializer INSTANCE = null;

        Serializer() {
        }

        static {
            INSTANCE = new Serializer();
        }

        public void serialize(FolderAction folderAction, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (folderAction) {
                case CHANGE_OPTIONS:
                    jsonGenerator.writeString("change_options");
                    return;
                case DISABLE_VIEWER_INFO:
                    jsonGenerator.writeString("disable_viewer_info");
                    return;
                case EDIT_CONTENTS:
                    jsonGenerator.writeString("edit_contents");
                    return;
                case ENABLE_VIEWER_INFO:
                    jsonGenerator.writeString("enable_viewer_info");
                    return;
                case INVITE_EDITOR:
                    jsonGenerator.writeString("invite_editor");
                    return;
                case INVITE_VIEWER:
                    jsonGenerator.writeString("invite_viewer");
                    return;
                case INVITE_VIEWER_NO_COMMENT:
                    jsonGenerator.writeString("invite_viewer_no_comment");
                    return;
                case RELINQUISH_MEMBERSHIP:
                    jsonGenerator.writeString("relinquish_membership");
                    return;
                case UNMOUNT:
                    jsonGenerator.writeString("unmount");
                    return;
                case UNSHARE:
                    jsonGenerator.writeString("unshare");
                    return;
                case LEAVE_A_COPY:
                    jsonGenerator.writeString("leave_a_copy");
                    return;
                case SHARE_LINK:
                    jsonGenerator.writeString("share_link");
                    return;
                case CREATE_LINK:
                    jsonGenerator.writeString("create_link");
                    return;
                case SET_ACCESS_INHERITANCE:
                    jsonGenerator.writeString("set_access_inheritance");
                    return;
                default:
                    jsonGenerator.writeString("other");
                    return;
            }
        }

        public FolderAction deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            FolderAction folderAction;
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
                if ("change_options".equals(str)) {
                    folderAction = FolderAction.CHANGE_OPTIONS;
                } else if ("disable_viewer_info".equals(str)) {
                    folderAction = FolderAction.DISABLE_VIEWER_INFO;
                } else if ("edit_contents".equals(str)) {
                    folderAction = FolderAction.EDIT_CONTENTS;
                } else if ("enable_viewer_info".equals(str)) {
                    folderAction = FolderAction.ENABLE_VIEWER_INFO;
                } else if ("invite_editor".equals(str)) {
                    folderAction = FolderAction.INVITE_EDITOR;
                } else if ("invite_viewer".equals(str)) {
                    folderAction = FolderAction.INVITE_VIEWER;
                } else if ("invite_viewer_no_comment".equals(str)) {
                    folderAction = FolderAction.INVITE_VIEWER_NO_COMMENT;
                } else if ("relinquish_membership".equals(str)) {
                    folderAction = FolderAction.RELINQUISH_MEMBERSHIP;
                } else if ("unmount".equals(str)) {
                    folderAction = FolderAction.UNMOUNT;
                } else if ("unshare".equals(str)) {
                    folderAction = FolderAction.UNSHARE;
                } else if ("leave_a_copy".equals(str)) {
                    folderAction = FolderAction.LEAVE_A_COPY;
                } else if ("share_link".equals(str)) {
                    folderAction = FolderAction.SHARE_LINK;
                } else if ("create_link".equals(str)) {
                    folderAction = FolderAction.CREATE_LINK;
                } else if ("set_access_inheritance".equals(str)) {
                    folderAction = FolderAction.SET_ACCESS_INHERITANCE;
                } else {
                    folderAction = FolderAction.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return folderAction;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }
}
