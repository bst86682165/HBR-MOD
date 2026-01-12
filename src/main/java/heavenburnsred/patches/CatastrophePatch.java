package heavenburnsred.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import heavenburnsred.powers.BasePower;
import heavenburnsred.powers.CatastropheFirePower;
import heavenburnsred.powers.DestructionPower;

@SpirePatch(clz = AbstractCreature.class, method = "brokeBlock")
public class CatastrophePatch {
    // 此处 rloc 也可以写成，rloc = 目标行 - 函数初始行，可能会更方便
    @SpirePrefixPatch
    // 注意捕获的局部变量要在 patch 方法的其他参数之后
    public static void GiveDestroyLimit(AbstractCreature _inst) {
        AbstractCreature p = AbstractDungeon.player;
        if (_inst instanceof AbstractMonster && p.hasPower(CatastropheFirePower.POWER_ID)) {
            int upperLimit = p.getPower(CatastropheFirePower.POWER_ID).amount;
            // 有破坏就提升上限
            if (_inst.hasPower(DestructionPower.POWER_ID)) {
                BasePower targetPower = (BasePower)_inst.getPower(DestructionPower.POWER_ID);
                targetPower.amount2 += upperLimit;
                if (targetPower.amount2 > 99) {
                    targetPower.amount2 = 99;
                }
            }
            // 没有破坏就新给一层（默认10层上限）
            else {
                BasePower des = new DestructionPower(_inst, 1);
                des.amount2 = upperLimit;
                AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(_inst, p, des, 1));
            }
        }
    }
}
