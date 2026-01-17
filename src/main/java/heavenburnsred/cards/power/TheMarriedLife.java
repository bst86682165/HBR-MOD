package heavenburnsred.cards.power;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import heavenburnsred.cards.BaseCard;
import heavenburnsred.cards.skill.CharlotComposition;
import heavenburnsred.character.MyCharacter;
import heavenburnsred.powers.InnerStrengthPower;
import heavenburnsred.powers.TheMarriedLifePower;
import heavenburnsred.util.CardStats;

public class TheMarriedLife extends BaseCard {
    public static final String ID = makeID(TheMarriedLife.class.getSimpleName());
    public static final CardStats info = new CardStats(
            MyCharacter.Meta.CARD_COLOR, //The card color. If you're making your own character, it'll look something like this. Otherwise, it'll be CardColor.RED or similar for a basegame character color.
            CardType.POWER, //The type. ATTACK/SKILL/POWER/CURSE/STATUS
            CardRarity.RARE, //Rarity. BASIC is for starting cards, then there's COMMON/UNCOMMON/RARE, and then SPECIAL and CURSE. SPECIAL is for cards you only get from events. Curse is for curses, except for special curses like Curse of the Bell and Necronomicurse.
            CardTarget.SELF, //The target. Single target is ENEMY, all enemies is ALL_ENEMY. Look at cards similar to what you want to see what to use.
            -1);//The card's base cost. -1 is X cost, -2 is no cost for unplayable cards like curses, or Reflex.

    public TheMarriedLife() {
        super(ID,info);
        this.cardsToPreview = new CharlotComposition();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int effect = this.energyOnUse;
        if (p.hasRelic("Chemical X")) {
            effect += 2;
        }
        if (this.upgraded) {
            effect += 1;
        }
        addToBot(new ApplyPowerAction(p, p, new TheMarriedLifePower(p, effect), effect));

        if (!this.freeToPlayOnce) {
            p.energy.use(EnergyPanel.totalCount);
        }
    }
}
