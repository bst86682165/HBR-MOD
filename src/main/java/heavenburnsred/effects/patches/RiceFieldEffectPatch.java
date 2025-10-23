package heavenburnsred.effects.patches;


import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.vfx.stance.StanceAuraEffect;
import heavenburnsred.stances.RiceField;


// 仿照人工的判定方式，只消除攻击附带的debuff
@SpirePatch(clz = StanceAuraEffect.class, method = SpirePatch.CONSTRUCTOR)
public class RiceFieldEffectPatch {
    @SpirePostfixPatch
    public static void RiceAura(StanceAuraEffect _inst, String stanceId) {
        // 调整颜色即可
        if (stanceId.equals(RiceField.STANCE_ID)) {
            // 继承的abstractGameAction的color，所以必须用setPrivateInherited
            ReflectionHacks.setPrivateInherited(_inst, StanceAuraEffect.class, "color",
                new Color(
                MathUtils.random(0.90F, 1.0F),   // 红色分量：亮
                MathUtils.random(0.80F, 0.9F),   // 绿色分量：略低
                MathUtils.random(0.25F, 0.35F),  // 蓝色分量：较低
                0.0F)                            // 初始透明度
            );
        }
    }
}
