package com.microsoft.aad.adal;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

final class TelemetryUtils {
    static final Set<String> GDPR_FILTERED_FIELDS = new HashSet();
    private static final String TAG = TelemetryUtils.class.getSimpleName();

    static class CliTelemInfo implements Serializable {
        private String mRefreshTokenAge;
        private String mServerErrorCode;
        private String mServerSubErrorCode;
        private String mSpeRing;
        private String mVersion;

        CliTelemInfo() {
        }

        /* access modifiers changed from: 0000 */
        public String getVersion() {
            return this.mVersion;
        }

        /* access modifiers changed from: 0000 */
        public void setVersion(String str) {
            this.mVersion = str;
        }

        /* access modifiers changed from: 0000 */
        public String getServerErrorCode() {
            return this.mServerErrorCode;
        }

        /* access modifiers changed from: 0000 */
        public void setServerErrorCode(String str) {
            this.mServerErrorCode = str;
        }

        /* access modifiers changed from: 0000 */
        public String getServerSubErrorCode() {
            return this.mServerSubErrorCode;
        }

        /* access modifiers changed from: 0000 */
        public void setServerSubErrorCode(String str) {
            this.mServerSubErrorCode = str;
        }

        /* access modifiers changed from: 0000 */
        public String getRefreshTokenAge() {
            return this.mRefreshTokenAge;
        }

        /* access modifiers changed from: 0000 */
        public void setRefreshTokenAge(String str) {
            this.mRefreshTokenAge = str;
        }

        /* access modifiers changed from: 0000 */
        public String getSpeRing() {
            return this.mSpeRing;
        }

        /* access modifiers changed from: 0000 */
        public void setSpeRing(String str) {
            this.mSpeRing = str;
        }
    }

    static {
        initializeGdprFilteredFields();
    }

    private TelemetryUtils() {
    }

    private static void initializeGdprFilteredFields() {
        GDPR_FILTERED_FIELDS.addAll(Arrays.asList(new String[]{"Microsoft.ADAL.login_hint", "Microsoft.ADAL.user_id", "Microsoft.ADAL.tenant_id"}));
    }

    static CliTelemInfo parseXMsCliTelemHeader(String str) {
        if (StringExtensions.isNullOrBlank(str)) {
            return null;
        }
        String[] split = str.split(PreferencesConstants.COOKIE_DELIMITER);
        if (split.length == 0) {
            Logger.m239w(TAG, "SPE Ring header missing version field.", null, ADALError.X_MS_CLITELEM_VERSION_UNRECOGNIZED);
            return null;
        }
        String str2 = split[0];
        CliTelemInfo cliTelemInfo = new CliTelemInfo();
        cliTelemInfo.setVersion(str2);
        if (!str2.equals("1")) {
            StringBuilder sb = new StringBuilder();
            sb.append("Header version: ");
            sb.append(str2);
            Logger.m239w(TAG, "Unexpected header version. ", sb.toString(), ADALError.X_MS_CLITELEM_VERSION_UNRECOGNIZED);
            return null;
        } else if (!Pattern.compile("^[1-9]+\\.?[0-9|\\.]*,[0-9|\\.]*,[0-9|\\.]*,[^,]*[0-9\\.]*,[^,]*$").matcher(str).matches()) {
            Logger.m239w(TAG, "", "", ADALError.X_MS_CLITELEM_MALFORMED);
            return null;
        } else {
            String[] split2 = str.split(PreferencesConstants.COOKIE_DELIMITER, 5);
            cliTelemInfo.setServerErrorCode(split2[1]);
            cliTelemInfo.setServerSubErrorCode(split2[2]);
            cliTelemInfo.setRefreshTokenAge(split2[3]);
            cliTelemInfo.setSpeRing(split2[4]);
            return cliTelemInfo;
        }
    }
}
