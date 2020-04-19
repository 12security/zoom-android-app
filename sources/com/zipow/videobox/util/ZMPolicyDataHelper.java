package com.zipow.videobox.util;

import com.zipow.videobox.mainboard.Mainboard;

public class ZMPolicyDataHelper {
    private static final String TAG = "ZMPolicyDataHelper";
    private static ZMPolicyDataHelper instance;

    private static class BaseQueryResult {
        private boolean isMandatory;
        private boolean isManual;
        private boolean success;

        BaseQueryResult(boolean z, boolean z2, boolean z3) {
            this.success = z;
            this.isMandatory = z2;
            this.isManual = z3;
        }

        public boolean isSuccess() {
            return this.success;
        }

        public boolean isMandatory() {
            return this.isMandatory;
        }

        public boolean isManual() {
            return this.isManual;
        }
    }

    public static class BooleanQueryResult extends BaseQueryResult {
        private boolean result;

        public /* bridge */ /* synthetic */ boolean isMandatory() {
            return super.isMandatory();
        }

        public /* bridge */ /* synthetic */ boolean isManual() {
            return super.isManual();
        }

        public /* bridge */ /* synthetic */ boolean isSuccess() {
            return super.isSuccess();
        }

        public BooleanQueryResult(boolean z, boolean z2, boolean z3, boolean z4) {
            super(z, z2, z3);
            this.result = z4;
        }

        public boolean getResult() {
            return this.result;
        }
    }

    public static class IntQueryResult extends BaseQueryResult {
        private int result;

        public /* bridge */ /* synthetic */ boolean isMandatory() {
            return super.isMandatory();
        }

        public /* bridge */ /* synthetic */ boolean isManual() {
            return super.isManual();
        }

        public /* bridge */ /* synthetic */ boolean isSuccess() {
            return super.isSuccess();
        }

        IntQueryResult(boolean z, boolean z2, boolean z3, int i) {
            super(z, z2, z3);
            this.result = i;
        }

        public int getResult() {
            return this.result;
        }
    }

    public static class StringQueryResult extends BaseQueryResult {
        private String result;

        public /* bridge */ /* synthetic */ boolean isMandatory() {
            return super.isMandatory();
        }

        public /* bridge */ /* synthetic */ boolean isManual() {
            return super.isManual();
        }

        public /* bridge */ /* synthetic */ boolean isSuccess() {
            return super.isSuccess();
        }

        StringQueryResult(boolean z, boolean z2, boolean z3, String str) {
            super(z, z2, z3);
            this.result = str;
        }

        public String getResult() {
            return this.result;
        }
    }

    private native Object queryBooleanPolicyImpl(int i);

    private native Object queryIntPolicyImpl(int i);

    private native Object queryStringPolicyImpl(int i);

    private native boolean setBooleanValueImpl(int i, boolean z);

    private native boolean setIntValueImpl(int i, int i2);

    private native boolean setStringValueImpl(int i, String str);

    private ZMPolicyDataHelper() {
    }

    public static ZMPolicyDataHelper getInstance() {
        if (instance == null) {
            instance = new ZMPolicyDataHelper();
        }
        return instance;
    }

    public BooleanQueryResult queryBooleanPolicy(int i) {
        if (!isInitialForMainboard()) {
            return new BooleanQueryResult(false, false, false, false);
        }
        Object queryBooleanPolicyImpl = queryBooleanPolicyImpl(i);
        if (queryBooleanPolicyImpl instanceof BooleanQueryResult) {
            return (BooleanQueryResult) queryBooleanPolicyImpl;
        }
        return new BooleanQueryResult(false, false, false, false);
    }

    public IntQueryResult queryIntPolicy(int i) {
        if (!isInitialForMainboard()) {
            return new IntQueryResult(false, false, false, 0);
        }
        Object queryIntPolicyImpl = queryIntPolicyImpl(i);
        if (queryIntPolicyImpl instanceof IntQueryResult) {
            return (IntQueryResult) queryIntPolicyImpl;
        }
        return new IntQueryResult(false, false, false, 0);
    }

    public StringQueryResult queryStringPolicy(int i) {
        if (!isInitialForMainboard()) {
            return new StringQueryResult(false, false, false, "");
        }
        Object queryStringPolicyImpl = queryStringPolicyImpl(i);
        if (queryStringPolicyImpl instanceof StringQueryResult) {
            return (StringQueryResult) queryStringPolicyImpl;
        }
        return new StringQueryResult(false, false, false, "");
    }

    public boolean setBooleanValue(int i, boolean z) {
        if (!isInitialForMainboard()) {
            return false;
        }
        return setBooleanValueImpl(i, z);
    }

    public boolean setIntValue(int i, int i2) {
        if (!isInitialForMainboard()) {
            return false;
        }
        return setIntValueImpl(i, i2);
    }

    public boolean setStringValue(int i, String str) {
        if (!isInitialForMainboard()) {
            return false;
        }
        return setStringValueImpl(i, str);
    }

    private boolean isInitialForMainboard() {
        Mainboard mainboard = Mainboard.getMainboard();
        return mainboard != null && mainboard.isInitialized();
    }
}
