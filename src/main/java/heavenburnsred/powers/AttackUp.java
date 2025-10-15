package heavenburnsred.powers;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;

import static heavenburnsred.BasicMod.makeID;

public class AttackUp extends HBRTurnStackPower {
    public static final String POWER_ID = makeID(AttackUp.class.getSimpleName());
    private static final AbstractPower.PowerType TYPE = AbstractPower.PowerType.BUFF;
    private static final boolean TURN_BASED = true;
    //The only thing TURN_BASED controls is the color of the number on the power icon.
    //Turn based powers are white, non-turn based powers are red or green depending on if their amount is positive or negative.
    //For a power to actually decrease/go away on its own they do it themselves.
    //Look at powers that do this like VulnerablePower and DoubleTapPower.

    public AttackUp(AbstractCreature owner, int amount, int stack_layers) {
        super(POWER_ID, TYPE, TURN_BASED, owner, amount, stack_layers);
        this.priority = 6;  // 和幻杀一个等级，乘算的优先级为6，力量的优先级为5
    }

    public float atDamageGive(float damage, DamageInfo.DamageType type) {
        if (!this.isEffected) {  // 计算伤害时第一个加攻buff
            int layers = calculateTotalLayers();
            for (AbstractPower p : this.owner.powers) {
                if (p.ID.equals(this.ID)) {
                    ((HBRTurnStackPower)p).isEffected = true;  // 先把所有的标记为true，防止其他同名buff重复计算
                }
            }
            this.isEffected = false;  // 由于不会再把本buff调用一遍atDamageGive，因此可以还原为false
            //If NORMAL (attack) damage, modify damage by this power's amount
            return type == DamageInfo.DamageType.NORMAL ? damage * (1 + layers * 0.5F) : damage;
        }
        // 计算伤害时其他的buff，不改变damage的值，同时还原为false
        this.isEffected = false;
        return damage;
    }

    public void updateDescription() {
        // 返回上限为2的层数，每层50%加攻量
        this.description = DESCRIPTIONS[0] + (calculateTotalLayers() * 50) + "% 。";
    }
}
