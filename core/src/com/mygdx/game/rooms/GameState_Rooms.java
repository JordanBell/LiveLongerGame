package com.mygdx.game.rooms;

import java.io.IOException;
import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.mygdx.game.GameState;
import com.mygdx.game.InputButton;
import com.mygdx.game.LiveLongerGame;
import com.mygdx.game.Resources;
import com.mygdx.game.SaveData;
import com.mygdx.game.SaveData.EProgress;
import com.mygdx.game.util.BlendTimer;
import com.mygdx.game.util.BlendTimer.EFadeType;
import com.mygdx.game.util.Config;

public class GameState_Rooms extends GameState
{
	enum ETransition
	{
		In, // Into a new room
		Out, // Out of an old room
		None, // Not transitioning
	}
	
	ETransition m_eTransition = ETransition.None;
	
	Level m_pCurrentLevel;
	Room m_pCurrentRoom;
	Crawler m_pPlayer;
	Camera m_pCamera = null;
	
	float m_fTransitionBlackness = 0.f;
	boolean m_bIsEnded = false;
	
	ArrayList<String> m_lsInventory = new ArrayList<String>();
	
	// Dialogue stuff
	boolean m_bDialogueActive = false;
	TextureRegion m_pDialogueChathead = null;
	Label m_pLabelDialogue;

	InputButton m_pButtonLeft = null;
	InputButton m_pButtonRight = null;
	InputButton m_pButtonMid = null;
	
	private static ArrayList<String> m_lsEventQueue = new ArrayList<String>();

	NPC m_pDialogueNPC;
	
	BlendTimer m_pDialogueShutterTimer = new BlendTimer(0.f, 0.25f) { public void onOut()	{ onDialogueFinish(); }};
	
	@Override
	public void create(Camera i_pCamera)
	{
		m_pPlayer = new Crawler();
		m_pCamera = i_pCamera;
		
		// Create all rooms
		try
		{
			LevelLoader.parseFile("data/level_find_him.xml");
			LevelLoader.parseFile("data/level_heal_her.xml");
			LevelLoader.parseFile("data/level_market.xml");
			LevelLoader.parseFile("data/level_gem2.xml");
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		// Create player
		m_pPlayer.m_pGraphics = new AnimHero();
		
		// UI stuff
		m_pButtonLeft = new InputButton(48, 24, 64, 64) { public void onRelease() { onDialogueLeft(); } };
		m_pButtonRight = new InputButton(270 - (48 + 64), 24, 64, 64) { public void onRelease() { onDialogueRight(); } };
		m_pButtonMid = new InputButton(135 - 32, 24, 64, 64) { public void onRelease() { onDialogueMid(); } };
		
		m_pLabelDialogue = new Label("", Resources.m_pStyleRoomDialogue);
		m_pLabelDialogue.setAlignment(Align.topLeft);
		m_pLabelDialogue.setEllipsis(false);
		m_pLabelDialogue.setWrap(true);
		m_pLabelDialogue.setX(86.f);
		m_pLabelDialogue.setY(480.f - 16.f);
		m_pLabelDialogue.setWidth(170.f);
	}
	
	public void start()
	{
		start(LevelLoader.getLevelByID(SaveData.m_eProgress.getLevel()));
	}
	
	public void start(Level i_pLevel)
	{
		if(i_pLevel == null)
		{
			throw new RuntimeException("Null level; perhaps its file has not been parsed into the GameState_Rooms file?");
		}
		
		m_bIsEnded = false;
		m_pCurrentLevel = i_pLevel;
		m_pCurrentRoom = m_pCurrentLevel.m_pStartRoom;
		m_pPlayer.m_pNode = m_pCurrentLevel.m_pStartNode;
		m_pPlayer.m_pToDoor = null;
		m_pPlayer.m_bTransitionActive = false;
	}
	
	void onTransitionEnd() {}
	
	void onTransitionOpaque()
	{
		// Teleport player to new room and position in that room
		m_pCurrentRoom = m_pPlayer.m_pToDoor.m_pToRoom;
		m_pPlayer.m_pNode = m_pPlayer.m_pToDoor.m_pToNode;
		m_pPlayer.m_pToDoor = null;
		m_pPlayer.m_bTransitionActive = false;
	}
	
	void updateTransition()
	{
		if(m_bIsEnded) return;
		
		// Transition state management
		if(m_eTransition != ETransition.None)
		{
			final float k_fTransitionSpeed = 0.02f;
			if(m_eTransition == ETransition.Out)
			{
				m_fTransitionBlackness += Config.m_bTurbo ? k_fTransitionSpeed * 10.f : k_fTransitionSpeed;
				if(m_fTransitionBlackness >= 1.f)
				{
					m_fTransitionBlackness = 1.f;
					m_eTransition = ETransition.In;
					onTransitionOpaque();
				}
			}
			else if(m_eTransition == ETransition.In)
			{
				m_fTransitionBlackness -= Config.m_bTurbo ? k_fTransitionSpeed * 10.f : k_fTransitionSpeed;
				if(m_fTransitionBlackness <= 0.f)
				{
					m_fTransitionBlackness = 0.f;
					m_eTransition = ETransition.None;
					onTransitionEnd();
				}
			}
		}
		else if(m_pPlayer.m_bTransitionActive)
		{
			if(m_pPlayer.m_pToDoor.m_bIsEndOfLevel)
			{
				m_bIsEnded = true;
				// Start transition into next game state
				loadMeta(EProgress.values()[SaveData.m_eProgress.ordinal() + 1]);
			}
			else
			{
				// Start a transition into the next room
				m_eTransition = ETransition.Out;
			}
			
			// End all dialogue
			onDialogueFinish();
		}
	}
	
	void handleEvent(String i_sEventID)
	{
		if(i_sEventID.equals("vial_for_key"))
		{
			SaveData.m_lsItems.remove("vial");
			SaveData.m_lsItems.add("key");
		}
		else if(i_sEventID.equals("key_to_owner"))
		{
			SaveData.m_lsItems.remove("key");
			SaveData.m_lsItems.add("key_karma");
		}
		else if(i_sEventID.startsWith("itemget_"))
		{
			String sItemName = i_sEventID.substring(8);
			
			// Handle certain items differently
			if(sItemName.equals("medicine"))
			{
				// End the level; start Meta 2
				loadMeta(EProgress.Meta2);
			}
			else if(sItemName.equals("gem"))
			{
				// Notify all NPCs of an acquired gem
				for(Room pRoom : m_pCurrentLevel.m_lpRooms)
				{
					for(NPC pNPC : pRoom.m_lpNPCs)
					{
						pNPC.onGemGrab();
					}
				}
			}
			else
			{
				// Add the item to the inventory
				SaveData.m_lsItems.add(sItemName);
			}
		}
	}
	
	void loadMeta(EProgress i_eProgress)
	{
		LiveLongerGame.s_bTransitionState = true;
		SaveData.setProgress(i_eProgress);
		SaveData.save();
	}

	@Override
	public void render(SpriteBatch i_pBatch, ShapeRenderer i_pShapeRenderer)
	{
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		// Handle all events since the last render
		for(String sEventID : m_lsEventQueue)
		{
			handleEvent(sEventID);
		}
		m_lsEventQueue.clear();
		
		if(!m_bIsEnded)
		{
			m_pCurrentRoom.update(m_pPlayer);
		}
		
		i_pBatch.begin();
		{
			// Render dialogue UI
			if(m_pDialogueNPC != null && !m_pDialogueNPC.m_bCanTalk && !m_pDialogueShutterTimer.isBlending())
			{
				endDialogue();
			}
			
			if(m_bDialogueActive)
			{
				i_pBatch.draw(Resources.m_pDialogueBorder, 0, 480 - 87);
				i_pBatch.draw(m_pDialogueChathead, 12, 480 - 76);
				
				// Draw text
				m_pLabelDialogue.draw(i_pBatch, 1.f);

				// Draw choice buttons
				if(m_pButtonLeft.m_pSpriteSheetStates != null && m_pButtonRight.m_pSpriteSheetStates != null)
				{
					m_pButtonLeft.draw(i_pBatch);
					m_pButtonRight.draw(i_pBatch);
				}
				
				if(m_pButtonMid.m_pSpriteSheetStates != null)
				{
					m_pButtonMid.draw(i_pBatch);
				}
				
				// Draw the reveal shutter over everything
				i_pBatch.draw(Resources.m_pDialogueShutter, m_pDialogueShutterTimer.get() * 440 - 100, 480 - 87);
				i_pBatch.draw(Resources.m_pDialogueShutter, m_pDialogueShutterTimer.get() * 440 - 100, 0);
				
				// Finally, update the shutter timer
				m_pDialogueShutterTimer.update();
			}
			
			// Room
			m_pCurrentRoom.render(i_pBatch, i_pShapeRenderer, m_pPlayer);
		}
		i_pBatch.end();
		
		updateTransition();
		
		if(m_fTransitionBlackness > 0.f)
		{
			Gdx.gl.glEnable(GL20.GL_BLEND);
			Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);	
			{
				i_pShapeRenderer.begin(ShapeType.Filled);
				i_pShapeRenderer.setColor(new Color(0.f, 0.f, 0.f, m_fTransitionBlackness));
				i_pShapeRenderer.rect(0.f, 0.f, 270.f, 480.f);
				i_pShapeRenderer.end();
			}
			Gdx.gl.glDisable(GL20.GL_BLEND);
		}
	}
	public boolean keyUp(int keycode) { return false; }
	public boolean keyDown(int keycode) { return false; }
	public boolean keyTyped(char character) { return false; }
	public boolean touchDragged(int screenX, int screenY, int pointer) { return false; }
	public boolean mouseMoved(int screenX, int screenY) { return false; }
	public boolean scrolled(int amount) { return false; }

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button)
	{
		if(!m_bActive) return false;
		
		// Unproject the touch coordinates
		Vector3 vTouch = new Vector3(screenX, screenY, 0);
		m_pCamera.unproject(vTouch);
		screenX = (int) vTouch.x;
		screenY = (int) vTouch.y;
		
		boolean r_b = false;
		
		if(m_bDialogueActive)
		{
			if(m_pButtonLeft.m_pSpriteSheetStates != null && m_pButtonRight.m_pSpriteSheetStates != null)
			{
				r_b |= m_pButtonLeft.touchDown(screenX, screenY, pointer, button);
				r_b |= m_pButtonRight.touchDown(screenX, screenY, pointer, button);
			}
			
			if(m_pButtonMid.m_pSpriteSheetStates != null)
			{
				r_b	|= m_pButtonMid.touchDown(screenX, screenY, pointer, button); 
			}
		}
		
		return r_b;
	}
	
	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) 
	{ 
		if(!m_bActive) return false;
		
		// Unproject the touch coordinates
		Vector3 vTouch = new Vector3(screenX, screenY, 0);
		m_pCamera.unproject(vTouch);
		screenX = (int) vTouch.x;
		screenY = (int) vTouch.y;
		
		// Check if touched NPC
		NPC pTouchedNPC = m_pCurrentRoom.getNPCInteraction(screenX, screenY);
		if(pTouchedNPC != null)
		{
			setDialogue(pTouchedNPC);
			return true;
		}
		
		// Check if touched node or door
		if(m_pPlayer.m_pNode != null)
		{
			// Move to tapped node
			Node pToNode = m_pCurrentRoom.m_pNodes.getNodeByPosition(screenX, screenY);
			if(pToNode != null && pToNode != m_pPlayer.m_pToNode)
			{
				if(pToNode != m_pPlayer.m_pNode)
				{
					// Move to it if it's not our current node
					m_pPlayer.goToNode(pToNode, m_pCurrentRoom.m_pNodes);
				}
				else
				{
					// Check if rope action
					if(pToNode.m_pRopeConnection != null && !pToNode.m_pRopeConnection.m_bRopeAdded)
					{
						pToNode.useRope(m_pCurrentRoom.m_pNodes);
					}
				}
				return true;
			}
			
			Door pToDoor = m_pCurrentRoom.getDoorByPosition(screenX, screenY);
			if(pToDoor != null)
			{
				m_pPlayer.goToDoor(pToDoor, m_pCurrentRoom.m_pNodes);	
				return true;
			}
		}
		
		boolean r_b = false;
		
		if(m_bDialogueActive)
		{
			if(m_pButtonLeft.m_pSpriteSheetStates != null && m_pButtonRight.m_pSpriteSheetStates != null)
			{
				r_b |= m_pButtonLeft.touchUp(screenX, screenY, pointer, button);
				r_b |= m_pButtonRight.touchUp(screenX, screenY, pointer, button);
			}
			
			if(m_pButtonMid.m_pSpriteSheetStates != null)
			{
				r_b	|= m_pButtonMid.touchUp(screenX, screenY, pointer, button); 
			}
		}
		
		return r_b;
	}
	
	void onDialogueChanged()
	{
		DialogueNode pCurrentDialogue = m_pDialogueNPC.m_lpDialogue.get(m_pDialogueNPC.m_iDialogueIndex);
		m_pLabelDialogue.setText(pCurrentDialogue.m_sText);
		if(pCurrentDialogue.m_pLeftOpt == null || pCurrentDialogue.m_pRightOpt == null)
		{
			m_pButtonLeft.m_pSpriteSheetStates = null;
			m_pButtonRight.m_pSpriteSheetStates = null;
		}
		else
		{
			m_pButtonLeft.m_pSpriteSheetStates = pCurrentDialogue.m_pLeftOpt.m_eType.getSheet();
			m_pButtonRight.m_pSpriteSheetStates = pCurrentDialogue.m_pRightOpt.m_eType.getSheet();
		}
		
		if(pCurrentDialogue.m_pMidOpt == null)
		{
			m_pButtonMid.m_pSpriteSheetStates = null;
		}
		else
		{
			m_pButtonMid.m_pSpriteSheetStates = pCurrentDialogue.m_pMidOpt.m_eType.getSheet();
		}
	}
	
	void setDialogue(NPC i_pNPC)
	{
		if(m_pDialogueNPC == i_pNPC) return;
		
		m_bDialogueActive = true;
		m_pDialogueChathead = i_pNPC.getChathead();
		m_pDialogueShutterTimer.start(EFadeType.In);
		m_pDialogueNPC = i_pNPC;
		m_pDialogueNPC.onStartDialogue();
		for(NPC pNPC : m_pCurrentRoom.m_lpNPCs) pNPC.m_bHideBubble = true;
		onDialogueChanged();
	}
	
	void onDialogueLeft()
	{
		m_pDialogueNPC.onDialogueLeft(this);
	}
	
	void onDialogueRight()
	{
		m_pDialogueNPC.onDialogueRight(this);
	}
	
	void onDialogueMid()
	{
		m_pDialogueNPC.onDialogueMid(this);
	}
	
	void onDialogueFinish()
	{
		for(NPC pNPC : m_pCurrentRoom.m_lpNPCs) pNPC.m_bHideBubble = false;
		m_pDialogueNPC = null;
		m_bDialogueActive = false;
		m_pDialogueChathead = null;
	}
	
	void endDialogue()
	{
		m_pDialogueShutterTimer.start(EFadeType.Out);
	}

	public static void onEvent(String text)
	{
		System.out.println("Event: " + text);
		m_lsEventQueue.add(text);	
	}
}
