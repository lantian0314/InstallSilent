package com.example.vpn;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

import com.example.test.ceshiLog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MainActivity extends Activity {

	private WebView webView;
	private static int count=1;//链接次数
	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		webView = new WebView(getApplicationContext());
		WebSettings webSettings = webView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webSettings.setAllowFileAccess(true);
		webSettings.setBuiltInZoomControls(true);
		webView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				if (url != null) {
					String path = Environment.getExternalStorageDirectory()
							.getAbsolutePath() + "/"+"India.txt";
					boolean write = writeFile(path, "第"+count+"次URL:"+url+"\n");
					count++;
					ceshiLog.PrintLog("url:" + url);
					if (write) {
						ceshiLog.PrintLog("write:" + write);
					}
				}
				return super.shouldOverrideUrlLoading(view, url);
			}
		});
		webView.loadUrl("http://nts.androidadvertisement.com/in/pz/");
		setContentView(webView);
	}

	private boolean writeFile(String path, String data) {
		try {
			FileOutputStream writerStream = new FileOutputStream(path,true);
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
					writerStream, "UTF-8"));
			writer.write(data);
			writer.flush();
			writer.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}
