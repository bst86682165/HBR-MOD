package heavenburnsred.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import heavenburnsred.relics.Attribute;

import static heavenburnsred.BasicMod.makeID;

public class SpecialOrderPower extends BasePower {
    public static final String POWER_ID = makeID(SpecialOrderPower.class.getSimpleName());
    private static final PowerType TYPE = PowerType.BUFF;
    private static final int UPPER_LIMIT = 10;

    public SpecialOrderPower(AbstractCreature owner, int amount) {
        super(POWER_ID, TYPE, false, owner, amount);
    }

    public void onInitialApplication(){
        Attribute.AddTempAttribute(amount,amount,amount,amount);
        updateDescription();
    }

    public void stackPower(int stackAmount){
        int extra_added = this.amount + stackAmount - UPPER_LIMIT;
        int stack_layer;
        // 没超上限时，按stackAmount来
        if (extra_added <= 0) {
            stack_layer = stackAmount;
        }
        // 超上限时，补齐到上限
        else {
            stack_layer = UPPER_LIMIT - this.amount;
        }
        super.stackPower(stack_layer);
        if (stack_layer > 0) {
            Attribute.AddTempAttribute(stack_layer,stack_layer,stack_layer,stack_layer);
            updateDescription();
        }
    }

    public void updateDescription() {
        this.description = DESCRIPTIONS[0]+amount+DESCRIPTIONS[1];
    }

    public void onRemove(){
        Attribute.AddTempAttribute(-amount,-amount,-amount,-amount);
    }
}
