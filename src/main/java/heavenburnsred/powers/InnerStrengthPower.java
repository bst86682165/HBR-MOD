package heavenburnsred.powers;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import static heavenburnsred.BasicMod.makeID;

public class InnerStrengthPower extends BasePower {
    public static final String POWER_ID = makeID(InnerStrengthPower.class.getSimpleName());
    private static final PowerType TYPE = PowerType.BUFF;

    public InnerStrengthPower(AbstractCreature owner, int amount) {
        super(POWER_ID, TYPE, false, owner, amount);
    }

    @Override
    public void atStartOfTurn() {
        AbstractCreature p = AbstractDungeon.player;
        flash();
        addToBot(new ApplyPowerAction(p, p, new TokenPower(p, amount), amount));
    }

    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
    }
}
