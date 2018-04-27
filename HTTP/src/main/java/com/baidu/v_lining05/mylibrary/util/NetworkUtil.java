//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.baidu.v_lining05.mylibrary.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;

public class NetworkUtil {
    private static boolean mIsNetworkAvailable = true;
    private static NetworkUtil.NetworkStateThread mNetworkStateThread = null;

    public NetworkUtil() {
    }

    public static synchronized boolean isNetworkRealConnected(Context context) {
        if(mNetworkStateThread == null) {
            mNetworkStateThread = new NetworkUtil.NetworkStateThread();
            mNetworkStateThread.start();
            mIsNetworkAvailable = isNetworkConnected(context);
        }

        return mIsNetworkAvailable;
    }

    public static boolean isNetworkConnected(Context context) {
        if(context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager)context.getSystemService("connectivity");
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if(mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }

        return false;
    }

    public static boolean isWifiConnected(Context context) {
        if(context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager)context.getSystemService("connectivity");
            NetworkInfo mWiFiNetworkInfo = mConnectivityManager.getNetworkInfo(1);
            if(mWiFiNetworkInfo != null) {
                return mWiFiNetworkInfo.isAvailable();
            }
        }

        return false;
    }

    public static boolean isMobileConnected(Context context) {
        if(context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager)context.getSystemService("connectivity");
            NetworkInfo mMobileNetworkInfo = mConnectivityManager.getNetworkInfo(0);
            if(mMobileNetworkInfo != null) {
                return mMobileNetworkInfo.isAvailable();
            }
        }

        return false;
    }

    public static int getConnectedType(Context context) {
        if(context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager)context.getSystemService("connectivity");
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if(mNetworkInfo != null && mNetworkInfo.isAvailable()) {
                return mNetworkInfo.getType();
            }
        }

        return -1;
    }

    public static int getAPNType(Context context) {
        int netType = 0;
        ConnectivityManager connMgr = (ConnectivityManager)context.getSystemService("connectivity");
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if(networkInfo == null) {
            return netType;
        } else {
            int nType = networkInfo.getType();
            if(nType == 1) {
                netType = 1;
            } else if(nType == 0) {
                int nSubType = networkInfo.getSubtype();
                TelephonyManager mTelephony = (TelephonyManager)context.getSystemService("phone");
                if(nSubType == 3 && !mTelephony.isNetworkRoaming()) {
                    netType = 2;
                } else {
                    netType = 3;
                }
            }

            return netType;
        }
    }

    private static class NetworkStateThread extends Thread {
        private final String[] dnsAddress;
        private int maxIndex;

        private NetworkStateThread() {
            this.dnsAddress = new String[]{"114.114.114.114", "119.29.29.29", "223.5.5.5", "114.114.115.115", "223.6.6.6", "182.254.116.116", "180.76.76.76"};
            this.maxIndex = this.dnsAddress.length;
        }

        public void run() {
            super.run();

            while(true) {
                for(int i = 0; i < this.maxIndex; ++i) {
                    boolean result = this.isOnline(i);
                    if(!result) {
                        for(int j = i + 1; j < 4 + i; ++j) {
                            result = this.isOnline(j % this.maxIndex);
                            if(result) {
                                i = j;
                                break;
                            }
                        }
                    }

                    NetworkUtil.mIsNetworkAvailable = result;
                    synchronized(this) {
                        try {
                            this.wait(30000L);
                        } catch (InterruptedException var6) {
                            var6.printStackTrace();
                        }
                    }
                }
            }
        }

        private boolean isOnline(int index) {
            String ip = this.dnsAddress[index];
            return this.tcp2DNSServer(ip);
        }

        public boolean resolveDomain(String dnsServerIP) {
            try {
                InetAddress[] addresses = InetAddress.getAllByName("www.baidu.com");

                for(int i = 0; i < addresses.length; ++i) {
                    String var4 = addresses[i].getHostAddress();
                }

                return true;
            } catch (UnknownHostException var5) {
                return false;
            }
        }

        private boolean tcp2DNSServer(String dnsServerIP) {
            try {
                int timeoutMs = 1500;
                Socket sock = new Socket();
                SocketAddress sockaddr = new InetSocketAddress(dnsServerIP, 53);
                sock.connect(sockaddr, timeoutMs);
                sock.close();
                return true;
            } catch (IOException var5) {
                return false;
            }
        }
    }
}
