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

public class Room
{
	List<Door> m_lpDoors = new ArrayList<Door>();
	NodeContainer m_pNodes = new NodeContainer();
	
	String m_sID;
	
	Room(String i_sID)
	{
		m_sID = i_sID;
	}
	
	Door getDoorByID(String i_sID)
	{
		for(Door pDoor : m_lpDoors)
		{
			if(pDoor.m_sID.equals(i_sID))
			{
				return pDoor;
			}
		}
		
		return null;
	}
	
	Door getDoorByPosition(float i_fX, float i_fY)
	{
		for(Door pDoor : m_lpDoors)
		{
			float fDist = pDoor.m_ePosition.toPositionCenter().dst(i_fX, i_fY);
			if(fDist < 32.f)
			{
				return pDoor;
			}
		}
		
		return null;
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
			else if(sChildName.equals("d")) // Door
			{
				Door pDoor = new Door();
				pDoor.m_sID = pChild.getAttribute("id");
				pDoor.m_sToIDRoom = pChild.getAttribute("to");
				pDoor.m_sToIDNode = pChild.getAttribute("node");
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
	}
	
	void render(SpriteBatch i_pBatch, ShapeRenderer i_pShapeRenderer)
	{
		// BG
		i_pBatch.draw(Resources.m_pBackground, 135.f - Resources.m_pBackground.getWidth() * 0.5f, 110);
		
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
		
		// Render doors
		for(Door pDoor : m_lpDoors)
		{
			pDoor.draw(i_pBatch);
		}
		
		// Nodes
		m_pNodes.render(i_pBatch);
	}
}
