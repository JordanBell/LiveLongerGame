package com.mygdx.game.meta;

import java.util.ArrayList;
import java.util.List;

import com.mygdx.game.SaveData;

public class SequenceManager
{
	List<Sequence> m_lpSequences = new ArrayList<Sequence>();
	
	SequenceManager()
	{
		m_lpSequences.add(new SequenceStart());
		m_lpSequences.add(new SequenceStolen());
		m_lpSequences.add(new SequenceDream1());
		m_lpSequences.add(new SequenceDream2());
	}
	
	public Sequence getCurrent()
	{
		return m_lpSequences.get(3);
	}
}
