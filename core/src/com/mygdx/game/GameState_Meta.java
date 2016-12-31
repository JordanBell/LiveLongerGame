package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;

public class GameState_Meta extends GameState
{
	Label m_pLabelMessage;
	Label m_pLabelLeft;
	Label m_pLabelRight;

	InputButton m_pButtonLeft = null;
	InputButton m_pButtonRight = null;
	
	String m_sLevelStart = null; // When not null, starts a new level
	
	Camera m_pCamera = null;

	BlendTimer m_pAlphaTimerTop = new BlendTimer(0.5f, 2.f);
	BlendTimer m_pAlphaTimerBL = new BlendTimer(2.5f, 2.f);
	BlendTimer m_pAlphaTimerBR = new BlendTimer(2.5025f, 2.f);
	
	@Override
	void create(Camera i_pCamera)
	{
		m_pCamera = i_pCamera;
		// Create labels
		m_pLabelMessage = new Label("Your daughter is dying.\nYour son is missing.", Resources.m_pStyleMetaLarge);
		m_pLabelMessage.setAlignment(Align.center);
		m_pLabelMessage.setEllipsis(false);
		m_pLabelMessage.setY(332.f);
		m_pLabelMessage.setWidth(270.f);
		
		m_pLabelLeft = new Label("Heal her", Resources.m_pStyleMetaSmall);
		m_pLabelLeft.setAlignment(Align.center);
		m_pLabelLeft.setEllipsis(false);
		m_pLabelLeft.setY(108.f);
		
		m_pLabelRight = new Label("Find him", Resources.m_pStyleMetaSmall);
		m_pLabelRight.setAlignment(Align.center);
		m_pLabelRight.setEllipsis(false);
		m_pLabelRight.setY(108.f);
		
		// Create buttons
		m_pButtonLeft = new InputButton(25, 128, 96, 96) { public void onPress() { onLeft(); } };
		m_pButtonRight = new InputButton(149, 128, 96, 96) { public void onPress() { onRight(); } };
		m_pButtonLeft.m_pSpriteSheetStates = Resources.m_pButtonMeta_Health;
		m_pButtonRight.m_pSpriteSheetStates = Resources.m_pButtonMeta_Search;
	}
	
	void start()
	{
		
	}

	void onLeft()
	{
		m_sLevelStart = "level_heal_her";
	}

	void onRight()
	{
		m_sLevelStart = "level_heal_her";
	}
	
	void updateFades()
	{
		m_pAlphaTimerBL.update();
		m_pAlphaTimerBR.update();
		m_pAlphaTimerTop.update();
	}
	
	@Override
	void render(SpriteBatch i_pBatch, ShapeRenderer i_pShapeRenderer)
	{
		// Update stuff
		updateFades();
		
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		// Update label positions
		m_pLabelMessage.setX(135.f - m_pLabelMessage.getWidth() * 0.5f);
		m_pLabelLeft.setX(m_pButtonLeft.m_iX + m_pButtonLeft.m_iWidth * 0.5f - m_pLabelLeft.getWidth() * 0.5f);
		m_pLabelRight.setX(m_pButtonRight.m_iX + m_pButtonRight.m_iWidth * 0.5f - m_pLabelRight.getWidth() * 0.5f);
		
		i_pBatch.begin();
		{
			m_pLabelMessage.draw(i_pBatch, 1.f);

			m_pButtonLeft.draw(i_pBatch);
			m_pLabelLeft.draw(i_pBatch, 1.f);

			m_pButtonRight.draw(i_pBatch);
			m_pLabelRight.draw(i_pBatch, 1.f);
			
			i_pBatch.end();
			Gdx.gl.glEnable(GL20.GL_BLEND);
			Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);	
			i_pShapeRenderer.begin(ShapeType.Filled);
			{
				// Top
				i_pShapeRenderer.setColor(new Color(0.f, 0.f, 0.f, 1.f - m_pAlphaTimerTop.getAlpha()));
				i_pShapeRenderer.rect(0.f, 240.f, 270.f, 240.f);

				// Bottom Left
				i_pShapeRenderer.setColor(new Color(0.f, 0.f, 0.f, 1.f - m_pAlphaTimerBL.getAlpha()));
				i_pShapeRenderer.rect(0.f, 0.f, 135.f, 240.f);

				// Bottom Right
				i_pShapeRenderer.setColor(new Color(0.f, 0.f, 0.f, 1.f - m_pAlphaTimerBR.getAlpha()));
				i_pShapeRenderer.rect(135.f, 0.f, 135.f, 240.f);
			}
			i_pShapeRenderer.end();
			Gdx.gl.glDisable(GL20.GL_BLEND);
			i_pBatch.begin();
		}
		i_pBatch.end();
		
	}
	
	public boolean touchDown(int screenX, int screenY, int pointer, int button) 
	{ 
		if(!m_bActive) return false;
		
		// Unproject the touch coordinates
		Vector3 vTouch = new Vector3(screenX, screenY, 0);
		m_pCamera.unproject(vTouch);
		screenX = (int) vTouch.x;
		screenY = (int) vTouch.y;

		// Pass event to the buttons
		return m_pButtonLeft.touchDown(screenX, screenY, pointer, button)
			|| m_pButtonRight.touchDown(screenX, screenY, pointer, button);
	}
	
	public boolean touchUp(int screenX, int screenY, int pointer, int button) 
	{ 
		if(!m_bActive) return false;
		
		// Unproject the touch coordinates
		Vector3 vTouch = new Vector3(screenX, screenY, 0);
		m_pCamera.unproject(vTouch);
		screenX = (int) vTouch.x;
		screenY = (int) vTouch.y;
		
		// Pass event to the buttons
		return m_pButtonLeft.touchUp(screenX, screenY, pointer, button)
			|| m_pButtonRight.touchUp(screenX, screenY, pointer, button);
	}

	public boolean keyDown(int keycode) { return false; }
	public boolean keyUp(int keycode) { return false; }
	public boolean keyTyped(char character) { return false; }
	public boolean touchDragged(int screenX, int screenY, int pointer) { return false; }
	public boolean mouseMoved(int screenX, int screenY) { return false; }
	public boolean scrolled(int amount) { return false; }
}
