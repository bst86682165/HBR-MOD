package heavenburnsred.cards.attack;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import heavenburnsred.character.MyCharacter;
import heavenburnsred.powers.AttackLossHPPower;
import heavenburnsred.powers.TokenPower;
import heavenburnsred.util.CardStats;

public class TokenRememberForever extends HBRHitAndTypeAttackCard {
    public static final String ID = makeID(TokenRememberForever.class.getSimpleName()); //makeID adds the mod ID, so the final ID will be something like "modID:MyCard"
    public static final CardStats info = new CardStats(
            MyCharacter.Meta.CARD_COLOR, //The card color. If you're making your own character, it'll look something like this. Otherwise, it'll be CardColor.RED or similar for a basegame character color.
            CardType.ATTACK, //The type. ATTACK/SKILL/POWER/CURSE/STATUS
            CardRarity.RARE, //Rarity. BASIC is for starting cards, then there's COMMON/UNCOMMON/RARE, and then SPECIAL and CURSE. SPECIAL is for cards you only get from events. Curse is for curses, except for special curses like Curse of the Bell and Necronomicurse.
            CardTarget.ALL_ENEMY, //The target. Single target is ENEMY, all enemies is ALL_ENEMY. Look at cards similar to what you want to see what to use.
            2);//The card's base cost. -1 is X cost, -2 is no cost for unplayable cards like curses, or Reflex.

    //These will be used in the constructor. Technically you can just use the values directly,
    //but constants at the top of the file are easy to adjust.
    private static final int DAMAGE = 20;
    private static final int UPG_DAMAGE = 5;
    private static final int TOKEN_GAIN = 2;
    private static final int HIT = 2;
    private static final int LIFE_LOSS = 3;
    private static final int LIFE_LOSS_TURN = 2;
    private static final float ATTACK_ENHANCE = 0.1f;

    public TokenRememberForever() {
        super(ID,info,HBRAttackType.LL,HIT); //Pass the required information to the BaseCard constructor.

        setMagic(TOKEN_GAIN);
        setDamage(DAMAGE, UPG_DAMAGE);
        this.isMultiDamage = true;
    }

    @Override
    public void applyPowers() {
        AbstractCreature p = AbstractDungeon.player;
        if (p.hasPower(TokenPower.POWER_ID)) {
            int tmp_damage = this.upgraded ? DAMAGE + UPG_DAMAGE : DAMAGE;
            this.baseDamage = MathUtils.floor(tmp_damage * (1 + p.getPower(TokenPower.POWER_ID).amount * ATTACK_ENHANCE));
        }
        super.applyPowers();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DamageAllEnemiesAction(p, this.multiDamage, this.damageType, AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
        addToBot(new ApplyPowerAction(p, p, new TokenPower(p, this.magicNumber), this.magicNumber));
        addToBot(new ApplyPowerAction(p, p, new AttackLossHPPower(p, LIFE_LOSS_TURN, LIFE_LOSS), LIFE_LOSS_TURN));
    }

}
