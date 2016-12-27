package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;

public class Resources
{
	static SaveData m_pSaveData = new SaveData();
	
	static Texture m_pBackground;
	static Texture m_pNode;
	
	// Door textures
	static Texture m_pDoorTL;
	static Texture m_pDoorTM;
	static Texture m_pDoorTR;
	static Texture m_pDoorBL;
	static Texture m_pDoorBM;
	static Texture m_pDoorBR;
	static Texture m_pDoorLT;
	static Texture m_pDoorLM;
	static Texture m_pDoorLB;
	static Texture m_pDoorRT;
	static Texture m_pDoorRM;
	static Texture m_pDoorRB;
	
	// Sprite Sheets
	static SpriteSheetGDX m_pPlayer;
	static SpriteSheetGDX m_pButtonMeta_Base;
	static SpriteSheetGDX m_pButtonMeta_Health;
	static SpriteSheetGDX m_pButtonMeta_Search;
	
	// Fonts
	public static Color m_pColorDark = new Color(28.f / 255.f, 20.f / 255.f, 26.f / 255.f, 1.f);
	public static Color m_pColorLight = new Color(152.f / 255.f, 165.f / 255.f, 139.f / 255.f, 1.f);
	
	private static FreeTypeFontGenerator fontGenerator0 = new FreeTypeFontGenerator(Gdx.files.internal("fonts/chary.ttf"));
	private static FreeTypeFontGenerator fontGenerator1 = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Golden-Sun.ttf"));
	private static FreeTypeFontGenerator fontGenerator3 = new FreeTypeFontGenerator(Gdx.files.internal("fonts/retganon.ttf"));
	private static FreeTypeFontGenerator fontGenerator4 = new FreeTypeFontGenerator(Gdx.files.internal("fonts/RUBBBB__.TTF"));
	private static FreeTypeFontParameter m_pParamMeta = new FreeTypeFontParameter();
	private static BitmapFont m_pFontMetaLarge = null;
	private static BitmapFont m_pFontMetaSmall = null;
	public static LabelStyle m_pStyleMetaLarge = null;
	public static LabelStyle m_pStyleMetaSmall = null;
	
	static void load()
	{
		m_pBackground = new Texture("room/background_1.png");
		m_pNode = new Texture("ui/node.png");
		m_pPlayer = new SpriteSheetGDX("player.png", 1, 1, 19, 26);
		m_pButtonMeta_Base = new SpriteSheetGDX("ui/button_meta.png", 1, 2, 96, 96);
		m_pButtonMeta_Health = new SpriteSheetGDX("ui/button_meta_health.png", 1, 2, 96, 96);
		m_pButtonMeta_Search = new SpriteSheetGDX("ui/button_meta_search.png", 1, 2, 96, 96);
		
		// Doors
		{
			m_pDoorTL = new Texture("room/door_tl.png");
			m_pDoorTM = new Texture("room/door_tm.png");
			m_pDoorTR = new Texture("room/door_tr.png");
			m_pDoorBL = new Texture("room/door_bl.png");
			m_pDoorBM = new Texture("room/door_bm.png");
			m_pDoorBR = new Texture("room/door_br.png");
			m_pDoorLT = new Texture("room/door_lt.png");
			m_pDoorLM = new Texture("room/door_lm.png");
			m_pDoorLB = new Texture("room/door_lb.png");
			m_pDoorRT = new Texture("room/door_rt.png");
			m_pDoorRM = new Texture("room/door_rm.png");
			m_pDoorRB = new Texture("room/door_rb.png");
		}
		
		// Fonts
		m_pParamMeta.color = m_pColorLight;
		m_pParamMeta.size = 24;
		m_pFontMetaLarge = fontGenerator0.generateFont(m_pParamMeta);
		m_pParamMeta.size = 20;
		m_pFontMetaSmall = fontGenerator0.generateFont(m_pParamMeta);
		
		m_pStyleMetaLarge = new LabelStyle(m_pFontMetaLarge, m_pColorLight);
		m_pStyleMetaSmall = new LabelStyle(m_pFontMetaSmall, m_pColorLight);
	}
	
	static Texture getDoor(Door.EPosition i_ePosition)
	{
		switch(i_ePosition)
		{
			case TL: return m_pDoorTL;
			case TM: return m_pDoorTM;
			case TR: return m_pDoorTR;
			case BL: return m_pDoorBL;
			case BM: return m_pDoorBM;
			case BR: return m_pDoorBR;
			case LT: return m_pDoorLT;
			case LM: return m_pDoorLM;
			case LB: return m_pDoorLB;
			case RT: return m_pDoorRT;
			case RM: return m_pDoorRM;
			case RB: return m_pDoorRB;
			default: return null;
		}
	}
}
