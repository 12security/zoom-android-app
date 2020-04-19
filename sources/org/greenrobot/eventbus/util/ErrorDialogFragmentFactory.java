package org.greenrobot.eventbus.util;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.os.Bundle;

public abstract class ErrorDialogFragmentFactory<T> {
    protected final ErrorDialogConfig config;

    @TargetApi(11)
    public static class Honeycomb extends ErrorDialogFragmentFactory<Fragment> {
        public Honeycomb(ErrorDialogConfig errorDialogConfig) {
            super(errorDialogConfig);
        }

        /* access modifiers changed from: protected */
        public Fragment createErrorFragment(ThrowableFailureEvent throwableFailureEvent, Bundle bundle) {
            org.greenrobot.eventbus.util.ErrorDialogFragments.Honeycomb honeycomb = new org.greenrobot.eventbus.util.ErrorDialogFragments.Honeycomb();
            honeycomb.setArguments(bundle);
            return honeycomb;
        }
    }

    public static class Support extends ErrorDialogFragmentFactory<androidx.fragment.app.Fragment> {
        public Support(ErrorDialogConfig errorDialogConfig) {
            super(errorDialogConfig);
        }

        /* access modifiers changed from: protected */
        public androidx.fragment.app.Fragment createErrorFragment(ThrowableFailureEvent throwableFailureEvent, Bundle bundle) {
            org.greenrobot.eventbus.util.ErrorDialogFragments.Support support = new org.greenrobot.eventbus.util.ErrorDialogFragments.Support();
            support.setArguments(bundle);
            return support;
        }
    }

    /* access modifiers changed from: protected */
    public abstract T createErrorFragment(ThrowableFailureEvent throwableFailureEvent, Bundle bundle);

    protected ErrorDialogFragmentFactory(ErrorDialogConfig errorDialogConfig) {
        this.config = errorDialogConfig;
    }

    /* access modifiers changed from: protected */
    public T prepareErrorFragment(ThrowableFailureEvent throwableFailureEvent, boolean z, Bundle bundle) {
        Bundle bundle2;
        if (throwableFailureEvent.isSuppressErrorUi()) {
            return null;
        }
        if (bundle != null) {
            bundle2 = (Bundle) bundle.clone();
        } else {
            bundle2 = new Bundle();
        }
        if (!bundle2.containsKey(ErrorDialogManager.KEY_TITLE)) {
            bundle2.putString(ErrorDialogManager.KEY_TITLE, getTitleFor(throwableFailureEvent, bundle2));
        }
        if (!bundle2.containsKey(ErrorDialogManager.KEY_MESSAGE)) {
            bundle2.putString(ErrorDialogManager.KEY_MESSAGE, getMessageFor(throwableFailureEvent, bundle2));
        }
        if (!bundle2.containsKey(ErrorDialogManager.KEY_FINISH_AFTER_DIALOG)) {
            bundle2.putBoolean(ErrorDialogManager.KEY_FINISH_AFTER_DIALOG, z);
        }
        if (!bundle2.containsKey(ErrorDialogManager.KEY_EVENT_TYPE_ON_CLOSE) && this.config.defaultEventTypeOnDialogClosed != null) {
            bundle2.putSerializable(ErrorDialogManager.KEY_EVENT_TYPE_ON_CLOSE, this.config.defaultEventTypeOnDialogClosed);
        }
        if (!bundle2.containsKey(ErrorDialogManager.KEY_ICON_ID) && this.config.defaultDialogIconId != 0) {
            bundle2.putInt(ErrorDialogManager.KEY_ICON_ID, this.config.defaultDialogIconId);
        }
        return createErrorFragment(throwableFailureEvent, bundle2);
    }

    /* access modifiers changed from: protected */
    public String getTitleFor(ThrowableFailureEvent throwableFailureEvent, Bundle bundle) {
        return this.config.resources.getString(this.config.defaultTitleId);
    }

    /* access modifiers changed from: protected */
    public String getMessageFor(ThrowableFailureEvent throwableFailureEvent, Bundle bundle) {
        return this.config.resources.getString(this.config.getMessageIdForThrowable(throwableFailureEvent.throwable));
    }
}
