package com.mygdx.game;

import com.badlogic.gdx.Gdx;

public class BlendTimer
{
	enum EFadeType
	{
		In,
		Out,
		None
	}
	
	EFadeType m_eType = EFadeType.In;
	
	float m_fStartTime;
	float m_fPeriod;
	
	float m_fTimer = 0.f;
	float m_fPercent = 0.f;
	
	public BlendTimer(float i_fStartTime, float i_fPeriod)
	{
		m_fStartTime = i_fStartTime;
		m_fPeriod = i_fPeriod;
	}
	
	void update()
	{
		m_fTimer += Gdx.graphics.getDeltaTime();
		
		// Top Alpha
		if(m_fTimer < m_fStartTime)
		{
			m_fPercent = 0.f;
		}
		else if(m_fTimer < m_fStartTime + m_fPeriod)
		{
			m_fPercent = (m_fTimer - m_fStartTime) / m_fPeriod;
		}
		else
		{
			m_fPercent = 1.f;
		}
	}
	
	float getAlpha()
	{
		switch(m_eType)
		{
			case In:
				return m_fPercent;
			case Out:
				return 1.f - m_fPercent;
			default:
				return 1.f;
		}
	}
}
