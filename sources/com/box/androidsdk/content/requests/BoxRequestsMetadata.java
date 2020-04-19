package com.box.androidsdk.content.requests;

import com.box.androidsdk.content.models.BoxArray;
import com.box.androidsdk.content.models.BoxJsonObject;
import com.box.androidsdk.content.models.BoxMetadata;
import com.box.androidsdk.content.models.BoxSession;
import com.box.androidsdk.content.models.BoxVoid;
import com.box.androidsdk.content.requests.BoxRequest.ContentTypes;
import com.box.androidsdk.content.requests.BoxRequest.Methods;
import java.util.Map;

public class BoxRequestsMetadata {

    public static class AddFileMetadata extends BoxRequest<BoxMetadata, AddFileMetadata> {
        public AddFileMetadata(Map<String, Object> map, String str, BoxSession boxSession) {
            super(BoxMetadata.class, str, boxSession);
            this.mRequestMethod = Methods.POST;
            setValues(map);
        }

        /* access modifiers changed from: protected */
        public AddFileMetadata setValues(Map<String, Object> map) {
            this.mBodyMap.putAll(map);
            return this;
        }
    }

    public static class DeleteFileMetadata extends BoxRequest<BoxVoid, DeleteFileMetadata> {
        public DeleteFileMetadata(String str, BoxSession boxSession) {
            super(BoxVoid.class, str, boxSession);
            this.mRequestMethod = Methods.DELETE;
        }
    }

    public static class GetFileMetadata extends BoxRequest<BoxMetadata, GetFileMetadata> {
        public GetFileMetadata(String str, BoxSession boxSession) {
            super(BoxMetadata.class, str, boxSession);
            this.mRequestMethod = Methods.GET;
        }
    }

    public static class GetMetadataTemplateSchema extends BoxRequest<BoxMetadata, GetMetadataTemplateSchema> {
        public GetMetadataTemplateSchema(String str, BoxSession boxSession) {
            super(BoxMetadata.class, str, boxSession);
            this.mRequestMethod = Methods.GET;
        }
    }

    public static class GetMetadataTemplates extends BoxRequest<BoxMetadata, GetMetadataTemplates> {
        public GetMetadataTemplates(String str, BoxSession boxSession) {
            super(BoxMetadata.class, str, boxSession);
            this.mRequestMethod = Methods.GET;
        }
    }

    public static class UpdateFileMetadata extends BoxRequest<BoxMetadata, UpdateFileMetadata> {
        private BoxArray<BoxMetadataUpdateTask> mUpdateTasks;

        private class BoxMetadataUpdateTask extends BoxJsonObject {
            public static final String OPERATION = "op";
            public static final String PATH = "path";
            public static final String VALUE = "value";

            public BoxMetadataUpdateTask(Operations operations, String str, String str2) {
                this.mProperties.put(OPERATION, operations.toString());
                StringBuilder sb = new StringBuilder();
                sb.append("/");
                sb.append(str);
                this.mProperties.put("path", sb.toString());
                if (operations != Operations.REMOVE) {
                    this.mProperties.put("value", str2);
                }
            }
        }

        public enum Operations {
            ADD("add"),
            REPLACE("replace"),
            REMOVE("remove"),
            TEST("test");
            
            private String mName;

            private Operations(String str) {
                this.mName = str;
            }

            public String toString() {
                return this.mName;
            }
        }

        public UpdateFileMetadata(String str, BoxSession boxSession) {
            super(BoxMetadata.class, str, boxSession);
            this.mRequestMethod = Methods.PUT;
            this.mContentType = ContentTypes.JSON_PATCH;
            this.mUpdateTasks = new BoxArray<>();
        }

        /* access modifiers changed from: protected */
        public UpdateFileMetadata setUpdateTasks(BoxArray<BoxMetadataUpdateTask> boxArray) {
            this.mBodyMap.put(BoxRequest.JSON_OBJECT, boxArray);
            return this;
        }

        public UpdateFileMetadata addUpdateTask(Operations operations, String str, String str2) {
            this.mUpdateTasks.add(new BoxMetadataUpdateTask(operations, str, str2));
            return setUpdateTasks(this.mUpdateTasks);
        }

        public UpdateFileMetadata addUpdateTask(Operations operations, String str) {
            return addUpdateTask(operations, str, "");
        }
    }
}
