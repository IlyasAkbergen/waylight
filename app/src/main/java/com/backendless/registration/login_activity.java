package com.backendless.registration;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.examples.login_with_sdk.R;
import com.backendless.examples.login_with_sdk.form_activity;
import com.backendless.exceptions.BackendlessFault;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class login_activity extends Activity
{
    private static final int REGISTER_REQUEST_CODE = 1;

    protected void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_login );

        Backendless.initApp( this, backend_settings.APPLICATION_ID, backend_settings.ANDROID_SECRET_KEY);

        Button loginButton = (Button) findViewById( R.id.loginB );
        loginButton.setOnClickListener( new View.OnClickListener(){
            public void onClick( View v ) {
                EditText emailField = (EditText) findViewById( R.id.emailField );
                EditText passwordField = (EditText) findViewById( R.id.passwordField );

                CharSequence email = emailField.getText();
                CharSequence password = passwordField.getText();

                if( isLoginValuesValid( password ) ) {
                    Backendless.UserService.login(email.toString(), password.toString(), new AsyncCallback<BackendlessUser>() {
                        @Override
                        public void handleResponse(BackendlessUser response) {
                            Toast.makeText( login_activity.this, getString( R.string.info_logged_in ), Toast.LENGTH_LONG ).show();
                            Intent intent = new Intent(login_activity.this, form_activity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }

                        public void handleFault(BackendlessFault fault) {
                            Toast.makeText(login_activity.this, "Error loggin in.", Toast.LENGTH_SHORT).show();
                        }
                    }, true);

//                    loading_callback<BackendlessUser> loginCallback = createLoginCallback();
//
//                    loginCallback.showLoading();
//                    loginUser( email, password, loginCallback );
                }
            }
        });

        makeRegistrationLink();
    }

    public void makeRegistrationLink()
    {
        SpannableString registrationPrompt = new SpannableString( getString( R.string.register_prompt ) );

        ClickableSpan clickableSpan = new ClickableSpan()
        {
            @Override
            public void onClick( View widget )
            {
                startRegistrationActivity();
            }
        };

        String linkText = getString( R.string.register_link );
        int linkStartIndex = registrationPrompt.toString().indexOf( linkText );
        int linkEndIndex = linkStartIndex + linkText.length();
        registrationPrompt.setSpan( clickableSpan, linkStartIndex, linkEndIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE );

        TextView registerPromptView = (TextView) findViewById( R.id.registerPromptText );
        registerPromptView.setText( registrationPrompt );
        registerPromptView.setMovementMethod( LinkMovementMethod.getInstance() );
    }


    public void startRegistrationActivity()
    {
        Intent registrationIntent = new Intent( this, registration_activity.class );
        startActivityForResult( registrationIntent, REGISTER_REQUEST_CODE );
    }

//
//    public void loginUser( String email, String password, AsyncCallback<BackendlessUser> loginCallback )
//    {
//        Backendless.UserService.login( email, password, loginCallback, true );
//    }

//    public View.OnClickListener createLoginButtonListener() {
//        return new View.OnClickListener() {
//            @Override
//
//        };
//    }

    public boolean isLoginValuesValid( CharSequence password ) {
        return validator.isPasswordValid( this, password );
    }

//    public loading_callback<BackendlessUser> createLoginCallback()
//    {
//        return new loading_callback<BackendlessUser>( this, getString( R.string.loading_login ) )
//        {
//            @Override
//            public void handleResponse( BackendlessUser loggedInUser )
//            {
//                super.handleResponse( loggedInUser );
//                Toast.makeText( login_activity.this, String.format( getString( R.string.info_logged_in ), loggedInUser.getObjectId() ), Toast.LENGTH_LONG ).show();
//            }
//        };
//    }

    @Override
    protected void onActivityResult( int requestCode, int resultCode, Intent data ) {
        if( resultCode == RESULT_OK ) {
            switch( requestCode ) {
                case REGISTER_REQUEST_CODE:
                    String email = data.getStringExtra( BackendlessUser.EMAIL_KEY );
                    EditText emailField = (EditText) findViewById( R.id.emailField );
                    emailField.setText( email );

                    EditText passwordField = (EditText) findViewById( R.id.passwordField );
                    passwordField.requestFocus();

                    Toast.makeText( this, getString( R.string.info_registered_success ), Toast.LENGTH_SHORT ).show();
            }
        }
    }
}