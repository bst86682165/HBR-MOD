package heavenburnsred.patches;


import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.helpers.PowerTip;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@SpirePatch(clz = AbstractPlayer.class, method = "renderPowerTips")
public class PlayerPowerTipsPatch {
    // 此处 rloc 也可以写成，rloc = 目标行 - 函数初始行，可能会更方便
    @SpireInsertPatch(rloc = 2212 - 2198, localvars = {"tips"})
    // 注意捕获的局部变量要在 patch 方法的其他参数之后
    public static void FilterInsert(AbstractPlayer _inst, SpriteBatch sb, ArrayList<PowerTip> tips) {
        List<PowerTip> filtered = new ArrayList<PowerTip>(
            tips.stream()
                .collect(Collectors.toMap(
                    obj -> obj.header,  // header作为键筛重
                    obj -> obj,  // obj本身作为值用来取values赋值列表
                    (first, duplicate) -> duplicate  // 选后出现的计算description的层数更准确（只要后出现就一定至少是2层）
                    ))
                    .values()
        );
        // 清空原列表并写回过滤后的元素
        tips.clear();
        tips.addAll(filtered);
    }
}
