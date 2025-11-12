package heavenburnsred.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import heavenburnsred.cards.attack.HBRHitAndTypeAttackCard;


// 加入检测破盾的机制，允许
public class BlockRelatedDamageAction extends DamageAction {
    private final HBRHitAndTypeAttackCard HBRCard;

    public BlockRelatedDamageAction(AbstractCreature target, DamageInfo info, AbstractGameAction.AttackEffect effect, HBRHitAndTypeAttackCard card) {
        super(target, info, effect);
        HBRCard = card;
    }

    public BlockRelatedDamageAction(AbstractCreature target, DamageInfo info, HBRHitAndTypeAttackCard card) {
        this(target,info,AttackEffect.NONE,card);
    }

    @Override
    public void update() {
        int previousHealth = target.currentHealth;
        int previousBlock = target.currentBlock;
        // 先结算damage
        super.update();
        // 在damage结算后，判断打HP等条件并调用HBR攻击卡适当的方法
        if (this.isDone) {
            AbstractPlayer p = AbstractDungeon.player;
            // 穿盾打到HP也算未被格挡的伤害，因此会和onBreakBlock有重叠
            if (target.currentHealth < previousHealth)
                this.HBRCard.onAttackHP(p, target);
            // 恰好打穿格挡也算击破
            if (previousBlock > 0 && target.currentBlock == 0)
                this.HBRCard.onBreakBlock(p, target);
            // 完全被怪格挡住
            if (previousBlock > 0 && target.currentBlock > 0)
                this.HBRCard.onBeingBlocked(p, target);
        }
    }
}
