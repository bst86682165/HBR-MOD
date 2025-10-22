package heavenburnsred.patches;


import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import heavenburnsred.powers.AngelWingsPower;

import java.util.EnumSet;


// 仿照人工的判定方式，只消除攻击附带的debuff
@SpirePatch(clz = ApplyPowerAction.class, method = "update")
public class AngelWingsPatch {
    // 此处 rloc 也可以写成，rloc = 目标行 - 函数初始行，可能会更方便
    @SpireInsertPatch(rloc = 196 - 141)
    // 注意捕获的局部变量要在 patch 方法的其他参数之后
    public static SpireReturn<Void> ignoreDebuff(ApplyPowerAction _inst) {
        // 判断是怪物打到有天使之翼buff的玩家身上
        if (_inst.target.hasPower(AngelWingsPower.POWER_ID) && _inst.source instanceof AbstractMonster) {
            AbstractPower powerToApply = ReflectionHacks.getPrivate(_inst, ApplyPowerAction.class, "powerToApply");
            // 接着判断上的是debuff
            if (powerToApply.type == AbstractPower.PowerType.DEBUFF) {
                EnumSet<AbstractMonster.Intent> dangerousTypes = EnumSet.of(AbstractMonster.Intent.ATTACK, AbstractMonster.Intent.ATTACK_BUFF, AbstractMonster.Intent.ATTACK_DEBUFF, AbstractMonster.Intent.ATTACK_DEFEND);
                // 最后判断是攻击意图附带的debuff
                if (dangerousTypes.contains(((AbstractMonster)_inst.source).intent)) {
                    ((AngelWingsPower)AbstractDungeon.player.getPower(AngelWingsPower.POWER_ID)).mayReduceLayers(_inst.source);
                    _inst.isDone = true;
                    return SpireReturn.Return();
                }
            }
        }
        return SpireReturn.Continue();
    }
}
