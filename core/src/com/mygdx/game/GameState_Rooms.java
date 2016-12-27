package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class GameState_Rooms extends GameState
{
	Room m_pCurrentRoom;
	Crawler m_pPlayer = new Crawler();
	
	@Override
	void create(Camera i_pCamera, Resources i_pResources)
	{
		// Create all rooms
		m_pCurrentRoom = new Room();
		m_pCurrentRoom.m_pBackground = i_pResources.m_pBackground;
		
		// Create player
		m_pPlayer.m_pSpriteSheet = i_pResources.m_pPlayer;
		m_pPlayer.m_pNode = m_pCurrentRoom.m_pNodes.m_lpNodes.get(0);
	}

	@Override
	void render(SpriteBatch i_pBatch, ShapeRenderer i_pShapeRenderer, Resources i_pResources)
	{
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		i_pBatch.begin();
		{
			// Room
			m_pCurrentRoom.render(i_pBatch, i_pShapeRenderer, i_pResources);
			m_pPlayer.render(i_pBatch);
			// UI (optional)
		}
		i_pBatch.end();
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
		if(m_pPlayer.m_pNode != null)
		{
			// Move to tapped node
			Node pToNode = m_pCurrentRoom.m_pNodes.getNodeByLocation(screenX, screenY);
			if(pToNode != null)
			{
				m_pPlayer.goToNode(pToNode);	
				return true;
			}
			
			return false;
		}
		
		return false; 
	}
}
