package com.mygdx.game.rooms;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Resources;

public class NodeContainer
{
	List<Node> m_lpNodes = new ArrayList<Node>();
	
	Node getNodeByPosition(float i_fX, float i_fY)
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
	
	Node getNodeByID(String i_sID)
	{
		for(Node pNode : m_lpNodes)
		{
			if(pNode.m_sID.equals(i_sID))
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
			for(Connection pCon : pNode.m_lpConnections)
			{
				Node pNeighbor = getNodeByID(pCon.m_sToID);
				if(pNeighbor == null) continue;
				
				// Only draw if neighbor has not already drawn
				if(!pNeighbor.m_bConnectionsRendered)
				{
					// Draw brown if rope, blue otherwise
					if(pCon.m_bRopeRequired)
					{
						if(pCon.m_bRopeAdded)
						{
							i_pShapeRenderer.setColor(0.6f, 0.6f, 0.4f, 1.f);
							i_pShapeRenderer.rectLine(pNode.m_vPos, pNeighbor.m_vPos, 2.f);
						}
					}
					else
					{
						i_pShapeRenderer.setColor(0.f, 0.521f, 0.569f, 0.1f);
						i_pShapeRenderer.rectLine(pNode.m_vPos, pNeighbor.m_vPos, 2.f);				
					}
				}
			}
			
			// Draw connections through doors
			for(Door pDoor : pNode.m_lpNeighborDoors)
			{
				Vector2 vDoorPos = pDoor.m_ePosition.toPosition().cpy();
				Texture pDoorTexture = Resources.getDoor(pDoor.m_ePosition); 
				vDoorPos.x += pDoorTexture.getWidth() / 2;  // Adjust for w & h
				vDoorPos.y += pDoorTexture.getHeight() / 2;
				i_pShapeRenderer.rectLine(pNode.m_vPos, vDoorPos, 2.f);		
			}
			
			// Flip flag for render drawing
			pNode.m_bConnectionsRendered = true;
		}
	}
	
	void render(SpriteBatch i_pBatch)
	{
		Texture pNodeTexture = Resources.m_pNode;

		int iHW = pNodeTexture.getWidth() / 2;	// Half width
		int iHH = pNodeTexture.getHeight() / 2;	// Half height
		
		for(Node pNode : m_lpNodes)
		{
			i_pBatch.draw(pNodeTexture, pNode.m_vPos.x - iHW, pNode.m_vPos.y - iHH);
			
			// If the node can have a rope placed on it, draw that too
			if(pNode.m_pRopeConnection != null)
			{
				Texture pHookTexture = Resources.m_pHook;
				int iHHW = pHookTexture.getWidth() / 2;	// Half width
				i_pBatch.draw(Resources.m_pHook, (int)pNode.m_vPos.x - iHHW, (int)pNode.m_vPos.y + 1);
				
			}
			
			// If the node has an item, draw that too
			if(pNode.m_pItem != null)
			{
				pNode.m_pItem.draw(i_pBatch, (int)pNode.m_vPos.x, (int)pNode.m_vPos.y);
			}
		}
	}
}
