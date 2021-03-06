package com.backendless.examples.login_with_sdk;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.api.Status;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;

import java.util.Map;
import java.util.concurrent.TimeUnit;
public class login_with_google_plus_sdk_activity extends Activity {


	private SignInButton loginGooglePlusButton;
	private Button gpLogoutBackendlessButton;

	private final int RC_SIGN_IN = 112233; // arbitrary number
	private GoogleApiClient mGoogleApiClient;
	private String gpAccessToken = null;

	private boolean isLoggedInGoogle = false;
	private boolean isLoggedInBackendless = false;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login_with_google_plus_sdk);
		initUI();
		initUIBehaviour();
	}

	private void initUI() {
		loginGooglePlusButton = (SignInButton) findViewById(R.id.button_googlePlusLogin);
	}

	private void initUIBehaviour() {
		configureGooglePlusSDK();

		if (mGoogleApiClient.isConnected())
		{
			OptionalPendingResult pendingResult = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
			if (pendingResult.isDone())
				handleSignInResult((GoogleSignInResult) pendingResult.get());
		}


		BackendlessUser user = Backendless.UserService.CurrentUser();
		if (user != null)
		{
			isLoggedInBackendless = true;

			loginGooglePlusButton.setVisibility(View.INVISIBLE);
			gpLogoutBackendlessButton.setVisibility(View.VISIBLE);
		}
	}

	private void loginToBackendless()
	{
		Backendless.UserService.loginWithGooglePlusSdk(gpAccessToken, new AsyncCallback<BackendlessUser>() {
			@Override
			public void handleResponse(BackendlessUser response) {
				isLoggedInBackendless = true;

				String msg = "ObjectId: " + response.getObjectId() + "\n"
						+ "UserId: " + response.getUserId() + "\n"
						+ "Email: " + response.getEmail() + "\n"
						+ "Properties: " + "\n";

				for (Map.Entry<String, Object> entry : response.getProperties().entrySet())
					msg += entry.getKey() + " : " + entry.getValue() + "\n";

				loginGooglePlusButton.setVisibility(View.INVISIBLE);
				gpLogoutBackendlessButton.setVisibility(View.VISIBLE);
			}

			@Override
			public void handleFault(BackendlessFault fault) {
			}
		});
	}

	private void logoutFromBackendless(){
		Backendless.UserService.logout(new AsyncCallback<Void>() {
			@Override
			public void handleResponse(Void response) {
				isLoggedInBackendless = false;
				gpLogoutBackendlessButton.setVisibility(View.INVISIBLE);
				loginGooglePlusButton.setVisibility(View.VISIBLE);
			}

			@Override
			public void handleFault(BackendlessFault fault) {
			}
		});
	}

	private void configureGooglePlusSDK()
	{
		GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
				.requestScopes(new Scope(Scopes.PROFILE), new Scope(Scopes.PLUS_ME))
				.requestId()
				.requestIdToken(getString(R.string.gp_WebApp_ClientId))
				.requestServerAuthCode(getString(R.string.gp_WebApp_ClientId), false)
				.requestEmail()
				.build();

		mGoogleApiClient = new GoogleApiClient.Builder(this)
//				.enableAutoManage(this , this /* OnConnectionFailedListener */)
				//.addOnConnectionFailedListener((GoogleApiClient.OnConnectionFailedListener) this)
				.addApi(Auth.CREDENTIALS_API)
				.addApi(Auth.GOOGLE_SIGN_IN_API, gso)
				.addScope(new Scope(Scopes.PROFILE))
				.addScope(new Scope(Scopes.PLUS_ME))
				.build();

		loginGooglePlusButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
				startActivityForResult(signInIntent, RC_SIGN_IN);
			}
		});
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		// Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
		if (requestCode == RC_SIGN_IN) {
			GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
			handleSignInResult(result);
		}
	}

	private void handleSignInResult(GoogleSignInResult result) {
		//Log.d(TAG, "handleSignInResult:" + result.isSuccess());
		if (result.isSuccess()) {
			isLoggedInGoogle = true;
			loginGooglePlusButton.setVisibility(View.INVISIBLE);
			gpLogoutBackendlessButton.setVisibility(View.VISIBLE);

			//this is old approach to get google access token:
			//final String scopes = "oauth2:" + Scopes.PLUS_LOGIN + " " + Scopes.PLUS_ME + " " + Scopes.PROFILE + " " + Scopes.EMAIL;
			//gpAccessToken = GoogleAuthUtil.getToken(LoginWithGooglePlusSDKActivity.this, result.getSignInAccount().getEmail(), scopes);

			final String gpAuthToken = result.getSignInAccount().getServerAuthCode();

			Thread t = new Thread(new Runnable() {
				@Override
				public void run() {
					exchangeAuthTokenOnAccessToken(gpAuthToken);

					/***********************************************************************
					 * Now that the login to Google has completed successfully, the code
					 * will login to Backendless by "exchanging" Google's access token
					 * for BackendlessUser. This is done in the loginToBackendless() method.
					 ***********************************************************************/
					loginToBackendless();
				}
			});
			t.setDaemon(true);
			t.start();

			//updateUI(true);
		} else {
			// Signed out, show unauthenticated UI.
			gpAccessToken = null;
			String msg = "Unsuccessful login.\nStatus message:\n" + result.getStatus().getStatusMessage();
			isLoggedInGoogle = false;
			//updateUI(false);
		}
	}

	private String exchangeAuthTokenOnAccessToken(String gpAuthToken ) {
		GoogleTokenResponse tokenResponse = null;
		try {
			tokenResponse = new GoogleAuthorizationCodeTokenRequest(
				new NetHttpTransport(),
				JacksonFactory.getDefaultInstance(),
				"https://www.googleapis.com/oauth2/v4/token",
				getString(R.string.gp_WebApp_ClientId),
				getString(R.string.gp_WebApp_ClientSecret),
				gpAuthToken,
				"")  // Specify the same redirect URI that you use with your web
				// app. If you don't have a web version of your app, you can
				// specify an empty string.
				.execute();
		} catch (Exception e) {
			Log.e("LoginWithGooglePlus", e.getMessage(), e);
			return null;
		}

		gpAccessToken = tokenResponse.getAccessToken();

		this.runOnUiThread(new Runnable() {
			@Override
			public void run() {
			}
		});
		return gpAccessToken;
	}

	private void logoutFromGoogle()
	{
		AsyncTask.execute(new Runnable() {
			@Override
			public void run() {
				mGoogleApiClient.blockingConnect(10, TimeUnit.SECONDS);
				if (!mGoogleApiClient.isConnected())
				{
					return;
				}

				Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
					new ResultCallback<Status>() {
						@Override
						public void onResult(@NonNull Status status) {
							if (!status.isSuccess())
								return;

							isLoggedInGoogle = false;
							gpLogoutBackendlessButton.setVisibility(View.INVISIBLE);
							loginGooglePlusButton.setVisibility(View.VISIBLE);

							gpAccessToken = null;
						}
					});
			}
		});
	}
}