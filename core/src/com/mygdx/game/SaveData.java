package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class SaveData
{
	Preferences m_pSaveData = Gdx.app.getPreferences("Save");
	
	void save()
	{
		m_pSaveData.flush();
	}
}
