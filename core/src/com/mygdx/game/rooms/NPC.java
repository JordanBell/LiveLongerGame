package com.mygdx.game.rooms;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.XmlReader;
import com.mygdx.game.Resources;
import com.mygdx.game.SaveData;
import com.mygdx.game.SpriteSheetGDX;

class DialogueOption
{
	enum EType
	{
		Okay,
		Yes,
		No,
		
		Error;
		
		static public EType fromString(String i_s)
		{
			if(i_s.equalsIgnoreCase("ok"))
			{
				return Okay;
			}
			if(i_s.equalsIgnoreCase("y"))
			{
				return Yes;
			}
			if(i_s.equalsIgnoreCase("n"))
			{
				return No;
			}
			return Error;
		}
		
		public SpriteSheetGDX getSheet()
		{
			switch(this)
			{
				case Yes:
					return Resources.m_pButtonRoom_Yes;
				case No:
					return Resources.m_pButtonRoom_No;
				case Okay:
					return Resources.m_pButtonRoom_Okay;
				default:
					return null;
			}
		}
	}
	
	EType m_eType;
	String m_sToDialogueNode;
	
	DialogueOption(EType i_eType, String i_sToDialogueNode)
	{
		m_eType = i_eType;
		m_sToDialogueNode = i_sToDialogueNode;
	}
	
	DialogueOption(XmlReader.Element i_pNode)
	{
		m_eType = DialogueOption.EType.fromString(i_pNode.getAttribute("type", "y"));
		m_sToDialogueNode = i_pNode.getAttribute("to", null);
	}
}

class DialogueCondition
{
	String m_sItemID;
	String m_sToIfFalse;
	String m_sToIfTrue;
	
	DialogueCondition(XmlReader.Element i_pNode)
	{
		m_sItemID = i_pNode.getAttribute("item");
		m_sToIfTrue = i_pNode.getChildByName("true").getText();
		m_sToIfFalse = i_pNode.getChildByName("false").getText();
	}
};

class DialogueNode
{
	String m_sID = "unnamed";
	
	// Either contains a condition or a piece of text + options
	DialogueCondition m_pCondition;
	
	String m_sText;
	DialogueOption m_pLeftOpt;
	DialogueOption m_pRightOpt;
	DialogueOption m_pMidOpt;

	String m_sEventID = null;
	
	public DialogueNode(String i_sID)
	{
		if(i_sID == null) i_sID = "unnamed";
		m_sID = i_sID;
		m_pCondition = null;
		m_sText = null;
		m_pLeftOpt = null;
		m_pRightOpt = null;
		m_pMidOpt = null;
	}
}

public class NPC
{
	enum EType
	{
		Adult,
		Child,
		
		Error;
		
		public static EType fromString(String i_s)
		{
			if(i_s.equalsIgnoreCase("adult"))
			{
				return Adult;
			}
			else if(i_s.equalsIgnoreCase("child"))
			{
				return Child;
			}
			
			return Error;
		}
	}
	
	GraphicsComponent m_pGraphics = null;
	List<DialogueNode> m_lpDialogue = new ArrayList<DialogueNode>();
	Vector2 m_vPosition;
	public boolean m_bCanTalk = false;
	boolean m_bHideBubble = false;
	
	float m_fBubbleTimer = 0.f;
	static final float sk_fBubbleTime = 0.5f;
	EType m_eType;
	int m_iColorType = -1;
	
	int m_iDialogueIndex = 0;
	
	public NPC(EType i_eType, int i_iColorType)
	{
		if(i_iColorType < 0)
		{
			// If color type is negative, it is randomised
			i_iColorType = (int)(Math.random() * getColorVariationCountFor(i_eType));
		}
		
		m_iColorType = i_iColorType;
		m_eType = i_eType;
		m_pGraphics = getGraphicsComponentFor(i_eType);
	}
	
	int getColorVariationCountFor(EType i_eType)
	{
		switch(i_eType)
		{
			case Adult:
			{
				return AnimNPCAdult.sk_iNumColors;
			}
			case Child:
			{
				return AnimNPCChild.sk_iNumColors;
			}
			case Error:
				throw new RuntimeException("Type error.");
			default:
				throw new RuntimeException("Unknown type.");
		}
	}
	
	GraphicsComponent getGraphicsComponentFor(EType i_eType)
	{
		switch(i_eType)
		{
			case Adult:
			{
				return new AnimNPCAdult(m_iColorType);
			}
			case Child:
			{
				return new AnimNPCChild(m_iColorType);
			}
			case Error:
				throw new RuntimeException("Type error.");
			default:
				throw new RuntimeException("Unknown type.");
		}
	}
	
	public void draw(SpriteBatch i_pBatch)
	{
		Vector2 vRenderPos = m_vPosition.cpy();
		vRenderPos.x -= m_pGraphics.getWidth() * 0.5f;
		m_pGraphics.update();
		m_pGraphics.draw(i_pBatch, vRenderPos.x, vRenderPos.y);
	}
	
	public void drawBubble(SpriteBatch i_pBatch)
	{
		if(!m_bHideBubble && m_bCanTalk)
		{
			Vector2 vBubblePos = m_vPosition.cpy().add(m_pGraphics.getCenter());
			vBubblePos.x -= (m_pGraphics.getWidth() * 0.5f) + (Resources.m_pNPCBubble.getWidth() * 0.5f);
			vBubblePos.y += (m_pGraphics.getHeight() * 0.5f) + 3.f;
			
			// Bob the bubble up and down
			m_fBubbleTimer += Gdx.graphics.getDeltaTime();
			if(m_fBubbleTimer > 2.f * sk_fBubbleTime)
			{
				m_fBubbleTimer -= 2.f * sk_fBubbleTime;
			}
			else if(m_fBubbleTimer > sk_fBubbleTime)
			{
				++vBubblePos.y;
			}
			
			// Draw the bubble
			i_pBatch.draw(Resources.m_pNPCBubble, (int)vBubblePos.x, (int)vBubblePos.y);
		}
	}
	
	void nextDialogue(GameState_Rooms i_pGameState)
	{
		if(++m_iDialogueIndex >= m_lpDialogue.size())
		{
			--m_iDialogueIndex;
			i_pGameState.endDialogue();
		}
		
		i_pGameState.onDialogueChanged();
	}
	
	void setDialogueByID(String i_sID)
	{
		for(int i = 0; i < m_lpDialogue.size(); ++i)
		{
			DialogueNode pDialogueNode = m_lpDialogue.get(i);
			if(pDialogueNode.m_sID != null && pDialogueNode.m_sID.equals(i_sID))
			{
				if(pDialogueNode.m_pCondition != null)
				{
					// If this node is a condition node, check the condition and set the dialogue to be the one that result points to
					final boolean bItemExists = SaveData.m_lsItems.contains(pDialogueNode.m_pCondition.m_sItemID);
					setDialogueByID(bItemExists ? pDialogueNode.m_pCondition.m_sToIfTrue : pDialogueNode.m_pCondition.m_sToIfFalse);
				}
				else
				{
					// Set the dialogue as this one 
					m_iDialogueIndex = i;

					if(pDialogueNode.m_sEventID != null)
					{
						GameState_Rooms.onEvent(pDialogueNode.m_sEventID);
						pDialogueNode.m_sEventID = null;
					}
				}
				return;
			}
		}
		
		throw new RuntimeException("No dialogue option with ID: " + i_sID);
	}
	
	void onDialogueOption(DialogueOption i_pOption, GameState_Rooms i_pGameState)
	{
		if(i_pOption == null)
		{
			throw new RuntimeException("Somenow clicked an option when none was available");
		}
		
		// If this leads to a new node, go there. Otherwise end the dialogue
		DialogueNode pDNode = m_lpDialogue.get(m_iDialogueIndex);
		if(i_pOption.m_sToDialogueNode == null)
		{
			i_pGameState.endDialogue();
		}
		else
		{
			setDialogueByID(i_pOption.m_sToDialogueNode);
			
			i_pGameState.onDialogueChanged();
		}
	}
	
	void onDialogueLeft(GameState_Rooms i_pGameState)
	{
		onDialogueOption(m_lpDialogue.get(m_iDialogueIndex).m_pLeftOpt, i_pGameState);
	}
	
	void onDialogueRight(GameState_Rooms i_pGameState)
	{
		onDialogueOption(m_lpDialogue.get(m_iDialogueIndex).m_pRightOpt, i_pGameState);
	}
	
	void onDialogueMid(GameState_Rooms i_pGameState)
	{
		onDialogueOption(m_lpDialogue.get(m_iDialogueIndex).m_pMidOpt, i_pGameState);
	}

	public void onStartDialogue()
	{
		m_iDialogueIndex = 0;
		m_bHideBubble = true;
		setDialogueByID(m_lpDialogue.get(0).m_sID);
	}
}
