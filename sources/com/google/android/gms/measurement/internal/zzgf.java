package com.google.android.gms.measurement.internal;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Arrays;
import javax.net.ssl.HandshakeCompletedListener;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;

final class zzgf extends SSLSocket {
    private final SSLSocket zztg;

    zzgf(zzge zzge, SSLSocket sSLSocket) {
        this.zztg = sSLSocket;
    }

    public final void setEnabledProtocols(String[] strArr) {
        if (strArr != null && Arrays.asList(strArr).contains("SSLv3")) {
            ArrayList arrayList = new ArrayList(Arrays.asList(this.zztg.getEnabledProtocols()));
            if (arrayList.size() > 1) {
                arrayList.remove("SSLv3");
            }
            strArr = (String[]) arrayList.toArray(new String[arrayList.size()]);
        }
        this.zztg.setEnabledProtocols(strArr);
    }

    public final String[] getSupportedCipherSuites() {
        return this.zztg.getSupportedCipherSuites();
    }

    public final String[] getEnabledCipherSuites() {
        return this.zztg.getEnabledCipherSuites();
    }

    public final void setEnabledCipherSuites(String[] strArr) {
        this.zztg.setEnabledCipherSuites(strArr);
    }

    public final String[] getSupportedProtocols() {
        return this.zztg.getSupportedProtocols();
    }

    public final String[] getEnabledProtocols() {
        return this.zztg.getEnabledProtocols();
    }

    public final SSLSession getSession() {
        return this.zztg.getSession();
    }

    public final void addHandshakeCompletedListener(HandshakeCompletedListener handshakeCompletedListener) {
        this.zztg.addHandshakeCompletedListener(handshakeCompletedListener);
    }

    public final void removeHandshakeCompletedListener(HandshakeCompletedListener handshakeCompletedListener) {
        this.zztg.removeHandshakeCompletedListener(handshakeCompletedListener);
    }

    public final void startHandshake() throws IOException {
        this.zztg.startHandshake();
    }

    public final void setUseClientMode(boolean z) {
        this.zztg.setUseClientMode(z);
    }

    public final boolean getUseClientMode() {
        return this.zztg.getUseClientMode();
    }

    public final void setNeedClientAuth(boolean z) {
        this.zztg.setNeedClientAuth(z);
    }

    public final void setWantClientAuth(boolean z) {
        this.zztg.setWantClientAuth(z);
    }

    public final boolean getNeedClientAuth() {
        return this.zztg.getNeedClientAuth();
    }

    public final boolean getWantClientAuth() {
        return this.zztg.getWantClientAuth();
    }

    public final void setEnableSessionCreation(boolean z) {
        this.zztg.setEnableSessionCreation(z);
    }

    public final boolean getEnableSessionCreation() {
        return this.zztg.getEnableSessionCreation();
    }

    public final void bind(SocketAddress socketAddress) throws IOException {
        this.zztg.bind(socketAddress);
    }

    public final synchronized void close() throws IOException {
        this.zztg.close();
    }

    public final void connect(SocketAddress socketAddress) throws IOException {
        this.zztg.connect(socketAddress);
    }

    public final void connect(SocketAddress socketAddress, int i) throws IOException {
        this.zztg.connect(socketAddress, i);
    }

    public final SocketChannel getChannel() {
        return this.zztg.getChannel();
    }

    public final InetAddress getInetAddress() {
        return this.zztg.getInetAddress();
    }

    public final InputStream getInputStream() throws IOException {
        return this.zztg.getInputStream();
    }

    public final boolean getKeepAlive() throws SocketException {
        return this.zztg.getKeepAlive();
    }

    public final InetAddress getLocalAddress() {
        return this.zztg.getLocalAddress();
    }

    public final int getLocalPort() {
        return this.zztg.getLocalPort();
    }

    public final SocketAddress getLocalSocketAddress() {
        return this.zztg.getLocalSocketAddress();
    }

    public final boolean getOOBInline() throws SocketException {
        return this.zztg.getOOBInline();
    }

    public final OutputStream getOutputStream() throws IOException {
        return this.zztg.getOutputStream();
    }

    public final int getPort() {
        return this.zztg.getPort();
    }

    public final synchronized int getReceiveBufferSize() throws SocketException {
        return this.zztg.getReceiveBufferSize();
    }

    public final SocketAddress getRemoteSocketAddress() {
        return this.zztg.getRemoteSocketAddress();
    }

    public final boolean getReuseAddress() throws SocketException {
        return this.zztg.getReuseAddress();
    }

    public final synchronized int getSendBufferSize() throws SocketException {
        return this.zztg.getSendBufferSize();
    }

    public final int getSoLinger() throws SocketException {
        return this.zztg.getSoLinger();
    }

    public final synchronized int getSoTimeout() throws SocketException {
        return this.zztg.getSoTimeout();
    }

    public final boolean getTcpNoDelay() throws SocketException {
        return this.zztg.getTcpNoDelay();
    }

    public final int getTrafficClass() throws SocketException {
        return this.zztg.getTrafficClass();
    }

    public final boolean isBound() {
        return this.zztg.isBound();
    }

    public final boolean isClosed() {
        return this.zztg.isClosed();
    }

    public final boolean isConnected() {
        return this.zztg.isConnected();
    }

    public final boolean isInputShutdown() {
        return this.zztg.isInputShutdown();
    }

    public final boolean isOutputShutdown() {
        return this.zztg.isOutputShutdown();
    }

    public final void sendUrgentData(int i) throws IOException {
        this.zztg.sendUrgentData(i);
    }

    public final void setKeepAlive(boolean z) throws SocketException {
        this.zztg.setKeepAlive(z);
    }

    public final void setOOBInline(boolean z) throws SocketException {
        this.zztg.setOOBInline(z);
    }

    public final void setPerformancePreferences(int i, int i2, int i3) {
        this.zztg.setPerformancePreferences(i, i2, i3);
    }

    public final synchronized void setReceiveBufferSize(int i) throws SocketException {
        this.zztg.setReceiveBufferSize(i);
    }

    public final void setReuseAddress(boolean z) throws SocketException {
        this.zztg.setReuseAddress(z);
    }

    public final synchronized void setSendBufferSize(int i) throws SocketException {
        this.zztg.setSendBufferSize(i);
    }

    public final void setSoLinger(boolean z, int i) throws SocketException {
        this.zztg.setSoLinger(z, i);
    }

    public final synchronized void setSoTimeout(int i) throws SocketException {
        this.zztg.setSoTimeout(i);
    }

    public final void setTcpNoDelay(boolean z) throws SocketException {
        this.zztg.setTcpNoDelay(z);
    }

    public final void setTrafficClass(int i) throws SocketException {
        this.zztg.setTrafficClass(i);
    }

    public final void shutdownInput() throws IOException {
        this.zztg.shutdownInput();
    }

    public final void shutdownOutput() throws IOException {
        this.zztg.shutdownOutput();
    }

    public final String toString() {
        return this.zztg.toString();
    }

    public final boolean equals(Object obj) {
        return this.zztg.equals(obj);
    }
}
