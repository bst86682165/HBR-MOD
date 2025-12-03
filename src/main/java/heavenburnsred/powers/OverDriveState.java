package heavenburnsred.powers;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;

import static heavenburnsred.BasicMod.makeID;

public class OverDriveState extends BasePower {
    public static final String POWER_ID = makeID(OverDriveState.class.getSimpleName());
    private static final PowerType TYPE = PowerType.BUFF;

    public OverDriveState(AbstractCreature owner, int amount) {
        super(POWER_ID, TYPE, false, owner, amount);
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, this.ID));
    }

    public void updateDescription() {
        this.description = DESCRIPTIONS[0];
    }
}
