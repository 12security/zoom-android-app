package com.onedrive.sdk.http;

import com.microsoft.aad.adal.WebRequestHandler;
import com.onedrive.sdk.core.ClientException;
import com.onedrive.sdk.core.OneDriveErrorCodes;
import com.onedrive.sdk.options.HeaderOption;
import com.onedrive.sdk.serializer.ISerializer;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.apache.http.message.TokenParser;
import org.json.JSONException;
import org.json.JSONObject;

public class OneDriveServiceException extends ClientException {
    public static final int INDENT_SPACES = 3;
    public static final int INTERNAL_SERVER_ERROR = 500;
    protected static final int MAX_BREVITY_LENGTH = 50;
    protected static final int MAX_BYTE_COUNT_BEFORE_TRUNCATION = 8;
    protected static final char NEW_LINE = '\n';
    protected static final String TRUNCATION_MARKER = "[...]";
    protected static final String X_THROWSITE = "x-throwsite";
    private final OneDriveErrorResponse mError;
    private final String mMethod;
    private final String mRequestBody;
    private final List<String> mRequestHeaders;
    private final int mResponseCode;
    private final List<String> mResponseHeaders;
    private final String mResponseMessage;
    private final String mUrl;

    protected OneDriveServiceException(String str, String str2, List<String> list, String str3, int i, String str4, List<String> list2, OneDriveErrorResponse oneDriveErrorResponse) {
        super(str4, null, null);
        this.mMethod = str;
        this.mUrl = str2;
        this.mRequestHeaders = list;
        this.mRequestBody = str3;
        this.mResponseCode = i;
        this.mResponseMessage = str4;
        this.mResponseHeaders = list2;
        this.mError = oneDriveErrorResponse;
    }

    public String getMessage() {
        return getMessage(false);
    }

    public List<String> getResponseHeaders() {
        return this.mResponseHeaders;
    }

    public String getMessage(boolean z) {
        StringBuilder sb = new StringBuilder();
        OneDriveErrorResponse oneDriveErrorResponse = this.mError;
        if (!(oneDriveErrorResponse == null || oneDriveErrorResponse.error == null)) {
            sb.append("Error code: ");
            sb.append(this.mError.error.code);
            sb.append(10);
            sb.append("Error message: ");
            sb.append(this.mError.error.message);
            sb.append(10);
            sb.append(10);
        }
        sb.append(this.mMethod);
        sb.append(TokenParser.f498SP);
        sb.append(this.mUrl);
        sb.append(10);
        for (String str : this.mRequestHeaders) {
            if (z) {
                sb.append(str);
            } else {
                String substring = str.substring(0, Math.min(50, str.length()));
                sb.append(substring);
                if (substring.length() == 50) {
                    sb.append(TRUNCATION_MARKER);
                }
            }
            sb.append(10);
        }
        String str2 = this.mRequestBody;
        if (str2 != null) {
            if (z) {
                sb.append(str2);
            } else {
                String substring2 = this.mRequestBody.substring(0, Math.min(50, str2.length()));
                sb.append(substring2);
                if (substring2.length() == 50) {
                    sb.append(TRUNCATION_MARKER);
                }
            }
        }
        sb.append(10);
        sb.append(10);
        sb.append(this.mResponseCode);
        sb.append(" : ");
        sb.append(this.mResponseMessage);
        sb.append(10);
        for (String str3 : this.mResponseHeaders) {
            if (z) {
                sb.append(str3);
                sb.append(10);
            } else if (str3.toLowerCase(Locale.ROOT).startsWith(X_THROWSITE)) {
                sb.append(str3);
                sb.append(10);
            }
        }
        if (z) {
            OneDriveErrorResponse oneDriveErrorResponse2 = this.mError;
            if (!(oneDriveErrorResponse2 == null || oneDriveErrorResponse2.rawObject == null)) {
                try {
                    sb.append(new JSONObject(this.mError.rawObject.toString()).toString(3));
                    sb.append(10);
                } catch (JSONException unused) {
                    sb.append("[Warning: Unable to parse error message body]");
                    sb.append(10);
                    sb.append(this.mError.rawObject.toString());
                    sb.append(10);
                }
                return sb.toString();
            }
        }
        sb.append(TRUNCATION_MARKER);
        sb.append(10);
        sb.append(10);
        sb.append("[Some information was truncated for brevity, enable debug logging for more details]");
        return sb.toString();
    }

    public OneDriveError getServiceError() {
        return this.mError.error;
    }

    public boolean isError(OneDriveErrorCodes oneDriveErrorCodes) {
        if (getServiceError() != null) {
            return getServiceError().isError(oneDriveErrorCodes);
        }
        return false;
    }

    public static <T> OneDriveServiceException createFromConnection(IHttpRequest iHttpRequest, T t, ISerializer iSerializer, IConnection iConnection) throws IOException {
        String str;
        Exception e;
        OneDriveErrorResponse oneDriveErrorResponse;
        String str2;
        String requestMethod = iConnection.getRequestMethod();
        String url = iHttpRequest.getRequestUrl().toString();
        LinkedList linkedList = new LinkedList();
        for (HeaderOption headerOption : iHttpRequest.getHeaders()) {
            StringBuilder sb = new StringBuilder();
            sb.append(headerOption.getName());
            sb.append(" : ");
            sb.append(headerOption.getValue());
            linkedList.add(sb.toString());
        }
        OneDriveErrorResponse oneDriveErrorResponse2 = null;
        if (t instanceof byte[]) {
            byte[] bArr = (byte[]) t;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("byte[");
            sb2.append(bArr.length);
            sb2.append("]");
            sb2.append(" {");
            int i = 0;
            while (i < 8 && i < bArr.length) {
                sb2.append(bArr[i]);
                sb2.append(", ");
                i++;
            }
            if (bArr.length > 8) {
                sb2.append(TRUNCATION_MARKER);
                sb2.append("}");
            }
            str = sb2.toString();
        } else {
            str = t != null ? iSerializer.serializeObject(t) : null;
        }
        int responseCode = iConnection.getResponseCode();
        LinkedList linkedList2 = new LinkedList();
        Map headers = iConnection.getHeaders();
        for (String str3 : headers.keySet()) {
            if (str3 == null) {
                str2 = "";
            } else {
                StringBuilder sb3 = new StringBuilder();
                sb3.append(str3);
                sb3.append(" : ");
                str2 = sb3.toString();
            }
            StringBuilder sb4 = new StringBuilder();
            sb4.append(str2);
            sb4.append((String) headers.get(str3));
            linkedList2.add(sb4.toString());
        }
        String responseMessage = iConnection.getResponseMessage();
        String streamToString = DefaultHttpProvider.streamToString(iConnection.getInputStream());
        String str4 = (String) headers.get("Content-Type");
        if (str4 == null || !str4.contains(WebRequestHandler.HEADER_ACCEPT_JSON)) {
            e = null;
        } else {
            try {
                oneDriveErrorResponse2 = (OneDriveErrorResponse) iSerializer.deserializeObject(streamToString, OneDriveErrorResponse.class);
                e = null;
            } catch (Exception e2) {
                e = e2;
            }
        }
        if (oneDriveErrorResponse2 == null) {
            OneDriveErrorResponse oneDriveErrorResponse3 = new OneDriveErrorResponse();
            oneDriveErrorResponse3.error = new OneDriveError();
            oneDriveErrorResponse3.error.code = "Unable to parse error response message";
            OneDriveError oneDriveError = oneDriveErrorResponse3.error;
            StringBuilder sb5 = new StringBuilder();
            sb5.append("Raw error: ");
            sb5.append(streamToString);
            oneDriveError.message = sb5.toString();
            if (e != null) {
                oneDriveErrorResponse3.error.innererror = new OneDriveInnerError();
                oneDriveErrorResponse3.error.innererror.code = e.getMessage();
            }
            oneDriveErrorResponse = oneDriveErrorResponse3;
        } else {
            oneDriveErrorResponse = oneDriveErrorResponse2;
        }
        if (responseCode == 500) {
            OneDriveFatalServiceException oneDriveFatalServiceException = new OneDriveFatalServiceException(requestMethod, url, linkedList, str, responseCode, responseMessage, linkedList2, oneDriveErrorResponse);
            return oneDriveFatalServiceException;
        }
        OneDriveServiceException oneDriveServiceException = new OneDriveServiceException(requestMethod, url, linkedList, str, responseCode, responseMessage, linkedList2, oneDriveErrorResponse);
        return oneDriveServiceException;
    }
}
