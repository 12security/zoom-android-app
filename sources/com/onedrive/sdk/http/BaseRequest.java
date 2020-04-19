package com.onedrive.sdk.http;

import android.net.Uri;
import android.net.Uri.Builder;
import com.microsoft.onedrivesdk.BuildConfig;
import com.onedrive.sdk.concurrency.ICallback;
import com.onedrive.sdk.core.ClientException;
import com.onedrive.sdk.core.OneDriveErrorCodes;
import com.onedrive.sdk.extensions.IOneDriveClient;
import com.onedrive.sdk.options.HeaderOption;
import com.onedrive.sdk.options.Option;
import com.onedrive.sdk.options.QueryOption;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public abstract class BaseRequest implements IHttpRequest {
    private static final String REQUEST_STATS_HEADER_NAME = "X-RequestStats";
    public static final String REQUEST_STATS_HEADER_VALUE_FORMAT_STRING = "SDK-Version=Android-v%s";
    private final IOneDriveClient mClient;
    private final List<HeaderOption> mHeadersOptions = new ArrayList();
    private HttpMethod mMethod;
    private final List<QueryOption> mQueryOptions = new ArrayList();
    private final String mRequestUrl;
    private final Class mResponseClass;

    public BaseRequest(String str, IOneDriveClient iOneDriveClient, List<Option> list, Class cls) {
        this.mRequestUrl = str;
        this.mClient = iOneDriveClient;
        this.mResponseClass = cls;
        if (list != null) {
            for (Option option : list) {
                if (option instanceof HeaderOption) {
                    this.mHeadersOptions.add((HeaderOption) option);
                }
                if (option instanceof QueryOption) {
                    this.mQueryOptions.add((QueryOption) option);
                }
            }
        }
        this.mHeadersOptions.add(new HeaderOption(REQUEST_STATS_HEADER_NAME, String.format(REQUEST_STATS_HEADER_VALUE_FORMAT_STRING, new Object[]{BuildConfig.VERSION_NAME})));
    }

    public URL getRequestUrl() {
        Uri parse = Uri.parse(this.mRequestUrl);
        Builder encodedQuery = new Builder().scheme(parse.getScheme()).encodedAuthority(parse.getEncodedAuthority()).encodedQuery(parse.getEncodedQuery());
        for (String appendPath : parse.getPathSegments()) {
            encodedQuery.appendPath(appendPath);
        }
        for (QueryOption queryOption : this.mQueryOptions) {
            encodedQuery.appendQueryParameter(queryOption.getName(), queryOption.getValue());
        }
        String uri = encodedQuery.build().toString();
        try {
            return new URL(uri);
        } catch (MalformedURLException e) {
            StringBuilder sb = new StringBuilder();
            sb.append("Invalid URL: ");
            sb.append(uri);
            throw new ClientException(sb.toString(), e, OneDriveErrorCodes.InvalidRequest);
        }
    }

    public HttpMethod getHttpMethod() {
        return this.mMethod;
    }

    public List<HeaderOption> getHeaders() {
        return this.mHeadersOptions;
    }

    public void addHeader(String str, String str2) {
        this.mHeadersOptions.add(new HeaderOption(str, str2));
    }

    /* access modifiers changed from: protected */
    public <T1, T2> void send(HttpMethod httpMethod, ICallback<T1> iCallback, T2 t2) {
        this.mMethod = httpMethod;
        this.mClient.getHttpProvider().send((IHttpRequest) this, iCallback, this.mResponseClass, t2);
    }

    /* access modifiers changed from: protected */
    public <T1, T2> T1 send(HttpMethod httpMethod, T2 t2) throws ClientException {
        this.mMethod = httpMethod;
        return this.mClient.getHttpProvider().send(this, this.mResponseClass, t2);
    }

    public List<QueryOption> getQueryOptions() {
        return this.mQueryOptions;
    }

    public List<Option> getOptions() {
        LinkedList linkedList = new LinkedList();
        linkedList.addAll(this.mHeadersOptions);
        linkedList.addAll(this.mQueryOptions);
        return Collections.unmodifiableList(linkedList);
    }

    public void setHttpMethod(HttpMethod httpMethod) {
        this.mMethod = httpMethod;
    }

    public IOneDriveClient getClient() {
        return this.mClient;
    }

    public Class getResponseType() {
        return this.mResponseClass;
    }
}
