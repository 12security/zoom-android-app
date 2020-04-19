package com.microsoft.services.msa;

import com.microsoft.services.msa.OAuth.DisplayType;

enum DeviceType {
    PHONE {
        public DisplayType getDisplayParameter() {
            return DisplayType.ANDROID_PHONE;
        }
    },
    TABLET {
        public DisplayType getDisplayParameter() {
            return DisplayType.ANDROID_TABLET;
        }
    };

    public abstract DisplayType getDisplayParameter();
}
