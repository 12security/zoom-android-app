package com.box.androidsdk.content.requests;

import com.box.androidsdk.content.models.BoxListUsers;
import com.box.androidsdk.content.models.BoxSession;
import com.box.androidsdk.content.models.BoxUser;
import com.box.androidsdk.content.models.BoxUser.Role;
import com.box.androidsdk.content.models.BoxUser.Status;
import com.box.androidsdk.content.models.BoxVoid;
import com.box.androidsdk.content.requests.BoxRequest.Methods;
import com.eclipsesource.json.JsonObject;
import java.util.Map.Entry;

public class BoxRequestsUser {

    public static class CreateEnterpriseUser extends BoxRequestUserUpdate<BoxUser, CreateEnterpriseUser> {
        public /* bridge */ /* synthetic */ String getAddress() {
            return super.getAddress();
        }

        public /* bridge */ /* synthetic */ boolean getCanSeeManagedUsers() {
            return super.getCanSeeManagedUsers();
        }

        public /* bridge */ /* synthetic */ boolean getIsExemptFromDeviceLimits() {
            return super.getIsExemptFromDeviceLimits();
        }

        public /* bridge */ /* synthetic */ boolean getIsExemptFromLoginVerification() {
            return super.getIsExemptFromLoginVerification();
        }

        public /* bridge */ /* synthetic */ boolean getIsSyncEnabled() {
            return super.getIsSyncEnabled();
        }

        public /* bridge */ /* synthetic */ String getJobTitle() {
            return super.getJobTitle();
        }

        public /* bridge */ /* synthetic */ String getName() {
            return super.getName();
        }

        public /* bridge */ /* synthetic */ String getPhone() {
            return super.getPhone();
        }

        public /* bridge */ /* synthetic */ Role getRole() {
            return super.getRole();
        }

        public /* bridge */ /* synthetic */ double getSpaceAmount() {
            return super.getSpaceAmount();
        }

        public /* bridge */ /* synthetic */ Status getStatus() {
            return super.getStatus();
        }

        public /* bridge */ /* synthetic */ String getTimezone() {
            return super.getTimezone();
        }

        public CreateEnterpriseUser(String str, BoxSession boxSession, String str2, String str3) {
            super(BoxUser.class, null, str, boxSession);
            this.mRequestMethod = Methods.POST;
            setLogin(str2);
            setName(str3);
        }

        public String getLogin() {
            return (String) this.mBodyMap.get("login");
        }

        public CreateEnterpriseUser setLogin(String str) {
            this.mBodyMap.put("login", str);
            return this;
        }
    }

    public static class DeleteEnterpriseUser extends BoxRequest<BoxVoid, DeleteEnterpriseUser> {
        protected static final String QUERY_FORCE = "force";
        protected static final String QUERY_NOTIFY = "notify";
        protected String mId;

        public DeleteEnterpriseUser(String str, BoxSession boxSession, String str2) {
            super(BoxVoid.class, str, boxSession);
            this.mRequestMethod = Methods.DELETE;
            this.mId = str2;
        }

        public String getId() {
            return this.mId;
        }

        public Boolean getShouldNotify() {
            return Boolean.valueOf((String) this.mQueryMap.get(QUERY_NOTIFY));
        }

        public DeleteEnterpriseUser setShouldNotify(Boolean bool) {
            this.mQueryMap.put(QUERY_NOTIFY, Boolean.toString(bool.booleanValue()));
            return this;
        }

        public Boolean getShouldForce() {
            return Boolean.valueOf((String) this.mQueryMap.get(QUERY_FORCE));
        }

        public DeleteEnterpriseUser setShouldForce(Boolean bool) {
            this.mQueryMap.put(QUERY_FORCE, Boolean.toString(bool.booleanValue()));
            return this;
        }
    }

    public static class GetEnterpriseUsers extends BoxRequestItem<BoxListUsers, GetEnterpriseUsers> {
        protected static final String QUERY_FILTER_TERM = "filter_term";
        protected static final String QUERY_LIMIT = "limit";
        protected static final String QUERY_OFFSET = "offset";

        public GetEnterpriseUsers(String str, BoxSession boxSession) {
            super(BoxListUsers.class, null, str, boxSession);
            this.mRequestMethod = Methods.GET;
        }

        public String getFilterTerm() {
            return (String) this.mQueryMap.get(QUERY_FILTER_TERM);
        }

        public GetEnterpriseUsers setFilterTerm(String str) {
            this.mQueryMap.put(QUERY_FILTER_TERM, str);
            return this;
        }

        public long getLimit() {
            return Long.valueOf((String) this.mQueryMap.get("limit")).longValue();
        }

        public GetEnterpriseUsers setLimit(long j) {
            this.mQueryMap.put("limit", Long.toString(j));
            return this;
        }

        public long getOffset() {
            return Long.valueOf((String) this.mQueryMap.get("offset")).longValue();
        }

        public GetEnterpriseUsers setOffset(long j) {
            this.mQueryMap.put("offset", Long.toString(j));
            return this;
        }
    }

    public static class GetUserInfo extends BoxRequestItem<BoxUser, GetUserInfo> {
        public GetUserInfo(String str, BoxSession boxSession) {
            super(BoxUser.class, null, str, boxSession);
            this.mRequestMethod = Methods.GET;
        }
    }

    public static class UpdateUserInformation extends BoxRequestUserUpdate<BoxUser, UpdateUserInformation> {
        protected static final String FIELD_IS_PASSWORD_RESET_REQUIRED = "is_password_reset_required";

        public /* bridge */ /* synthetic */ String getAddress() {
            return super.getAddress();
        }

        public /* bridge */ /* synthetic */ boolean getCanSeeManagedUsers() {
            return super.getCanSeeManagedUsers();
        }

        public /* bridge */ /* synthetic */ boolean getIsExemptFromDeviceLimits() {
            return super.getIsExemptFromDeviceLimits();
        }

        public /* bridge */ /* synthetic */ boolean getIsExemptFromLoginVerification() {
            return super.getIsExemptFromLoginVerification();
        }

        public /* bridge */ /* synthetic */ boolean getIsSyncEnabled() {
            return super.getIsSyncEnabled();
        }

        public /* bridge */ /* synthetic */ String getJobTitle() {
            return super.getJobTitle();
        }

        public /* bridge */ /* synthetic */ String getName() {
            return super.getName();
        }

        public /* bridge */ /* synthetic */ String getPhone() {
            return super.getPhone();
        }

        public /* bridge */ /* synthetic */ Role getRole() {
            return super.getRole();
        }

        public /* bridge */ /* synthetic */ double getSpaceAmount() {
            return super.getSpaceAmount();
        }

        public /* bridge */ /* synthetic */ Status getStatus() {
            return super.getStatus();
        }

        public /* bridge */ /* synthetic */ String getTimezone() {
            return super.getTimezone();
        }

        public UpdateUserInformation(String str, BoxSession boxSession, String str2, String str3) {
            super(BoxUser.class, null, str, boxSession);
            this.mRequestMethod = Methods.PUT;
        }

        public String getEnterprise() {
            return (String) this.mBodyMap.get("enterprise");
        }

        public UpdateUserInformation setEnterprise(String str) {
            this.mBodyMap.put("enterprise", str);
            return this;
        }

        public String getIsPasswordResetRequired() {
            return (String) this.mBodyMap.get(FIELD_IS_PASSWORD_RESET_REQUIRED);
        }

        public UpdateUserInformation setIsPasswordResetRequired(boolean z) {
            this.mBodyMap.put(FIELD_IS_PASSWORD_RESET_REQUIRED, Boolean.valueOf(z));
            return this;
        }

        /* access modifiers changed from: protected */
        public void parseHashMapEntry(JsonObject jsonObject, Entry<String, Object> entry) {
            String str;
            if (((String) entry.getKey()).equals("enterprise")) {
                String str2 = (String) entry.getKey();
                if (entry.getValue() == null) {
                    str = null;
                } else {
                    str = (String) entry.getValue();
                }
                jsonObject.add(str2, str);
                return;
            }
            super.parseHashMapEntry(jsonObject, entry);
        }
    }
}
