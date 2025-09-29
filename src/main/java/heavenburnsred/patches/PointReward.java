package heavenburnsred.patches;


import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoomBoss;
import heavenburnsred.cards.PointCard.LLcard;
import heavenburnsred.cards.PointCard.LQcard;
import heavenburnsred.cards.PointCard.TJcard;
import heavenburnsred.cards.PointCard.ZYcard;

import java.util.ArrayList;

public class PointReward extends RewardItem {

    public PointReward(){

        this.hb = new Hitbox(460.0F * Settings.xScale, 90.0F * Settings.yScale);
        this.flashTimer = 0.0F;
        this.isDone = false;
        this.ignoreReward = false;
        this.redText = false;
        this.type = RewardItem.RewardType.CARD;
        ArrayList<AbstractCard> PointCards = new ArrayList<>();
        PointCards.add(new LLcard());
        PointCards.add(new LQcard());
        PointCards.add(new TJcard());
        PointCards.add(new ZYcard());
        this.cards = PointCards;
        this.text = "Point";

    }

}