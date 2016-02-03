package com.visualdialer.visualdialer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseTables {
	private static final String DATABASE_TABLE_CATA = "CatagoryTable";
	public static final String KEY_CATA_ROWID = "_Cata_id";
	public static final String KEY_CATAGORY_NAME = "catagory_name";
	
	private static final String DATABASE_TABLE_FAV = "FavoritesTable";
	public static final String FAV_NAME = "_FAV_NAME";
	public static final String FAV_CAT_ID = "_cat_id";
	public static final String FAV_PHONE_BUS_ID = "_bus_id";
	public static final String FAV_PHONE_ROWID = "_fav_phone_id";
	public static final String FAV_IS_LEAF = "_is_leaf";

	private static final String DATABASE_TABLE_BUSINESS = "BusinessTable";
	public static final String KEY_BUS_ROWID = "_Bus_id";
	public static final String KEY_BUSINESS_NAME = "business_name";
	
	private static final String DATABASE_TABLE_PHONE = "PhoneMenuTable";
	public static final String KEY_PHONE_MENU_ROWID = "_phone_id";
	public static final String KEY_PHONE_MENU_NAME = "phone_name";
	public static final String KEY_PHONE_MENU_PARENT_ID = "_pmparent_id";
	public static final String KEY_IS_LEAF = "is_leaf";
	
	private static final String DATABASE_NAME = "VisualDialerdb";
	private static final int DATABASE_VERSION = 1;
	
	private static final String DATABASE_TABLE_VERSION = "VersionTable";
	public static final String VERSION_INFO = "_version_id";
	
	private DbHelper ourHelper;
	private final Context ourContext;
	private SQLiteDatabase ourDatabase;
	
	
	private static class DbHelper extends SQLiteOpenHelper{

		public DbHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		public void onCreate(SQLiteDatabase db) {
			db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_CATA);
			db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_BUSINESS);
			db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_PHONE);
			db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_FAV);
			
			db.execSQL("CREATE TABLE " + DATABASE_TABLE_CATA +" (" +
				KEY_CATA_ROWID + " INTEGER NOT NULL, " +
				KEY_CATAGORY_NAME +  " TEXT NOT NULL);"
				);
			db.execSQL("CREATE TABLE " + DATABASE_TABLE_VERSION +" (" +
					VERSION_INFO +  " TEXT NOT NULL);"
					);
			
			db.execSQL("CREATE TABLE " + DATABASE_TABLE_BUSINESS +" (" +
					KEY_BUS_ROWID + " INTEGER NOT NULL, " +
					KEY_CATA_ROWID + " INTEGER NOT NULL, " +
					KEY_BUSINESS_NAME +  " TEXT NOT NULL);"
				);			
			
			db.execSQL("CREATE TABLE " + DATABASE_TABLE_PHONE +" (" +
					KEY_BUS_ROWID + " INTEGER NOT NULL, " +
					KEY_PHONE_MENU_ROWID + " INTEGER NOT NULL, " +
					KEY_IS_LEAF + " INTEGER NOT NULL, " +
					KEY_PHONE_MENU_PARENT_ID + " INTEGER NOT NULL, " +
					KEY_PHONE_MENU_NAME +  " TEXT NOT NULL);"
				);
			
			db.execSQL("CREATE TABLE " + DATABASE_TABLE_FAV + " (" + 
					FAV_NAME + " TEXT NOT NULL, " +   //doenst need to be business name, just a fav item name
					FAV_CAT_ID + " INTEGER NOT NULL, " +
					FAV_PHONE_BUS_ID + " INTEGER NOT NULL, " +
					FAV_PHONE_ROWID + " INTEGER NOT NULL, " +
					FAV_IS_LEAF + " INTEGER NOT NULL); "
					
					
					//fav name and id should be sufficient to identify whole fav item.
				);
		}
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_CATA);
			db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_BUSINESS);
			db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_PHONE);
			db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_FAV);
			db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_VERSION);
			onCreate(db);
		}
		
	}
	
	public DatabaseTables(Context c){
		ourContext = c;
	}
	
	public DatabaseTables open() throws SQLException{
		ourHelper = new DbHelper(ourContext);
		ourDatabase = ourHelper.getWritableDatabase();
		return this;
	}
	
	
	public void close(){
		ourHelper.close();
	}
	
	public long createVersionEntry(String version) {
		ContentValues cv = new ContentValues();
		cv.put(VERSION_INFO, version);
		return ourDatabase.insert(DATABASE_TABLE_VERSION, null, cv);
	}

	public long createCataEntry(String name, int id) {
		ContentValues cv = new ContentValues();
		cv.put(KEY_CATAGORY_NAME, name);
		cv.put(KEY_CATA_ROWID, id);
		return ourDatabase.insert(DATABASE_TABLE_CATA, null, cv);
	}
	
	public long createBusEntry(String name, int id, int parentId) {
		ContentValues cv = new ContentValues();
		cv.put(KEY_BUSINESS_NAME, name);
		cv.put(KEY_CATA_ROWID, parentId);
		cv.put(KEY_BUS_ROWID, id);
		return ourDatabase.insert(DATABASE_TABLE_BUSINESS, null, cv);
	}
	
	public long createPhoneMenuEntry(String name, int id, int parentId, int busID, int isLeaf) {
		ContentValues cv = new ContentValues();
		cv.put(KEY_BUS_ROWID, busID);
		cv.put(KEY_PHONE_MENU_ROWID, id);
		cv.put(KEY_IS_LEAF, isLeaf);
		cv.put(KEY_PHONE_MENU_PARENT_ID, parentId);
		cv.put(KEY_PHONE_MENU_NAME, name);
		return ourDatabase.insert(DATABASE_TABLE_PHONE, null, cv);
	}
	
	public long createFavMenuEntry(String fName, int catId, int busId, int fpid, int isLeaf)
	{
		ContentValues cv = new ContentValues();
		cv.put(FAV_PHONE_BUS_ID, busId);
		cv.put(FAV_PHONE_ROWID, fpid);
		cv.put(FAV_IS_LEAF, isLeaf);
		cv.put(FAV_CAT_ID, catId);
		cv.put(FAV_NAME, fName);
		return ourDatabase.insert(DATABASE_TABLE_FAV, null, cv);
	}
	public String getFavData() {
		String[] columns = new String[]{FAV_NAME, FAV_CAT_ID, FAV_PHONE_BUS_ID, FAV_PHONE_ROWID, FAV_IS_LEAF};
		Cursor c = ourDatabase.query(DATABASE_TABLE_FAV, columns, null, null, null, null, null, null);
		String result = "";
		int iLeaf = c.getColumnIndex(FAV_IS_LEAF);
		int iBusId = c.getColumnIndex(FAV_PHONE_BUS_ID);
		int iCatId = c.getColumnIndex(FAV_CAT_ID);
		int iRow = c.getColumnIndex(FAV_PHONE_ROWID);
		int iBusName = c.getColumnIndex(FAV_NAME);
		for(c.moveToFirst();!c.isAfterLast(); c.moveToNext()){
			result = result + c.getString(iBusName) + "  "					
					+ c.getString(iCatId) + "  "
					+ c.getString(iBusId) +  "  "
					+ c.getString(iRow) + "  " 
					+ c.getString(iLeaf) + "\n";
		}
		return result;
	}
	public String getCataData() {
		String[] columns = new String[]{KEY_CATA_ROWID, KEY_CATAGORY_NAME};
		Cursor c = ourDatabase.query(DATABASE_TABLE_CATA, columns, null, null, null, null, null);
		String result = "";
		int iRow = c.getColumnIndex(KEY_CATA_ROWID);
		int iName = c.getColumnIndex(KEY_CATAGORY_NAME);
		
		for(c.moveToFirst();!c.isAfterLast(); c.moveToNext()){
			result = result + c.getString(iRow) + "  " + 
			c.getString(iName) +  "\n";
		}
		c.close();
	
		return result;
	}
	public String getVersion() {
		String[] columns = new String[]{VERSION_INFO};
		Cursor c = ourDatabase.query(DATABASE_TABLE_VERSION, columns, null, null, null, null, null);
		String result = "";
		int iVersion = c.getColumnIndex(VERSION_INFO);
		
		for(c.moveToFirst();!c.isAfterLast(); c.moveToNext()){
			result = result + c.getString(iVersion) +  "\n";
		}
		c.close();
	
		return result;
	}
	public void updateVersion(String newVersion){
		String[] columns = new String[]{VERSION_INFO};
		Cursor c = ourDatabase.query(DATABASE_TABLE_VERSION, columns, null, null, null, null, null);
		int iVersion = c.getColumnIndex(VERSION_INFO);
		
		for(c.moveToFirst();!c.isAfterLast(); c.moveToNext()){
			ourDatabase.delete(DATABASE_TABLE_VERSION, VERSION_INFO + "=" + (c.getString(iVersion)), null);
		}
		c.close();
		createVersionEntry(newVersion);
	}
	
	public String getCataContains(String name) {
		String[] columns = new String[]{KEY_CATA_ROWID, KEY_CATAGORY_NAME};
		Cursor c = ourDatabase.query(DATABASE_TABLE_CATA, columns, null, null, null, null, null);
		String result = "";
		int iRow = c.getColumnIndex(KEY_CATA_ROWID);
		int iName = c.getColumnIndex(KEY_CATAGORY_NAME);
		
		for(c.moveToFirst();!c.isAfterLast(); c.moveToNext()){
			if(c.getString(iName).toLowerCase().contains(name.toLowerCase()))
				result = result + c.getString(iRow) + "  " + 
						c.getString(iName) +  "\n";
		}
		c.close();
	
		return result;
	}
	
	public String getBusinessData() {
		String[] columns = new String[]{KEY_CATA_ROWID, KEY_BUS_ROWID, KEY_BUSINESS_NAME};
		Cursor c = ourDatabase.query(DATABASE_TABLE_BUSINESS, columns, null, null, null, null, null);
		String result = "";
		int iParentRow = c.getColumnIndex(KEY_CATA_ROWID);
		int iRow = c.getColumnIndex(KEY_BUS_ROWID);
		int iName = c.getColumnIndex(KEY_BUSINESS_NAME);
		
		for(c.moveToFirst();!c.isAfterLast(); c.moveToNext()){
			result = result + c.getString(iParentRow) + "  "
					+ c.getString(iRow) + "  " + 
					c.getString(iName) +  "\n";
		}
		c.close();
		return result;
	}
	
	public String getBusinessData(int parent) {
		String[] columns = new String[]{KEY_CATA_ROWID, KEY_BUS_ROWID, KEY_BUSINESS_NAME};
		Cursor c = ourDatabase.query(DATABASE_TABLE_BUSINESS, columns, null, null, null, null, null);
		String result = "";
		int iParentRow = c.getColumnIndex(KEY_CATA_ROWID);
		int iRow = c.getColumnIndex(KEY_BUS_ROWID);
		int iName = c.getColumnIndex(KEY_BUSINESS_NAME);
		
		for(c.moveToFirst();!c.isAfterLast(); c.moveToNext()){
			if(c.getString(iParentRow).equals(parent + ""))
				result = result + c.getString(iParentRow) + "  "
					+ c.getString(iRow) + "  " + 
					c.getString(iName) +  "\n";
		}
		c.close();
		return result;
	}
	
	public String getBusinessContains(String name) {
		String[] columns = new String[]{KEY_CATA_ROWID, KEY_BUS_ROWID, KEY_BUSINESS_NAME};
		Cursor c = ourDatabase.query(DATABASE_TABLE_BUSINESS, columns, null, null, null, null, null);
		String result = "";
		int iParentRow = c.getColumnIndex(KEY_CATA_ROWID);
		int iRow = c.getColumnIndex(KEY_BUS_ROWID);
		int iName = c.getColumnIndex(KEY_BUSINESS_NAME);
		
		for(c.moveToFirst();!c.isAfterLast(); c.moveToNext()){
			if(c.getString(iName).toLowerCase().contains(name.toLowerCase()))
				result = result + c.getString(iParentRow) + "  "
					+ c.getString(iRow) + "  " + 
					c.getString(iName) +  "\n";
		}
		c.close();
		return result;
	}
	
	public String getPhoneMenuData() {
		String[] columns = new String[]{KEY_IS_LEAF, KEY_BUS_ROWID,KEY_PHONE_MENU_ROWID,KEY_PHONE_MENU_PARENT_ID,KEY_PHONE_MENU_NAME};
		Cursor c = ourDatabase.query(DATABASE_TABLE_PHONE, columns, null, null, null, null, null);
		String result = "";
		int iLeaf = c.getColumnIndex(KEY_IS_LEAF);
		int iBus = c.getColumnIndex(KEY_BUS_ROWID);
		int iParentRow = c.getColumnIndex(KEY_PHONE_MENU_PARENT_ID);
		int iRow = c.getColumnIndex(KEY_PHONE_MENU_ROWID);
		int iName = c.getColumnIndex(KEY_PHONE_MENU_NAME);
		
		for(c.moveToFirst();!c.isAfterLast(); c.moveToNext()){
			result = result + c.getString(iLeaf) + "  "
					+ c.getString(iBus) + "  "
					+ c.getString(iParentRow) + "  "
					+ c.getString(iRow) + "  " + 
					c.getString(iName) +  "\n";
		}
		c.close();
		return result;
	}
	
	public String getPhoneMenuData(int parent) {
		String[] columns = new String[]{KEY_IS_LEAF,
				KEY_BUS_ROWID,KEY_PHONE_MENU_ROWID,
				KEY_PHONE_MENU_PARENT_ID, 
				KEY_PHONE_MENU_NAME};
		Cursor c = ourDatabase.query(DATABASE_TABLE_PHONE, columns, null, null, null, null, null);
		String result = "";
		int iLeaf = c.getColumnIndex(KEY_IS_LEAF);
		int iBus = c.getColumnIndex(KEY_BUS_ROWID);
		int iParentRow = c.getColumnIndex(KEY_PHONE_MENU_PARENT_ID);
		int iRow = c.getColumnIndex(KEY_PHONE_MENU_ROWID);
		int iName = c.getColumnIndex(KEY_PHONE_MENU_NAME);
		
		for(c.moveToFirst();!c.isAfterLast(); c.moveToNext()){
			if(c.getString(iBus).equals(parent + "") && c.getString(iParentRow).equals(0 + ""))
				result = result + c.getString(iLeaf) + "  "
					+ c.getString(iBus) + "  "
					+ c.getString(iParentRow) + "  "
					+ c.getString(iRow) + "  " + 
					c.getString(iName) +  "\n";
		}
		c.close();
		return result;
		
	}
	
	public String getPhoneMenuContains(String name) {
		String[] columns = new String[]{KEY_IS_LEAF,
				KEY_BUS_ROWID,KEY_PHONE_MENU_ROWID,
				KEY_PHONE_MENU_PARENT_ID, 
				KEY_PHONE_MENU_NAME};
		Cursor c = ourDatabase.query(DATABASE_TABLE_PHONE, columns, null, null, null, null, null);
		String result = "";
		int iLeaf = c.getColumnIndex(KEY_IS_LEAF);
		int iBus = c.getColumnIndex(KEY_BUS_ROWID);
		int iParentRow = c.getColumnIndex(KEY_PHONE_MENU_PARENT_ID);
		int iRow = c.getColumnIndex(KEY_PHONE_MENU_ROWID);
		int iName = c.getColumnIndex(KEY_PHONE_MENU_NAME);
		
		for(c.moveToFirst();!c.isAfterLast(); c.moveToNext()){
			if(c.getString(iName).toLowerCase().contains(name.toLowerCase()))
				result = result + c.getString(iLeaf) + "  "
					+ c.getString(iBus) + "  "
					+ c.getString(iParentRow) + "  "
					+ c.getString(iRow) + "  " + 
					c.getString(iName) +  "\n";
		}
		c.close();
		return result;
		
	}
	
	public String getPhoneMenuInnerData(int parent) {
		String[] columns = new String[]{KEY_IS_LEAF, KEY_BUS_ROWID,KEY_PHONE_MENU_ROWID,KEY_PHONE_MENU_PARENT_ID, KEY_PHONE_MENU_NAME};
		
		Cursor c = ourDatabase.query(DATABASE_TABLE_PHONE, columns, null, null, null, null, null);
		String result = "";
		int iLeaf = c.getColumnIndex(KEY_IS_LEAF);
		int iBus = c.getColumnIndex(KEY_BUS_ROWID);
		int iParentRow = c.getColumnIndex(KEY_PHONE_MENU_PARENT_ID);
		int iRow = c.getColumnIndex(KEY_PHONE_MENU_ROWID);
		int iName = c.getColumnIndex(KEY_PHONE_MENU_NAME);
		
		for(c.moveToFirst();!c.isAfterLast(); c.moveToNext()){
			if(c.getString(iParentRow).equals(parent + ""))
				result = result + c.getString(iLeaf) + "  "
					+ c.getString(iBus) + "  "
					+ c.getString(iParentRow) + "  "
					+ c.getString(iRow) + "  " + 
					c.getString(iName) +  "\n";
		}
		c.close();
		return result;
	}
	
	public String getCataName(long l) throws SQLException{
		String[] columns = new String[]{KEY_CATA_ROWID, KEY_CATAGORY_NAME};
		Cursor c = ourDatabase.query(DATABASE_TABLE_CATA, columns, KEY_CATA_ROWID + "=" +l, 
				null, null, null, null);
		if(c != null){
			c.moveToFirst();
			String name = c.getString(1);
			c.close();
			return name;
		}
		c.close();
		return null;
	}
	
	public String getBusName(long l) throws SQLException{
		String[] columns = new String[]{KEY_BUS_ROWID, KEY_BUSINESS_NAME};
		Cursor c = ourDatabase.query(DATABASE_TABLE_BUSINESS, columns, KEY_BUS_ROWID + "=" +l, 
				null, null, null, null);
		if(c != null){
			c.moveToFirst();
			String name = c.getString(1);
			c.close();
			return name;
		}
		c.close();
		return null;
	}
	
	
	
	public boolean inFavs(long l) throws SQLException{
		String[] columns = new String[]{FAV_PHONE_ROWID, FAV_NAME};
		Cursor c = ourDatabase.query(DATABASE_TABLE_BUSINESS, columns, FAV_PHONE_ROWID + "=" +l, 
				null, null, null, null);
		if(c != null){
			c.close();
			return true;
		}
		c.close();
		return false;
	}
	
	public String getPhoneMenuName(long l) throws SQLException{
		String[] columns = new String[]{KEY_PHONE_MENU_ROWID, KEY_PHONE_MENU_NAME};
		Cursor c = ourDatabase.query(DATABASE_TABLE_PHONE, columns, KEY_PHONE_MENU_ROWID + "=" +l, 
				null, null, null, null);
		if(c != null){
			c.moveToFirst();
			String name = c.getString(1);
			c.close();
			return name;
		}
		c.close();
		return null;
	}
	
	public int getBusParentId(long l) throws SQLException{
		String[] columns = new String[]{KEY_BUS_ROWID, KEY_CATA_ROWID};
		Cursor c = ourDatabase.query(DATABASE_TABLE_BUSINESS, columns, KEY_BUS_ROWID + "=" +l, 
				null, null, null, null);
		if(c != null){
			c.moveToFirst();
			String name = c.getString(1);
			c.close();
			return Integer.parseInt(name);
		}
		c.close();
		return -1;
	}
	

	
	public int getPhoneMenuParentId(long l) throws SQLException{
		String[] columns = new String[]{KEY_PHONE_MENU_ROWID, KEY_PHONE_MENU_PARENT_ID};
		Cursor c = ourDatabase.query(DATABASE_TABLE_PHONE, columns, KEY_PHONE_MENU_ROWID + "=" +l, 
				null, null, null, null);
		if(c != null){
			c.moveToFirst();
			String name = c.getString(1);
			c.close();
			return Integer.parseInt(name);
		}
		c.close();
		return -1;
	}
	
	public int getPhoneMenuBusId(int l) throws SQLException{
		String[] columns = new String[]{KEY_PHONE_MENU_ROWID, KEY_BUS_ROWID};
		Cursor c = ourDatabase.query(DATABASE_TABLE_PHONE, columns, KEY_PHONE_MENU_ROWID + "=" +l, 
				null, null, null, null);
		if(c != null){
			c.moveToFirst();
			String name = c.getString(1);
			c.close();
			return Integer.parseInt(name);
		}
		c.close();
		return -1;
	}

	public void updateCataEntry(long num, String modName) throws SQLException{
		ContentValues cvUpdate = new ContentValues();
		cvUpdate.put(KEY_CATAGORY_NAME, modName);
		ourDatabase.update(DATABASE_TABLE_CATA, cvUpdate, 
				KEY_CATA_ROWID + "=" + num, null);
		
	}
	
	public void updateBusinessEntry(long num, String modName) throws SQLException{
		ContentValues cvUpdate = new ContentValues();
		cvUpdate.put(KEY_BUSINESS_NAME, modName);
		ourDatabase.update(DATABASE_TABLE_BUSINESS, cvUpdate, 
				KEY_BUS_ROWID + "=" + num, null);
		
	}
	
	public void updatePhoneMenuEntry(long num, String modName) throws SQLException{
		ContentValues cvUpdate = new ContentValues();
		cvUpdate.put(KEY_PHONE_MENU_NAME, modName);
		ourDatabase.update(DATABASE_TABLE_PHONE, cvUpdate, 
				KEY_PHONE_MENU_ROWID + "=" + num, null);
		
	}

	public void deleteCataEntry(long lRow) throws SQLException{
		ourDatabase.delete(DATABASE_TABLE_CATA, KEY_CATA_ROWID+ "=" + lRow, null);
	}
	
	public void deleteBusEntry(long lRow) throws SQLException{
		ourDatabase.delete(DATABASE_TABLE_BUSINESS, KEY_BUS_ROWID+ "=" + lRow, null);
	}
	
	public void deletePhoneMenuEntry(long lRow) throws SQLException{
		ourDatabase.delete(DATABASE_TABLE_PHONE, KEY_PHONE_MENU_ROWID+ "=" + lRow, null);
	}
	
	
	public void emptyDatabase(){
		
		
		String[] columns3 = new String[]{KEY_PHONE_MENU_ROWID, KEY_PHONE_MENU_NAME};
		Cursor c3 = ourDatabase.query(DATABASE_TABLE_PHONE, columns3, null, null, null, null, null);
		int iRow = c3.getColumnIndex(KEY_PHONE_MENU_ROWID);
		
		for(c3.moveToFirst();!c3.isAfterLast(); c3.moveToNext()){
			ourDatabase.delete(DATABASE_TABLE_PHONE, KEY_PHONE_MENU_ROWID+ "=" + Long.parseLong(c3.getString(iRow)), null);
		}
		c3.close();
		//.c3.close();
		String[] columns2 = new String[]{KEY_BUS_ROWID, KEY_BUSINESS_NAME};
		Cursor c2 = ourDatabase.query(DATABASE_TABLE_BUSINESS, columns2, null, null, null, null, null);
		iRow = c2.getColumnIndex(KEY_BUS_ROWID);
		
		for(c2.moveToFirst();!c2.isAfterLast(); c2.moveToNext()){
			ourDatabase.delete(DATABASE_TABLE_BUSINESS, KEY_BUS_ROWID+ "=" + Long.parseLong(c2.getString(iRow)), null);
		}
		c2.close();
		
		String[] columns = new String[]{KEY_CATA_ROWID, KEY_CATAGORY_NAME};
		Cursor c = ourDatabase.query(DATABASE_TABLE_CATA, columns, null, null, null, null, null);
		iRow = c.getColumnIndex(KEY_CATA_ROWID);
		
		for(c.moveToFirst();!c.isAfterLast(); c.moveToNext()){
			ourDatabase.delete(DATABASE_TABLE_CATA, KEY_CATA_ROWID+ "=" + Long.parseLong(c.getString(iRow)), null);
		}
		c.close();
		
	}
	
	public void emptyFavs(){
		String[] columns = new String[]{FAV_PHONE_ROWID, FAV_NAME};
		Cursor c = ourDatabase.query(DATABASE_TABLE_FAV, columns, null, null, null, null, null);
		int iRow = c.getColumnIndex(FAV_PHONE_ROWID);
		
		for(c.moveToFirst();!c.isAfterLast(); c.moveToNext()){
			ourDatabase.delete(DATABASE_TABLE_FAV, FAV_PHONE_ROWID+ "=" + Long.parseLong(c.getString(iRow)), null);
		}
		c.close();
	}
	
	
}