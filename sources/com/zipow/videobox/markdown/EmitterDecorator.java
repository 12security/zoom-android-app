package com.zipow.videobox.markdown;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.net.Uri;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.BackgroundColorSpan;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.text.style.LeadingMarginSpan.Standard;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.TypefaceSpan;
import android.text.style.URLSpan;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import com.zipow.videobox.AddrBookItemDetailsActivity;
import com.zipow.videobox.VideoBoxApplication;
import com.zipow.videobox.markdown.URLImageParser.OnUrlDrawableUpdateListener;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.p013mm.ZoomBuddy;
import com.zipow.videobox.ptapp.p013mm.ZoomChatSession;
import com.zipow.videobox.ptapp.p013mm.ZoomMessenger;
import com.zipow.videobox.sip.server.CmmSIPCallManager;
import com.zipow.videobox.util.ActivityStartHelper;
import com.zipow.videobox.view.IMAddrBookItem;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.videomeetings.C4558R;

public class EmitterDecorator {
    public static boolean emitterImage(@Nullable SpannableStringBuilder spannableStringBuilder, SpannableStringBuilder spannableStringBuilder2, @NonNull TextView textView, String str, OnUrlDrawableUpdateListener onUrlDrawableUpdateListener) {
        if (spannableStringBuilder == null || TextUtils.isEmpty(spannableStringBuilder2)) {
            return false;
        }
        int length = spannableStringBuilder.length();
        URLImageParser uRLImageParser = new URLImageParser(textView.getContext(), (int) textView.getTextSize());
        uRLImageParser.setOnUrlDrawableUpdateListener(onUrlDrawableUpdateListener);
        ImageSpan imageSpan = new ImageSpan(uRLImageParser.getDrawable(str), 0);
        spannableStringBuilder.append(spannableStringBuilder2);
        spannableStringBuilder.setSpan(imageSpan, length, spannableStringBuilder.length(), 33);
        return true;
    }

    public static boolean emitterLargeImage(@Nullable SpannableStringBuilder spannableStringBuilder, SpannableStringBuilder spannableStringBuilder2, @NonNull TextView textView, String str, OnUrlDrawableUpdateListener onUrlDrawableUpdateListener) {
        if (spannableStringBuilder == null || TextUtils.isEmpty(spannableStringBuilder2)) {
            return false;
        }
        int length = spannableStringBuilder.length();
        URLImageParser uRLImageParser = new URLImageParser(textView.getContext());
        uRLImageParser.setOnUrlDrawableUpdateListener(onUrlDrawableUpdateListener);
        ImageSpan imageSpan = new ImageSpan(uRLImageParser.getDrawable(str), 0);
        spannableStringBuilder.append(spannableStringBuilder2);
        spannableStringBuilder.setSpan(imageSpan, length, spannableStringBuilder.length(), 33);
        return true;
    }

    public static boolean emitterBold(@Nullable SpannableStringBuilder spannableStringBuilder, SpannableStringBuilder spannableStringBuilder2) {
        if (spannableStringBuilder == null || TextUtils.isEmpty(spannableStringBuilder2)) {
            return false;
        }
        StyleSpan styleSpan = new StyleSpan(1);
        int length = spannableStringBuilder.length();
        spannableStringBuilder.append(spannableStringBuilder2);
        spannableStringBuilder.setSpan(styleSpan, length, spannableStringBuilder.length(), 33);
        return true;
    }

    public static boolean emitterItalic(@Nullable SpannableStringBuilder spannableStringBuilder, SpannableStringBuilder spannableStringBuilder2) {
        if (spannableStringBuilder == null || TextUtils.isEmpty(spannableStringBuilder2)) {
            return false;
        }
        StyleSpan styleSpan = new StyleSpan(2);
        int length = spannableStringBuilder.length();
        spannableStringBuilder.append(spannableStringBuilder2);
        spannableStringBuilder.setSpan(styleSpan, length, spannableStringBuilder.length(), 33);
        return true;
    }

    public static boolean emitterMonospace(@Nullable SpannableStringBuilder spannableStringBuilder, SpannableStringBuilder spannableStringBuilder2) {
        if (spannableStringBuilder == null || TextUtils.isEmpty(spannableStringBuilder2) || VideoBoxApplication.getGlobalContext() == null) {
            return false;
        }
        int length = spannableStringBuilder.length();
        TypefaceSpan typefaceSpan = new TypefaceSpan("monospace");
        spannableStringBuilder.append(spannableStringBuilder2);
        spannableStringBuilder.setSpan(typefaceSpan, length, spannableStringBuilder.length(), 33);
        spannableStringBuilder.setSpan(new BackgroundImageSpan(), length, spannableStringBuilder.length(), 33);
        return true;
    }

    public static boolean emitterMonospace(int i, @NonNull SpannableStringBuilder spannableStringBuilder) {
        if (i < 0 || TextUtils.isEmpty(spannableStringBuilder) || i >= spannableStringBuilder.length() || VideoBoxApplication.getGlobalContext() == null) {
            return false;
        }
        spannableStringBuilder.setSpan(new BackgroundImageSpan(), i, spannableStringBuilder.length(), 33);
        return true;
    }

    public static boolean emitterQuotes(int i, @NonNull SpannableStringBuilder spannableStringBuilder) {
        if (i < 0 || TextUtils.isEmpty(spannableStringBuilder) || i >= spannableStringBuilder.length()) {
            return false;
        }
        Standard standard = new Standard(UIUtil.dip2px(VideoBoxApplication.getGlobalContext(), 16.0f));
        spannableStringBuilder.setSpan(new StyleSpan(2), i, spannableStringBuilder.length(), 33);
        spannableStringBuilder.setSpan(standard, i, spannableStringBuilder.length(), 33);
        return true;
    }

    public static boolean emitterQuotes(@Nullable SpannableStringBuilder spannableStringBuilder, SpannableStringBuilder spannableStringBuilder2) {
        if (spannableStringBuilder == null || TextUtils.isEmpty(spannableStringBuilder2) || VideoBoxApplication.getGlobalContext() == null) {
            return false;
        }
        int length = spannableStringBuilder.length();
        Standard standard = new Standard(UIUtil.dip2px(VideoBoxApplication.getGlobalContext(), 16.0f));
        StyleSpan styleSpan = new StyleSpan(2);
        spannableStringBuilder.append(spannableStringBuilder2);
        spannableStringBuilder.setSpan(styleSpan, length, spannableStringBuilder.length(), 33);
        spannableStringBuilder.setSpan(standard, length, spannableStringBuilder.length(), 33);
        return true;
    }

    public static boolean emitterStrikethrough(@Nullable SpannableStringBuilder spannableStringBuilder, SpannableStringBuilder spannableStringBuilder2) {
        if (spannableStringBuilder == null || TextUtils.isEmpty(spannableStringBuilder2)) {
            return false;
        }
        StrikethroughSpan strikethroughSpan = new StrikethroughSpan();
        int length = spannableStringBuilder.length();
        spannableStringBuilder.append(spannableStringBuilder2);
        spannableStringBuilder.setSpan(strikethroughSpan, length, spannableStringBuilder.length(), 33);
        return true;
    }

    public static boolean emitterHyperLink(@Nullable SpannableStringBuilder spannableStringBuilder, SpannableStringBuilder spannableStringBuilder2, String str) {
        if (spannableStringBuilder == null || TextUtils.isEmpty(spannableStringBuilder2) || TextUtils.isEmpty(str) || VideoBoxApplication.getGlobalContext() == null) {
            return false;
        }
        int length = spannableStringBuilder.length();
        spannableStringBuilder.append(spannableStringBuilder2);
        spannableStringBuilder.setSpan(new URLSpan(str) {
            public void onClick(View view) {
                UIUtil.openURL(view.getContext(), getURL());
            }

            public void updateDrawState(@NonNull TextPaint textPaint) {
                textPaint.setColor(ContextCompat.getColor(VideoBoxApplication.getGlobalContext(), C4558R.color.zm_template_link));
                textPaint.setUnderlineText(true);
            }
        }, length, spannableStringBuilder.length(), 33);
        return true;
    }

    public static boolean emitterSipcall(@Nullable SpannableStringBuilder spannableStringBuilder, SpannableStringBuilder spannableStringBuilder2, @NonNull final String str) {
        if (spannableStringBuilder == null || TextUtils.isEmpty(spannableStringBuilder2) || TextUtils.isEmpty(str) || VideoBoxApplication.getGlobalContext() == null) {
            return false;
        }
        int length = spannableStringBuilder.length();
        spannableStringBuilder.append(spannableStringBuilder2);
        spannableStringBuilder.setSpan(new ClickableSpan() {
            public void onClick(@NonNull View view) {
                CmmSIPCallManager.getInstance().callPeer(str);
            }

            public void updateDrawState(@NonNull TextPaint textPaint) {
                textPaint.setColor(ContextCompat.getColor(VideoBoxApplication.getGlobalContext(), C4558R.color.zm_template_link));
                textPaint.setUnderlineText(false);
            }
        }, length, spannableStringBuilder.length(), 33);
        return true;
    }

    public static boolean emitterMailto(@Nullable SpannableStringBuilder spannableStringBuilder, SpannableStringBuilder spannableStringBuilder2, @NonNull final String str) {
        if (spannableStringBuilder == null || TextUtils.isEmpty(spannableStringBuilder2) || TextUtils.isEmpty(str) || VideoBoxApplication.getGlobalContext() == null) {
            return false;
        }
        int length = spannableStringBuilder.length();
        spannableStringBuilder.append(spannableStringBuilder2);
        if (!str.startsWith("mailto:")) {
            StringBuilder sb = new StringBuilder();
            sb.append("mailto:");
            sb.append(str);
            str = sb.toString();
        }
        spannableStringBuilder.setSpan(new ClickableSpan() {
            public void onClick(@NonNull View view) {
                ActivityStartHelper.startActivityForeground(view.getContext(), new Intent("android.intent.action.SENDTO", Uri.parse(str)));
            }

            public void updateDrawState(@NonNull TextPaint textPaint) {
                textPaint.setColor(ContextCompat.getColor(VideoBoxApplication.getGlobalContext(), C4558R.color.zm_template_link));
                textPaint.setUnderlineText(false);
            }
        }, length, spannableStringBuilder.length(), 33);
        return true;
    }

    public static boolean emitterHyperlink(@Nullable SpannableStringBuilder spannableStringBuilder, @NonNull String str) {
        final String str2;
        if (spannableStringBuilder == null || TextUtils.isEmpty(str) || VideoBoxApplication.getGlobalContext() == null) {
            return false;
        }
        int length = spannableStringBuilder.length();
        String[] split = str.split("\\|");
        if (split.length > 1) {
            String str3 = split[0];
            str2 = str3;
            str = split[1];
        } else {
            str2 = str;
        }
        spannableStringBuilder.append(str);
        if (str2.startsWith("sip")) {
            spannableStringBuilder.setSpan(new ClickableSpan() {
                public void onClick(@NonNull View view) {
                }

                public void updateDrawState(@NonNull TextPaint textPaint) {
                    textPaint.setColor(ContextCompat.getColor(VideoBoxApplication.getGlobalContext(), C4558R.color.zm_template_link));
                    textPaint.setUnderlineText(false);
                }
            }, length, spannableStringBuilder.length(), 33);
        } else if (str2.startsWith("mailto")) {
            spannableStringBuilder.setSpan(new ClickableSpan() {
                public void onClick(@NonNull View view) {
                    ActivityStartHelper.startActivityForeground(view.getContext(), new Intent("android.intent.action.SENDTO", Uri.parse(str2)));
                }

                public void updateDrawState(@NonNull TextPaint textPaint) {
                    textPaint.setColor(ContextCompat.getColor(VideoBoxApplication.getGlobalContext(), C4558R.color.zm_template_link));
                    textPaint.setUnderlineText(false);
                }
            }, length, spannableStringBuilder.length(), 33);
        } else {
            spannableStringBuilder.setSpan(new URLSpan(str2) {
                public void onClick(View view) {
                    UIUtil.openURL(view.getContext(), getURL());
                }

                public void updateDrawState(@NonNull TextPaint textPaint) {
                    textPaint.setColor(ContextCompat.getColor(VideoBoxApplication.getGlobalContext(), C4558R.color.zm_template_link));
                    textPaint.setUnderlineText(false);
                }
            }, length, spannableStringBuilder.length(), 33);
        }
        return true;
    }

    public static boolean emitterProfileLink(@Nullable SpannableStringBuilder spannableStringBuilder, SpannableStringBuilder spannableStringBuilder2, final String str) {
        if (spannableStringBuilder == null || TextUtils.isEmpty(spannableStringBuilder2) || TextUtils.isEmpty(str) || VideoBoxApplication.getGlobalContext() == null) {
            return false;
        }
        int length = spannableStringBuilder.length();
        spannableStringBuilder.append(spannableStringBuilder2);
        spannableStringBuilder.setSpan(new ProfileLinkSpan(ContextCompat.getColor(VideoBoxApplication.getGlobalContext(), C4558R.color.zm_ui_kit_color_blue_0E71EB), str), 0, spannableStringBuilder.length(), 33);
        spannableStringBuilder.setSpan(new ForegroundColorSpan(ContextCompat.getColor(VideoBoxApplication.getGlobalContext(), C4558R.color.zm_white)), 0, spannableStringBuilder.length(), 33);
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            ZoomChatSession sessionById = zoomMessenger.getSessionById(str);
            final ZoomBuddy myself = zoomMessenger.getMyself();
            if (sessionById != null) {
                if (!sessionById.isGroup()) {
                    final ZoomBuddy sessionBuddy = sessionById.getSessionBuddy();
                    spannableStringBuilder.setSpan(new ClickableSpan() {
                        public void onClick(@NonNull View view) {
                            ZoomBuddy zoomBuddy = myself;
                            if (zoomBuddy != null && !TextUtils.equals(zoomBuddy.getJid(), str)) {
                                for (Context context = view.getContext(); context instanceof ContextWrapper; context = ((ContextWrapper) context).getBaseContext()) {
                                    if (context instanceof ZMActivity) {
                                        AddrBookItemDetailsActivity.show((ZMActivity) context, IMAddrBookItem.fromZoomBuddy(sessionBuddy), 0);
                                        return;
                                    }
                                }
                            }
                        }

                        public void updateDrawState(@NonNull TextPaint textPaint) {
                            textPaint.setColor(ContextCompat.getColor(VideoBoxApplication.getGlobalContext(), C4558R.color.zm_white));
                            textPaint.setUnderlineText(false);
                        }
                    }, length, spannableStringBuilder.length(), 33);
                } else {
                    spannableStringBuilder.setSpan(new ClickableSpan() {
                        public void onClick(@NonNull View view) {
                        }

                        public void updateDrawState(@NonNull TextPaint textPaint) {
                            textPaint.setColor(ContextCompat.getColor(VideoBoxApplication.getGlobalContext(), C4558R.color.zm_white));
                            textPaint.setUnderlineText(false);
                        }
                    }, length, spannableStringBuilder.length(), 33);
                }
            }
        }
        return true;
    }

    public static boolean emitterProfileLink(@Nullable SpannableStringBuilder spannableStringBuilder, @NonNull String str) {
        if (spannableStringBuilder == null || TextUtils.isEmpty(str) || VideoBoxApplication.getGlobalContext() == null) {
            return false;
        }
        int length = spannableStringBuilder.length();
        String[] split = str.split("\\|");
        if (split.length < 2) {
            spannableStringBuilder.append(str);
        } else {
            spannableStringBuilder.append(split[1]);
            String str2 = split[0];
            if (!TextUtils.isEmpty(str2)) {
                ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
                if (zoomMessenger != null) {
                    ZoomChatSession sessionById = zoomMessenger.getSessionById(str2);
                    if (sessionById != null && !sessionById.isGroup()) {
                        final ZoomBuddy sessionBuddy = sessionById.getSessionBuddy();
                        spannableStringBuilder.setSpan(new ClickableSpan() {
                            public void onClick(@NonNull View view) {
                                for (Context context = view.getContext(); context instanceof ContextWrapper; context = ((ContextWrapper) context).getBaseContext()) {
                                    if (context instanceof ZMActivity) {
                                        AddrBookItemDetailsActivity.show((ZMActivity) context, IMAddrBookItem.fromZoomBuddy(sessionBuddy), 0);
                                        return;
                                    }
                                }
                            }

                            public void updateDrawState(@NonNull TextPaint textPaint) {
                                textPaint.setColor(ContextCompat.getColor(VideoBoxApplication.getGlobalContext(), C4558R.color.zm_template_link));
                                textPaint.setUnderlineText(false);
                            }
                        }, length, spannableStringBuilder.length(), 33);
                    }
                }
            }
        }
        return true;
    }

    public static boolean emitterMentionLink(@Nullable SpannableStringBuilder spannableStringBuilder, SpannableStringBuilder spannableStringBuilder2, String str, boolean z) {
        if (spannableStringBuilder == null || TextUtils.isEmpty(spannableStringBuilder2) || VideoBoxApplication.getGlobalContext() == null) {
            return false;
        }
        int color = ContextCompat.getColor(VideoBoxApplication.getGlobalContext(), C4558R.color.zm_template_link);
        if (TextUtils.isEmpty(str)) {
            str = "";
        }
        MentionLinkSpan mentionLinkSpan = new MentionLinkSpan(color, str);
        spannableStringBuilder.append(spannableStringBuilder2);
        spannableStringBuilder.setSpan(mentionLinkSpan, 0, spannableStringBuilder.length(), 33);
        return true;
    }

    public static boolean emitterMentionLink(@Nullable SpannableStringBuilder spannableStringBuilder, @NonNull String str) {
        if (spannableStringBuilder == null || TextUtils.isEmpty(str) || VideoBoxApplication.getGlobalContext() == null) {
            return false;
        }
        BackgroundColorSpan backgroundColorSpan = new BackgroundColorSpan(ContextCompat.getColor(VideoBoxApplication.getGlobalContext(), C4558R.color.zm_template_link));
        int length = spannableStringBuilder.length();
        String[] split = str.split("\\|");
        if (split.length < 2) {
            spannableStringBuilder.append(str);
        } else {
            spannableStringBuilder.append("@").append(split[1]);
        }
        spannableStringBuilder.setSpan(backgroundColorSpan, length, spannableStringBuilder.length(), 33);
        return true;
    }
}
