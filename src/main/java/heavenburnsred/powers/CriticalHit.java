package heavenburnsred.powers;

import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import heavenburnsred.actions.UpdateAttributeDescriptionAction;
import heavenburnsred.relics.Attribute;

import static heavenburnsred.BasicMod.makeID;

public class CriticalHit extends BasePower{
    public static final String POWER_ID = makeID(CriticalHit.class.getSimpleName());
    private static final AbstractPower.PowerType TYPE = AbstractPower.PowerType.BUFF;
    private static final boolean TURN_BASED = false;

    public CriticalHit(AbstractCreature owner, int amount) {
        super(POWER_ID, TYPE, TURN_BASED, owner, amount);
    }

    public void onUseCard(AbstractCard targetCard, UseCardAction useCardAction){
        if (targetCard.type == AbstractCard.CardType.ATTACK){
            addToBot(new RemoveSpecificPowerAction(AbstractDungeon.player,AbstractDungeon.player, CriticalHit.POWER_ID));
            addToBot(new UpdateAttributeDescriptionAction());
        }
    }

    @Override
    public void onInitialApplication() {
        addToBot(new UpdateAttributeDescriptionAction());
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0];
    }
}
