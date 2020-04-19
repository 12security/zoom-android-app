package p021us.zoom.thirdparty.cnlogin;

import android.app.Activity;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import p021us.zoom.androidlib.p022cn.login.CnLoginCallBack;
import p021us.zoom.androidlib.p022cn.login.CnLoginType;
import p021us.zoom.thirdparty.cnlogin.model.AbstractBaseCnLogin;
import p021us.zoom.thirdparty.cnlogin.model.AbstractCnLogin;
import p021us.zoom.thirdparty.cnlogin.model.AlipayCnLogin;
import p021us.zoom.thirdparty.cnlogin.model.WechatCnLogin;
import p021us.zoom.thirdparty.cnlogin.model.ZmCnAuthResult;

/* renamed from: us.zoom.thirdparty.cnlogin.CnLoginProxy */
public class CnLoginProxy extends AbstractBaseCnLogin {
    private static final String ALIPAY_APPID = "2021001133624097";
    public static String ALIPAY_CURRENT_APPID = "2021001117683305";
    private static final String ALIPAY_PRODUCT_APPID = "2021001117683305";
    private static final String QQ_APPID = "1106196086";
    public static String QQ_CURRENT_APPID = "1106196086";
    private static final String QQ_PRODUCT_APPID = "1106196086";
    private static final String WECHAT_APPID = "wxbcc2200e9019ab4f";
    public static String WECHAT_CURRENT_APPID = "wx168a26bec2905463";
    private static final String WECHAT_PRODUCT_APPID = "wx168a26bec2905463";
    private static final CnLoginProxy ourInstance = new CnLoginProxy();
    @NonNull
    private Map<CnLoginType, AbstractCnLogin> cnLoginMap = new HashMap();

    public static CnLoginProxy getInstance() {
        return ourInstance;
    }

    private CnLoginProxy() {
        this.cnLoginMap.put(CnLoginType.Wechat, new WechatCnLogin());
        this.cnLoginMap.put(CnLoginType.Alipay, new AlipayCnLogin());
    }

    public void init(@NonNull Activity activity) {
        for (AbstractCnLogin abstractCnLogin : this.cnLoginMap.values()) {
            if (abstractCnLogin != null) {
                abstractCnLogin.init(activity);
            }
        }
    }

    public void registerApp() {
        for (AbstractCnLogin abstractCnLogin : this.cnLoginMap.values()) {
            if (abstractCnLogin != null) {
                abstractCnLogin.registerApp();
            }
        }
    }

    public void onResultCallBack(@NonNull ZmCnAuthResult zmCnAuthResult) {
        AbstractCnLogin abstractCnLogin = (AbstractCnLogin) this.cnLoginMap.get(zmCnAuthResult.getAuthType());
        if (abstractCnLogin != null) {
            abstractCnLogin.onResultCallBack(zmCnAuthResult);
        }
    }

    public void init(boolean z, @NonNull Activity activity) {
        if (z) {
            WECHAT_CURRENT_APPID = WECHAT_PRODUCT_APPID;
            QQ_CURRENT_APPID = "1106196086";
            ALIPAY_CURRENT_APPID = ALIPAY_PRODUCT_APPID;
        } else {
            WECHAT_CURRENT_APPID = WECHAT_APPID;
            QQ_CURRENT_APPID = "1106196086";
            ALIPAY_CURRENT_APPID = ALIPAY_APPID;
        }
        init(activity);
    }

    public void unInit() {
        for (AbstractCnLogin abstractCnLogin : this.cnLoginMap.values()) {
            if (abstractCnLogin != null) {
                abstractCnLogin.unInit();
            }
        }
    }

    public void requestAuth(CnLoginType cnLoginType, @NonNull CnLoginCallBack cnLoginCallBack) {
        AbstractCnLogin abstractCnLogin = (AbstractCnLogin) this.cnLoginMap.get(cnLoginType);
        if (abstractCnLogin != null) {
            abstractCnLogin.login(cnLoginCallBack);
        }
    }

    @Nullable
    public AbstractCnLogin getCnLogin(CnLoginType cnLoginType) {
        return (AbstractCnLogin) this.cnLoginMap.get(cnLoginType);
    }
}
