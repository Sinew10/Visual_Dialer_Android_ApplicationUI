package com.visualdialer.visualdialer;

public class Phone extends Business
{
	private int isLeaf;
	private int menu_id;
	private int parent_id;
	//private int phone_number;
	//default constructor
	Phone()
	{
		setIsLeaf(1);//1 is a leaf 0 is not
		setMenu_id(999);
		setParent_id(-99);
	}
	public Phone(int bus_id, int m_id, int p_id, String p_n, int tf)
	{
		this.setIsLeaf(tf);
		setMenu_id(m_id);
		setParent_id(p_id);
		super.setName(p_n);
		super.setBus_id(bus_id);
	}
	Phone(favItem fav)
	{
		this.setIsLeaf(fav.getIsLeaf());
		setMenu_id(fav.getPhoneId());
		setParent_id(fav.getParId());
		super.setName(fav.getfavName());
		super.setBus_id(fav.getBusId());
	}
	public int getIsLeaf() {
		return isLeaf;
	}
	public void setIsLeaf(int isLeaf) {
		this.isLeaf = isLeaf;
	}
	public int getMenu_id() {
		return menu_id;
	}
	public void setMenu_id(int menu_id) {
		this.menu_id = menu_id;
	}
	public int getParent_id() {
		return parent_id;
	}
	public void setParent_id(int parent_id) {
		this.parent_id = parent_id;
	}
}
