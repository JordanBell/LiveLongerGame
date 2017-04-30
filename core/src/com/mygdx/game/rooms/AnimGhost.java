package com.mygdx.game.rooms;

import java.util.ArrayList;

import com.mygdx.game.Resources;
import com.mygdx.game.rooms.Item.EType;

public class AnimGhost extends Anim
{
	static final float sk_fTimePerFrame = 0.25f;

	static final int sk_iFadeSpeed = 50 + (int)(Math.random() * 20);
	int m_iUpdateCounter = (int)(Math.random() * 100);
	
	public AnimGhost()
	{
		m_pSpriteSheet = Resources.m_pGhost;
		ArrayList<Frame> pFrames = new ArrayList<Frame>();
		pFrames.add(new Frame(sk_fTimePerFrame * 2.f, 0, 0, 0));
		pFrames.add(new Frame(sk_fTimePerFrame, 0, 0, 1));
		pFrames.add(new Frame(sk_fTimePerFrame, 0, 0, 2));
		pFrames.add(new Frame(sk_fTimePerFrame * 2.f, 0, 0, 3));
		pFrames.add(new Frame(sk_fTimePerFrame, 0, 0, 2));
		pFrames.add(new Frame(sk_fTimePerFrame, 0, 0, 1));
		m_pAnimStates.add(pFrames);
	}
	
	@Override
	public void update()
	{
		super.update();
		
		++m_iUpdateCounter;
		
		// Set the changing alpha value
		float fFadeFactor = ((float)Math.sin((float)m_iUpdateCounter / (float)sk_iFadeSpeed) + 1.f) / 2.f;
		m_pCurrentFrame.m_fAlpha = 0.1f + fFadeFactor * 0.2f + (float)Math.random() * 0.06f;
		//m_pCurrentFrame.m_fAlpha = fFadeFactor;
	}
}
