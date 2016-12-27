package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.utils.Align;

public class Resources
{
	SaveData m_pSaveData = new SaveData();
	
	Texture m_pBackground;
	Texture m_pNode;
	
	// Sprite Sheets
	SpriteSheetGDX m_pPlayer;
	SpriteSheetGDX m_pButtonMeta_Base;
	SpriteSheetGDX m_pButtonMeta_Health;
	SpriteSheetGDX m_pButtonMeta_Search;
	
	
	// Fonts
	public Color m_pColorDark = new Color(28.f / 255.f, 20.f / 255.f, 26.f / 255.f, 1.f);
	public Color m_pColorLight = new Color(152.f / 255.f, 165.f / 255.f, 139.f / 255.f, 1.f);
	
	private FreeTypeFontGenerator fontGenerator0 = new FreeTypeFontGenerator(Gdx.files.internal("fonts/chary.ttf"));
	private FreeTypeFontGenerator fontGenerator1 = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Golden-Sun.ttf"));
	private FreeTypeFontGenerator fontGenerator3 = new FreeTypeFontGenerator(Gdx.files.internal("fonts/retganon.ttf"));
	private FreeTypeFontGenerator fontGenerator4 = new FreeTypeFontGenerator(Gdx.files.internal("fonts/RUBBBB__.TTF"));
	private FreeTypeFontParameter m_pParamMeta = new FreeTypeFontParameter();
	private BitmapFont m_pFontMetaLarge = null;
	private BitmapFont m_pFontMetaSmall = null;
	public LabelStyle m_pStyleMetaLarge = null;
	public LabelStyle m_pStyleMetaSmall = null;
	
	void load()
	{
		m_pBackground = new Texture("room/background_1.png");
		m_pNode = new Texture("ui/node.png");
		m_pPlayer = new SpriteSheetGDX("child.png", 1, 2, 12, 14);
		m_pButtonMeta_Base = new SpriteSheetGDX("ui/button_meta.png", 1, 2, 96, 96);
		m_pButtonMeta_Health = new SpriteSheetGDX("ui/button_meta_health.png", 1, 2, 96, 96);
		m_pButtonMeta_Search = new SpriteSheetGDX("ui/button_meta_search.png", 1, 2, 96, 96);
		
		// Fonts
		m_pParamMeta.color = m_pColorLight;
		m_pParamMeta.size = 24;
		m_pFontMetaLarge = fontGenerator0.generateFont(m_pParamMeta);
		m_pParamMeta.size = 20;
		m_pFontMetaSmall = fontGenerator0.generateFont(m_pParamMeta);
		
		m_pStyleMetaLarge = new LabelStyle(m_pFontMetaLarge, m_pColorLight);
		m_pStyleMetaSmall = new LabelStyle(m_pFontMetaSmall, m_pColorLight);
	}
}
