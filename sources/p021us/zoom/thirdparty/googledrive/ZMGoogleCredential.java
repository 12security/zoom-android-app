package p021us.zoom.thirdparty.googledrive;

import androidx.annotation.Nullable;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.CredentialRefreshListener;
import com.google.api.client.auth.oauth2.TokenErrorResponse;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.ZMAsyncTask;

/* renamed from: us.zoom.thirdparty.googledrive.ZMGoogleCredential */
public class ZMGoogleCredential implements CredentialRefreshListener {
    private static final String USER_ID = "googleAuthClientId";
    /* access modifiers changed from: private */
    public GoogleAuthorizationCodeFlow mAuthFlow;
    /* access modifiers changed from: private */
    public CredentialListener mListener;
    /* access modifiers changed from: private */
    public List<CredentialGettingTask> mTasks = new ArrayList();

    /* renamed from: us.zoom.thirdparty.googledrive.ZMGoogleCredential$CredentialGettingTask */
    private class CredentialGettingTask extends ZMAsyncTask<Void, Void, Runnable> {
        private boolean bCancel = false;
        private String mAuthCode;
        /* access modifiers changed from: private */
        public Credential mCredential;
        private String mRedirectUrl;

        /* renamed from: us.zoom.thirdparty.googledrive.ZMGoogleCredential$CredentialGettingTask$onCancelRunnable */
        private class onCancelRunnable implements Runnable {
            private onCancelRunnable() {
            }

            public void run() {
                if (ZMGoogleCredential.this.mListener != null) {
                    ZMGoogleCredential.this.mListener.onCancel();
                }
            }
        }

        /* renamed from: us.zoom.thirdparty.googledrive.ZMGoogleCredential$CredentialGettingTask$onCompeletedRunnable */
        private class onCompeletedRunnable implements Runnable {
            private onCompeletedRunnable() {
            }

            public void run() {
                if (ZMGoogleCredential.this.mListener != null) {
                    ZMGoogleCredential.this.mListener.onCompeleted(CredentialGettingTask.this.mCredential);
                }
            }
        }

        /* renamed from: us.zoom.thirdparty.googledrive.ZMGoogleCredential$CredentialGettingTask$onErrorRunnable */
        private class onErrorRunnable implements Runnable {
            private Exception exception;

            public onErrorRunnable(Exception exc) {
                this.exception = exc;
            }

            public void run() {
                if (ZMGoogleCredential.this.mListener != null) {
                    ZMGoogleCredential.this.mListener.onError(this.exception);
                }
            }
        }

        public CredentialGettingTask(String str, String str2) {
            this.mAuthCode = str;
            this.mRedirectUrl = str2;
        }

        /* access modifiers changed from: protected */
        public void onCancelled() {
            super.onCancelled();
            this.bCancel = true;
        }

        /* access modifiers changed from: protected */
        public Runnable doInBackground(Void... voidArr) {
            if (StringUtil.isEmptyOrNull(this.mAuthCode) || ZMGoogleCredential.this.mAuthFlow == null) {
                return new onErrorRunnable(new Exception("The parameter is invalid!"));
            }
            if (this.bCancel) {
                return new onCancelRunnable();
            }
            try {
                Credential loadCredential = ZMGoogleCredential.this.mAuthFlow.loadCredential(ZMGoogleCredential.USER_ID);
                if (loadCredential != null) {
                    this.mCredential = loadCredential;
                    return new onCompeletedRunnable();
                }
            } catch (IOException unused) {
            }
            GoogleAuthorizationCodeTokenRequest newTokenRequest = ZMGoogleCredential.this.mAuthFlow.newTokenRequest(this.mAuthCode);
            newTokenRequest.setRedirectUri(this.mRedirectUrl);
            try {
                this.mCredential = ZMGoogleCredential.this.mAuthFlow.createAndStoreCredential(newTokenRequest.execute(), ZMGoogleCredential.USER_ID);
                if (this.bCancel) {
                    return new onCancelRunnable();
                }
                return new onCompeletedRunnable();
            } catch (IOException unused2) {
                if (this.bCancel) {
                    return new onCancelRunnable();
                }
                return new onCompeletedRunnable();
            }
        }

        /* access modifiers changed from: protected */
        public void onPostExecute(Runnable runnable) {
            ZMGoogleCredential.this.mTasks.remove(this);
            runnable.run();
        }
    }

    /* renamed from: us.zoom.thirdparty.googledrive.ZMGoogleCredential$CredentialListener */
    public interface CredentialListener {
        void onCancel();

        void onCompeleted(Credential credential);

        void onError(Exception exc);

        void onRefreshToken(Credential credential, Credential credential2);

        void onRefreshTokenError(Credential credential, String str);
    }

    private native Object createAuthorFlowImpl(HttpTransport httpTransport, JsonFactory jsonFactory, Collection<String> collection, CredentialRefreshListener credentialRefreshListener, boolean z);

    public ZMGoogleCredential(String[] strArr, CredentialListener credentialListener, boolean z) {
        this.mListener = credentialListener;
        this.mAuthFlow = (GoogleAuthorizationCodeFlow) createAuthorFlowImpl(AndroidHttp.newCompatibleTransport(), AndroidJsonFactory.getDefaultInstance(), Arrays.asList(strArr), this, z);
    }

    public void onTokenResponse(Credential credential, TokenResponse tokenResponse) throws IOException {
        if (credential == null) {
            CredentialListener credentialListener = this.mListener;
            if (credentialListener != null) {
                credentialListener.onRefreshTokenError(null, null);
            }
            return;
        }
        Credential createAndStoreCredential = createAndStoreCredential(tokenResponse);
        if (createAndStoreCredential == null) {
            CredentialListener credentialListener2 = this.mListener;
            if (credentialListener2 != null) {
                credentialListener2.onRefreshTokenError(credential, null);
            }
            return;
        }
        CredentialListener credentialListener3 = this.mListener;
        if (credentialListener3 != null) {
            credentialListener3.onRefreshToken(credential, createAndStoreCredential);
        }
    }

    public void onTokenErrorResponse(Credential credential, TokenErrorResponse tokenErrorResponse) throws IOException {
        CredentialListener credentialListener = this.mListener;
        if (credentialListener == null) {
            return;
        }
        if (tokenErrorResponse != null) {
            credentialListener.onRefreshTokenError(credential, tokenErrorResponse.getError());
        } else {
            credentialListener.onRefreshTokenError(credential, null);
        }
    }

    @Nullable
    private Credential createAndStoreCredential(TokenResponse tokenResponse) throws IOException {
        if (tokenResponse == null) {
            return null;
        }
        return this.mAuthFlow.createAndStoreCredential(tokenResponse, USER_ID);
    }

    public void exchangeCode(String str, String str2) {
        if (!StringUtil.isEmptyOrNull(str)) {
            cancel();
            CredentialGettingTask credentialGettingTask = new CredentialGettingTask(str, str2);
            this.mTasks.add(credentialGettingTask);
            credentialGettingTask.execute((Params[]) new Void[0]);
        }
    }

    public void cancel() {
        for (CredentialGettingTask cancel : this.mTasks) {
            cancel.cancel(true);
        }
        this.mTasks.clear();
    }
}
