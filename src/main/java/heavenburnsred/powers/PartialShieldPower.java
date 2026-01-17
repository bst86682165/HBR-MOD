package heavenburnsred.powers;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;

import static heavenburnsred.BasicMod.makeID;

public class PartialShieldPower extends BasePower {
    public static final String POWER_ID = makeID(PartialShieldPower.class.getSimpleName());
    private static final PowerType TYPE = PowerType.BUFF;

    public PartialShieldPower(AbstractCreature owner, int amount) {
        super(POWER_ID, TYPE, true, owner, amount);
    }

    public void updateDescription() {
        if (this.amount == 1) {
            this.description = DESCRIPTIONS[0];
        } else {
            this.description = DESCRIPTIONS[1] + this.amount + DESCRIPTIONS[2];
        }
    }

    public void atStartOfTurn() {
        flash();
        addToBot(new ApplyPowerAction(this.owner, this.owner, new DoubleDefencePower(this.owner, 1, false), this.amount));
        addToBot(new ReducePowerAction(this.owner, this.owner, this.ID, 1));
    }
}
