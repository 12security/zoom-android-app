package com.google.api.client.googleapis.media;

import com.google.api.client.http.HttpIOExceptionHandler;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpUnsuccessfulResponseHandler;
import com.google.api.client.util.Beta;
import com.google.api.client.util.Preconditions;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

@Beta
class MediaUploadErrorHandler implements HttpUnsuccessfulResponseHandler, HttpIOExceptionHandler {
    static final Logger LOGGER = Logger.getLogger(MediaUploadErrorHandler.class.getName());
    private final HttpIOExceptionHandler originalIOExceptionHandler;
    private final HttpUnsuccessfulResponseHandler originalUnsuccessfulHandler;
    private final MediaHttpUploader uploader;

    public MediaUploadErrorHandler(MediaHttpUploader mediaHttpUploader, HttpRequest httpRequest) {
        this.uploader = (MediaHttpUploader) Preconditions.checkNotNull(mediaHttpUploader);
        this.originalIOExceptionHandler = httpRequest.getIOExceptionHandler();
        this.originalUnsuccessfulHandler = httpRequest.getUnsuccessfulResponseHandler();
        httpRequest.setIOExceptionHandler(this);
        httpRequest.setUnsuccessfulResponseHandler(this);
    }

    public boolean handleIOException(HttpRequest httpRequest, boolean z) throws IOException {
        HttpIOExceptionHandler httpIOExceptionHandler = this.originalIOExceptionHandler;
        boolean z2 = httpIOExceptionHandler != null && httpIOExceptionHandler.handleIOException(httpRequest, z);
        if (z2) {
            try {
                this.uploader.serverErrorCallback();
            } catch (IOException e) {
                LOGGER.log(Level.WARNING, "exception thrown while calling server callback", e);
            }
        }
        return z2;
    }

    public boolean handleResponse(HttpRequest httpRequest, HttpResponse httpResponse, boolean z) throws IOException {
        HttpUnsuccessfulResponseHandler httpUnsuccessfulResponseHandler = this.originalUnsuccessfulHandler;
        boolean z2 = httpUnsuccessfulResponseHandler != null && httpUnsuccessfulResponseHandler.handleResponse(httpRequest, httpResponse, z);
        if (z2 && z && httpResponse.getStatusCode() / 100 == 5) {
            try {
                this.uploader.serverErrorCallback();
            } catch (IOException e) {
                LOGGER.log(Level.WARNING, "exception thrown while calling server callback", e);
            }
        }
        return z2;
    }
}
