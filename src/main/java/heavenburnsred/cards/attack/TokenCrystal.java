package heavenburnsred.cards.attack;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import heavenburnsred.actions.ApplyHBRStackPowerAction;
import heavenburnsred.character.MyCharacter;
import heavenburnsred.powers.AttackUp;
import heavenburnsred.powers.TokenPower;
import heavenburnsred.util.CardStats;

public class TokenCrystal extends HBRHitAndTypeAttackCard {
    public static final String ID = makeID(TokenCrystal.class.getSimpleName()); //makeID adds the mod ID, so the final ID will be something like "modID:MyCard"
    public static final CardStats info = new CardStats(
            MyCharacter.Meta.CARD_COLOR, //The card color. If you're making your own character, it'll look something like this. Otherwise, it'll be CardColor.RED or similar for a basegame character color.
            CardType.ATTACK, //The type. ATTACK/SKILL/POWER/CURSE/STATUS
            CardRarity.UNCOMMON, //Rarity. BASIC is for starting cards, then there's COMMON/UNCOMMON/RARE, and then SPECIAL and CURSE. SPECIAL is for cards you only get from events. Curse is for curses, except for special curses like Curse of the Bell and Necronomicurse.
            CardTarget.ENEMY, //The target. Single target is ENEMY, all enemies is ALL_ENEMY. Look at cards similar to what you want to see what to use.
            1);//The card's base cost. -1 is X cost, -2 is no cost for unplayable cards like curses, or Reflex.

    //These will be used in the constructor. Technically you can just use the values directly,
    //but constants at the top of the file are easy to adjust.
    private static final int DAMAGE = 2;
    private static final int TOKEN_GAIN = 2;
    private static final int ATTACKUP_GAIN = 1;
    private static final int ATTACKUP_GAIN_UPGRADE = 1;
    private static final int HIT = 5;

    public TokenCrystal() {
        super(ID,info,HBRAttackType.LL,HIT); //Pass the required information to the BaseCard constructor.

        setMagic(ATTACKUP_GAIN, ATTACKUP_GAIN_UPGRADE);
        setDamage(DAMAGE);
    }

    @Override
    public void applyPowers() {
        // 根据信念层数确定基础伤害baseDamage
        AbstractCreature p = AbstractDungeon.player;
        if (p.hasPower(TokenPower.POWER_ID)) {
            int token_layers = p.getPower(TokenPower.POWER_ID).amount + 2;
            if (token_layers > 10) token_layers = 10;
            this.baseDamage = token_layers;
        }
        else {
            this.baseDamage = DAMAGE;
        }

        super.applyPowers();

        // 增加当前伤害的描述
        this.rawDescription = cardStrings.DESCRIPTION + cardStrings.EXTENDED_DESCRIPTION[0];
        initializeDescription();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new TokenPower(p, TOKEN_GAIN), TOKEN_GAIN));
        addToBot(new DamageAction(m, new DamageInfo(p, damage, DamageInfo.DamageType.NORMAL), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
        addToBot(new DamageAction(m, new DamageInfo(p, damage, DamageInfo.DamageType.NORMAL), AbstractGameAction.AttackEffect.SLASH_VERTICAL));
        addToBot(new ApplyHBRStackPowerAction(p,p,new AttackUp(p, magicNumber, 1)));
        initializeDescription();
    }

    @Override
    public void onMoveToDiscard() {
        this.rawDescription = cardStrings.DESCRIPTION;
        initializeDescription();
    }

}
