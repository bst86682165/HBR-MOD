package heavenburnsred.cards.skill;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import heavenburnsred.actions.VisitRightAwayAction;
import heavenburnsred.cards.BaseCard;
import heavenburnsred.character.MyCharacter;
import heavenburnsred.relics.ODBar;
import heavenburnsred.util.CardStats;

public class VisitRightAway extends BaseCard {
    public static final String ID = makeID(VisitRightAway.class.getSimpleName()); //makeID adds the mod ID, so the final ID will be something like "modID:MyCard"
    public static final CardStats info = new CardStats(
            MyCharacter.Meta.CARD_COLOR, //The card color. If you're making your own character, it'll look something like this. Otherwise, it'll be CardColor.RED or similar for a basegame character color.
            CardType.SKILL, //The type. ATTACK/SKILL/POWER/CURSE/STATUS
            CardRarity.RARE, //Rarity. BASIC is for starting cards, then there's COMMON/UNCOMMON/RARE, and then SPECIAL and CURSE. SPECIAL is for cards you only get from events. Curse is for curses, except for special curses like Curse of the Bell and Necronomicurse.
            CardTarget.SELF, //The target. Single target is ENEMY, all enemies is ALL_ENEMY. Look at cards similar to what you want to see what to use.
            0);//The card's base cost. -1 is X cost, -2 is no cost for unplayable cards like curses, or Reflex.

    //These will be used in the constructor. Technically you can just use the values directly,
    //but constants at the top of the file are easy to adjust.
    private static final int MAGIC = 20;

    public VisitRightAway() {
        super(ID,info); //Pass the required information to the BaseCard constructor.
        setMagic(MAGIC); //Sets the card's damage and how much it changes when upgraded.
        setExhaust(true);
        setSelfRetain(true);
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        return p.gold >= this.magicNumber;
    }

    public void triggerOnGlowCheck() {
        if (AbstractDungeon.player.gold >= this.magicNumber) {
            this.glowColor = AbstractCard.BLUE_BORDER_GLOW_COLOR.cpy();
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new VisitRightAwayAction(this.upgraded));
        p.loseGold(this.magicNumber);
        CardCrawlGame.sound.play("EVENT_PURCHASE");
    }
}
