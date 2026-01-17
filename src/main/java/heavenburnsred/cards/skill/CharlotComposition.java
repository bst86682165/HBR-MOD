package heavenburnsred.cards.skill;

import com.evacipated.cardcrawl.mod.stslib.patches.FlavorText;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.LightBulbEffect;
import heavenburnsred.cards.BaseCard;
import heavenburnsred.character.MyCharacter;
import heavenburnsred.util.CardStats;

import java.util.ArrayList;

public class CharlotComposition extends BaseCard {
    public static final String ID = makeID(CharlotComposition.class.getSimpleName()); //makeID adds the mod ID, so the final ID will be something like "modID:MyCard"
    public static final CardStats info = new CardStats(
            MyCharacter.Meta.CARD_COLOR, //The card color. If you're making your own character, it'll look something like this. Otherwise, it'll be CardColor.RED or similar for a basegame character color.
            CardType.SKILL, //The type. ATTACK/SKILL/POWER/CURSE/STATUS
            CardRarity.SPECIAL, //Rarity. BASIC is for starting cards, then there's COMMON/UNCOMMON/RARE, and then SPECIAL and CURSE. SPECIAL is for cards you only get from events. Curse is for curses, except for special curses like Curse of the Bell and Necronomicurse.
            CardTarget.NONE, //The target. Single target is ENEMY, all enemies is ALL_ENEMY. Look at cards similar to what you want to see what to use.
            0);//The card's base cost. -1 is X cost, -2 is no cost for unplayable cards like curses, or Reflex.

    //These will be used in the constructor. Technically you can just use the values directly,
    //but constants at the top of the file are easy to adjust.
    private static final int MAGIC_UPG = 1;

    public CharlotComposition() {
        super(ID,info); //Pass the required information to the BaseCard constructor.
        setExhaust(true);
        setSelfRetain(true);
        FlavorText.AbstractCardFlavorFields.flavor.set(this, cardStrings.EXTENDED_DESCRIPTION[1]);
    }

    public void setM(int magic, int flavor_index) {
        setMagic(magic, MAGIC_UPG);
        this.rawDescription = cardStrings.EXTENDED_DESCRIPTION[0];
        initializeDescription();
        FlavorText.AbstractCardFlavorFields.flavor.set(this, cardStrings.EXTENDED_DESCRIPTION[flavor_index]);
    }

    @Override
    public AbstractCard makeStatEquivalentCopy() {
        AbstractCard card = super.makeStatEquivalentCopy();
        card.baseMagicNumber = this.baseMagicNumber;
        card.magicNumber = this.magicNumber;
        card.description = (ArrayList)this.description.clone();
        FlavorText.AbstractCardFlavorFields.flavor.set(card, FlavorText.AbstractCardFlavorFields.flavor.get(this));
        return card;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (Settings.FAST_MODE) {
            addToBot(new VFXAction(new LightBulbEffect(p.hb)));
        } else {
            addToBot(new VFXAction(new LightBulbEffect(p.hb), 0.2F));
        }
        addToBot(new DrawCardAction(p, this.magicNumber));
    }
}
