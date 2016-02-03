package com.visualdialer.visualdialer;

import com.google.android.gms.common.*;
import com.google.android.gms.plus.PlusClient;
import com.google.android.gms.plus.PlusShare;
//import com.google.android.gms.samples.plus.R;
//import com.google.android.gms.samples.plus.MomentUtil;
//import com.google.android.gms.samples.plus.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.IntentSender;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class SignIn extends Activity implements OnClickListener,
		PlusClient.ConnectionCallbacks, PlusClient.OnConnectionFailedListener,
		PlusClient.OnAccessRevokedListener {

	private static final int DIALOG_GET_GOOGLE_PLAY_SERVICES = 1;

	private static final int REQUEST_CODE_SIGN_IN = 1;
	private static final int REQUEST_CODE_GET_GOOGLE_PLAY_SERVICES = 2;

	private TextView SignInStat;
	private PlusClient Client;
	private SignInButton Sign_In_Button;
	private View SignOut;
	private View Post;
	private ConnectionResult ConnectResult;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sign_in);

		Client = new PlusClient.Builder(this, this, this)
		.setActions("http://schemas.google.com/AddActivity", "http://schemas.google.com/BuyActivity")
        .build();
		SignInStat = (TextView) findViewById(R.id.tvGoogle);
		Sign_In_Button = (SignInButton) findViewById(R.id.bSignIn);
		Sign_In_Button.setOnClickListener(this);
		SignOut = findViewById(R.id.bSignOut);
		SignOut.setOnClickListener(this);
		Post = findViewById(R.id.bPost);
		Post.setOnClickListener(this);
	}

	@Override
	public void onStart() {
		super.onStart();
		Client.connect();
	}

	@Override
	public void onStop() {
		Client.disconnect();
		super.onStop();
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.bSignIn:
			int available = GooglePlayServicesUtil
					.isGooglePlayServicesAvailable(this);
			if (available != ConnectionResult.SUCCESS) {
				showDialog(DIALOG_GET_GOOGLE_PLAY_SERVICES);
				return;
			}

			try {
				SignInStat.setText("Signing in with Google+.");
							ConnectResult.startResolutionForResult(this,
						REQUEST_CODE_SIGN_IN);
			} catch (IntentSender.SendIntentException e) {
				// Fetch a new result to start.
				Client.connect();
			}
			break;
		case R.id.bSignOut:
			if (Client.isConnected()) {
				Client.clearDefaultAccount();
				Client.disconnect();
				Intent s;
				s = new Intent("com.visualdialer.visualdialer.Share");
				startActivity(s);
			}
			break;
		
		case R.id.bPost:
			if (Client.isConnected()) 
			{
				gPost();			
//				Client.clearDefaultAccount();
//				Client.disconnect();
//				Client.connect();
			}
			break;
		}
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		if (id != DIALOG_GET_GOOGLE_PLAY_SERVICES) {
			return super.onCreateDialog(id);
		}

		int available = GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(this);
		if (available == ConnectionResult.SUCCESS) {
			return null;
		}
		if (GooglePlayServicesUtil.isUserRecoverableError(available)) {
			return GooglePlayServicesUtil.getErrorDialog(available, this,
					REQUEST_CODE_GET_GOOGLE_PLAY_SERVICES);
		}
		return new AlertDialog.Builder(this)
				.setMessage("Sign in with Google Plus is not available.")
				.setCancelable(true).create();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_CODE_SIGN_IN
				|| requestCode == REQUEST_CODE_GET_GOOGLE_PLAY_SERVICES) {
			if (resultCode == RESULT_OK && !Client.isConnected()
					&& !Client.isConnecting()) {
				// This time, connect should succeed.
				Client.connect();
			}
		}
	}

	@Override
	public void onAccessRevoked(ConnectionResult status) {
		if (status.isSuccess()) {
			SignInStat.setText("Revoked Access");
		} else {
			SignInStat.setText("Unable to revoke access");
			Client.disconnect();
		}
		Client.connect();
	}

	@Override
	public void onConnected(Bundle connectionHint) {
		String currentPersonName = Client.getCurrentPerson() != null ? Client
				.getCurrentPerson().getDisplayName()
				: getString(R.string.unknown_person);
		SignInStat.setText("Signed in as " + currentPersonName + ".");
		updateButtons(true /* isSignedIn */);		
	}

	@Override
	public void onDisconnected() {
		SignInStat.setText("Loading...");
		Client.connect();
		updateButtons(false /* isSignedIn */);
	}

	@Override
	public void onConnectionFailed(ConnectionResult result) {
		ConnectResult = result;
	}

	private void updateButtons(boolean isSignedIn) {
		if (isSignedIn) {
			Sign_In_Button.setVisibility(View.INVISIBLE);
			SignOut.setEnabled(true);
		} else {
			if (ConnectResult == null) {
				// Disable the sign-in button until onConnectionFailed is called
				// with result.
				Sign_In_Button.setVisibility(View.INVISIBLE);
				SignInStat.setText("Loading...");
			} else {
				// Enable the sign-in button since a connection result is
				// available.
				Sign_In_Button.setVisibility(View.VISIBLE);
				SignInStat.setText("Signed Out.");
			}
			SignOut.setEnabled(false);
			//mRevokeAccessButton.setEnabled(false);
		}
	}
	public void gPost()
	{
		String postMessage = null;
		if (globVar.getSelectedBus() == null)//we know we are in cats or w/e
		{
			
		}else if(globVar.getSelectedItem() ==  null)//we knew we are in bus
		{
			postMessage = globVar.getSelectedBus().get_name();
		}
		else
		{
			postMessage = globVar.getSelectedBus().get_name() + " - " + globVar.getSelectedItem().get_name();
		   
		}
	      Intent shareIntent = new PlusShare.Builder(this)
	          .setType("text/plain")
	          .setText("I have contacted " + postMessage + " from Visual Dialer")
	          .setContentUrl(Uri.parse("visualdialer.com"))
	          .getIntent();
	      startActivityForResult(shareIntent, 0);
	}
}