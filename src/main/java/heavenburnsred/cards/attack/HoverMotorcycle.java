package heavenburnsred.cards.attack;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import heavenburnsred.character.MyCharacter;
import heavenburnsred.util.CardStats;

import java.util.ArrayList;

public class HoverMotorcycle extends HBRHitAndTypeAttackCard {
    public static final String ID = makeID(HoverMotorcycle.class.getSimpleName());
    public static final CardStats info = new CardStats(
            MyCharacter.Meta.CARD_COLOR, //The card color. If you're making your own character, it'll look something like this. Otherwise, it'll be CardColor.RED or similar for a basegame character color.
            CardType.ATTACK, //The type. ATTACK/SKILL/POWER/CURSE/STATUS
            CardRarity.SPECIAL, //Rarity. BASIC is for starting cards, then there's COMMON/UNCOMMON/RARE, and then SPECIAL and CURSE. SPECIAL is for cards you only get from events. Curse is for curses, except for special curses like Curse of the Bell and Necronomicurse.
            CardTarget.ALL_ENEMY, //The target. Single target is ENEMY, all enemies is ALL_ENEMY. Look at cards similar to what you want to see what to use.
            1);//The card's base cost. -1 is X cost, -2 is no cost for unplayable cards like curses, or Reflex.

    public HoverMotorcycle() {
        super(ID,info,HBRAttackType.LL);
        setCostUpgrade(0);
        this.isMultiDamage = true;
    }

    public void setInitialDamage(int d) {
        setDamage(d);
        this.rawDescription = cardStrings.EXTENDED_DESCRIPTION[0];
        initializeDescription();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DamageAllEnemiesAction(p, this.multiDamage, this.damageType, AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
    }

    @Override
    public AbstractCard makeStatEquivalentCopy() {
        AbstractCard card = super.makeStatEquivalentCopy();
        card.baseDamage = this.baseDamage;
        card.damage = this.damage;
        card.description = (ArrayList)this.description.clone();
        return card;
    }
}
