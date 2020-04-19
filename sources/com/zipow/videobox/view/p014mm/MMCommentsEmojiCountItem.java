package com.zipow.videobox.view.p014mm;

import androidx.annotation.NonNull;
import com.zipow.videobox.ptapp.IMProtos.EmojiCountInfo;
import com.zipow.videobox.ptapp.IMProtos.EmojiDetailInfo;
import java.util.ArrayList;
import java.util.List;

/* renamed from: com.zipow.videobox.view.mm.MMCommentsEmojiCountItem */
public class MMCommentsEmojiCountItem {
    private boolean containMe;
    private long count;
    private String emoji;
    private List<EmojiComment> emojiComments;
    private long firstEmojiTime;

    /* renamed from: com.zipow.videobox.view.mm.MMCommentsEmojiCountItem$EmojiComment */
    public static class EmojiComment {
        private String buddyJid;
        private long commentTime;

        public EmojiComment(@NonNull com.zipow.videobox.ptapp.IMProtos.EmojiComment emojiComment) {
            this.buddyJid = emojiComment.getJid();
            this.commentTime = emojiComment.getCommentT();
        }

        public String getBuddyJid() {
            return this.buddyJid;
        }

        public void setBuddyJid(String str) {
            this.buddyJid = str;
        }

        public long getCommentTime() {
            return this.commentTime;
        }

        public void setCommentTime(long j) {
            this.commentTime = j;
        }
    }

    public MMCommentsEmojiCountItem(@NonNull EmojiCountInfo emojiCountInfo) {
        this.emoji = emojiCountInfo.getEmoji();
        this.containMe = emojiCountInfo.getContainMine();
        this.count = emojiCountInfo.getCount();
        this.firstEmojiTime = emojiCountInfo.getFirstEmojiT();
    }

    public void updateEmojiComments(EmojiDetailInfo emojiDetailInfo) {
        this.emojiComments = new ArrayList();
        if (emojiDetailInfo != null && emojiDetailInfo.getCommentsCount() != 0) {
            for (com.zipow.videobox.ptapp.IMProtos.EmojiComment emojiComment : emojiDetailInfo.getCommentsList()) {
                this.emojiComments.add(new EmojiComment(emojiComment));
            }
        }
    }

    public String getEmoji() {
        return this.emoji;
    }

    public void setEmoji(String str) {
        this.emoji = str;
    }

    public long getCount() {
        return this.count;
    }

    public void setCount(long j) {
        this.count = j;
    }

    public long getFirstEmojiTime() {
        return this.firstEmojiTime;
    }

    public void setFirstEmojiTime(long j) {
        this.firstEmojiTime = j;
    }

    public boolean isContainMe() {
        return this.containMe;
    }

    public void setContainMe(boolean z) {
        this.containMe = z;
    }

    public List<EmojiComment> getEmojiComments() {
        return this.emojiComments;
    }
}
