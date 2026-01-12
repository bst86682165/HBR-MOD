package heavenburnsred.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;

import static heavenburnsred.BasicMod.makeID;

// 破盾逻辑放在patch里写
public class CatastropheFirePower extends BasePower {
    public static final String POWER_ID = makeID(CatastropheFirePower.class.getSimpleName());
    private static final PowerType TYPE = PowerType.BUFF;

    public CatastropheFirePower(AbstractCreature owner, int amount) {
        super(POWER_ID, TYPE, false, owner, amount);
    }

    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
    }
}
