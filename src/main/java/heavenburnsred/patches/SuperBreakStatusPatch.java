package heavenburnsred.patches;


import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import heavenburnsred.powers.SuperBreakStatusPower;

@SpirePatch(clz = AbstractCreature.class, method = "brokeBlock")
public class SuperBreakStatusPatch {
    public static void Prefix(AbstractCreature __instance) {
        if (__instance instanceof AbstractMonster && AbstractDungeon.player.hasPower(SuperBreakStatusPower.POWER_ID)) {
            ((SuperBreakStatusPower)AbstractDungeon.player.getPower(SuperBreakStatusPower.POWER_ID)).onBreakBlock();
        }
    }
}
