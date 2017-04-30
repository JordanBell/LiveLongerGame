package com.mygdx.game.meta;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.mygdx.game.InputButton;
import com.mygdx.game.LiveLongerGame;
import com.mygdx.game.SaveData;
import com.mygdx.game.SaveData.EProgress;
import com.mygdx.game.SpriteSheetGDX;

abstract public class Sequence
{
	String m_sID;
	static GameState_Meta s_pGame;
	
	Sequence(String i_sID)
	{
		m_sID = i_sID;
	}
	
	public void loadRooms(EProgress i_eProgress)
	{
		s_pGame.m_sLevelStart = i_eProgress.getLevel();
		LiveLongerGame.s_bTransitionState = true;
		SaveData.setProgress(i_eProgress);
		SaveData.save();
	}

	public abstract void update();
	public abstract String getTopString();
	public abstract void setLeft(InputButton i_pButton, Label i_pLabel);
	public abstract void setMid(InputButton i_pButton, Label i_pLabel);
	public abstract void setRight(InputButton i_pButton, Label i_pLabel);
	public abstract void onLeft();
	public abstract void onMid();
	public abstract void onRight();
}
