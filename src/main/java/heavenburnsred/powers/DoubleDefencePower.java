package heavenburnsred.powers;

import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;

import static heavenburnsred.BasicMod.makeID;

public class DoubleDefencePower extends BasePower {
    public static final String POWER_ID = makeID(DoubleDefencePower.class.getSimpleName());
    private static final PowerType TYPE = PowerType.BUFF;
    private boolean justApplied = false;

    public DoubleDefencePower(AbstractCreature owner, int amount, boolean isSourceMonster) {
        super(POWER_ID, TYPE, true, owner, amount);
        this.justApplied = isSourceMonster;
        this.priority = 6;
    }

    public void atEndOfRound() {
        if (this.justApplied) {
            this.justApplied = false;
            return;
        }
        addToBot(new ReducePowerAction(this.owner, this.owner, this.ID, 1));
    }

    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
    }

    public float modifyBlock(float blockAmount) {
        return blockAmount * 2;
    }
}
