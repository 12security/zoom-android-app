package p021us.zoom.net;

import androidx.annotation.NonNull;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/* renamed from: us.zoom.net.AndroidCertVerifyResult */
public class AndroidCertVerifyResult {
    @NonNull
    private final List<X509Certificate> mCertificateChain;
    private final boolean mIsIssuedByKnownRoot;
    private final int mStatus;

    public AndroidCertVerifyResult(int i, boolean z, @NonNull List<X509Certificate> list) {
        this.mStatus = i;
        this.mIsIssuedByKnownRoot = z;
        this.mCertificateChain = new ArrayList(list);
    }

    public AndroidCertVerifyResult(int i) {
        this.mStatus = i;
        this.mIsIssuedByKnownRoot = false;
        this.mCertificateChain = Collections.emptyList();
    }

    public int getStatus() {
        return this.mStatus;
    }

    public boolean isIssuedByKnownRoot() {
        return this.mIsIssuedByKnownRoot;
    }

    @NonNull
    public byte[][] getCertificateChainEncoded() {
        byte[][] bArr = new byte[this.mCertificateChain.size()][];
        int i = 0;
        while (i < this.mCertificateChain.size()) {
            try {
                bArr[i] = ((X509Certificate) this.mCertificateChain.get(i)).getEncoded();
                i++;
            } catch (CertificateEncodingException unused) {
                return new byte[0][];
            }
        }
        return bArr;
    }
}
