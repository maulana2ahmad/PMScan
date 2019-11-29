package com.example.pmscan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;


public class SplashScreenActivity extends AppCompatActivity {

	final private static int TIME_SPLASH = 2000;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash_screen);

		TextView tvVersion = (TextView) findViewById(R.id.versionId);

		PackageInfo packageInfo = null;
		try {
			packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);

		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}
		tvVersion.setText(packageInfo.versionName);

		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {

				startActivity(new Intent(getApplicationContext(), MainActivity.class));
				finish();
			}
		}, TIME_SPLASH);
	}
}
