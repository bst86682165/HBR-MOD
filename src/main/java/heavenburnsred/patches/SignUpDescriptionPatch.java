package heavenburnsred.patches;


import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import heavenburnsred.cards.power.SignUpDaily;


@SpirePatch(clz = CardLibrary.class, method = "getCopy", paramtypez = {String.class, int.class, int.class})
public class SignUpDescriptionPatch {
    // 此处 rloc 也可以写成，rloc = 目标行 - 函数初始行，可能会更方便
    @SpireInsertPatch(rloc = 1009 - 994, localvars = {"retVal"})
    // 注意捕获的局部变量要在 patch 方法的其他参数之后
    public static void SignUpDesChange(String key, int upgradeTime, int misc, AbstractCard retVal) {
        if (retVal.cardID.equals(SignUpDaily.ID)) {
            retVal.magicNumber = misc;
            retVal.baseMagicNumber = misc;
            retVal.initializeDescription();
        }
    }
}
