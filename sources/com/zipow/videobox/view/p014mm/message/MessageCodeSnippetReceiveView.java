package com.zipow.videobox.view.p014mm.message;

import android.content.Context;
import android.graphics.Rect;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.gson.Gson;
import com.zipow.videobox.fragment.ZMCodeViewFragment;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.ZoomMessengerUI;
import com.zipow.videobox.ptapp.ZoomMessengerUI.IZoomMessengerUIListener;
import com.zipow.videobox.ptapp.ZoomMessengerUI.SimpleZoomMessengerUIListener;
import com.zipow.videobox.ptapp.p013mm.BuddyNameUtil;
import com.zipow.videobox.ptapp.p013mm.ZoomBuddy;
import com.zipow.videobox.ptapp.p013mm.ZoomChatSession;
import com.zipow.videobox.ptapp.p013mm.ZoomGroup;
import com.zipow.videobox.ptapp.p013mm.ZoomMessage;
import com.zipow.videobox.ptapp.p013mm.ZoomMessage.FileTransferInfo;
import com.zipow.videobox.ptapp.p013mm.ZoomMessenger;
import com.zipow.videobox.util.CodeSnipptUtils;
import com.zipow.videobox.util.CodeSnipptUtils.CodeSnippetInfo;
import com.zipow.videobox.view.AvatarView;
import com.zipow.videobox.view.IMAddrBookItem;
import com.zipow.videobox.view.ReactionLabelsView;
import com.zipow.videobox.view.p014mm.AbsMessageView;
import com.zipow.videobox.view.p014mm.AbsMessageView.OnClickAvatarListener;
import com.zipow.videobox.view.p014mm.AbsMessageView.OnLongClickAvatarListener;
import com.zipow.videobox.view.p014mm.AbsMessageView.OnShowContextMenuListener;
import com.zipow.videobox.view.p014mm.MMMessageItem;
import com.zipow.videobox.view.p014mm.ProportyBean;
import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.util.FileUtils;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.TimeUtil;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.videomeetings.C4558R;

/* renamed from: com.zipow.videobox.view.mm.message.MessageCodeSnippetReceiveView */
public class MessageCodeSnippetReceiveView extends AbsMessageView {
    private static final String TAG = "MessageCodeSnippetReceiveView";
    protected LinearLayout codeSnippetList;
    private LinearLayout contactLinear;
    private TextView contactName;
    private TextView groupContact;
    private LinearLayout groupLinear;
    private TextView groupName;
    protected LinearLayout holderFailed;
    protected LinearLayout holderProgress;
    protected TextView mAvatarName;
    protected AvatarView mAvatarView;
    protected ImageView mImgStarred;
    protected LinearLayout mItemFive;
    protected TextView mItemFiveTxt;
    protected LinearLayout mItemFour;
    protected TextView mItemFourTxt;
    protected TextView mItemMore;
    protected LinearLayout mItemOne;
    protected TextView mItemOneTxt;
    protected LinearLayout mItemThree;
    protected TextView mItemThreeTxt;
    protected LinearLayout mItemTwo;
    protected TextView mItemTwoTxt;
    protected MMMessageItem mMessageItem;
    protected ReactionLabelsView mReactionLabels;
    protected TextView mTitile;
    protected LinearLayout mTitleLinear;
    protected TextView mTitleType;
    @NonNull
    private IZoomMessengerUIListener mZoomMessengerUIListener = new SimpleZoomMessengerUIListener() {
        public void onConfirmFileDownloaded(String str, String str2, int i) {
            super.onConfirmFileDownloaded(str, str2, i);
            if (MessageCodeSnippetReceiveView.this.mMessageItem != null && str2 != null && str2.equals(MessageCodeSnippetReceiveView.this.mMessageItem.messageXMPPId)) {
                if (i == 0) {
                    MessageCodeSnippetReceiveView messageCodeSnippetReceiveView = MessageCodeSnippetReceiveView.this;
                    messageCodeSnippetReceiveView.setMessageItem(messageCodeSnippetReceiveView.mMessageItem);
                    return;
                }
                MessageCodeSnippetReceiveView.this.setHolderFailed();
            }
        }

        public void confirm_EditedFileDownloadedIml(int i, @NonNull Map<String, String> map) {
            super.confirm_EditedFileDownloadedIml(i, map);
            if (MessageCodeSnippetReceiveView.this.mMessageItem != null && map.containsKey(MessageCodeSnippetReceiveView.this.mMessageItem.sessionId) && ((String) map.get(MessageCodeSnippetReceiveView.this.mMessageItem.sessionId)).equalsIgnoreCase(MessageCodeSnippetReceiveView.this.mMessageItem.messageXMPPId)) {
                if (i == 0) {
                    MessageCodeSnippetReceiveView messageCodeSnippetReceiveView = MessageCodeSnippetReceiveView.this;
                    messageCodeSnippetReceiveView.setMessageItem(messageCodeSnippetReceiveView.mMessageItem);
                    return;
                }
                MessageCodeSnippetReceiveView.this.setHolderFailed();
            }
        }

        public void Indicate_FileDownloaded(String str, String str2, int i) {
            super.Indicate_FileDownloaded(str, str2, i);
        }

        public void FT_OnDownloadByFileIDTimeOut(String str, String str2) {
            super.FT_OnDownloadByFileIDTimeOut(str, str2);
        }

        public void FT_OnDownloadByMsgIDTimeOut(String str, String str2) {
            super.FT_OnDownloadByMsgIDTimeOut(str, str2);
            if (MessageCodeSnippetReceiveView.this.mMessageItem != null && str2 != null && str2.equals(MessageCodeSnippetReceiveView.this.mMessageItem.messageXMPPId)) {
                MessageCodeSnippetReceiveView.this.setHolderFailed();
            }
        }
    };
    private LinearLayout starredMsgTitleLinear;
    private TextView time;
    private TextView txtStarDes;

    public MessageCodeSnippetReceiveView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        initView();
    }

    public MessageCodeSnippetReceiveView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initView();
    }

    public MessageCodeSnippetReceiveView(Context context) {
        super(context);
        initView();
    }

    private void initView() {
        inflateLayout();
    }

    /* access modifiers changed from: protected */
    public void inflateLayout() {
        View.inflate(getContext(), getLayoutId(), this);
        this.mAvatarView = (AvatarView) findViewById(C4558R.C4560id.code_snippet_avatar);
        this.mAvatarName = (TextView) findViewById(C4558R.C4560id.code_snippet_name);
        this.mTitleLinear = (LinearLayout) findViewById(C4558R.C4560id.code_snippet_title_linear);
        this.mTitile = (TextView) findViewById(C4558R.C4560id.code_snippet_title);
        this.mTitleType = (TextView) findViewById(C4558R.C4560id.code_snippet_title_type);
        this.mItemOne = (LinearLayout) findViewById(C4558R.C4560id.code_snippet_item_one);
        this.mItemOneTxt = (TextView) findViewById(C4558R.C4560id.code_snippet_item_one_txt);
        this.mItemTwo = (LinearLayout) findViewById(C4558R.C4560id.code_snippet_item_two);
        this.mItemTwoTxt = (TextView) findViewById(C4558R.C4560id.code_snippet_item_two_txt);
        this.mItemThree = (LinearLayout) findViewById(C4558R.C4560id.code_snippet_item_three);
        this.mItemThreeTxt = (TextView) findViewById(C4558R.C4560id.code_snippet_item_three_txt);
        this.mItemFour = (LinearLayout) findViewById(C4558R.C4560id.code_snippet_item_four);
        this.mItemFourTxt = (TextView) findViewById(C4558R.C4560id.code_snippet_item_four_txt);
        this.mItemFive = (LinearLayout) findViewById(C4558R.C4560id.code_snippet_item_five);
        this.mItemFiveTxt = (TextView) findViewById(C4558R.C4560id.code_snippet_item_five_txt);
        this.mItemMore = (TextView) findViewById(C4558R.C4560id.code_snippet_item_more);
        this.codeSnippetList = (LinearLayout) findViewById(C4558R.C4560id.code_snippet_list);
        this.holderProgress = (LinearLayout) findViewById(C4558R.C4560id.code_snippet_holder_progress);
        this.holderFailed = (LinearLayout) findViewById(C4558R.C4560id.code_snippet_holder_failed);
        this.starredMsgTitleLinear = (LinearLayout) findViewById(C4558R.C4560id.zm_starred_message_list_item_title_linear);
        this.contactLinear = (LinearLayout) findViewById(C4558R.C4560id.zm_starred_message_list_item_contact_linear);
        this.contactName = (TextView) findViewById(C4558R.C4560id.zm_starred_message_list_item_contact_name);
        this.groupLinear = (LinearLayout) findViewById(C4558R.C4560id.zm_starred_message_list_item_group_linear);
        this.groupContact = (TextView) findViewById(C4558R.C4560id.zm_starred_message_list_item_group_contact);
        this.groupName = (TextView) findViewById(C4558R.C4560id.zm_starred_message_list_item_group_name);
        this.time = (TextView) findViewById(C4558R.C4560id.zm_starred_message_list_item_time);
        this.txtStarDes = (TextView) findViewById(C4558R.C4560id.txtStarDes);
        this.mImgStarred = (ImageView) findViewById(C4558R.C4560id.zm_mm_starred);
        this.mReactionLabels = (ReactionLabelsView) findViewById(C4558R.C4560id.reaction_labels_view);
        AvatarView avatarView = this.mAvatarView;
        if (avatarView != null) {
            avatarView.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    OnClickAvatarListener onClickAvatarListener = MessageCodeSnippetReceiveView.this.getOnClickAvatarListener();
                    if (onClickAvatarListener != null) {
                        onClickAvatarListener.onClickAvatar(MessageCodeSnippetReceiveView.this.mMessageItem);
                    }
                }
            });
            this.mAvatarView.setOnLongClickListener(new OnLongClickListener() {
                public boolean onLongClick(View view) {
                    OnLongClickAvatarListener onLongClickAvatarListener = MessageCodeSnippetReceiveView.this.getOnLongClickAvatarListener();
                    if (onLongClickAvatarListener != null) {
                        return onLongClickAvatarListener.onLongClickAvatar(MessageCodeSnippetReceiveView.this.mMessageItem);
                    }
                    return false;
                }
            });
        }
    }

    public void changeAvatar(boolean z) {
        if (z) {
            LayoutParams layoutParams = (LayoutParams) this.mAvatarView.getLayoutParams();
            layoutParams.width = UIUtil.dip2px(getContext(), 24.0f);
            layoutParams.height = UIUtil.dip2px(getContext(), 24.0f);
            layoutParams.leftMargin = UIUtil.dip2px(getContext(), 16.0f);
            this.mAvatarView.setLayoutParams(layoutParams);
            return;
        }
        LayoutParams layoutParams2 = (LayoutParams) this.mAvatarView.getLayoutParams();
        layoutParams2.width = UIUtil.dip2px(getContext(), 40.0f);
        layoutParams2.height = UIUtil.dip2px(getContext(), 40.0f);
        this.mAvatarView.setLayoutParams(layoutParams2);
    }

    /* access modifiers changed from: protected */
    public int getLayoutId() {
        return C4558R.layout.zm_message_code_snippet_msg_receive;
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        ZoomMessengerUI.getInstance().addListener(this.mZoomMessengerUIListener);
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        ZoomMessengerUI.getInstance().removeListener(this.mZoomMessengerUIListener);
    }

    public void setMessageName(String str) {
        TextView textView = this.mAvatarName;
        if (textView != null) {
            textView.setText(str);
        }
    }

    public void setScreenName(@Nullable String str) {
        if (str != null) {
            TextView textView = this.mAvatarName;
            if (textView != null) {
                textView.setText(str);
            }
        }
    }

    public void setTitle(@NonNull String str) {
        if (!TextUtils.isEmpty(str)) {
            String[] split = str.split("\\.");
            if (split.length > 0) {
                str = split[0];
            }
        } else {
            str = "";
        }
        this.mTitile.setText(str);
    }

    public void setHolderVisible(int i) {
        this.holderFailed.setVisibility(8);
        if (i == 0) {
            this.codeSnippetList.setVisibility(8);
            this.holderProgress.setVisibility(0);
            return;
        }
        this.codeSnippetList.setVisibility(0);
        this.holderProgress.setVisibility(8);
    }

    public void setHolderFailed() {
        this.holderFailed.setVisibility(0);
        this.codeSnippetList.setVisibility(8);
        this.holderProgress.setVisibility(8);
    }

    public void setCodeSnippet(@Nullable ZoomMessage zoomMessage) {
        if (zoomMessage != null && !TextUtils.isEmpty(zoomMessage.getLocalFilePath())) {
            CodeSnippetInfo parseZipSnippet = CodeSnipptUtils.parseZipSnippet(zoomMessage, "html", 5);
            if (parseZipSnippet != null) {
                int lineNo = parseZipSnippet.getLineNo();
                List contents = parseZipSnippet.getContents();
                if (lineNo < 1) {
                    this.mItemMore.setVisibility(8);
                    this.mItemFive.setVisibility(8);
                    this.mItemFour.setVisibility(8);
                    this.mItemThree.setVisibility(8);
                    this.mItemTwo.setVisibility(8);
                    this.mItemOne.setVisibility(0);
                    this.mItemOneTxt.setText("");
                } else if (lineNo == 1) {
                    this.mItemMore.setVisibility(8);
                    this.mItemFive.setVisibility(8);
                    this.mItemFour.setVisibility(8);
                    this.mItemThree.setVisibility(8);
                    this.mItemTwo.setVisibility(8);
                    this.mItemOne.setVisibility(0);
                    if (contents != null && contents.size() > 0) {
                        this.mItemOneTxt.setText((CharSequence) contents.get(0));
                    }
                } else if (lineNo == 2) {
                    this.mItemMore.setVisibility(8);
                    this.mItemFive.setVisibility(8);
                    this.mItemFour.setVisibility(8);
                    this.mItemThree.setVisibility(8);
                    this.mItemTwo.setVisibility(0);
                    this.mItemOne.setVisibility(0);
                    if (contents != null && contents.size() > 1) {
                        this.mItemOneTxt.setText((CharSequence) contents.get(0));
                        this.mItemTwoTxt.setText((CharSequence) contents.get(1));
                    }
                } else if (lineNo == 3) {
                    this.mItemMore.setVisibility(8);
                    this.mItemFive.setVisibility(8);
                    this.mItemFour.setVisibility(8);
                    this.mItemThree.setVisibility(0);
                    this.mItemTwo.setVisibility(0);
                    this.mItemOne.setVisibility(0);
                    if (contents != null && contents.size() > 2) {
                        this.mItemOneTxt.setText((CharSequence) contents.get(0));
                        this.mItemTwoTxt.setText((CharSequence) contents.get(1));
                        this.mItemThreeTxt.setText((CharSequence) contents.get(2));
                    }
                } else if (lineNo == 4) {
                    this.mItemMore.setVisibility(8);
                    this.mItemFive.setVisibility(8);
                    this.mItemFour.setVisibility(0);
                    this.mItemThree.setVisibility(0);
                    this.mItemTwo.setVisibility(0);
                    this.mItemOne.setVisibility(0);
                    if (contents != null && contents.size() > 3) {
                        this.mItemOneTxt.setText((CharSequence) contents.get(0));
                        this.mItemTwoTxt.setText((CharSequence) contents.get(1));
                        this.mItemThreeTxt.setText((CharSequence) contents.get(2));
                        this.mItemFourTxt.setText((CharSequence) contents.get(3));
                    }
                } else if (lineNo == 5) {
                    this.mItemMore.setVisibility(8);
                    this.mItemFive.setVisibility(0);
                    this.mItemFour.setVisibility(0);
                    this.mItemThree.setVisibility(0);
                    this.mItemTwo.setVisibility(0);
                    this.mItemOne.setVisibility(0);
                    if (contents != null && contents.size() > 4) {
                        this.mItemOneTxt.setText((CharSequence) contents.get(0));
                        this.mItemTwoTxt.setText((CharSequence) contents.get(1));
                        this.mItemThreeTxt.setText((CharSequence) contents.get(2));
                        this.mItemFourTxt.setText((CharSequence) contents.get(3));
                        this.mItemFiveTxt.setText((CharSequence) contents.get(4));
                    }
                } else {
                    this.mItemMore.setVisibility(0);
                    this.mItemFive.setVisibility(0);
                    this.mItemFour.setVisibility(0);
                    this.mItemThree.setVisibility(0);
                    this.mItemTwo.setVisibility(0);
                    this.mItemOne.setVisibility(0);
                    if (contents != null && contents.size() >= 5) {
                        this.mItemOneTxt.setText((CharSequence) contents.get(0));
                        this.mItemTwoTxt.setText((CharSequence) contents.get(1));
                        this.mItemThreeTxt.setText((CharSequence) contents.get(2));
                        this.mItemFourTxt.setText((CharSequence) contents.get(3));
                        this.mItemFiveTxt.setText((CharSequence) contents.get(4));
                        this.mItemMore.setText(getContext().getString(C4558R.string.zm_mm_code_snippet_more_31945, new Object[]{Integer.valueOf(lineNo - 5)}));
                    }
                }
            }
        }
    }

    public void setStarredMessage(@NonNull MMMessageItem mMMessageItem) {
        if (this.starredMsgTitleLinear != null) {
            if (mMMessageItem.hideStarView) {
                this.starredMsgTitleLinear.setVisibility(0);
                this.mAvatarName.setVisibility(8);
                ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
                if (zoomMessenger != null) {
                    ZoomBuddy myself = zoomMessenger.getMyself();
                    if (myself != null) {
                        ZoomChatSession sessionById = zoomMessenger.getSessionById(mMMessageItem.sessionId);
                        if (sessionById != null) {
                            if (mMMessageItem.isGroupMessage) {
                                this.contactLinear.setVisibility(8);
                                this.groupLinear.setVisibility(0);
                                ZoomGroup sessionGroup = sessionById.getSessionGroup();
                                if (sessionGroup != null) {
                                    this.groupName.setText(sessionGroup.getGroupName());
                                }
                            } else {
                                this.contactLinear.setVisibility(0);
                                this.groupLinear.setVisibility(8);
                                ZoomBuddy sessionBuddy = sessionById.getSessionBuddy();
                                if (sessionBuddy != null) {
                                    this.groupName.setText(BuddyNameUtil.getMyDisplayName(sessionBuddy));
                                } else if (TextUtils.equals(mMMessageItem.sessionId, myself.getJid())) {
                                    this.groupName.setText(BuddyNameUtil.getMyDisplayName(myself));
                                }
                            }
                            this.time.setText(TimeUtil.formatDateTimeCap(getContext(), mMMessageItem.serverSideTime));
                            String string = StringUtil.isSameString(myself.getJid(), mMMessageItem.fromJid) ? getContext().getString(C4558R.string.zm_lbl_content_you) : mMMessageItem.fromScreenName;
                            this.groupContact.setText(string);
                            this.contactName.setText(string);
                            if (mMMessageItem.isComment) {
                                this.txtStarDes.setText(C4558R.string.zm_lbl_from_thread_88133);
                                this.txtStarDes.setVisibility(0);
                            } else if (mMMessageItem.commentsCount > 0) {
                                this.txtStarDes.setText(getResources().getQuantityString(C4558R.plurals.zm_lbl_comment_reply_title_88133, (int) mMMessageItem.commentsCount, new Object[]{Integer.valueOf((int) mMMessageItem.commentsCount)}));
                                this.txtStarDes.setVisibility(0);
                            } else {
                                this.txtStarDes.setVisibility(8);
                            }
                        }
                    }
                }
            } else {
                this.starredMsgTitleLinear.setVisibility(8);
                this.txtStarDes.setVisibility(8);
            }
        }
    }

    public void setMessageItem(@NonNull final MMMessageItem mMMessageItem) {
        this.mMessageItem = mMMessageItem;
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (mMMessageItem.hideStarView || !mMMessageItem.isMessgeStarred) {
            this.mImgStarred.setVisibility(8);
        } else {
            this.mImgStarred.setVisibility(0);
        }
        setMessageName(String.valueOf(mMMessageItem.message));
        AvatarView avatarView = this.mAvatarView;
        if (avatarView != null) {
            avatarView.setVisibility(0);
        }
        TextView textView = this.mAvatarName;
        if (textView != null) {
            textView.setVisibility(0);
        }
        if (!mMMessageItem.isSendingMessage() || getContext() == null) {
            setScreenName(mMMessageItem.fromScreenName);
        } else {
            setScreenName(getContext().getString(C4558R.string.zm_lbl_content_you));
            this.mAvatarName.setVisibility(0);
        }
        setReactionLabels(mMMessageItem);
        if (!isInEditMode()) {
            String str = mMMessageItem.fromJid;
            if (zoomMessenger != null) {
                ZoomBuddy myself = zoomMessenger.getMyself();
                if (myself == null || str == null || !str.equals(myself.getJid())) {
                    myself = zoomMessenger.getBuddyWithJID(str);
                }
                if (mMMessageItem.fromContact == null && myself != null) {
                    mMMessageItem.fromContact = IMAddrBookItem.fromZoomBuddy(myself);
                }
                if (mMMessageItem.fromContact != null) {
                    AvatarView avatarView2 = this.mAvatarView;
                    if (avatarView2 != null) {
                        avatarView2.show(mMMessageItem.fromContact.getAvatarParamsBuilder());
                    }
                }
            }
            if (mMMessageItem.fileInfo != null) {
                setTitle(mMMessageItem.fileInfo.name);
            }
            if (zoomMessenger == null) {
                setHolderFailed();
                return;
            }
            this.mTitleLinear.setVisibility(8);
            ZoomChatSession sessionById = zoomMessenger.getSessionById(mMMessageItem.sessionId);
            if (sessionById != null) {
                ZoomMessage messageById = sessionById.getMessageById(mMMessageItem.messageXMPPId);
                if (messageById != null) {
                    FileTransferInfo fileTransferInfo = messageById.getFileTransferInfo();
                    if (fileTransferInfo != null) {
                        String localFilePath = messageById.getLocalFilePath();
                        if (fileTransferInfo.state == 13 && FileUtils.fileIsExists(localFilePath)) {
                            String readZipFile = FileUtils.readZipFile(localFilePath, "properties");
                            if (TextUtils.isEmpty(readZipFile)) {
                                setHolderFailed();
                            } else {
                                this.mTitleLinear.setVisibility(0);
                                setHolderVisible(8);
                                try {
                                    ProportyBean proportyBean = (ProportyBean) new Gson().fromJson(readZipFile, ProportyBean.class);
                                    if (proportyBean != null) {
                                        TextView textView2 = this.mTitleType;
                                        Context context = getContext();
                                        int i = C4558R.string.zm_mm_code_snippet_title_31945;
                                        Object[] objArr = new Object[1];
                                        objArr[0] = proportyBean.getType() == null ? "" : proportyBean.getType();
                                        textView2.setText(context.getString(i, objArr));
                                    }
                                } catch (Exception unused) {
                                }
                                setCodeSnippet(messageById);
                            }
                        } else if (zoomMessenger.isConnectionGood()) {
                            setHolderVisible(0);
                            if (fileTransferInfo.state == 0) {
                                if (TextUtils.isEmpty(localFilePath)) {
                                    sessionById.downloadFileForMessage(mMMessageItem.messageXMPPId);
                                } else {
                                    File file = new File(localFilePath);
                                    if (file.exists()) {
                                        file.delete();
                                    }
                                    File parentFile = file.getParentFile();
                                    if (parentFile != null) {
                                        parentFile = parentFile.getParentFile();
                                    }
                                    if (parentFile == null || mMMessageItem.fileInfo == null) {
                                        setHolderFailed();
                                    } else {
                                        try {
                                            File file2 = new File(parentFile, UUID.randomUUID().toString());
                                            file2.mkdirs();
                                            sessionById.downloadFileForMessage(mMMessageItem.messageXMPPId, new File(file2, mMMessageItem.fileInfo.name).getAbsolutePath());
                                        } catch (Exception unused2) {
                                            setHolderFailed();
                                        }
                                    }
                                }
                            } else if (fileTransferInfo.state != 10) {
                                sessionById.downloadFileForMessage(mMMessageItem.messageXMPPId);
                            } else {
                                return;
                            }
                        } else {
                            setHolderFailed();
                        }
                        this.codeSnippetList.setOnClickListener(new OnClickListener() {
                            public void onClick(View view) {
                                ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
                                if (zoomMessenger != null) {
                                    ZoomChatSession sessionById = zoomMessenger.getSessionById(mMMessageItem.sessionId);
                                    if (sessionById != null) {
                                        ZoomMessage messageById = sessionById.getMessageById(mMMessageItem.messageXMPPId);
                                        if (messageById != null) {
                                            String localFilePath = messageById.getLocalFilePath();
                                            if (!TextUtils.isEmpty(localFilePath)) {
                                                File file = new File(localFilePath);
                                                if (file.exists() && file.isFile() && (MessageCodeSnippetReceiveView.this.getContext() instanceof ZMActivity) && MessageCodeSnippetReceiveView.this.mMessageItem != null) {
                                                    ZMCodeViewFragment.showAsFragment((ZMActivity) MessageCodeSnippetReceiveView.this.getContext(), MessageCodeSnippetReceiveView.this.mMessageItem.sessionId, MessageCodeSnippetReceiveView.this.mMessageItem.messageId, file, MessageCodeSnippetReceiveView.this.mTitile.getText().toString());
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        });
                        this.codeSnippetList.setOnLongClickListener(new OnLongClickListener() {
                            public boolean onLongClick(View view) {
                                OnShowContextMenuListener onShowContextMenuListener = MessageCodeSnippetReceiveView.this.getOnShowContextMenuListener();
                                if (onShowContextMenuListener != null) {
                                    return onShowContextMenuListener.onShowContextMenu(view, MessageCodeSnippetReceiveView.this.mMessageItem);
                                }
                                return false;
                            }
                        });
                        this.holderFailed.setOnClickListener(new OnClickListener() {
                            public void onClick(View view) {
                                ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
                                if (zoomMessenger != null) {
                                    ZoomChatSession sessionById = zoomMessenger.getSessionById(mMMessageItem.sessionId);
                                    if (sessionById != null) {
                                        ZoomMessage messageById = sessionById.getMessageById(mMMessageItem.messageXMPPId);
                                        if (messageById != null) {
                                            FileTransferInfo fileTransferInfo = messageById.getFileTransferInfo();
                                            if (!(fileTransferInfo == null || fileTransferInfo.state == 13 || !zoomMessenger.isConnectionGood())) {
                                                MessageCodeSnippetReceiveView.this.setHolderVisible(0);
                                                sessionById.downloadFileForMessage(mMMessageItem.messageXMPPId);
                                            }
                                        }
                                    }
                                }
                            }
                        });
                    } else {
                        return;
                    }
                } else {
                    return;
                }
            } else {
                return;
            }
        }
        setStarredMessage(mMMessageItem);
        setReactionLabels(mMMessageItem);
    }

    public void setMessageItem(MMMessageItem mMMessageItem, boolean z) {
        setMessageItem(mMMessageItem);
        if (z) {
            this.mTitleLinear.setVisibility(4);
            this.mAvatarView.setVisibility(4);
            this.mReactionLabels.setVisibility(8);
            if (this.mAvatarName.getVisibility() == 0) {
                this.mAvatarName.setVisibility(4);
            }
        }
    }

    public Rect getMessageLocationOnScreen() {
        int[] iArr = new int[2];
        getLocationOnScreen(iArr);
        ReactionLabelsView reactionLabelsView = this.mReactionLabels;
        return new Rect(iArr[0], iArr[1], iArr[0] + getWidth(), (iArr[0] + getHeight()) - ((reactionLabelsView == null || reactionLabelsView.getVisibility() == 8) ? 0 : this.mReactionLabels.getHeight() + (UIUtil.dip2px(getContext(), 4.0f) * 2)));
    }

    public void setReactionLabels(MMMessageItem mMMessageItem) {
        if (!(mMMessageItem == null || this.mReactionLabels == null)) {
            if (mMMessageItem.hideStarView) {
                this.mReactionLabels.setVisibility(8);
                return;
            }
            this.mReactionLabels.setLabels(mMMessageItem, getOnClickReactionLabelListener());
        }
    }

    public MMMessageItem getMessageItem() {
        return this.mMessageItem;
    }
}
