package heavenburnsred.powers;

import com.evacipated.cardcrawl.mod.stslib.patches.core.AbstractCreature.TempHPField;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import static heavenburnsred.BasicMod.makeID;

public class AngelBeatsLoseHPPower extends BasePower {
    public static final String POWER_ID = makeID(AngelBeatsLoseHPPower.class.getSimpleName());
    private static final PowerType TYPE = PowerType.DEBUFF;

    public AngelBeatsLoseHPPower(AbstractCreature owner, int amount) {
        super(POWER_ID, TYPE, true, owner, amount);
    }

    private void safeLoseHP() {
        AbstractPlayer p = AbstractDungeon.player;
        int LOSS = this.amount;
        if (this.amount > p.currentHealth + TempHPField.tempHp.get(p))
            LOSS = p.currentHealth + TempHPField.tempHp.get(p) - 1;
        addToBot(new LoseHPAction(p, p, LOSS));
    }

    @Override
    public void onVictory() {
        safeLoseHP();
    }

    @Override
    public void atEndOfRound() {
        safeLoseHP();
        addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, this.ID));
    }

    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
    }
}
