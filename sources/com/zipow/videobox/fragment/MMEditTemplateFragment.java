package com.zipow.videobox.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.zipow.videobox.SimpleActivity;
import com.zipow.videobox.SimpleActivity.ExtListener;
import com.zipow.videobox.ptapp.IMProtos.EditParam;
import com.zipow.videobox.ptapp.IMProtos.FieldsEditParam;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.ZoomMessageTemplateUI;
import com.zipow.videobox.ptapp.ZoomMessageTemplateUI.SimpleZoomMessageTemplateUIListener;
import com.zipow.videobox.ptapp.p013mm.MMFileContentMgr;
import com.zipow.videobox.ptapp.p013mm.ZoomBuddy;
import com.zipow.videobox.ptapp.p013mm.ZoomChatSession;
import com.zipow.videobox.ptapp.p013mm.ZoomMessage;
import com.zipow.videobox.ptapp.p013mm.ZoomMessageTemplate;
import com.zipow.videobox.ptapp.p013mm.ZoomMessenger;
import com.zipow.videobox.tempbean.IMessageTemplateFieldItem;
import com.zipow.videobox.tempbean.IMessageTemplateMessage;
import com.zipow.videobox.tempbean.IZoomMessageTemplate;
import com.zipow.videobox.view.IMAddrBookItem;
import com.zipow.videobox.view.p014mm.MMMessageItem;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.videomeetings.C4558R;

public class MMEditTemplateFragment extends ZMDialogFragment implements OnClickListener, ExtListener {
    public static final String ARGS_EVENT_ID = "event_id";
    public static final String ARGS_FIELD_KEY = "field_key";
    public static final String ARGS_GUID = "guid";
    private static final String ARGS_SESSION_ID = "session_id";
    /* access modifiers changed from: private */
    @Nullable
    public Object editItem;
    /* access modifiers changed from: private */
    public TextView mBtnCancel;
    /* access modifiers changed from: private */
    public TextView mBtnDone;
    private EditText mContent;
    /* access modifiers changed from: private */
    @Nullable
    public String mEventID;
    /* access modifiers changed from: private */
    @Nullable
    public String mFieldKey;
    /* access modifiers changed from: private */
    @Nullable
    public String mGuid;
    @Nullable
    private MMMessageItem mMessageItem;
    /* access modifiers changed from: private */
    @Nullable
    public IZoomMessageTemplate mMessageTemplate;
    /* access modifiers changed from: private */
    @Nullable
    public String mSessionId;
    /* access modifiers changed from: private */
    public TextView mTitle;
    @Nullable
    private SimpleZoomMessageTemplateUIListener mZoomMessageTemplateUIListener;
    private String reqID;

    public boolean onBackPressed() {
        return false;
    }

    public void onKeyboardClosed() {
    }

    public boolean onSearchRequested() {
        return false;
    }

    public boolean onTipLayerTouched() {
        return false;
    }

    public static void showAsFragment(Fragment fragment, String str, String str2, String str3, String str4) {
        showAsFragment(fragment, str, str2, str3, str4, -1);
    }

    public static void showAsFragment(Fragment fragment, @Nullable String str, @Nullable String str2, @Nullable String str3, @Nullable String str4, int i) {
        Bundle bundle = new Bundle();
        String str5 = "session_id";
        if (str == null) {
            str = "";
        }
        bundle.putString(str5, str);
        String str6 = "guid";
        if (str2 == null) {
            str2 = "";
        }
        bundle.putString(str6, str2);
        String str7 = "event_id";
        if (str3 == null) {
            str3 = "";
        }
        bundle.putString(str7, str3);
        String str8 = ARGS_FIELD_KEY;
        if (str4 == null) {
            str4 = "";
        }
        bundle.putString(str8, str4);
        SimpleActivity.show(fragment, MMEditTemplateFragment.class.getName(), bundle, i, 2);
    }

    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        return layoutInflater.inflate(C4558R.layout.zm_mm_edit_template, viewGroup, false);
    }

    public void onViewCreated(@NonNull View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        this.mBtnCancel = (TextView) view.findViewById(C4558R.C4560id.btn_cancel);
        this.mBtnDone = (TextView) view.findViewById(C4558R.C4560id.btn_done);
        this.mTitle = (TextView) view.findViewById(C4558R.C4560id.title);
        this.mContent = (EditText) view.findViewById(C4558R.C4560id.ext_content);
    }

    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        this.mBtnCancel.setOnClickListener(this);
        this.mBtnDone.setOnClickListener(this);
        this.mContent.setOnClickListener(this);
        Bundle arguments = getArguments();
        if (arguments != null) {
            this.mSessionId = arguments.getString("session_id");
            this.mGuid = arguments.getString("guid");
            this.mEventID = arguments.getString("event_id");
            this.mFieldKey = arguments.getString(ARGS_FIELD_KEY);
        }
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            ZoomChatSession sessionById = zoomMessenger.getSessionById(this.mSessionId);
            if (sessionById != null) {
                ZoomMessage messageByXMPPGuid = sessionById.getMessageByXMPPGuid(this.mGuid);
                if (messageByXMPPGuid != null) {
                    ZoomBuddy myself = zoomMessenger.getMyself();
                    if (myself != null) {
                        MMFileContentMgr zoomFileContentMgr = PTApp.getInstance().getZoomFileContentMgr();
                        if (zoomFileContentMgr != null) {
                            this.mMessageItem = MMMessageItem.initWithZoomMessage(messageByXMPPGuid, this.mSessionId, zoomMessenger, sessionById.isGroup(), StringUtil.isSameString(messageByXMPPGuid.getSenderID(), myself.getJid()), getActivity(), IMAddrBookItem.fromZoomBuddy(sessionById.getSessionBuddy()), zoomFileContentMgr);
                            MMMessageItem mMMessageItem = this.mMessageItem;
                            if (mMMessageItem != null) {
                                this.mMessageTemplate = mMMessageItem.template;
                                IZoomMessageTemplate iZoomMessageTemplate = this.mMessageTemplate;
                                if (iZoomMessageTemplate != null) {
                                    this.editItem = iZoomMessageTemplate.findEditItem(this.mEventID, this.mFieldKey);
                                    Object obj = this.editItem;
                                    if (obj != null) {
                                        if (obj instanceof IMessageTemplateMessage) {
                                            this.mContent.setText(((IMessageTemplateMessage) obj).getText());
                                        } else if (obj instanceof IMessageTemplateFieldItem) {
                                            this.mContent.setText(((IMessageTemplateFieldItem) obj).getValue());
                                        }
                                        EditText editText = this.mContent;
                                        editText.setSelection(editText.getText().length());
                                        this.mContent.addTextChangedListener(new TextWatcher() {
                                            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                                            }

                                            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                                            }

                                            public void afterTextChanged(Editable editable) {
                                                if (TextUtils.isEmpty(editable)) {
                                                    MMEditTemplateFragment.this.mBtnDone.setEnabled(false);
                                                    return;
                                                }
                                                String str = "";
                                                if (MMEditTemplateFragment.this.editItem instanceof IMessageTemplateMessage) {
                                                    str = ((IMessageTemplateMessage) MMEditTemplateFragment.this.editItem).getText();
                                                } else if (MMEditTemplateFragment.this.editItem instanceof IMessageTemplateFieldItem) {
                                                    str = ((IMessageTemplateFieldItem) MMEditTemplateFragment.this.editItem).getValue();
                                                }
                                                if (TextUtils.equals(editable, str)) {
                                                    MMEditTemplateFragment.this.mBtnDone.setEnabled(false);
                                                } else {
                                                    MMEditTemplateFragment.this.mBtnDone.setEnabled(true);
                                                }
                                            }
                                        });
                                        this.mZoomMessageTemplateUIListener = new SimpleZoomMessageTemplateUIListener() {
                                            public void Notify_SendPostHttpMessageDone(String str, int i) {
                                                MMEditTemplateFragment.this.handleResult(str, i);
                                                super.Notify_SendPostHttpMessageDone(str, i);
                                            }

                                            public void Notify_SendGetHttpMessageDone(String str, int i) {
                                                MMEditTemplateFragment.this.handleResult(str, i);
                                                super.Notify_SendGetHttpMessageDone(str, i);
                                            }

                                            public void Notify_EditCommandResponse(boolean z, @NonNull EditParam editParam) {
                                                super.Notify_EditCommandResponse(z, editParam);
                                                if (TextUtils.equals(MMEditTemplateFragment.this.mSessionId, editParam.getSessionId()) && TextUtils.equals(MMEditTemplateFragment.this.mGuid, editParam.getMessageId()) && TextUtils.equals(MMEditTemplateFragment.this.mEventID, editParam.getEventId())) {
                                                    if (!z) {
                                                        MMEditTemplateFragment.this.mBtnCancel.setEnabled(true);
                                                        MMEditTemplateFragment.this.mBtnDone.setEnabled(true);
                                                        MMEditTemplateFragment.this.mTitle.setText(MMEditTemplateFragment.this.getResources().getString(C4558R.string.zm_mm_edit_message_19884));
                                                    } else if (MMEditTemplateFragment.this.getActivity() != null && MMEditTemplateFragment.this.mMessageTemplate != null && PTApp.getInstance().getZoomMessageTemplate() != null) {
                                                        Intent intent = new Intent();
                                                        intent.putExtra("guid", MMEditTemplateFragment.this.mGuid);
                                                        MMEditTemplateFragment.this.getActivity().setResult(-1, intent);
                                                        MMEditTemplateFragment.this.getActivity().finish();
                                                    }
                                                }
                                            }

                                            public void Notify_FieldsEditCommandResponse(boolean z, @NonNull FieldsEditParam fieldsEditParam) {
                                                super.Notify_FieldsEditCommandResponse(z, fieldsEditParam);
                                                if (TextUtils.equals(MMEditTemplateFragment.this.mSessionId, fieldsEditParam.getSessionId()) && TextUtils.equals(MMEditTemplateFragment.this.mGuid, fieldsEditParam.getMessageId()) && TextUtils.equals(MMEditTemplateFragment.this.mEventID, fieldsEditParam.getEventId()) && TextUtils.equals(MMEditTemplateFragment.this.mFieldKey, fieldsEditParam.getKey())) {
                                                    if (!z) {
                                                        MMEditTemplateFragment.this.mBtnCancel.setEnabled(true);
                                                        MMEditTemplateFragment.this.mBtnDone.setEnabled(true);
                                                        MMEditTemplateFragment.this.mTitle.setText(MMEditTemplateFragment.this.getResources().getString(C4558R.string.zm_mm_edit_message_19884));
                                                    } else if (MMEditTemplateFragment.this.getActivity() != null && MMEditTemplateFragment.this.mMessageTemplate != null && PTApp.getInstance().getZoomMessageTemplate() != null) {
                                                        Intent intent = new Intent();
                                                        intent.putExtra("guid", MMEditTemplateFragment.this.mGuid);
                                                        MMEditTemplateFragment.this.getActivity().setResult(-1, intent);
                                                        MMEditTemplateFragment.this.getActivity().finish();
                                                    }
                                                }
                                            }
                                        };
                                        ZoomMessageTemplateUI.getInstance().addListener(this.mZoomMessageTemplateUIListener);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void handleResult(String str, int i) {
        if (!TextUtils.isEmpty(str) && TextUtils.equals(str, this.reqID)) {
            if (i != 0) {
                this.mBtnCancel.setEnabled(true);
                this.mBtnDone.setEnabled(true);
                this.mTitle.setText(getResources().getString(C4558R.string.zm_mm_edit_message_19884));
            } else if (getActivity() != null && this.mMessageTemplate != null && PTApp.getInstance().getZoomMessageTemplate() != null) {
                Intent intent = new Intent();
                intent.putExtra("guid", this.mGuid);
                getActivity().setResult(-1, intent);
                getActivity().finish();
            }
        }
    }

    public void onDestroy() {
        ZoomMessageTemplateUI.getInstance().removeListener(this.mZoomMessageTemplateUIListener);
        super.onDestroy();
    }

    public void onClick(@NonNull View view) {
        int id = view.getId();
        if (id == C4558R.C4560id.btn_cancel) {
            dismiss();
        } else if (id == C4558R.C4560id.btn_done) {
            onClickDone();
        }
    }

    private void onClickDone() {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null && zoomMessenger.getSessionById(this.mSessionId) != null && this.mMessageItem != null) {
            ZoomMessageTemplate zoomMessageTemplate = PTApp.getInstance().getZoomMessageTemplate();
            if (zoomMessageTemplate != null) {
                Object obj = this.editItem;
                if (obj != null && this.mContent != null) {
                    String str = obj instanceof IMessageTemplateMessage ? ((IMessageTemplateMessage) obj).getText() : obj instanceof IMessageTemplateFieldItem ? ((IMessageTemplateFieldItem) obj).getValue() : "";
                    if (!TextUtils.equals(str, this.mContent.getText()) && !TextUtils.isEmpty(this.mContent.getText())) {
                        Object obj2 = this.editItem;
                        boolean z = obj2 instanceof IMessageTemplateMessage ? zoomMessageTemplate.sendEditCommand(this.mSessionId, this.mGuid, this.mEventID, str, this.mContent.getText().toString()) : obj2 instanceof IMessageTemplateFieldItem ? zoomMessageTemplate.sendFieldsEditCommand(this.mSessionId, this.mGuid, this.mEventID, this.mFieldKey, str, this.mContent.getText().toString()) : false;
                        if (z && getActivity() != null) {
                            this.mBtnDone.setEnabled(false);
                            this.mBtnCancel.setEnabled(false);
                            this.mTitle.setText(getResources().getString(C4558R.string.zm_mm_edit_message_saving_19884));
                            UIUtil.closeSoftKeyboard(getActivity(), this.mContent);
                        }
                    }
                }
            }
        }
    }

    public void dismiss() {
        UIUtil.closeSoftKeyboard(getActivity(), getView());
        finishFragment(0);
    }

    public void onKeyboardOpen() {
        if (!this.mContent.hasFocus()) {
            this.mContent.requestFocus();
        }
    }
}
