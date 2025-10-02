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

    //对怪物造成伤害时，计算伤害
    public float atDamageReceive(float damage, DamageInfo.DamageType type) {
        int Point = Attribute.getATTpoint() - this.amount;
        float DamageRatio = 0;
        if (Point >= 10) {
            DamageRatio = 2;
        } else if (Point < 10 && Point >= 0) {
            DamageRatio = Point / 10f + 1;
        } else if (Point < 0 && Point >= -20) {
            DamageRatio = (20 + Point) / 20f;
        } else if (Point < -20) {
            DamageRatio = 0;
        }
        damage = damage * DamageRatio;
        return damage;
    }

    public void updateDescription() {
        this.description = DESCRIPTIONS[0];
    }

}
