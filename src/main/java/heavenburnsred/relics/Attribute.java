package heavenburnsred.relics;

import basemod.patches.com.megacrit.cardcrawl.dungeons.AbstractDungeon.RemoveExcludedCardsFromPools;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import com.megacrit.cardcrawl.rewards.RewardItem;
import heavenburnsred.cards.PointCard.LLcard;
import heavenburnsred.cards.PointCard.LQcard;
import heavenburnsred.cards.PointCard.TJcard;
import heavenburnsred.cards.PointCard.ZYcard;
import heavenburnsred.powers.AttributeCal;
import heavenburnsred.powers.MonsterPoint;


import static heavenburnsred.BasicMod.makeID;
import static heavenburnsred.patches.MonsterPointMap.MPMap;

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
    public static RewardItem AddPoint = new RewardItem();


    public Attribute() {
        super(ID, NAME, RARITY, SOUND);
        AddPoint.cards.clear();
        AddPoint.cards.add(0, new LLcard());
        AddPoint.cards.add(0, new LQcard());
        AddPoint.cards.add(0, new TJcard());
        AddPoint.cards.add(0, new ZYcard());
    }

    //以buff的形式赋予自身防御值与怪兽白值,改为每回合开始以应对复活的情况
    public void atStartOfTurnPostDraw() {
        if (!AbstractDungeon.player.hasPower("heavenburnsred:AttributeCal")){
            addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new AttributeCal(AbstractDungeon.player, 1)));
        }
        if (!AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
            for (AbstractMonster m : (AbstractDungeon.getMonsters()).monsters) {
                if (!m.isDead && !m.isDying && !m.hasPower("heavenburnsred:MonsterPoint")){
                    int MonPoint = MPMap.get(m.id);
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
    public void onVictory(){
        AbstractDungeon.getCurrRoom().addCardReward(AddPoint);
    }


    public void onObtainCard(AbstractCard c) {
        if (c.cardID.endsWith("card")){
            addToBot(new UseCardAction(c));
            addToBot(new );
        }
    }


}
