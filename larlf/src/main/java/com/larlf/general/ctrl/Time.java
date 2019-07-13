package com.larlf.general.ctrl;

import java.util.Calendar;

public class Time
{
	// 时间的位置
	static int POS_YEAR = 0;
	static int POS_MON = 1;
	static int POS_DAY = 2;
	static int POS_WEEK = 3;
	static int POS_HOUR = 4;
	static int POS_MIN = 5;
	static int POS_SEC = 6;
	static int POS_COUNT = 7;

	// 用于存放时间的数组，为负值时为空
	public int[] data = new int[Time.POS_COUNT];

	public Time()
	{
		for(int i = 0; i < POS_COUNT; ++i)
		{
			this.data[i] = -1;
		}
	}

	public Time(long t)
	{
		this.parse(t);
	}

	int getYear()
	{
		return this.data[POS_YEAR];
	}

	int getMon()
	{
		return this.data[POS_MON];
	}

	int getDay()
	{
		return this.data[POS_DAY];
	}

	int getWeek()
	{
		return this.data[POS_WEEK];
	}

	int getHour()
	{
		return this.data[POS_HOUR];
	}

	int getMin()
	{
		return this.data[POS_MIN];
	}

	int getSec()
	{
		return this.data[POS_SEC];
	}

	void parse(long t)
	{
		Calendar date = Calendar.getInstance();
		date.setTimeInMillis(t * 1000);
		this.data[POS_YEAR] = date.get(Calendar.YEAR);
		this.data[POS_MON] = date.get(Calendar.MONTH) + 1;
		this.data[POS_DAY] = date.get(Calendar.DAY_OF_MONTH);
		this.data[POS_WEEK] = date.get(Calendar.DAY_OF_WEEK) - 1;
		this.data[POS_HOUR] = date.get(Calendar.HOUR_OF_DAY);
		this.data[POS_MIN] = date.get(Calendar.MINUTE);
		this.data[POS_SEC] = date.get(Calendar.SECOND);
	}
}
