package com.zipow.videobox.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputFilter.LengthFilter;
import android.text.TextWatcher;
import android.text.method.NumberKeyListener;
import android.text.method.ReplacementTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.ShareScreenDialogHelper;
import com.zipow.videobox.ptapp.delegate.PTAppDelegation;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.app.ZMFragment;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.androidlib.widget.ZMAlertDialog;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;
import p021us.zoom.videomeetings.C4558R;

public class ShareScreenDialog extends ZMDialogFragment {
    /* access modifiers changed from: private */
    @NonNull
    public InputFilter[] inputFiltersForMeetingId = {new NumberKeyListener() {
        public int getInputType() {
            return 2;
        }

        /* access modifiers changed from: protected */
        @NonNull
        public char[] getAcceptedChars() {
            return "0123456789".toCharArray();
        }
    }, new LengthFilter(11)};
    /* access modifiers changed from: private */
    @NonNull
    public InputFilter[] inputFiltersForSharingKey = {new NumberKeyListener() {
        public int getInputType() {
            return 4242;
        }

        /* access modifiers changed from: protected */
        @NonNull
        public char[] getAcceptedChars() {
            return "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
        }
    }, new LengthFilter(6)};
    /* access modifiers changed from: private */
    @NonNull
    public String lastText = "";
    /* access modifiers changed from: private */
    public EditText mEtShareID = null;
    private RequestPermissionListener mRequestPermissionListener;
    @Nullable
    private RetainedFragment mRetainedFragment;

    public interface RequestPermissionListener {
        void requestPermission();
    }

    public static class RetainedFragment extends ZMFragment {
        private RequestPermissionListener mRequestPermissionListener;

        public RetainedFragment() {
            setRetainInstance(true);
        }

        public void saveRequestPermissionListener(RequestPermissionListener requestPermissionListener) {
            this.mRequestPermissionListener = requestPermissionListener;
        }

        public RequestPermissionListener restoreRequestPermissionListener() {
            return this.mRequestPermissionListener;
        }
    }

    @NonNull
    public static ShareScreenDialog newInstance() {
        return new ShareScreenDialog();
    }

    public ShareScreenDialog() {
        setCancelable(true);
    }

    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        initRetainedFragment();
    }

    @NonNull
    public Dialog onCreateDialog(Bundle bundle) {
        FragmentActivity activity = getActivity();
        if (activity == null) {
            return createEmptyDialog();
        }
        View inflate = LayoutInflater.from(getActivity()).inflate(C4558R.layout.zm_share_screen, null, false);
        this.mEtShareID = (EditText) inflate.findViewById(C4558R.C4560id.edtShareId);
        final ZMAlertDialog create = new Builder(activity).setTitle(C4558R.string.zm_btn_mm_share_screen_52777).setView(inflate).setNegativeButton(C4558R.string.zm_btn_cancel, (OnClickListener) new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                ShareScreenDialog.this.dismiss();
                ShareScreenDialogHelper instance = ShareScreenDialogHelper.getInstance();
                if (instance.isInputNewParingCode()) {
                    PTAppDelegation.getInstance().stopPresentToRoom(false);
                }
                if (instance.isFinishActivity()) {
                    ZMActivity zMActivity = (ZMActivity) ShareScreenDialog.this.getActivity();
                    if (zMActivity != null) {
                        zMActivity.finish();
                    }
                }
            }
        }).setPositiveButton(C4558R.string.zm_mm_msg_timed_chat_ok_33479, (OnClickListener) new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                ShareScreenDialog.this.presentToRoom();
                ShareScreenDialog.this.dismiss();
            }
        }).create();
        this.mEtShareID.setTransformationMethod(new ReplacementTransformationMethod() {
            @NonNull
            private char[] lower = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
            @NonNull
            private char[] upper = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};

            /* access modifiers changed from: protected */
            @NonNull
            public char[] getOriginal() {
                return this.lower;
            }

            /* access modifiers changed from: protected */
            @NonNull
            public char[] getReplacement() {
                return this.upper;
            }
        });
        this.mEtShareID.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void afterTextChanged(@NonNull Editable editable) {
                Button button = create.getButton(-1);
                String obj = editable.toString();
                if (editable.length() == 0) {
                    ShareScreenDialog.this.mEtShareID.setFilters(ShareScreenDialog.this.inputFiltersForSharingKey);
                    return;
                }
                if (Character.isDigit(obj.charAt(0))) {
                    ShareScreenDialog.this.mEtShareID.setFilters(ShareScreenDialog.this.inputFiltersForMeetingId);
                } else {
                    ShareScreenDialog.this.mEtShareID.setFilters(ShareScreenDialog.this.inputFiltersForSharingKey);
                }
                if (button != null) {
                    if (ShareScreenDialog.isSharingKey(editable.toString()) || ShareScreenDialog.isMeetingId(editable.toString())) {
                        button.setClickable(true);
                        button.setTextColor(ShareScreenDialog.this.getResources().getColor(C4558R.color.zm_highlight_pressed));
                    } else {
                        button.setClickable(false);
                        button.setTextColor(-7829368);
                    }
                }
            }
        });
        if (create != null) {
            create.setCanceledOnTouchOutside(false);
        }
        return create == null ? createEmptyDialog() : create;
    }

    /* access modifiers changed from: private */
    public void presentToRoom() {
        ZMActivity zMActivity = (ZMActivity) getActivity();
        if (zMActivity != null) {
            ShareScreenDialogHelper.getInstance().showWaitingDialog();
            zMActivity.runOnUiThread(new Runnable() {
                public void run() {
                    long j;
                    String str;
                    int i;
                    String obj = ShareScreenDialog.this.mEtShareID.getText().toString();
                    if (!StringUtil.isEmptyOrNull(obj)) {
                        ShareScreenDialog.this.lastText = obj;
                        String str2 = "";
                        if (ShareScreenDialog.isMeetingId(obj)) {
                            str = str2;
                            j = ShareScreenDialog.this.getConfNumber();
                            i = 6;
                        } else {
                            str = obj.toUpperCase();
                            j = 0;
                            i = 5;
                        }
                        PTApp.getInstance().presentToRoom(i, str, j, false);
                    }
                }
            });
        }
    }

    public void onResume() {
        super.onResume();
        Button button = ((ZMAlertDialog) getDialog()).getButton(-1);
        if (button != null) {
            button.setClickable(false);
            button.setTextColor(-7829368);
        }
        this.mEtShareID.setFocusable(true);
        this.mEtShareID.setFocusableInTouchMode(true);
        this.mEtShareID.requestFocus();
        this.mEtShareID.post(new Runnable() {
            public void run() {
                UIUtil.closeSoftKeyboard(ShareScreenDialog.this.getContext(), ShareScreenDialog.this.mEtShareID, 1);
            }
        });
    }

    public void onPause() {
        super.onPause();
        this.lastText = this.mEtShareID.getText().toString();
    }

    public static boolean isMeetingId(@Nullable CharSequence charSequence) {
        return charSequence != null && Character.isDigit(charSequence.charAt(0)) && charSequence.length() >= 9;
    }

    public static boolean isSharingKey(@Nullable CharSequence charSequence) {
        return charSequence != null && !Character.isDigit(charSequence.charAt(0)) && charSequence.length() >= 6;
    }

    public long getConfNumber() {
        String replaceAll = this.mEtShareID.getText().toString().replaceAll("\\s", "");
        if (replaceAll.length() <= 0) {
            return 0;
        }
        try {
            return Long.parseLong(replaceAll);
        } catch (NumberFormatException unused) {
            return 0;
        }
    }

    private void initRetainedFragment() {
        this.mRetainedFragment = getRetainedFragment();
        RetainedFragment retainedFragment = this.mRetainedFragment;
        if (retainedFragment == null) {
            this.mRetainedFragment = new RetainedFragment();
            this.mRetainedFragment.saveRequestPermissionListener(this.mRequestPermissionListener);
            ((ZMActivity) getContext()).getSupportFragmentManager().beginTransaction().add((Fragment) this.mRetainedFragment, RetainedFragment.class.getName()).commit();
            return;
        }
        RequestPermissionListener restoreRequestPermissionListener = retainedFragment.restoreRequestPermissionListener();
        if (restoreRequestPermissionListener != null) {
            this.mRequestPermissionListener = restoreRequestPermissionListener;
        }
    }

    @Nullable
    private RetainedFragment getRetainedFragment() {
        RetainedFragment retainedFragment = this.mRetainedFragment;
        if (retainedFragment != null) {
            return retainedFragment;
        }
        return (RetainedFragment) ((ZMActivity) getContext()).getSupportFragmentManager().findFragmentByTag(RetainedFragment.class.getName());
    }

    public void dismiss() {
        UIUtil.closeSoftKeyboard(getActivity(), this.mEtShareID);
        finishFragment(false);
        super.dismiss();
    }
}
