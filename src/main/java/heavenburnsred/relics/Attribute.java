package heavenburnsred.relics;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import heavenburnsred.powers.AttributeCal;

import static heavenburnsred.BasicMod.makeID;
import static heavenburnsred.patches.MonsterPointMap.MPMap;

public class Attribute extends BaseRelic
{
    private static final String NAME = "Attribute"; // The name will be used for determining the image file as well as the
    // ID.
    public static final String ID = makeID(NAME); // This adds the mod's prefix to the relic ID, resulting in
    // modID:MyRelic
    private static final AbstractRelic.RelicTier RARITY = AbstractRelic.RelicTier.COMMON; // The relic's rarity.
    private static final AbstractRelic.LandingSound SOUND = AbstractRelic.LandingSound.CLINK; // The sound played when
    // the relic is clicked.

    public static int hbrLL = 0;
    public static int hbrLQ = 0;
    public static int hbrTJ = 0;
    public static int hbrZY = 0;
    public static int ATTpoint = 0;

    public Attribute() {
        super(ID, NAME, RARITY, SOUND);
    }

    public void atBattleStartPreDraw(){
        addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new AttributeCal(AbstractDungeon.player, 1)));
    }

    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (card.type == AbstractCard.CardType.ATTACK) {
            String Mon = action.target.id;
            int floor = AbstractDungeon.floorNum;
            int act = AbstractDungeon.actNum;
            int MonPoint = MPMap.get(Mon) + 6 * act + floor / 4;

            if (card.cardID.endsWith("_LL")){
                ATTpoint = (hbrLL * 2 + hbrLQ) / 3 - MonPoint;
            }
            else if (card.cardID.endsWith("_LQ")){
                ATTpoint = (hbrLL + hbrLQ * 2) / 3 - MonPoint;
            }
            else if (card.cardID.endsWith("_TJ")){
                ATTpoint = hbrTJ - MonPoint;
            }
            else if (card.cardID.endsWith("_ZY")){
                ATTpoint = hbrZY - MonPoint;
            }
            else if (card.cardID.endsWith("_WP")){
                ATTpoint = (hbrLL + hbrLQ) / 2 - MonPoint;
            }
        }
    }


}
