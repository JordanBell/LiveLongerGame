package com.mygdx.game.rooms;

import java.util.ArrayList;

import com.mygdx.game.Resources;
import com.mygdx.game.rooms.Item.EType;

public class AnimItem extends Anim
{
	static final float sk_fTimePerFrame = 0.15f;
	
	public AnimItem(Item.EType i_eType)
	{
		m_pSpriteSheet = Resources.m_pItems;
		if(i_eType == EType.EType_Gem)
		{
			// Gem; beat twice, then wait
			ArrayList<Frame> pFrames = new ArrayList<Frame>();
			
			// Establish sprite sheet indexes for the beating animation
			int iSerialIndex_Idle = i_eType.ordinal();
			int iSerialIndex_ThrobUp = i_eType.ordinal() + m_pSpriteSheet.getColumns();
			int iSerialIndex_ThrobMid = i_eType.ordinal() + m_pSpriteSheet.getColumns()*2;
			int iSerialIndex_ThrobDown = i_eType.ordinal() + m_pSpriteSheet.getColumns()*3;
			
			// Timing
			float fTotal = 2.f;
			float fBeatGap = 0.5f;
			float fBeatTotal = 0.3f;
			
			pFrames.add(new Frame(fTotal - fBeatGap * 2.f, iSerialIndex_Idle));
			
			for(int iBeatNum = 0; iBeatNum < 2; iBeatNum++)
			{
				pFrames.add(new Frame(fBeatTotal / 3.f, iSerialIndex_ThrobUp));
				pFrames.add(new Frame(fBeatTotal / 3.f, iSerialIndex_ThrobMid));
				pFrames.add(new Frame(fBeatTotal / 3.f, iSerialIndex_ThrobDown));
				pFrames.add(new Frame(fBeatGap - fBeatTotal, iSerialIndex_Idle));
			}
			
			m_pAnimStates.add(pFrames);
		}
		else
		{
			// Floating, no-sprites
			ArrayList<Frame> pFrames = new ArrayList<Frame>();
			pFrames.add(new Frame(sk_fTimePerFrame * 2.f, i_eType.ordinal(), 0, 0));
			pFrames.add(new Frame(sk_fTimePerFrame, i_eType.ordinal(), 0, 1));
			pFrames.add(new Frame(sk_fTimePerFrame * 2.f, i_eType.ordinal(), 0, 2));
			pFrames.add(new Frame(sk_fTimePerFrame, i_eType.ordinal(), 0, 1));
			m_pAnimStates.add(pFrames);
		}
	}
}
