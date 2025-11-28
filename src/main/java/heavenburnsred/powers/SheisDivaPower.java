package heavenburnsred.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;

import static heavenburnsred.BasicMod.makeID;

public class SheisDivaPower extends BasePower {
    public static final String POWER_ID = makeID(SheisDivaPower.class.getSimpleName());
    private static final PowerType TYPE = PowerType.BUFF;

    public SheisDivaPower(AbstractCreature owner, int amount) {
        super(POWER_ID, TYPE, false, owner, amount);
    }

    public void updateDescription() {
        this.description = DESCRIPTIONS[0];
    }
}
