package com.mygdx.game;

import java.util.Hashtable;

import com.badlogic.gdx.utils.XmlReader;

public class Level
{
	Hashtable<String, Room> m_tRooms = new Hashtable<String, Room>();
	String m_sID;
	
	Level(String i_sID)
	{
		m_sID = i_sID;
	}
	
	void parse(XmlReader.Element i_pElement)
	{
		for (int i = 0; i < i_pElement.getChildCount(); ++i)
		{
			XmlReader.Element pRoomNode = i_pElement.getChild(i);
			Room pRoom = new Room(pRoomNode.getAttribute("id"));
			m_tRooms.put(pRoom.m_sID, pRoom);

			// Parse the room
			pRoom.parse(pRoomNode);
		}
	}
}
