package com.zipow.videobox.poll;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import p021us.zoom.androidlib.app.ZMFragment;
import p021us.zoom.videomeetings.C4558R;

public class PollingResultFragment extends ZMFragment implements OnClickListener {
    public static final String ARG_POLLING_ID = "pollingId";
    private static final String TAG = "PollingResultFragment";
    private View mBtnBack;
    @Nullable
    private String mPollingId;
    private PollingResultListView mResultListView;
    private TextView mTxtTitle;

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Bundle arguments = getArguments();
        if (arguments != null) {
            this.mPollingId = arguments.getString("pollingId");
        }
    }

    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(C4558R.layout.zm_polling_result_view, viewGroup, false);
        this.mBtnBack = inflate.findViewById(C4558R.C4560id.btnBack);
        this.mTxtTitle = (TextView) inflate.findViewById(C4558R.C4560id.txtTitle);
        this.mResultListView = (PollingResultListView) inflate.findViewById(C4558R.C4560id.resultListView);
        this.mBtnBack.setOnClickListener(this);
        initView(layoutInflater, bundle);
        return inflate;
    }

    private void initView(LayoutInflater layoutInflater, Bundle bundle) {
        PollingResultActivity pollingResultActivity = (PollingResultActivity) getActivity();
        if (pollingResultActivity != null) {
            IPollingDoc pollingDoc = getPollingDoc();
            if (pollingDoc != null) {
                String pollingName = pollingDoc.getPollingName();
                if (pollingName == null) {
                    pollingName = "";
                }
                this.mTxtTitle.setText(pollingName);
                IPollingMgr pollingMgr = pollingResultActivity.getPollingMgr();
                boolean z = true;
                if (!(pollingMgr == null || pollingMgr.getPollingRole() == PollingRole.Host)) {
                    z = false;
                }
                this.mResultListView.loadPollingResult(pollingDoc, z);
            }
        }
    }

    private IPollingDoc getPollingDoc() {
        if (this.mPollingId == null) {
            return null;
        }
        PollingResultActivity pollingResultActivity = (PollingResultActivity) getActivity();
        if (pollingResultActivity == null) {
            return null;
        }
        IPollingMgr pollingMgr = pollingResultActivity.getPollingMgr();
        if (pollingMgr == null) {
            return null;
        }
        IPollingDoc pollingDocById = pollingMgr.getPollingDocById(this.mPollingId);
        if (pollingDocById == null) {
            return null;
        }
        return pollingDocById;
    }

    public void onClick(View view) {
        if (view == this.mBtnBack) {
            onClickBtnBack();
        }
    }

    private void onClickBtnBack() {
        PollingResultActivity pollingResultActivity = (PollingResultActivity) getActivity();
        if (pollingResultActivity != null) {
            pollingResultActivity.finish();
        }
    }
}
