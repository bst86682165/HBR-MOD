package heavenburnsred.patches;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.Objects;

public class CountCards {
    public CountCards (){
    }
    public int CountCardsInWholeDeck(String CardID){
        int CardCount = 0;
        String TargetID = CardID;
        for (AbstractCard card : AbstractDungeon.player.drawPile.group) {
            if (Objects.equals(card.cardID, TargetID)) {
                CardCount++;
            }
        }
        for (AbstractCard card : AbstractDungeon.player.hand.group) {
            if (Objects.equals(card.cardID, TargetID)) {
                CardCount++;
            }
        }
        for (AbstractCard card : AbstractDungeon.player.discardPile.group) {
            if (Objects.equals(card.cardID, TargetID)) {
                CardCount++;
            }
        }
        return CardCount;
    }
}
