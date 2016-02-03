package com.visualdialer.visualdialer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.ListView;

public class MenuController extends Activity implements OnItemClickListener{ //extends ListActivity{
	ListView results;
	ArrayAdapter<String> adapter;
	//TextView label;
	ImageView image;

	@Override
	public boolean onMenuOpened(int featureId, Menu menu) {
		return super.onMenuOpened(featureId, menu);
	}


	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_menu);
		//this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		results = (ListView) findViewById(R.id.lvMainMenu);
		//label = (TextView) findViewById(R.id.tv_action_menu);
		results.setOnItemClickListener(this);
		if(globVar.getCategories().isEmpty())
			setUpCatagories();
		int width = getWindowManager().getDefaultDisplay().getWidth();
		image = (ImageView) findViewById(R.id.imLogoTopView);
		image.setImageResource(R.drawable.visualdialerlogo);
		image.setMinimumWidth(width);
		image.setMaxWidth(width);
		image.setScaleType(ScaleType.CENTER_INSIDE);
		adapter = new ArrayAdapter<String>(this,
		        android.R.layout.simple_list_item_1, globVar.getDisplayedNames());
		results.setAdapter(adapter);	
	}		
	
	public void setUpCatagories(){
		globVar.clearDispalyedNames();
		globVar.clearCategories();
		DatabaseTables table = new DatabaseTables(this);
		table.open();
		String info[] = table.getCataData().split("\n");
		table.close();
		//cats = new Category[info.length];
		for(int i = 0 ; i < info.length; i++)
		{
			String temp[] = info[i].split("  ");
			if(!temp[0].equals(""))
			{
				globVar.addCategories(new Category(Integer.parseInt(temp[0]), temp[1]));
			}			
		}
		
		for(int i = 0; i < globVar.getCategories().size(); i++){
			globVar.addDisplayName(globVar.getCategories().elementAt(i).get_name());
		}
	}
	
	public void setUpBusiness( int p){

		DatabaseTables table = new DatabaseTables(this);
		table.open();
		String info[] = table.getBusinessData(p).split("\n");
		table.close();
		if(info[0].equals("")){
			globVar.toast(this.getApplicationContext(), "Invalid call");
			globVar.decMenuPosition();
			//globVar.toast(this.getApplicationContext(), globVar.getMenuPosition() + "");
		}else{
			globVar.clearBusinesses();
			for(int i = 0 ; i < info.length; i++){
				String temp[] = info[i].split("  ");
				globVar.addBusinesses(new Business(Integer.parseInt(temp[0]),Integer.parseInt(temp[1]),temp[2]));
			}
			
			globVar.clearDispalyedNames();
			for(int i = 0; i < globVar.getBusinesses().size(); i++){
				globVar.addDisplayName(globVar.getBusinesses().elementAt(i).get_name());
			}
		}
		adapter = new ArrayAdapter<String>(this,
		        android.R.layout.simple_list_item_1, globVar.getDisplayedNames());
		results.setAdapter(adapter);
	}
	
	public void setUpPhoneMenu(int p){
		DatabaseTables table = new DatabaseTables(this);
		table.open();
		String info[] = table.getPhoneMenuData(p).split("\n");
		table.close();
		if(info[0].equals("")){
			globVar.toast(this.getApplicationContext(), "Invalid call");
			globVar.decMenuPosition();
			//globVar.toast(this.getApplicationContext(), globVar.getMenuPosition() + "");
		}else{
			if(!globVar.getPhoneMenu().isEmpty())
				globVar.clearPhoneMenu();
			for(int i = 0 ; i < info.length; i++){
				String temp[] = info[i].split("  ");
				globVar.addPhoneElement(new Phone(Integer.parseInt(temp[2]), Integer.parseInt(temp[3]),
									Integer.parseInt(temp[1]),temp[4], Integer.parseInt(temp[0])));			
			}
			
			globVar.clearDispalyedNames();
			for(int i = 0; i < globVar.getPhoneMenu().size(); i++){
				globVar.addDisplayName(globVar.getPhoneMenu().elementAt(i).get_name());
			}
		}
		adapter = new ArrayAdapter<String>(this,
		        android.R.layout.simple_list_item_1, globVar.getDisplayedNames());
		results.setAdapter(adapter);
	}
	
	public void setUpInnerPhoneMenu(int p){
		DatabaseTables table = new DatabaseTables(this);
		table.open();
		String info[] = table.getPhoneMenuInnerData(p).split("\n");
		table.close();
		if(info[0].equals("")){	
			globVar.toast(this.getApplicationContext(), "Invalid call");
			globVar.decMenuPosition();
			//globVar.toast(this.getApplicationContext(), globVar.getMenuPosition() + "");
		}else{
			globVar.clearPhoneMenu();
			for(int i = 0 ; i < info.length; i++){
				String temp[] = info[i].split("  ");
				globVar.addPhoneElement(new Phone(Integer.parseInt(temp[2]), Integer.parseInt(temp[3]),
						Integer.parseInt(temp[1]),temp[4], Integer.parseInt(temp[0])));	
				
			}
			
			globVar.clearDispalyedNames();
			for(int i = 0; i < globVar.getPhoneMenu().size(); i++){
				globVar.addDisplayName(globVar.getPhoneMenu().elementAt(i).get_name());
			}
		}
		
		adapter = new ArrayAdapter<String>(this,
		        android.R.layout.simple_list_item_1, globVar.getDisplayedNames());
		results.setAdapter(adapter);
	}

	public boolean onCreateOptionsMenu(android.view.Menu menu) {
		super.onCreateOptionsMenu(menu);
		MenuInflater x = getMenuInflater();
		x.inflate(R.menu.cool_menu, menu);
		return true;
	}
	
	protected void onResume() {
		super.onResume();
		adapter = new ArrayAdapter<String>(this,
		        android.R.layout.simple_list_item_1, globVar.getDisplayedNames());
		results.setAdapter(adapter);
	}

	public void onBackPressed() {
		//
		if(globVar.getMenuPosition() == 0){
			super.onBackPressed();
		//we are at the root/ categories menu
		}else if(globVar.getMenuPosition() == 1 || globVar.getMenuPosition() < 0){
			globVar.setSelectedBus(null);
			globVar.setSelectedItem(null);
			//we are at th business menu
			//change to categories menu
			globVar.clearDispalyedNames();
			for(int i = 0; i < globVar.getCategories().size(); i++){
				globVar.addDisplayName(globVar.getCategories().elementAt(i).get_name());
			}
			if(globVar.getMenuPosition() < 0)
				globVar.setMenuPosition(1);
			adapter = new ArrayAdapter<String>(this,
			        android.R.layout.simple_list_item_1, globVar.getDisplayedNames());
			results.setAdapter(adapter);
		}else if(globVar.getMenuPosition() == 2){
			globVar.setSelectedItem(null);
			globVar.setSelectedBus(null);
			//we are at the first phone menu 
			//change to business menu
			globVar.clearDispalyedNames();
			for(int i = 0; i < globVar.getBusinesses().size(); i++){
				globVar.addDisplayName(globVar.getBusinesses().elementAt(i).get_name());
			}
			adapter = new ArrayAdapter<String>(this,
			        android.R.layout.simple_list_item_1, globVar.getDisplayedNames());
			results.setAdapter(adapter);
		}else{
			globVar.setFavSelectedItem(null);
			int parentNodeId;
			DatabaseTables table = new DatabaseTables(this);
			table.open();
			int nodeB;
			if(globVar.getMenuPosition() ==3 ){
				
				parentNodeId = globVar.getPhoneMenu().elementAt(0).getBus_id();
				nodeB = table.getPhoneMenuBusId(1);//parentNodeId);
				String info[] = table.getPhoneMenuData(nodeB).split("\n");
				globVar.clearPhoneMenu();
				if(info.length <= 1){
					globVar.clearDispalyedNames();
					globVar.addDisplayName("Wrong");
				}else{
					for(int i = 0 ; i < info.length; i++){
						String temp[] = info[i].split("  ");
						globVar.addPhoneElement(new Phone(Integer.parseInt(temp[2]), Integer.parseInt(temp[3]),
								Integer.parseInt(temp[1]),temp[4], Integer.parseInt(temp[0])));	
						
					}
					globVar.clearDispalyedNames();
					for(int i = 0; i < globVar.getPhoneMenu().size(); i++){
						globVar.addDisplayName(globVar.getPhoneMenu().elementAt(i).get_name());
					}
				}
				adapter = new ArrayAdapter<String>(this,
				        android.R.layout.simple_list_item_1, globVar.getDisplayedNames());
				results.setAdapter(adapter);
			}else{
				parentNodeId = globVar.getPhoneMenu().elementAt(0).getParent_id();
			
				nodeB = table.getPhoneMenuParentId(parentNodeId);
				String info[] = table.getPhoneMenuInnerData(nodeB).split("\n");
				globVar.clearPhoneMenu();
				for(int i = 0 ; i < info.length; i++){
					String temp[] = info[i].split("  ");
					globVar.addPhoneElement(new Phone(Integer.parseInt(temp[2]), Integer.parseInt(temp[3]),
							Integer.parseInt(temp[1]),temp[4], Integer.parseInt(temp[0])));
				}
				globVar.clearDispalyedNames();
				for(int i = 0; i < globVar.getPhoneMenu().size(); i++){
					globVar.addDisplayName(globVar.getPhoneMenu().elementAt(i).get_name());
				}
				adapter = new ArrayAdapter<String>(this,
				        android.R.layout.simple_list_item_1, globVar.getDisplayedNames());
				results.setAdapter(adapter);
			}
			table.close();
			//we are at an inner phone menu
			//change the one step up
			//look at the parent id of the current phone menu item A
			//find the node B with the id equal to that id of A
			//get a list of nodes whose parent  id is B's id
		}
		globVar.decMenuPosition();
		//globVar.toast(this.getApplicationContext(), globVar.getMenuPosition() + "");
	}


	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent = null;
		switch(item.getItemId()){
			case R.id.CallMenu:
				globVar.incMenuPosition();
				intent = new Intent(this, Action_Menu.class);
				break;
			case R.id.fav:
				intent = new Intent(this, favorites.class);
				break;
			case R.id.search:
				intent = new Intent(this, Search.class);
				break;
			case R.id.preferences:
				intent = new Intent(this, Preferences.class);
				break;
			case R.id.connectToRep:
				intent = new Intent(this, ConnectToRepActivity.class);
				break;
		}
		if (intent != null) {
			startActivity(intent);
		}
		return false;
	}


	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		globVar.setPosition(position);
		
		switch(globVar.getMenuPosition()){
			case 0:
				globVar.setSelectedItem(null);
				globVar.setSelectedBus(null);
				setUpBusiness(globVar.getCategories().elementAt(position).get_cat_id());
				break;
			case 1:
				globVar.setSelectedItem(null);
				globVar.setSelectedBus(globVar.getBusinesses().elementAt(position));
				setUpPhoneMenu(globVar.getBusinesses().elementAt(position).getBus_id());
				break;
			default:
				if(globVar.getPhoneMenu().get(position).getIsLeaf() == 1){
					globVar.setFavSelectedItem(globVar.getPhoneMenu().get(position));
					globVar.setSelectedItem(globVar.getPhoneMenu().get(position));
					Intent openActionMenu = new Intent("com.visualdialer.visualdialer.ActionMenu");
					startActivity(openActionMenu);
				}
				//if the current select .getLeaf is 1 update call number
					//Update next page
				else{
					globVar.setFavSelectedItem(globVar.getPhoneMenu().get(position));
					setUpInnerPhoneMenu(globVar.getPhoneMenu().get(position).getMenu_id());
					//changed for test
					//globVar.setSelectedItem(globVar.getPhoneMenu().get(position));
				}
				//else 
					//get the index of the PhoneMenu from the position in the array
					//use the index as the parent p for setUpInnerPhoneMenu
				break;
		}
		globVar.incMenuPosition();
		//globVar.toast(this.getApplicationContext(), globVar.getMenuPosition() + "");
	}
	
}
