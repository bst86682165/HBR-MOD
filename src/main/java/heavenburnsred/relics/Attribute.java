package heavenburnsred.relics;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import com.megacrit.cardcrawl.rooms.MonsterRoomBoss;
import com.megacrit.cardcrawl.rooms.MonsterRoomElite;
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

    // 定义非静态成员变量四维
    private int hbrLL = 10;
    private int hbrLQ = 10;
    private int hbrTJ = 10;
    private int hbrZY = 10;
    // 定义每次战斗重新计算的静态成员变量，怪物白值和我方攻击白值
    private static float MonPoint;
    private static float[] AttackPoint = new float[5];

    // 构建遗物实例及其描述，刷新鼠标放在其上出现的tips
    public Attribute() {
        super(ID, NAME, RARITY, SOUND);
        this.counter = encodeStates(hbrLL,hbrLQ,hbrTJ,hbrZY);
        this.tips.clear();
        this.tips.add(new PowerTip(this.name, this.getUpdatedTip()));
        this.initializeTips();
    }

    // 设立4个属性的get和plus方法
    public int getHbrLL() {
        return hbrLL;
    }

    public void plusHbrLL(int delta_hbrLL) {
        this.hbrLL += delta_hbrLL;
        // 加入上限120
        if (this.hbrLL > 120) this.hbrLL = 120;
        this.counter = encodeStates(hbrLL,hbrLQ,hbrTJ,hbrZY);
    }

    public int getHbrLQ() {
        return hbrLQ;
    }

    public void plusHbrLQ(int delta_hbrLQ) {
        this.hbrLQ += delta_hbrLQ;
        // 加入上限120
        if (this.hbrLQ > 120) this.hbrLQ = 120;
        this.counter = encodeStates(hbrLL,hbrLQ,hbrTJ,hbrZY);
    }

    public int getHbrTJ() {
        return hbrTJ;
    }

    public void plusHbrTJ(int delta_hbrTJ) {
        this.hbrTJ += delta_hbrTJ;
        // 加入上限120
        if (this.hbrTJ > 120) this.hbrTJ = 120;
        this.counter = encodeStates(hbrLL,hbrLQ,hbrTJ,hbrZY);
    }

    public int getHbrZY() {
        return hbrZY;
    }

    public void plusHbrZY(int delta_hbrZY) {
        this.hbrZY += delta_hbrZY;
        // 加入上限120
        if (this.hbrZY > 120) this.hbrZY = 120;
        this.counter = encodeStates(hbrLL,hbrLQ,hbrTJ,hbrZY);
    }

    // 把4个属性每个占8位存储到counter这个32位int中，便于原生sl的保存和加载
    // 将4个属性编码为1个32位整数
    public static int encodeStates(int s1, int s2, int s3, int s4) {
        return (s4 & 0xFF) |
                ((s3 & 0xFF) << 8) |
                ((s2 & 0xFF) << 16) |
                ((s1 & 0xFF) << 24);
    }
    // 把1个32位整数解码为4个整数状态
    public static int[] decodeStates(int encoded) {
        int s4 = (encoded) & 0xFF;
        int s3 = (encoded >> 8) & 0xFF;
        int s2 = (encoded >> 16) & 0xFF;
        int s1 = (encoded >> 24) & 0xFF;
        return new int[] { s1, s2, s3, s4 };
    }

    @Override
    // 在sl时会调用setCounter加载保存的counter值，在此处顺便加载4个属性值，并更新遗物tips
    public void setCounter(int counter) {
        super.setCounter(counter);
        int[] newStatus = decodeStates(this.counter);
        hbrLL = newStatus[0];
        hbrLQ = newStatus[1];
        hbrTJ = newStatus[2];
        hbrZY = newStatus[3];
        this.tips.clear();
        this.tips.add(new PowerTip(this.name, this.getUpdatedTip()));
        this.initializeTips();
    }

    // MonsterPoint的get、set方法
    public static float getMonPoint() {
        return MonPoint;
    }

    public static void setMonPoint(float monPoint) {
        MonPoint = monPoint;
    }

    // 战斗开局时调用，计算怪物白值，此处可以待怪物与白值对照表完成后再修改
    private float calculateMonPoint() {
        int Floor = AbstractDungeon.floorNum;
        int Act = AbstractDungeon.actNum;
        MonPoint = 6 + Floor + 3 * Act;
        if(AbstractDungeon.getCurrRoom() instanceof MonsterRoomBoss){
            MonPoint = MonPoint + 3;
        } else if (AbstractDungeon.getCurrRoom() instanceof MonsterRoomElite) {
            MonPoint = MonPoint + 1;
        }
        return MonPoint;
    }

    // 提供临时提升白值的方法，不改变本身白值，附加描述更新
    private static int TTLL = 0;
    private static int TTLQ = 0;
    private static int TTTJ = 0;
    private static int TTZY = 0;

    public int getCurTJ(){
        return TTTJ + hbrTJ;
    }
    public int getCurZY(){
        return TTZY + hbrZY;
    }

    public static void AddTempAttribute(int TempLL, int TempLQ, int TempTJ, int TempZY) {
        TTLL += TempLL;
        TTLQ += TempLQ;
        TTTJ += TempTJ;
        TTZY += TempZY;
        ((Attribute)AbstractDungeon.player.getRelic(Attribute.ID)).initializeAttackPoint();
        AbstractDungeon.player.getPower(AttributeCal.POWER_ID).updateDescription();
        for (AbstractMonster m : (AbstractDungeon.getMonsters()).monsters) {
            if (!m.isDead && !m.isDying && m.hasPower(MonsterPoint.POWER_ID)){
                m.getPower(MonsterPoint.POWER_ID).updateDescription();
            }
        }
        ((Attribute)AbstractDungeon.player.getRelic(Attribute.ID)).onSelectPoint();
    }

    // AttackPoint的get、set方法
    public static float[] getAttackPoint() {
        return AttackPoint;
    }

    public static void setAttackPoint(float[] attackPoint) {
        AttackPoint = attackPoint;
    }

    // 战斗开局时调用，初始化各类型攻击牌的attackpoint
    private void initializeAttackPoint() {
        // 这里全部用小数计算
        float LL_preferred = ((this.hbrLL + TTLL) * 2f + this.hbrLQ + TTLQ) / 3f;
        float LQ_preferred = (this.hbrLL + TTLL + (this.hbrLQ + TTLQ) * 2f) / 3f;
        float TJ_preferred = this.hbrTJ + TTTJ;
        float ZY_preferred = this.hbrZY + TTZY;
        float No_preferred = (this.hbrLL + this.hbrLQ + TTLL + TTLQ) / 2f;
        AttackPoint[0] = LL_preferred;
        AttackPoint[1] = LQ_preferred;
        AttackPoint[2] = TJ_preferred;
        AttackPoint[3] = ZY_preferred;
        AttackPoint[4] = No_preferred;
    }

    public void atBattleStartPreDraw(){
        this.flash();
        // 战斗开始先初始化怪物白值和己方5种攻击牌的攻击点数
        TTLL = TTLQ = TTTJ = TTZY = 0;
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

    // 重置临时6维属性值
    public void onVictory() {
        TTLL = TTLQ = TTTJ = TTZY = 0;
        this.tips.clear();
        this.tips.add(new PowerTip(this.name, this.getUpdatedTip()));
        this.initializeTips();
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[1];
    }

    public String getUpdatedTip() {
        String LQ_text =
               TTZY > 0 ? ("+ #b" + TTZY + " 。") :
               TTZY < 0 ? ("- #r" + (-TTZY) + " 。") :
               "。";
        return DESCRIPTIONS[2] + DESCRIPTIONS[0] +
               "力量：" + this.hbrLL + (TTLL > 0 ? "+ #b" + TTLL + " 。":"。") + DESCRIPTIONS[0] +
               "灵巧：" + this.hbrLQ + (TTLQ > 0 ? "+ #b" + TTLQ + " 。":"。") + DESCRIPTIONS[0] +
               "体精：" + this.hbrTJ + (TTTJ > 0 ? "+ #b" + TTTJ + " 。":"。") + DESCRIPTIONS[0] +
               "智运：" + this.hbrTJ + LQ_text;
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
    public void onUseCard(AbstractCard targetCard, UseCardAction useCardAction) {}

    @Override
    public void renderCounter(SpriteBatch sb, boolean inTopPanel) {
        // 只利用counter储存4个状态，完全不需要任何游戏画面的渲染
    }
}
