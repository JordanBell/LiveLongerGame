package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class Crawler
{
	Node m_pNode = null;
	
	// Transition data
	Vector2 m_vPosition = null;
	Node m_pToNode = null;
	static final float sk_fTransitionSpeed = 2.f;
	
	// Anim/render data
	SpriteSheetGDX m_pSpriteSheet = null;
	int m_iAnimIndex = 0;
	
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
	
	void updateTravel()
	{
		if(m_pToNode != null)
		{
			// Check if we've reached our destination
			if(m_vPosition.dst(m_pToNode.m_vPos) <= 1.f)
			{
				m_pNode = m_pToNode;
				m_pToNode = null;
			}
		}
		
		// Update position
		if(m_pNode != null)
		{
			m_vPosition = m_pNode.m_vPos;
		}
		else if(m_pToNode != null)
		{
			Vector2 vDirection = m_pToNode.m_vPos.cpy().sub(m_vPosition).nor();
			m_vPosition.mulAdd(vDirection, sk_fTransitionSpeed);
		}
		else
		{
			// Error state
			assert(false);
		}
	}
	
	void render(SpriteBatch i_pBatch)
	{
		if(m_pSpriteSheet == null)
		{
			System.err.println("Sprite sheet or position not set.");
			assert(false);
			return;
		}
		
		updateTravel();
		
		// Draw the crawler
		TextureRegion pDrawRegion = m_pSpriteSheet.getRegion(m_iAnimIndex); 
		i_pBatch.draw(pDrawRegion, m_vPosition.x - (pDrawRegion.getRegionWidth() * 0.5f), m_vPosition.y - (pDrawRegion.getRegionHeight() * 0.5f));
	}
}
