package heavenburnsred.cards.skill;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import heavenburnsred.cards.BaseCard;
import heavenburnsred.cards.attack.ElegantSerious;
import heavenburnsred.character.MyCharacter;
import heavenburnsred.powers.ChargePower;
import heavenburnsred.util.CardStats;

public class SummerDream extends BaseCard {
    public static final String ID = makeID(SummerDream.class.getSimpleName()); //makeID adds the mod ID, so the final ID will be something like "modID:MyCard"
    public static final CardStats info = new CardStats(
            MyCharacter.Meta.CARD_COLOR, //The card color. If you're making your own character, it'll look something like this. Otherwise, it'll be CardColor.RED or similar for a basegame character color.
            CardType.SKILL, //The type. ATTACK/SKILL/POWER/CURSE/STATUS
            CardRarity.RARE, //Rarity. BASIC is for starting cards, then there's COMMON/UNCOMMON/RARE, and then SPECIAL and CURSE. SPECIAL is for cards you only get from events. Curse is for curses, except for special curses like Curse of the Bell and Necronomicurse.
            CardTarget.NONE, //The target. Single target is ENEMY, all enemies is ALL_ENEMY. Look at cards similar to what you want to see what to use.
            0);//The card's base cost. -1 is X cost, -2 is no cost for unplayable cards like curses, or Reflex.

    //These will be used in the constructor. Technically you can just use the values directly,
    //but constants at the top of the file are easy to adjust.
    private static final int MAGIC = 2;  // 使用为加攻效果的回合数

    public SummerDream() {
        super(ID,info); //Pass the required information to the BaseCard constructor.
        setMagic(MAGIC); //Sets the card's damage and how much it changes when upgraded.
        this.exhaust = true;
        this.cardsToPreview = new ElegantSerious(true);
    }

    public SummerDream(boolean onlyForDisplay) {
        super(ID,info); //Pass the required information to the BaseCard constructor.
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            if (this.cardsToPreview != null) {
                this.cardsToPreview.upgrade();
            }
        }
        super.upgrade();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        {
            addToBot((AbstractGameAction)new GainEnergyAction(2));
            if (!p.hasPower(ChargePower.POWER_ID)) {
                addToBot(new ApplyPowerAction(p,p,new ChargePower(p,-1)));
            }
            if (this.upgraded) {
                AbstractCard addCard = new ElegantSerious();
                addCard.upgrade();
                addToBot((AbstractGameAction)new MakeTempCardInDrawPileAction(addCard, 1, true, true));
            } else {
                addToBot((AbstractGameAction)new MakeTempCardInDiscardAction((AbstractCard)new ElegantSerious(), 1));
            }
        }
    }

    @Override
    public AbstractCard makeCopy() { //Optional
        return new SummerDream();
    }
}
