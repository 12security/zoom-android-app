package com.zipow.videobox.login;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.login.model.ZmChinaMultiLogin;
import com.zipow.videobox.login.model.ZmComboMultiLogin;
import com.zipow.videobox.login.model.ZmInternationalMultiLogin;
import p021us.zoom.androidlib.app.ZMFragment;
import p021us.zoom.videomeetings.C4558R;

public class ZMCNLoginPanelFragment extends ZMFragment implements OnClickListener {
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
    }

    @Nullable
    public View onCreateView(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        return layoutInflater.inflate(C4558R.layout.zm_fragment_cn_login, viewGroup, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle bundle) {
        super.onViewCreated(view, bundle);
        view.findViewById(C4558R.C4560id.linkSSOLogin).setOnClickListener(this);
        view.findViewById(C4558R.C4560id.linkWeChatLogin).setOnClickListener(this);
        view.findViewById(C4558R.C4560id.linkQQLogin).setOnClickListener(this);
        view.findViewById(C4558R.C4560id.linkAliPayLogin).setOnClickListener(this);
    }

    public void onDestroyView() {
        super.onDestroyView();
    }

    public void onDestroy() {
        super.onDestroy();
    }

    public void onDetach() {
        super.onDetach();
    }

    public void onClick(View view) {
        int id = view.getId();
        ZmInternationalMultiLogin zmInternationalMultiLogin = ZmComboMultiLogin.getInstance().getmZmInternationalMultiLogin();
        if (zmInternationalMultiLogin != null) {
            if (id == C4558R.C4560id.linkSSOLogin) {
                zmInternationalMultiLogin.onClickBtnLoginSSO();
            }
            ZmChinaMultiLogin zmChinaMultiLogin = ZmComboMultiLogin.getInstance().getmZmChinaMultiLogin();
            if (zmChinaMultiLogin != null) {
                if (id == C4558R.C4560id.linkWeChatLogin) {
                    zmChinaMultiLogin.onClickBtnLoginWeChat();
                } else if (id == C4558R.C4560id.linkQQLogin) {
                    zmChinaMultiLogin.onClickBtnLoginQQ();
                } else if (id == C4558R.C4560id.linkAliPayLogin) {
                    zmChinaMultiLogin.onClickBtnLoginAliPay();
                }
            }
        }
    }
}
