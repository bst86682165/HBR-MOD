package heavenburnsred.patches;


import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.utility.HandCheckAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import heavenburnsred.cards.skill.NightAwake;
import heavenburnsred.powers.NightAwakePower;
import heavenburnsred.powers.SuperBreakStatusPower;

@SpirePatch(clz = UseCardAction.class, method = "update")
public class NightAwakePatch {
    // 能力牌回手=复制一份到手中
    @SpireInsertPatch(rloc = 120 - 84)
    public static void returnForPower(UseCardAction __instance) {
        AbstractPlayer p = AbstractDungeon.player;
        AbstractCard targetCard = ReflectionHacks.getPrivate(__instance, UseCardAction.class, "targetCard");
        // 有夜醒并且打出非夜醒牌时，复制一份能力牌
        if (p.hasPower(NightAwakePower.POWER_ID) && !(targetCard instanceof NightAwake) && targetCard.type == AbstractCard.CardType.POWER) {
            AbstractDungeon.actionManager.addToTop(new MakeTempCardInHandAction(targetCard.makeStatEquivalentCopy()));
            // 减少一层夜醒
            AbstractDungeon.actionManager.addToBottom(new ReducePowerAction(p,p, NightAwakePower.POWER_ID, 1));
        }
        // 没有夜醒时无事发生
    }

    // 非能力牌回手
    @SpireInsertPatch(rloc = 127 - 84)
    public static SpireReturn<Void> returnForNonPower(UseCardAction __instance) {
        AbstractPlayer p = AbstractDungeon.player;
        AbstractCard targetCard = ReflectionHacks.getPrivate(__instance, UseCardAction.class, "targetCard");
        // 有夜醒并且打出非夜醒牌时，回手并提前返回，不处理消耗等逻辑
        if (p.hasPower(NightAwakePower.POWER_ID) && !(targetCard instanceof NightAwake) && targetCard.type != AbstractCard.CardType.POWER) {
            p.hand.moveToHand(targetCard);
            // 源代码中回手相应逻辑处理（好像主要是小刀精准）
            p.onCardDrawOrDiscard();
            // 减少一层夜醒
            AbstractDungeon.actionManager.addToBottom(new ReducePowerAction(p,p, NightAwakePower.POWER_ID, 1));
            // 处理其他打出卡牌的相关逻辑（保留源代码）
            targetCard.exhaustOnUseOnce = false;
            targetCard.dontTriggerOnUseCard = false;
            AbstractDungeon.actionManager.addToBottom(new HandCheckAction());
            // 为该UseCardAction来tickDuration
            ReflectionHacks.RMethod tickDuration = ReflectionHacks.privateMethod(AbstractGameAction.class,"tickDuration");
            tickDuration.invoke(__instance);
            return SpireReturn.Return();
        }
        // 没有夜醒时无事发生
        else {
            return SpireReturn.Continue();
        }
    }
}
