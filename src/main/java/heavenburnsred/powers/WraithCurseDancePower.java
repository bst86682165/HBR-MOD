package heavenburnsred.powers;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;

import static heavenburnsred.BasicMod.makeID;

public class WraithCurseDancePower extends BasePower {
    public static final String POWER_ID = makeID(WraithCurseDancePower.class.getSimpleName());
    private static final PowerType TYPE = PowerType.BUFF;

    public WraithCurseDancePower(AbstractCreature owner, int amount) {
        super(POWER_ID, TYPE, false, owner, amount);
    }

    @Override
    public void onApplyPower(AbstractPower power, AbstractCreature target, AbstractCreature source) {
        if ((power.type == PowerType.DEBUFF && !power.ID.equals("Shackled") && source == this.owner && target != this.owner && !target.hasPower("Artifact")) ||
            (power.type == PowerType.BUFF && source == this.owner && target == this.owner)) {
            this.flash();
            addToBot(new GainBlockAction(this.owner, this.owner, this.amount));
        }
    }

    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
    }
}
