package com.mygdx.game;

import java.io.IOException;
import java.util.Hashtable;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.XmlReader;

public class LevelLoader
{
	Hashtable<String, Level> m_tLevels = new Hashtable<String, Level>();
	
	XmlReader reader = new XmlReader();
	
	public void parseFile(String i_sFilePath) throws IOException
	{
		// Add a new level
		Level pLevel = new Level(i_sFilePath.substring(0, i_sFilePath.lastIndexOf('.')));
		m_tLevels.put(pLevel.m_sID, pLevel);
		pLevel.parse(reader.parse(Gdx.files.internal(i_sFilePath)));
	}
}
