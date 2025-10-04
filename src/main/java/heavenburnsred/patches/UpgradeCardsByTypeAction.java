package heavenburnsred.patches;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.ThoughtBubble;

import java.util.ArrayList;
import java.util.function.Predicate;

public class UpgradeCardsByTypeAction extends AbstractGameAction {
    private Predicate<AbstractCard> cardFilter;
    private String cardTypeName;

    public UpgradeCardsByTypeAction(Predicate<AbstractCard> filter, String typeName) {
        this.cardFilter = filter;
        this.cardTypeName = typeName;
        this.actionType = ActionType.CARD_MANIPULATION;
    }

    @Override
    public void update() {
        int upgradedCount = 0;
        upgradedCount += upgradeCardsInGroup(AbstractDungeon.player.hand.group);
        upgradedCount += upgradeCardsInGroup(AbstractDungeon.player.drawPile.group);
        upgradedCount += upgradeCardsInGroup(AbstractDungeon.player.discardPile.group);
        if (upgradedCount > 0) {
            playEffects(upgradedCount);
        }
        AbstractDungeon.player.hand.refreshHandLayout();
        this.isDone = true;
    }

    private int upgradeCardsInGroup(ArrayList<AbstractCard> cardGroup) {
        int count = 0;
        for (AbstractCard card : cardGroup) {
            if (cardFilter.test(card) && !card.upgraded) {
                card.upgrade();
                count++;
            }
        }
        return count;
    }

    private void playEffects(int upgradedCount) {
        CardCrawlGame.sound.play("CARD_UPGRADE");
        AbstractDungeon.effectList.add(new ThoughtBubble(
                AbstractDungeon.player.dialogX, AbstractDungeon.player.dialogY, 3.0f,
                "共计 " + (upgradedCount+1) + " 张" + cardTypeName + "获得了升级！", true
        ));
    }
}
