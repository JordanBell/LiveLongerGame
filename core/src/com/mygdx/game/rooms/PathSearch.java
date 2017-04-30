package com.mygdx.game.rooms;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.math.Vector2;

public class PathSearch
{
	public static List<Node> getPath(Node i_pFrom, Node i_pTo, NodeContainer i_pContainer)
	{
		// Get the path recursively
		return getPathRec(i_pFrom, i_pTo, new ArrayList<Node>(), i_pContainer);
	}

	private static List<Node> getPathRec(Node i_pFrom, Node i_pTo, List<Node> i_pExplored, NodeContainer i_pContainer)
	{
		// Add this node to the explored nodes
		i_pExplored.add(i_pFrom);
		
		if(i_pFrom == i_pTo)
		{
			List<Node> r_lpPath = new ArrayList<Node>();
			r_lpPath.add(i_pTo);
			return r_lpPath;
		}
		else
		{
			// Get paths along all neighbor connections
			List<List<Node>> lsChildPaths = new ArrayList<List<Node>>();
			for(Connection pCon : i_pFrom.m_lpConnections)
			{
				if(pCon.isBlocked()) continue;
				
				Node pNeighbor = i_pContainer.getNodeByID(pCon.m_sToID);
				if(pNeighbor == null) continue;
				
				// Skip if already explored
				if(i_pExplored.contains(pNeighbor)) continue;
				
				// Add the path from the neighbor to the destination
				List<Node> pChildPath = getPathRec(pNeighbor, i_pTo, new ArrayList<Node>(i_pExplored), i_pContainer);
				if(!pChildPath.isEmpty())
				{
					pChildPath.add(0, i_pFrom);
					lsChildPaths.add(pChildPath);
				}
			}
			
			if(lsChildPaths.isEmpty())
			{
				// Leaf node; return empty, indicating error
				return new ArrayList<Node>();
			}
			
			// Sort by length
			lsChildPaths = sortPathsByLength(lsChildPaths);
			
			// Add the contents of the 
			return lsChildPaths.get(0);
		}
	}
	
	// Insertion sort
	private static List<List<Node>> sortPathsByLength(List<List<Node>> i_pPath)
	{
		List<List<Node>> r_lp = new ArrayList<List<Node>>();
		for(List<Node> pPath : i_pPath)
		{
			float fPathLength = getPathLength(pPath);
			boolean bIsAdded = false;
			for(int i = 0; !bIsAdded && i < r_lp.size(); ++i)
			{
				if(getPathLength(r_lp.get(i)) > fPathLength)
				{
					r_lp.add(i, pPath);
					bIsAdded = true;
				}
			}
			
			if(!bIsAdded)
			{
				// Add it to the end
				r_lp.add(pPath);
			}
		}
		return r_lp;
	}
	
	private static float getPathLength(List<Node> i_pPath)
	{
		float r_f = 0.f;
		for(int i = 0; i < i_pPath.size() - 1; ++i)
		{
			Vector2 vDiff = i_pPath.get(i).m_vPos.cpy().sub(i_pPath.get(i+1).m_vPos);
			r_f += vDiff.len();
		}
		return r_f;
	}
}
