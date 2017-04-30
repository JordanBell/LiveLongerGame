package com.mygdx.game.rooms;

import java.util.ArrayList;

import com.mygdx.game.Resources;

public class AnimItem extends Anim
{
	static final float sk_fTimePerFrame = 0.15f;
	
	public AnimItem(Item.EType i_eType)
	{
		m_pSpriteSheet = Resources.m_pItems;
		ArrayList<Frame> pFrames = new ArrayList<Frame>();
		pFrames.add(new Frame(sk_fTimePerFrame * 2.f, i_eType.ordinal(), 0, 0));
		pFrames.add(new Frame(sk_fTimePerFrame, i_eType.ordinal(), 0, 1));
		pFrames.add(new Frame(sk_fTimePerFrame * 2.f, i_eType.ordinal(), 0, 2));
		pFrames.add(new Frame(sk_fTimePerFrame, i_eType.ordinal(), 0, 1));
		m_pAnimStates.add(pFrames);
	}
}
