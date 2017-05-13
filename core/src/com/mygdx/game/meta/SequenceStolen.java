package com.mygdx.game.meta;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.mygdx.game.InputButton;
import com.mygdx.game.LiveLongerGame;
import com.mygdx.game.Resources;
import com.mygdx.game.SaveData;
import com.mygdx.game.SaveData.EProgress;

public class SequenceStolen extends Sequence
{
	enum EState 
	{
		Start_ChildTalk, Start_ChildTalk_Yes, Start_ChildTalk_No,
		HungrySelf, HungrySelf_Yes, HungrySelf_No,
		HungryChild, HungryChild_Yes, HungryChild_No,
		Man1, Man1_Kill, Man1_Leave,
		ThirstySelf, ThirstySelf_Yes, ThirstySelf_No,
		ThirstyChild, ThirstyChild_Yes, ThirstyChild_No,
		SecondMan, QuestionDeath, QuestionChild,
		
		SeenFriend_Yes, SeenFriend_No,
		Child_No,
		
		ChildTaken, BringThreeGems, KnockedOut
	}
	
	EState m_eState = EState.Start_ChildTalk;
	
	boolean m_bDidKill;
	boolean m_bDidFeedSelf;
	boolean m_bDidQuenchSelf;
	
	SequenceStolen()
	{
		super("stolen");
	}

	@Override
	public String getTopString()
	{
		switch (m_eState)
		{
			case Start_ChildTalk: 		return SaveData.m_sChild + " bored. Talk?";
			case Start_ChildTalk_Yes: 	return SaveData.m_sChild + " is happy you talk.";
			case Start_ChildTalk_No: 	return SaveData.m_sChild + " looks sad.";

			case HungrySelf: 			return "You are hungry. Eat food can?";
			case HungrySelf_Yes:		return "Feel nourished."; 
			case HungrySelf_No:			return "Can afford to go without."; 
			
			case HungryChild: 			return SaveData.m_sChild + " hungry. Give Food can?";
			case HungryChild_Yes:		return m_bDidFeedSelf ? "Plenty of food." 	: SaveData.getChildProSubj() + " can't afford to go without. Food given."; 
			case HungryChild_No:		return m_bDidFeedSelf ? "Not for " + SaveData.getChildProObj() + "." : "Set an example."; 

			case Man1:					return "Suddenly, door opens.\nMan say, \"Found you by accident. Let me leave. I tell no one you're here.\"";
			case Man1_Kill:				return "Send " + SaveData.m_sChild.toLowerCase() + " away. Knife silences man. None know you're here.";
			case Man1_Leave:			return "\"Thank you. Will leave.\" \nMan walks away. Don't trust him.";
			
			case ThirstySelf: 			return "You are thirsty. Drink water bottle?";
			case ThirstySelf_Yes:		return "Feel quenched."; 
			case ThirstySelf_No:		return "Need to ration."; 
			
			case ThirstyChild: 			return SaveData.m_sChild + " thirsty. Give water bottle?";
			case ThirstyChild_Yes:		return m_bDidQuenchSelf ? "Plenty of water." : SaveData.getChildProSubj() + " needs to survive. Water given"; 
			case ThirstyChild_No:		return m_bDidQuenchSelf ? "None for " + SaveData.getChildProObj() + "." 	: "Neither did you.";

			case SecondMan:				return "You hear a knock on the door. You open it.";
			case QuestionDeath: 		return "\"My friend came through here. Have you seen him?\"";
			case QuestionChild: 		return "\"My friend tells me you have a " + SaveData.m_sChild.toLowerCase() + " here.\"";	
			
			case Child_No:				return "\"You lie. Should not.\"";
			case SeenFriend_No:			return "\"You lie. Should not.\nI see you have " + SaveData.m_sChild.toLowerCase() + ". A life, for a life.\"";
			case SeenFriend_Yes:		return "\"Killer.\nI see you have " + SaveData.m_sChild.toLowerCase() + ". A life, for a life.\"";
			
			case ChildTaken:			return "Man pushes past. " + SaveData.m_sChild + " cries out. Another man appears, holds you down.";
			case BringThreeGems:		return "\"Want " + SaveData.getChildProObj() + " to live longer? Bring three Throbbing Gems.\"";
			case KnockedOut:			return "Pain. Black. Wake up hours later. " + SaveData.m_sChild + " gone.";
			
			default: 					return "[Unimplemented]";
		}
	}

	@Override
	public void update() {}
	
	public void setLeft(InputButton i_pButton, Label i_pLabel)
	{
		switch(m_eState)
		{
			// Unary answer states
			case Start_ChildTalk_Yes:
			case Start_ChildTalk_No:
			case HungrySelf_Yes:
			case HungrySelf_No:
			case ThirstySelf_Yes:
			case ThirstySelf_No:
			case HungryChild_Yes:
			case HungryChild_No:
			case ThirstyChild_Yes:
			case ThirstyChild_No:
			case Man1_Kill:
			case Man1_Leave:
			case SecondMan:
			case Child_No:
			case SeenFriend_No:
			case SeenFriend_Yes:
			case ChildTaken:
			case BringThreeGems:
			case KnockedOut:
			{
				// No button
				i_pButton.m_bIsActive = false;
				i_pButton.m_pSpriteSheetStates = null;
				i_pLabel.setText("");
				i_pLabel.setVisible(false);
				break;
			}

			// Yes/No
			case Man1:
				i_pButton.m_bIsActive = true;
				i_pButton.m_pSpriteSheetStates = Resources.m_pButtonMeta_Yes;
				i_pLabel.setVisible(true);
				i_pLabel.setText("Spare him.");
				break;
				
			case Start_ChildTalk:
			case QuestionChild:
			case QuestionDeath:
				i_pButton.m_bIsActive = true;
				i_pButton.m_pSpriteSheetStates = Resources.m_pButtonMeta_Yes;
				i_pLabel.setText("");
				i_pLabel.setVisible(false);
				break;
			
			// Food
			case HungrySelf:
			case HungryChild:
				i_pButton.m_bIsActive = true;
				i_pButton.m_pSpriteSheetStates = Resources.m_pButtonMeta_Food;
				i_pLabel.setVisible(true);
				i_pLabel.setText("Eat");
				break;
			
			// Water
			case ThirstySelf:
			case ThirstyChild:
				i_pButton.m_bIsActive = true;
				i_pButton.m_pSpriteSheetStates = Resources.m_pButtonMeta_Water;
				i_pLabel.setVisible(true);
				i_pLabel.setText("Drink");
				break;
		}
	}

	public void setRight(InputButton i_pButton, Label i_pLabel)
	{
		switch(m_eState)
		{
			// Unary answer states
			case Start_ChildTalk_Yes:
			case Start_ChildTalk_No:
			case HungrySelf_Yes:
			case HungrySelf_No:
			case ThirstySelf_Yes:
			case ThirstySelf_No:
			case HungryChild_Yes:
			case HungryChild_No:
			case ThirstyChild_Yes:
			case ThirstyChild_No:
			case Man1_Kill:
			case Man1_Leave:
			case SecondMan:
			case Child_No:
			case SeenFriend_No:
			case SeenFriend_Yes:
			case ChildTaken:
			case BringThreeGems:
			case KnockedOut:
			{
				// No button
				i_pButton.m_bIsActive = false;
				i_pButton.m_pSpriteSheetStates = null;
				i_pLabel.setText("");
				i_pLabel.setVisible(false);
				break;
			}

			// Yes/No
			case Man1:
				i_pButton.m_bIsActive = true;
				i_pButton.m_pSpriteSheetStates = Resources.m_pButtonMeta_No;
				i_pLabel.setVisible(true);
				i_pLabel.setText("Kill him.");
				break;

			case Start_ChildTalk:
			case QuestionChild:
			case QuestionDeath:
				i_pButton.m_bIsActive = true;
				i_pButton.m_pSpriteSheetStates = Resources.m_pButtonMeta_No;
				i_pLabel.setText("");
				i_pLabel.setVisible(false);
				break;
			
			// Food/Water
			case HungrySelf:
			case HungryChild:
			case ThirstySelf:
			case ThirstyChild:
				i_pButton.m_bIsActive = true;
				i_pButton.m_pSpriteSheetStates = Resources.m_pButtonMeta_No;
				i_pLabel.setVisible(true);
				i_pLabel.setText("Ration");
				break;
		}
	}

	public void setMid(InputButton i_pButton, Label i_pLabel)
	{
		switch(m_eState)
		{
			// Unary answer states
			case Start_ChildTalk_Yes:
			case Start_ChildTalk_No:
			case HungrySelf_Yes:
			case HungrySelf_No:
			case ThirstySelf_Yes:
			case ThirstySelf_No:
			case HungryChild_Yes:
			case HungryChild_No:
			case ThirstyChild_Yes:
			case ThirstyChild_No:
			case Man1_Kill:
			case Man1_Leave:
			case SecondMan:
			case BringThreeGems:
			{
				// Okay button
				i_pButton.m_bIsActive = true;
				i_pButton.m_pSpriteSheetStates = Resources.m_pButtonMeta_Okay;
				i_pLabel.setText("");
				i_pLabel.setVisible(false);
				break;
			}

			case Child_No:
			case SeenFriend_No:
			case SeenFriend_Yes:
			case ChildTaken:
			case KnockedOut:
			{
				// No button
				i_pButton.m_bIsActive = true;
				i_pButton.m_pSpriteSheetStates = Resources.m_pButtonMeta_No;
				i_pLabel.setText("");
				i_pLabel.setVisible(false);
				break;
			}

			// Yes/No
			case Start_ChildTalk:
			case QuestionChild:
			case QuestionDeath:
			case Man1:
			case HungrySelf:
			case HungryChild:
			case ThirstySelf:
			case ThirstyChild:
			{
				// No button
				i_pButton.m_bIsActive = false;
				i_pButton.m_pSpriteSheetStates = null;
				i_pLabel.setText("");
				i_pLabel.setVisible(false);
				break;
			}
		}
	}

	@Override
	public void onMid()
	{
		switch(m_eState)
		{
			// Unary answer states
			case Start_ChildTalk_Yes:
			case Start_ChildTalk_No:
				m_eState = EState.HungrySelf;
				break;
				
			case HungrySelf_Yes:
			case HungrySelf_No:
				m_bDidFeedSelf = m_eState == EState.HungrySelf_Yes;
				m_eState = EState.HungryChild;
				break;
				
			case HungryChild_Yes:
			case HungryChild_No:
				m_eState = EState.Man1;
				break;
				
			case Man1_Kill:
			case Man1_Leave:
				m_bDidKill = m_eState == EState.Man1_Kill;
				m_eState = EState.ThirstySelf;
				break;
				
			case ThirstySelf_Yes:
			case ThirstySelf_No:
				m_bDidQuenchSelf = m_eState == EState.ThirstySelf_Yes;
				m_eState = EState.ThirstyChild;
				break;
				
			case ThirstyChild_Yes:
			case ThirstyChild_No:
				m_eState = EState.SecondMan;
				break;
				
			case SecondMan:
				if(m_bDidKill)
				{
					m_eState = EState.QuestionDeath;
					break;
				}
				else
				{
					m_eState = EState.QuestionChild;
					break;
				}
				
			case Child_No:
				m_eState = EState.ChildTaken;
				break;
				
			case SeenFriend_No:
			case SeenFriend_Yes:
				m_eState = EState.ChildTaken;
				break;
				
			case ChildTaken:
			case BringThreeGems:
			case KnockedOut:
				if(m_eState.ordinal() < EState.values().length - 1)
				{
					m_eState = EState.values()[m_eState.ordinal() + 1];
					if(m_eState == EState.KnockedOut)
					{
						// Camera shake
						 LiveLongerGame.setCameraShake(); 
					}
				}
				else
				{
					loadRooms(EProgress.Rooms2_Market);
				}
				break;

			// Yes/No
			case Start_ChildTalk:
			case QuestionChild:
			case QuestionDeath:
			case Man1:
			case HungrySelf:
			case HungryChild:
			case ThirstySelf:
			case ThirstyChild:
				throw new RuntimeException("Invalid state");
			
			default: throw new RuntimeException("State not handled.");
		}
	}
	
	@Override
	public void onLeft()
	{
		switch(m_eState)
		{
			// Unary answer states
			case Start_ChildTalk_Yes:
			case Start_ChildTalk_No:
			case HungrySelf_Yes:
			case HungrySelf_No:
			case ThirstySelf_Yes:
			case ThirstySelf_No:
			case HungryChild_Yes:
			case HungryChild_No:
			case ThirstyChild_Yes:
			case ThirstyChild_No:
			case Man1_Kill:
			case Man1_Leave:
			case SecondMan:
			case BringThreeGems:
			case Child_No:
			case SeenFriend_No:
			case SeenFriend_Yes:
			case ChildTaken:
			case KnockedOut:
				throw new RuntimeException("Invalid state");

			// Yes
			case Start_ChildTalk:	m_eState = EState.Start_ChildTalk_Yes; break; 
			case QuestionChild:		m_eState = EState.ChildTaken; break;
			case QuestionDeath:		m_eState = EState.SeenFriend_Yes; break;
			case Man1:				m_eState = EState.Man1_Leave; break;
			case HungrySelf:		m_eState = EState.HungrySelf_Yes; break;
			case HungryChild:		m_eState = EState.HungryChild_Yes; break;
			case ThirstySelf:		m_eState = EState.ThirstySelf_Yes; break;
			case ThirstyChild:		m_eState = EState.ThirstyChild_Yes; break;
			
			default: throw new RuntimeException("State not handled.");
		}
	}

	@Override
	public void onRight()
	{
		switch(m_eState)
		{
			// Unary answer states
			case Start_ChildTalk_Yes:
			case Start_ChildTalk_No:
			case HungrySelf_Yes:
			case HungrySelf_No:
			case ThirstySelf_Yes:
			case ThirstySelf_No:
			case HungryChild_Yes:
			case HungryChild_No:
			case ThirstyChild_Yes:
			case ThirstyChild_No:
			case Man1_Kill:
			case Man1_Leave:
			case SecondMan:
			case BringThreeGems:
			case Child_No:
			case SeenFriend_No:
			case SeenFriend_Yes:
			case ChildTaken:
			case KnockedOut:
				throw new RuntimeException("Invalid state: " + m_eState);

			// No
			case Start_ChildTalk:	m_eState = EState.Start_ChildTalk_No; break; 
			case QuestionChild:		m_eState = EState.Child_No; break;
			case QuestionDeath:		m_eState = EState.SeenFriend_No; break;
			case Man1:				m_eState = EState.Man1_Kill; LiveLongerGame.setCameraShake(); break;
			case HungrySelf:		m_eState = EState.HungrySelf_No; break;
			case HungryChild:		m_eState = EState.HungryChild_No; break;
			case ThirstySelf:		m_eState = EState.ThirstySelf_No; break;
			case ThirstyChild:		m_eState = EState.ThirstyChild_No; break;
			
			default: throw new RuntimeException("State not handled.");
		}
	}
}
