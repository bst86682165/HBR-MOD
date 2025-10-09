package heavenburnsred.patches;


import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.screens.CardRewardScreen;


public class CardRewardScreenPatch {

    // 用于阻止没选完奖励时选择卡牌后奖励被清除，同时更改screen为CARD_REWARD
    @SpirePatch(clz = CardRewardScreen.class, method = "takeReward")
    public static class notTakeReward {
        @SpirePrefixPatch
        public static SpireReturn<Void> aheadReturn(CardRewardScreen _inst) {
            if (_inst.rItem instanceof PointReward && ((PointReward)_inst.rItem).getRewardNumber() > 1) {
                AbstractDungeon.previousScreen = AbstractDungeon.CurrentScreen.CARD_REWARD;
                return SpireReturn.Return();
            }
            else {
                return SpireReturn.Continue();
            }
        }
    }

    // 用于刷新计数、卡牌和页面渲染
    @SpirePatch(clz = CardRewardScreen.class, method = "cardSelectUpdate")
    public static class refreshReward {
        // 293行是先close再渲染＋重新打开
        @SpireInsertPatch(rloc = 293 - 244)
        public static void intoNextPointReward(CardRewardScreen _inst) {
            if (_inst.rItem instanceof PointReward && ((PointReward)_inst.rItem).getRewardNumber() > 1) {
                PointReward targetPointReward = (PointReward)_inst.rItem;
                targetPointReward.setRewardNumber(targetPointReward.getRewardNumber()-1);
                targetPointReward.updateCards();
                targetPointReward.claimReward();
            }
        }
    }
}
