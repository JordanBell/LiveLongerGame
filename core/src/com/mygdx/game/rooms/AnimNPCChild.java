package com.mygdx.game.rooms;

import java.util.ArrayList;

import com.mygdx.game.Resources;

public class AnimNPCChild extends Anim
{
	int m_iColorIndex;
	
	static final int sk_iNumColors = 4;
	static final float sk_fTimePerFrame = 0.5f;
	
	public AnimNPCChild(int i_iColorIndex)
	{
		if(i_iColorIndex < 0)
		{
			i_iColorIndex = (int)(Math.random() * sk_iNumColors);
		}
		
		m_iColorIndex = i_iColorIndex;
		m_pSpriteSheet = Resources.m_pNPCChild;
		
		ArrayList<Frame> pFrames = new ArrayList<Frame>();
		pFrames.add(new Frame(sk_fTimePerFrame, m_iColorIndex));
		pFrames.add(new Frame(sk_fTimePerFrame, m_iColorIndex + sk_iNumColors));
		m_pAnimStates.add(pFrames);
	}
}
