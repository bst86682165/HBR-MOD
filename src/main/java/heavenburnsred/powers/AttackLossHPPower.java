package heavenburnsred.powers;

import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static heavenburnsred.BasicMod.makeID;

public class AttackLossHPPower extends BasePower {
    public static final String POWER_ID = makeID(AttackLossHPPower.class.getSimpleName());
    private static final PowerType TYPE = PowerType.DEBUFF;
    private final int hp_loss;

    public AttackLossHPPower(AbstractCreature owner, int amount, int hp_loss) {
        super(POWER_ID, TYPE, true, owner, amount);
        this.hp_loss = hp_loss;
        updateDescription();
    }

    @Override
    public void onPlayCard(AbstractCard card, AbstractMonster m) {
        if (card.type == AbstractCard.CardType.ATTACK) {
            AbstractCreature p = AbstractDungeon.player;
            addToTop(new LoseHPAction(p, p, hp_loss));
        }
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (this.amount == 0) {
            addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, this.ID));
        } else {
            addToBot(new ReducePowerAction(this.owner, this.owner, this.ID, 1));
        }
    }

    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.hp_loss + DESCRIPTIONS[1] + this.amount + DESCRIPTIONS[2];
    }
}
