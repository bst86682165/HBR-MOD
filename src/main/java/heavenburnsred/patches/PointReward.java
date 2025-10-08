package heavenburnsred.patches;


import basemod.ReflectionHacks;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.screens.CardRewardScreen;
import com.megacrit.cardcrawl.ui.buttons.PeekButton;
import heavenburnsred.cards.PointCard.LLcard;
import heavenburnsred.cards.PointCard.LQcard;
import heavenburnsred.cards.PointCard.TJcard;
import heavenburnsred.cards.PointCard.ZYcard;

import java.util.ArrayList;

public class PointReward extends RewardItem {

    public PointReward(){
        // 卡牌奖励实际上是4选1的options，并非实际可以添加的卡牌3选1
        // 这里模仿生成卡牌奖励的空参构造
        this.type = RewardItem.RewardType.CARD;
        ArrayList<AbstractCard> PointCards = new ArrayList<>();
        PointCards.add(new LLcard());
        PointCards.add(new LQcard());
        PointCards.add(new TJcard());
        PointCards.add(new ZYcard());
        this.cards = PointCards;
        this.text = "白值奖励";
    }

    @Override
    // 选择时展开属性4选1面板
    public boolean claimReward() {
        // 可以跳过，没有颂钵，没有peek按钮，选择后不进入牌组而是直接生效，选择后奖励消失
        AbstractDungeon.cardRewardScreen.customCombatOpen(this.cards, "请选择要升级的属性", true);
        PeekButton peekButton = ReflectionHacks.getPrivate(AbstractDungeon.cardRewardScreen, CardRewardScreen.class, "peekButton");
        peekButton.hideInstantly();
        ReflectionHacks.setPrivate(AbstractDungeon.cardRewardScreen, CardRewardScreen.class, "rItem", this);
        ReflectionHacks.setPrivate(AbstractDungeon.cardRewardScreen, CardRewardScreen.class, "chooseOne", true);
        ReflectionHacks.setPrivate(AbstractDungeon.cardRewardScreen, CardRewardScreen.class, "discovery", false);
        // 跳过返回时回到奖励页面
        AbstractDungeon.previousScreen = AbstractDungeon.CurrentScreen.COMBAT_REWARD;
        // 跳过后该奖励不消失
        return false;
    }
}