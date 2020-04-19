package p021us.zoom.net;

/* renamed from: us.zoom.net.AndroidNetworkLibrary */
class AndroidNetworkLibrary {
    private static final String TAG = "AndroidNetworkLibrary";

    AndroidNetworkLibrary() {
    }

    public static AndroidCertVerifyResult verifyServerCertificates(byte[][] bArr, String str, String str2) {
        try {
            return X509Util.verifyServerCertificates(bArr, str, str2);
        } catch (Throwable unused) {
            return new AndroidCertVerifyResult(-1);
        }
    }
}
