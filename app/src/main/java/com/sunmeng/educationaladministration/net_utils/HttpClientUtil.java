package com.sunmeng.educationaladministration.net_utils;

import java.io.IOException;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

public class HttpClientUtil {
	/**
	 * *网络连接是否可用,Sunmeng
	 * */
	public static boolean isConnnected(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (null != connectivityManager) {
			NetworkInfo networkInfo[] = connectivityManager.getAllNetworkInfo();

			if (null != networkInfo) {
				for (NetworkInfo info : networkInfo) {
					if (info.getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		}
		Toast.makeText(context, "网络连接失败", Toast.LENGTH_SHORT).show();
		return false;
	}

	/**
	 * 处理结果的公共方法
	 * 
	 * @param request
	 * @return
	 */
	public static String getRespose(HttpUriRequest request) {
		try {
			HttpResponse httpResponse = new DefaultHttpClient()
					.execute(request);
			int statusCode = httpResponse.getStatusLine().getStatusCode();
			if (HttpStatus.SC_OK == statusCode) {
				String result = EntityUtils.toString(httpResponse.getEntity());
				return result;
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * get请求
	 * 
	 * @param context
	 * @param urlPath
	 * @return
	 */
	public static String sendGet(Context context, String urlPath) {
		if (isConnnected(context)) {
			HttpGet httpGet = new HttpGet(urlPath);
			return getRespose(httpGet);
		}
		return null;
	}

	/**
	 * Post请求
	 * 
	 * @param context
	 * @param urlPath
	 * @param nameValuePairs
	 * @return
	 * @throws Exception
	 */
	public static String sendPost(Context context, String urlPath,
			List<NameValuePair> nameValuePairs) throws Exception {
		if (isConnnected(context)) {
			HttpPost httpPost = new HttpPost(urlPath);

//			httpPost.addHeader("Content-Type", "text/json"); // 这行很重要
//			httpPost.addHeader("charset", HTTP.UTF_8); // 这行很重要
			
			if (nameValuePairs != null) {
				UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(
						nameValuePairs,"UTF-8");
				httpPost.setEntity(formEntity);
			}
			return getRespose(httpPost);
		}
		return null;
	}

}
