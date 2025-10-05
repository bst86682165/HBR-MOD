package heavenburnsred.patches;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ExhaustAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import heavenburnsred.cards.skill.FallingintoaFantasy;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class WaveofAffectionAction extends AbstractGameAction {
    public WaveofAffectionAction() {
        this.actionType = AbstractGameAction.ActionType.CARD_MANIPULATION;
    }

    public void update() {
        int count = AbstractDungeon.player.hand.size();
        addToTop(new MakeTempCardInHandAction(new FallingintoaFantasy(),count));
        for (int i = 0; i < count; i++) {
            if (Settings.FAST_MODE) {
                addToTop(new ExhaustAction(1, true, true, false, Settings.ACTION_DUR_XFAST));
            } else {
                addToTop(new ExhaustAction(1, true, true));
            }
        }
        this.isDone = true;
    }


}
