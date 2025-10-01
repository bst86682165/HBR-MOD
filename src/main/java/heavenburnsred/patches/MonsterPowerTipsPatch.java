package heavenburnsred.patches;


import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@SpirePatch(clz = AbstractMonster.class, method = "renderTip")
public class MonsterPowerTipsPatch {
    // 此处 rloc 也可以写成，rloc = 目标行 - 函数初始行，可能会更方便
    @SpireInsertPatch(rloc = 320 - 306, localvars = {"tips"})
    // 注意捕获的局部变量要在 patch 方法的其他参数之后
    public static void FilterInsert(AbstractMonster _inst, SpriteBatch sb, ArrayList<PowerTip> tips) {
        List<PowerTip> filtered = new ArrayList<PowerTip>(
            tips.stream()
                .collect(Collectors.toMap(
                    obj -> obj.header,  // header作为键筛重
                    obj -> obj,  // obj本身作为值用来取values赋值列表
                    (first, duplicate) -> first  // 每一个power理论上显示一样的description所以先后都行
                    ))
                    .values()
        );
        // 清空原列表并写回过滤后的元素
        tips.clear();
        tips.addAll(filtered);
    }
}
