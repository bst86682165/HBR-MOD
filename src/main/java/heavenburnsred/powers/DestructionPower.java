package heavenburnsred.powers;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import static heavenburnsred.BasicMod.makeID;

// 破盾逻辑放在patch里写
public class DestructionPower extends BasePower {
    public static final String POWER_ID = makeID(DestructionPower.class.getSimpleName());
    private static final PowerType TYPE = PowerType.DEBUFF;
    private static final int DamagePerLayer = 10;
    private static final int InitiateLayer = 10;

    public DestructionPower(AbstractCreature owner, int amount) {
        super(POWER_ID, TYPE, false, owner, amount);
        this.amount2 = InitiateLayer;
    }

    @Override
    public float atDamageReceive(float damage, DamageInfo.DamageType damageType) {
        if (damageType == DamageInfo.DamageType.NORMAL && this.owner.currentBlock <= 0) {
            return damage * (1 + this.amount * (DamagePerLayer / 100f));
        }
        return damage;
    }

    // 叠加不超过amount2的上限
    @Override
    public void stackPower(int stackAmount) {
        super.stackPower(stackAmount);
        if (this.amount > this.amount2) {
            this.amount = this.amount2;
        }
        updateDescription();
    }

    // 与以手拒之一样，受到伤害+1层数
    @Override
    public int onAttacked(DamageInfo info, int damageAmount) {
        if (info.type != DamageInfo.DamageType.THORNS && info.type != DamageInfo.DamageType.HP_LOSS && info.owner != null && info.owner != this.owner) {
            flash();
            AbstractCreature p = AbstractDungeon.player;
            addToTop(new ApplyPowerAction(this.owner, p, new DestructionPower(this.owner, 1), 1));
        }
        return damageAmount;
    }

    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.amount * DamagePerLayer + DESCRIPTIONS[1];
    }
}
