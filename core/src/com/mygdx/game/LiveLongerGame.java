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
import com.mygdx.game.SaveData.EProgress;
import com.mygdx.game.meta.GameState_Meta;
import com.mygdx.game.rooms.GameState_Rooms;
import com.mygdx.game.rooms.LevelLoader;
import com.mygdx.game.util.BlendTimer;
import com.mygdx.game.util.BlendTimer.EFadeType;
import com.mygdx.game.util.Config;

public class LiveLongerGame implements ApplicationListener
{
	OrthographicCamera m_pCamera;
	Viewport m_pViewport;
	
	InputMultiplexer m_pInputMultiplexer;
	public static boolean s_bTransitionState = false;
	
	// Renderers
	SpriteBatch m_pBatch;
	ShapeRenderer m_pShapeRenderer;

	GameState_Meta m_pGameStateMeta = new GameState_Meta();
	GameState_Rooms m_pGameStateRooms = new GameState_Rooms();
	GameState m_pCurrentState = null;
	
	private static int s_iFreezeCounter = 0;
	
	// Transition data
	BlendTimer m_pTransitionAlpha;
	
	@Override
	public void create() 
	{
		// Load resources
		Resources.load();
		
		// Load save data
		SaveData.load();
		
		m_pTransitionAlpha = new BlendTimer(0.25f, 1.5f)
		{
			@Override
			public void onOut() { onFadeOpaque(); }
			public void onIn() { m_pTransitionAlpha.start(EFadeType.None); }
		};
		
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
		
		// Set initial state based on save data
//		setState(SaveData.getStateIndex() == 0 ? m_pGameStateMeta : m_pGameStateRooms);
//		if(m_pCurrentState == m_pGameStateMeta) m_pGameStateMeta.start();
//		else									m_pGameStateRooms.start();
		
		// Debug: Skip to a particular state
		setState(m_pGameStateRooms);
		m_pGameStateRooms.start(LevelLoader.getLevelByID("level_gem2"));
		SaveData.m_eProgress = EProgress.Rooms4_Gem2;

//		setState(m_pGameStateMeta);
//		SaveData.m_eProgress = EProgress.Meta2;
//		m_pGameStateMeta.start();
	}
	
	public static void setCameraShake()
	{
		s_iFreezeCounter = 5;
	}
	
	void onFadeOpaque()
	{
		// If we're currently in the meta game state, switch to the tooms, and vice versa
		if(m_pCurrentState == m_pGameStateMeta)
		{
			// Be careful with this function; No state validation
			setState(m_pGameStateRooms);
			m_pGameStateRooms.start(LevelLoader.getLevelByID(m_pGameStateMeta.m_sLevelStart));
			m_pGameStateMeta.m_sLevelStart = null;
		}
		else
		{
			setState(m_pGameStateMeta);
			// TODO: Here or elsewhere, set the progress so that the meta can be updated
			m_pGameStateMeta.start();
		}
		
		m_pTransitionAlpha.start(EFadeType.In);
	}
	
	public void checkForStateTransitions()
	{
		if(s_bTransitionState)
		{
			if(!m_pTransitionAlpha.isBlending())
			{
				m_pTransitionAlpha.start(EFadeType.Out);
			}

			s_bTransitionState = false;
		}

	}

	@Override
	public void render () 
	{
		// Apply and update camera
		m_pViewport.apply();
	    m_pCamera.position.set(m_pCamera.viewportWidth / 2, m_pCamera.viewportHeight / 2, 0);
	    
		int iShakeX = s_iFreezeCounter > 0 ? (int)(Math.random() * 6) - 3 : 0;
		int iShakeY = s_iFreezeCounter > 0 ? (int)(Math.random() * 6) - 3 : 0;
	    m_pCamera.position.set(m_pCamera.viewportWidth / 2 + iShakeX, m_pCamera.viewportHeight / 2 + iShakeY, 0);
	    
	    // Update projection matrices after camera update
	    m_pBatch.setProjectionMatrix(m_pCamera.combined);
		m_pShapeRenderer.setProjectionMatrix(m_pCamera.combined);
	    
	    if(s_iFreezeCounter > 0)
		{
			s_iFreezeCounter--;
		}

	    checkForStateTransitions();
		m_pTransitionAlpha.update();
		
		// Render the current state
		{
			m_pCurrentState.render(m_pBatch, m_pShapeRenderer);
		}
		
		if(m_pTransitionAlpha.isBlending())
		{
			Gdx.gl.glEnable(GL20.GL_BLEND);
			Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);	
			{
				m_pShapeRenderer.begin(ShapeType.Filled);
				m_pShapeRenderer.setColor(new Color(0.f, 0.f, 0.f, 1.f - m_pTransitionAlpha.get()));
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
