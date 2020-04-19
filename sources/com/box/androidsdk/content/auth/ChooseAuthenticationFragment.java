package com.box.androidsdk.content.auth;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import com.box.androidsdk.content.auth.AuthenticatedAccountsAdapter.DifferentAuthenticationInfo;
import com.box.androidsdk.content.auth.BoxAuthentication.BoxAuthenticationInfo;
import com.box.sdk.android.C0469R;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

public class ChooseAuthenticationFragment extends Fragment {
    private static final String EXTRA_BOX_AUTHENTICATION_INFOS = "boxAuthenticationInfos";
    private ListView mListView;

    public interface OnAuthenticationChosen {
        void onAuthenticationChosen(BoxAuthenticationInfo boxAuthenticationInfo);

        void onDifferentAuthenticationChosen();
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        ArrayList authenticationInfoList = getAuthenticationInfoList();
        View inflate = layoutInflater.inflate(C0469R.layout.boxsdk_choose_auth_activity, null);
        this.mListView = (ListView) inflate.findViewById(C0469R.C0471id.boxsdk_accounts_list);
        if (authenticationInfoList == null) {
            getActivity().getFragmentManager().beginTransaction().remove(this).commit();
        } else {
            this.mListView.setAdapter(new AuthenticatedAccountsAdapter(getActivity(), C0469R.layout.boxsdk_list_item_account, authenticationInfoList));
            this.mListView.setOnItemClickListener(new OnItemClickListener() {
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                    if (adapterView.getAdapter() instanceof AuthenticatedAccountsAdapter) {
                        BoxAuthenticationInfo item = ((AuthenticatedAccountsAdapter) adapterView.getAdapter()).getItem(i);
                        if (item instanceof DifferentAuthenticationInfo) {
                            if (ChooseAuthenticationFragment.this.getActivity() instanceof OnAuthenticationChosen) {
                                ((OnAuthenticationChosen) ChooseAuthenticationFragment.this.getActivity()).onDifferentAuthenticationChosen();
                            }
                        } else if (ChooseAuthenticationFragment.this.getActivity() instanceof OnAuthenticationChosen) {
                            ((OnAuthenticationChosen) ChooseAuthenticationFragment.this.getActivity()).onAuthenticationChosen(item);
                        }
                    }
                }
            });
        }
        return inflate;
    }

    public ArrayList<BoxAuthenticationInfo> getAuthenticationInfoList() {
        if (getArguments() == null || getArguments().getCharSequenceArrayList(EXTRA_BOX_AUTHENTICATION_INFOS) == null) {
            Map storedAuthInfo = BoxAuthentication.getInstance().getStoredAuthInfo(getActivity());
            if (storedAuthInfo == null) {
                return null;
            }
            ArrayList<BoxAuthenticationInfo> arrayList = new ArrayList<>(storedAuthInfo.size());
            for (String str : storedAuthInfo.keySet()) {
                arrayList.add(storedAuthInfo.get(str));
            }
            return arrayList;
        }
        ArrayList charSequenceArrayList = getArguments().getCharSequenceArrayList(EXTRA_BOX_AUTHENTICATION_INFOS);
        ArrayList<BoxAuthenticationInfo> arrayList2 = new ArrayList<>(charSequenceArrayList.size());
        Iterator it = charSequenceArrayList.iterator();
        while (it.hasNext()) {
            CharSequence charSequence = (CharSequence) it.next();
            BoxAuthenticationInfo boxAuthenticationInfo = new BoxAuthenticationInfo();
            boxAuthenticationInfo.createFromJson(charSequence.toString());
            arrayList2.add(boxAuthenticationInfo);
        }
        return arrayList2;
    }

    public static ChooseAuthenticationFragment createAuthenticationActivity(Context context) {
        return new ChooseAuthenticationFragment();
    }

    public static ChooseAuthenticationFragment createChooseAuthenticationFragment(Context context, ArrayList<BoxAuthenticationInfo> arrayList) {
        ChooseAuthenticationFragment createAuthenticationActivity = createAuthenticationActivity(context);
        Bundle arguments = createAuthenticationActivity.getArguments();
        if (arguments == null) {
            arguments = new Bundle();
        }
        ArrayList arrayList2 = new ArrayList(arrayList.size());
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            arrayList2.add(((BoxAuthenticationInfo) it.next()).toJson());
        }
        arguments.putCharSequenceArrayList(EXTRA_BOX_AUTHENTICATION_INFOS, arrayList2);
        createAuthenticationActivity.setArguments(arguments);
        return createAuthenticationActivity;
    }
}
