package heavenburnsred.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.screens.CardRewardScreen;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToDiscardEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToHandEffect;
import heavenburnsred.powers.AttributeCal;

import java.util.ArrayList;

import static com.megacrit.cardcrawl.dungeons.AbstractDungeon.cardRandomRng;
import static com.megacrit.cardcrawl.dungeons.AbstractDungeon.srcRareCardPool;

// 在已经有该power的时候直接return，不会发生任何事，这样的power可以调用本方法而不是ApplyPowerAction来上power
public class VisitRightAwayAction extends AbstractGameAction {
    private boolean retrieveCard = false;
    private final boolean upgraded;

    public VisitRightAwayAction(boolean upgraded) {
        this.actionType = AbstractGameAction.ActionType.CARD_MANIPULATION;
        this.duration = Settings.ACTION_DUR_FAST;
        this.amount = 1;
        this.upgraded = upgraded;
    }

    public void update() {
        ArrayList<AbstractCard> generatedCards = generateCardChoices(this.upgraded);

        if (this.duration == Settings.ACTION_DUR_FAST) {
            AbstractDungeon.cardRewardScreen.customCombatOpen(generatedCards, CardRewardScreen.TEXT[1], true);
            tickDuration();
            return;
        }
        if (!this.retrieveCard) {
            if (AbstractDungeon.cardRewardScreen.discoveryCard != null) {
                AbstractCard disCard = AbstractDungeon.cardRewardScreen.discoveryCard.makeStatEquivalentCopy();
                if (AbstractDungeon.player.hasPower("MasterRealityPower")) {
                    disCard.upgrade();
                }
                disCard.setCostForTurn(0);
                disCard.current_x = -1000.0F * Settings.xScale;
                if (AbstractDungeon.player.hand.size() < 10) {
                    AbstractDungeon.effectList.add(new ShowCardAndAddToHandEffect(disCard, Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F));
                } else {
                    AbstractDungeon.effectList.add(new ShowCardAndAddToDiscardEffect(disCard, Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F));
                }
                AbstractDungeon.cardRewardScreen.discoveryCard = null;
            }
            this.retrieveCard = true;
        }
        tickDuration();
    }

    private ArrayList<AbstractCard> generateCardChoices(boolean upgraded) {
        ArrayList<AbstractCard> derp = new ArrayList<>();
        ArrayList<AbstractCard> rareCardList = new ArrayList<>();
        for(AbstractCard c : srcRareCardPool.group) {
            if (!c.hasTag(AbstractCard.CardTags.HEALING)) {
                rareCardList.add(c);
                UnlockTracker.markCardAsSeen(c.cardID);
            }
        }
        while (derp.size() != 3) {
            boolean dupe = false;
            AbstractCard tmp = rareCardList.get(cardRandomRng.random(rareCardList.size() - 1));
            for (AbstractCard c : derp) {
                if (c.cardID.equals(tmp.cardID)) {
                    dupe = true;
                    break;
                }
            }
            if (!dupe) {
                AbstractCard addCard = tmp.makeCopy();
                if (upgraded) {
                    addCard.upgrade();
                }
                derp.add(addCard);
            }
        }
        return derp;
    }
}
