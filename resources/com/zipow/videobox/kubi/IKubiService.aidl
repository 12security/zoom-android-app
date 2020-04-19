package com.zipow.videobox.kubi;

import com.zipow.videobox.kubi.KubiDevice;

interface IKubiService {
	
	int getKubiStatus();
	boolean findKubiDevice();
	boolean disconnectKubi();
	void findAllKubiDevices();
	void connectToKubi(in KubiDevice device);
	KubiDevice getCurrentKubi();
	float getPan();
	float getTilt();
	void moveInPanDirectionWithSpeed(int direction, int speed);
	void moveInTiltDirectionWithSpeed(int direction, int speed);
	void moveTo(float pan, float tilt, float speed);
	boolean resetDevicePosition();
}
