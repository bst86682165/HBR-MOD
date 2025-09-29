package heavenburnsred.relics;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import com.megacrit.cardcrawl.rewards.RewardItem;
import heavenburnsred.patches.PointReward;
import heavenburnsred.powers.AttributeCal;
import heavenburnsred.powers.MonsterPoint;


import java.util.ArrayList;

import static heavenburnsred.BasicMod.makeID;

public class Attribute extends BaseRelic
{
    private static final String NAME = "Attribute"; // The name will be used for determining the image file as well as the
    // ID.
    public static final String ID = makeID(NAME); // This adds the mod's prefix to the relic ID, resulting in
    // modID:MyRelic
    private static final AbstractRelic.RelicTier RARITY = AbstractRelic.RelicTier.COMMON; // The relic's rarity.
    private static final AbstractRelic.LandingSound SOUND = AbstractRelic.LandingSound.CLINK; // The sound played when
    // the relic is clicked.

    //定义四维
    public static int hbrLL = 10;
    public static int hbrLQ = 10;
    public static int hbrTJ = 10;
    public static int hbrZY = 10;
    public static int ATTpoint = 0;


    //构建四维奖励卡
    public Attribute() {
        super(ID, NAME, RARITY, SOUND);
    }

    //在此书写智运提高后的增益
    public void atBattleStartPreDraw(){

        if (hbrZY > 15){
            addToBot(new ApplyPowerAction(AbstractDungeon.player,AbstractDungeon.player,new ArtifactPower(AbstractDungeon.player,1)));
        }
    }

    //以buff的形式赋予自身防御值与怪兽白值,改为每回合开始以应对复活的情况
    public void atTurnStart() {
        if (!AbstractDungeon.player.hasPower("heavenburnsred:AttributeCal")){
            addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new AttributeCal(AbstractDungeon.player, 1)));
        }
        if (!AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
            for (AbstractMonster m : (AbstractDungeon.getMonsters()).monsters) {
                if (!m.isDead && !m.isDying && !m.hasPower("heavenburnsred:MonsterPoint")){
                    int Floor = AbstractDungeon.floorNum;
                    int Act = AbstractDungeon.actNum;
                    int MonPoint = 10;
//此处可以待怪物与白值对照表完成后再修改
                    addToBot(new ApplyPowerAction(m,AbstractDungeon.player,new MonsterPoint(m,MonPoint)));
                }
            }
        }

    }

    //使用攻击牌时，计算ATTpoint以供调用
    public void onUseCard(AbstractCard card, UseCardAction action) {

        if (card.type == AbstractCard.CardType.ATTACK) {
            if (card.cardID.endsWith("_LL")){
                ATTpoint = (hbrLL * 2 + hbrLQ) / 3;
            }
            else if (card.cardID.endsWith("_LQ")){
                ATTpoint = (hbrLL + hbrLQ * 2) / 3;
            }
            else if (card.cardID.endsWith("_TJ")){
                ATTpoint = hbrTJ;
            }
            else if (card.cardID.endsWith("_ZY")){
                ATTpoint = hbrZY;
            }
            else if (card.cardID.endsWith("_WP")){
                ATTpoint = (hbrLL + hbrLQ) / 2;
            }
        }
    }

    //奖励添加点数增长
    public void onVictory() {
        AbstractDungeon.getCurrRoom().addCardReward(new PointReward());
    }

    //实现点数增长
    public void onObtainCard(AbstractCard c) {
        if (c.cardID.endsWith("card")){
            addToBot(new UseCardAction(c));
            AbstractDungeon.player.masterDeck.removeCard(c);
        }
    }

}
