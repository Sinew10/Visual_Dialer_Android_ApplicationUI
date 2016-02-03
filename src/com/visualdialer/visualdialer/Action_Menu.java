package com.visualdialer.visualdialer;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class Action_Menu extends Activity implements View.OnClickListener
{
	private final HttpUtils httpUtils = new HttpUtils();
	Button bConn;
	Button bShare;
	Button bFav;
	Button bDial;
	TextView TVAction;
	Intent dialIntent;
	Intent connectToRepIntent;
	String myNumber;
	DatabaseTables table = new DatabaseTables(this);
	int ID;
	String callNumber = "";
	private static final String POST_SELECTED_DATA_URL =  "http://198.71.205.11/visualdialer/VisualDialerAPI/api/UserSelections";
	private static final String DELETE_SELECTED_DATA_URL =  "http://198.71.205.11/visualdialer/VisualDialerAPI/api/UserSelections/";
	
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.action);
		initialize();
		ID = (new Random()).nextInt();
		myNumber = globVar.getMyNumber(this);
		postSelection();
	}
	
	private void postSelection() {
		
		if(globVar.getSelectedBus() == null){
			globVar.toast(this, "Not a Valid Selected Item");
		}else{
			try{
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("Id",ID +"" ));
				params.add(new BasicNameValuePair("BusinessId", globVar.getSelectedBus().getBus_id() + ""));
				if(globVar.getSelectedItem() == null)
					params.add(new BasicNameValuePair("PhoneMenuId", -1 + ""));
				else
					params.add(new BasicNameValuePair("PhoneMenuId", globVar.getSelectedItem().getMenu_id() + ""));
				params.add(new BasicNameValuePair("UserCallerID", globVar.getMyNumber(this)));
				String content = httpUtils.makeHttpRequest(POST_SELECTED_DATA_URL, HttpUtils.RequestMethod.POST, params);
				if(!content.equals("")){
					String temp = remove(content);
					String[] stuff = temp.split(":");
					callNumber = stuff[stuff.length - 1];
				}
			}catch(Exception e){
				globVar.toast(this, "Post didn't work");
			}			
		}		
	}

	private void initialize() 
	{
		TVAction = (TextView)findViewById(R.id.tv_action_menu);
		if(globVar.getSelectedBus() == null)
			TVAction.setText("Call Menu");
		else if(globVar.getSelectedItem() == null)
			TVAction.setText(globVar.getSelectedBus().get_name());
		else
			TVAction.setText(globVar.getSelectedBus().get_name() + "-" + globVar.getSelectedItem().get_name());
			
		bConn = (Button)findViewById(R.id.b_connect_rep);
		bConn.setOnClickListener(this);
		bShare = (Button)findViewById(R.id.b_share);
		bShare.setOnClickListener(this);
		bFav = (Button)findViewById(R.id.b_favorites);
		bFav.setOnClickListener(this);
		bDial = (Button)findViewById(R.id.b_dial_now);
		bDial.setOnClickListener(this);
		dialIntent = new Intent(Intent.ACTION_DIAL);;
		connectToRepIntent = new Intent(this, ConnectToRepActivity.class);		
	}
	protected void onPause() {
		super.onPause();
		try{
			httpUtils.makeHttpRequest(DELETE_SELECTED_DATA_URL + ID , HttpUtils.RequestMethod.DELETE, null);
		}catch(Exception e){
			globVar.toast(this, "Can't delete");
		}
	}

	@Override
	public void onClick(View v) 
	{
		// TODO Auto-generated method stub
		switch(v.getId())
		{
			case R.id.b_connect_rep:
				startActivity(connectToRepIntent);
				break;
			case R.id.b_share:
				//put test to check if at least a business is selected
				Intent s;
				s = new Intent("com.visualdialer.visualdialer.Share");
				startActivity(s);
				break;
			case R.id.b_favorites:				
				runFav();
				break;
			case R.id.b_dial_now:
				if(globVar.getSelectedBus() == null){
					globVar.toast(this, "Can't call that");
				}else{
					String posted_by;
					if(callNumber.equals(""))
						posted_by = "19492298540";
					else{
	
						posted_by = callNumber;
					}
					String uri = "tel:" + posted_by.trim();
					dialIntent.setData(Uri.parse(uri));
					startActivity(dialIntent);
				}
				break;			
		}		
	}

	public void onBackPressed() {
		globVar.decMenuPosition();
		super.onBackPressed();
	}
	public void runFav()
	{
		DatabaseTables table = new DatabaseTables(this);
		table.open();

		if(globVar.getSelectedBus() == null){
			globVar.toast(this.getApplicationContext(), "You Must First select a company.");
		}else if(globVar.getSelectedItem() == null){
			String FavData = table.getFavData();
			if(FavData.equals("")){
				table.createFavMenuEntry(globVar.getSelectedBus().get_name(),
							globVar.getSelectedBus().get_cat_id(),
							globVar.getSelectedBus().getBus_id(),
							-1,
							-1);
			}else{
				String[] FavElement = FavData.split("\n");
				boolean there = false;
				for(String x : FavElement){
					String[] element = x.split("  ");
					if((Integer.parseInt(element[2]) == globVar.getSelectedBus().getBus_id()) &&
							(Integer.parseInt(element[3]) == -1)){
						there = true;
					}
				}
				if(there){
					globVar.toast(this, "That is already in favorites");
				}else{
					table.createFavMenuEntry(globVar.getSelectedBus().get_name(),
							globVar.getSelectedBus().get_cat_id(),
							globVar.getSelectedBus().getBus_id(),
							-1,
							-1);
				}
			}
		}else{
			String FavData = table.getFavData();
			if(FavData.equals("")){
				table.createFavMenuEntry(globVar.getSelectedBus().get_name() + "-" + globVar.getSelectedItem().get_name() ,
						globVar.getSelectedBus().get_cat_id(),
						globVar.getSelectedBus().getBus_id(), 
						globVar.getSelectedItem().getMenu_id(), 
						globVar.getSelectedItem().getIsLeaf());
			
			}else{
				String[] FavElement = FavData.split("\n");
				boolean there = false;
				for(String x : FavElement){
					String[] element = x.split("  ");
					if(Integer.parseInt(element[3]) == globVar.getSelectedItem().getMenu_id()){
						there = true;
					}
				}
				if(there){
					globVar.toast(this, "That is already in favorites");
				}else{
					table.createFavMenuEntry(globVar.getSelectedBus().get_name() + "-" + globVar.getSelectedItem().get_name() ,
							globVar.getSelectedBus().get_cat_id(),
							globVar.getSelectedBus().getBus_id(),
							globVar.getSelectedItem().getMenu_id(), 
							globVar.getSelectedItem().getIsLeaf());
				}
			}
		}
		table.close();
	}
	
	public String remove(String x){
		
		String content = x.replace("{", "");
		content = content.replace("}", "");
		content = content.replaceAll("[\"]", "");
		
		return content;
	}
	
	public boolean onCreateOptionsMenu(android.view.Menu menu) {
		super.onCreateOptionsMenu(menu);
		MenuInflater x = getMenuInflater();
		x.inflate(R.menu.cool_menu, menu);
		return true;
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
	public void runFav(DatabaseTables table1)
	{
		
//		table1.open();
		if(globVar.getSelectedBus() == null){
			globVar.toast(this.getApplicationContext(), "You Must First select a company.");
		}else if(globVar.getSelectedItem() == null){
			String FavData = table1.getFavData();
			if(FavData.equals("")){
				table1.createFavMenuEntry(globVar.getSelectedBus().get_name(),
							globVar.getSelectedBus().get_cat_id(),
							globVar.getSelectedBus().getBus_id(),
							-1,
							-1);
			}else{
				String[] FavElement = FavData.split("\n");
				boolean there = false;
				for(String x : FavElement){
					String[] element = x.split("  ");
					if((Integer.parseInt(element[2]) == globVar.getSelectedBus().getBus_id()) &&
							(Integer.parseInt(element[3]) == -1)){
						there = true;
					}
				}
				if(there){
					globVar.toast(this, "That is already in favorites");
				}else{
					table1.createFavMenuEntry(globVar.getSelectedBus().get_name(),
							globVar.getSelectedBus().get_cat_id(),
							globVar.getSelectedBus().getBus_id(),
							-1,
							-1);
				}
			}
		}else{
			String FavData = table1.getFavData();
			if(FavData.equals("")){
				table1.createFavMenuEntry(globVar.getSelectedBus().get_name() + "-" + globVar.getSelectedItem().get_name() ,
						globVar.getSelectedBus().get_cat_id(),
						globVar.getSelectedBus().getBus_id(), 
						globVar.getSelectedItem().getMenu_id(), 
						globVar.getSelectedItem().getIsLeaf());
			
			}else{
				String[] FavElement = FavData.split("\n");
				boolean there = false;
				for(String x : FavElement){
					String[] element = x.split("  ");
					if(Integer.parseInt(element[3]) == globVar.getSelectedItem().getMenu_id()){
						there = true;
					}
				}
				if(there){
					globVar.toast(this, "That is already in favorites");
				}else{
					table1.createFavMenuEntry(globVar.getSelectedBus().get_name() + "-" + globVar.getSelectedItem().get_name() ,
							globVar.getSelectedBus().get_cat_id(),
							globVar.getSelectedBus().getBus_id(),
							globVar.getSelectedItem().getMenu_id(), 
							globVar.getSelectedItem().getIsLeaf());
				}
			}
		}
//		table1.close();
	}
}

