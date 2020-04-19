package com.zipow.videobox;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.p013mm.ZoomChatSession;
import com.zipow.videobox.ptapp.p013mm.ZoomMessage;
import com.zipow.videobox.ptapp.p013mm.ZoomMessenger;
import com.zipow.videobox.util.ActivityStartHelper;
import com.zipow.videobox.view.p014mm.MMContentFileViewerFragment;
import com.zipow.videobox.view.p014mm.MMMessageItem;
import java.util.ArrayList;
import java.util.List;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.videomeetings.C4558R;

public class MMImageListActivity extends ZMActivity {
    public static final String ARG_MESSAGE_ID = "message_id";
    public static final String ARG_MESSAGE_IDS = "message_ids";
    public static final String ARG_SESSION_ID = "session_id";
    /* access modifiers changed from: private */
    @NonNull
    public List<Fragment> fragments = new ArrayList();
    private ImagesViewPagerAdapter mAdapter;
    private ViewPager mViewPager;
    private String messageID;
    private List<String> messageIDs = new ArrayList();
    private String sessionID;

    class ImagesViewPagerAdapter extends FragmentPagerAdapter {
        public ImagesViewPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        public Fragment getItem(int i) {
            return (Fragment) MMImageListActivity.this.fragments.get(i);
        }

        public int getCount() {
            return MMImageListActivity.this.fragments.size();
        }
    }

    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
        return false;
    }

    public static void launch(@Nullable Context context, @Nullable String str, @Nullable String str2, @Nullable List<MMMessageItem> list) {
        if (context != null && str != null && list != null && str2 != null) {
            ArrayList arrayList = new ArrayList();
            for (MMMessageItem mMMessageItem : list) {
                arrayList.add(mMMessageItem.messageXMPPId);
            }
            launch(context, str, str2, arrayList);
        }
    }

    public static void launch(@Nullable Context context, @Nullable String str, @Nullable String str2, @Nullable ArrayList<String> arrayList) {
        if (context != null && str != null && arrayList != null && str2 != null) {
            Intent intent = new Intent(context, MMImageListActivity.class);
            intent.putStringArrayListExtra(ARG_MESSAGE_IDS, arrayList);
            intent.putExtra("session_id", str);
            intent.putExtra(ARG_MESSAGE_ID, str2);
            ActivityStartHelper.startActivityForeground(context, intent);
        }
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(C4558R.layout.activity_mmimage_list);
        this.mViewPager = (ViewPager) findViewById(C4558R.C4560id.mm_image_list_viewPager);
        this.mAdapter = new ImagesViewPagerAdapter(getSupportFragmentManager());
        this.mViewPager.setAdapter(this.mAdapter);
        Intent intent = getIntent();
        if (intent != null) {
            this.messageIDs = intent.getStringArrayListExtra(ARG_MESSAGE_IDS);
            this.sessionID = intent.getStringExtra("session_id");
            this.messageID = intent.getStringExtra(ARG_MESSAGE_ID);
        }
        List filterList = filterList(this.sessionID, this.messageIDs);
        List buildFragments = buildFragments(filterList);
        if (buildFragments != null && !buildFragments.isEmpty()) {
            int i = 0;
            for (int i2 = 0; i2 < filterList.size(); i2++) {
                String messageXMPPGuid = ((ZoomMessage) filterList.get(i2)).getMessageXMPPGuid();
                if (messageXMPPGuid != null && messageXMPPGuid.equalsIgnoreCase(this.messageID)) {
                    i = i2;
                }
            }
            this.fragments.addAll(buildFragments);
            this.mAdapter.notifyDataSetChanged();
            this.mViewPager.setCurrentItem(i);
        }
    }

    @Nullable
    private List<ZoomMessage> filterList(@Nullable String str, @Nullable List<String> list) {
        if (str == null || list == null) {
            return null;
        }
        ArrayList arrayList = new ArrayList();
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            ZoomChatSession sessionById = zoomMessenger.getSessionById(str);
            if (sessionById != null) {
                for (String messageByXMPPGuid : list) {
                    ZoomMessage messageByXMPPGuid2 = sessionById.getMessageByXMPPGuid(messageByXMPPGuid);
                    if (messageByXMPPGuid2 != null) {
                        int messageType = messageByXMPPGuid2.getMessageType();
                        if (messageType == 1 || messageType == 5 || messageType == 12 || messageType == 6) {
                            arrayList.add(messageByXMPPGuid2);
                        }
                    }
                }
            }
        }
        return arrayList;
    }

    @Nullable
    private List<Fragment> buildFragments(@Nullable List<ZoomMessage> list) {
        if (list == null || list.isEmpty()) {
            return null;
        }
        ArrayList arrayList = new ArrayList();
        for (ZoomMessage zoomMessage : list) {
            Bundle bundle = new Bundle();
            bundle.putString("messageId", zoomMessage.getMessageID());
            bundle.putString("sessionId", this.sessionID);
            bundle.putString(MMContentFileViewerFragment.ARGS_ZOOM_MESSAGE_XMPP_ID, zoomMessage.getMessageXMPPGuid());
            MMContentFileViewerFragment mMContentFileViewerFragment = new MMContentFileViewerFragment();
            mMContentFileViewerFragment.setArguments(bundle);
            arrayList.add(mMContentFileViewerFragment);
        }
        return arrayList;
    }
}
