package heavenburnsred.powers;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;

import static heavenburnsred.BasicMod.makeID;

public class AttackUp extends HBRTurnStackPower {
    public static final String POWER_ID = makeID(AttackUp.class.getSimpleName());
    private static final AbstractPower.PowerType TYPE = AbstractPower.PowerType.BUFF;
    private static final boolean TURN_BASED = true;
    private boolean isEffected;  // 默认为false
    //The only thing TURN_BASED controls is the color of the number on the power icon.
    //Turn based powers are white, non-turn based powers are red or green depending on if their amount is positive or negative.
    //For a power to actually decrease/go away on its own they do it themselves.
    //Look at powers that do this like VulnerablePower and DoubleTapPower.

    public AttackUp(AbstractCreature owner, int amount, int stack_layers) {
        super(POWER_ID, TYPE, TURN_BASED, owner, amount, stack_layers);
    }

    public float atDamageGive(float damage, DamageInfo.DamageType type) {
        //If NORMAL (attack) damage, modify damage by this power's amount
        if (!this.isEffected) {
            int total_layers = 0;
            for (AbstractPower p : AbstractDungeon.player.powers) {
                if (p.ID.equals(this.ID)) {
                    total_layers += ((AttackUp)p).stack_layers;
                    ((AttackUp) p).isEffected = true;
                }
            }
            int layers = Math.min(total_layers, 2);
            this.isEffected = false;
            return type == DamageInfo.DamageType.NORMAL ? damage * (1 + layers * 0.5F) : damage;
        }
        this.isEffected = false;
        return damage;
    }

    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + (Math.min(stack_layers, 2) * 50) + "%";
    }
}
