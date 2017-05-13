package com.baidu.lining.displayadapter.net;

import android.content.Context;
import android.util.Log;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;

public abstract class VolleyInterface {
	
	protected static final String TAG = "TAG";
	public Context mContext;
	public static Listener<String> mListener;
	public static ErrorListener mErrorListener;
	
	public VolleyInterface(Context context,Listener<String> listener,ErrorListener errorListener){
		this.mContext = context;
		this.mListener = listener;
		this.mErrorListener = errorListener;
	}
	
	// 成功
	public abstract void onSuccess(String result);
	
	// 失败
	public abstract void onError(VolleyError error);
	
	public Listener<String> loadingListener(){
		mListener = new Listener<String>() {

			@Override
			public void onResponse(String result) {
				// TODO Auto-generated method stub
				Log.d(TAG,"result:"+result);
				onSuccess(result);
			}
		};
		return mListener;
	}
	
	// Ո��ʧ���O 
	public ErrorListener errorListener(){
		mErrorListener = new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				// TODO Auto-generated method stub
				onError(error);
			}
		};
		return mErrorListener;
	}
}
