package heavenburnsred.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import heavenburnsred.relics.Attribute;

import static heavenburnsred.BasicMod.makeID;

public class SpecialOrderPower extends BasePower {
    public static final String POWER_ID = makeID(SpecialOrderPower.class.getSimpleName());
    private static final PowerType TYPE = PowerType.BUFF;

    public SpecialOrderPower(AbstractCreature owner, int amount) {
        super(POWER_ID, TYPE, false, owner, amount);
    }

    public void onInitialApplication(){
        Attribute.AddTempAttribute(amount,amount,amount,amount);
        updateDescription();
    }

    public void stackPower(int stackAmount){
        super.stackPower(stackAmount);
        Attribute.AddTempAttribute(stackAmount,stackAmount,stackAmount,stackAmount);
        updateDescription();
    }

    public void updateDescription() {
        this.description = DESCRIPTIONS[0]+amount+DESCRIPTIONS[1];
    }

    public void onRemove(){
        Attribute.AddTempAttribute(-amount,-amount,-amount,-amount);
    }
}
