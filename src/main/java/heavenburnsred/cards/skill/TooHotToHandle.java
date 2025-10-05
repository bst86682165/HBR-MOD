package heavenburnsred.cards.skill;

import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import heavenburnsred.cards.BaseCard;
import heavenburnsred.character.MyCharacter;
import heavenburnsred.patches.ApplyHBRStackPowerAction;
import heavenburnsred.powers.DefendDown;
import heavenburnsred.util.CardStats;

public class TooHotToHandle extends BaseCard {
    public static final String ID = makeID(TooHotToHandle.class.getSimpleName()); //makeID adds the mod ID, so the final ID will be something like "modID:MyCard"
    public static final CardStats info = new CardStats(
            MyCharacter.Meta.CARD_COLOR, //The card color. If you're making your own character, it'll look something like this. Otherwise, it'll be CardColor.RED or similar for a basegame character color.
            CardType.SKILL, //The type. ATTACK/SKILL/POWER/CURSE/STATUS
            CardRarity.UNCOMMON, //Rarity. BASIC is for starting cards, then there's COMMON/UNCOMMON/RARE, and then SPECIAL and CURSE. SPECIAL is for cards you only get from events. Curse is for curses, except for special curses like Curse of the Bell and Necronomicurse.
            CardTarget.ALL_ENEMY, //The target. Single target is ENEMY, all enemies is ALL_ENEMY. Look at cards similar to what you want to see what to use.
            1);//The card's base cost. -1 is X cost, -2 is no cost for unplayable cards like curses, or Reflex.

    //These will be used in the constructor. Technically you can just use the values directly,
    //but constants at the top of the file are easy to adjust.
    private static final int MAGIC = 1;  // 使用为加攻效果的回合数


    public TooHotToHandle() {
        super(ID,info); //Pass the required information to the BaseCard constructor.
        setMagic(MAGIC); //Sets the card's damage and how much it changes when upgraded.
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
        {
            for (AbstractMonster monster : (AbstractDungeon.getMonsters()).monsters) {
                if (!monster.isDead && !monster.isDying) {
                    addToBot(new ApplyHBRStackPowerAction(m,p,new DefendDown(AbstractDungeon.player,magicNumber,1)));
                    if (monster.currentBlock > 0){
                        addToBot(new ApplyHBRStackPowerAction(m,p,new DefendDown(AbstractDungeon.player,magicNumber,1)));
                    }
                }
            }
            if (this.upgraded){
                addToBot(new MakeTempCardInHandAction(new FallingintoaFantasy(),3));
            }
        }
    }

    @Override
    public AbstractCard makeCopy() { //Optional
        return new TooHotToHandle();
    }
}
