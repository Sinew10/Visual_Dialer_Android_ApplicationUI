package com.visualdialer.visualdialer;

import android.app.Instrumentation;
import android.content.Intent;
import android.test.ActivityUnitTestCase;
import android.test.suitebuilder.annotation.SmallTest;

import com.visualdialer.visualdialer.favorites;
import java.io.IOException;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.EditTextPreference;
import android.preference.PreferenceManager;
import android.test.ActivityInstrumentationTestCase2;
import android.test.ActivityUnitTestCase;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.app.Instrumentation;
import android.content.Intent;
import android.test.ActivityUnitTestCase;
import android.test.suitebuilder.annotation.SmallTest;

import com.visualdialer.visualdialer.Action_Menu;
import com.visualdialer.visualdialer.Phone;
import com.visualdialer.visualdialer.favItem;
import com.visualdialer.visualdialer.favorites;
import com.visualdialer.visualdialer.Business;
import com.visualdialer.visualdialer.Category;
import com.visualdialer.visualdialer.DatabaseTables;
import com.visualdialer.visualdialer.MenuController;
import com.visualdialer.visualdialer.globVar;
import com.visualdialer.visualdialer.ConnectToRepActivity;
import com.visualdialer.visualdialer.Preferences;
import com.visualdialer.visualdialer.R;

import junit.framework.TestCase;

public class FavoritesTest extends ActivityUnitTestCase<favorites>{
	private Instrumentation instrumentation;
	private favorites activity;
	private favorites mockFav;
	private Instrumentation instrument;
	private Phone mockFavToAdd, mockFavInList;
	private Action_Menu mockActMenu;
	private globVar gloVa;
	private DatabaseTables mockDB;
	public FavoritesTest() {
		super(favorites.class);
	}
	
	@Override
	protected void setUp() throws Exception {
//		super.setUp();
//		instrument = getInstrumentation();
//		final Intent intent = new Intent(instrument.getTargetContext(), favorites.class);
//		startActivity(intent, null, null);
//		mockFav = getActivity();
	    super.setUp();
	    instrumentation = getInstrumentation();
	    final Intent intent = new Intent(instrumentation.getTargetContext(), Action_Menu.class);
	    startActivity(intent, null, null);
	    activity = getActivity();
    }
	@SmallTest
	public void testAddToFav() 
	{
		mockFavInList = new Phone();
		mockFavToAdd = new Phone();
		mockActMenu = new Action_Menu();
		DatabaseTables table = new DatabaseTables(instrumentation.getTargetContext());
		table.open();
		table.emptyFavs();
		//table.close();
		final String inFavName = "Best Buy";
		mockFavInList.setName(inFavName);
		final int inBusId = 0;
		mockFavInList.setBus_id(inBusId);
		final int inCatId = 0;
		mockFavInList.setCatId(inCatId);
		gloVa.setSelectedBus(mockFavInList);
		mockActMenu.runFav(table);
		String temp[] = table.getFavData().split("/n");
		assertEquals(temp.length, 1);
		final String addFavName = "Best Buy";
		mockFavToAdd.setName(addFavName);
		final int addBusId = 0;
		mockFavToAdd.setBus_id(addBusId);
		final int addCatId = 0;
		mockFavToAdd.setCatId(addCatId);
		gloVa.setSelectedBus(mockFavToAdd);
		try{
		mockActMenu.runFav(table);		
		}catch(Exception e){
			temp = table.getFavData().split("/n");
		assertEquals(temp.length, 1);
		}
		
		//table.open();
		table.emptyFavs();
		table.close();
	}
}
