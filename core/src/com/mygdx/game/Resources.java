package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;

public class Resources
{
	Texture m_pBackground;
	Texture m_pNode;
	
	void load()
	{
		m_pBackground = new Texture("room/background_1.png");
		m_pNode = new Texture("ui/node.png");
	}
}
