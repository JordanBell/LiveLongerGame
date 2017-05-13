package com.mygdx.game.rooms;

import java.util.ArrayList;
import com.mygdx.game.Resources;

public class AnimCreature extends Anim
{
	enum EState
	{
		Dormant,
		Idle,
		Running,
	};
	
	EState m_eState = EState.Dormant;
	
	public AnimCreature()
	{
		m_pSpriteSheet = Resources.m_pCreature;
		ArrayList<Frame> pFrames;

		// Anim state: Dormant
		pFrames = new ArrayList<Frame>();
		pFrames.add(new Frame(0.f, 3));
		m_pAnimStates.add(pFrames);
		
		// Anim state: Idle
		pFrames = new ArrayList<Frame>();
		pFrames.add(new Frame(0.f, 0));
		m_pAnimStates.add(pFrames);
		
		// Anim state: Running
		pFrames = new ArrayList<Frame>();
		pFrames.add(new Frame(0.2f, 0));
		pFrames.add(new Frame(0.2f, 1));
		pFrames.add(new Frame(0.2f, 2));
		pFrames.add(new Frame(0.2f, 1));
		m_pAnimStates.add(pFrames);
	}
	
	void setState(EState i_eState)
	{
		if(m_eState == i_eState) return;
		m_eState = i_eState;
		
		setAnimState(m_eState.ordinal());
	}
}