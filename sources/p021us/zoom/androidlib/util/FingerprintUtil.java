package p021us.zoom.androidlib.util;

import android.app.KeyguardManager;
import android.hardware.fingerprint.FingerprintManager;
import android.hardware.fingerprint.FingerprintManager.AuthenticationCallback;
import android.hardware.fingerprint.FingerprintManager.AuthenticationResult;
import android.os.CancellationSignal;
import androidx.annotation.RequiresApi;
import p021us.zoom.androidlib.app.ZMActivity;

/* renamed from: us.zoom.androidlib.util.FingerprintUtil */
public class FingerprintUtil {
    private ZMActivity mActivity;
    private CancellationSignal mCancellationSignal;
    private FingerprintManager mFingerprintManager;
    private KeyguardManager mKeyManager = ((KeyguardManager) this.mActivity.getSystemService("keyguard"));

    /* renamed from: us.zoom.androidlib.util.FingerprintUtil$IFingerprintResultListener */
    public interface IFingerprintResultListener {
        void onAuthenticateError(int i, CharSequence charSequence);

        void onAuthenticateFailed();

        void onAuthenticateHelp(int i, CharSequence charSequence);

        void onAuthenticateStart();

        void onAuthenticateSucceeded(AuthenticationResult authenticationResult);

        void onInSecurity();

        void onNoEnroll();

        void onNoHardwareDetected();

        void onSupport();
    }

    @RequiresApi(api = 23)
    public FingerprintUtil(ZMActivity zMActivity) {
        this.mActivity = zMActivity;
        this.mFingerprintManager = (FingerprintManager) zMActivity.getSystemService(FingerprintManager.class);
    }

    @RequiresApi(api = 23)
    public void callFingerPrintVerify(final IFingerprintResultListener iFingerprintResultListener) {
        if (!isHardwareDetected()) {
            if (iFingerprintResultListener != null) {
                iFingerprintResultListener.onNoHardwareDetected();
            }
        } else if (!isHasEnrolledFingerprints()) {
            if (iFingerprintResultListener != null) {
                iFingerprintResultListener.onNoEnroll();
            }
        } else if (!isKeyguardSecure()) {
            if (iFingerprintResultListener != null) {
                iFingerprintResultListener.onInSecurity();
            }
        } else {
            if (iFingerprintResultListener != null) {
                iFingerprintResultListener.onSupport();
            }
            if (iFingerprintResultListener != null) {
                iFingerprintResultListener.onAuthenticateStart();
            }
            if (this.mCancellationSignal == null) {
                this.mCancellationSignal = new CancellationSignal();
            }
            try {
                this.mFingerprintManager.authenticate(null, this.mCancellationSignal, 0, new AuthenticationCallback() {
                    public void onAuthenticationError(int i, CharSequence charSequence) {
                        IFingerprintResultListener iFingerprintResultListener = iFingerprintResultListener;
                        if (iFingerprintResultListener != null) {
                            iFingerprintResultListener.onAuthenticateError(i, charSequence);
                        }
                    }

                    public void onAuthenticationFailed() {
                        IFingerprintResultListener iFingerprintResultListener = iFingerprintResultListener;
                        if (iFingerprintResultListener != null) {
                            iFingerprintResultListener.onAuthenticateFailed();
                        }
                    }

                    public void onAuthenticationHelp(int i, CharSequence charSequence) {
                        IFingerprintResultListener iFingerprintResultListener = iFingerprintResultListener;
                        if (iFingerprintResultListener != null) {
                            iFingerprintResultListener.onAuthenticateHelp(i, charSequence);
                        }
                    }

                    public void onAuthenticationSucceeded(AuthenticationResult authenticationResult) {
                        IFingerprintResultListener iFingerprintResultListener = iFingerprintResultListener;
                        if (iFingerprintResultListener != null) {
                            iFingerprintResultListener.onAuthenticateSucceeded(authenticationResult);
                        }
                    }
                }, null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @RequiresApi(api = 23)
    public boolean isSupportFingerprint() {
        return isHardwareDetected() && isKeyguardSecure() && isHasEnrolledFingerprints();
    }

    @RequiresApi(api = 23)
    private boolean isHasEnrolledFingerprints() {
        try {
            return this.mFingerprintManager.hasEnrolledFingerprints();
        } catch (Exception unused) {
            return false;
        }
    }

    @RequiresApi(api = 23)
    public boolean isHardwareDetected() {
        try {
            return this.mFingerprintManager.isHardwareDetected();
        } catch (Exception unused) {
            return false;
        }
    }

    @RequiresApi(api = 16)
    private boolean isKeyguardSecure() {
        try {
            return this.mKeyManager.isKeyguardSecure();
        } catch (Exception unused) {
            return false;
        }
    }

    @RequiresApi(api = 16)
    public void cancelAuthenticate() {
        CancellationSignal cancellationSignal = this.mCancellationSignal;
        if (cancellationSignal != null) {
            cancellationSignal.cancel();
            this.mCancellationSignal = null;
        }
    }

    @RequiresApi(api = 16)
    public void onDestroy() {
        cancelAuthenticate();
        this.mKeyManager = null;
        this.mFingerprintManager = null;
    }
}
