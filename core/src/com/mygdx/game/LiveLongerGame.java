package com.mygdx.game;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class LiveLongerGame implements ApplicationListener, GestureListener
{
	Resources m_pResources = new Resources();
	
	OrthographicCamera m_pCamera;
	Viewport m_pViewport;
	
	GestureDetector m_pGestureDetector;
	InputMultiplexer m_pInputMultiplexer;
	
	Room m_pCurrentRoom;

	Crawler m_pPlayer = new Crawler();
	
	// Renderers
	SpriteBatch m_pBatch;
	ShapeRenderer m_pShapeRenderer;
	
	@Override
	public void create() 
	{
		// Load resources
		m_pResources.load();
		
		// Create all rooms
		m_pCurrentRoom = new Room();
		m_pCurrentRoom.m_pBackground = m_pResources.m_pBackground;
		
		// Create player
		m_pPlayer.m_pSpriteSheet = new SpriteSheetGDX("child.png", 1, 2, 12, 14);
		m_pPlayer.m_pNode = m_pCurrentRoom.m_pNodes.m_lpNodes.get(0);
		
		m_pGestureDetector = new GestureDetector(this);
		m_pInputMultiplexer = new InputMultiplexer(m_pGestureDetector);
		Gdx.input.setInputProcessor(m_pInputMultiplexer);
		
		m_pCamera = new OrthographicCamera();
	    m_pViewport = new FitViewport(270, 480, m_pCamera);
	    m_pViewport.apply();
	    m_pCamera.position.set(m_pCamera.viewportWidth/2, m_pCamera.viewportHeight/2, 0);
		
		m_pBatch = new SpriteBatch();
		m_pShapeRenderer = new ShapeRenderer();
		
		init();
	}
	
	void init()
	{
		// Code that is called whenever re-starting the game
	}

	@Override
	public void render () 
	{
		// Apply and update camera
		m_pViewport.apply();
	    m_pCamera.position.set(m_pCamera.viewportWidth / 2, m_pCamera.viewportHeight / 2, 0);

	    // Update projection matrices after camera update
	    m_pBatch.setProjectionMatrix(m_pCamera.combined);
		m_pShapeRenderer.setProjectionMatrix(m_pCamera.combined);
		
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		m_pBatch.begin();
		{
			// Room
			m_pCurrentRoom.render(m_pBatch, m_pShapeRenderer, m_pResources);
			m_pPlayer.render(m_pBatch);
			// UI (optional)
		}
		m_pBatch.end();
	}

	@Override
	public void resize(int width, int height)
	{
		m_pViewport.update(width, height);
		m_pCamera.position.set(m_pCamera.viewportWidth / 2, m_pCamera.viewportHeight / 2, 0);
	}

	@Override
	public void pause()
	{
	}

	@Override
	public void resume()
	{
	}

	@Override
	public void dispose()
	{
	}

	@Override
	public boolean touchDown(float x, float y, int pointer, int button)
	{
		return false;
	}

	@Override
	public boolean tap(float x, float y, int count, int button)
	{
		// Unproject the touch coordinates
		Vector3 vTouch = new Vector3(x, y, 0);
		m_pCamera.unproject(vTouch);
		x = vTouch.x;
		y = vTouch.y;
		
		// TODO Move to tapped node
		m_pPlayer.goToNode(m_pCurrentRoom.m_pNodes.getNodeByLocation(x, y));
		return false;
	}

	@Override
	public boolean longPress(float x, float y)
	{
		return false;
	}

	@Override
	public boolean fling(float velocityX, float velocityY, int button)
	{
		// TODO Move to node in fling direction
		return false;
	}

	@Override
	public boolean pan(float x, float y, float deltaX, float deltaY)
	{
		return false;
	}

	@Override
	public boolean panStop(float x, float y, int pointer, int button)
	{
		return false;
	}

	@Override
	public boolean zoom(float initialDistance, float distance)
	{
		return false;
	}

	@Override
	public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2)
	{
		return false;
	}

	@Override
	public void pinchStop()
	{
	}
}
