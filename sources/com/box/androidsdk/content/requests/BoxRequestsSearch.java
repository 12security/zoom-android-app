package com.box.androidsdk.content.requests;

import com.box.androidsdk.content.models.BoxListItems;
import com.box.androidsdk.content.models.BoxSession;
import com.box.androidsdk.content.requests.BoxRequest.Methods;
import com.box.androidsdk.content.utils.BoxDateFormat;
import com.box.androidsdk.content.utils.SdkUtils;
import java.util.Date;
import java.util.Locale;

public class BoxRequestsSearch {

    public static class Search extends BoxRequest<BoxListItems, Search> {
        public static String CONTENT_TYPE_COMMENTS = "comments";
        public static String CONTENT_TYPE_DESCRIPTION = "description";
        public static String CONTENT_TYPE_FILE_CONTENTS = "file_content";
        public static String CONTENT_TYPE_NAME = "name";
        public static String CONTENT_TYPE_TAGS = "tags";
        protected static final String FIELD_ANCESTOR_FOLDER_IDS = "ancestor_folder_ids";
        protected static final String FIELD_CONTENT_TYPES = "content_types";
        protected static final String FIELD_CREATED_AT_RANGE = "created_at_range";
        protected static final String FIELD_FILE_EXTENSIONS = "file_extensions";
        protected static final String FIELD_LIMIT = "limit";
        protected static final String FIELD_OFFSET = "offset";
        protected static final String FIELD_OWNER_USER_IDS = "owner_user_ids";
        protected static final String FIELD_QUERY = "query";
        protected static final String FIELD_SCOPE = "scope";
        protected static final String FIELD_SIZE_RANGE = "size_range";
        protected static final String FIELD_TYPE = "type";
        protected static final String FIELD_UPDATED_AT_RANGE = "updated_at_range";

        public enum Scope {
            USER_CONTENT,
            ENTERPRISE_CONTENT
        }

        public Search(String str, String str2, BoxSession boxSession) {
            super(BoxListItems.class, str2, boxSession);
            limitValueForKey("query", str);
            this.mRequestMethod = Methods.GET;
        }

        public Search limitValueForKey(String str, String str2) {
            this.mQueryMap.put(str, str2);
            return this;
        }

        public Search limitSearchScope(Scope scope) {
            limitValueForKey("scope", scope.name().toLowerCase(Locale.US));
            return this;
        }

        public Search limitFileExtensions(String[] strArr) {
            limitValueForKey(FIELD_FILE_EXTENSIONS, SdkUtils.concatStringWithDelimiter(strArr, PreferencesConstants.COOKIE_DELIMITER));
            return this;
        }

        public Search limitCreationTime(Date date, Date date2) {
            addTimeRange(FIELD_CREATED_AT_RANGE, date, date2);
            return this;
        }

        public Search limitLastUpdateTime(Date date, Date date2) {
            addTimeRange(FIELD_UPDATED_AT_RANGE, date, date2);
            return this;
        }

        public Search limitSizeRange(long j, long j2) {
            limitValueForKey(FIELD_SIZE_RANGE, String.format("%d,%d", new Object[]{Long.valueOf(j), Long.valueOf(j2)}));
            return this;
        }

        public Search limitOwnerUserIds(String[] strArr) {
            limitValueForKey(FIELD_OWNER_USER_IDS, SdkUtils.concatStringWithDelimiter(strArr, PreferencesConstants.COOKIE_DELIMITER));
            return this;
        }

        public Search limitAncestorFolderIds(String[] strArr) {
            limitValueForKey(FIELD_ANCESTOR_FOLDER_IDS, SdkUtils.concatStringWithDelimiter(strArr, PreferencesConstants.COOKIE_DELIMITER));
            return this;
        }

        public Search limitContentTypes(String[] strArr) {
            limitValueForKey(FIELD_CONTENT_TYPES, SdkUtils.concatStringWithDelimiter(strArr, PreferencesConstants.COOKIE_DELIMITER));
            return this;
        }

        public Search limitType(String str) {
            limitValueForKey("type", str);
            return this;
        }

        public Search setLimit(int i) {
            limitValueForKey("limit", String.valueOf(i));
            return this;
        }

        public Search setOffset(int i) {
            limitValueForKey("offset", String.valueOf(i));
            return this;
        }

        public Date getLastUpdatedAtDateRangeFrom() {
            return returnFromDate(FIELD_UPDATED_AT_RANGE);
        }

        public Date getLastUpdatedAtDateRangeTo() {
            return returnToDate(FIELD_UPDATED_AT_RANGE);
        }

        public Date getCreatedAtDateRangeFrom() {
            return returnFromDate(FIELD_CREATED_AT_RANGE);
        }

        public Date getCreatedAtDateRangeTo() {
            return returnToDate(FIELD_CREATED_AT_RANGE);
        }

        public Long getSizeRangeFrom() {
            String str = (String) this.mQueryMap.get(FIELD_SIZE_RANGE);
            if (SdkUtils.isEmptyString(str)) {
                return null;
            }
            return Long.valueOf(Long.parseLong(str.split(PreferencesConstants.COOKIE_DELIMITER)[0]));
        }

        public Long getSizeRangeTo() {
            String str = (String) this.mQueryMap.get(FIELD_SIZE_RANGE);
            if (SdkUtils.isEmptyString(str)) {
                return null;
            }
            return Long.valueOf(Long.parseLong(str.split(PreferencesConstants.COOKIE_DELIMITER)[1]));
        }

        public String[] getOwnerUserIds() {
            return getStringArray(FIELD_OWNER_USER_IDS);
        }

        public String[] getAncestorFolderIds() {
            return getStringArray(FIELD_ANCESTOR_FOLDER_IDS);
        }

        public String[] getContentTypes() {
            return getStringArray(FIELD_CONTENT_TYPES);
        }

        public String getType() {
            return (String) this.mQueryMap.get("type");
        }

        public Integer getLimit() {
            String str = (String) this.mQueryMap.get("limit");
            if (str == null) {
                return null;
            }
            try {
                return Integer.valueOf(Integer.parseInt(str));
            } catch (NumberFormatException unused) {
                return null;
            }
        }

        public Integer getOffset() {
            String str = (String) this.mQueryMap.get("offset");
            if (str == null) {
                return null;
            }
            try {
                return Integer.valueOf(Integer.parseInt(str));
            } catch (NumberFormatException unused) {
                return null;
            }
        }

        public String getQuery() {
            return (String) this.mQueryMap.get("query");
        }

        public String getScope() {
            return (String) this.mQueryMap.get("scope");
        }

        public String[] getFileExtensions() {
            return getStringArray(FIELD_FILE_EXTENSIONS);
        }

        private String[] getStringArray(String str) {
            String str2 = (String) this.mQueryMap.get(str);
            if (SdkUtils.isEmptyString(str2)) {
                return null;
            }
            return str2.split(PreferencesConstants.COOKIE_DELIMITER);
        }

        private Date returnFromDate(String str) {
            String str2 = (String) this.mQueryMap.get(str);
            if (!SdkUtils.isEmptyString(str2)) {
                return BoxDateFormat.getTimeRangeDates(str2)[0];
            }
            return null;
        }

        private Date returnToDate(String str) {
            String str2 = (String) this.mQueryMap.get(str);
            if (!SdkUtils.isEmptyString(str2)) {
                return BoxDateFormat.getTimeRangeDates(str2)[1];
            }
            return null;
        }

        private void addTimeRange(String str, Date date, Date date2) {
            String timeRangeString = BoxDateFormat.getTimeRangeString(date, date2);
            if (!SdkUtils.isEmptyString(timeRangeString)) {
                limitValueForKey(str, timeRangeString);
            }
        }
    }
}
