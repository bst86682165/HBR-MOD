package heavenburnsred.patches;


import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rewards.RewardItem;
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
    // 选择时展开没有跳过选项的4选1面板，参照许愿
    public boolean claimReward() {
        // 这里没有跳过按钮，还是应该修改一下
        AbstractDungeon.cardRewardScreen.chooseOneOpen(this.cards);
        // 返回时回到奖励页面
        AbstractDungeon.previousScreen = AbstractDungeon.CurrentScreen.COMBAT_REWARD;
        // 选择后该奖励消失
        return true;
    }
}