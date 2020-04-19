package com.google.api.client.googleapis.auth.oauth2;

class SystemEnvironmentProvider {
    static final SystemEnvironmentProvider INSTANCE = new SystemEnvironmentProvider();

    SystemEnvironmentProvider() {
    }

    /* access modifiers changed from: 0000 */
    public String getEnv(String str) {
        return System.getenv(str);
    }

    /* access modifiers changed from: 0000 */
    public boolean getEnvEquals(String str, String str2) {
        return System.getenv().containsKey(str) && System.getenv(str).equals(str2);
    }
}
