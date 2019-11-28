package com.example.pmscan;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.google.zxing.Result;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

@SuppressWarnings({"ALL", "deprecation"})
public class MainActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {

	private static final String TAG = MainActivity.class.getSimpleName();
	private WebView wv_web_Id;
	private String barcode = "http://pm.mncgroup.com:8008/docs/show?b=";
	//private String home = "http://pm.mncgroup.com:8008/home";
	private String linked;
	private String pageWebViewlogin = "http://pm.mncgroup.com:8008/login";

	private SwipeRefreshLayout swipeRefresh_ID;

	private ZXingScannerView scannerView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		wv_web_Id = findViewById(R.id.wv_web_Id);
		swipeRefresh_ID = findViewById(R.id.swipeRefreshID);
		//wv_web_Id.loadUrl(pageWebViewlogin);

		setActionBar();

		swipeRefresh_ID.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
			@Override
			public void onRefresh() {
				setLoadWeb();
			}
		});

		setLoadWeb();
	}

	@Override
	public void onStart() {
		super.onStart();
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onStop() {
		super.onStop();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}


	private void setActionBar() {

		Toolbar toolbar = findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		ActionBar bar = getSupportActionBar();
		//bar.setDisplayUseLogoEnabled(false);
		//bar.setDisplayHomeAsUpEnabled(true); //arrow
		bar.setDisplayShowTitleEnabled(false);
	}

	@SuppressWarnings("deprecation")
	private void setLoadWeb() {

		//object Zxin
		scannerView = new ZXingScannerView(MainActivity.this);
		//noinspection deprecation,deprecation
		wv_web_Id.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
		wv_web_Id.getSettings().setJavaScriptEnabled(true);
		wv_web_Id.getSettings().setLoadsImagesAutomatically(true);
		wv_web_Id.getSettings().setDomStorageEnabled(true);
		wv_web_Id.getSettings().setAppCacheEnabled(true);
		wv_web_Id.loadUrl(pageWebViewlogin);
		Log.d("Web Load", "WEBVIEW " + pageWebViewlogin);
		wv_web_Id.requestFocusFromTouch();

		//scroll
		//wv_web_Id.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
		swipeRefresh_ID.setRefreshing(true);
		wv_web_Id.setWebViewClient(new WebViewClient() {

			public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
				//Toast.makeText(getApplicationContext(), "Error is occured, please try again..." + request, Toast.LENGTH_LONG).show();
				String errorHtml = "<html><body><h2><center>Please swipe refresh to start again!</center></h2></body></html>";
				view.loadDataWithBaseURL("", errorHtml, "text/html", "utf-8", null);

//				LayoutInflater inflater1 = getLayoutInflater();
//				View v = inflater1.inflate(R.layout.placeholder_empty, null);
//				view.addView(v);
//
//				//wv_web_Id.setVisibility(View.GONE);
//				v.setVisibility(View.GONE);
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);

				swipeRefresh_ID.setRefreshing(false);
				Log.d(TAG, "onPageFinished: " + swipeRefresh_ID);
			}
		});

		/*reload pade web*/
		wv_web_Id.setWebChromeClient(new WebChromeClient() {

			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				super.onProgressChanged(view, newProgress);

				if (view.getProgress() == 100) {
					swipeRefresh_ID.setRefreshing(false);

				} else {
					swipeRefresh_ID.setRefreshing(true);
				}
			}
		});

		/*start internet connection*/
		if (new InternetDialog(this).getInternetStatus()) {
			Toast.makeText(this, "INTERNET VALIDATION PASSED", Toast.LENGTH_SHORT).show();
		}
		/*end internet connection*/
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(@NonNull MenuItem item) {

		int itemMenu = item.getItemId();
		if (itemMenu == R.id.scan_id) {

			new IntentIntegrator(MainActivity.this).initiateScan();
			IntentIntegrator integrator = new IntentIntegrator(this);
			integrator.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);
			integrator.setPrompt("Scan a barcode");
			integrator.setCameraId(0);  // Use a specific camera of the device
			integrator.setBeepEnabled(false);
			integrator.setBarcodeImageEnabled(true);
			integrator.initiateScan();

		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public void handleResult(Result rawResult) {

		Log.v("TAG", rawResult.getText()); // Prints scan results
		Log.v("TAG", rawResult.getBarcodeFormat().toString());

//		Intent i = new Intent();
//		i.putExtra("code", rawResult.getText());
//		Log.e("CODE", "handleResult: " + rawResult.getText());
//		MainActivity.this.setResult(10, i);
//
//		scannerView.resumeCameraPreview(this);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
		String isi = result.getContents();

		if (result != null) {
			linked = barcode + isi;
			wv_web_Id.loadUrl(linked);
			Log.d("LINKED", "onActivityResult: " + linked);
		} else {

			onBackPressed();

		}
	}

	//clickback
	private boolean clickback = false;
	private int timeOut = 3000;

	@Override
	public void onBackPressed() {

		if (clickback == true) {
			super.onBackPressed();
			return;
		}
		clickback = true;

		Toast toast = Toast.makeText(getApplicationContext(), "Press once againt to exit", Toast.LENGTH_LONG);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();

		Handler h = new Handler();
		h.postDelayed(new Runnable() {
			@Override
			public void run() {

				clickback = false;
			}
		}, timeOut);
	}
}
