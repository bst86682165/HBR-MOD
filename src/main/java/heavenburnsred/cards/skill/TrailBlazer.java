package heavenburnsred.cards.skill;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import heavenburnsred.cards.BaseCard;
import heavenburnsred.character.MyCharacter;
import heavenburnsred.powers.CriticalHit;
import heavenburnsred.util.CardStats;

public class TrailBlazer extends BaseCard {
    public static final String ID = makeID(TrailBlazer.class.getSimpleName()); //makeID adds the mod ID, so the final ID will be something like "modID:MyCard"
    public static final CardStats info = new CardStats(
            MyCharacter.Meta.CARD_COLOR, //The card color. If you're making your own character, it'll look something like this. Otherwise, it'll be CardColor.RED or similar for a basegame character color.
            CardType.SKILL, //The type. ATTACK/SKILL/POWER/CURSE/STATUS
            CardRarity.UNCOMMON, //Rarity. BASIC is for starting cards, then there's COMMON/UNCOMMON/RARE, and then SPECIAL and CURSE. SPECIAL is for cards you only get from events. Curse is for curses, except for special curses like Curse of the Bell and Necronomicurse.
            CardTarget.SELF, //The target. Single target is ENEMY, all enemies is ALL_ENEMY. Look at cards similar to what you want to see what to use.
            2);//The card's base cost. -1 is X cost, -2 is no cost for unplayable cards like curses, or Reflex.

    private static final int MAGIC = 10;  // 使用为加攻效果的回合数

    public TrailBlazer() {
        super(ID,info); //Pass the required information to the BaseCard constructor.
        //Sets the card's damage and how much it changes when upgraded.
        setMagic(MAGIC);
    }

    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GainBlockAction(p,magicNumber));
        addToBot(new ApplyPowerAction(p,p,new CriticalHit(p,1)));
        if(this.upgraded){
            for (AbstractPower power : p.powers){
                if(power.type == AbstractPower.PowerType.DEBUFF){
                    addToBot(new RemoveSpecificPowerAction(p,p,power));
                }
            }
        }
    }

    @Override
    public AbstractCard makeCopy() { //Optional
        return new TrailBlazer();
    }
}

