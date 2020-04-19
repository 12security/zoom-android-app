package com.google.api.services.drive.model;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Data;
import com.google.api.client.util.Key;
import java.util.List;

public final class TeamDriveList extends GenericJson {
    @Key
    private String kind;
    @Key
    private String nextPageToken;
    @Key
    private List<TeamDrive> teamDrives;

    static {
        Data.nullOf(TeamDrive.class);
    }

    public String getKind() {
        return this.kind;
    }

    public TeamDriveList setKind(String str) {
        this.kind = str;
        return this;
    }

    public String getNextPageToken() {
        return this.nextPageToken;
    }

    public TeamDriveList setNextPageToken(String str) {
        this.nextPageToken = str;
        return this;
    }

    public List<TeamDrive> getTeamDrives() {
        return this.teamDrives;
    }

    public TeamDriveList setTeamDrives(List<TeamDrive> list) {
        this.teamDrives = list;
        return this;
    }

    public TeamDriveList set(String str, Object obj) {
        return (TeamDriveList) super.set(str, obj);
    }

    public TeamDriveList clone() {
        return (TeamDriveList) super.clone();
    }
}
