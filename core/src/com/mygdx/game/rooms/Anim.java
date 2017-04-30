package com.mygdx.game.rooms;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.game.SpriteSheetGDX;

class Frame
{
	float m_fTime;
	int m_iIndex;
	int m_iOffsetX;
	int m_iOffsetY;
	float m_fAlpha;
	
	Frame(float i_fTime, int i_iIndex)
	{
		this(i_fTime, i_iIndex, 0, 0, 1.f);
	}
	
	Frame(float i_fTime, int i_iIndex, int i_iOffsetX, int i_iOffsetY)
	{
		this(i_fTime, i_iIndex, i_iOffsetX, i_iOffsetY, 1.f);
	}
	
	Frame(float i_fTime, int i_iIndex, int i_iOffsetX, int i_iOffsetY, float i_fAlpha)
	{
		m_fTime = i_fTime;
		m_iIndex = i_iIndex;
		m_iOffsetX = i_iOffsetX;
		m_iOffsetY = i_iOffsetY;
		m_fAlpha = i_fAlpha;
	}
}

class Anim extends GraphicsComponent
{
	SpriteSheetGDX m_pSpriteSheet = null;

	float m_fTimer = (float)Math.random() * 10.f;
	ArrayList<ArrayList<Frame>> m_pAnimStates = new ArrayList<ArrayList<Frame>>();
	int m_iAnimState = 0;
	
	Frame m_pCurrentFrame = null;
	
	void setAsPingPong(SpriteSheetGDX i_pSpriteSheet, final float i_fFrameTime)
	{
		m_pSpriteSheet = i_pSpriteSheet;
		
		ArrayList<Frame> pFrames = new ArrayList<Frame>();
		
		final int iNumRegions = i_pSpriteSheet.getNumRegions();
		int i = 0;
		for(; i < iNumRegions; ++i)
		{
			pFrames.add(new Frame(i_fFrameTime, i));
		}
		i -= 2;
		for(; i > 0; --i)
		{
			pFrames.add(new Frame(i_fFrameTime, i));
		}
		
		m_pAnimStates.add(pFrames);
	}
	
	void setAsLooping(SpriteSheetGDX i_pSpriteSheet, final float i_fFrameTime)
	{
		m_pSpriteSheet = i_pSpriteSheet;
		
		ArrayList<Frame> pFrames = new ArrayList<Frame>();
		
		final int iNumRegions = i_pSpriteSheet.getNumRegions();
		int i = 0;
		for(; i < iNumRegions; ++i)
		{
			pFrames.add(new Frame(i_fFrameTime, i));
		}
		
		m_pAnimStates.add(pFrames);
	}
	
	public void setAnimState(int i_iNewState)
	{
		m_iAnimState = i_iNewState;
		m_pCurrentFrame = getFrames().get(0);
	}
	
	ArrayList<Frame> getFrames()
	{
		return m_pAnimStates.get(m_iAnimState);
	}
	
	@Override
	public void update()
	{
		ArrayList<Frame> pFrames = getFrames();

		if(pFrames.size() == 1)
		{
			m_pCurrentFrame = pFrames.get(0);
			return;
		}
		
		// Update the anim index
		m_fTimer += Gdx.graphics.getDeltaTime();
		
		float fAcc = 0.f;
		m_pCurrentFrame = null;
		int i;
		for(i = 0; m_pCurrentFrame == null && i < pFrames.size(); ++i)
		{
			Frame pFrame = pFrames.get(i);
			
			if(m_fTimer > fAcc && m_fTimer < fAcc + pFrame.m_fTime)
			{
				m_pCurrentFrame = pFrames.get(i);
			}
			
			fAcc += pFrame.m_fTime;
		}
		
		// Not found
		if(m_pCurrentFrame == null)
		{
			m_fTimer -= fAcc;
		}
		
		--i;
		
		m_pCurrentFrame = pFrames.get(i);
	}
	
	@Override
	public void draw(SpriteBatch i_pBatch, float x, float y)
	{
		i_pBatch.setColor(1.f, 1.f, 1.f, m_pCurrentFrame.m_fAlpha);
		i_pBatch.draw(getCurrentRegion(), x + m_pCurrentFrame.m_iOffsetX, y + m_pCurrentFrame.m_iOffsetY);
		i_pBatch.setColor(1.f, 1.f, 1.f, 1.f);
	}
	
	public TextureRegion getCurrentRegion()
	{
		if(m_pCurrentFrame == null) m_pCurrentFrame = getFrames().get(0);
		return m_pSpriteSheet.getRegion(m_pCurrentFrame.m_iIndex);
	}

	@Override
	public float getWidth()
	{
		return getCurrentRegion().getRegionWidth();
	}

	@Override
	public float getHeight()
	{
		return getCurrentRegion().getRegionHeight();
	}
}