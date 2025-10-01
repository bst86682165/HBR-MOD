package heavenburnsred.patches;

import basemod.ReflectionHacks;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import com.megacrit.cardcrawl.vfx.combat.PowerBuffEffect;
import com.megacrit.cardcrawl.vfx.combat.PowerDebuffEffect;
import heavenburnsred.powers.HBRTurnStackPower;

import java.util.Collections;

// 用来改变施加buff/debuff的叠加逻辑，实现power的mount仍作为持续回合数，stack_layer为叠加层数
// 目标身上已有相同ID且相同持续回合数的power时，叠加到stack_layer，否则新建一个power
// 只用来上HBR里回合生效，且能叠加层数的power，例如加攻和降防
public class ApplyHBRStackPowerAction extends ApplyPowerAction {

    // 全参数构建方法，大概率用不上先写着
    public ApplyHBRStackPowerAction(AbstractCreature target, AbstractCreature source, AbstractPower powerToApply, int stackAmount, boolean isFast, AbstractGameAction.AttackEffect effect) {
        super(target, source, powerToApply, stackAmount, isFast, effect);
    }

    // 常用的构建方法，调用时stackAmount为0
    public ApplyHBRStackPowerAction(AbstractCreature target, AbstractCreature source, AbstractPower powerToApply) {
        super(target, source, powerToApply, 0);
    }

    @Override
    public void update() {
        if (this.target == null || this.target.isDeadOrEscaped()) {
            this.isDone = true;
            return;
        }
        float startingDuration = (float) ReflectionHacks.getPrivate(this, ApplyPowerAction.class, "startingDuration");
        AbstractPower powerToApply = (AbstractPower) ReflectionHacks.getPrivate(this, ApplyPowerAction.class, "powerToApply");
        if (this.duration == startingDuration) {
            if (this.source != null)
                for (AbstractPower pow : this.source.powers)
                    pow.onApplyPower(powerToApply, this.target, this.source);
            if (this.target instanceof com.megacrit.cardcrawl.monsters.AbstractMonster &&
                    this.target.isDeadOrEscaped()) {
                this.duration = 0.0F;
                this.isDone = true;
                return;
            }
            // 上动画效果的不知道有没有用，先留着吧
            AbstractDungeon.effectList.add(new FlashAtkImgEffect(this.target.hb.cX, this.target.hb.cY, this.attackEffect));
            // 判断是否已经有相同的buff上过了
            boolean hasBuffAlready = false;
            for (AbstractPower p : this.target.powers) {
                if (p.ID.equals(powerToApply.ID) && p.amount == powerToApply.amount) {
                    // 重复直接叠加stack_layers即可
                    int powerToApply_layers = ((HBRTurnStackPower)powerToApply).getStack_layers();
                    p.stackPower(powerToApply_layers);
                    p.flash();
                    if (p.type == AbstractPower.PowerType.BUFF) {
                        AbstractDungeon.effectList.add(new PowerBuffEffect(this.target.hb.cX - this.target.animX, this.target.hb.cY + this.target.hb.height / 2.0F, "+" + powerToApply_layers + " " + powerToApply.name));
                    } else {
                        AbstractDungeon.effectList.add(new PowerDebuffEffect(this.target.hb.cX - this.target.animX, this.target.hb.cY + this.target.hb.height / 2.0F, "+" + powerToApply_layers + " " + powerToApply.name));
                    }
                    hasBuffAlready = true;
                    AbstractDungeon.onModifyPower();
                }
            }
            if (powerToApply.type == AbstractPower.PowerType.DEBUFF)
                this.target.useFastShakeAnimation(0.5F);
            // 新上的buff/debuff
            if (!hasBuffAlready) {
                this.target.powers.add(powerToApply);
                Collections.sort(this.target.powers);
                powerToApply.onInitialApplication();
                powerToApply.flash();
                if (powerToApply.type == AbstractPower.PowerType.BUFF) {
                    AbstractDungeon.effectList.add(new PowerBuffEffect(this.target.hb.cX - this.target.animX, this.target.hb.cY + this.target.hb.height / 2.0F, powerToApply.name));
                } else {
                    AbstractDungeon.effectList.add(new PowerDebuffEffect(this.target.hb.cX - this.target.animX, this.target.hb.cY + this.target.hb.height / 2.0F, powerToApply.name));
                }
                AbstractDungeon.onModifyPower();
                if (this.target.isPlayer) {
                    int buffCount = 0;
                    for (AbstractPower p : this.target.powers) {
                        if (p.type == AbstractPower.PowerType.BUFF)
                            buffCount++;
                    }
                    if (buffCount >= 10)
                        UnlockTracker.unlockAchievement("POWERFUL");
                }
            }
            // 增加新的/叠加旧的buff时把所有同ID的描述均更新一遍
            for (AbstractPower p : this.target.powers) {
                if (p.ID.equals(powerToApply.ID)) {
                    p.updateDescription();
                }
            }
        }
        tickDuration();
    }
}
