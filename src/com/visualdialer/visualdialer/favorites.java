package com.visualdialer.visualdialer;

import java.util.ArrayList;
import java.util.Vector;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class favorites extends Activity implements View.OnClickListener, OnItemClickListener
{
	//Vector<String> displayedNames = new Vector<String>();
	Vector<favItem> fav = new Vector<favItem>();
	favItem fav1[];
	ListView favList;
	ArrayList<String> displayFavs = new ArrayList<String>();
	ArrayAdapter<String> favsAdapter;
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.favorites);
		setUpFavs();
		getFavs();
	}
	public void setUpFavs()
	{						
			favList = (ListView) findViewById(R.id.lvFavList);
			favsAdapter = new ArrayAdapter<String>(this,
			        android.R.layout.simple_list_item_1, displayFavs);
			favList.setAdapter(favsAdapter);
			favList.setOnItemClickListener(this);
	}
	public void getFavs()
	{
		displayFavs.clear();
		DatabaseTables table = new DatabaseTables(this);
		table.open();
		String[] favTemp = table.getFavData().split("\n");		
		if(favTemp[0] == "")
		{
			displayFavs.add("You have no saved favorites.");
		}
		else
		{
			for(int i = 0 ; i < favTemp.length; i++)
			{
				String temp[] = favTemp[i].split("  ");
				fav.add(new favItem(temp[0], Integer.parseInt(temp[1]), Integer.parseInt(temp[2]), Integer.parseInt(temp[3]), Integer.parseInt(temp[4])));
				displayFavs.add(temp[0]);
			}
			favsAdapter = new ArrayAdapter<String>(this,
			        android.R.layout.simple_list_item_1, displayFavs);
			favList.setAdapter(favsAdapter);
		}
		table.close();
	}
	@Override
	
	public void onItemClick(AdapterView<?> arg0, View arg1, int place, long arg3) 
	{
		favItem temp = fav.get(place); 
		
		if(temp.getPhoneId()== -1){
			globVar.setSelectedBus(new Business(temp.getCatId(),temp.getBusId(),temp.getfavName()));
			globVar.setSelectedItem(null);
		}else{
			String[] names = temp.getfavName().split("-");
			globVar.setSelectedBus(new Business(temp.getCatId(),temp.getBusId(),names[0]));
			globVar.setSelectedItem(new Phone(temp.getBusId(),temp.getPhoneId(),temp.getParId(),names[1],temp.getIsLeaf()));
		}
		//globVar.setSelectedItem(pho);
		Intent openActionMenu = new Intent("com.visualdialer.visualdialer.ActionMenu");
		startActivity(openActionMenu);
		
	}
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
	}
	public boolean onCreateOptionsMenu(android.view.Menu menu) {
		super.onCreateOptionsMenu(menu);
		MenuInflater x = getMenuInflater();
		x.inflate(R.menu.main_screen, menu);
		return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
		DatabaseTables table = new DatabaseTables(this);
		switch(item.getItemId()){
			case R.id.clearItems:
				
				table.open();
				table.emptyFavs();
				table.close();
				getFavs();
				favsAdapter = new ArrayAdapter<String>(this,
				        android.R.layout.simple_list_item_1, displayFavs);
				favList.setAdapter(favsAdapter);
				break;
		}
		return false;
	}
}