package com.mygdx.game;

import com.badlogic.gdx.math.Vector2;

public class GridGuide
{
	static final Vector2 sk_vOrigin = new Vector2(39, 144);
	
	static Vector2 get7x7(int i_iX, int i_iY)
	{
		Vector2 vLocalPos = new Vector2(i_iX * 32.f, i_iY * 32.f);
		return vLocalPos.add(sk_vOrigin);
	}
}
