package com.zipow.videobox;

interface IConfService {
	void sendMessage(in byte[] message);
	boolean isConfAppAtFront();
	
	void sinkPTAppEvent(int event, long result);
	void sinkDataNetworkStatusChanged(boolean connected);
	void sinkIMLocalStatusChanged(int status);
	void sinkIMReceived(in byte[] content);
	void sinkIMBuddyPresence(in byte[] content);
	void sinkIMBuddyPic(in byte[] content);
	void sinkIMBuddySort();
	void sinkPTAppCustomEvent(int event, long result);
	void sinkPTPresentToRoomEvent(int status);
	void sinkPTCommonEvent(int event, in byte[] content);
	void onPTUIMoveToFront(String activityClass);
	void onPTUIMoveToBackground();
	void leaveCurrentMeeting(boolean endIfPossible);
	void pauseCurrentMeeting();
	void resumeCurrentMeeting();
	boolean isCurrentMeetingLocked();
	boolean isCurrentMeetingHost();
	boolean startCallOut(String number);
	boolean hangUpCallOut();
	boolean isCallOutInProgress();
	int getCallMeStatus();
	boolean isCallOutSupported();
	boolean isInviteRoomSystemSupported();
	void onRoomCallEvent(int event, long result, boolean isActiveMeeting);
	boolean tryRetrieveMicrophone();
	void onSipCallEvent(int status,String callId);
	void onSipStatusEvent(boolean isAvailable);
	boolean onAlertWhenAvailable(String name,String bitmapPath,String time,boolean playsound,String jid);
	void dispacthPtLoginResultEvent(boolean isLoginSuccess, String urlAction, String screenName);
	void  onNewIncomingCallCanceled(long meetingNo);
	boolean notifyPTStartLogin(String reason);
	boolean disableConfAudio();
}
