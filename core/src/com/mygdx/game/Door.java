package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Door
{
	enum EPosition
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
	}

	Room m_pToRoom;
	Node m_pToNode;
	EPosition m_ePosition;
	String m_sID;
	String m_sToIDRoom;
	String m_sToIDNode;
	
	void draw(SpriteBatch i_pBatch)
	{
		Vector2 vPos = m_ePosition.toPosition();
		i_pBatch.draw(Resources.getDoor(m_ePosition), vPos.x, vPos.y);
	}
}
