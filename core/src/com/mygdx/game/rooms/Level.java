package com.mygdx.game.rooms;

import java.util.ArrayList;
import java.util.List;

import javax.sql.ConnectionPoolDataSource;

import com.badlogic.gdx.utils.XmlReader;

public class Level
{
	List<Room> m_lpRooms = new ArrayList<Room>();
	
	String m_sID;
	String m_sStartIDRoom;
	String m_sStartIDNode;
	
	Room m_pStartRoom;
	Node m_pStartNode;
	
	Level(String i_sID)
	{
		m_sID = i_sID;
	}
	
	void parse(XmlReader.Element i_pElement)
	{
		m_sStartIDRoom = i_pElement.getAttribute("startRoom");
		m_sStartIDNode = i_pElement.getAttribute("startNode");
		
		for (int i = 0; i < i_pElement.getChildCount(); ++i)
		{
			XmlReader.Element pRoomNode = i_pElement.getChild(i);
			Room pRoom = new Room(pRoomNode.getAttribute("id"));
			m_lpRooms.add(pRoom);

			// Parse the room
			pRoom.parse(pRoomNode);
		}
	}
	 
	// Establish connections and whatnot
	void linkAll()
	{
		// Determine the start state
		m_pStartRoom = getRoomByID(m_sStartIDRoom);
		m_pStartNode = m_pStartRoom.m_pNodes.getNodeByID(m_sStartIDNode);
		
		// Node connections
		for(Room pRoom : m_lpRooms)
		{
			// Go through doors and connect them to rooms
			for(Door pDoor : pRoom.m_lpDoors)
			{
				if(!pDoor.m_bIsEndOfLevel) // Only link if this door isn't the end of level
				{
					pDoor.m_pToRoom = getRoomByID(pDoor.m_sToIDRoom);
					if(pDoor.m_pToRoom == null)
					{
						throw new RuntimeException("Unrecognised room that this door leads to: " + pDoor.m_sToIDRoom);
					}
					pDoor.m_pToNode = pDoor.m_pToRoom.m_pNodes.getNodeByID(pDoor.m_sToIDNode);
					if(pDoor.m_pToNode == null)
					{
						throw new RuntimeException("Unrecognised node (in room " + pDoor.m_sToIDRoom + ") that this door leads to: " + pDoor.m_sToIDNode);
					}
				}
			}
			
			for(Node pNode : pRoom.m_pNodes.m_lpNodes)
			{
				for(Connection pConnection : pNode.m_lpConnections)
				{
					Node pConnAsNode = pRoom.m_pNodes.getNodeByID(pConnection.m_sToID);
					Door pConnAsDoor = pRoom.getDoorByID(pConnection.m_sToID);
					if(pConnAsNode != null)
					{
						// Add the node as a neighbor
						if(pConnection.m_bRopeRequired)
						{
							pNode.m_pRopeConnection = pConnection;
						}
					}
					else if(pConnAsDoor != null)
					{
						pConnAsDoor.m_pFromNode = pNode;
						pNode.m_lpNeighborDoors.add(pConnAsDoor); // TODO: Allow player to move through doors by tapping
					}
				}
			}
			
			for(NPC pNPC : pRoom.m_lpNPCs)
			{
				if(pNPC.m_sPlaceAtNode != null)
				{
					// Place at the node by that name
					Node pAtNode = pRoom.m_pNodes.getNodeByID(pNPC.m_sPlaceAtNode);
					
					if(pAtNode == null)
					{
						throw new RuntimeException("Failed to find a node named " + pNPC.m_sPlaceAtNode + " to place an NPC.");
					}
					
					pNPC.m_vPosition = pAtNode.m_vPos.cpy();
				}
			}
			
			// TODO: Go through melee and connect them to where they watch
		}
	}
	
	Room getRoomByID(String i_sToID)
	{
		for(Room pRoom : m_lpRooms)
		{
			if(pRoom.m_sID.equals(i_sToID))
			{
				return pRoom;
			}
		}
		
		return null;
	}
}
