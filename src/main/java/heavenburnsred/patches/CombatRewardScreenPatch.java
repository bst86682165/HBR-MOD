package heavenburnsred.patches;


import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoomBoss;
import com.megacrit.cardcrawl.rooms.MonsterRoomElite;
import com.megacrit.cardcrawl.screens.CombatRewardScreen;
import heavenburnsred.relics.Attribute;

@SpirePatch(clz = CombatRewardScreen.class, method = "setupItemReward")
public class CombatRewardScreenPatch {
    // 此处 rloc 也可以写成，rloc = 目标行 - 函数初始行，可能会更方便
    @SpireInsertPatch(rloc = 93 - 72)
    // 注意捕获的局部变量要在 patch 方法的其他参数之后
    public static void PointRewardInsert(CombatRewardScreen _inst) {
        // 模仿遗物转经轮的写法
        if (AbstractDungeon.getCurrRoom() instanceof MonsterRoom
            && AbstractDungeon.player.hasRelic(Attribute.ID)) {
            // 将奖励添加至第一位
            _inst.rewards.add(0, new PointReward());
            _inst.rewards.add(0, new PointReward());
            if (AbstractDungeon.getCurrRoom() instanceof MonsterRoomElite){
                _inst.rewards.add(0,new PointReward());
            }
            if (AbstractDungeon.getCurrRoom() instanceof MonsterRoomBoss){
                _inst.rewards.add(0, new PointReward());
                _inst.rewards.add(0, new PointReward());
                _inst.rewards.add(0, new PointReward());
            }
        }
    }
}
