package com.visualdialer.visualdialer;

import android.app.Instrumentation;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.test.ActivityInstrumentationTestCase2;

public class PreferencesTest extends ActivityInstrumentationTestCase2<Preferences> {

	private Preferences mActivity;
	
	public PreferencesTest(){
		super("com.visualdialer.visualdialer", Preferences.class);
	}
	
	protected void setUp() throws Exception {
		super.setUp();
		mActivity = getActivity();
	}
	
	public void testMappingPause(){
		final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mActivity);
		final Editor editor = preferences.edit();
		
		Instrumentation mInstr = this.getInstrumentation(); 
		
		//Set preferences.
		final String firstName = "Mary";
		editor.putString("prefFirstName", firstName);
		final String lastName = "Sue";
		editor.putString("prefLastName", lastName);
		final String address1 = "123 Fake Street";
		editor.putString("prefAddr1", address1);
		final String address2 = "";
		editor.putString("prefAddr2", address2);
		final String city = "San Francisco";
		editor.putString("prefCity", city);
		final String zipCode = "94101";
		editor.putString("prefZipCode", zipCode);
		final String cellPhone = "555.555.5555";
		editor.putString("prefCellPhone", cellPhone);
		editor.commit();
		
		//Tests if preferences match inputs.
		mInstr.callActivityOnResume(mActivity);
		assertEquals(firstName, preferences.getString("prefFirstName", ""));
	    assertEquals(lastName, preferences.getString("prefLastName", ""));
	    assertEquals(address1, preferences.getString("prefAddr1", ""));
	    assertEquals(address2, preferences.getString("prefAddr2", ""));
	    assertEquals(city, preferences.getString("prefCity", ""));
	    assertEquals(zipCode, preferences.getString("prefZipCode", ""));
	    assertEquals(cellPhone, preferences.getString("prefCellPhone", ""));
	}
	
	public void testMappingDestroy(){
		final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mActivity);
		final Editor editor = preferences.edit();
		
		//Set preferences.
		final String firstName = "Mary";
		editor.putString("prefFirstName", firstName);
		final String lastName = "Sue";
		editor.putString("prefLastName", lastName);
		final String address1 = "123 Fake Street";
		editor.putString("prefAddr1", address1);
		final String address2 = "";
		editor.putString("prefAddr2", address2);
		final String city = "San Francisco";
		editor.putString("prefCity", city);
		final String zipCode = "94101";
		editor.putString("prefZipCode", zipCode);
		final String cellPhone = "555.555.5555";
		editor.putString("prefCellPhone", cellPhone);
		editor.commit();
		
		//Tests if preferences match inputs after app terminates 
		// and restarts.
		//mInstr.callActivityOnResume(mActivity);
		mActivity.finish();
		mActivity = this.getActivity();
		
		assertEquals(firstName, preferences.getString("prefFirstName", ""));
	    assertEquals(lastName, preferences.getString("prefLastName", ""));
	    assertEquals(address1, preferences.getString("prefAddr1", ""));
	    assertEquals(address2, preferences.getString("prefAddr2", ""));
	    assertEquals(city, preferences.getString("prefCity", ""));
	    assertEquals(zipCode, preferences.getString("prefZipCode", ""));
	    assertEquals(cellPhone, preferences.getString("prefCellPhone", ""));
	}
}
