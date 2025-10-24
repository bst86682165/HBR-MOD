package heavenburnsred.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import heavenburnsred.cards.attack.HBRHitAndTypeAttackCard;


// 加入检测破盾的机制，允许
public class BreakBlockHBRDamageAction extends DamageAction {
    private final AbstractCreature m;
    private final HBRHitAndTypeAttackCard HBRCard;
    private int previousBlock;

    public BreakBlockHBRDamageAction(AbstractCreature target, DamageInfo info, AbstractGameAction.AttackEffect effect, HBRHitAndTypeAttackCard card) {
        super(target, info, effect);
        m = target;
        HBRCard = card;
    }

    public BreakBlockHBRDamageAction(AbstractCreature target, DamageInfo info, HBRHitAndTypeAttackCard card) {
        super(target, info);
        m = target;
        HBRCard = card;
    }

    @Override
    public void update() {
        // 最开始没结算damage时先保存当前的格挡值
        if (this.duration == 0.1f) {
            previousBlock = m.currentBlock;
        }
        super.update();
        // 在damage结算后，判断是否破盾并调用card的onBreakBlock
        if (this.isDone && previousBlock > 0 && m.currentBlock == 0) {
            this.HBRCard.onBreakBlock();
        }
    }
}
