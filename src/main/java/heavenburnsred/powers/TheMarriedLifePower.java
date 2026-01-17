package heavenburnsred.powers;

import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import heavenburnsred.cards.skill.CharlotComposition;

import static heavenburnsred.BasicMod.makeID;

public class TheMarriedLifePower extends BasePower {
    public static final String POWER_ID = makeID(TheMarriedLifePower.class.getSimpleName());
    private static final PowerType TYPE = PowerType.BUFF;
    private static int Counter = 1;
    private static final int Counter_Length = 9;

    public TheMarriedLifePower(AbstractCreature owner, int amount) {
        super(POWER_ID, TYPE, false, owner, amount);
    }

    public void atEndOfTurn(boolean playerTurn) {
        // 放牌库顶
        CharlotComposition card = new CharlotComposition();
        card.setM(this.amount, Counter);
        addToBot(new MakeTempCardInDrawPileAction(card, 1, false, true));
        Counter = Counter % Counter_Length + 1;
    }

    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
    }
}
