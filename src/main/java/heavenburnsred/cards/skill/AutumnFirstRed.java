package heavenburnsred.cards.skill;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import heavenburnsred.actions.ApplyHBRStackPowerAction;
import heavenburnsred.actions.ChangeODBarHitAction;
import heavenburnsred.cards.BaseCard;
import heavenburnsred.character.MyCharacter;
import heavenburnsred.powers.AttackUp;
import heavenburnsred.powers.DefendDown;
import heavenburnsred.relics.ODBar;
import heavenburnsred.util.CardStats;

public class AutumnFirstRed extends BaseCard {
    public static final String ID = makeID(AutumnFirstRed.class.getSimpleName()); //makeID adds the mod ID, so the final ID will be something like "modID:MyCard"
    public static final CardStats info = new CardStats(
            MyCharacter.Meta.CARD_COLOR, //The card color. If you're making your own character, it'll look something like this. Otherwise, it'll be CardColor.RED or similar for a basegame character color.
            CardType.SKILL, //The type. ATTACK/SKILL/POWER/CURSE/STATUS
            CardRarity.UNCOMMON, //Rarity. BASIC is for starting cards, then there's COMMON/UNCOMMON/RARE, and then SPECIAL and CURSE. SPECIAL is for cards you only get from events. Curse is for curses, except for special curses like Curse of the Bell and Necronomicurse.
            CardTarget.SELF, //The target. Single target is ENEMY, all enemies is ALL_ENEMY. Look at cards similar to what you want to see what to use.
            0);//The card's base cost. -1 is X cost, -2 is no cost for unplayable cards like curses, or Reflex.

    //These will be used in the constructor. Technically you can just use the values directly,
    //but constants at the top of the file are easy to adjust.
    private static final int MAGIC = 20;
    private static final int UPG_MAGIC = -10;
    private static final int BUFF_LAYER = 1;
    private static final int BUFF_TURN = 1;

    public AutumnFirstRed() {
        super(ID,info); //Pass the required information to the BaseCard constructor.

        setMagic(MAGIC,UPG_MAGIC); //Sets the card's damage and how much it changes when upgraded.
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        boolean canUse = super.canUse(p, m);
        if (!canUse)
            return false;
        if (ODBar.getCounter() < 0) {
            this.cantUseMessage = cardStrings.EXTENDED_DESCRIPTION[0];
            return false;
        }
        return canUse;
    }

    public void triggerOnGlowCheck() {
        this.glowColor = AbstractCard.GOLD_BORDER_GLOW_COLOR.cpy();
        if (ODBar.getCounter() < 0) {
            this.glowColor = AbstractCard.BLUE_BORDER_GLOW_COLOR.cpy();
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyHBRStackPowerAction(p,p,new AttackUp(p, BUFF_TURN, BUFF_LAYER)));
        for (AbstractMonster monster : (AbstractDungeon.getMonsters()).monsters) {
            if (!monster.isDead && !monster.isDying) {
                addToBot(new ApplyHBRStackPowerAction(monster,p,new DefendDown(monster, BUFF_TURN, BUFF_LAYER)));
            }
        }
        addToBot(new ChangeODBarHitAction(-magicNumber));
    }
}
