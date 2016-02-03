package com.visualdialer.visualdialer;


import java.util.Vector;

import android.app.Instrumentation;
import android.content.Intent;
import android.test.ActivityUnitTestCase;
import android.test.suitebuilder.annotation.SmallTest;

import com.visualdialer.visualdialer.Business;
import com.visualdialer.visualdialer.Category;
import com.visualdialer.visualdialer.DatabaseTables;
import com.visualdialer.visualdialer.MenuController;
import com.visualdialer.visualdialer.Phone;
import com.visualdialer.visualdialer.globVar;

public class MenuControllerTest extends ActivityUnitTestCase<MenuController>{
	private Instrumentation instrumentation;
	private MenuController activity;
	Vector<Category> cataTemp = new Vector<Category>();
	Vector<Business> busTemp = new Vector<Business>();
	Vector<Phone> phoneTemp = new Vector<Phone>();
	Vector<Phone> innerPhoneTemp = new Vector<Phone>();
	
	public MenuControllerTest() {
		super(MenuController.class);
	}
	
	@Override
	protected void setUp() throws Exception {
	    super.setUp();
	    instrumentation = getInstrumentation();
	    final Intent intent = new Intent(instrumentation.getTargetContext(), MenuController.class);
	    startActivity(intent, null, null);
	    activity = getActivity();
    }
	
	@SmallTest
	public void testSetUpCatagories(){
		cataTemp.add(new Category(1,"Test Category"));
		DatabaseTables tables = new DatabaseTables(instrumentation.getTargetContext());
		tables.open();

		tables.createCataEntry(cataTemp.elementAt(0).get_name(), cataTemp.elementAt(0).get_cat_id());
		tables. close();
		
		activity.setUpCatagories();
		assertEquals(cataTemp.size(),globVar.getCategories().size());
		for(Category i: globVar.getCategories())
			assertEquals(globVar.getCategories().contains(i),cataTemp.contains(i));
		for(Category t : cataTemp)
			assertEquals(cataTemp.contains(t),globVar.getCategories().contains(t));
	}
	
	
	@SmallTest
	public void testSetUpBusinesses(){
		busTemp.add(new Business(1,1,"Test Business"));
		globVar.clearBusinesses();
		DatabaseTables t = new DatabaseTables(activity);
		t.open();

		t.createBusEntry(busTemp.elementAt(0).get_name(), busTemp.elementAt(0).getBus_id(), busTemp.elementAt(0).get_cat_id());
		t.close();
		
		for(Category i : cataTemp){
			activity.setUpBusiness(i.get_cat_id());
			assertEquals(busTemp,globVar.getBusinesses());
		}
	}
	
	@SmallTest
	public void testSetUpPhoneMenu(){
		phoneTemp.add(new Phone(1,1,0,"Test Phone Menu",0));
		globVar.clearPhoneMenu();
		DatabaseTables t = new DatabaseTables(activity);
		t.open();

		t.createPhoneMenuEntry(phoneTemp.elementAt(0).get_name(), phoneTemp.elementAt(0).getMenu_id(), phoneTemp.elementAt(0).getParent_id(), phoneTemp.elementAt(0).getBus_id(), phoneTemp.elementAt(0).getIsLeaf());
		t.close();
		
		for(Business i : busTemp){
			activity.setUpPhoneMenu(i.get_cat_id());
			assertEquals(phoneTemp,globVar.getPhoneMenu());
		}
	}
	
	@SmallTest
	public void testSetUpInnerPhoneMenu(){
		innerPhoneTemp.add(new Phone(1,2,1,"Test Phone Menu",1));
		globVar.clearPhoneMenu();
		DatabaseTables t = new DatabaseTables(activity);
		t.open();

		t.createPhoneMenuEntry(innerPhoneTemp.elementAt(0).get_name(), 
				innerPhoneTemp.elementAt(0).getMenu_id(), 
				innerPhoneTemp.elementAt(0).getParent_id(), 
				innerPhoneTemp.elementAt(0).getBus_id(), 
				innerPhoneTemp.elementAt(0).getIsLeaf());
		t.close();
		
		for(Phone i : phoneTemp){
			activity.setUpInnerPhoneMenu(i.get_cat_id());
			assertEquals(innerPhoneTemp,globVar.getPhoneMenu());
		}
	}
	@SmallTest
	public void testOnItemClicked(){
		DatabaseTables table = new DatabaseTables(activity);
		
		cataTemp.add(new Category(1,"Test Category"));
		busTemp.add(new Business(1,1,"Test Business"));
		phoneTemp.add(new Phone(1,1,0,"Test Phone Menu",0));
		innerPhoneTemp.add(new Phone(1,2,1,"Test Phone Menu",1));
		
		
		table.open();
		table.emptyDatabase();
		table.createCataEntry(cataTemp.elementAt(0).get_name(), cataTemp.elementAt(0).get_cat_id());
		table.createBusEntry(busTemp.elementAt(0).get_name(), busTemp.elementAt(0).getBus_id(), busTemp.elementAt(0).get_cat_id());
		table.createPhoneMenuEntry(phoneTemp.elementAt(0).get_name(), phoneTemp.elementAt(0).getMenu_id(), phoneTemp.elementAt(0).getParent_id(), phoneTemp.elementAt(0).getBus_id(), phoneTemp.elementAt(0).getIsLeaf());
		table.createPhoneMenuEntry(innerPhoneTemp.elementAt(0).get_name(), 
				innerPhoneTemp.elementAt(0).getMenu_id(), 
				innerPhoneTemp.elementAt(0).getParent_id(), 
				innerPhoneTemp.elementAt(0).getBus_id(), 
				innerPhoneTemp.elementAt(0).getIsLeaf());
		table.close();
		
		globVar.addCategories(new Category(1,"Test Category"));
		
		globVar.setMenuPosition(0);
		activity.onItemClick(null, null, 0, 1);
		assertEquals(busTemp,globVar.getBusinesses());
		
		//menu position going from 1 to 2 - business to phone
		globVar.setMenuPosition(1);
		activity.onItemClick(null, null, 0, 1);
		assertEquals(phoneTemp,globVar.getPhoneMenu());
		//Business has been selected
		assertEquals(globVar.getSelectedBus(),busTemp.elementAt(0));
		
		
		//menu position going from 2 to 3 - phone to innerPhone
		globVar.setMenuPosition(2);
		activity.onItemClick(null, null, 0, 1);
		assertEquals(innerPhoneTemp,globVar.getPhoneMenu());
		//Phone Menu Item has been selected
		assertEquals(globVar.getSelectedItem(),phoneTemp.elementAt(0));
		cataTemp.clear();
		busTemp.clear();
		phoneTemp.clear();
		innerPhoneTemp.clear();
		table.open();
		table.emptyDatabase();
		table.close();
	}
}
