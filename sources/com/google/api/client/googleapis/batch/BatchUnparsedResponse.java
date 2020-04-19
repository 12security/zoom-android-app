package com.google.api.client.googleapis.batch;

import com.google.api.client.http.BackOffPolicy;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpContent;
import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpStatusCodes;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.HttpUnsuccessfulResponseHandler;
import com.google.api.client.http.LowLevelHttpRequest;
import com.google.api.client.http.LowLevelHttpResponse;
import com.google.api.client.testing.http.HttpTesting;
import com.google.api.client.util.ByteStreams;
import com.zipow.videobox.view.p014mm.message.FontStyleHelper;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

final class BatchUnparsedResponse {
    boolean backOffRequired;
    private final String boundary;
    private int contentId = 0;
    boolean hasNext = true;
    private final InputStream inputStream;
    private final List<RequestInfo<?, ?>> requestInfos;
    private final boolean retryAllowed;
    List<RequestInfo<?, ?>> unsuccessfulRequestInfos = new ArrayList();

    private static class FakeLowLevelHttpRequest extends LowLevelHttpRequest {
        private List<String> headerNames;
        private List<String> headerValues;
        private InputStream partContent;
        private int statusCode;

        public void addHeader(String str, String str2) {
        }

        FakeLowLevelHttpRequest(InputStream inputStream, int i, List<String> list, List<String> list2) {
            this.partContent = inputStream;
            this.statusCode = i;
            this.headerNames = list;
            this.headerValues = list2;
        }

        public LowLevelHttpResponse execute() {
            return new FakeLowLevelHttpResponse(this.partContent, this.statusCode, this.headerNames, this.headerValues);
        }
    }

    private static class FakeLowLevelHttpResponse extends LowLevelHttpResponse {
        private List<String> headerNames = new ArrayList();
        private List<String> headerValues = new ArrayList();
        private InputStream partContent;
        private int statusCode;

        public String getContentEncoding() {
            return null;
        }

        public long getContentLength() {
            return 0;
        }

        public String getContentType() {
            return null;
        }

        public String getReasonPhrase() {
            return null;
        }

        public String getStatusLine() {
            return null;
        }

        FakeLowLevelHttpResponse(InputStream inputStream, int i, List<String> list, List<String> list2) {
            this.partContent = inputStream;
            this.statusCode = i;
            this.headerNames = list;
            this.headerValues = list2;
        }

        public InputStream getContent() {
            return this.partContent;
        }

        public int getStatusCode() {
            return this.statusCode;
        }

        public int getHeaderCount() {
            return this.headerNames.size();
        }

        public String getHeaderName(int i) {
            return (String) this.headerNames.get(i);
        }

        public String getHeaderValue(int i) {
            return (String) this.headerValues.get(i);
        }
    }

    private static class FakeResponseHttpTransport extends HttpTransport {
        private List<String> headerNames;
        private List<String> headerValues;
        private InputStream partContent;
        private int statusCode;

        FakeResponseHttpTransport(int i, InputStream inputStream, List<String> list, List<String> list2) {
            this.statusCode = i;
            this.partContent = inputStream;
            this.headerNames = list;
            this.headerValues = list2;
        }

        /* access modifiers changed from: protected */
        public LowLevelHttpRequest buildRequest(String str, String str2) {
            return new FakeLowLevelHttpRequest(this.partContent, this.statusCode, this.headerNames, this.headerValues);
        }
    }

    BatchUnparsedResponse(InputStream inputStream2, String str, List<RequestInfo<?, ?>> list, boolean z) throws IOException {
        this.boundary = str;
        this.requestInfos = list;
        this.retryAllowed = z;
        this.inputStream = inputStream2;
        checkForFinalBoundary(readLine());
    }

    /* access modifiers changed from: 0000 */
    public void parseNextResponse() throws IOException {
        String readLine;
        String readLine2;
        InputStream inputStream2;
        String readRawLine;
        this.contentId++;
        do {
            readLine = readLine();
            if (readLine == null) {
                break;
            }
        } while (!readLine.equals(""));
        int parseInt = Integer.parseInt(readLine().split(OAuth.SCOPE_DELIMITER)[1]);
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        long j = -1;
        while (true) {
            readLine2 = readLine();
            if (readLine2 == null || readLine2.equals("")) {
                int i = (j > -1 ? 1 : (j == -1 ? 0 : -1));
            } else {
                String[] split = readLine2.split(": ", 2);
                String str = split[0];
                String str2 = split[1];
                arrayList.add(str);
                arrayList2.add(str2);
                if ("Content-Length".equalsIgnoreCase(str.trim())) {
                    j = Long.parseLong(str2);
                }
            }
        }
        int i2 = (j > -1 ? 1 : (j == -1 ? 0 : -1));
        if (i2 == 0) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            while (true) {
                readRawLine = readRawLine();
                if (readRawLine == null || readRawLine.startsWith(this.boundary)) {
                    inputStream2 = trimCrlf(byteArrayOutputStream.toByteArray());
                    readLine2 = trimCrlf(readRawLine);
                } else {
                    byteArrayOutputStream.write(readRawLine.getBytes("ISO-8859-1"));
                }
            }
            inputStream2 = trimCrlf(byteArrayOutputStream.toByteArray());
            readLine2 = trimCrlf(readRawLine);
        } else {
            inputStream2 = new FilterInputStream(ByteStreams.limit(this.inputStream, j)) {
                public void close() {
                }
            };
        }
        parseAndCallback((RequestInfo) this.requestInfos.get(this.contentId - 1), parseInt, getFakeResponse(parseInt, inputStream2, arrayList, arrayList2));
        while (true) {
            if (inputStream2.skip(j) <= 0 && inputStream2.read() == -1) {
                break;
            }
        }
        if (i2 != 0) {
            readLine2 = readLine();
        }
        while (readLine2 != null && readLine2.length() == 0) {
            readLine2 = readLine();
        }
        checkForFinalBoundary(readLine2);
    }

    private <T, E> void parseAndCallback(RequestInfo<T, E> requestInfo, int i, HttpResponse httpResponse) throws IOException {
        BatchCallback<T, E> batchCallback = requestInfo.callback;
        HttpHeaders headers = httpResponse.getHeaders();
        HttpUnsuccessfulResponseHandler unsuccessfulResponseHandler = requestInfo.request.getUnsuccessfulResponseHandler();
        BackOffPolicy backOffPolicy = requestInfo.request.getBackOffPolicy();
        boolean z = false;
        this.backOffRequired = false;
        if (!HttpStatusCodes.isSuccess(i)) {
            HttpContent content = requestInfo.request.getContent();
            boolean z2 = this.retryAllowed && (content == null || content.retrySupported());
            boolean handleResponse = unsuccessfulResponseHandler != null ? unsuccessfulResponseHandler.handleResponse(requestInfo.request, httpResponse, z2) : false;
            if (!handleResponse) {
                if (requestInfo.request.handleRedirect(httpResponse.getStatusCode(), httpResponse.getHeaders())) {
                    z = true;
                } else if (z2 && backOffPolicy != null && backOffPolicy.isBackOffRequired(httpResponse.getStatusCode())) {
                    this.backOffRequired = true;
                }
            }
            if (z2 && (handleResponse || this.backOffRequired || z)) {
                this.unsuccessfulRequestInfos.add(requestInfo);
            } else if (batchCallback != null) {
                batchCallback.onFailure(getParsedDataClass(requestInfo.errorClass, httpResponse, requestInfo), headers);
            }
        } else if (batchCallback != null) {
            batchCallback.onSuccess(getParsedDataClass(requestInfo.dataClass, httpResponse, requestInfo), headers);
        }
    }

    private <A, T, E> A getParsedDataClass(Class<A> cls, HttpResponse httpResponse, RequestInfo<T, E> requestInfo) throws IOException {
        if (cls == Void.class) {
            return null;
        }
        return requestInfo.request.getParser().parseAndClose(httpResponse.getContent(), httpResponse.getContentCharset(), cls);
    }

    private HttpResponse getFakeResponse(int i, InputStream inputStream2, List<String> list, List<String> list2) throws IOException {
        HttpRequest buildPostRequest = new FakeResponseHttpTransport(i, inputStream2, list, list2).createRequestFactory().buildPostRequest(new GenericUrl(HttpTesting.SIMPLE_URL), null);
        buildPostRequest.setLoggingEnabled(false);
        buildPostRequest.setThrowExceptionOnExecuteError(false);
        return buildPostRequest.execute();
    }

    private String readRawLine() throws IOException {
        int read = this.inputStream.read();
        if (read == -1) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        while (read != -1) {
            sb.append((char) read);
            if (read == 10) {
                break;
            }
            read = this.inputStream.read();
        }
        return sb.toString();
    }

    private String readLine() throws IOException {
        return trimCrlf(readRawLine());
    }

    private static String trimCrlf(String str) {
        if (str.endsWith("\r\n")) {
            return str.substring(0, str.length() - 2);
        }
        return str.endsWith(FontStyleHelper.SPLITOR) ? str.substring(0, str.length() - 1) : str;
    }

    private static InputStream trimCrlf(byte[] bArr) {
        int length = bArr.length;
        if (length > 0 && bArr[length - 1] == 10) {
            length--;
        }
        if (length > 0 && bArr[length - 1] == 13) {
            length--;
        }
        return new ByteArrayInputStream(bArr, 0, length);
    }

    private void checkForFinalBoundary(String str) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append(this.boundary);
        sb.append("--");
        if (str.equals(sb.toString())) {
            this.hasNext = false;
            this.inputStream.close();
        }
    }
}
