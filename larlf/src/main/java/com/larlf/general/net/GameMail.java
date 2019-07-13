package com.larlf.general.net;

import java.util.ArrayList;
import java.util.List;

public class GameMail
{
	private int type;// 1为对等级i - j的角色群发，0为单发
	private String receiver;// 单发时为角色主键id，群发时为“startLevel-endLevel”,包括startLevel，不包括endLevel
	private String subject;
	private String content;
	// arg1~6 --邮件附件物品Id(堆叠型为模板Id，非堆叠为实例Id, 金钱则为"money")
	private List<String> itemList = new ArrayList<String>(6);
	// v1~6 --该物品数量,金钱数.附:若类型为实例ID,则数量必须为1
	private List<Integer> itemNumList = new ArrayList<Integer>(6);

	public int getType()
	{
		return type;
	}

	public void setType(int type)
	{
		this.type = type;
	}

	public String getReceiver()
	{
		return receiver;
	}

	public void setReceiver(String receiver)
	{
		this.receiver = receiver;
	}

	public String getSubject()
	{
		return subject;
	}

	public void setSubject(String subject)
	{
		this.subject = subject;
	}

	public String getContent()
	{
		return content;
	}

	public void setContent(String content)
	{
		this.content = content;
	}

	public List<String> getItemList()
	{
		return itemList;
	}

	public void setItemList(List<String> itemList)
	{
		this.itemList = itemList;
	}

	public List<Integer> getItemNumList()
	{
		return itemNumList;
	}

	public void setItemNumList(List<Integer> itemNumList)
	{
		this.itemNumList = itemNumList;
	}

}
