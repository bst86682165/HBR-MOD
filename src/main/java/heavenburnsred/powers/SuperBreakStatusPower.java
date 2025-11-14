package heavenburnsred.powers;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import static heavenburnsred.BasicMod.makeID;

public class SuperBreakStatusPower extends BasePower {
    public static final String POWER_ID = makeID(SuperBreakStatusPower.class.getSimpleName());
    private static final PowerType TYPE = PowerType.BUFF;

    public SuperBreakStatusPower(AbstractCreature owner, int amount) {
        super(POWER_ID, TYPE, false, owner, amount);
    }

    public void onBreakBlock() {
        addToBot(new DrawCardAction(AbstractDungeon.player, amount));
    }

    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
    }
}
