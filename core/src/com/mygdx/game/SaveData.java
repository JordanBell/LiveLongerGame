package com.mygdx.game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class SaveData
{
	public enum EProgress
	{
		Meta1,
		Rooms1_Heal,
		Rooms1_Find,
		Meta2,
		Rooms2_Market,
		Meta3,
		Rooms3_Gem1,
		Meta4,
		Rooms4_Gem2;
		
		public String getLevel()
		{
			switch(this)
			{
				case Rooms1_Heal:
					return "level_heal_her";
				case Rooms1_Find:
					return "level_find_him";
				case Rooms2_Market:
					return "level_market";
				case Rooms4_Gem2:
					return "level_gem2";
				default:
					throw new RuntimeException("Progress is not associated to a Rooms level: " + this);
			}
		}
	}
	
	public static Preferences m_pPrefs = Gdx.app.getPreferences("Save");

	// Saved data
	public static EProgress m_eProgress = EProgress.Meta1;
	public static String m_sChild = "";
	
	public static final String ID_Progress = "progressEnum";
	public static final String ID_ChildType = "childType";
	public static final String ID_Items = "itemList";
	public static List<String> m_lsItems;
	
	public static void load()
	{
		m_pPrefs.clear(); // Uncomment to clear save at start
		
		m_eProgress = EProgress.values()[m_pPrefs.getInteger(ID_Progress, 0)];
		m_sChild = m_pPrefs.getString(ID_ChildType, "Daughter");
		
		// Items list - convert from csv
		String itemsList = m_pPrefs.getString(ID_Items, null);
		if(itemsList == null)
		{
			// Start a new list
			m_lsItems = new ArrayList<String>();
		}
		else
		{
			// No previous list exists
			m_lsItems = Arrays.asList(itemsList.split(","));
			
		}
	}
	
	public static String getChildProSubj()
	{
		if(m_sChild.equals("Daughter"))
		{
			return "She";
		}
		else
		{
			return "He";
		}
	}
	
	public static String getChildProObj()
	{
		if(m_sChild.equals("Daughter"))
		{
			return "her";
		}
		else
		{
			return "him";
		}
	}
	
	public static void save()
	{
		m_pPrefs.putInteger(ID_Progress, m_eProgress.ordinal());
		m_pPrefs.putString(ID_ChildType, m_sChild);
		
		// Items list - convert to csv
		StringBuilder sItemsCSV = new StringBuilder();
		for(String sItemID : m_lsItems) sItemsCSV.append(sItemID + ",");
		m_pPrefs.putString(ID_Items, sItemsCSV.toString());
		
		m_pPrefs.flush();
	}
	
	public static void setProgress(EProgress i_eProg)
	{
		if(i_eProg == EProgress.Rooms1_Heal)
		{
			m_sChild = "Daughter";
		}
		else if(i_eProg == EProgress.Rooms1_Find)
		{
			m_sChild = "Son";
		}
		m_eProgress = i_eProg;
	}
	
	public static int getMetaSequence()
	{
		switch(m_eProgress)
		{
			case Meta1: return 0;
			case Meta2: return 1;
			case Meta3: return 2;
			case Meta4: return 3;
			default: return -1;
		}
	}
	
	public static int getStateIndex()
	{
		switch(m_eProgress)
		{
			case Meta1:
			case Meta2:
			case Meta3:
			case Meta4:
				return 0;

			case Rooms1_Heal:
			case Rooms1_Find:
			case Rooms2_Market:
			case Rooms3_Gem1:
			case Rooms4_Gem2:
				return 1;
				
			default: 
				return -1;
		}
	}
}
