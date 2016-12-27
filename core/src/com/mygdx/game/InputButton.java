package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

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
	
	abstract void onPress();
	
	@Override
	public void onButtonPressed()
	{
		m_eState = EState.EState_Down;
		onPress();
	}

	@Override
	public void onButtonReleased()
	{
		m_eState = EState.EState_Up;
	}
	
	public void draw(SpriteBatch batch)
	{
		if(m_pSpriteSheetStates == null)
		{
			System.err.println("Cannot render a button if it has no sprite sheet");
			return;
		}
		
		batch.draw(m_pSpriteSheetStates.getRegion(m_eState.ordinal()), m_iX, m_iY);
	}
}
