package heavenburnsred.powers;

import basemod.ReflectionHacks;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import static heavenburnsred.BasicMod.makeID;

public class AngelWingsPower extends BasePower {
    public static final String POWER_ID = makeID(AngelWingsPower.class.getSimpleName());
    private static final PowerType TYPE = PowerType.BUFF;

    public AngelWingsPower(AbstractCreature owner, int amount) {
        super(POWER_ID, TYPE, false, owner, amount);
    }

    // 检查一个action是否是某目标怪物发出的
    private boolean checkActions(AbstractGameAction a, AbstractCreature m) {
        // 普通的攻击action
        if (a instanceof DamageAction) {
            DamageAction dam = (DamageAction)a;
            DamageInfo info = ReflectionHacks.getPrivate(dam, DamageAction.class,"info");
            if (a.source == m && info.type == DamageInfo.DamageType.NORMAL) return true;
        }
        // 给玩家施加的debuff
        if (a instanceof ApplyPowerAction) {
            ApplyPowerAction app = (ApplyPowerAction)a;
            AbstractPower powerToApply = ReflectionHacks.getPrivate(app, ApplyPowerAction.class, "powerToApply");
            if (app.source == m && app.target == AbstractDungeon.player && powerToApply.type == PowerType.DEBUFF) return true;
        }
        // 都没有则返回false
        return false;
    }

    // 若某怪物已经完成全部action（用于应对连击和上debuff的意图）则减少一层
    public void mayReduceLayers(AbstractCreature m) {
        boolean stillMoreActions = AbstractDungeon.actionManager.actions.stream()
                .anyMatch(a -> (checkActions(a, m)));
        if (!stillMoreActions) {
            addToTop((AbstractGameAction)new ReducePowerAction(this.owner, this.owner, this.ID, 1));
        }
    }

    // 强制怪物打我们0伤害，包括盾。连击算1次攻击，生效顺序先于缓冲
    public void onAttackedBeforeDefence(DamageInfo info) {
        if (info.type == DamageInfo.DamageType.NORMAL) {
            if (info.output > 0) {
                mayReduceLayers(info.owner);
            }
            info.output = 0;
        }
    }

    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
    }
}
