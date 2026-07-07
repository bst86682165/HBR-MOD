package heavenburnsred.powers;

import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;

import static heavenburnsred.BasicMod.makeID;

public class EnergizedLessNextTurnPower extends BasePower {
    public static final String POWER_ID = makeID(EnergizedLessNextTurnPower.class.getSimpleName());
    private static final PowerType TYPE = PowerType.DEBUFF;
    public EnergizedLessNextTurnPower(AbstractCreature owner, int amount) {
        super(POWER_ID, TYPE, false, owner, amount);
        this.priority = 20;
    }
    private static final PowerStrings powerStrings =
            CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    @Override
    public void updateDescription() {
        if (this.amount == 1) {
            this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
        } else {
            this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[2];
        }
    }

    // 能量重置后扣除
    @Override
    public void onEnergyRecharge() {
        flash();
        int tempEn = AbstractDungeon.player.energy.energy;
        AbstractDungeon.player.energy.energy = Math.max(0, AbstractDungeon.player.energy.energy - this.amount);
        EnergyPanel.totalCount = AbstractDungeon.player.energy.energy;
        addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, POWER_ID));
        AbstractDungeon.player.energy.energy = Math.min(tempEn,AbstractDungeon.player.energy.energy + this.amount);
    }
}