package heavenburnsred.cards.attack;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import heavenburnsred.character.MyCharacter;
import heavenburnsred.util.CardStats;

public class CutenessOverload extends HBRHitAndTypeAttackCard {
    public static final String ID = makeID(CutenessOverload.class.getSimpleName());
    public static final CardStats info = new CardStats(
            MyCharacter.Meta.CARD_COLOR, //The card color. If you're making your own character, it'll look something like this. Otherwise, it'll be CardColor.RED or similar for a basegame character color.
            CardType.ATTACK, //The type. ATTACK/SKILL/POWER/CURSE/STATUS
            CardRarity.UNCOMMON, //Rarity. BASIC is for starting cards, then there's COMMON/UNCOMMON/RARE, and then SPECIAL and CURSE. SPECIAL is for cards you only get from events. Curse is for curses, except for special curses like Curse of the Bell and Necronomicurse.
            CardTarget.ENEMY, //The target. Single target is ENEMY, all enemies is ALL_ENEMY. Look at cards similar to what you want to see what to use.
            2);//The card's base cost. -1 is X cost, -2 is no cost for unplayable cards like curses, or Reflex.

    private static final int DAMAGE = 8;
    private static final int UPG_DAMAGE = 3;
    private static final int BLOCK = 7;
    private static final int UPG_BLOCK = 2;
    private static final int HIT = 5;
    private boolean lastAttackState = false;


    public CutenessOverload() {
        super(ID,info,HBRAttackType.WP,HIT);

        setDamage(DAMAGE, UPG_DAMAGE);
        setBlock(BLOCK,UPG_BLOCK);
    }

    private boolean enemyAttack(){
        boolean AttackEnemy = false;
        for (AbstractMonster m : AbstractDungeon.getMonsters().monsters){
            if (m.intent.name().contains("ATTACK") && !m.isDead && !m.isDying) {
                AttackEnemy = true;
                break;
            }
        }
        return AttackEnemy;
    }

    private void updateCostBasedOnEnemyAction() {
        boolean currentAttackState = enemyAttack(); // 当前敌人是否有攻击意图
        if (!lastAttackState && currentAttackState) {
            int baseCost = this.isCostModified ? this.costForTurn : this.cost;
            int newCost = Math.max(0, baseCost - 1);
            this.costForTurn = newCost;
            this.isCostModified = (newCost != this.cost) || this.isCostModified;
        }
        else if (lastAttackState && !currentAttackState) {
            int baseCost = this.isCostModified ? this.costForTurn : this.cost;
            int newCost = Math.min(this.cost, baseCost + 1); // 不超过原始费用
            this.costForTurn = newCost;
            this.isCostModified = (newCost != this.cost) || this.isCostModified;
        }
        lastAttackState = currentAttackState;
    }

    @Override
    public void applyPowers() {
        super.applyPowers();
        updateCostBasedOnEnemyAction();
    }

    @Override
    public void atTurnStart() {
        super.atTurnStart();
        lastAttackState = enemyAttack();
        boolean currentAttackState = enemyAttack();
        int newCost = currentAttackState ? Math.max(0, this.cost - 1) : this.cost;

        this.costForTurn = newCost;
        this.isCostModified = (newCost != this.cost);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GainBlockAction(p,block));
        addToBot(new DamageAction(m, new DamageInfo(p, damage, DamageInfo.DamageType.NORMAL), AbstractGameAction.AttackEffect.SLASH_VERTICAL));
    }
}
