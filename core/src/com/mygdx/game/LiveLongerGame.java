package com.mygdx.game;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class LiveLongerGame implements ApplicationListener
{
	OrthographicCamera m_pCamera;
	Viewport m_pViewport;
	
	InputMultiplexer m_pInputMultiplexer;
	
	// Renderers
	SpriteBatch m_pBatch;
	ShapeRenderer m_pShapeRenderer;

	GameState_Meta m_pGameStateMeta = new GameState_Meta();
	GameState_Rooms m_pGameStateRooms = new GameState_Rooms();
	GameState m_pCurrentState = null;
	
	// Transition data
	float m_fTransitionPercent = 0.f;
	
	@Override
	public void create() 
	{
		// Load resources
		Resources.load();
		
		m_pCamera = new OrthographicCamera();
	    m_pViewport = new FitViewport(270, 480, m_pCamera);
	    m_pViewport.apply();
	    m_pCamera.position.set(m_pCamera.viewportWidth/2, m_pCamera.viewportHeight/2, 0);
		
		// Create gamestates
		m_pGameStateMeta.create(m_pCamera);
		m_pGameStateRooms.create(m_pCamera);
		
		m_pInputMultiplexer = new InputMultiplexer(m_pGameStateMeta, m_pGameStateRooms);
		Gdx.input.setInputProcessor(m_pInputMultiplexer);
		
		m_pBatch = new SpriteBatch();
		m_pShapeRenderer = new ShapeRenderer();
		
		setState(m_pGameStateMeta);
		m_pGameStateRooms.start(LevelLoader.m_lpLevels.get(0));
	}
	
	void instantTransitionToRooms()
	{
		// Be careful with this function; No state validation
		setState(m_pGameStateRooms);
		m_pGameStateRooms.start(LevelLoader.getLevelByID(m_pGameStateMeta.m_sLevelStart));
		m_pGameStateMeta.m_sLevelStart = null;
	}
	
	void updateTransition()
	{
		final float k_fTransitionSpeed = 0.01f;
		
		if(m_pGameStateMeta.m_sLevelStart != null)
		{
			// Transition should happen soon
			if(Config.m_bTurbo)
			{
				instantTransitionToRooms();
				return;
			}
			
			m_fTransitionPercent += k_fTransitionSpeed;
			if(m_fTransitionPercent >= 1.f)
			{
				instantTransitionToRooms();
			}
		}
		else if(m_fTransitionPercent > 0.f)
		{
			m_fTransitionPercent -= k_fTransitionSpeed;
			if(m_fTransitionPercent <= 0.f)
			{
				m_fTransitionPercent = 0.f;
			}
		}
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
		
		updateTransition();
		
		// Render the current state
		{
			m_pCurrentState.render(m_pBatch, m_pShapeRenderer);
		}
		
		if(m_fTransitionPercent > 0.f)
		{
			Gdx.gl.glEnable(GL20.GL_BLEND);
			Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);	
			{
				m_pShapeRenderer.begin(ShapeType.Filled);
				m_pShapeRenderer.setColor(new Color(0.f, 0.f, 0.f, m_fTransitionPercent));
				m_pShapeRenderer.rect(0.f, 0.f, 270.f, 480.f);
				m_pShapeRenderer.end();
			}
			Gdx.gl.glDisable(GL20.GL_BLEND);
		}
	}
	
	void setState(GameState i_pNewState)
	{
		if(i_pNewState == m_pCurrentState)
		{
			return;
		}
		
		if(m_pCurrentState != null)
		{
			m_pCurrentState.m_bActive = false;
		}
		
		// Set new state
		m_pCurrentState = i_pNewState;
		m_pCurrentState.m_bActive = true;
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
