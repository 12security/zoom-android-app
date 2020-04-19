package com.zipow.videobox;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IPTService extends IInterface {

    public static class Default implements IPTService {
        public String[] ABContactsHelper_getMatchedPhoneNumbers() throws RemoteException {
            return null;
        }

        public int ABContactsHelper_inviteABContacts(String[] strArr, String str) throws RemoteException {
            return 0;
        }

        public byte[] FavoriteMgr_getFavoriteListWithFilter(String str) throws RemoteException {
            return null;
        }

        public String FavoriteMgr_getLocalPicturePath(String str) throws RemoteException {
            return null;
        }

        public IBinder asBinder() {
            return null;
        }

        public boolean callOutRoomSystem(String str, int i) throws RemoteException {
            return false;
        }

        public boolean cancelCallOut() throws RemoteException {
            return false;
        }

        public boolean cancelCallOutRoomSystem() throws RemoteException {
            return false;
        }

        public boolean disablePhoneAudio() throws RemoteException {
            return false;
        }

        public String[] filterBuddyWithInput(String str) throws RemoteException {
            return null;
        }

        public int getBuddyItemCount() throws RemoteException {
            return 0;
        }

        public int getCallOutStatus() throws RemoteException {
            return 0;
        }

        public String[] getH323Gateway() throws RemoteException {
            return null;
        }

        public String getH323Password() throws RemoteException {
            return null;
        }

        public String[] getLatestMeetingInfo() throws RemoteException {
            return null;
        }

        public int getPTLoginType() throws RemoteException {
            return 0;
        }

        public int inviteBuddiesToConf(String[] strArr, String[] strArr2, String str, long j, String str2) throws RemoteException {
            return 0;
        }

        public boolean inviteCallOutUser(String str, String str2) throws RemoteException {
            return false;
        }

        public boolean isAuthenticating() throws RemoteException {
            return false;
        }

        public boolean isCallOutInProgress() throws RemoteException {
            return false;
        }

        public boolean isIMSignedIn() throws RemoteException {
            return false;
        }

        public boolean isPTAppAtFront() throws RemoteException {
            return false;
        }

        public boolean isSdkNeedWaterMark() throws RemoteException {
            return false;
        }

        public boolean isSignedIn() throws RemoteException {
            return false;
        }

        public boolean isTaiWanZH() throws RemoteException {
            return false;
        }

        public void logout() throws RemoteException {
        }

        public void notifyLeaveAndPerformAction(int i, int i2, int i3) throws RemoteException {
        }

        public void onBOStatusChangeComplete() throws RemoteException {
        }

        public void onBOStatusChangeStart(boolean z, int i, String str) throws RemoteException {
        }

        public void onCallOutStatus(int i) throws RemoteException {
        }

        public void onConfUIMoveToBackground() throws RemoteException {
        }

        public void onConfUIMoveToFront(String str) throws RemoteException {
        }

        public void onJoinConfMeetingStatus(boolean z, boolean z2) throws RemoteException {
        }

        public boolean presentToRoom(int i, String str, long j, boolean z) throws RemoteException {
            return false;
        }

        public void reloadAllBuddyItems() throws RemoteException {
        }

        public boolean sendMeetingParingCode(long j, String str) throws RemoteException {
            return false;
        }

        public void sendMessage(byte[] bArr) throws RemoteException {
        }

        public void sendMessageFromSip(byte[] bArr) throws RemoteException {
        }

        public void setNeedCheckSwitchCall(boolean z) throws RemoteException {
        }

        public void showNeedUpdate() throws RemoteException {
        }

        public void showRateZoomDialog() throws RemoteException {
        }

        public void stopPresentToRoom(boolean z) throws RemoteException {
        }
    }

    public static abstract class Stub extends Binder implements IPTService {
        private static final String DESCRIPTOR = "com.zipow.videobox.IPTService";
        static final int TRANSACTION_ABContactsHelper_getMatchedPhoneNumbers = 14;
        static final int TRANSACTION_ABContactsHelper_inviteABContacts = 15;
        static final int TRANSACTION_FavoriteMgr_getFavoriteListWithFilter = 13;
        static final int TRANSACTION_FavoriteMgr_getLocalPicturePath = 12;
        static final int TRANSACTION_callOutRoomSystem = 30;
        static final int TRANSACTION_cancelCallOut = 23;
        static final int TRANSACTION_cancelCallOutRoomSystem = 31;
        static final int TRANSACTION_disablePhoneAudio = 40;
        static final int TRANSACTION_filterBuddyWithInput = 9;
        static final int TRANSACTION_getBuddyItemCount = 7;
        static final int TRANSACTION_getCallOutStatus = 25;
        static final int TRANSACTION_getH323Gateway = 27;
        static final int TRANSACTION_getH323Password = 28;
        static final int TRANSACTION_getLatestMeetingInfo = 16;
        static final int TRANSACTION_getPTLoginType = 11;
        static final int TRANSACTION_inviteBuddiesToConf = 6;
        static final int TRANSACTION_inviteCallOutUser = 22;
        static final int TRANSACTION_isAuthenticating = 36;
        static final int TRANSACTION_isCallOutInProgress = 24;
        static final int TRANSACTION_isIMSignedIn = 4;
        static final int TRANSACTION_isPTAppAtFront = 5;
        static final int TRANSACTION_isSdkNeedWaterMark = 32;
        static final int TRANSACTION_isSignedIn = 3;
        static final int TRANSACTION_isTaiWanZH = 41;
        static final int TRANSACTION_logout = 38;
        static final int TRANSACTION_notifyLeaveAndPerformAction = 39;
        static final int TRANSACTION_onBOStatusChangeComplete = 20;
        static final int TRANSACTION_onBOStatusChangeStart = 19;
        static final int TRANSACTION_onCallOutStatus = 26;
        static final int TRANSACTION_onConfUIMoveToBackground = 18;
        static final int TRANSACTION_onConfUIMoveToFront = 17;
        static final int TRANSACTION_onJoinConfMeetingStatus = 21;
        static final int TRANSACTION_presentToRoom = 34;
        static final int TRANSACTION_reloadAllBuddyItems = 8;
        static final int TRANSACTION_sendMeetingParingCode = 29;
        static final int TRANSACTION_sendMessage = 1;
        static final int TRANSACTION_sendMessageFromSip = 2;
        static final int TRANSACTION_setNeedCheckSwitchCall = 37;
        static final int TRANSACTION_showNeedUpdate = 10;
        static final int TRANSACTION_showRateZoomDialog = 33;
        static final int TRANSACTION_stopPresentToRoom = 35;

        private static class Proxy implements IPTService {
            public static IPTService sDefaultImpl;
            private IBinder mRemote;

            public String getInterfaceDescriptor() {
                return Stub.DESCRIPTOR;
            }

            Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public void sendMessage(byte[] bArr) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeByteArray(bArr);
                    if (this.mRemote.transact(1, obtain, obtain2, 0) || Stub.getDefaultImpl() == null) {
                        obtain2.readException();
                        obtain2.recycle();
                        obtain.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().sendMessage(bArr);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void sendMessageFromSip(byte[] bArr) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeByteArray(bArr);
                    if (this.mRemote.transact(2, obtain, obtain2, 0) || Stub.getDefaultImpl() == null) {
                        obtain2.readException();
                        obtain2.recycle();
                        obtain.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().sendMessageFromSip(bArr);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean isSignedIn() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = false;
                    if (!this.mRemote.transact(3, obtain, obtain2, 0) && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isSignedIn();
                    }
                    obtain2.readException();
                    if (obtain2.readInt() != 0) {
                        z = true;
                    }
                    obtain2.recycle();
                    obtain.recycle();
                    return z;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean isIMSignedIn() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = false;
                    if (!this.mRemote.transact(4, obtain, obtain2, 0) && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isIMSignedIn();
                    }
                    obtain2.readException();
                    if (obtain2.readInt() != 0) {
                        z = true;
                    }
                    obtain2.recycle();
                    obtain.recycle();
                    return z;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean isPTAppAtFront() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = false;
                    if (!this.mRemote.transact(5, obtain, obtain2, 0) && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isPTAppAtFront();
                    }
                    obtain2.readException();
                    if (obtain2.readInt() != 0) {
                        z = true;
                    }
                    obtain2.recycle();
                    obtain.recycle();
                    return z;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int inviteBuddiesToConf(String[] strArr, String[] strArr2, String str, long j, String str2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    String[] strArr3 = strArr;
                    obtain.writeStringArray(strArr);
                    String[] strArr4 = strArr2;
                    obtain.writeStringArray(strArr2);
                    String str3 = str;
                    obtain.writeString(str);
                    obtain.writeLong(j);
                    obtain.writeString(str2);
                    try {
                        if (this.mRemote.transact(6, obtain, obtain2, 0) || Stub.getDefaultImpl() == null) {
                            obtain2.readException();
                            int readInt = obtain2.readInt();
                            obtain2.recycle();
                            obtain.recycle();
                            return readInt;
                        }
                        int inviteBuddiesToConf = Stub.getDefaultImpl().inviteBuddiesToConf(strArr, strArr2, str, j, str2);
                        obtain2.recycle();
                        obtain.recycle();
                        return inviteBuddiesToConf;
                    } catch (Throwable th) {
                        th = th;
                        obtain2.recycle();
                        obtain.recycle();
                        throw th;
                    }
                } catch (Throwable th2) {
                    th = th2;
                    obtain2.recycle();
                    obtain.recycle();
                    throw th;
                }
            }

            public int getBuddyItemCount() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (!this.mRemote.transact(7, obtain, obtain2, 0) && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getBuddyItemCount();
                    }
                    obtain2.readException();
                    int readInt = obtain2.readInt();
                    obtain2.recycle();
                    obtain.recycle();
                    return readInt;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void reloadAllBuddyItems() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(8, obtain, obtain2, 0) || Stub.getDefaultImpl() == null) {
                        obtain2.readException();
                        obtain2.recycle();
                        obtain.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().reloadAllBuddyItems();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public String[] filterBuddyWithInput(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    if (!this.mRemote.transact(9, obtain, obtain2, 0) && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().filterBuddyWithInput(str);
                    }
                    obtain2.readException();
                    String[] createStringArray = obtain2.createStringArray();
                    obtain2.recycle();
                    obtain.recycle();
                    return createStringArray;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void showNeedUpdate() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(10, obtain, obtain2, 0) || Stub.getDefaultImpl() == null) {
                        obtain2.readException();
                        obtain2.recycle();
                        obtain.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().showNeedUpdate();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int getPTLoginType() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (!this.mRemote.transact(11, obtain, obtain2, 0) && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getPTLoginType();
                    }
                    obtain2.readException();
                    int readInt = obtain2.readInt();
                    obtain2.recycle();
                    obtain.recycle();
                    return readInt;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public String FavoriteMgr_getLocalPicturePath(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    if (!this.mRemote.transact(12, obtain, obtain2, 0) && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().FavoriteMgr_getLocalPicturePath(str);
                    }
                    obtain2.readException();
                    String readString = obtain2.readString();
                    obtain2.recycle();
                    obtain.recycle();
                    return readString;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public byte[] FavoriteMgr_getFavoriteListWithFilter(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    if (!this.mRemote.transact(13, obtain, obtain2, 0) && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().FavoriteMgr_getFavoriteListWithFilter(str);
                    }
                    obtain2.readException();
                    byte[] createByteArray = obtain2.createByteArray();
                    obtain2.recycle();
                    obtain.recycle();
                    return createByteArray;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public String[] ABContactsHelper_getMatchedPhoneNumbers() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (!this.mRemote.transact(14, obtain, obtain2, 0) && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().ABContactsHelper_getMatchedPhoneNumbers();
                    }
                    obtain2.readException();
                    String[] createStringArray = obtain2.createStringArray();
                    obtain2.recycle();
                    obtain.recycle();
                    return createStringArray;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int ABContactsHelper_inviteABContacts(String[] strArr, String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeStringArray(strArr);
                    obtain.writeString(str);
                    if (!this.mRemote.transact(15, obtain, obtain2, 0) && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().ABContactsHelper_inviteABContacts(strArr, str);
                    }
                    obtain2.readException();
                    int readInt = obtain2.readInt();
                    obtain2.recycle();
                    obtain.recycle();
                    return readInt;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public String[] getLatestMeetingInfo() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (!this.mRemote.transact(16, obtain, obtain2, 0) && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getLatestMeetingInfo();
                    }
                    obtain2.readException();
                    String[] createStringArray = obtain2.createStringArray();
                    obtain2.recycle();
                    obtain.recycle();
                    return createStringArray;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void onConfUIMoveToFront(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    if (this.mRemote.transact(17, obtain, obtain2, 0) || Stub.getDefaultImpl() == null) {
                        obtain2.readException();
                        obtain2.recycle();
                        obtain.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().onConfUIMoveToFront(str);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void onConfUIMoveToBackground() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(18, obtain, obtain2, 0) || Stub.getDefaultImpl() == null) {
                        obtain2.readException();
                        obtain2.recycle();
                        obtain.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().onConfUIMoveToBackground();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void onBOStatusChangeStart(boolean z, int i, String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(z ? 1 : 0);
                    obtain.writeInt(i);
                    obtain.writeString(str);
                    if (this.mRemote.transact(19, obtain, obtain2, 0) || Stub.getDefaultImpl() == null) {
                        obtain2.readException();
                        obtain2.recycle();
                        obtain.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().onBOStatusChangeStart(z, i, str);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void onBOStatusChangeComplete() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(20, obtain, obtain2, 0) || Stub.getDefaultImpl() == null) {
                        obtain2.readException();
                        obtain2.recycle();
                        obtain.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().onBOStatusChangeComplete();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void onJoinConfMeetingStatus(boolean z, boolean z2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    obtain.writeInt(z ? 1 : 0);
                    if (!z2) {
                        i = 0;
                    }
                    obtain.writeInt(i);
                    if (this.mRemote.transact(21, obtain, obtain2, 0) || Stub.getDefaultImpl() == null) {
                        obtain2.readException();
                        obtain2.recycle();
                        obtain.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().onJoinConfMeetingStatus(z, z2);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean inviteCallOutUser(String str, String str2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    boolean z = false;
                    if (!this.mRemote.transact(22, obtain, obtain2, 0) && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().inviteCallOutUser(str, str2);
                    }
                    obtain2.readException();
                    if (obtain2.readInt() != 0) {
                        z = true;
                    }
                    obtain2.recycle();
                    obtain.recycle();
                    return z;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean cancelCallOut() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = false;
                    if (!this.mRemote.transact(23, obtain, obtain2, 0) && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().cancelCallOut();
                    }
                    obtain2.readException();
                    if (obtain2.readInt() != 0) {
                        z = true;
                    }
                    obtain2.recycle();
                    obtain.recycle();
                    return z;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean isCallOutInProgress() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = false;
                    if (!this.mRemote.transact(24, obtain, obtain2, 0) && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isCallOutInProgress();
                    }
                    obtain2.readException();
                    if (obtain2.readInt() != 0) {
                        z = true;
                    }
                    obtain2.recycle();
                    obtain.recycle();
                    return z;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int getCallOutStatus() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (!this.mRemote.transact(25, obtain, obtain2, 0) && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getCallOutStatus();
                    }
                    obtain2.readException();
                    int readInt = obtain2.readInt();
                    obtain2.recycle();
                    obtain.recycle();
                    return readInt;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void onCallOutStatus(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    if (this.mRemote.transact(26, obtain, obtain2, 0) || Stub.getDefaultImpl() == null) {
                        obtain2.readException();
                        obtain2.recycle();
                        obtain.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().onCallOutStatus(i);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public String[] getH323Gateway() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (!this.mRemote.transact(27, obtain, obtain2, 0) && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getH323Gateway();
                    }
                    obtain2.readException();
                    String[] createStringArray = obtain2.createStringArray();
                    obtain2.recycle();
                    obtain.recycle();
                    return createStringArray;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public String getH323Password() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (!this.mRemote.transact(28, obtain, obtain2, 0) && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getH323Password();
                    }
                    obtain2.readException();
                    String readString = obtain2.readString();
                    obtain2.recycle();
                    obtain.recycle();
                    return readString;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean sendMeetingParingCode(long j, String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeLong(j);
                    obtain.writeString(str);
                    boolean z = false;
                    if (!this.mRemote.transact(29, obtain, obtain2, 0) && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().sendMeetingParingCode(j, str);
                    }
                    obtain2.readException();
                    if (obtain2.readInt() != 0) {
                        z = true;
                    }
                    obtain2.recycle();
                    obtain.recycle();
                    return z;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean callOutRoomSystem(String str, int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeInt(i);
                    boolean z = false;
                    if (!this.mRemote.transact(30, obtain, obtain2, 0) && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().callOutRoomSystem(str, i);
                    }
                    obtain2.readException();
                    if (obtain2.readInt() != 0) {
                        z = true;
                    }
                    obtain2.recycle();
                    obtain.recycle();
                    return z;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean cancelCallOutRoomSystem() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = false;
                    if (!this.mRemote.transact(31, obtain, obtain2, 0) && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().cancelCallOutRoomSystem();
                    }
                    obtain2.readException();
                    if (obtain2.readInt() != 0) {
                        z = true;
                    }
                    obtain2.recycle();
                    obtain.recycle();
                    return z;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean isSdkNeedWaterMark() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = false;
                    if (!this.mRemote.transact(32, obtain, obtain2, 0) && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isSdkNeedWaterMark();
                    }
                    obtain2.readException();
                    if (obtain2.readInt() != 0) {
                        z = true;
                    }
                    obtain2.recycle();
                    obtain.recycle();
                    return z;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void showRateZoomDialog() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(33, obtain, obtain2, 0) || Stub.getDefaultImpl() == null) {
                        obtain2.readException();
                        obtain2.recycle();
                        obtain.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().showRateZoomDialog();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean presentToRoom(int i, String str, long j, boolean z) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i2 = i;
                    obtain.writeInt(i);
                    String str2 = str;
                    obtain.writeString(str);
                    long j2 = j;
                    obtain.writeLong(j);
                    boolean z2 = true;
                    obtain.writeInt(z ? 1 : 0);
                    try {
                        if (this.mRemote.transact(34, obtain, obtain2, 0) || Stub.getDefaultImpl() == null) {
                            obtain2.readException();
                            if (obtain2.readInt() == 0) {
                                z2 = false;
                            }
                            obtain2.recycle();
                            obtain.recycle();
                            return z2;
                        }
                        boolean presentToRoom = Stub.getDefaultImpl().presentToRoom(i, str, j, z);
                        obtain2.recycle();
                        obtain.recycle();
                        return presentToRoom;
                    } catch (Throwable th) {
                        th = th;
                        obtain2.recycle();
                        obtain.recycle();
                        throw th;
                    }
                } catch (Throwable th2) {
                    th = th2;
                    obtain2.recycle();
                    obtain.recycle();
                    throw th;
                }
            }

            public void stopPresentToRoom(boolean z) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(z ? 1 : 0);
                    if (this.mRemote.transact(35, obtain, obtain2, 0) || Stub.getDefaultImpl() == null) {
                        obtain2.readException();
                        obtain2.recycle();
                        obtain.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().stopPresentToRoom(z);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean isAuthenticating() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = false;
                    if (!this.mRemote.transact(36, obtain, obtain2, 0) && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isAuthenticating();
                    }
                    obtain2.readException();
                    if (obtain2.readInt() != 0) {
                        z = true;
                    }
                    obtain2.recycle();
                    obtain.recycle();
                    return z;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void setNeedCheckSwitchCall(boolean z) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(z ? 1 : 0);
                    if (this.mRemote.transact(37, obtain, obtain2, 0) || Stub.getDefaultImpl() == null) {
                        obtain2.readException();
                        obtain2.recycle();
                        obtain.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setNeedCheckSwitchCall(z);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void logout() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(38, obtain, obtain2, 0) || Stub.getDefaultImpl() == null) {
                        obtain2.readException();
                        obtain2.recycle();
                        obtain.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().logout();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void notifyLeaveAndPerformAction(int i, int i2, int i3) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    obtain.writeInt(i3);
                    if (this.mRemote.transact(39, obtain, obtain2, 0) || Stub.getDefaultImpl() == null) {
                        obtain2.readException();
                        obtain2.recycle();
                        obtain.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().notifyLeaveAndPerformAction(i, i2, i3);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean disablePhoneAudio() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = false;
                    if (!this.mRemote.transact(40, obtain, obtain2, 0) && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().disablePhoneAudio();
                    }
                    obtain2.readException();
                    if (obtain2.readInt() != 0) {
                        z = true;
                    }
                    obtain2.recycle();
                    obtain.recycle();
                    return z;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean isTaiWanZH() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = false;
                    if (!this.mRemote.transact(41, obtain, obtain2, 0) && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isTaiWanZH();
                    }
                    obtain2.readException();
                    if (obtain2.readInt() != 0) {
                        z = true;
                    }
                    obtain2.recycle();
                    obtain.recycle();
                    return z;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
        }

        public IBinder asBinder() {
            return this;
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IPTService asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(DESCRIPTOR);
            if (queryLocalInterface == null || !(queryLocalInterface instanceof IPTService)) {
                return new Proxy(iBinder);
            }
            return (IPTService) queryLocalInterface;
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            String str = DESCRIPTOR;
            if (i != 1598968902) {
                boolean z = false;
                switch (i) {
                    case 1:
                        parcel.enforceInterface(str);
                        sendMessage(parcel.createByteArray());
                        parcel2.writeNoException();
                        return true;
                    case 2:
                        parcel.enforceInterface(str);
                        sendMessageFromSip(parcel.createByteArray());
                        parcel2.writeNoException();
                        return true;
                    case 3:
                        parcel.enforceInterface(str);
                        boolean isSignedIn = isSignedIn();
                        parcel2.writeNoException();
                        parcel2.writeInt(isSignedIn ? 1 : 0);
                        return true;
                    case 4:
                        parcel.enforceInterface(str);
                        boolean isIMSignedIn = isIMSignedIn();
                        parcel2.writeNoException();
                        parcel2.writeInt(isIMSignedIn ? 1 : 0);
                        return true;
                    case 5:
                        parcel.enforceInterface(str);
                        boolean isPTAppAtFront = isPTAppAtFront();
                        parcel2.writeNoException();
                        parcel2.writeInt(isPTAppAtFront ? 1 : 0);
                        return true;
                    case 6:
                        parcel.enforceInterface(str);
                        int inviteBuddiesToConf = inviteBuddiesToConf(parcel.createStringArray(), parcel.createStringArray(), parcel.readString(), parcel.readLong(), parcel.readString());
                        parcel2.writeNoException();
                        parcel2.writeInt(inviteBuddiesToConf);
                        return true;
                    case 7:
                        parcel.enforceInterface(str);
                        int buddyItemCount = getBuddyItemCount();
                        parcel2.writeNoException();
                        parcel2.writeInt(buddyItemCount);
                        return true;
                    case 8:
                        parcel.enforceInterface(str);
                        reloadAllBuddyItems();
                        parcel2.writeNoException();
                        return true;
                    case 9:
                        parcel.enforceInterface(str);
                        String[] filterBuddyWithInput = filterBuddyWithInput(parcel.readString());
                        parcel2.writeNoException();
                        parcel2.writeStringArray(filterBuddyWithInput);
                        return true;
                    case 10:
                        parcel.enforceInterface(str);
                        showNeedUpdate();
                        parcel2.writeNoException();
                        return true;
                    case 11:
                        parcel.enforceInterface(str);
                        int pTLoginType = getPTLoginType();
                        parcel2.writeNoException();
                        parcel2.writeInt(pTLoginType);
                        return true;
                    case 12:
                        parcel.enforceInterface(str);
                        String FavoriteMgr_getLocalPicturePath = FavoriteMgr_getLocalPicturePath(parcel.readString());
                        parcel2.writeNoException();
                        parcel2.writeString(FavoriteMgr_getLocalPicturePath);
                        return true;
                    case 13:
                        parcel.enforceInterface(str);
                        byte[] FavoriteMgr_getFavoriteListWithFilter = FavoriteMgr_getFavoriteListWithFilter(parcel.readString());
                        parcel2.writeNoException();
                        parcel2.writeByteArray(FavoriteMgr_getFavoriteListWithFilter);
                        return true;
                    case 14:
                        parcel.enforceInterface(str);
                        String[] ABContactsHelper_getMatchedPhoneNumbers = ABContactsHelper_getMatchedPhoneNumbers();
                        parcel2.writeNoException();
                        parcel2.writeStringArray(ABContactsHelper_getMatchedPhoneNumbers);
                        return true;
                    case 15:
                        parcel.enforceInterface(str);
                        int ABContactsHelper_inviteABContacts = ABContactsHelper_inviteABContacts(parcel.createStringArray(), parcel.readString());
                        parcel2.writeNoException();
                        parcel2.writeInt(ABContactsHelper_inviteABContacts);
                        return true;
                    case 16:
                        parcel.enforceInterface(str);
                        String[] latestMeetingInfo = getLatestMeetingInfo();
                        parcel2.writeNoException();
                        parcel2.writeStringArray(latestMeetingInfo);
                        return true;
                    case 17:
                        parcel.enforceInterface(str);
                        onConfUIMoveToFront(parcel.readString());
                        parcel2.writeNoException();
                        return true;
                    case 18:
                        parcel.enforceInterface(str);
                        onConfUIMoveToBackground();
                        parcel2.writeNoException();
                        return true;
                    case 19:
                        parcel.enforceInterface(str);
                        if (parcel.readInt() != 0) {
                            z = true;
                        }
                        onBOStatusChangeStart(z, parcel.readInt(), parcel.readString());
                        parcel2.writeNoException();
                        return true;
                    case 20:
                        parcel.enforceInterface(str);
                        onBOStatusChangeComplete();
                        parcel2.writeNoException();
                        return true;
                    case 21:
                        parcel.enforceInterface(str);
                        boolean z2 = parcel.readInt() != 0;
                        if (parcel.readInt() != 0) {
                            z = true;
                        }
                        onJoinConfMeetingStatus(z2, z);
                        parcel2.writeNoException();
                        return true;
                    case 22:
                        parcel.enforceInterface(str);
                        boolean inviteCallOutUser = inviteCallOutUser(parcel.readString(), parcel.readString());
                        parcel2.writeNoException();
                        parcel2.writeInt(inviteCallOutUser ? 1 : 0);
                        return true;
                    case 23:
                        parcel.enforceInterface(str);
                        boolean cancelCallOut = cancelCallOut();
                        parcel2.writeNoException();
                        parcel2.writeInt(cancelCallOut ? 1 : 0);
                        return true;
                    case 24:
                        parcel.enforceInterface(str);
                        boolean isCallOutInProgress = isCallOutInProgress();
                        parcel2.writeNoException();
                        parcel2.writeInt(isCallOutInProgress ? 1 : 0);
                        return true;
                    case 25:
                        parcel.enforceInterface(str);
                        int callOutStatus = getCallOutStatus();
                        parcel2.writeNoException();
                        parcel2.writeInt(callOutStatus);
                        return true;
                    case 26:
                        parcel.enforceInterface(str);
                        onCallOutStatus(parcel.readInt());
                        parcel2.writeNoException();
                        return true;
                    case 27:
                        parcel.enforceInterface(str);
                        String[] h323Gateway = getH323Gateway();
                        parcel2.writeNoException();
                        parcel2.writeStringArray(h323Gateway);
                        return true;
                    case 28:
                        parcel.enforceInterface(str);
                        String h323Password = getH323Password();
                        parcel2.writeNoException();
                        parcel2.writeString(h323Password);
                        return true;
                    case 29:
                        parcel.enforceInterface(str);
                        boolean sendMeetingParingCode = sendMeetingParingCode(parcel.readLong(), parcel.readString());
                        parcel2.writeNoException();
                        parcel2.writeInt(sendMeetingParingCode ? 1 : 0);
                        return true;
                    case 30:
                        parcel.enforceInterface(str);
                        boolean callOutRoomSystem = callOutRoomSystem(parcel.readString(), parcel.readInt());
                        parcel2.writeNoException();
                        parcel2.writeInt(callOutRoomSystem ? 1 : 0);
                        return true;
                    case 31:
                        parcel.enforceInterface(str);
                        boolean cancelCallOutRoomSystem = cancelCallOutRoomSystem();
                        parcel2.writeNoException();
                        parcel2.writeInt(cancelCallOutRoomSystem ? 1 : 0);
                        return true;
                    case 32:
                        parcel.enforceInterface(str);
                        boolean isSdkNeedWaterMark = isSdkNeedWaterMark();
                        parcel2.writeNoException();
                        parcel2.writeInt(isSdkNeedWaterMark ? 1 : 0);
                        return true;
                    case 33:
                        parcel.enforceInterface(str);
                        showRateZoomDialog();
                        parcel2.writeNoException();
                        return true;
                    case 34:
                        parcel.enforceInterface(str);
                        boolean presentToRoom = presentToRoom(parcel.readInt(), parcel.readString(), parcel.readLong(), parcel.readInt() != 0);
                        parcel2.writeNoException();
                        parcel2.writeInt(presentToRoom ? 1 : 0);
                        return true;
                    case 35:
                        parcel.enforceInterface(str);
                        if (parcel.readInt() != 0) {
                            z = true;
                        }
                        stopPresentToRoom(z);
                        parcel2.writeNoException();
                        return true;
                    case 36:
                        parcel.enforceInterface(str);
                        boolean isAuthenticating = isAuthenticating();
                        parcel2.writeNoException();
                        parcel2.writeInt(isAuthenticating ? 1 : 0);
                        return true;
                    case 37:
                        parcel.enforceInterface(str);
                        if (parcel.readInt() != 0) {
                            z = true;
                        }
                        setNeedCheckSwitchCall(z);
                        parcel2.writeNoException();
                        return true;
                    case 38:
                        parcel.enforceInterface(str);
                        logout();
                        parcel2.writeNoException();
                        return true;
                    case 39:
                        parcel.enforceInterface(str);
                        notifyLeaveAndPerformAction(parcel.readInt(), parcel.readInt(), parcel.readInt());
                        parcel2.writeNoException();
                        return true;
                    case 40:
                        parcel.enforceInterface(str);
                        boolean disablePhoneAudio = disablePhoneAudio();
                        parcel2.writeNoException();
                        parcel2.writeInt(disablePhoneAudio ? 1 : 0);
                        return true;
                    case 41:
                        parcel.enforceInterface(str);
                        boolean isTaiWanZH = isTaiWanZH();
                        parcel2.writeNoException();
                        parcel2.writeInt(isTaiWanZH ? 1 : 0);
                        return true;
                    default:
                        return super.onTransact(i, parcel, parcel2, i2);
                }
            } else {
                parcel2.writeString(str);
                return true;
            }
        }

        public static boolean setDefaultImpl(IPTService iPTService) {
            if (Proxy.sDefaultImpl != null || iPTService == null) {
                return false;
            }
            Proxy.sDefaultImpl = iPTService;
            return true;
        }

        public static IPTService getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    String[] ABContactsHelper_getMatchedPhoneNumbers() throws RemoteException;

    int ABContactsHelper_inviteABContacts(String[] strArr, String str) throws RemoteException;

    byte[] FavoriteMgr_getFavoriteListWithFilter(String str) throws RemoteException;

    String FavoriteMgr_getLocalPicturePath(String str) throws RemoteException;

    boolean callOutRoomSystem(String str, int i) throws RemoteException;

    boolean cancelCallOut() throws RemoteException;

    boolean cancelCallOutRoomSystem() throws RemoteException;

    boolean disablePhoneAudio() throws RemoteException;

    String[] filterBuddyWithInput(String str) throws RemoteException;

    int getBuddyItemCount() throws RemoteException;

    int getCallOutStatus() throws RemoteException;

    String[] getH323Gateway() throws RemoteException;

    String getH323Password() throws RemoteException;

    String[] getLatestMeetingInfo() throws RemoteException;

    int getPTLoginType() throws RemoteException;

    int inviteBuddiesToConf(String[] strArr, String[] strArr2, String str, long j, String str2) throws RemoteException;

    boolean inviteCallOutUser(String str, String str2) throws RemoteException;

    boolean isAuthenticating() throws RemoteException;

    boolean isCallOutInProgress() throws RemoteException;

    boolean isIMSignedIn() throws RemoteException;

    boolean isPTAppAtFront() throws RemoteException;

    boolean isSdkNeedWaterMark() throws RemoteException;

    boolean isSignedIn() throws RemoteException;

    boolean isTaiWanZH() throws RemoteException;

    void logout() throws RemoteException;

    void notifyLeaveAndPerformAction(int i, int i2, int i3) throws RemoteException;

    void onBOStatusChangeComplete() throws RemoteException;

    void onBOStatusChangeStart(boolean z, int i, String str) throws RemoteException;

    void onCallOutStatus(int i) throws RemoteException;

    void onConfUIMoveToBackground() throws RemoteException;

    void onConfUIMoveToFront(String str) throws RemoteException;

    void onJoinConfMeetingStatus(boolean z, boolean z2) throws RemoteException;

    boolean presentToRoom(int i, String str, long j, boolean z) throws RemoteException;

    void reloadAllBuddyItems() throws RemoteException;

    boolean sendMeetingParingCode(long j, String str) throws RemoteException;

    void sendMessage(byte[] bArr) throws RemoteException;

    void sendMessageFromSip(byte[] bArr) throws RemoteException;

    void setNeedCheckSwitchCall(boolean z) throws RemoteException;

    void showNeedUpdate() throws RemoteException;

    void showRateZoomDialog() throws RemoteException;

    void stopPresentToRoom(boolean z) throws RemoteException;
}
