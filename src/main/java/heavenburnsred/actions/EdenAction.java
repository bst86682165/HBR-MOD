package heavenburnsred.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.cards.CardGroup;

public class EdenAction extends AbstractGameAction {

    // 目前似乎只需要一个空的构造方法
    public EdenAction() {
        this.actionType = AbstractGameAction.ActionType.CARD_MANIPULATION;
        this.duration = Settings.ACTION_DUR_FAST;
    }

    @Override
    public void update() {
        if (this.duration == Settings.ACTION_DUR_FAST) {
            AbstractPlayer p = AbstractDungeon.player;
            plusCardCost(p.hand);
            plusCardCost(p.drawPile);
            plusCardCost(p.discardPile);
            plusCardCost(p.exhaustPile);
        }
        tickDuration();
    }

    private void plusCardCost(CardGroup cardGroup) {
        for (AbstractCard c : cardGroup.group) {
            if (c.costForTurn >= 0) {
                c.costForTurn += 1;
                c.isCostModifiedForTurn = true;
            }
            if (c.cost >= 0) {
                c.cost += 1;
                c.isCostModified = true;
            }
        }
    }
}
