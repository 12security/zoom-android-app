package com.zipow.videobox;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import androidx.annotation.NonNull;
import com.zipow.videobox.fragment.StarredMessageFragment;
import com.zipow.videobox.util.ActivityStartHelper;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.videomeetings.C4558R;

public class StarredMessageActivity extends ZMActivity implements OnClickListener {
    private static final String KEY_SESSION = "key_session";

    public static void launch(@NonNull Context context, String str) {
        Intent intent = new Intent(context, StarredMessageActivity.class);
        intent.putExtra(KEY_SESSION, str);
        ActivityStartHelper.startActivityForeground(context, intent);
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(C4558R.layout.activity_starred_message);
        UIUtil.renderStatueBar(this, true, C4558R.color.zm_white);
        String stringExtra = getIntent().getStringExtra(KEY_SESSION);
        StarredMessageFragment starredMessageFragment = new StarredMessageFragment();
        Bundle bundle2 = new Bundle();
        bundle2.putString("session", stringExtra);
        starredMessageFragment.setArguments(bundle2);
        findViewById(C4558R.C4560id.zm_starred_message_title_back_btn).setOnClickListener(this);
        getSupportFragmentManager().beginTransaction().replace(C4558R.C4560id.zm_starred_message_fragment_container, starredMessageFragment).commit();
    }

    public void onClick(@NonNull View view) {
        if (view.getId() == C4558R.C4560id.zm_starred_message_title_back_btn) {
            finish();
        }
    }
}
