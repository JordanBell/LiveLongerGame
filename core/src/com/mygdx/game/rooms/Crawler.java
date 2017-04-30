package com.mygdx.game.rooms;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.SaveData;
import com.mygdx.game.SpriteSheetGDX;
import com.mygdx.game.util.Config;

public class Crawler
{
	Node m_pNode = null;
	
	// Transition data
	Vector2 m_vPosition = new Vector2();
	Node m_pToNode = null;
	static final float sk_fTransitionSpeed = 2.f;
	
	// Transitions between rooms
	Door m_pToDoor = null;
	boolean m_bTransitionActive = false;
	
	// Anim/render data
	GraphicsComponent m_pGraphics = null;
	
	public Crawler()
	{
	}
	
	void goToNode(Node i_pToNode)
	{
		if(i_pToNode == null || i_pToNode == m_pToNode)
		{
			return;
		}
		
		m_pToNode = i_pToNode;
		m_vPosition = m_pNode.m_vPos.cpy(); // Set a node-independant position
		m_pNode = null; // Detach ourselves
	}
	
	void goToDoor(Door i_pToDoor)
	{
		if(i_pToDoor == null || i_pToDoor == m_pToDoor)
		{
			return;
		}
		
		m_pToDoor = i_pToDoor;
		m_vPosition = m_pNode.m_vPos.cpy(); // Set a node-independant position
		m_pNode = null; // Detach ourselves
	}
	
	void updateTravel()
	{
		if(m_bTransitionActive)
		{
			return;
		}
		
		if(m_pToNode != null)
		{
			// Check if we've reached our destination
			if(m_vPosition.dst(m_pToNode.m_vPos) <= 1.f)
			{
				m_pNode = m_pToNode;
				m_pToNode = null;
				if(m_pNode.m_pItem != null)
				{
					// Add the item to the inventory
					SaveData.m_lsItems.add(m_pNode.m_pItem.m_eType.toString());
					
					// Remove the item
					m_pNode.m_pItem = null;
				}
			}
		}
		else if (m_pToDoor != null)
		{
			// Check if we've reached our destination
			if(m_vPosition.dst(m_pToDoor.m_ePosition.toPositionCenter()) <= 1.f)
			{
				m_bTransitionActive = true;
			}
		}
		
		// Update position
		if(m_pNode != null)
		{
			m_vPosition = m_pNode.m_vPos;
		}
		else if(m_pToNode != null)
		{
			if(Config.m_bTurbo)
			{
				m_vPosition = m_pToNode.m_vPos;
			}
			else
			{
				Vector2 vDirection = m_pToNode.m_vPos.cpy().sub(m_vPosition).nor();
				m_vPosition.mulAdd(vDirection, sk_fTransitionSpeed);
			}
		}
		else if(m_pToDoor != null)
		{
			if(Config.m_bTurbo)
			{
				m_vPosition = m_pToDoor.m_ePosition.toPositionCenter();
			}
			else
			{
				Vector2 vDirection = m_pToDoor.m_ePosition.toPositionCenter().sub(m_vPosition).nor();
				m_vPosition.mulAdd(vDirection, Config.m_bTurbo ? sk_fTransitionSpeed * 10.f : sk_fTransitionSpeed);
			}
		}
		else
		{
			// Error state
			throw new RuntimeException("Error movement state");
		}
	}
	
	void render(SpriteBatch i_pBatch)
	{
		if(m_pGraphics == null)
		{
			System.err.println("Sprite sheet or position not set.");
			assert(false);
			return;
		}
		
		updateTravel();
		m_pGraphics.update();
		
		// Draw the crawler
		m_pGraphics.draw(i_pBatch, m_vPosition.x - (m_pGraphics.getWidth() * 0.5f), m_vPosition.y);
	}
}
