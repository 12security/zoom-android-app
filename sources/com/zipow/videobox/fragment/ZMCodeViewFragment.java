package com.zipow.videobox.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.zipow.videobox.SimpleActivity;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.ZoomMessengerUI;
import com.zipow.videobox.ptapp.ZoomMessengerUI.IZoomMessengerUIListener;
import com.zipow.videobox.ptapp.ZoomMessengerUI.SimpleZoomMessengerUIListener;
import com.zipow.videobox.ptapp.p013mm.MMFileContentMgr;
import com.zipow.videobox.ptapp.p013mm.ZoomBuddy;
import com.zipow.videobox.ptapp.p013mm.ZoomChatSession;
import com.zipow.videobox.ptapp.p013mm.ZoomFile;
import com.zipow.videobox.ptapp.p013mm.ZoomMessage;
import com.zipow.videobox.ptapp.p013mm.ZoomMessenger;
import com.zipow.videobox.util.CodeSnipptUtils;
import com.zipow.videobox.util.TintUtil;
import com.zipow.videobox.view.IMAddrBookItem;
import com.zipow.videobox.view.ZMCodeView;
import com.zipow.videobox.view.p014mm.MMMessageItem;
import com.zipow.videobox.view.p014mm.MMShareZoomFileDialogFragment;
import com.zipow.videobox.view.p014mm.MMZoomFile;
import com.zipow.videobox.view.p014mm.MMZoomShareAction;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.androidlib.widget.ZMAlertDialog;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;
import p021us.zoom.androidlib.widget.ZMMenuAdapter;
import p021us.zoom.androidlib.widget.ZMSimpleMenuItem;
import p021us.zoom.videomeetings.C4558R;

public class ZMCodeViewFragment extends ZMDialogFragment {
    private static final String ARGS_CODE_FILE = "code_file";
    private static final String ARGS_CODE_FILENAME = "code_filename";
    private static final String ARGS_CODE_HTML = "code_html";
    private static final String ARGS_CODE_SESSION_ID = "sessionid";
    private static final String ARGS_CODE_URL = "code_url";
    private static final String ARG_SHARED_MESSAGE_ID = "messageid";
    private static final int REQUEST_GET_SHAREES = 100;
    private ImageView mCloseBtn;
    private TextView mContent;
    /* access modifiers changed from: private */
    public String mMessageId;
    /* access modifiers changed from: private */
    @Nullable
    public MMMessageItem mMessageItem = null;
    private ImageView mMoreBtn;
    /* access modifiers changed from: private */
    public String mSessionId;
    private TextView mTitle;
    @NonNull
    private IZoomMessengerUIListener mZoomMessengerUIListener = new SimpleZoomMessengerUIListener() {
        public void Indicate_FileActionStatus(int i, String str, String str2, String str3, String str4, String str5) {
            if (ZMCodeViewFragment.this.mMessageItem != null && str != null && str.equals(ZMCodeViewFragment.this.mMessageItem.fileId) && ZMCodeViewFragment.this.mSessionId != null && ZMCodeViewFragment.this.mSessionId.equals(str4)) {
                if (i == 1 || i == 2) {
                    ZMCodeViewFragment.this.finishFragment(true);
                }
            }
        }
    };
    private ZMCodeView zmCodeView;

    public static void showAsFragment(Fragment fragment, URL url) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(ARGS_CODE_URL, url);
        SimpleActivity.show(fragment, ZMCodeViewFragment.class.getName(), bundle, -1);
    }

    public static void showAsFragment(Fragment fragment, File file) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(ARGS_CODE_FILE, file);
        SimpleActivity.show(fragment, ZMCodeViewFragment.class.getName(), bundle, -1);
    }

    public static void showAsFragment(@NonNull ZMActivity zMActivity, String str, String str2, File file, String str3) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(ARGS_CODE_FILE, file);
        bundle.putString(ARGS_CODE_FILENAME, str3);
        bundle.putString(ARGS_CODE_SESSION_ID, str);
        bundle.putString(ARG_SHARED_MESSAGE_ID, str2);
        SimpleActivity.show(zMActivity, ZMCodeViewFragment.class.getName(), bundle, -1);
    }

    public static void showAsFragment(Fragment fragment, String str) {
        Bundle bundle = new Bundle();
        bundle.putString(ARGS_CODE_HTML, str);
        SimpleActivity.show(fragment, ZMCodeViewFragment.class.getName(), bundle, -1);
    }

    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        UIUtil.renderStatueBar(getActivity(), true, C4558R.color.zm_code_view_title_bg);
    }

    @Nullable
    public View onCreateView(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        View inflate = layoutInflater.inflate(C4558R.layout.zm_code_view_fragment, viewGroup, false);
        this.zmCodeView = (ZMCodeView) inflate.findViewById(C4558R.C4560id.codeView);
        this.mTitle = (TextView) inflate.findViewById(C4558R.C4560id.zm_code_view_title_name);
        this.mCloseBtn = (ImageView) inflate.findViewById(C4558R.C4560id.zm_code_view_close_btn);
        this.mContent = (TextView) inflate.findViewById(C4558R.C4560id.zm_code_view_bottom_content);
        this.mMoreBtn = (ImageView) inflate.findViewById(C4558R.C4560id.zm_code_view_more_btn);
        if (getContext() != null) {
            this.mCloseBtn.setImageDrawable(TintUtil.tintColor(getContext(), C4558R.C4559drawable.zm_btn_viewer_close, C4558R.color.zm_code_view_close_btn));
            this.mMoreBtn.setImageDrawable(TintUtil.tintColor(getContext(), C4558R.C4559drawable.zm_ic_btn_more, C4558R.color.zm_code_view_bottom_txt));
        }
        ZoomMessengerUI.getInstance().addListener(this.mZoomMessengerUIListener);
        return inflate;
    }

    public void onDestroyView() {
        ZoomMessengerUI.getInstance().removeListener(this.mZoomMessengerUIListener);
        super.onDestroyView();
    }

    public void shareMessage(String str) {
        Bundle bundle = new Bundle();
        bundle.putString(ARG_SHARED_MESSAGE_ID, str);
        MMSelectSessionAndBuddyFragment.showAsFragment(this, bundle, false, false, 100);
    }

    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        Bundle arguments = getArguments();
        if (arguments != null) {
            URL url = (URL) arguments.getSerializable(ARGS_CODE_URL);
            if (url != null) {
                this.zmCodeView.setSource(url);
            }
            final String string = arguments.getString(ARGS_CODE_FILENAME, "");
            this.mTitle.setText(string);
            this.mSessionId = arguments.getString(ARGS_CODE_SESSION_ID, "");
            this.mMessageId = arguments.getString(ARG_SHARED_MESSAGE_ID, "");
            final ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
            if (zoomMessenger != null) {
                ZoomChatSession sessionById = zoomMessenger.getSessionById(this.mSessionId);
                if (sessionById != null) {
                    ZoomMessage messageById = sessionById.getMessageById(this.mMessageId);
                    if (messageById != null) {
                        ZoomBuddy myself = zoomMessenger.getMyself();
                        if (myself != null) {
                            MMFileContentMgr zoomFileContentMgr = PTApp.getInstance().getZoomFileContentMgr();
                            if (zoomFileContentMgr != null) {
                                if (((File) arguments.getSerializable(ARGS_CODE_FILE)) != null) {
                                    this.zmCodeView.setSource(CodeSnipptUtils.parseZipSnippetSrc(messageById, "html"));
                                }
                                boolean isSameString = StringUtil.isSameString(messageById.getSenderID(), myself.getJid());
                                this.mMessageItem = MMMessageItem.initWithZoomMessage(messageById, this.mSessionId, zoomMessenger, sessionById.isGroup(), isSameString, getActivity(), IMAddrBookItem.fromZoomBuddy(sessionById.getSessionBuddy()), zoomFileContentMgr);
                                if (!TextUtils.isEmpty(arguments.getString(ARGS_CODE_HTML))) {
                                    this.zmCodeView.setSource(readAssetsTxt(getContext(), "test.html"));
                                }
                                this.mCloseBtn.setOnClickListener(new OnClickListener() {
                                    public void onClick(View view) {
                                        ZMCodeViewFragment.this.finishFragment(false);
                                    }
                                });
                                final ZMMenuAdapter zMMenuAdapter = new ZMMenuAdapter(getContext(), false);
                                ZoomFile fileWithMessageID = zoomFileContentMgr.getFileWithMessageID(this.mSessionId, this.mMessageItem.messageXMPPId);
                                if (!(fileWithMessageID == null || this.mMessageItem.isE2E || zoomMessenger.e2eGetMyOption() == 2)) {
                                    int fileTransferState = fileWithMessageID.getFileTransferState();
                                    if (!(fileTransferState == 2 || fileTransferState == 3 || fileTransferState == 1 || fileTransferState == 0 || getContext() == null)) {
                                        zMMenuAdapter.addItem(new ZMSimpleMenuItem(0, getContext().getString(C4558R.string.zm_btn_share)));
                                    }
                                }
                                if (this.mMessageItem.isDeleteable(this.mSessionId) && getContext() != null) {
                                    zMMenuAdapter.addItem(new ZMSimpleMenuItem(1, getContext().getString(C4558R.string.zm_btn_delete)));
                                }
                                if (zMMenuAdapter.getCount() <= 0) {
                                    this.mMoreBtn.setVisibility(8);
                                } else {
                                    this.mMoreBtn.setVisibility(0);
                                }
                                this.mMoreBtn.setOnClickListener(new OnClickListener() {
                                    public void onClick(View view) {
                                        ZMAlertDialog create = new Builder(ZMCodeViewFragment.this.getContext()).setTitle((CharSequence) string).setAdapter(zMMenuAdapter, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                ZMSimpleMenuItem zMSimpleMenuItem = (ZMSimpleMenuItem) zMMenuAdapter.getItem(i);
                                                if (zMSimpleMenuItem.getAction() == 0) {
                                                    ZMCodeViewFragment.this.shareMessage(ZMCodeViewFragment.this.mMessageId);
                                                } else if (zMSimpleMenuItem.getAction() != 1) {
                                                } else {
                                                    if (!zoomMessenger.isConnectionGood()) {
                                                        Toast.makeText(ZMCodeViewFragment.this.getContext(), ZMCodeViewFragment.this.getResources().getString(C4558R.string.zm_mm_msg_network_unavailable), 1).show();
                                                    } else if (ZMCodeViewFragment.this.getActivity() != null) {
                                                        ZMCodeViewFragment.this.mMessageItem.deleteMessage(ZMCodeViewFragment.this.getActivity());
                                                    }
                                                }
                                            }
                                        }).create();
                                        create.setCanceledOnTouchOutside(true);
                                        create.show();
                                    }
                                });
                            }
                        }
                    }
                }
            }
        }
    }

    public void onResume() {
        refreshFileShares(this.mSessionId, this.mMessageId);
        super.onResume();
    }

    private void refreshFileShares(String str, String str2) {
        String str3;
        String str4;
        String str5 = "";
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            ZoomBuddy myself = zoomMessenger.getMyself();
            if (myself != null) {
                String jid = myself.getJid();
                MMFileContentMgr zoomFileContentMgr = PTApp.getInstance().getZoomFileContentMgr();
                if (zoomFileContentMgr != null) {
                    ZoomFile fileWithMessageID = zoomFileContentMgr.getFileWithMessageID(str, str2);
                    if (fileWithMessageID != null) {
                        MMZoomFile initWithZoomFile = MMZoomFile.initWithZoomFile(fileWithMessageID, zoomFileContentMgr);
                        List<MMZoomShareAction> shareAction = initWithZoomFile.getShareAction();
                        if (shareAction != null) {
                            StringBuffer stringBuffer = new StringBuffer();
                            for (MMZoomShareAction mMZoomShareAction : shareAction) {
                                if (mMZoomShareAction.isGroup() && !mMZoomShareAction.isMUC()) {
                                    stringBuffer.append(mMZoomShareAction.getShareeName(getActivity()));
                                    stringBuffer.append(PreferencesConstants.COOKIE_DELIMITER);
                                }
                            }
                            if (StringUtil.isSameString(initWithZoomFile.getOwnerJid(), jid)) {
                                str3 = getString(C4558R.string.zm_lbl_content_me);
                            } else {
                                str3 = initWithZoomFile.getOwnerName();
                            }
                            if (stringBuffer.length() == 0) {
                                if (StringUtil.isSameString(initWithZoomFile.getOwnerJid(), jid)) {
                                    str4 = "";
                                } else {
                                    str4 = getString(C4558R.string.zm_lbl_content_share_in_buddy, str3);
                                }
                                str5 = str4;
                            } else {
                                str5 = getString(C4558R.string.zm_lbl_content_share_in_group, stringBuffer.subSequence(0, stringBuffer.length() - 1));
                            }
                        }
                        if (str5.length() > 0) {
                            this.mContent.setText(str5);
                        } else {
                            this.mContent.setText(getString(C4558R.string.zm_lbl_content_no_share));
                        }
                    }
                }
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:30:0x0043, code lost:
        r6 = th;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:31:0x0044, code lost:
        r1 = null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:35:0x0048, code lost:
        r1 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:36:0x0049, code lost:
        r4 = r1;
        r1 = r6;
        r6 = r4;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Removed duplicated region for block: B:30:0x0043 A[ExcHandler: all (th java.lang.Throwable), Splitter:B:3:0x0005] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.String readAssetsTxt(android.content.Context r5, @androidx.annotation.NonNull java.lang.String r6) {
        /*
            r0 = 0
            android.content.res.AssetManager r5 = r5.getAssets()     // Catch:{ IOException -> 0x005d }
            java.io.InputStream r6 = r5.open(r6)     // Catch:{ Throwable -> 0x0046, all -> 0x0043 }
            int r1 = r6.available()     // Catch:{ Throwable -> 0x002c, all -> 0x0029 }
            byte[] r1 = new byte[r1]     // Catch:{ Throwable -> 0x002c, all -> 0x0029 }
            r6.read(r1)     // Catch:{ Throwable -> 0x002c, all -> 0x0029 }
            r6.close()     // Catch:{ Throwable -> 0x002c, all -> 0x0029 }
            java.lang.String r2 = new java.lang.String     // Catch:{ Throwable -> 0x002c, all -> 0x0029 }
            java.nio.charset.Charset r3 = p021us.zoom.androidlib.util.CompatUtils.getStardardCharSetUTF8()     // Catch:{ Throwable -> 0x002c, all -> 0x0029 }
            r2.<init>(r1, r3)     // Catch:{ Throwable -> 0x002c, all -> 0x0029 }
            if (r6 == 0) goto L_0x0023
            r6.close()     // Catch:{ Throwable -> 0x0046, all -> 0x0043 }
        L_0x0023:
            if (r5 == 0) goto L_0x0028
            r5.close()     // Catch:{ IOException -> 0x005d }
        L_0x0028:
            return r2
        L_0x0029:
            r1 = move-exception
            r2 = r0
            goto L_0x0032
        L_0x002c:
            r1 = move-exception
            throw r1     // Catch:{ all -> 0x002e }
        L_0x002e:
            r2 = move-exception
            r4 = r2
            r2 = r1
            r1 = r4
        L_0x0032:
            if (r6 == 0) goto L_0x0042
            if (r2 == 0) goto L_0x003f
            r6.close()     // Catch:{ Throwable -> 0x003a, all -> 0x0043 }
            goto L_0x0042
        L_0x003a:
            r6 = move-exception
            r2.addSuppressed(r6)     // Catch:{ Throwable -> 0x0046, all -> 0x0043 }
            goto L_0x0042
        L_0x003f:
            r6.close()     // Catch:{ Throwable -> 0x0046, all -> 0x0043 }
        L_0x0042:
            throw r1     // Catch:{ Throwable -> 0x0046, all -> 0x0043 }
        L_0x0043:
            r6 = move-exception
            r1 = r0
            goto L_0x004c
        L_0x0046:
            r6 = move-exception
            throw r6     // Catch:{ all -> 0x0048 }
        L_0x0048:
            r1 = move-exception
            r4 = r1
            r1 = r6
            r6 = r4
        L_0x004c:
            if (r5 == 0) goto L_0x005c
            if (r1 == 0) goto L_0x0059
            r5.close()     // Catch:{ Throwable -> 0x0054 }
            goto L_0x005c
        L_0x0054:
            r5 = move-exception
            r1.addSuppressed(r5)     // Catch:{ IOException -> 0x005d }
            goto L_0x005c
        L_0x0059:
            r5.close()     // Catch:{ IOException -> 0x005d }
        L_0x005c:
            throw r6     // Catch:{ IOException -> 0x005d }
        L_0x005d:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.zipow.videobox.fragment.ZMCodeViewFragment.readAssetsTxt(android.content.Context, java.lang.String):java.lang.String");
    }

    public void onActivityResult(int i, int i2, @Nullable Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == 100 && i2 == -1 && intent != null) {
            Bundle extras = intent.getExtras();
            if (extras != null) {
                String string = extras.getString(ARG_SHARED_MESSAGE_ID);
                if (!StringUtil.isEmptyOrNull(string)) {
                    String stringExtra = intent.getStringExtra(MMSelectSessionAndBuddyFragment.RESULT_ARG_SELECTED_ITEM);
                    if (!StringUtil.isEmptyOrNull(stringExtra)) {
                        ArrayList arrayList = new ArrayList();
                        arrayList.add(stringExtra);
                        if (arrayList.size() > 0) {
                            doShareFile(arrayList, string);
                        }
                    }
                }
            }
        }
    }

    private void doShareFile(ArrayList<String> arrayList, String str) {
        MMFileContentMgr zoomFileContentMgr = PTApp.getInstance().getZoomFileContentMgr();
        if (zoomFileContentMgr != null) {
            ZoomFile fileWithMessageID = zoomFileContentMgr.getFileWithMessageID(this.mSessionId, str);
            if (fileWithMessageID != null) {
                MMShareZoomFileDialogFragment.showShareFileDialog(getFragmentManager(), arrayList, fileWithMessageID.getWebFileID(), str, this.mSessionId, null, 0);
                zoomFileContentMgr.destroyFileObject(fileWithMessageID);
            }
        }
    }
}
