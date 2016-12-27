package com.mygdx.game;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.math.Vector2;

public class Node
{
	Vector2 m_vPos;
	boolean m_bConnectionsRendered;
	List<Node> m_lpNeighbors = new ArrayList<Node>();
	
	Node(Vector2 i_vPos)
	{
		m_vPos = i_vPos;
	}
}
