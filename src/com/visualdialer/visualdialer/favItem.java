package com.visualdialer.visualdialer;

public class favItem 
{
	private int parId;
	private int isLeaf;
	private int catId;
	private int busId;
	private int phoneId;
	private String favName;
	favItem()
	{
		setParId(-1);
		setIsLeaf(-1);
		setCatId(-1);
		setBusId(-1);
		setPhoneId(-1);
		setfavName("NULL");
	}
	favItem(Phone pho)
	{
		setfavName(pho.get_name());
		setParId(pho.getParent_id());
		setIsLeaf(pho.getIsLeaf());
		setCatId(pho.get_cat_id());
		setBusId(pho.getBus_id());
		setPhoneId(pho.getMenu_id());
	} 
	favItem(String fName, int cat,int bus)
	{
		setfavName(fName);
		setCatId(cat);
		setBusId(bus);
		setParId(-1);
		setIsLeaf(-1);
		setPhoneId(-1);
	}
	favItem(String fName, int cat,int bus, int phone)
	{
		setfavName(fName);
		setCatId(cat);
		setBusId(bus);
		setParId(-1);
		setIsLeaf(-1);
		setPhoneId(phone);
	}
	favItem(String fName, int cat,int bus, int par, int phone)
	{
		setfavName(fName);
		setCatId(cat);
		setBusId(bus);
		setParId(par);
		setIsLeaf(-1);
		setPhoneId(phone);
	}
	favItem(String fName,  int cat, int bus, int par, int phone,int leaf)
	{
		setfavName(fName);
		setCatId(cat);
		setBusId(bus);
		setParId(par);
		setIsLeaf(leaf);
		setPhoneId(phone);
	}
	public void setParId(int parId) 
	{
		this.parId = parId;
	}
	public int getParId() 
	{
		return parId;
	}
	public void setIsLeaf(int isLeaf) 
	{
		this.isLeaf = isLeaf;
	}
	public int getIsLeaf() 
	{
		return isLeaf;
	}
	public void setCatId(int catId) 
	{
		this.catId = catId;
	}
	public int getCatId() 
	{
		return catId;
	}
	public void setBusId(int busId) 
	{
		this.busId = busId;
	}
	public int getBusId() 
	{
		return busId;
	}
	public void setPhoneId(int phoneId) 
	{
		this.phoneId = phoneId;
	}
	public int getPhoneId() 
	{
		return phoneId;
	}
	public void setfavName(String favName) 
	{
		this.favName = favName;
	}
	public String getfavName() 
	{
		return favName;
	}	
}
