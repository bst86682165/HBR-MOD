package heavenburnsred.cards.attack;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import heavenburnsred.cards.BaseCard;
import heavenburnsred.cards.HbrTags;
import heavenburnsred.cards.skill.SummerDream;
import heavenburnsred.character.MyCharacter;
import heavenburnsred.powers.ChargePower;
import heavenburnsred.util.CardStats;

import java.util.Arrays;

public class ElegantSerious extends BaseCard {
    public static final String ID = makeID(ElegantSerious.class.getSimpleName()); //makeID adds the mod ID, so the final ID will be something like "modID:MyCard"
    public static final CardStats info = new CardStats(
            MyCharacter.Meta.CARD_COLOR, //The card color. If you're making your own character, it'll look something like this. Otherwise, it'll be CardColor.RED or similar for a basegame character color.
            CardType.ATTACK, //The type. ATTACK/SKILL/POWER/CURSE/STATUS
            CardRarity.SPECIAL, //Rarity. BASIC is for starting cards, then there's COMMON/UNCOMMON/RARE, and then SPECIAL and CURSE. SPECIAL is for cards you only get from events. Curse is for curses, except for special curses like Curse of the Bell and Necronomicurse.
            CardTarget.ALL_ENEMY, //The target. Single target is ENEMY, all enemies is ALL_ENEMY. Look at cards similar to what you want to see what to use.
            4);//The card's base cost. -1 is X cost, -2 is no cost for unplayable cards like curses, or Reflex.

    //These will be used in the constructor. Technically you can just use the values directly,
    //but constants at the top of the file are easy to adjust.
    private static final int DAMAGE = 1;
    private static final int HIT = 1;
    private static final int CHARGE_DAMAGE = 50;

    public ElegantSerious() {
        super(ID,info); //Pass the required information to the BaseCard constructor.
        setDamage(DAMAGE); //Sets the card's damage and how much it changes when upgraded.
        this.isMultiDamage = true;
        this.exhaust = true;
        this.cardsToPreview = new SummerDream(true);

        setCustomVar("hit", HIT);
        tags.add(HbrTags.HIT);
        tags.add(HbrTags.LL);
    }

    public ElegantSerious(boolean onlyForDisplay) {
        super(ID,info); //Pass the required information to the BaseCard constructor.
        setDamage(DAMAGE); //Sets the card's damage and how much it changes when upgraded.
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            this.initializeDescription();
            if (this.cardsToPreview != null) {
                this.cardsToPreview.upgrade();
            }
        }
    }

//    private void updateDescriptionInBattle() {
//        String lastDescription;
//        if (this.upgraded) {
//            lastDescription = cardStrings.UPGRADE_DESCRIPTION;
//        } else {
//            lastDescription = cardStrings.EXTENDED_DESCRIPTION[0];
//        }
//        AbstractPlayer player = AbstractDungeon.player;
//        if (player.hasPower(ChargePower.POWER_ID)) {
//            this.rawDescription = cardStrings.DESCRIPTION + cardStrings.EXTENDED_DESCRIPTION[1] + lastDescription;
//        } else {
//            this.rawDescription = cardStrings.DESCRIPTION + cardStrings.EXTENDED_DESCRIPTION[0] + lastDescription;
//        }
//        initializeDescription();
//    }

    private void updateBaseDamage() {
        AbstractPlayer player = AbstractDungeon.player;
        if (player.hasPower(ChargePower.POWER_ID)) {
            this.baseDamage = CHARGE_DAMAGE;
        } else {
            this.baseDamage = DAMAGE;
        }
    }

    @Override
    public void calculateCardDamage(AbstractMonster m) {
        super.calculateCardDamage(m);
//        updateDescriptionInBattle();
        initializeDescription();
    }

    @Override
    public void applyPowers() {
        updateBaseDamage();
        super.applyPowers();
//        updateDescriptionInBattle();
        initializeDescription();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        updateBaseDamage();
        calculateCardDamage(m);
        addToBot(new DamageAllEnemiesAction(p, this.multiDamage, this.damageType, AbstractGameAction.AttackEffect.SLASH_VERTICAL));
        AbstractCard addCard =  new SummerDream();
        if (this.upgraded) {
            addCard.upgrade();
        }
        addToBot((AbstractGameAction)new MakeTempCardInDiscardAction(addCard, 1));
//        updateDescriptionInBattle();
        initializeDescription();
    }

    @Override
    public AbstractCard makeCopy() { //Optional
        return new ElegantSerious();
    }
}
