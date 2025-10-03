package heavenburnsred.relics;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;

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
    private static float MonPoint;
    private static float[] AttackPoint = new float[5];

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

    public static float getMonPoint() {
        return MonPoint;
    }

    public static void setMonPoint(float monPoint) {
        MonPoint = monPoint;
    }

    public static float[] getAttackPoint() {
        return AttackPoint;
    }

    public static void setAttackPoint(float[] attackPoint) {
        AttackPoint = attackPoint;
    }

    private void initializeAttackPoint() {
        // 这里是向下取整了
        float LL_preferred = (Attribute.getHbrLL() * 2f + Attribute.getHbrLQ()) / 3f;
        float LQ_preferred = (Attribute.getHbrLL() + Attribute.getHbrLQ() * 2f) / 3f;
        float TJ_preferred = Attribute.getHbrTJ();
        float ZY_preferred = Attribute.getHbrZY();
        float No_preferred = (Attribute.getHbrLL() + Attribute.getHbrLQ()) / 2f;
        AttackPoint[0] = LL_preferred;
        AttackPoint[1] = LQ_preferred;
        AttackPoint[2] = TJ_preferred;
        AttackPoint[3] = ZY_preferred;
        AttackPoint[4] = No_preferred;
    }

    //计算怪物白值，此处可以待怪物与白值对照表完成后再修改
    private float calculateMonPoint() {
        int Floor = AbstractDungeon.floorNum;
        int Act = AbstractDungeon.actNum;
        return 10;
    }

    public void atBattleStartPreDraw(){
        this.flash();
        // 战斗开始先初始化怪物白值和己方5种攻击牌的攻击点数
        setMonPoint(calculateMonPoint());
        initializeAttackPoint();
        //在此书写智运提高后的增益
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
                    addToBot(new ApplyPowerAction(m,AbstractDungeon.player,new MonsterPoint(m,(int)MonPoint)));
                }
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

    // 选完提升的属性之后刷新显示
    public void onSelectPoint() {
        this.flash();
        this.tips.clear();
        this.tips.add(new PowerTip(this.name, this.getUpdatedTip()));
        this.initializeTips();
    }

    // 为什么规定遗物一定需要这个呢？
    @Override
    public void onUseCard(AbstractCard targetCard, UseCardAction useCardAction) {

    }
}
