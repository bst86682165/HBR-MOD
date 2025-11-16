package heavenburnsred.cards.attack;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.unique.DamagePerAttackPlayedAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import heavenburnsred.character.MyCharacter;
import heavenburnsred.util.CardStats;

import java.util.HashSet;
import java.util.Set;

public class HBRNuclearExplosion extends HBRHitAndTypeAttackCard {
    public static final String ID = makeID(HBRNuclearExplosion.class.getSimpleName());
    public static final CardStats info = new CardStats(
            MyCharacter.Meta.CARD_COLOR, //The card color. If you're making your own character, it'll look something like this. Otherwise, it'll be CardColor.RED or similar for a basegame character color.
            CardType.ATTACK, //The type. ATTACK/SKILL/POWER/CURSE/STATUS
            CardRarity.UNCOMMON, //Rarity. BASIC is for starting cards, then there's COMMON/UNCOMMON/RARE, and then SPECIAL and CURSE. SPECIAL is for cards you only get from events. Curse is for curses, except for special curses like Curse of the Bell and Necronomicurse.
            CardTarget.ENEMY, //The target. Single target is ENEMY, all enemies is ALL_ENEMY. Look at cards similar to what you want to see what to use.
            1);//The card's base cost. -1 is X cost, -2 is no cost for unplayable cards like curses, or Reflex.

    private static final int DAMAGE = 4;
    private static final int UPG_DAMAGE = 1;

    public HBRNuclearExplosion() {
        super(ID,info);

        setDamage(DAMAGE, UPG_DAMAGE);
    }

    private int calculateAttackTime() {
        AbstractCreature p = AbstractDungeon.player;
        Set<String> buffIds = new HashSet<>();

        for (AbstractPower pow : p.powers) {
            if (pow.type == AbstractPower.PowerType.BUFF) {
                buffIds.add(pow.name);   // 利用 Set 自动去重，主要针对加攻的不同回合数和炸弹
            }
        }

        return buffIds.size();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        for (int i = 0; i < calculateAttackTime(); i++) {
            addToBot(new DamageAction(m, new DamageInfo(p, damage, DamageInfo.DamageType.NORMAL), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
        }
        this.rawDescription = cardStrings.DESCRIPTION;
        initializeDescription();
    }

    @Override
    public void applyPowers() {
        super.applyPowers();
        int count = calculateAttackTime();
        this.rawDescription = cardStrings.DESCRIPTION;
        this.rawDescription += cardStrings.EXTENDED_DESCRIPTION[0] + count + cardStrings.EXTENDED_DESCRIPTION[1];
        initializeDescription();
    }

    @Override
    public void onMoveToDiscard() {
        this.rawDescription = cardStrings.DESCRIPTION;
        initializeDescription();
    }
}
