package com.mygdx.game;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.XmlReader;

public class LevelLoader
{
	static XmlReader reader = new XmlReader();
	static List<Level> m_lpLevels = new ArrayList<Level>();
	
	static public void parseFile(String i_sFilePath) throws IOException
	{
		// Add a new level
		Level pLevel = new Level(i_sFilePath.substring(i_sFilePath.indexOf('/') + 1, i_sFilePath.lastIndexOf('.')));
		m_lpLevels.add(pLevel);
		pLevel.parse(reader.parse(Gdx.files.internal(i_sFilePath)));
		
		// Post-parse linking
		pLevel.linkAll();
	}
	
	static Level getLevelByID(String i_sID)
	{
		for(Level pLevel : m_lpLevels)
		{
			if(pLevel.m_sID.equals(i_sID))
			{
				return pLevel;
			}
		}
		
		return null;
	}
}
