package com.visualdialer.visualdialer;
import java.util.List;
import java.util.Vector;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.widget.Toast;



public class globVar {
	
	private static Vector<String> displayedNames = new Vector<String>();
	private static Vector<Category> cats = new Vector<Category>();
	private static Vector<Business> bus = new Vector<Business>();
	private static Vector<Phone> phone = new Vector<Phone>();
	private static int menuPosition;
	private static boolean running = false;
	private static int position;
	private static Phone selectedItem;
	private static Business selectedBus = null;
	private static Phone favSelectedItem = null;
	private static String label = "";
	//private static int myNumber;
	
	public static String getMyNumber(Context c){
		TelephonyManager tm = (TelephonyManager) c.getSystemService(Context.TELEPHONY_SERVICE);
		return tm.getLine1Number();
	}
	public static void setMenuPosition(int m){
		menuPosition = m;
	}
	public static void setSelectedItem(Phone p){
		selectedItem = p;
	}
	public static Phone getSelectedItem(){
		return selectedItem;
	}	
	public static void addPhoneElement(Phone ele){
		int counter = 0;
		boolean notAdded = true;
		if(phone.size() == 0)
			phone.add(ele);
		else{
			while(counter < phone.size() && notAdded){
				if(phone.elementAt(counter).get_name().compareTo(ele.get_name()) > 0){
					phone.add(counter,ele);
					notAdded = false;
				}else if(counter + 1 == phone.size()){
					phone.add(counter + 1,ele);
					notAdded = false;
				}else if ((phone.elementAt(counter).get_name().compareTo(ele.get_name()) < 0) &&
						(phone.elementAt(counter + 1).get_name().compareTo(ele.get_name()) >= 0)){
					phone.add(counter + 1, ele);
					notAdded = false;
				}
				counter++;
			}
		}
	}
	public static void clearPhoneMenu(){
		phone.clear();
	}
	public static Vector<Phone> getPhoneMenu(){
		return phone;
	}
	public static void incMenuPosition(){
		menuPosition++;
	}
	public static void decMenuPosition(){
		menuPosition--;
	}
	public static int getMenuPosition(){
		return menuPosition;
	}
	public static Vector<String> getDisplayedNames(){
		return displayedNames;
	}
	
	public static void clearDispalyedNames(){
		displayedNames.clear();
	}
	
	public static void addDisplayName(String ele){
		displayedNames.add(ele);
	}	
	public static Vector<Category> getCategories(){
		return cats;
	}
	public static void addCategories(Category i){
		int counter = 0;
		boolean NotAdded = true;
		if(cats.size() == 0)
			cats.add(i);
		else{
			while(NotAdded){
				if(cats.elementAt(counter).get_name().compareTo(i.get_name()) > 0){
					cats.add(counter,i);
					NotAdded = false;
				}else if(counter + 1 == cats.size()){
					cats.add(counter + 1,i);
					NotAdded = false;
				}else if ((cats.elementAt(counter).get_name().compareTo(i.get_name()) < 0) &&
						(cats.elementAt(counter + 1).get_name().compareTo(i.get_name()) >= 0)){
					cats.add(counter + 1,i);
					NotAdded = false;
				}
				counter++;
			}
		}
	}
	public static void clearCategories(){
		cats.clear();
	}	
	public static Vector<Business> getBusinesses(){
		return bus;
	}
	public static void addBusinesses(Business b){
		int counter = 0;
		boolean notAdded = true;
		if(bus.size() == 0)
			bus.add(b);
		else{
			while(counter < bus.size() && notAdded){
				if(bus.elementAt(counter).get_name().compareTo(b.get_name()) > 0){
					bus.add(counter,b);
					notAdded = false;
				}else if(counter + 1 == bus.size()){
					bus.add(counter + 1, b);
					notAdded = false;
				}
				else if ((bus.elementAt(counter).get_name().compareTo(b.get_name()) < 0) &&
						(bus.elementAt(counter + 1).get_name().compareTo(b.get_name()) >= 0)){
					bus.add(counter + 1, b);
					notAdded = false;
				}
				counter++;
			}
		}
	}
	public static void clearBusinesses(){
		bus.clear();
	}
	public static boolean isRunning() {
		return running;
	}	
	public static void setPosition(int position) {
		globVar.position = position;
	}
	public static int getPosition() {
		return position;
	}
	public static void setFavSelectedItem(Phone favSelectedItem) {
		globVar.favSelectedItem = favSelectedItem;
	}
	public static Phone getFavSelectedItem() {
		return favSelectedItem;
	}
	public static void setRunning(boolean running) {
		globVar.running = running;
	}
	
	public static void toast(Context c, String s){

		int duration = Toast.LENGTH_SHORT;

		Toast toast = Toast.makeText(c, s, duration);
		//tost.setView(view);
		toast.show();
	}
	public static String getLabel() {
		return label;
	}
	public static void setLabel(String label) {
		globVar.label = label;
	}
	public static Business getSelectedBus() {
		return selectedBus;
	}
	public static void setSelectedBus(Business selectedBus) {
		globVar.selectedBus = selectedBus;
	}
	public static String busName()
	{
		int temp = globVar.selectedItem.getBus_id();
		bus = globVar.getBusinesses();
		return bus.get(temp).get_name();
	}
}
