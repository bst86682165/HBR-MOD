package heavenburnsred.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;

import static heavenburnsred.BasicMod.makeID;

public class PlayHBRForever extends BasePower {
    public static final String POWER_ID = makeID(PlayHBRForever.class.getSimpleName());
    private static final PowerType TYPE = PowerType.BUFF;

    public PlayHBRForever(AbstractCreature owner, int amount) {
        super(POWER_ID, TYPE, false, owner, amount);
    }

    public void updateDescription() {
        this.description = DESCRIPTIONS[0];
    }
}
