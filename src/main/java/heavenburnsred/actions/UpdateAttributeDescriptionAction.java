package heavenburnsred.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import heavenburnsred.powers.AttributeCal;

// 在已经有该power的时候直接return，不会发生任何事，这样的power可以调用本方法而不是ApplyPowerAction来上power
public class UpdateAttributeDescriptionAction extends AbstractGameAction {

    // 目前似乎只需要一个空的构造方法
    public UpdateAttributeDescriptionAction() {
    }

    @Override
    public void update() {
        AbstractDungeon.player.getPower(AttributeCal.POWER_ID).updateDescription();
        this.isDone = true;
    }
}
