package heavenburnsred.powers;

import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import heavenburnsred.BasicMod;
import heavenburnsred.cards.BaseCard;
import heavenburnsred.cards.HbrTags;

import static heavenburnsred.BasicMod.makeID;

public class FreeModCardPower extends BasePower {
    public static final String POWER_ID = makeID(FreeModCardPower.class.getSimpleName());
    private static final PowerType TYPE = PowerType.BUFF;

    public FreeModCardPower(AbstractCreature owner, int amount) {
        super(POWER_ID, TYPE, false, owner, amount);
    }

    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
    }

    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (card instanceof BaseCard && !card.hasTag(HbrTags.NO_GIRLS) && !card.purgeOnUse && this.amount > 0) {
            flash();
            this.amount--;
            if (this.amount == 0)
                addToTop(new RemoveSpecificPowerAction(this.owner, this.owner, FreeModCardPower.POWER_ID));
        }
    }
}
