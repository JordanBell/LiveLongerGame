package com.mygdx.game;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class LiveLongerGame implements ApplicationListener
{
	Resources m_pResources;
	
	OrthographicCamera m_pCamera;
	Viewport m_pViewport;
	
	InputMultiplexer m_pInputMultiplexer;
	
	// Renderers
	SpriteBatch m_pBatch;
	ShapeRenderer m_pShapeRenderer;

	GameState_Meta m_pGameStateMeta = new GameState_Meta();
	GameState_Rooms m_pGameStateRooms = new GameState_Rooms();
	GameState m_pCurrentState = m_pGameStateMeta;
	
	@Override
	public void create() 
	{
		// Load resources
		m_pResources = new Resources();
		m_pResources.load();
		
		m_pCamera = new OrthographicCamera();
	    m_pViewport = new FitViewport(270, 480, m_pCamera);
	    m_pViewport.apply();
	    m_pCamera.position.set(m_pCamera.viewportWidth/2, m_pCamera.viewportHeight/2, 0);
		
		// Create gamestates
		m_pGameStateMeta.create(m_pCamera, m_pResources);
		m_pGameStateRooms.create(m_pCamera, m_pResources);
		
		m_pInputMultiplexer = new InputMultiplexer(m_pGameStateMeta, m_pGameStateRooms);
		Gdx.input.setInputProcessor(m_pInputMultiplexer);
		
		m_pBatch = new SpriteBatch();
		m_pShapeRenderer = new ShapeRenderer();
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
		
		m_pCurrentState.render(m_pBatch, m_pShapeRenderer, m_pResources);
	}

	@Override
	public void resize(int width, int height)
	{
		m_pViewport.update(width, height);
		m_pCamera.position.set(m_pCamera.viewportWidth / 2, m_pCamera.viewportHeight / 2, 0);
	}
	
	@Override
	public void dispose()
	{
		m_pBatch.dispose();
	}

	public void pause()	{}
	public void resume() {}
}
