package com.visualdialer.visualdialer;

import java.util.ArrayList;
import java.util.Vector;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class Search extends Activity implements View.OnClickListener, OnItemClickListener{
	Button search;
	EditText input;
	ListView results;
	ArrayList<String> display = new ArrayList<String>();
	ArrayAdapter<String> adapter;
	boolean continueOn = false;
	Vector<Category> found = new Vector<Category>();
	Vector<Integer> positions = new Vector<Integer>();
	
	
	@Override
	protected void onPause() {
		super.onPause();
		finish();
	}

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search);
		init();
	}

	private void init() {
		search = (Button) findViewById(R.id.bSearch);
		input = (EditText) findViewById(R.id.etSearchInput);
		search.setOnClickListener(this);
		results = (ListView) findViewById(R.id.lvResults);
		adapter = new ArrayAdapter<String>(this,
		        android.R.layout.simple_list_item_1, display);
		results.setAdapter(adapter);
		results.setOnItemClickListener(this);

	}
	
	public void onClick(View v) 
	{
		switch(v.getId()){
			case R.id.bSearch:
				display.clear();
				found.clear();
				positions.clear();
				checkTable();
				break;
		}
	}

	void checkTable() {
		String looking = input.getText().toString().trim();
		
		if(looking.equals("")){
			display.add("Nothing in text box.");
		}
		else{
			String[] words = looking.split(" ");
			DatabaseTables table = new DatabaseTables(this);
			table.open();
			
			for(int i =0; i < words.length; i++){
				boolean looping = true;
				int position = 0;
				String temp;
				while(looping){
					if(position == 0){
						temp = table.getCataContains(words[i]);
						if(temp != ""){
							String[] list = temp.split("\n");
							for(int t = 0; t < list.length; t++){
								String[] elements = list[t].split("  ");
								display.add(elements[1]);
								found.add(new Category(Integer.parseInt(elements[0]),elements[1]));
								positions.add(position);
							}
						}
					}else if(position == 1){
						temp = table.getBusinessContains(words[i]);
						if(temp != ""){
							String[] list = temp.split("\n");
							for(int t = 0; t < list.length; t++){
								String[] elements = list[t].split("  ");
								display.add(elements[2]);
								found.add(new Business(Integer.parseInt(elements[0]), Integer.parseInt(elements[1]),elements[2]));
								positions.add(position);
							}
						}
					}else if(position == 2){
						temp = table.getPhoneMenuContains(words[i]);
						if(temp != ""){
							String[] list = temp.split("\n");
							for(int t = 0; t < list.length; t++){
								String[] elements = list[t].split("  ");
								display.add(elements[4]);
								found.add(new Phone(Integer.parseInt(elements[1]), Integer.parseInt(elements[3]), 
										Integer.parseInt(elements[2]), elements[4], Integer.parseInt(elements[0])));
								positions.add(position);
							}
						}
						looping = false;
					}
					
					
					position++;
				}
			}
			if(display.isEmpty()){
				Context context = getApplicationContext();
				CharSequence text = "NOTHING FOUND";
				int duration = Toast.LENGTH_SHORT;
				Toast toast = Toast.makeText(context, text, duration);
				toast.show();
				continueOn = false;
			}else
				continueOn = true;
			
			
			table.close();
		}
		adapter = new ArrayAdapter<String>(this,
		        android.R.layout.simple_list_item_1, display);
		results.setAdapter(adapter);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		if(continueOn){
			globVar.clearDispalyedNames();
			switch(positions.elementAt(position)){
				
				case 0:
					globVar.setMenuPosition(1);
					globVar.clearDispalyedNames();
					globVar.clearBusinesses();
					
					//set up the business menu with found.elementAt(position)

					DatabaseTables tablePosition1 = new DatabaseTables(this);
					tablePosition1.open();
					String businessMenuPos1 = tablePosition1.getBusinessData(found.elementAt(position).get_cat_id());
					tablePosition1.close();
					
					if(businessMenuPos1 == ""){
						display.add("Error position 1");
					}else{
						String[] busin = businessMenuPos1.split("\n");
						
						for(String i: busin){
							String[] BusEle = i.split("  ");
							globVar.addBusinesses(new Business(Integer.parseInt(BusEle[0]),Integer.parseInt(BusEle[1]),BusEle[2]));
							globVar.addDisplayName(BusEle[2]);
						}
						
						Intent i = new Intent("com.visualdialer.visualdialer.MENU");
						startActivity(i);
					}
					break;
				case 1:
					globVar.setSelectedItem(null);
					globVar.setSelectedBus((Business)found.elementAt(position));
					//set up the phone menu with found.elelemtAt(position)
					//set up the business menu by getting the categoryId from found.elementAt(position).getBus_id()
					DatabaseTables tab = new DatabaseTables(this);
					tab.open();
					String businessMenu = tab.getBusinessData(((Business) found.elementAt(position)).get_cat_id());
					String phoneMenu = tab.getPhoneMenuData(((Business) found.elementAt(position)).getBus_id());
					tab.close();
					globVar.clearBusinesses();
					globVar.clearDispalyedNames();
					globVar.clearPhoneMenu();
					globVar.setMenuPosition(2);
					if(businessMenu == ""){
						display.add("Business: " + businessMenu);
						display.add("Phone: " + phoneMenu);
					}else if(phoneMenu == ""){
						Intent i = new Intent("com.visualdialer.visualdialer.ActionMenu");
						startActivity(i);
					}
					else{
						String[] busin = businessMenu.split("\n");
						for(String i: busin){
							String[] BusEle = i.split("  ");
							globVar.addBusinesses(new Business(Integer.parseInt(BusEle[0]),Integer.parseInt(BusEle[1]),BusEle[2]));
						}	
						
						String[] phone = phoneMenu.split("\n");
						for(String i: phone){
							String[] phoneEle = i.split("  ");
							globVar.addPhoneElement(new Phone( Integer.parseInt(phoneEle[1]), Integer.parseInt(phoneEle[3]),Integer.parseInt(phoneEle[2]) ,
									phoneEle[4], Integer.parseInt(phoneEle[0])));
							globVar.addDisplayName(phoneEle[4]);
						}
						
						Intent i = new Intent("com.visualdialer.visualdialer.MENU");
						startActivity(i);
					}
					
					break;
				case 2:
					globVar.setSelectedItem((Phone) found.elementAt(position));
					if(((Phone) found.elementAt(position)).getIsLeaf() == 1){
						DatabaseTables table = new DatabaseTables(this);
						table.open();
						
						int businessParentID = table.getPhoneMenuBusId(((Phone) found.elementAt(position)).getMenu_id());
						int catagoryID = table.getBusParentId(businessParentID);
						String[] business = table.getBusinessData(catagoryID).split("\n");
						String phones = table.getPhoneMenuData(businessParentID);
						
						table.close();
						globVar.clearPhoneMenu();
						globVar.setMenuPosition(3);
						
						if(phones.equals("")){
							display.add("error position 3");
						}else{
							String[] p = phones.split("\n");
							
							
							globVar.clearBusinesses();
							for(String t: business){
								String[] elements = t.split("  ");
								if(Integer.parseInt(elements[1]) ==((Phone) found.elementAt(position)).getBus_id())
									globVar.setSelectedBus(new Business(Integer.parseInt(elements[0]),Integer.parseInt(elements[1]),elements[2]));
								globVar.addBusinesses(new Business(Integer.parseInt(elements[0]),Integer.parseInt(elements[1]),elements[2]));
							}
							
							globVar.clearDispalyedNames();
							for(int i = 0; i < p.length; i++){
								String[] data = p[i].split("  ");
								globVar.addPhoneElement(new Phone(Integer.parseInt(data[1]), Integer.parseInt(data[3]), 
										Integer.parseInt(data[2]), data[4], Integer.parseInt(data[0])));
								globVar.addDisplayName(data[4]);
							}
						}
					
						
						
						globVar.setSelectedItem((Phone) found.elementAt(position));
						Intent i = new Intent("com.visualdialer.visualdialer.ActionMenu");
						startActivity(i);
					}else{
						DatabaseTables table = new DatabaseTables(this);
						table.open();
						String children = table.getPhoneMenuInnerData(((Phone) found.elementAt(position)).getMenu_id());
						
						
						int businessParentID = table.getPhoneMenuBusId(((Phone) found.elementAt(position)).getMenu_id());
						int catagoryID = table.getBusParentId(businessParentID);
						String[] business = table.getBusinessData(catagoryID).split("\n");
						table.close();
						globVar.clearPhoneMenu();
						globVar.setMenuPosition(3);
						
						if(children.equals("")){
							display.add("error position 3");
						}else{
							String[] child = children.split("\n");
							
							
							globVar.clearBusinesses();
							for(String t: business){
								String[] elements = t.split("  ");
								if(Integer.parseInt(elements[1]) ==((Phone) found.elementAt(position)).getBus_id())
									globVar.setSelectedBus(new Business(Integer.parseInt(elements[0]),Integer.parseInt(elements[1]),elements[2]));
								
								globVar.addBusinesses(new Business(Integer.parseInt(elements[0]),Integer.parseInt(elements[1]),elements[2]));
							}
							
							globVar.clearDispalyedNames();
							for(int i = 0; i < child.length; i++){
								String[] data = child[i].split("  ");
								globVar.addPhoneElement(new Phone(Integer.parseInt(data[1]), Integer.parseInt(data[3]), 
										Integer.parseInt(data[2]), data[4], Integer.parseInt(data[0])));
								globVar.addDisplayName(data[4]);
							}
							
							Intent i = new Intent("com.visualdialer.visualdialer.MENU");
							startActivity(i);
						}
					}
					break;
			}
			adapter = new ArrayAdapter<String>(this,
			        android.R.layout.simple_list_item_1, display);
			results.setAdapter(adapter);
		}		
	}

	public void onBackPressed() {
		super.onBackPressed();
		finish();
	}
	
}
