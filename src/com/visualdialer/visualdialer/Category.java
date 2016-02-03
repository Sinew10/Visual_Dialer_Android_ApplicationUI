package com.visualdialer.visualdialer;

public class Category 
{
	private static final String DELIMITER = ",";
	private String name;
	private int cat_id;
	
	//default constructor
	Category()
	{
		name = "Empty";
		cat_id = 999;
	}
	//manual constructor
	public Category(int id_input, String name_input)
	{
		name = name_input;
		cat_id = id_input;
	}
	//returns category id
	public int get_cat_id()
	{
		return cat_id;
	}
	public void setCatId(int i){
		cat_id = i;
	}
	//returns category name
	public String get_name()
	{
		return name;
	}
	public void setName(String n){
		name = n;
	}
	
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append(name).append(DELIMITER);
		builder.append(cat_id).append(DELIMITER);
		return builder.toString();
	}
}
