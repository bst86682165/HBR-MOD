package heavenburnsred.powers;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import heavenburnsred.relics.Attribute;

import static heavenburnsred.BasicMod.makeID;

public class MonsterPoint extends BasePower{

    public static final String POWER_ID = makeID("MonsterPoint");
    private static final AbstractPower.PowerType TYPE = AbstractPower.PowerType.BUFF;
    private static final boolean TURN_BASED = false;


    public MonsterPoint(AbstractCreature owner, int amount) {
        super(POWER_ID, TYPE, TURN_BASED, owner, amount);
    }

    // 计算被怪物攻击时的伤害倍率
    private float calculateBeAttackedRatio() {
        float MonsterPoint = Attribute.getMonPoint();
        float DEFpoint = MonsterPoint - Attribute.getHbrTJ();
        float DamageRatio = 0;
        if (DEFpoint >= 10) {
            DamageRatio = 2;
        } else if (DEFpoint < 10 && DEFpoint >= 0) {
            DamageRatio = DEFpoint / 10f + 1;
        } else if (DEFpoint < 0 && DEFpoint >= -20) {
            DamageRatio = (20 + DEFpoint) / 20f;
        } else if (DEFpoint < -20) {
            DamageRatio = 0;
        }
        return DamageRatio;
    }

    // 受到伤害时计算减伤，可以直接反应到怪物头上的数值
    @Override
    public float atDamageGive(float damage, DamageInfo.DamageType type) {
        if (type == DamageInfo.DamageType.NORMAL) {
            damage *= calculateBeAttackedRatio();
        }
        return damage;
    }

    // 目前采用保留2位小数的描述方式
    public void updateDescription() {
        this.description = "造成伤害倍率：" + String.format("%.2f", calculateBeAttackedRatio()) + "。";
    }
}
