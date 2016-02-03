package com.visualdialer.visualdialer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.telephony.SmsManager;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.plus.PlusClient;
import com.google.android.gms.plus.model.moments.Moment;
import com.google.android.gms.plus.model.moments.MomentBuffer;
import com.google.android.gms.plus.PlusShare;
//import com.google.android.gms.plus.PlusShare;

public class Share extends Activity implements View.OnClickListener
{	
	Button bG;
	Button bTwit;
	Button bFace;
	Button bEmail;
	Button bText;
	TextView TVShare;
	String email = "g8keeper87@gmail.com";
	String textNumber = "18053146230";
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.share);
		initialize();
	}
	private void initialize() 
	{
		TVShare = (TextView)findViewById(R.id.tvShare);	
		bG = (Button)findViewById(R.id.bGoogle);
		bG.setOnClickListener(this);
//		bTwit = (Button)findViewById(R.id.bShareT);
//		bTwit.setOnClickListener(this);
//		bFace = (Button)findViewById(R.id.bShareFB);
//		bFace.setOnClickListener(this);
		bText = (Button)findViewById(R.id.bText);
		bText.setOnClickListener(this);
		bEmail = (Button)findViewById(R.id.bEmail);
		bEmail.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) 
	{
		// TODO Auto-generated method stub
		switch(v.getId())
		{
			case R.id.bGoogle:
				//shareGoogle();
				Intent s;
				//s = new Intent("com.visualdialer.visualdialer.Sample");
				s = new Intent("com.visualdialer.visualdialer.SignIn");
				startActivity(s);
				break;
//			case R.id.bShareT:
//				break;
			case R.id.bEmail:
				sendEmail();
				break;
			case R.id.bText:
				sendText("18053146230");
				break;
//			case R.id.bShareFB:
//				break;
		}
	}
	public void shareGoogle()
	{
		Intent shareIntent = new PlusShare.Builder(this)
        .setType("text/plain")
        .setText("Welcome to the Google+ platform.")
        .setContentUrl(Uri.parse("https://developers.google.com/+/"))
        .getIntent();
		startActivityForResult(shareIntent, 0);
	}
	public void sendText(String phoneNum)
	{
		try 
		{
			AlertDialog.Builder alert = new AlertDialog.Builder(this);

			alert.setTitle("PHONE NUMBER");
			alert.setMessage("Who do you want to send a text to?");

			// Set an EditText view to get user input 
			final EditText input = new EditText(this);
			alert.setView(input);

			alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						textNumber = input.getText().toString();
						SmsManager.getDefault().sendTextMessage(textNumber, null, "Ive just used Visual Dialer to contact " + globVar.getSelectedBus().get_name() + " - " + globVar.getSelectedItem().get_name() +"." , null, null);
					}
				}
			);

			alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			  public void onClick(DialogInterface dialog, int whichButton) {
			    // Canceled.
			  }
			});

			alert.show();
			
		} 
		catch (Exception e) 
		{
			globVar.toast(this, "DIDN'T SEND");
			AlertDialog.Builder alertDialogBuilder = new
			AlertDialog.Builder(this);
			AlertDialog dialog = alertDialogBuilder.create();
			dialog.setMessage(e.getMessage());
		}
	}
	public void sendEmail()
	{
		AlertDialog.Builder alert = new AlertDialog.Builder(this);

		alert.setTitle("EMAIL ADDRESS");
		alert.setMessage("Who do you want to send an email to?");

		// Set an EditText view to get user input 
		final EditText input = new EditText(this);
		alert.setView(input);

		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					email = input.getText().toString();
					Intent displayIntent = new Intent(android.content.Intent.ACTION_SEND);
		            displayIntent.setType("text/plain");
		            displayIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{email});
		            displayIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject");
		            displayIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Ive just used Visual Dialer to contact " + globVar.getSelectedBus().get_name() + " - " + globVar.getSelectedItem().get_name() +".");

		            startActivity(displayIntent); 
				}
			}
		);

		alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
		  public void onClick(DialogInterface dialog, int whichButton) {
		    // Canceled.
		  }
		});

		alert.show();			
	}	
}
