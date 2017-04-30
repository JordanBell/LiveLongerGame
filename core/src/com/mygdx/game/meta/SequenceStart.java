package com.mygdx.game.meta;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.mygdx.game.InputButton;
import com.mygdx.game.LiveLongerGame;
import com.mygdx.game.Resources;
import com.mygdx.game.SaveData;
import com.mygdx.game.SaveData.EProgress;

public class SequenceStart extends Sequence
{
	SequenceStart()
	{
		super("start");
	}

	@Override
	public String getTopString()
	{
		return "Your daughter is dying.\nYour son is missing.";
	}

	@Override
	public void update() {}
	

	public void setLeft(InputButton i_pButton, Label i_pLabel)
	{
		i_pButton.m_pSpriteSheetStates = Resources.m_pButtonMeta_Health;
		i_pLabel.setText("Heal Her");
	}

	public void setMid(InputButton i_pButton, Label i_pLabel)
	{
		i_pButton.m_pSpriteSheetStates = null;
		i_pButton.m_bIsActive = false;
		i_pLabel.setVisible(false);
	}

	public void setRight(InputButton i_pButton, Label i_pLabel)
	{
		i_pButton.m_pSpriteSheetStates = Resources.m_pButtonMeta_Search;
		i_pLabel.setText("Find Him");
	}
	
	@Override
	public void onLeft()
	{
		loadRooms(EProgress.Rooms1_Heal);
	}

	@Override
	public void onRight()
	{
		loadRooms(EProgress.Rooms1_Find);
	}

	@Override
	public void onMid()
	{
		throw new RuntimeException("Error state");
	}
}
