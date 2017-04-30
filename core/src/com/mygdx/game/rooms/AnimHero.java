package com.mygdx.game.rooms;

import java.util.ArrayList;

import com.mygdx.game.Resources;

public class AnimHero extends Anim
{
	public AnimHero()
	{
		m_pSpriteSheet = Resources.m_pHero;
		ArrayList<Frame> pFrames = new ArrayList<Frame>();
		pFrames.add(new Frame(0.f, 0));
		m_pAnimStates.add(pFrames);
	}
}
