package heavenburnsred.cards.PointCard;


import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import heavenburnsred.cards.BaseCard;
import heavenburnsred.character.MyCharacter;
import heavenburnsred.relics.Attribute;
import heavenburnsred.util.CardStats;

import static heavenburnsred.relics.Attribute.AddTempAttribute;

public class LLcard extends BaseCard {
    public static final String ID = makeID(LLcard.class.getSimpleName()); //makeID adds the mod ID, so the final ID will be something like "modID:MyCard"
    public static final CardStats info = new CardStats(
            MyCharacter.Meta.CARD_COLOR, //The card color. If you're making your own character, it'll look something like this. Otherwise, it'll be CardColor.RED or similar for a basegame character color.
            CardType.POWER, //The type. ATTACK/SKILL/POWER/CURSE/STATUS
            CardRarity.SPECIAL, //Rarity. BASIC is for starting cards, then there's COMMON/UNCOMMON/RARE, and then SPECIAL and CURSE. SPECIAL is for cards you only get from events. Curse is for curses, except for special curses like Curse of the Bell and Necronomicurse.
            CardTarget.SELF, //The target. Single target is ENEMY, all enemies is ALL_ENEMY. Look at cards similar to what you want to see what to use.
            -2);//T

    public LLcard() {
        super(ID,info); //Pass the required information to the BaseCard constructor.
    }

    @Override
    public void upgrade() {
    }

    @Override
    // 给debug时直接把对应点数卡加到手牌里用
    public void use(AbstractPlayer abstractPlayer, AbstractMonster abstractMonster) {
        AbstractRelic attribute = AbstractDungeon.player.getRelic(Attribute.ID);
        ((Attribute)attribute).plusHbrLL(1);
        AddTempAttribute(0,0,0,0);
    }

    @Override
    public void onChoseThisOption() {
        AbstractRelic attribute = AbstractDungeon.player.getRelic(Attribute.ID);
        ((Attribute)attribute).plusHbrLL(1);
        ((Attribute)attribute).onSelectPoint();
    }

    @Override
    public AbstractCard makeCopy() {
        return new LLcard();
    }
}
