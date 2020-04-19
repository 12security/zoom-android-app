package com.microsoft.services.msa;

import android.app.Activity;
import android.util.Log;

enum ScreenSize {
    SMALL {
        public DeviceType getDeviceType() {
            return DeviceType.PHONE;
        }
    },
    NORMAL {
        public DeviceType getDeviceType() {
            return DeviceType.PHONE;
        }
    },
    LARGE {
        public DeviceType getDeviceType() {
            return DeviceType.TABLET;
        }
    },
    XLARGE {
        public DeviceType getDeviceType() {
            return DeviceType.TABLET;
        }
    };
    
    private static final int SCREENLAYOUT_SIZE_XLARGE = 4;

    public abstract DeviceType getDeviceType();

    public static ScreenSize determineScreenSize(Activity activity) {
        switch (activity.getResources().getConfiguration().screenLayout & 15) {
            case 1:
                return SMALL;
            case 2:
                return NORMAL;
            case 3:
                return LARGE;
            case 4:
                return XLARGE;
            default:
                Log.d("Live SDK ScreenSize", "Unable to determine ScreenSize. A Normal ScreenSize will be returned.");
                return NORMAL;
        }
    }
}
