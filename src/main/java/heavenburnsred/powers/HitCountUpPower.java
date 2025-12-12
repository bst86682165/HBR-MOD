package heavenburnsred.powers;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import heavenburnsred.actions.ChangeODBarHitAction;

import static heavenburnsred.BasicMod.makeID;

public class HitCountUpPower extends BasePower {
    public static final String POWER_ID = makeID(HitCountUpPower.class.getSimpleName());
    private static final PowerType TYPE = PowerType.BUFF;

    public HitCountUpPower(AbstractCreature owner, int amount) {
        super(POWER_ID, TYPE, false, owner, amount);
    }

    @Override
    public void onPlayCard(AbstractCard card, AbstractMonster m) {
        addToBot(new ChangeODBarHitAction(this.amount));
    }

    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
    }
}
