package heavenburnsred.powers;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import heavenburnsred.cards.attack.HBRHitAndTypeAttackCard;
import heavenburnsred.relics.Attribute;

import static heavenburnsred.BasicMod.makeID;

public class AttributeCal extends BasePower {
    public static final String POWER_ID = makeID("AttributeCal");
    private static final AbstractPower.PowerType TYPE = AbstractPower.PowerType.BUFF;
    private static final boolean TURN_BASED = false;
    private static final int CRITICAL = 10;

    public AttributeCal(AbstractCreature owner, int amount) {
        super(POWER_ID, TYPE, TURN_BASED, owner, amount);
        this.priority = 3;  // 优先级仅次于混乱
    }

    // 计算不同类型的伤害倍率
    private float calculateGiveDamageRatio(float deltaAttack) {
        float DamageRatio = 0;
        if (deltaAttack >= 10) {
            DamageRatio = 2;
        } else if (deltaAttack < 10 && deltaAttack >= 0) {
            DamageRatio = deltaAttack / 10f + 1;
        } else if (deltaAttack < 0 && deltaAttack >= -20) {
            DamageRatio = (20 + deltaAttack) / 20f;
        } else if (deltaAttack < -20) {
            DamageRatio = 0;
        }
        return DamageRatio;
    }

    // 对怪物造成伤害时，计算伤害数值
    @Override
    public float atDamageGive(float damage, DamageInfo.DamageType type, AbstractCard card) {
        if (type != DamageInfo.DamageType.NORMAL) {
            return damage;
        }
        float deltaAttack = 0;
        // 针对HBRHitAndTypeAttackCard类别的卡
        if (card instanceof HBRHitAndTypeAttackCard) {
            HBRHitAndTypeAttackCard.HBRAttackType attackCardType = ((HBRHitAndTypeAttackCard)card).attackPreference;
            switch (attackCardType) {
                case LL:
                    deltaAttack = Attribute.getAttackPoint()[0] - Attribute.getMonPoint();
                    break;
                case LQ:
                    deltaAttack = Attribute.getAttackPoint()[1] - Attribute.getMonPoint();
                    break;
                case TJ:
                    deltaAttack = Attribute.getAttackPoint()[2] - Attribute.getMonPoint();
                    break;
                case ZY:
                    deltaAttack = Attribute.getAttackPoint()[3] - Attribute.getMonPoint();
                    break;
                case WP:
                    deltaAttack = Attribute.getAttackPoint()[4] - Attribute.getMonPoint();
                    break;
            }
        // 针对非本mod的攻击卡，默认为无偏
        } else if (card.type == AbstractCard.CardType.ATTACK) {
            deltaAttack = Attribute.getAttackPoint()[4] - Attribute.getMonPoint();
        }

        //处理暴击效果
        if (AbstractDungeon.player.hasPower(CriticalHit.POWER_ID)){
            deltaAttack += CRITICAL;
        }

        damage *= calculateGiveDamageRatio(deltaAttack);
        return damage;
    }

    // 目前采用保留2位小数的描述方式
    public void updateDescription() {
        int tmpDelta = 0;
        String tmpSeperation = "";
        if (AbstractDungeon.player.hasPower(CriticalHit.POWER_ID)) {
            tmpDelta = CRITICAL;
            tmpSeperation = " #y";
        }
        this.description =
                "力量型攻击倍率：" + tmpSeperation + String.format("%.2f", calculateGiveDamageRatio(tmpDelta + Attribute.getAttackPoint()[0] - Attribute.getMonPoint())) + " 。" + DESCRIPTIONS[1] +
                "灵巧型攻击倍率：" + tmpSeperation + String.format("%.2f", calculateGiveDamageRatio(tmpDelta + Attribute.getAttackPoint()[1] - Attribute.getMonPoint())) + " 。" + DESCRIPTIONS[1] +
                "体精型攻击倍率：" + tmpSeperation + String.format("%.2f", calculateGiveDamageRatio(tmpDelta + Attribute.getAttackPoint()[2] - Attribute.getMonPoint())) + " 。" + DESCRIPTIONS[1] +
                "智运型攻击倍率：" + tmpSeperation + String.format("%.2f", calculateGiveDamageRatio(tmpDelta + Attribute.getAttackPoint()[3] - Attribute.getMonPoint())) + " 。" + DESCRIPTIONS[1] +
                "均衡型攻击倍率：" + tmpSeperation + String.format("%.2f", calculateGiveDamageRatio(tmpDelta + Attribute.getAttackPoint()[4] - Attribute.getMonPoint())) + " 。";
    }

    // 防止战斗中被移除，未测试
    // 被反伤反死再复活是有buff的，不过没测试没有这一条方法buff是不是真的会消失
//    public void onRemove(){
//        if (AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT){
//            addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new AttributeCal(AbstractDungeon.player, -1)));
//        }
//    }
}
