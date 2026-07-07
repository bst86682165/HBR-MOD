package heavenburnsred.cards.skill;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.BlurPower;
import heavenburnsred.cards.BaseCard;
import heavenburnsred.character.MyCharacter;
import heavenburnsred.powers.DrawLessNextTurnPower;
import heavenburnsred.powers.EnergizedLessNextTurnPower;
import heavenburnsred.util.CardStats;

import static heavenburnsred.BasicMod.makeID;

public class StrumtheKillerTune extends BaseCard {
    public static final String ID = makeID(StrumtheKillerTune.class.getSimpleName()); //makeID adds the mod ID, so the final ID will be something like "modID:MyCard"
    public static final CardStats info = new CardStats(
            MyCharacter.Meta.CARD_COLOR, //The card color. If you're making your own character, it'll look something like this. Otherwise, it'll be CardColor.RED or similar for a basegame character color.
            AbstractCard.CardType.SKILL, //The type. ATTACK/SKILL/POWER/CURSE/STATUS
            AbstractCard.CardRarity.RARE, //Rarity. BASIC is for starting cards, then there's COMMON/UNCOMMON/RARE, and then SPECIAL and CURSE. SPECIAL is for cards you only get from events. Curse is for curses, except for special curses like Curse of the Bell and Necronomicurse.
            AbstractCard.CardTarget.SELF, //The target. Single target is ENEMY, all enemies is ALL_ENEMY. Look at cards similar to what you want to see what to use.
            0);//The card's base cost. -1 is X cost, -2 is no cost for unplayable cards like curses, or Reflex.
    private static final int MAGIC = 1;

    public StrumtheKillerTune() {
        super(ID,info); //Pass the required information to the BaseCard constructor
        setExhaust(true);
        setMagic(MAGIC);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        addToBot(new GainEnergyAction(2));
        addToBot(new DrawCardAction(3));
        addToBot(new ApplyPowerAction(p,p,new DrawLessNextTurnPower(p,1),1));
        addToBot(new ApplyPowerAction(p,p,new EnergizedLessNextTurnPower(p,1),1));
        if (this.upgraded){
            addToBot(new ApplyPowerAction(p,p,new BlurPower(p,1),1));
        }
    }


}
