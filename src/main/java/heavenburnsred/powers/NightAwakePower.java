package heavenburnsred.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;

import static heavenburnsred.BasicMod.makeID;

// 大部分逻辑写在NightAwakePatch中
public class NightAwakePower extends BasePower {
    public static final String POWER_ID = makeID(NightAwakePower.class.getSimpleName());
    private static final PowerType TYPE = PowerType.BUFF;

    public NightAwakePower(AbstractCreature owner, int amount) {
        super(POWER_ID, TYPE, false, owner, amount);
    }

    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
    }
}
