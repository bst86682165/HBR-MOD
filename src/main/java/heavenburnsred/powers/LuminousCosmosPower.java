package heavenburnsred.powers;

import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;

import static heavenburnsred.BasicMod.makeID;

public class LuminousCosmosPower extends BasePower {
    public static final String POWER_ID = makeID(LuminousCosmosPower.class.getSimpleName());
    private static final PowerType TYPE = PowerType.BUFF;

    public LuminousCosmosPower(AbstractCreature owner, int amount) {
        super(POWER_ID, TYPE, true, owner, amount);
    }

    public void atEndOfRound() {
        if (this.amount == 0) {
            addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, LuminousCosmosPower.POWER_ID));
        } else {
            addToBot(new ReducePowerAction(this.owner, this.owner, LuminousCosmosPower.POWER_ID, 1));
        }
    }

    public float atDamageReceive(float damage, DamageInfo.DamageType type) {
        if (type == DamageInfo.DamageType.NORMAL)
            return damage * 0.75F;
        return damage;
    }

    public void updateDescription() {
        this.description = DESCRIPTIONS[0];
    }
}
