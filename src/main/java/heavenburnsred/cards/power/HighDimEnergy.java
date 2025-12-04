package heavenburnsred.cards.power;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import heavenburnsred.cards.BaseCard;
import heavenburnsred.character.MyCharacter;
import heavenburnsred.powers.HighDimEnergyPower;
import heavenburnsred.powers.InnerStrengthPower;
import heavenburnsred.util.CardStats;

public class HighDimEnergy extends BaseCard {
    public static final String ID = makeID(HighDimEnergy.class.getSimpleName());
    public static final CardStats info = new CardStats(
            MyCharacter.Meta.CARD_COLOR, //The card color. If you're making your own character, it'll look something like this. Otherwise, it'll be CardColor.RED or similar for a basegame character color.
            CardType.POWER, //The type. ATTACK/SKILL/POWER/CURSE/STATUS
            CardRarity.UNCOMMON, //Rarity. BASIC is for starting cards, then there's COMMON/UNCOMMON/RARE, and then SPECIAL and CURSE. SPECIAL is for cards you only get from events. Curse is for curses, except for special curses like Curse of the Bell and Necronomicurse.
            CardTarget.SELF, //The target. Single target is ENEMY, all enemies is ALL_ENEMY. Look at cards similar to what you want to see what to use.
            1);//The card's base cost. -1 is X cost, -2 is no cost for unplayable cards like curses, or Reflex.

    public final int MAGIC = 3;
    public final int MAGIC_UPG = 1;

    public HighDimEnergy() {
        super(ID,info);

        setMagic(MAGIC, MAGIC_UPG);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new HighDimEnergyPower(p, this.magicNumber), this.magicNumber));
    }
}
