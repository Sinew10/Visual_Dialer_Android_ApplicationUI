package com.visualdialer.visualdialer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;


public class MainScreen  extends Activity{
	private final HttpUtils httpUtils = new HttpUtils();
	TextView testing;
	private static final String GET_DATA_URL =  "http://198.71.205.11/visualdialer/VisualDialerAPI/";
	private static final String CATAGORIES_URL = "api/Categories";
	private static final String BUSINESS_URL ="api/Businesses";
	private static final String PHONE_MENU_URL = "api/PhoneMenus";
	private static final String GET_VERSION_URL = "api/Status";

	private static boolean finished;

	String status = "";
	String versionNum;
	
		protected void onCreate(Bundle instState) {
			super.onCreate(instState);
			setContentView(R.layout.activity_main_screen);
			finished = false;
			globVar.setMenuPosition(0);
			finished = false;
			ImageView image = (ImageView) findViewById(R.id.logo_ver1);
			image.setImageResource(R.drawable.visualdialerlogo);
			
			Thread setUp = new Thread(){
				public void run(){
					try{
						settingUp();
						while(!finished){}
						sleep(1000);
					}catch(Exception e){
						e.printStackTrace();
					}finally{
						Intent openStartPoint = new Intent("com.visualdialer.visualdialer.MENU");
						startActivity(openStartPoint);
					}
				}
			};
			setUp.start();
		}

		protected void onPause() {
			super.onPause();
			finish();//this makes it so when you hit the back button this activity no longer works
		}
		

		public void settingUp(){
			DatabaseTables table = new DatabaseTables(this);
			table.open();
			//table.
			
			try{
				try{
					status = httpUtils.makeHttpRequest(GET_DATA_URL + GET_VERSION_URL, HttpUtils.RequestMethod.GET, null);
					String[] x = status.split(",");
					String[] num = x[2].split(":");
					versionNum = remove(num[1]);
					//.replace("\"", "");
					//globVar.toast(MainScreen.this, versionNum);
					String version = table.getVersion();
					
					if(version.equals("")){
						table.emptyDatabase();
						serverSetup();
						table.createVersionEntry(versionNum);
					}
					else if(!versionNum.equals(version)){
						table.emptyDatabase();
						serverSetup();
						table.updateVersion(versionNum);
					}
					
				}catch(Exception e){
					//if we can't check the version number then don't delete/update the internal database
				}
				
				
			}catch(Exception e){
				table.createCataEntry("Computers/Electronics", 1);
				table.createCataEntry("Phone/Internet", 2);
				table.createCataEntry("Banking", 3);
				table.createCataEntry("Credit Card", 4);
				
				table.createBusEntry("Best Buy", 1, 1);
				table.createBusEntry("Fry's Electronics", 2, 1);
				table.createBusEntry("Regal Theaters", 3, 1);
				
				table.createBusEntry("Comcast", 4, 2);
				table.createBusEntry("Verizon", 5, 2);
				table.createBusEntry("Charter", 6, 2);
				table.createBusEntry("8x8", 7, 2);
				table.createBusEntry("Dish Network", 8, 2);
				table.createBusEntry("Direct TV", 9, 2);
				table.createBusEntry("AT&T", 10, 2);
				
				table.createBusEntry("Wells Fargo", 11, 3);
				table.createBusEntry("Bank of America", 12, 3);
				table.createBusEntry("Rabobank", 13, 3);
				table.createBusEntry("Chase", 14, 3);
				table.createBusEntry("Citibank", 15, 3);
				
				table.createBusEntry("Citi", 16, 4);
				table.createBusEntry("BankAmericard", 17, 4);
				table.createBusEntry("Capital One", 18, 4);
				table.createBusEntry("Discover it", 19, 4);
				table.createBusEntry("Chase", 20, 4);
				
				table.createPhoneMenuEntry("TV and Home Theater", 1, 0, 1, 0);//
				table.createPhoneMenuEntry("TV", 6, 1, 1, 1);
				table.createPhoneMenuEntry("Home Threater", 7, 1, 1, 1);
				table.createPhoneMenuEntry("Audio and MP3", 2, 0, 1, 1);
				table.createPhoneMenuEntry("Mobile Phones and iPhones", 3,0, 1, 1);
				table.createPhoneMenuEntry("Cameras and Camcorders", 4, 0, 1, 1);
				table.createPhoneMenuEntry("Computer and Tablets", 5,0, 1, 1);
				finished = true;
			}
			table.close();
			finished = true;
		}



		private void serverSetup() {
			//final List<NameValuePair> params = new ArrayList<NameValuePair>();
			DatabaseTables table = new DatabaseTables(this);
			table.open();
			String content = httpUtils.makeHttpRequest(GET_DATA_URL + CATAGORIES_URL, HttpUtils.RequestMethod.GET, null);
			int id = 0;
			String name = "";
			if(content == null){
				globVar.addDisplayName("Error1");
			}else{
				content = remove(content);

				String[] categories = content.split(",");
				
				for(int i = 0 ; i < categories.length; i++){
					String[] elements = categories[i].split(":");
					
					if(i %3 == 1){
						id = Integer.parseInt(elements[1]);
					}else if(i % 3 ==2){
						name =elements[1];
						if(!name.equals("") && id != 0)
							table.createCataEntry(name,id);
						id = 0;
						name = "";
					}
				
				}
			}
			
			content = httpUtils.makeHttpRequest(GET_DATA_URL + BUSINESS_URL, HttpUtils.RequestMethod.GET, null);
//globVar.addDisplayName(content);
			
			if(content == null){
				globVar.addDisplayName("Error2");
			}else{
				String[] stuff = content.split("\\}\\},");
				for(int o = 0 ; o<stuff.length; o++){
					stuff[o] = remove(stuff[o]);

					String[] categories = stuff[o].split(",");
					int CategoryId =0;
					for(int i = 0 ; i < categories.length; i++){
						String[] elements = categories[i].split(":");
						
						if(i == 1){
							id = Integer.parseInt(elements[1]);
						}else if(i ==2){
							CategoryId = Integer.parseInt(elements[1]);
						}else if(i == 3){
							name =elements[1];
							if(!name.equals("") && id != 0)
								table.createBusEntry(name,id,CategoryId);
							id = 0;
							name = "";
						}
					}
				}
				
			}
			
			

			content = httpUtils.makeHttpRequest(GET_DATA_URL + PHONE_MENU_URL, HttpUtils.RequestMethod.GET, null);
			if(content == null){
				globVar.addDisplayName("Error3m,");
			}else{
				content = remove(content);

				String[] categories = content.split(",");
				int BusId =0;
				int isLeaf = 0;
				int parentId = 0;
				for(int i = 0 ; i < categories.length; i++){
					String[] elements = categories[i].split(":");
					
					if(i % 9 == 1){//menuId
						id = Integer.parseInt(elements[1]);
					}else if(i % 9 ==2){//ParentId
						parentId =  Integer.parseInt(elements[1]);
					}else if(i % 9 == 3){//BusinessId
						BusId = Integer.parseInt(elements[1]);
					}else if(i % 9 == 4){//name
						name =elements[1];
					}else if(i % 9 == 5){//is leaf
						if(elements[1].equals("true"))
							isLeaf = 1;
						else
							isLeaf = 0;
						if(!name.equals("") && id != 0)
							table.createPhoneMenuEntry(name, id, parentId, BusId, isLeaf);
						id = 0;
						name = "";
					}
				}
			}
			table.close();
			finished = true;
		}
		
		public String remove(String x){
			
			String content = x.replace("[", "");
			content = content.replace("]", "");
			content = content.replace("{", "");
			content = content.replace("}", "");
			content = content.replaceAll("[\"]", "");
			
			return content;
		}
		
		
}
