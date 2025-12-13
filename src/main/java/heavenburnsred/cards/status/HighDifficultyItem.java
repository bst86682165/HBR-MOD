package heavenburnsred.cards.status;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import heavenburnsred.actions.ChangeODBarHitAction;
import heavenburnsred.cards.BaseCard;
import heavenburnsred.character.MyCharacter;
import heavenburnsred.relics.ODBar;
import heavenburnsred.util.CardStats;

public class HighDifficultyItem extends BaseCard {
    public static final String ID = makeID(HighDifficultyItem.class.getSimpleName()); //makeID adds the mod ID, so the final ID will be something like "modID:MyCard"
    public static final CardStats info = new CardStats(
            MyCharacter.Meta.CARD_COLOR, //The card color. If you're making your own character, it'll look something like this. Otherwise, it'll be CardColor.RED or similar for a basegame character color.
            CardType.STATUS, //The type. ATTACK/SKILL/POWER/CURSE/STATUS
            CardRarity.SPECIAL, //Rarity. BASIC is for starting cards, then there's COMMON/UNCOMMON/RARE, and then SPECIAL and CURSE. SPECIAL is for cards you only get from events. Curse is for curses, except for special curses like Curse of the Bell and Necronomicurse.
            CardTarget.NONE, //The target. Single target is ENEMY, all enemies is ALL_ENEMY. Look at cards similar to what you want to see what to use.
            -2);//The card's base cost. -1 is X cost, -2 is no cost for unplayable cards like curses, or Reflex.

    //These will be used in the constructor. Technically you can just use the values directly,
    //but constants at the top of the file are easy to adjust.

    public HighDifficultyItem() {
        super(ID,info); //Pass the required information to the BaseCard constructor.

        setEthereal(true);
    }

    @Override
    public void triggerWhenDrawn() {
        addToBot(new ChangeODBarHitAction(-ODBar.getCounter()));
    }

    // 不能升级
    @Override
    public void upgrade() {
    }

    // 使用无事发生
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
    }
}
