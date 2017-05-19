package com.baidu.lining.displayadapter.net;

import java.util.Map;

import com.android.volley.AuthFailureError;
import com.android.volley.Request.Method;
import com.android.volley.toolbox.StringRequest;
import com.baidu.lining.displayadapter.App;

import android.content.Context;

public class HttpUtils {

	private static StringRequest request;

	public static void doGet(Context context,String url,String tag,VolleyInterface vif){
		// 獲取全局的請求隊列并把基於TAG標籤全部取消，防止重複請求
		App.getHttpQueues().cancelAll(tag);
		
		request = new StringRequest(Method.GET, url, vif.loadingListener(), vif.errorListener());
		// 設置标签
		request.setTag(tag);
		// 将请求添加到队列
		App.getHttpQueues().add(request);

		App.getHttpQueues().start();
	}
	
	public static void doPost(Context context,String url,String tag,final Map<String,String> params,VolleyInterface vif){
		App.getHttpQueues().cancelAll(tag);
		
		request = new StringRequest(Method.POST,url, vif.loadingListener(), vif.errorListener()){
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				return params;
			}
		};
		request.setTag(tag);
		App.getHttpQueues().add(request);
		App.getHttpQueues().start();
	}
	
	public static void doCancle(){
	//	App.getHttpQueues().cancelAll(Setting.TAG);
	}
}
