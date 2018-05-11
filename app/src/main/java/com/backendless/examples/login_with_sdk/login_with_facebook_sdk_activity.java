package com.backendless.examples.login_with_sdk;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.util.Map;

public class login_with_facebook_sdk_activity extends Activity {

	private LoginButton loginFacebookButton;

	private CallbackManager callbackManager;
	private String fbAccessToken = null;

	private boolean isLoggedInFacebook = false;
	private boolean isLoggedInBackendless = false;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login_with_facebook_sdk);
		initUI();
		initUIBehaviour();
	}

	private void initUI() {
		loginFacebookButton = (LoginButton) findViewById(R.id.button_FacebookLogin);
	}

	private void initUIBehaviour() {
		callbackManager = configureFacebookSDKLogin();

		if (AccessToken.getCurrentAccessToken() != null)
		{
			isLoggedInFacebook = true;
			fbAccessToken = AccessToken.getCurrentAccessToken().getToken();
		}

		BackendlessUser user = Backendless.UserService.CurrentUser();
		if (user != null)
		{
			isLoggedInBackendless = true;
			loginFacebookButton.setVisibility(View.INVISIBLE);
		}
	}

	private void loginToBackendless()
	{
		Backendless.UserService.loginWithFacebookSdk(fbAccessToken, new AsyncCallback<BackendlessUser>() {
			@Override
			public void handleResponse(BackendlessUser response) {
				isLoggedInBackendless = true;

				Intent intent = new Intent(login_with_facebook_sdk_activity.this, form_activity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
				startActivity(intent);
			}

			@Override
			public void handleFault(BackendlessFault fault) {
				//Toast.makeText(getBaseContext(), "Error during facebook login", Toast.LENGTH_SHORT).show();
			}
		}, true);
	}

//	private void logoutFromBackendless(){
//		Backendless.UserService.logout(new AsyncCallback<Void>() {
//			@Override
//			public void handleResponse(Void response) {
//				isLoggedInBackendless = false;
//				loginFacebookButton.setVisibility(View.VISIBLE);
//			}
//
//			@Override
//			public void handleFault(BackendlessFault fault) {
//			}
//		});
//	}

	private CallbackManager configureFacebookSDKLogin() {
		loginFacebookButton.setReadPermissions("email");
		// If using in a fragment
		//loginFacebookButton.setFragment(this);

		CallbackManager callbackManager = CallbackManager.Factory.create();

		// Callback registration
		loginFacebookButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
			@Override
			public void onSuccess(LoginResult loginResult) {
				isLoggedInFacebook = true;

				if (!isLoggedInBackendless)
					loginToBackendless();
				else
					loginFacebookButton.setVisibility(View.INVISIBLE);
			}

			@Override
			public void onCancel() {
				// App code
				Log.i("LoginProcess", "loginFacebookButton::onCancel");
			}

			@Override
			public void onError(FacebookException exception) {
				fbAccessToken = null;
				String msg = exception.getMessage() + "\nCause:\n" + (exception.getCause() != null ? exception.getCause().getMessage() : "none");
				isLoggedInFacebook = false;
			}
		});

		return callbackManager;
	}

	private void logoutFromFacebook()
	{
		if (!isLoggedInFacebook)
			return;

		if (AccessToken.getCurrentAccessToken() != null)
			LoginManager.getInstance().logOut();

		isLoggedInFacebook = false;
		fbAccessToken = null;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		callbackManager.onActivityResult(requestCode, resultCode, data);
	}
}