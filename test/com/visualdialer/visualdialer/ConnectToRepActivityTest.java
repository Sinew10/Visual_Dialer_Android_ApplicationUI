package com.visualdialer.visualdialer;

import android.app.Instrumentation;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.test.ActivityUnitTestCase;
import android.test.suitebuilder.annotation.SmallTest;

/**
 * {@link ActivityUnitTestCase} for {@link ConnectToRepActivity}.
 *
 * @author Peter Radmanesh (peter.radmanesh@gmail.com).
 */
public class ConnectToRepActivityTest extends ActivityUnitTestCase<ConnectToRepActivity> {
	private static final String LINE_BREAK = System.getProperty ("line.separator");

	private HttpUtils mockHttpUtils;

	private Instrumentation instrumentation;
	private ConnectToRepActivity activity;

	public ConnectToRepActivityTest() {
		super(ConnectToRepActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
	    super.setUp();
	    mockHttpUtils = new HttpUtils() {
	    	
	    };

	    instrumentation = getInstrumentation();

	    final Intent intent = new Intent(instrumentation.getTargetContext(), ConnectToRepActivity.class);
	    startActivity(intent, null, null);
	    activity = getActivity();
	    activity.setHttpUtils(mockHttpUtils);
    }

	@SmallTest
	public void testSetsDefaultsOnResume() {
		final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity);
	    final Editor editor = preferences.edit();
	    final String firstName = "John";
	    editor.putString("prefFirstName", firstName);
	    final String lastName = "Doe";
	    editor.putString("prefLastName", lastName);
	    final String address1 = "1234 Any Street";
	    editor.putString("prefAddr1", address1);
	    final String address2 = "Suite 987";
	    editor.putString("prefAddr2", address2);
	    final String city = "Anytown";
	    editor.putString("prefCity", city);
	    final String zipCode = "91827";
	    editor.putString("prefZipCode", zipCode);
	    final String cellPhone = "987.654.3210";
	    editor.putString("prefCellPhone", cellPhone);
	    editor.commit();

	    activity.firstNameEditText.setEnabled(false);
	    activity.lastNameEditText.setEnabled(false);
	    activity.addressEditText.setEnabled(false);
	    activity.phoneNumberEditText.setEnabled(false);
	    activity.statusText.setText("loading...");
	    activity.repIdEditText.setText("000A");
	    activity.repIdEditText.setEnabled(false);
	    activity.sendToRepButton.setEnabled(false);

	    instrumentation.callActivityOnResume(activity);
	    assertEquals(firstName, activity.firstNameEditText.getText().toString());
	    assertTrue(activity.firstNameEditText.isEnabled());
	    assertEquals(lastName, activity.lastNameEditText.getText().toString());
	    assertTrue(activity.lastNameEditText.isEnabled());
	    assertEquals(address1 + LINE_BREAK + address2 + LINE_BREAK + city + ", " + zipCode,
	    		activity.addressEditText.getText().toString());
	    assertTrue(activity.addressEditText.isEnabled());
	    assertEquals(cellPhone, activity.phoneNumberEditText.getText().toString());
	    assertTrue(activity.phoneNumberEditText.isEnabled());
	    assertTrue(activity.statusText.getText().toString().isEmpty());
	    assertTrue(activity.repIdEditText.getText().toString().isEmpty());
	    assertTrue(activity.repIdEditText.isEnabled());
	    assertTrue(activity.sendToRepButton.isEnabled());
	    assertTrue(activity.repIdEditText.hasFocus());
	}
}
