package com.mygdx.game.rooms;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.XmlReader;

class Connection
{
	String m_sToID;
	String m_sLockID;
}

class Item
{
	enum EType
	{
		EType_Key,
		EType_Weapon,
		EType_Vial,
		EType_Rope;
		
		@Override
		public String toString()
		{
			switch(this)
			{
				case EType_Key: return "key";
				case EType_Weapon: return "weapon";
				case EType_Vial: return "vial";
				case EType_Rope: return "rope";	
				default: throw new RuntimeException("All item types must convert to string.");
			}
		}
	}
	
	EType m_eType;
	String m_sKeyID;
	AnimItem m_pAnim;
	
	Item(EType i_eType)
	{
		m_eType = i_eType;
		m_pAnim = new AnimItem(m_eType);
	}
	
	void draw(SpriteBatch i_pBatch, int i_iX, int i_iY)
	{
		m_pAnim.update();
		TextureRegion pReg = m_pAnim.getCurrentRegion();
		int iHW = pReg.getRegionWidth() / 2;	// Half width
		m_pAnim.draw(i_pBatch, i_iX - iHW, i_iY + 2);
	}
}

public class Node
{
	Vector2 m_vPos;
	boolean m_bConnectionsRendered;
	List<Node> m_lpNeighbors = new ArrayList<Node>();
	List<Door> m_lpNeighborDoors = new ArrayList<Door>();
	String m_sID;

	List<Connection> m_lpConnections = new ArrayList<Connection>();
	Item m_pItem = null;
	
	Node(String i_sID, Vector2 i_vPos)
	{
		m_sID = i_sID;
		m_vPos = i_vPos;
	}
	
	void parse(XmlReader.Element i_pElement)
	{
		for (int i = 0; i < i_pElement.getChildCount(); ++i)
		{
			XmlReader.Element pChild = i_pElement.getChild(i);
			
			String sChildName = pChild.getName();
			if(sChildName.equals("c")) // Connection
			{
				Connection pConnection = new Connection();
				pConnection.m_sToID = pChild.getText();
				pConnection.m_sLockID = pChild.getAttribute("lock", "");
				m_lpConnections.add(pConnection);
			}
			else if(sChildName.equals("k")) // Key
			{
				m_pItem = new Item(Item.EType.EType_Key);
			}
			else if(sChildName.equals("w")) // Weapon
			{
				m_pItem = new Item(Item.EType.EType_Weapon);
			}
			else if(sChildName.equals("vial")) // Special item: Vial of Black
			{
				m_pItem = new Item(Item.EType.EType_Vial);
			}
//			else if(sChildName.equals("rope")) // Special item: Vial of Black
//			{
//				m_pItem = new Item(Item.EType.EType_Rope);
//			}
		}
	}
}