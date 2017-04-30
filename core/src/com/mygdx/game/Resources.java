package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.mygdx.game.rooms.Door;

public class Resources
{
	static SaveData m_pSaveData = new SaveData();
	
	public static Texture m_pBackground;
	public static Texture m_pNode;
	
	// Door textures
	public static Texture m_pDoorTL;
	public static Texture m_pDoorTM;
	public static Texture m_pDoorTR;
	public static Texture m_pDoorBL;
	public static Texture m_pDoorBM;
	public static Texture m_pDoorBR;
	public static Texture m_pDoorLT;
	public static Texture m_pDoorLM;
	public static Texture m_pDoorLB;
	public static Texture m_pDoorRT;
	public static Texture m_pDoorRM;
	public static Texture m_pDoorRB;
	
	// Misc Textures
	public static Texture m_pNPCBubble;
	public static Texture m_pDialogueBorder;
	public static Texture m_pDialogueShutter;
	
	// Sprite Sheets
	public static SpriteSheetGDX m_pHero;
	public static SpriteSheetGDX m_pNPCAdult;
	public static SpriteSheetGDX m_pNPCChild;
	public static SpriteSheetGDX m_pCreature;
	public static SpriteSheetGDX m_pItems;
	public static SpriteSheetGDX m_pChatheadNPCAdult;
	public static SpriteSheetGDX m_pChatheadNPCChild;
	public static SpriteSheetGDX m_pButtonMeta_Base;
	public static SpriteSheetGDX m_pButtonMeta_Okay;
	public static SpriteSheetGDX m_pButtonMeta_Health;
	public static SpriteSheetGDX m_pButtonMeta_Search;
	public static SpriteSheetGDX m_pButtonMeta_Yes;
	public static SpriteSheetGDX m_pButtonMeta_No;
	public static SpriteSheetGDX m_pButtonMeta_Food;
	public static SpriteSheetGDX m_pButtonMeta_Water;
	public static SpriteSheetGDX m_pButtonRoom_Yes;
	public static SpriteSheetGDX m_pButtonRoom_No;
	public static SpriteSheetGDX m_pButtonRoom_Okay;

	public static SpriteSheetGDX m_pScratch0_a;
	public static SpriteSheetGDX m_pScratch0_b;
	
	// Fonts
	public static Color m_pColorDark = new Color(28.f / 255.f, 20.f / 255.f, 26.f / 255.f, 1.f);
	public static Color m_pColorLight = new Color(165.f / 255.f, 165.f / 255.f, 140.f / 255.f, 1.f);
	
	private static FreeTypeFontGenerator fontGenerator0 = new FreeTypeFontGenerator(Gdx.files.internal("fonts/chary.ttf"));
	private static FreeTypeFontGenerator fontGenerator1 = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Golden-Sun.ttf"));
	private static FreeTypeFontGenerator fontGenerator3 = new FreeTypeFontGenerator(Gdx.files.internal("fonts/retganon.ttf"));
	private static FreeTypeFontGenerator fontGenerator4 = new FreeTypeFontGenerator(Gdx.files.internal("fonts/RUBBBB__.TTF"));
	private static FreeTypeFontParameter m_pFontParam = new FreeTypeFontParameter();
	private static BitmapFont m_pFontMetaLarge = null;
	private static BitmapFont m_pFontMetaSmall = null;
	private static BitmapFont m_pFontRoomDialogue = null;
	public static LabelStyle m_pStyleMetaLarge = null;
	public static LabelStyle m_pStyleMetaSmall = null;
	public static LabelStyle m_pStyleRoomDialogue = null;
	
	public static void load()
	{
		m_pBackground = new Texture("room/background_1.png");
		m_pNode = new Texture("ui/node.png");
		m_pHero = new SpriteSheetGDX("room/hero.png", 1, 4, 13, 30);
		m_pNPCAdult = new SpriteSheetGDX("room/NPC Adult.png", 2, 4, 16, 29);
		m_pNPCChild = new SpriteSheetGDX("room/NPC Child.png", 2, 4, 12, 25);
		m_pCreature = new SpriteSheetGDX("room/creature.png", 1, 4, 24, 24);
		m_pItems = new SpriteSheetGDX("room/items.png", 1, 3, 16, 16);
		m_pButtonMeta_Base = new SpriteSheetGDX("ui/button_meta.png", 1, 2, 96, 96);
		m_pButtonMeta_Health = new SpriteSheetGDX("ui/button_meta_health.png", 1, 2, 96, 96);
		m_pButtonMeta_Search = new SpriteSheetGDX("ui/button_meta_search.png", 1, 2, 96, 96);
		m_pButtonMeta_Okay = new SpriteSheetGDX("ui/button_meta_okay.png", 1, 2, 96, 96);
		m_pButtonMeta_Yes = new SpriteSheetGDX("ui/button_meta_yes.png", 1, 2, 96, 96);
		m_pButtonMeta_No = new SpriteSheetGDX("ui/button_meta_no.png", 1, 2, 96, 96);
		m_pButtonMeta_Food = new SpriteSheetGDX("ui/button_meta_food.png", 1, 2, 96, 96);
		m_pButtonMeta_Water = new SpriteSheetGDX("ui/button_meta_water.png", 1, 2, 96, 96);
		m_pButtonRoom_Yes = new SpriteSheetGDX("ui/button_rooms_yes.png", 1, 2, 64, 64);
		m_pButtonRoom_No = new SpriteSheetGDX("ui/button_rooms_no.png", 1, 2, 64, 64);
		m_pButtonRoom_Okay = new SpriteSheetGDX("ui/button_rooms_okay.png", 1, 2, 64, 64);
		m_pScratch0_a = new SpriteSheetGDX("room/scratch0_a.png", 1, 3, 270, 283);
		m_pScratch0_b = new SpriteSheetGDX("room/scratch0_b.png", 1, 3, 270, 283);
		
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
		
		// Misc
		m_pNPCBubble = new Texture("room/npcbubble.png");
		m_pDialogueBorder = new Texture("room/ui_top_border.png");
		m_pDialogueShutter = new Texture("room/ui_shutter.png");
		
		// Chatheads
		m_pChatheadNPCAdult = new SpriteSheetGDX("room/chatheads - NPC Adult.png", 1, 4, 64, 64);
		
		// Fonts
		m_pFontParam.color = m_pColorLight;
		m_pFontParam.size = 24;
		m_pFontMetaLarge = fontGenerator0.generateFont(m_pFontParam);
		m_pFontParam.size = 20;
		m_pFontMetaSmall = fontGenerator0.generateFont(m_pFontParam);
		m_pFontParam.size = 18;
		m_pFontRoomDialogue = fontGenerator0.generateFont(m_pFontParam);
		
		m_pStyleMetaLarge = new LabelStyle(m_pFontMetaLarge, m_pColorLight);
		m_pStyleMetaSmall = new LabelStyle(m_pFontMetaSmall, m_pColorLight);
		m_pStyleRoomDialogue = new LabelStyle(m_pFontRoomDialogue, m_pColorLight);
	}
	
	public static Texture getDoor(Door.EPosition i_ePosition)
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
