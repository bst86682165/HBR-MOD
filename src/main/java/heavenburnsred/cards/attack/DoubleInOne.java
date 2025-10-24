package heavenburnsred.cards.attack;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.watcher.ChooseOneAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import heavenburnsred.character.MyCharacter;
import heavenburnsred.util.CardStats;

import java.util.ArrayList;

public class DoubleInOne extends HBRHitAndTypeAttackCard {
    public static final String ID = makeID(DoubleInOne.class.getSimpleName()); //makeID adds the mod ID, so the final ID will be something like "modID:MyCard"
    public static final CardStats info = new CardStats(
            MyCharacter.Meta.CARD_COLOR, //The card color. If you're making your own character, it'll look something like this. Otherwise, it'll be CardColor.RED or similar for a basegame character color.
            CardType.ATTACK, //The type. ATTACK/SKILL/POWER/CURSE/STATUS
            CardRarity.RARE, //Rarity. BASIC is for starting cards, then there's COMMON/UNCOMMON/RARE, and then SPECIAL and CURSE. SPECIAL is for cards you only get from events. Curse is for curses, except for special curses like Curse of the Bell and Necronomicurse.
            CardTarget.ENEMY, //The target. Single target is ENEMY, all enemies is ALL_ENEMY. Look at cards similar to what you want to see what to use.
            1);//The card's base cost. -1 is X cost, -2 is no cost for unplayable cards like curses, or Reflex.

    //These will be used in the constructor. Technically you can just use the values directly,
    //but constants at the top of the file are easy to adjust.
    private static final int DAMAGE = 4;
    private static final int UPG_DAMAGE = 1;
    private static final int HIT = 8;
    private static AbstractMonster NowTargetMonster;

    public DoubleInOne() {
        super(ID,info,HIT); //Pass the required information to the BaseCard constructor.

        setDamage(DAMAGE, UPG_DAMAGE); //Sets the card's damage and how much it changes when upgraded.
    }

    public static AbstractMonster getNowTargetMonster() {
        return NowTargetMonster;
    }

    public static void setNowTargetMonster(AbstractMonster nowTargetMonster) {
        NowTargetMonster = nowTargetMonster;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        NowTargetMonster = m;
        ArrayList<AbstractCard> stanceChoices = new ArrayList<>();
        AbstractCard DoubleInOne_L_Choice = new DoubleInOne_L();
        AbstractCard DoubleInOne_R_Choice = new DoubleInOne_R();
        DoubleInOne_L_Choice.damage = this.damage;
        DoubleInOne_R_Choice.damage = this.damage;
        stanceChoices.add(DoubleInOne_L_Choice);
        stanceChoices.add(DoubleInOne_R_Choice);
        if (this.upgraded)
            for (AbstractCard c : stanceChoices)
                c.upgrade();
        addToBot(new ChooseOneAction(stanceChoices));
    }

    @Override
    public AbstractCard makeCopy() { //Optional
        return new DoubleInOne();
    }
}
