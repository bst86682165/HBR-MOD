package heavenburnsred.cards.attack;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import heavenburnsred.actions.BlockRelatedDamageAction;
import heavenburnsred.character.MyCharacter;
import heavenburnsred.util.CardStats;
import heavenburnsred.util.HBRImageMaster;

public class SettingOfSwordsman extends HBRHitAndTypeAttackCard {
    public static final String ID = makeID(SettingOfSwordsman.class.getSimpleName()); //makeID adds the mod ID, so the final ID will be something like "modID:MyCard"
    public static final CardStats info = new CardStats(
            MyCharacter.Meta.CARD_COLOR, //The card color. If you're making your own character, it'll look something like this. Otherwise, it'll be CardColor.RED or similar for a basegame character color.
            CardType.ATTACK, //The type. ATTACK/SKILL/POWER/CURSE/STATUS
            CardRarity.UNCOMMON, //Rarity. BASIC is for starting cards, then there's COMMON/UNCOMMON/RARE, and then SPECIAL and CURSE. SPECIAL is for cards you only get from events. Curse is for curses, except for special curses like Curse of the Bell and Necronomicurse.
            CardTarget.ENEMY, //The target. Single target is ENEMY, all enemies is ALL_ENEMY. Look at cards similar to what you want to see what to use.
            1);//The card's base cost. -1 is X cost, -2 is no cost for unplayable cards like curses, or Reflex.

    //These will be used in the constructor. Technically you can just use the values directly,
    //but constants at the top of the file are easy to adjust.
    private static final int DAMAGE = 13;
    private static final int UPG_DAMAGE = 5;
    private static final int USED_IN_COMBAT = 0;
    private static final int HP_LOSS = 10;

    public SettingOfSwordsman() {
        super(ID,info); //Pass the required information to the BaseCard constructor.

        setDamage(DAMAGE, UPG_DAMAGE);
        setMagic(USED_IN_COMBAT);  // 用来存储战斗中打出次数
    }

    @Override
    public void applyPowers() {
        super.applyPowers();
        this.rawDescription = cardStrings.DESCRIPTION + cardStrings.EXTENDED_DESCRIPTION[0] + (3-this.baseMagicNumber) + cardStrings.EXTENDED_DESCRIPTION[1];
        initializeDescription();
    }

    public void triggerOnGlowCheck() {
        // 要掉血时显示红色边框
        this.glowColor = (this.baseMagicNumber == 2) ? HBRImageMaster.RED_BORDER_COLOR : AbstractCard.BLUE_BORDER_GLOW_COLOR;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new BlockRelatedDamageAction(m, new DamageInfo(p, damage, DamageInfo.DamageType.NORMAL), AbstractGameAction.AttackEffect.SLASH_VERTICAL, this));
        if (this.baseMagicNumber == 2) {
            addToBot(new LoseHPAction(p, p, HP_LOSS));
            this.baseMagicNumber = 0;
        }
        else {
            this.baseMagicNumber++;
        }
        this.rawDescription = cardStrings.DESCRIPTION + cardStrings.EXTENDED_DESCRIPTION[0] + (3-this.baseMagicNumber) + cardStrings.EXTENDED_DESCRIPTION[1];
        initializeDescription();
    }

    @Override
    public void onBreakBlock() {
        addToBot(new GainEnergyAction(1));
    }

    @Override
    public AbstractCard makeCopy() {
        return new SettingOfSwordsman();
    }
}
