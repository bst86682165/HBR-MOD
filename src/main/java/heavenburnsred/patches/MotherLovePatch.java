package heavenburnsred.patches;


import com.evacipated.cardcrawl.mod.stslib.actions.tempHp.AddTemporaryHPAction;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import heavenburnsred.cards.skill.MotherLove;

import java.util.UUID;

@SpirePatch(clz = AbstractPlayer.class, method = "damage")
public class MotherLovePatch {
    private static final int GET_HP = 1;
    private static final float GET_TMP_HP = 0.3f;

    public static CardLocation findCardInBattle(UUID uuid) {
        AbstractPlayer p = AbstractDungeon.player;

        for (AbstractCard c : p.hand.group)
            if (c.uuid.equals(uuid))
                return new CardLocation(c, p.hand);

        for (AbstractCard c : p.drawPile.group)
            if (c.uuid.equals(uuid))
                return new CardLocation(c, p.drawPile);

        for (AbstractCard c : p.discardPile.group)
            if (c.uuid.equals(uuid))
                return new CardLocation(c, p.discardPile);

        // 不在战斗的3个牌堆中
        return null;
    }



    // 阻止死亡
    @SpireInsertPatch(rloc = 1853 - 1725)
    public static SpireReturn<Void> aheadReturn(AbstractPlayer __instance, DamageInfo info) {
        // 牌组中有理莎慈爱牌且没有绽放印记时复活，生效先于精灵药和尾巴，并把该卡移出牌组
        for(AbstractCard c : __instance.masterDeck.group) {
            if (c.cardID.equals(MotherLove.ID)) {
                // 回至1hp
                __instance.currentHealth = 0;
                __instance.heal(GET_HP, true);
                // 获得30%临时生命
                int TMP_HP = (int)(__instance.maxHealth * GET_TMP_HP);
                AbstractDungeon.actionManager.addToTop(new AddTemporaryHPAction(__instance,__instance,TMP_HP));
                // 牌库中清除该牌
                __instance.masterDeck.removeCard(c);
                // 战斗中消耗该牌
                CardLocation loc = findCardInBattle(c.uuid);
                if (loc != null) {
                    AbstractDungeon.actionManager.addToBottom(
                        new ExhaustSpecificCardAction(loc.card, loc.group)
                    );
                }
                return SpireReturn.Return();
            }
        }
        // 没有理莎牌时无事发生
        return SpireReturn.Continue();
    }

    public static class CardLocation {
        public AbstractCard card;
        public CardGroup group;

        public CardLocation(AbstractCard card, CardGroup group) {
            this.card = card;
            this.group = group;
        }
    }
}
