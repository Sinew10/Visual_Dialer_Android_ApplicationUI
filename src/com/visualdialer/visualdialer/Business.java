package com.visualdialer.visualdialer;

public class Business extends Category
{
	private int bus_id;
	//default constructor
	Business()
	{
		bus_id = 999;
	}
	//manual constructor
	public Business(int parent, int id_input, String name_input)
	{
		super.setCatId(parent);
		super.setName(name_input);
		bus_id = id_input;
	}
	public int getBus_id() 
	{
		return bus_id;
	}
	public void setBus_id(int id){
		bus_id = id;
	}
}
