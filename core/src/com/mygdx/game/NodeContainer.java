package com.mygdx.game;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class NodeContainer
{
	List<Node> m_lpNodes = new ArrayList<Node>();
	
	Node getNodeByLocation(float i_fX, float i_fY)
	{
		for(Node pNode : m_lpNodes)
		{
			float fDist = pNode.m_vPos.dst(i_fX, i_fY);
			if(fDist < 16.f)
			{
				return pNode;
			}
		}
		
		return null;
	}
	
	void renderConnections(ShapeRenderer i_pShapeRenderer)
	{
		// Reset render flags
		for(Node pNode : m_lpNodes)
		{
			pNode.m_bConnectionsRendered = false;
		}
		
		i_pShapeRenderer.setColor(0.f, 0.521f, 0.569f, 0.1f);
		for(Node pNode : m_lpNodes)
		{
			// Draw all neighbor connections for this node
			for(Node pNeighbor : pNode.m_lpNeighbors)
			{
				// Only draw if neighbor has not already drawn
				if(!pNeighbor.m_bConnectionsRendered)
				{
					i_pShapeRenderer.rectLine(pNode.m_vPos, pNeighbor.m_vPos, 2.f);				
				}
			}
			
			// Flip flag for render drawing
			pNode.m_bConnectionsRendered = true;
		}
	}
	
	void render(SpriteBatch i_pBatch, Resources i_pResources)
	{
		Texture pNodeTexture = i_pResources.m_pNode;

		int iHW = pNodeTexture.getWidth() / 2;	// Half width
		int iHH = pNodeTexture.getHeight() / 2;	// Half height
		
		for(Node pNode : m_lpNodes)
		{
			i_pBatch.draw(pNodeTexture, pNode.m_vPos.x - iHW, pNode.m_vPos.y - iHH);
		}
	}
}
