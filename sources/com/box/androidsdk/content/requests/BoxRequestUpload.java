package com.box.androidsdk.content.requests;

import com.box.androidsdk.content.BoxException;
import com.box.androidsdk.content.listeners.ProgressListener;
import com.box.androidsdk.content.models.BoxJsonObject;
import com.box.androidsdk.content.models.BoxList;
import com.box.androidsdk.content.models.BoxSession;
import com.box.androidsdk.content.requests.BoxRequest;
import com.box.androidsdk.content.requests.BoxRequest.BoxRequestHandler;
import com.box.androidsdk.content.requests.BoxRequest.Methods;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Locale;

public abstract class BoxRequestUpload<E extends BoxJsonObject, R extends BoxRequest<E, R>> extends BoxRequest<E, R> {
    Date mCreatedDate;
    File mFile;
    String mFileName;
    Date mModifiedDate;
    String mSha1;
    InputStream mStream;
    long mUploadSize;

    public BoxRequestUpload(Class<E> cls, InputStream inputStream, String str, BoxSession boxSession) {
        super(cls, str, boxSession);
        this.mRequestMethod = Methods.POST;
        this.mStream = inputStream;
        this.mFileName = "";
    }

    /* access modifiers changed from: protected */
    public void setHeaders(BoxHttpRequest boxHttpRequest) {
        super.setHeaders(boxHttpRequest);
        String str = this.mSha1;
        if (str != null) {
            boxHttpRequest.addHeader("Content-MD5", str);
        }
    }

    /* access modifiers changed from: protected */
    public InputStream getInputStream() throws FileNotFoundException {
        InputStream inputStream = this.mStream;
        if (inputStream != null) {
            return inputStream;
        }
        return new FileInputStream(this.mFile);
    }

    /* access modifiers changed from: protected */
    public BoxRequestMultipart createMultipartRequest() throws IOException, BoxException {
        BoxRequestMultipart boxRequestMultipart = new BoxRequestMultipart(buildUrl(), this.mRequestMethod, this.mListener);
        setHeaders(boxRequestMultipart);
        boxRequestMultipart.setFile(getInputStream(), this.mFileName, this.mUploadSize);
        Date date = this.mCreatedDate;
        if (date != null) {
            boxRequestMultipart.putField("content_created_at", date);
        }
        Date date2 = this.mModifiedDate;
        if (date2 != null) {
            boxRequestMultipart.putField("content_modified_at", date2);
        }
        return boxRequestMultipart;
    }

    /* access modifiers changed from: protected */
    public BoxHttpRequest createHttpRequest() throws IOException, BoxException {
        BoxRequestMultipart createMultipartRequest = createMultipartRequest();
        createMultipartRequest.writeBody(createMultipartRequest.mUrlConnection, this.mListener);
        return createMultipartRequest;
    }

    public E send() throws BoxException {
        BoxHttpResponse boxHttpResponse;
        IOException e;
        InstantiationException e2;
        IllegalAccessException e3;
        BoxException e4;
        BoxRequestHandler requestHandler = getRequestHandler();
        try {
            boxHttpResponse = new BoxHttpResponse(createHttpRequest().getUrlConnection());
            try {
                boxHttpResponse.open();
                logDebug(boxHttpResponse);
                if (requestHandler.isResponseSuccess(boxHttpResponse)) {
                    return (BoxJsonObject) ((BoxList) requestHandler.onResponse(BoxList.class, boxHttpResponse)).toArray()[0];
                }
                throw new BoxException(String.format(Locale.ENGLISH, "An error occurred while sending the request (%s)", new Object[]{Integer.valueOf(boxHttpResponse.getResponseCode())}), boxHttpResponse);
            } catch (IOException e5) {
                e = e5;
                throw handleSendException(requestHandler, boxHttpResponse, e);
            } catch (InstantiationException e6) {
                e2 = e6;
                throw handleSendException(requestHandler, boxHttpResponse, e2);
            } catch (IllegalAccessException e7) {
                e3 = e7;
                throw handleSendException(requestHandler, boxHttpResponse, e3);
            } catch (BoxException e8) {
                e4 = e8;
                throw handleSendException(requestHandler, boxHttpResponse, e4);
            }
        } catch (IOException e9) {
            boxHttpResponse = null;
            e = e9;
            throw handleSendException(requestHandler, boxHttpResponse, e);
        } catch (InstantiationException e10) {
            boxHttpResponse = null;
            e2 = e10;
            throw handleSendException(requestHandler, boxHttpResponse, e2);
        } catch (IllegalAccessException e11) {
            boxHttpResponse = null;
            e3 = e11;
            throw handleSendException(requestHandler, boxHttpResponse, e3);
        } catch (BoxException e12) {
            boxHttpResponse = null;
            e4 = e12;
            throw handleSendException(requestHandler, boxHttpResponse, e4);
        }
    }

    private BoxException handleSendException(BoxRequestHandler boxRequestHandler, BoxHttpResponse boxHttpResponse, Exception exc) throws BoxException {
        BoxException boxException = exc instanceof BoxException ? (BoxException) exc : new BoxException("Couldn't connect to the Box API due to a network error.", (Throwable) exc);
        boxRequestHandler.onException(this, boxHttpResponse, boxException);
        return boxException;
    }

    public R setProgressListener(ProgressListener progressListener) {
        this.mListener = progressListener;
        return this;
    }

    public long getUploadSize() {
        return this.mUploadSize;
    }

    public R setUploadSize(long j) {
        this.mUploadSize = j;
        return this;
    }

    public Date getModifiedDate() {
        return this.mModifiedDate;
    }

    public R setModifiedDate(Date date) {
        this.mModifiedDate = date;
        return this;
    }

    public Date getCreatedDate() {
        return this.mCreatedDate;
    }

    public R setCreatedDate(Date date) {
        this.mCreatedDate = date;
        return this;
    }

    public String getSha1() {
        return this.mSha1;
    }

    public void setSha1(String str) {
        this.mSha1 = str;
    }

    public File getFile() {
        return this.mFile;
    }
}
