package heavenburnsred.patches;


import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.stances.AbstractStance;
import heavenburnsred.stances.RiceField;

@SpirePatch(clz = AbstractStance.class, method = "getStanceFromName")
public class RiceFieldPatch {
    @SpirePrefixPatch
    // 注意捕获的局部变量要在 patch 方法的其他参数之后
    public static SpireReturn<AbstractStance> GetRiceStance(String newStance) {
        if (newStance.equals(RiceField.STANCE_ID)) {
            return SpireReturn.Return(new RiceField());
        }
        else {
            return SpireReturn.Continue();
        }
    }
}
