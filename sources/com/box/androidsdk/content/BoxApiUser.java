package com.box.androidsdk.content;

import com.box.androidsdk.content.models.BoxSession;
import com.box.androidsdk.content.requests.BoxRequestsUser.CreateEnterpriseUser;
import com.box.androidsdk.content.requests.BoxRequestsUser.DeleteEnterpriseUser;
import com.box.androidsdk.content.requests.BoxRequestsUser.GetEnterpriseUsers;
import com.box.androidsdk.content.requests.BoxRequestsUser.GetUserInfo;

public class BoxApiUser extends BoxApi {
    public BoxApiUser(BoxSession boxSession) {
        super(boxSession);
    }

    /* access modifiers changed from: protected */
    public String getUsersUrl() {
        return String.format("%s/users", new Object[]{getBaseUri()});
    }

    /* access modifiers changed from: protected */
    public String getUserInformationUrl(String str) {
        return String.format("%s/%s", new Object[]{getUsersUrl(), str});
    }

    public GetUserInfo getCurrentUserInfoRequest() {
        return new GetUserInfo(getUserInformationUrl("me"), this.mSession);
    }

    public GetUserInfo getUserInfoRequest(String str) {
        return new GetUserInfo(getUserInformationUrl(str), this.mSession);
    }

    public GetEnterpriseUsers getEnterpriseUsersRequest() {
        return new GetEnterpriseUsers(getUsersUrl(), this.mSession);
    }

    public CreateEnterpriseUser getCreateEnterpriseUserRequest(String str, String str2) {
        return new CreateEnterpriseUser(getUsersUrl(), this.mSession, str, str2);
    }

    public DeleteEnterpriseUser getDeleteEnterpriseUserRequest(String str) {
        return new DeleteEnterpriseUser(getUserInformationUrl(str), this.mSession, str);
    }
}
