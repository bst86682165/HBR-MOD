package heavenburnsred.actions;

import basemod.ReflectionHacks;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;

// 在已经有该power的时候直接return，不会发生任何事，这样的power可以调用本方法而不是ApplyPowerAction来上power
public class ApplyNotStackingPowerAction extends ApplyPowerAction {

    // 常用的构建方法，调用时stackAmount为0
    public ApplyNotStackingPowerAction(AbstractCreature target, AbstractCreature source, AbstractPower powerToApply) {
        super(target, source, powerToApply);
    }

    @Override
    public void update() {
        AbstractPower powerToApply = ReflectionHacks.getPrivate(this, ApplyPowerAction.class, "powerToApply");
        if (this.target.hasPower(powerToApply.ID)) {
            this.isDone = true;
            return;
        } else {
            super.update();
        }
    }
}
