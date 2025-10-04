package heavenburnsred.patches;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DiscardAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import heavenburnsred.cards.skill.FallingintoaFantasy;

import java.util.ArrayList;

public class SoaringExcitementAction extends AbstractGameAction {
    private int drawAmount = 3;
    private String targetCardId = FallingintoaFantasy.ID;
    private int actuallyDrawn = 0;
    private int discardAmount = 0;
    private boolean NextStage = true;

    public SoaringExcitementAction() {
        this(false,0);
    }
    public SoaringExcitementAction(boolean Next, int DisCount){
        this.NextStage = Next;
        discardAmount = DisCount;
        this.actionType = ActionType.CARD_MANIPULATION;
    }


    @Override
    public void update() {
        if (!NextStage) {
            int handSizeBefore = AbstractDungeon.player.hand.size();
            addToBot(new DrawCardAction(drawAmount));
            addToBot(new AbstractGameAction() {
                @Override
                public void update() {
                    actuallyDrawn = (AbstractDungeon.player.hand.size() - handSizeBefore);
                    discardAmount = actuallyDrawn;
                    countTargetCards();
                    addToBot(new SoaringExcitementAction(true,discardAmount));
                    this.isDone = true;
                }
            });
        } else {
            if(discardAmount > 0) {
                addToBot(new DiscardAction(AbstractDungeon.player, AbstractDungeon.player, discardAmount, false));
            }
        }
        this.isDone = true;
    }

        private void countTargetCards() {
        ArrayList<AbstractCard> hand = AbstractDungeon.player.hand.group;
        for (int i = hand.size() - 1; i >= 0 && actuallyDrawn > 0; i--) {
            AbstractCard card = hand.get(i);
            if (card.cardID.equals(targetCardId)) {
                discardAmount--;
            }
            actuallyDrawn--;
        }
    }
}
