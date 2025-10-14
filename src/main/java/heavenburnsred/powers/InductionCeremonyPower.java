package heavenburnsred.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import heavenburnsred.relics.Attribute;

import static heavenburnsred.BasicMod.makeID;

public class InductionCeremonyPower extends BasePower {
    public static final String POWER_ID = makeID(InductionCeremonyPower.class.getSimpleName());
    private static final PowerType TYPE = PowerType.BUFF;

    public InductionCeremonyPower(AbstractCreature owner, int amount) {
        super(POWER_ID, TYPE, false, owner, amount);
    }

    public void onInitialApplication(){
        ((Attribute) AbstractDungeon.player.getRelic(Attribute.ID)).AddTempAttribute(amount,amount,amount,amount);
        updateDescription();
    }

    public void stackPower(int stackAmount){
        super.stackPower(stackAmount);
        ((Attribute) AbstractDungeon.player.getRelic(Attribute.ID)).AddTempAttribute(stackAmount,stackAmount,stackAmount,stackAmount);
        updateDescription();
    }

    public void updateDescription() {
        this.description = DESCRIPTIONS[0]+amount+DESCRIPTIONS[1];
    }

    public void onRemove(){
        Attribute.AddTempAttribute(-amount,-amount,-amount,-amount);
    }
}
