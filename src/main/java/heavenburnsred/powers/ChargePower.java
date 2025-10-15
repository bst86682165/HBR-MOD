package heavenburnsred.powers;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;

import static heavenburnsred.BasicMod.makeID;

public class ChargePower extends BasePower {
    public static final String POWER_ID = makeID(ChargePower.class.getSimpleName());
    private static final PowerType TYPE = PowerType.BUFF;

    public ChargePower(AbstractCreature owner, int amount) {
        super(POWER_ID, TYPE, false, owner, amount);
        this.priority = 6;  // 和幻杀一个等级，乘算的优先级为6，力量的优先级为5
    }

    public float atDamageGive(float damage, DamageInfo.DamageType type) {
        if (type == DamageInfo.DamageType.NORMAL)
            return damage *= 1.1f;
        return damage;
    }

    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (card.type == AbstractCard.CardType.ATTACK) {
            flash();
            addToBot((AbstractGameAction)new RemoveSpecificPowerAction(this.owner, this.owner, ChargePower.POWER_ID));
        }
    }

    public void updateDescription() {
        this.description = DESCRIPTIONS[0];
    }
}
