package com.mygdx.game;

import com.badlogic.gdx.math.Vector2;

public class GridGuide
{
	static final Vector2 sk_vOrigin = new Vector2(45, 143);
	
	static Vector2 get7x7(int i_iX, int i_iY)
	{
		Vector2 vLocalPos = new Vector2(i_iX * 30.f, i_iY * 30.f);
		return vLocalPos.add(sk_vOrigin);
	}
}
