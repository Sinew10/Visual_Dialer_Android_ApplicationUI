package com.visualdialer.visualdialer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * {@link Activity} for sending profile info to rep.
 *
 * @author Peter Radmanesh (peter.radmanesh@gmail.com).
 */
public class ConnectToRepActivity extends Activity {

	private static final String REP_CONNECT_MESSAGE_URL =
			"http://198.71.205.11/visualdialer/VisualDialerAPI/api/RepConnectMessages";
	private static final String REP_CONNECT_MESSAGE_VALUES_URL =
			"http://198.71.205.11/visualdialer/VisualDialerAPI/api/RepConnectMessageValues";
	private static final String LINE_BREAK = System.getProperty ("line.separator");
 
	private static final String PASSCODE = "Passcode";
	private static final String ID = "Id";
	private static final String REP_CONNECT_MESSAGE_ID = "RepConnectMessageId";
	private static final String NAME = "Name";
	private static final String VALUE = "Value";
	private static final String FIRST_NAME = "firstName";
	private static final String LAST_NAME = "lastName";
	private static final String ADDRESS = "address";
	private static final String PHONE_NUMBER = "phoneNumber";
	private static final String CUSTOM_MESSAGE = "customMessage";

	private SharedPreferences preferences;
	private Intent gotoPreferencesIntent;

	public Button gotoPreferencesButton;
	public EditText firstNameEditText;
	public EditText lastNameEditText;
	public EditText addressEditText;
	public EditText phoneNumberEditText;
	public EditText repIdEditText;
	public EditText customMessageEditText;
	public ProgressBar statusBar;
	public ImageView greenCheckMarkImageView;
	public TextView statusText;
	public Button sendToRepButton;

	private HttpUtils httpUtils;

	public ConnectToRepActivity() {
		super();
		setHttpUtils(new HttpUtils());
	}

	public void setHttpUtils(HttpUtils httpUtils) {
		this.httpUtils = httpUtils;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_connect_to_rep);
		preferences = PreferenceManager.getDefaultSharedPreferences(this);
		initialize();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.connect_to_rep, menu);
		return true;
	}

	@Override
	protected void onResume() {
		super.onResume();
		initState();
	}

	private void initialize() {
		gotoPreferencesIntent = new Intent(this, Preferences.class);
		gotoPreferencesButton = (Button) findViewById(R.id.b_ctr_goto_preferences);
		firstNameEditText = (EditText) findViewById(R.id.et_ctr_first_name);
		lastNameEditText = (EditText) findViewById(R.id.et_ctr_last_name);
		addressEditText = (EditText) findViewById(R.id.et_ctr_address);
		phoneNumberEditText = (EditText) findViewById(R.id.et_ctr_phone_number);
		customMessageEditText = (EditText) findViewById(R.id.et_ctr_message_to_rep);
		statusBar = (ProgressBar) findViewById(R.id.pb_ctr_working);
		greenCheckMarkImageView = (ImageView) findViewById(R.id.i_ctr_green_check_mark);
		statusText = (TextView) findViewById(R.id.tv_ctr_status);
		repIdEditText = (EditText) findViewById(R.id.et_ctr_rep_id);
		sendToRepButton = (Button) findViewById(R.id.b_ctr_send_to_rep);
	}

	private void initState() {
		final StringBuilder address = new StringBuilder();
		final String address1 = preferences.getString("prefAddr1", "");
		if (address1.length() > 0) {
			address.append(address1);
			address.append(LINE_BREAK);
		}
		final String address2 = preferences.getString("prefAddr2", "");
		if (address2.length() > 0) {
			address.append(address2);
			address.append(LINE_BREAK);
		}
		final String city = preferences.getString("prefCity", "");
		if (city.length() > 0) {
			address.append(city);
		}
		final String zipCode = preferences.getString("prefZipCode", "");
		if (zipCode.length() > 0) {
			if (city.length() > 0) {
				address.append(", ");
			}
			address.append(zipCode);
		}

		firstNameEditText.setText(preferences.getString("prefFirstName", ""));
		lastNameEditText.setText(preferences.getString("prefLastName", ""));
		addressEditText.setText(address.toString());
		phoneNumberEditText.setText(preferences.getString("prefCellPhone", ""));
		statusBar.setVisibility(ProgressBar.INVISIBLE);
		greenCheckMarkImageView.setVisibility(ImageView.INVISIBLE);
		statusText.setText("");
		repIdEditText.setText("");
		customMessageEditText.setText("");

		setFieldsEnabled(true);
		repIdEditText.requestFocus();
	}

	private void setFieldsEnabled(boolean enabled) {
		gotoPreferencesButton.setEnabled(enabled);
		firstNameEditText.setEnabled(enabled);
		lastNameEditText.setEnabled(enabled);
		addressEditText.setEnabled(enabled);
		phoneNumberEditText.setEnabled(enabled);
		repIdEditText.setEnabled(enabled);
		customMessageEditText.setEnabled(enabled);
		sendToRepButton.setEnabled(enabled);
	}

	public void gotoToPreferences(View view) {
		startActivity(gotoPreferencesIntent);
	}

	public void sendToRep(View view) throws JSONException {
		if (!validateFields()) {
			return;
		}
		sendProfileInfo();
	}
	
	private boolean validateFields() {
		boolean valid = true;
		
		if (!validateRequiredEditText(repIdEditText)) {
			valid = false;
		}
		
		return valid;
	}

	private String getTrimmedText(EditText editText) {
		return editText.getText().toString().trim();
	}

	private boolean validateRequiredEditText(EditText editText) {
		if (getTrimmedText(editText).length() > 0) {
			return true;
		}
		
		editText.requestFocus();
		return false;
	}
	
	private void sendProfileInfo() throws JSONException {
		setFieldsEnabled(false);
		statusBar.setVisibility(ProgressBar.VISIBLE);
		greenCheckMarkImageView.setVisibility(ImageView.INVISIBLE);

		final String passcode = repIdEditText.getText().toString().trim();
		statusText.setText("Sending info to rep " + passcode);

		final JSONObject passcodeJson = new JSONObject();
		passcodeJson.put(PASSCODE, passcode);

		final String repConnectMessageResponse =
				httpUtils.makeJsonRequest(REP_CONNECT_MESSAGE_URL, passcodeJson);
		final JSONObject repConnectMessageJson = new JSONObject(repConnectMessageResponse);

		final String repConnectMessageId = repConnectMessageJson.getString(ID);
		final JSONArray repConnectMessageValuesJson = new JSONArray();
		repConnectMessageValuesJson.put(createRepConnectMessageJsonObject(
				repConnectMessageId, FIRST_NAME, firstNameEditText));
		repConnectMessageValuesJson.put(createRepConnectMessageJsonObject(
				repConnectMessageId, LAST_NAME, lastNameEditText));
		repConnectMessageValuesJson.put(createRepConnectMessageJsonObject(
				repConnectMessageId, ADDRESS, addressEditText));
		repConnectMessageValuesJson.put(createRepConnectMessageJsonObject(
				repConnectMessageId, PHONE_NUMBER, phoneNumberEditText));
		repConnectMessageValuesJson.put(createRepConnectMessageJsonObject(
				repConnectMessageId, CUSTOM_MESSAGE, customMessageEditText));

		final String repConnectMessageValuesResponse = httpUtils.makeJsonRequest(
				REP_CONNECT_MESSAGE_VALUES_URL, repConnectMessageValuesJson);
		statusBar.setVisibility(ProgressBar.INVISIBLE);
		final JSONArray repConnectMessageResponseJson =
				new JSONArray(repConnectMessageValuesResponse);
		if (repConnectMessageResponseJson.length() == repConnectMessageValuesJson.length()) {
			greenCheckMarkImageView.setVisibility(ImageView.VISIBLE);
			statusText.setText("Info sent to rep " + passcode);
		} else {
			statusText.setText("Please try again...");
		}
		setFieldsEnabled(true);
	}

	private JSONObject createRepConnectMessageJsonObject(String repConnectMessageId, String name,
			EditText editText) throws JSONException {
		final JSONObject jsonObject = new JSONObject();
		jsonObject.put(REP_CONNECT_MESSAGE_ID, repConnectMessageId);
		jsonObject.put(NAME, name);
		jsonObject.put(VALUE, editText.getText().toString().trim());
		return jsonObject;
	}
}
