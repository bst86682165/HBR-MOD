package heavenburnsred.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DescriptionLine;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.LocalizedStrings;
import com.megacrit.cardcrawl.screens.CardRewardScreen;

// 用于阻止没选完奖励时选择卡牌后奖励被清除，同时更改screen为CARD_REWARD
@SpirePatch(clz = AbstractCard.class, method = "initializeDescriptionCN")
public class ChineseDescriptionPatch {
    @SpireInsertPatch(rloc = 764 - 593)
    public static SpireReturn<Void> aheadReturn(AbstractCard _inst) {
        for (int i = _inst.description.size() - 1; i >= 0; i--) {
            DescriptionLine line = _inst.description.get(i);
            if (LocalizedStrings.PERIOD.equals(line.text)) {
                if (i > 0) {
                    DescriptionLine prev = _inst.description.get(i - 1);
                    prev.text = prev.text + LocalizedStrings.PERIOD;
                    _inst.description.remove(i);
                }
            }
        }
        return SpireReturn.Return();
    }
}