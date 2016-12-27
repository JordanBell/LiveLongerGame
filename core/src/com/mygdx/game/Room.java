package com.mygdx.game;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.XmlReader;

class Door
{
	enum EPosition
	{
		TL,
		TM,
		TR,
		
		LT,
		LM,
		LB,
		
		BL,
		BM,
		BR,
		
		RT,
		RM,
		RB
	}
	
	Room m_pToRoom;
	EPosition m_ePosition;
	String m_sID;
	String m_sToID;
}

public class Room
{
	Texture m_pBackground;
	
	List<Door> m_lpDoors = new ArrayList<Door>();
	NodeContainer m_pNodes = new NodeContainer();
	
	String m_sID;
	
	Room(String i_sID)
	{
		m_sID = i_sID;
	}
	
	void parse(XmlReader.Element i_pElement)
	{
		for (int i = 0; i < i_pElement.getChildCount(); ++i)
		{
			XmlReader.Element pChild = i_pElement.getChild(i);
			String sChildName = pChild.getName();
			
			if(sChildName.equals("n")) // Node
			{
				// Attributes
				String sID = pChild.getAttribute("id");
				String sGridPosX = pChild.getAttribute("gridposx");
				String sGridPosY = pChild.getAttribute("gridposy");

				int iGridPosX = Integer.parseInt(sGridPosX);
				int iGridPosY = Integer.parseInt(sGridPosY);
				
				Node pNode = new Node(sID, GridGuide.get7x7(iGridPosX, iGridPosY));
				m_pNodes.m_lpNodes.add(pNode);
				pNode.parse(pChild);
			}
			else if(sChildName.equals("d")) // TODO Door
			{
				Door pDoor = new Door();
				pDoor.m_sID = pChild.getAttribute("id");
				pDoor.m_sToID = pChild.getAttribute("to");
				pDoor.m_ePosition = Door.EPosition.fromString(pChild.getAttribute("pos"));
				m_lpDoors.add(pDoor);
			}
			else if(sChildName.equals("npc")) // TODO NPC
			{
				
			}
			else if(sChildName.equals("melee")) // TODO Melee enemy
			{
				
			}
		}
		
//		// Create nodes
//		Node pNode0 = new Node(GridGuide.get7x7(3, 0));
//		Node pNode1 = new Node(GridGuide.get7x7(3, 3));
//		Node pNode2 = new Node(GridGuide.get7x7(3, 6));
//
//		pNode0.m_lpNeighbors.add(pNode1);
//		pNode1.m_lpNeighbors.add(pNode0);
//		
//		pNode1.m_lpNeighbors.add(pNode2);
//		pNode2.m_lpNeighbors.add(pNode1);
//		
//		m_pNodes.m_lpNodes.add(pNode0);
//		m_pNodes.m_lpNodes.add(pNode1);
//		m_pNodes.m_lpNodes.add(pNode2);
	}
	
	void render(SpriteBatch i_pBatch, ShapeRenderer i_pShapeRenderer, Resources i_pResources)
	{
		// BG
		i_pBatch.draw(m_pBackground, 135.f - m_pBackground.getWidth() * 0.5f, 128);
		
		// Node connections
		i_pBatch.end();

		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);	
		i_pShapeRenderer.begin(ShapeType.Filled);
		{
			m_pNodes.renderConnections(i_pShapeRenderer);
		}
		i_pShapeRenderer.end();
		Gdx.gl.glDisable(GL20.GL_BLEND);
		i_pBatch.begin();
		
		// Nodes
		m_pNodes.render(i_pBatch, i_pResources);
	}
}
