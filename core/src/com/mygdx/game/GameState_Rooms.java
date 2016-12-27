package com.mygdx.game;

import java.io.IOException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector3;

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
	Crawler m_pPlayer = new Crawler();
	Camera m_pCamera = null;
	
	float m_fTransitionBlackness = 0.f;
	
	@Override
	void create(Camera i_pCamera)
	{
		m_pCamera = i_pCamera;
		
		// Create all rooms
		try
		{
			LevelLoader.parseFile("data/level_heal_her.xml");
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		// Create player
		m_pPlayer.m_pSpriteSheet = Resources.m_pPlayer;
	}
	
	void start(Level i_pLevel)
	{
		if(i_pLevel == null)
		{
			throw new RuntimeException("Null level");
		}
		
		m_pCurrentLevel = i_pLevel;
		m_pCurrentRoom = m_pCurrentLevel.m_pStartRoom;
		m_pPlayer.m_pNode = m_pCurrentLevel.m_pStartNode;
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
			// Start a transition into the next room
			m_eTransition = ETransition.Out;
		}
	}

	@Override
	void render(SpriteBatch i_pBatch, ShapeRenderer i_pShapeRenderer)
	{
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		i_pBatch.begin();
		{
			// Room
			m_pCurrentRoom.render(i_pBatch, i_pShapeRenderer);
			m_pPlayer.render(i_pBatch);
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

	public boolean keyDown(int keycode) { return false; }
	public boolean keyUp(int keycode) { return false; }
	public boolean keyTyped(char character) { return false; }
	public boolean touchDown(int screenX, int screenY, int pointer, int button) { return false; }
	public boolean touchDragged(int screenX, int screenY, int pointer) { return false; }
	public boolean mouseMoved(int screenX, int screenY) { return false; }
	public boolean scrolled(int amount) { return false; }
	
	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) 
	{ 
		if(!m_bActive) return false;
		
		// Unproject the touch coordinates
		Vector3 vTouch = new Vector3(screenX, screenY, 0);
		m_pCamera.unproject(vTouch);
		screenX = (int) vTouch.x;
		screenY = (int) vTouch.y;
		
		if(m_pPlayer.m_pNode != null)
		{
			// Move to tapped node
			Node pToNode = m_pCurrentRoom.m_pNodes.getNodeByPosition(screenX, screenY);
			if(pToNode != null && pToNode != m_pPlayer.m_pNode && pToNode != m_pPlayer.m_pToNode)
			{
				m_pPlayer.goToNode(pToNode);	
				return true;
			}
			
			Door pToDoor = m_pCurrentRoom.getDoorByPosition(screenX, screenY);
			if(pToDoor != null)
			{
				m_pPlayer.goToDoor(pToDoor);	
				return true;
			}
			
			return false;
		}
		
		return false; 
	}
}
