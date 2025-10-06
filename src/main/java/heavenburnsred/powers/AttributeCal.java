package heavenburnsred.powers;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import heavenburnsred.cards.HbrTags;
import heavenburnsred.relics.Attribute;

import static heavenburnsred.BasicMod.makeID;

public class AttributeCal extends BasePower {
    public static final String POWER_ID = makeID("AttributeCal");
    private static final AbstractPower.PowerType TYPE = AbstractPower.PowerType.BUFF;
    private static final boolean TURN_BASED = false;

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
        // 理论上可以保证有tag都是攻击牌，不过先留着这个if吧
        if (card.type == AbstractCard.CardType.ATTACK) {
            float deltaAttack = 0;
            if (card.hasTag(HbrTags.LL)){
                deltaAttack = Attribute.getAttackPoint()[0] - Attribute.getMonPoint();
            }
            else if (card.hasTag(HbrTags.LQ)){
                deltaAttack = Attribute.getAttackPoint()[1] - Attribute.getMonPoint();
            }
            else if (card.hasTag(HbrTags.TJ)){
                deltaAttack = Attribute.getAttackPoint()[2] - Attribute.getMonPoint();
            }
            else if (card.hasTag(HbrTags.ZY)){
                deltaAttack = Attribute.getAttackPoint()[3] - Attribute.getMonPoint();
            }
            else if (card.hasTag(HbrTags.WP)){
                deltaAttack = Attribute.getAttackPoint()[4] - Attribute.getMonPoint();
            }
            damage *= calculateGiveDamageRatio(deltaAttack);
        }
        return damage;
    }

    // 目前采用保留2位小数的描述方式
    public void updateDescription() {
        this.description =
                "力量型攻击基础倍率：" + String.format("%.2f", calculateGiveDamageRatio(Attribute.getAttackPoint()[0] - Attribute.getMonPoint())) + "。" + DESCRIPTIONS[1] +
                "灵巧型攻击基础倍率：" + String.format("%.2f", calculateGiveDamageRatio(Attribute.getAttackPoint()[1] - Attribute.getMonPoint())) + "。" + DESCRIPTIONS[1] +
                "体精型攻击基础倍率：" + String.format("%.2f", calculateGiveDamageRatio(Attribute.getAttackPoint()[2] - Attribute.getMonPoint())) + "。" + DESCRIPTIONS[1] +
                "智运型攻击基础倍率：" + String.format("%.2f", calculateGiveDamageRatio(Attribute.getAttackPoint()[3] - Attribute.getMonPoint())) + "。" + DESCRIPTIONS[1] +
                "均衡型攻击基础倍率：" + String.format("%.2f", calculateGiveDamageRatio(Attribute.getAttackPoint()[4] - Attribute.getMonPoint())) + "。";
    }

    // 防止战斗中被移除，未测试
    // 被反伤反死再复活是有buff的，不过没测试没有这一条方法buff是不是真的会消失
//    public void onRemove(){
//        if (AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT){
//            addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new AttributeCal(AbstractDungeon.player, -1)));
//        }
//    }
}
