package com.mygdx.game;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

abstract public class GameState implements InputProcessor
{
	abstract void create(Camera i_pCamera, Resources i_pResources);
	abstract void render(SpriteBatch i_pBatch, ShapeRenderer i_pShapeRenderer, Resources i_pResources);
}
