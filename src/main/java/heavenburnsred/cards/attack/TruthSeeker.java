package heavenburnsred.cards.attack;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import heavenburnsred.cards.skill.SummerDream;
import heavenburnsred.character.MyCharacter;
import heavenburnsred.powers.ChargePower;
import heavenburnsred.util.CardStats;

import java.util.ArrayList;


public class TruthSeeker extends HBRHitAndTypeAttackCard {
    public static final String ID = makeID(TruthSeeker.class.getSimpleName()); //makeID adds the mod ID, so the final ID will be something like "modID:MyCard"
    public static final CardStats info = new CardStats(
            MyCharacter.Meta.CARD_COLOR, //The card color. If you're making your own character, it'll look something like this. Otherwise, it'll be CardColor.RED or similar for a basegame character color.
            CardType.ATTACK, //The type. ATTACK/SKILL/POWER/CURSE/STATUS
            CardRarity.UNCOMMON, //Rarity. BASIC is for starting cards, then there's COMMON/UNCOMMON/RARE, and then SPECIAL and CURSE. SPECIAL is for cards you only get from events. Curse is for curses, except for special curses like Curse of the Bell and Necronomicurse.
            CardTarget.ALL_ENEMY, //The target. Single target is ENEMY, all enemies is ALL_ENEMY. Look at cards similar to what you want to see what to use.
            0);//The card's base cost. -1 is X cost, -2 is no cost for unplayable cards like curses, or Reflex.

    //These will be used in the constructor. Technically you can just use the values directly,
    //but constants at the top of the file are easy to adjust.
    private static final int DAMAGE = 6;
    private static final int UPG_DAMAGE = 4;
    private static final int MAGIC = 3;
    private static final int UPG_MAGIC = 2;
    private static final int HIT = 3;

    public TruthSeeker() {
        super(ID,info,HBRAttackType.LQ,HIT); //Pass the required information to the BaseCard constructor.
        setDamage(DAMAGE,UPG_DAMAGE); //Sets the card's damage and how much it changes when upgraded.
        this.isMultiDamage = true;
        this.exhaust = true;
        this.isInnate = true;
        setMagic(MAGIC,UPG_MAGIC);
    }

    @Override
    public void applyPowers() {
        int tmpDamage = this.baseDamage;
        this.baseDamage = this.baseMagicNumber;
        super.applyPowers();
        this.magicNumber = this.damage;
        this.isMagicNumberModified = this.isDamageModified;
        this.baseDamage = tmpDamage;
        super.applyPowers();
    }

    @Override
    public void calculateCardDamage(AbstractMonster m) {
        super.calculateCardDamage(m);
        ArrayList<AbstractMonster> mo = AbstractDungeon.getCurrRoom().monsters.monsters;
        int mon_size = mo.size();
        for (int i = 0; i < mon_size; i++) {
            if (mo.get(i).currentBlock > 0) {
                this.multiDamage[i] += this.magicNumber;
            }
        }
    }

    // 造成群体伤害
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DamageAllEnemiesAction(p, this.multiDamage, this.damageType, AbstractGameAction.AttackEffect.SLASH_VERTICAL));
    }
}
