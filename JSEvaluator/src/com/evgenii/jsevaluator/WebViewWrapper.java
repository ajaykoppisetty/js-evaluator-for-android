package com.evgenii.jsevaluator;

import java.io.UnsupportedEncodingException;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Base64;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.evgenii.jsevaluator.interfaces.CallJavaResultInterface;
import com.evgenii.jsevaluator.interfaces.WebViewWrapperInterface;

@SuppressLint("SetJavaScriptEnabled")
public class WebViewWrapper implements WebViewWrapperInterface {
	protected WebView mWebView;

	public WebViewWrapper(Context context, CallJavaResultInterface callJavaResult) {
		mWebView = new WebView(context);

		// web view will not draw anything - turn on optimizations
		mWebView.setWillNotDraw(true);

		final WebSettings webSettings = mWebView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		final JavaScriptInterface jsInterface = new JavaScriptInterface(callJavaResult);
		mWebView.addJavascriptInterface(jsInterface, JsEvaluator.JS_NAMESPACE);
		hackToMakeItWorkOnAndroid_4_3();
	}

	private void hackToMakeItWorkOnAndroid_4_3() {
		mWebView.loadUrl("about:blank");
	}

	@Override
	public void loadJavaScript(String javascript) {
		byte[] data;
		try {
			javascript = "<script>" + javascript + "</script>";
			data = javascript.getBytes("UTF-8");
			final String base64 = Base64.encodeToString(data, Base64.URL_SAFE);
			mWebView.loadData(base64, "text/html", "base64");
		} catch (final UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
}
