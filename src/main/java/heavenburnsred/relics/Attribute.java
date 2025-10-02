package heavenburnsred.relics;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import heavenburnsred.cards.HbrTags;
import heavenburnsred.patches.PointReward;
import heavenburnsred.powers.AttributeCal;
import heavenburnsred.powers.MonsterPoint;

import static heavenburnsred.BasicMod.makeID;

public class Attribute extends BaseRelic
{
    private static final String NAME = "Attribute"; // The name will be used for determining the image file as well as the
    // ID.
    public static final String ID = makeID(NAME); // This adds the mod's prefix to the relic ID, resulting in
    // modID:MyRelic
    private static final AbstractRelic.RelicTier RARITY = AbstractRelic.RelicTier.STARTER; // The relic's rarity.
    private static final AbstractRelic.LandingSound SOUND = AbstractRelic.LandingSound.CLINK; // The sound played when
    // the relic is clicked.

    //定义四维
    private static int hbrLL = 10;
    private static int hbrLQ = 10;
    private static int hbrTJ = 10;
    private static int hbrZY = 10;
    private static int ATTpoint = 0;

    // 构建遗物实例及其描述，刷新鼠标放在其上出现的tips
    public Attribute() {
        super(ID, NAME, RARITY, SOUND);
        this.tips.clear();
        this.tips.add(new PowerTip(this.name, this.getUpdatedTip()));
        this.initializeTips();
    }

    public static int getHbrLL() {
        return hbrLL;
    }

    public static void plusHbrLL(int hbrLL) {
        Attribute.hbrLL += hbrLL;
    }

    public static int getHbrLQ() {
        return hbrLQ;
    }

    public static void plusHbrLQ(int hbrLQ) {
        Attribute.hbrLQ += hbrLQ;
    }

    public static int getHbrTJ() {
        return hbrTJ;
    }

    public static void plusHbrTJ(int hbrTJ) {
        Attribute.hbrTJ += hbrTJ;
    }

    public static int getHbrZY() {
        return hbrZY;
    }

    public static void plusHbrZY(int hbrZY) {
        Attribute.hbrZY += hbrZY;
    }

    public static int getATTpoint() {
        return ATTpoint;
    }

    public static void plusATTpoint(int ATTpoint) {
        Attribute.ATTpoint += ATTpoint;
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
            addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new AttributeCal(AbstractDungeon.player, -1)));
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

        // 理论上可以保证有tag都是攻击牌，不过先留着这个if吧
        if (card.type == AbstractCard.CardType.ATTACK) {
            if (card.hasTag(HbrTags.LL)){
                ATTpoint = (hbrLL * 2 + hbrLQ) / 3;
            }
            else if (card.hasTag(HbrTags.LQ)){
                ATTpoint = (hbrLL + hbrLQ * 2) / 3;
            }
            else if (card.hasTag(HbrTags.TJ)){
                ATTpoint = hbrTJ;
            }
            else if (card.hasTag(HbrTags.ZY)){
                ATTpoint = hbrZY;
            }
            else if (card.hasTag(HbrTags.WP)){
                ATTpoint = (hbrLL + hbrLQ) / 2;
            }
        }
    }

    //战斗奖励添加点数增长，添加PointReward类
    public void onVictory() {
        AbstractDungeon.getCurrRoom().addCardReward(new PointReward());
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[1];
    }

    public String getUpdatedTip() {
        return DESCRIPTIONS[2] + DESCRIPTIONS[0] +
               "力量" + Attribute.getHbrLL() + "。" + DESCRIPTIONS[0] +
               "灵巧" + Attribute.getHbrLQ() + "。" + DESCRIPTIONS[0] +
               "体精" + Attribute.getHbrTJ() + "。" + DESCRIPTIONS[0] +
               "智运" + Attribute.getHbrZY() + "。";
    }

    public void onSelectPoint() {
        this.flash();
        this.tips.clear();
        this.tips.add(new PowerTip(this.name, this.getUpdatedTip()));
        this.initializeTips();
    }
}
