package com.mygdx.game.util;

import com.badlogic.gdx.Gdx;

public class BlendTimer
{
	public enum EFadeType
	{
		In,
		Out,
		None
	}
	
	private EFadeType m_eType = EFadeType.None;
	
	float m_fStartTime;
	float m_fPeriod;
	
	float m_fTimer = 0.f;
	float m_fPercent = 0.f;
	
	public BlendTimer(float i_fStartTime, float i_fPeriod)
	{
		m_fStartTime = i_fStartTime;
		m_fPeriod = i_fPeriod;
	}
	
	public boolean isBlending()
	{
		return m_eType != EFadeType.None;
	}
	
	public boolean isBlendingIn()
	{
		return m_eType == EFadeType.In && m_fPercent < 1.f;
	}
	
	public boolean isBlendingOut()
	{
		return m_eType == EFadeType.Out && m_fPercent > 0.f;
	}
	
	public void update()
	{
		if(m_eType == EFadeType.None)
		{
			return;
		}
		
		if(Config.m_bTurbo)
		{
			if(m_eType == EFadeType.In)
			{
				onIn();
			}
			else if(m_eType == EFadeType.Out)
			{
				onOut();
			}
			return;
		}
		
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
			if(m_eType == EFadeType.Out) 	onOut();
			else							onIn();
		}
	}
	
	public void start(EFadeType i_eType)
	{
		m_eType = i_eType;
		m_fTimer = 0.f;
		m_fPercent = 0.f;
	}
	
	public float get()
	{
		if(Config.m_bTurbo)
		{
			// Instant
			switch(m_eType)
			{
				case In:
					return 1.f;
				case Out:
					return 0.f;
				default:
					return 1.f;
			}
		}
		else
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

	// May be overridden
	public void onOut() { start(EFadeType.None); }
	public void onIn() { start(EFadeType.None); } 
}
