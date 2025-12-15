package heavenburnsred.powers;

import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import heavenburnsred.actions.UpdateAttributeDescriptionAction;

import static heavenburnsred.BasicMod.makeID;

public class CriticalHitSingleActivation extends BasePower {
    public static final String POWER_ID = makeID(CriticalHitSingleActivation.class.getSimpleName());
    private static final PowerType TYPE = PowerType.BUFF;

    public CriticalHitSingleActivation(AbstractCreature owner, int amount) {
        super(POWER_ID, TYPE, true, owner, amount);
    }

    public void onInitialApplication() {
        addToBot(new UpdateAttributeDescriptionAction());
    }


    public void atEndOfRound() {
        if (this.amount == 0) {
            addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, CriticalHitSingleActivation.POWER_ID));
        } else {
            addToBot(new ReducePowerAction(this.owner, this.owner, CriticalHitSingleActivation.POWER_ID, 1));
        }
    }

    public void onRemove() {
        addToBot(new UpdateAttributeDescriptionAction());
    }

    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
    }
}
