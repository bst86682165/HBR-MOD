package heavenburnsred.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import heavenburnsred.powers.AttributeCal;
import heavenburnsred.powers.DefendDown;

// 在已经有该power的时候直接return，不会发生任何事，这样的power可以调用本方法而不是ApplyPowerAction来上power
public class UnrivaledAction extends AbstractGameAction {
    private DamageInfo info;

    // 目前似乎只需要一个空的构造方法
    public UnrivaledAction(AbstractCreature target, DamageInfo info) {
        this.actionType = AbstractGameAction.ActionType.BLOCK;
        this.target = target;
        this.info = info;
    }

    @Override
    public void update() {
        if (this.target != null && this.target.hasPower(DefendDown.POWER_ID)) {
            addToTop(new DrawCardAction(AbstractDungeon.player, 1));
            addToTop(new GainEnergyAction(1));
        }
        addToTop(new DamageAction(this.target, this.info, AbstractGameAction.AttackEffect.BLUNT_HEAVY));
        this.isDone = true;
    }
}
