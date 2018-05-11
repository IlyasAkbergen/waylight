package com.backendless.examples.login_with_sdk;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.backendless.Backendless;
import com.backendless.registration.login_activity;
import com.backendless.registration.registration_activity;

public class choose_social_network_activity extends Activity {
	private ImageButton login_with_facebook_sdk;
	private ImageButton login_with_google_plus_sdk;
	private Button search;
	private Button login;
	private Button registration;
	SharedPreferences sPref;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_choose_social_network);
		RelativeLayout lay = (RelativeLayout)findViewById(R.id.activity_choose_social_network);
		lay.setBackgroundResource(R.drawable.background);
		initUI();
		initUIBehavior();
		sPref = getPreferences(MODE_PRIVATE);
		SharedPreferences.Editor ed = sPref.edit();
		ed.putString(getString(R.string.SAVED_REQUESTS), "");
		ed.commit();

		Backendless.initApp(getApplicationContext(), getString(R.string.backendless_AppId), getString(R.string.backendless_ApiKey));
		Backendless.setUrl(getString(R.string.backendless_ApiHost));
	}

	private void initUI() {
		login_with_facebook_sdk = (ImageButton) findViewById(R.id.button_loginWithFacebookSDK);
		login_with_google_plus_sdk = (ImageButton) findViewById(R.id.button_loginWithGooglePlusSDK);
		search = (Button) findViewById(R.id.search);
		login = (Button) findViewById(R.id.signInBtn);
		registration = (Button) findViewById(R.id.signUpBtn);
	}

	private void initUIBehavior() {
		login_with_facebook_sdk.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(choose_social_network_activity.this, login_with_facebook_sdk_activity.class);
				startActivity(intent);
			}
		});

		login_with_google_plus_sdk.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(choose_social_network_activity.this, login_with_google_plus_sdk_activity.class);
				startActivity(intent);
			}
		});

		search.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(choose_social_network_activity.this, form_activity.class);
				startActivity(intent);
			}
		});
		registration.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(choose_social_network_activity.this, registration_activity.class);
				startActivity(intent);
			}
		});
		login.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(choose_social_network_activity.this, login_activity.class);
				startActivity(intent);
			}
		});

	}
}