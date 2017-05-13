package com.mygdx.game.meta;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.mygdx.game.InputButton;
import com.mygdx.game.Resources;
import com.mygdx.game.SaveData;
import com.mygdx.game.SaveData.EProgress;

public class SequenceDream2 extends Sequence
{
	enum EState 
	{
		Start_Sleep,
		Start_Sleep_No,
		s1,
		s2,
		s3,
		s4,
		s5,
		s6
	};
	
	EState m_eState = EState.Start_Sleep;
	
	SequenceDream2()
	{
		super("dream2");
	}

	@Override
	public String getTopString()
	{
		switch (m_eState)
		{
			case Start_Sleep: 		return "Sleep?";
			case Start_Sleep_No: 	return "No choice.";

			case s1: 				return "Hazy dream. Find doctor. Bring home. \"Help.\"";
			case s2: 				return "Walk in. Silent room. No screaming.";
			case s3: 				return "Twins.";
			case s4: 				return "Three hearts sleeping.\nTwo hearts beating.\nOne heart stopped.";
			case s5: 				return "\"No.\n Live longer.\"";
			case s6: 				return "\"Live longer.\"";
			default: 				return "[Unimplemented]";
		}
	}

	@Override
	public void update() {}
	
	public void setLeft(InputButton i_pButton, Label i_pLabel)
	{
		switch(m_eState)
		{
			// Unary answer states
			case Start_Sleep_No:
			case s1:
			case s2:
			case s3:
			case s4:
			case s5:
			case s6:
			{
				// No button
				i_pButton.m_bIsActive = false;
				i_pButton.m_pSpriteSheetStates = null;
				i_pLabel.setText("");
				i_pLabel.setVisible(false);
				break;
			}

			// Yes/No
			case Start_Sleep:
				i_pButton.m_bIsActive = true;
				i_pButton.m_pSpriteSheetStates = Resources.m_pButtonMeta_Yes;
				i_pLabel.setVisible(false);
				break;
		}
	}

	public void setRight(InputButton i_pButton, Label i_pLabel)
	{
		switch(m_eState)
		{
			// Unary answer states
			case Start_Sleep_No:
			case s1:
			case s2:
			case s3:
			case s4:
			case s5:
			case s6:
			{
				// No button
				i_pButton.m_bIsActive = false;
				i_pButton.m_pSpriteSheetStates = null;
				i_pLabel.setText("");
				i_pLabel.setVisible(false);
				break;
			}

			// Yes/No
			case Start_Sleep:
				i_pButton.m_bIsActive = true;
				i_pButton.m_pSpriteSheetStates = Resources.m_pButtonMeta_No;
				i_pLabel.setVisible(false);
				break;
		}
	}

	public void setMid(InputButton i_pButton, Label i_pLabel)
	{
		switch(m_eState)
		{
			// No
			case s1:
			case s2:
			case s3:
			case s4:
			case s5:
			case s6:
			{
				i_pButton.m_bIsActive = true;
				i_pButton.m_pSpriteSheetStates = Resources.m_pButtonMeta_No;
				i_pLabel.setVisible(false);
				break;
			}

			// Okay
			case Start_Sleep_No:
				i_pButton.m_bIsActive = true;
				i_pButton.m_pSpriteSheetStates = Resources.m_pButtonMeta_Okay;
				i_pLabel.setVisible(false);
				break;

			// Yes/No
			case Start_Sleep:
				// Button hidden
				i_pButton.m_bIsActive = false;
				i_pButton.m_pSpriteSheetStates = null;
				i_pLabel.setText("");
				i_pLabel.setVisible(false);
				break;
		}
	}

	@Override
	public void onMid()
	{
		switch(m_eState)
		{
			// No
			case Start_Sleep_No:
			case s1:
			case s2:
			case s3:
			case s4:
			case s5:
				m_eState = EState.values()[m_eState.ordinal() + 1];
				break;

			case s6:
				loadRooms(EProgress.Rooms2_Market);
				break;

			// Yes/No
			case Start_Sleep:
				throw new RuntimeException("Invalid state");
			
			default: throw new RuntimeException("State not handled.");
		}
	}
	
	@Override
	public void onLeft()
	{
		switch(m_eState)
		{
			// No
			case Start_Sleep_No:
			case s1:
			case s2:
			case s3:
			case s4:
			case s5:
			case s6:
				throw new RuntimeException("Invalid state");

			// Yes
			case Start_Sleep:	m_eState = EState.s1; break; 
			
			default: throw new RuntimeException("State not handled.");
		}
	}

	@Override
	public void onRight()
	{
		switch(m_eState)
		{
			// No
			case Start_Sleep_No:
			case s1:
			case s2:
			case s3:
			case s4:
			case s5:
			case s6:
				throw new RuntimeException("Invalid state: " + m_eState);

			// No
			case Start_Sleep:	m_eState = EState.Start_Sleep_No; break; 
			
			default: throw new RuntimeException("State not handled.");
		}
	}
}
