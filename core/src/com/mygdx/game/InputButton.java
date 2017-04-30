package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.util.ButtonGDX;

abstract public class InputButton extends ButtonGDX
{
	public SpriteSheetGDX m_pSpriteSheetStates;
	
	public enum EState
	{
		EState_Up,
		EState_Down,
	}
	
	public EState m_eState = EState.EState_Up;
	
	public InputButton(int i_iX, int i_iY, int i_iW, int i_iH) 
	{
		super(i_iX, i_iY, i_iW, i_iH);
	}
	
	public abstract void onRelease();
	
	@Override
	public void onButtonPressed()
	{
		m_eState = EState.EState_Down;
	}

	@Override
	public void onButtonReleased()
	{
		if(m_eState == EState.EState_Down)
		{
			onRelease();
		}
		
		m_eState = EState.EState_Up;
	}
	
	public void draw(SpriteBatch batch)
	{
		if(m_pSpriteSheetStates == null)
		{
			return;
		}
		
		if(!m_bIsActive)
		{
			// Flash if trying to render when inactive
			if(Math.random() < 0.5)
			{
				return;
			}
		}
		batch.draw(m_pSpriteSheetStates.getRegion(m_eState.ordinal()), m_iX, m_iY);
	}
}
