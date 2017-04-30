package com.mygdx.game.rooms;

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
import com.mygdx.game.Resources;
import com.mygdx.game.rooms.Door.EPosition;
import com.mygdx.game.rooms.NPC.EType;
public class Room
{
	List<Door> m_lpDoors = new ArrayList<Door>();
	NodeContainer m_pNodes = new NodeContainer();
	List<NPC> m_lpNPCs = new ArrayList<NPC>();
	Texture m_pForeground = null;
	
	String m_sID;

	Room(String i_sID)
	{
		m_sID = i_sID;
		
		float fAnimTime = 0.025f;
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
	
	NPC getNPCInteraction(float i_fX, float i_fY)
	{
		for(NPC pNPC : m_lpNPCs)
		{
			if(!pNPC.m_bCanTalk) continue;
			
			final float fDist = pNPC.m_pGraphics.getCenter().add(pNPC.m_vPosition).dst(i_fX, i_fY);
			if(fDist < 32.f)
			{
				return pNPC;
			}
		}
		
		return null;
	}
	
	void parse(XmlReader.Element i_pElement)
	{
		String sForegroundFilepath = i_pElement.getAttribute("foreground", null);
		if(sForegroundFilepath != null)
		{
			m_pForeground = new Texture("room/foregrounds/" + sForegroundFilepath);
		}
		
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
				pDoor.m_sLock = pChild.getAttribute("lock", null);
				pDoor.m_ePosition = Door.EPosition.fromString(pChild.getAttribute("pos"));
				m_lpDoors.add(pDoor);
			}
			else if(sChildName.equals("npc")) // TODO NPC
			{
				NPC pNPC = new NPC(EType.fromString(pChild.getAttribute("type", "adult")), pChild.getIntAttribute("variant", -1));
				
				// Set position
				final String sGridPosX = pChild.getAttribute("gridposx");
				final String sGridPosY = pChild.getAttribute("gridposy");

				final int iGridPosX = Integer.parseInt(sGridPosX);
				final int iGridPosY = Integer.parseInt(sGridPosY);
				
				pNPC.m_vPosition = GridGuide.get7x7(iGridPosX, iGridPosY);
				pNPC.m_vPosition.x += pChild.getIntAttribute("offsetX", 0);
				pNPC.m_vPosition.y += pChild.getIntAttribute("offsetY", 0);
				
				final String sMsg = pChild.getAttribute("msg", "");
				if(!sMsg.isEmpty())
				{
					DialogueNode pDialogueNode = new DialogueNode(null);
					pDialogueNode.m_sText = sMsg;
					pNPC.m_lpDialogue.add(pDialogueNode);
					
					// Add OK for default dialogue (no responses given)
					pDialogueNode.m_pMidOpt = new DialogueOption(DialogueOption.EType.Okay, null);
				}
				
				for (int iMsgIndex = 0; iMsgIndex < pChild.getChildCount(); ++iMsgIndex)
				{
					XmlReader.Element pMsgChild = pChild.getChild(iMsgIndex);
					String sMsgChildName = pMsgChild.getName();
					if(sMsgChildName.equals("msg"))
					{
						String sChildText = pMsgChild.getText();
						
						DialogueNode pDialogueNode = new DialogueNode(pMsgChild.getAttribute("id", null));
						
						if(sChildText != null && sChildText != "")
						{
							pDialogueNode.m_sText = pMsgChild.getText();
							
							// Search for option children
							XmlReader.Element pNodeOptionLeft = pMsgChild.getChildByName("left");
							XmlReader.Element pNodeOptionRight = pMsgChild.getChildByName("right");
							
							if(pNodeOptionLeft != null && pNodeOptionRight != null)
							{
								pDialogueNode.m_pLeftOpt = new DialogueOption(pNodeOptionLeft);
								pDialogueNode.m_pRightOpt = new DialogueOption(pNodeOptionRight);
							}
							else
							{
								// If no l/r options, use the mid by default
								String sNextNodeID = pMsgChild.getAttribute("next", null);
								pDialogueNode.m_pMidOpt = new DialogueOption(DialogueOption.EType.Okay, sNextNodeID);
							}
							
							// Check if an event should be thrown when this message occurs
							XmlReader.Element pNodeEvent = pMsgChild.getChildByName("event");
							if(pNodeEvent != null)
							{
								pDialogueNode.m_sEventID = pNodeEvent.getText();
							}
						}
						else
						{
							// Not text. Check for a condition.
							pDialogueNode.m_pCondition = new DialogueCondition(pMsgChild.getChildByName("condition"));
						}
						
						pNPC.m_lpDialogue.add(pDialogueNode);
					}
				}
				
				m_lpNPCs.add(pNPC);
			}
			else if(sChildName.equals("melee")) // TODO Melee enemy
			{
				
			}
		}
	}
	
	void render(SpriteBatch i_pBatch, ShapeRenderer i_pShapeRenderer, Crawler i_pPlayer)
	{
		// BG
		i_pBatch.draw(Resources.m_pBackground, 135.f - Resources.m_pBackground.getWidth() * 0.5f, 110);
		
		if(m_pForeground != null)
		{
			// Render foreground in centre of screen
			i_pBatch.draw(m_pForeground, 0, 0);
		}
		
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
		
		// Render scene objects
		m_pNodes.render(i_pBatch);
		for(Door pDoor : m_lpDoors) pDoor.draw(i_pBatch);
		for(NPC pNPC : m_lpNPCs) pNPC.draw(i_pBatch);
		i_pPlayer.render(i_pBatch);
		for(NPC pNPC : m_lpNPCs) pNPC.drawBubble(i_pBatch);
	}
	
	void updateNPCs(Crawler i_pPlayer)
	{
		for(NPC pNPC : m_lpNPCs)
		{
			pNPC.m_bCanTalk = i_pPlayer.m_vPosition.dst(pNPC.m_vPosition) <= 32.f;
		}
	}

	public void update(Crawler i_pPlayer)
	{
		if(i_pPlayer == null) return;
		
		// Update NPC talk availability
		updateNPCs(i_pPlayer);
	}
}
