package heavenburnsred.cards.attack;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import heavenburnsred.actions.ApplyHBRStackPowerAction;
import heavenburnsred.actions.BreakBlockHBRDamageAction;
import heavenburnsred.character.MyCharacter;
import heavenburnsred.powers.AttackUp;
import heavenburnsred.util.CardStats;

// 可镰的破盾
public class DoubleInOne_L extends HBRHitAndTypeAttackCard {
    public static final String ID = makeID(DoubleInOne_L.class.getSimpleName()); //makeID adds the mod ID, so the final ID will be something like "modID:MyCard"
    public static final CardStats info = new CardStats(
            MyCharacter.Meta.CARD_COLOR, //The card color. If you're making your own character, it'll look something like this. Otherwise, it'll be CardColor.RED or similar for a basegame character color.
            CardType.ATTACK, //The type. ATTACK/SKILL/POWER/CURSE/STATUS
            CardRarity.SPECIAL, //Rarity. BASIC is for starting cards, then there's COMMON/UNCOMMON/RARE, and then SPECIAL and CURSE. SPECIAL is for cards you only get from events. Curse is for curses, except for special curses like Curse of the Bell and Necronomicurse.
            CardTarget.ENEMY, //The target. Single target is ENEMY, all enemies is ALL_ENEMY. Look at cards similar to what you want to see what to use.
            1);//The card's base cost. -1 is X cost, -2 is no cost for unplayable cards like curses, or Reflex.

    //These will be used in the constructor. Technically you can just use the values directly,
    //but constants at the top of the file are easy to adjust.
    private static final int DAMAGE = 4;
    private static final int UPG_DAMAGE = 1;
    private static final int HIT = 8;
    private static final int AttackUpTurn = 1;
    private static final int AttackUpLayers = 1;

    public DoubleInOne_L() {
        super(ID,info,HIT); //Pass the required information to the BaseCard constructor.

        setDamage(DAMAGE, UPG_DAMAGE); //Sets the card's damage and how much it changes when upgraded.
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        onChoseThisOption();
    }

    @Override
    public void onChoseThisOption() {
        AbstractPlayer p = AbstractDungeon.player;
        AbstractMonster m = DoubleInOne.getNowTargetMonster();
        DoubleInOne.setNowTargetMonster(null);
        if (m != null && AbstractDungeon.getMonsters().monsters.contains(m) && !m.isDeadOrEscaped()) {
            addToBot(new BreakBlockHBRDamageAction(m, new DamageInfo(p, damage, DamageInfo.DamageType.NORMAL), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL, this));
            addToBot(new BreakBlockHBRDamageAction(m, new DamageInfo(p, damage, DamageInfo.DamageType.NORMAL), AbstractGameAction.AttackEffect.SLASH_DIAGONAL, this));
            if (m.currentBlock > 0) {
                // 打第三段，相当于1.5倍伤害
                addToBot(new BreakBlockHBRDamageAction(m, new DamageInfo(p, damage, DamageInfo.DamageType.NORMAL), AbstractGameAction.AttackEffect.SLASH_VERTICAL, this));
            }
        }
    }

    @Override
    public void onBreakBlock() {
        AbstractPlayer p = AbstractDungeon.player;
        addToBot(new ApplyHBRStackPowerAction(p,p,new AttackUp(p, AttackUpTurn, AttackUpLayers)));
    }

    @Override
    public AbstractCard makeCopy() { //Optional
        return new DoubleInOne_L();
    }
}
