package com.mygdx.game.rooms;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Resources;

public class Door
{
	public enum EPosition
	{
		TL,
		TM,
		TR,
		
		LT,
		LM,
		LB,
		
		BL,
		BM,
		BR,
		
		RT,
		RM,
		RB;
		
		static EPosition fromString(String i_s)
		{
			if(i_s.equals("tl")) return TL;
			if(i_s.equals("tm")) return TM;
			if(i_s.equals("tr")) return TR;
			
			if(i_s.equals("lt")) return LT;
			if(i_s.equals("lm")) return LM;
			if(i_s.equals("lb")) return LB;

			if(i_s.equals("bl")) return BL;
			if(i_s.equals("bm")) return BM;
			if(i_s.equals("br")) return BR;

			if(i_s.equals("rt")) return RT;
			if(i_s.equals("rm")) return RM;
			if(i_s.equals("rb")) return RB;
			
			throw new RuntimeException("Unregognised string");
		}
		
		Vector2 toPosition()
		{
			Vector2 r_v = new Vector2();
			
			// Set y
			switch(this)
			{
				case TL:
				case TM:
				case TR:
					r_v.y = 338.f;
					break;

				case BL:
				case BM:
				case BR:
					r_v.y = 110.f;
					break;

				case RT:
				case LT:
					r_v.y = 274.f;
					break;

				case RM:
				case LM:
					r_v.y = 214.f;
					break;

				case RB:
				case LB:
					r_v.y = 155.f; 
					break;
			}
			
			// Set x
			switch(this)
			{
				case TL:
				case BL:
					r_v.x = 46.f;
					break;

				case BM:
				case TM:
					r_v.x = 114.f;
					break;

				case BR:
				case TR:
					r_v.x = 177.f;
					break;

				case RT:
				case RM:
				case RB:
					r_v.x = 239.f;
					break;

				case LT:
				case LM:
				case LB:
					r_v.x = 12.f;
					break;
			}
			
			return r_v;
		}
		
		Vector2 toPositionCenter()
		{
			Vector2 r_vPos = toPosition();
			Texture pTexture = Resources.getDoor(this);
			r_vPos.x += pTexture.getWidth() * 0.5f;
			r_vPos.y += pTexture.getHeight() * 0.5f;
			return r_vPos;
		}
		
		Vector2 toPositionBase()
		{
			Vector2 r_vPos = toPosition();
			Texture pTexture = Resources.getDoor(this);
			
			// Y Adjustment
			switch(this)
			{
				case RT:
				case RM:
				case RB:
				case LT:
				case LM:
				case LB: 
					r_vPos.y += pTexture.getHeight() * 0.25f;
					break;
				default: break;
			}
			
			// X Adjustment
			switch(this)
			{
				case TM:
				case BM:
					r_vPos.x += pTexture.getWidth() * 0.5f;
					break;
				case TL:
				case BL:
					r_vPos.x += pTexture.getWidth() * 0.5f;
					//r_vPos.x += 4; 
					break;
				case TR:
				case BR:
					r_vPos.x += pTexture.getWidth() * 0.5f;
					r_vPos.x -= 4; 
					break;
				case LT:
				case LM:
				case LB:
					r_vPos.x += pTexture.getWidth();
					break;
				default: break;
			}
			
			return r_vPos;
		}
	}

	Room m_pToRoom;
	Node m_pToNode;
	Node m_pFromNode;
	EPosition m_ePosition;
	String m_sID;
	String m_sToIDRoom;
	String m_sToIDNode;
	String m_sRequiredItem = null;
	boolean m_bIsEndOfLevel = false;
	boolean m_bLock;
	boolean m_bUnlocked = false;
	
	boolean isPositionLockSupported()
	{
		return m_ePosition == EPosition.TR
			|| m_ePosition == EPosition.LB;
	}
	
	void draw(SpriteBatch i_pBatch)
	{
		Vector2 vPos = m_ePosition.toPosition();
		TextureRegion pRegion;
		if(!m_bUnlocked && m_bLock)
		{
			// Render lock
			pRegion = new TextureRegion(Resources.getDoor(m_ePosition));
			if(!isPositionLockSupported())
			{
				throw new RuntimeException("Unsupported door for that lock. Add graphic to it.");
			}
			
			pRegion.setRegion(0, pRegion.getRegionHeight() / 2, pRegion.getRegionWidth(), pRegion.getRegionHeight() / 2);
		}
		else
		{
			// Render normally
			pRegion = new TextureRegion(Resources.getDoor(m_ePosition));
			if(isPositionLockSupported())
			{
				// Set region as second half; the locked half
				pRegion.setRegion(0, 0, pRegion.getRegionWidth(), pRegion.getRegionHeight() / 2);
			}
		}
		i_pBatch.draw(pRegion, vPos.x, vPos.y);
	}
}
