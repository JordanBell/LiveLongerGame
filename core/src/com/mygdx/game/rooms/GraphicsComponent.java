package com.mygdx.game.rooms;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public abstract class GraphicsComponent
{
	public abstract void draw(SpriteBatch i_pBatch, float x, float y);
	public abstract void update();
	public abstract float getWidth();
	public abstract float getHeight();
	
	public Vector2 getCenter()
	{
		return new Vector2(getWidth() * 0.5f, getHeight() * 0.5f);
	}
}
