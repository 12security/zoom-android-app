package com.zipow.videobox.view;

public interface ISearchableItem {
    boolean calculateMatchScore(String str);

    int getMatchScore();

    int getPriority();

    long getTimeStamp();

    String getTitle();
}
