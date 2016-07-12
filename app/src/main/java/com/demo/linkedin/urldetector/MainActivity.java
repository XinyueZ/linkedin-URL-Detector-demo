package com.demo.linkedin.urldetector;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.demo.linkedin.urldetector.databinding.MainActivityBinding;
import com.linkedin.urls.Url;
import com.linkedin.urls.detection.UrlDetector;
import com.linkedin.urls.detection.UrlDetectorOptions;

import java.util.List;

public final class MainActivity extends AppCompatActivity {
	private MainActivityBinding mBinding;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
		setSupportActionBar(mBinding.toolbar);
		mBinding.contentMainLayout.loadPb.hide();
		mBinding.contentMainLayout.webview.setWebViewClient(new WebViewClient() {
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				super.onPageStarted(view, url, favicon);
				mBinding.contentMainLayout.loadPb.show();
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				mBinding.contentMainLayout.loadPb.hide();
			}

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
				if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
					view.loadUrl(request.getUrl()
					                    .toString());
				}
				return true;
			}
		});


		mBinding.contentMainLayout.sourceEt.addTextChangedListener(new TextWatcher() {
			@Override
			public void afterTextChanged(Editable editable) {
				UrlDetector parser = new UrlDetector(editable.toString(), UrlDetectorOptions.Default);
				List<Url> found = parser.detect();
				//I use first element.
				if (found != null && found.size() > 0) {
					mBinding.contentMainLayout.loadPb.show();
					Url url = found.get(0);
					String urlStr = url.toString();
					mBinding.contentMainLayout.resultEt.setText(urlStr);
					mBinding.contentMainLayout.webview.loadUrl(urlStr);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

			}

			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

			}
		});

		mBinding.contentMainLayout.sourceEt.setText(getIntent().getStringExtra(Intent.EXTRA_TEXT));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement
		if (id == R.id.action_settings) {
			return true;
		}

		return super.onOptionsItemSelected(item);
	}
}
