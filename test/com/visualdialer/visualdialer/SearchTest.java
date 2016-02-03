package com.visualdialer.visualdialer;

import android.app.Instrumentation;
import android.content.Intent;
import android.test.ActivityUnitTestCase;
import android.test.suitebuilder.annotation.SmallTest;

import com.visualdialer.visualdialer.Search;

public class SearchTest extends ActivityUnitTestCase<Search>{
	private Instrumentation instrumentation;
	private Search activity;
	
	public SearchTest() {
		super(Search.class);
	}
	
	@Override
	protected void setUp() throws Exception {
	    super.setUp();
	    instrumentation = getInstrumentation();
	    final Intent intent = new Intent(instrumentation.getTargetContext(), Search.class);
	    startActivity(intent, null, null);
	    activity = getActivity();
    }
	
	@SmallTest
	public void testSearch(){
		Category x = new Category(1,"Testing");
		DatabaseTables tables = new DatabaseTables(instrumentation.getTargetContext());
		tables.open();

		tables.createCataEntry(x.get_name(), x.get_cat_id());
		tables. close();
		activity.input.setText(x.get_name());
		activity.checkTable();
		String inTable = activity.results.getItemAtPosition(0).toString();
		assertEquals(x.get_name(), inTable);
	}
}
