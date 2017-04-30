package com.mygdx.game.rooms;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.game.SpriteSheetGDX;

public class AnimThing extends Anim
{
	public AnimThing()
	{
		m_pSpriteSheet = new SpriteSheetGDX("room/thing.png", 1, 3, 32, 32);
		
		// Configure the frames
		ArrayList<Frame> pFrames = new ArrayList<Frame>();
		pFrames.add(new Frame(0.25f, 0));
		pFrames.add(new Frame(0.3f, 1));
		pFrames.add(new Frame(0.4f, 2));
		pFrames.add(new Frame(0.3f, 1));
		pFrames.add(new Frame(0.25f, 0));
		
		m_pAnimStates.add(pFrames);
	}
}
