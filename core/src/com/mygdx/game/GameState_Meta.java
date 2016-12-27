package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
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
	
	Camera m_pCamera = null;
	
	@Override
	void create(Camera i_pCamera, Resources i_pResources)
	{
		m_pCamera = i_pCamera;
		// Create labels
		m_pLabelMessage = new Label("Your daughter is ill.\nYour son is missing.", i_pResources.m_pStyleMetaLarge);
		m_pLabelMessage.setAlignment(Align.center);
		m_pLabelMessage.setEllipsis(false);
		m_pLabelMessage.setY(332.f);
		m_pLabelMessage.setWidth(270.f);
		
		m_pLabelLeft = new Label("Heal her", i_pResources.m_pStyleMetaSmall);
		m_pLabelLeft.setAlignment(Align.center);
		m_pLabelLeft.setEllipsis(false);
		m_pLabelLeft.setY(108.f);
		
		m_pLabelRight = new Label("Find him", i_pResources.m_pStyleMetaSmall);
		m_pLabelRight.setAlignment(Align.center);
		m_pLabelRight.setEllipsis(false);
		m_pLabelRight.setY(108.f);
		
		// Create buttons
		m_pButtonLeft = new InputButton(25, 128, 96, 96) { public void onPress() { onLeft(); } };
		m_pButtonRight = new InputButton(149, 128, 96, 96) { public void onPress() { onRight(); } };
		m_pButtonLeft.m_pSpriteSheetStates = i_pResources.m_pButtonMeta_Health;
		m_pButtonRight.m_pSpriteSheetStates = i_pResources.m_pButtonMeta_Search;
	}

	void onLeft()
	{
		System.out.println("Left Pressed");
	}

	void onRight()
	{
		System.out.println("Right Pressed");
	}
	
	@Override
	void render(SpriteBatch i_pBatch, ShapeRenderer i_pShapeRenderer, Resources i_pResources)
	{
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
		}
		i_pBatch.end();
		
	}
	
	public boolean touchDown(int screenX, int screenY, int pointer, int button) 
	{ 
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
