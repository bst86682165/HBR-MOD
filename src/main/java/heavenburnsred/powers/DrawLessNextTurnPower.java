package heavenburnsred.powers;

import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

import static heavenburnsred.BasicMod.makeID;

public class DrawLessNextTurnPower extends BasePower {
    public static final String POWER_ID = makeID(DrawLessNextTurnPower.class.getSimpleName());
    private static final PowerType TYPE = PowerType.DEBUFF;
    public DrawLessNextTurnPower(AbstractCreature owner, int amount) {
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

    // 回合开始、系统抽牌前：临时减少每回合抽牌数
    @Override
    public void atStartOfTurn() {
        AbstractDungeon.player.gameHandSize -= this.amount;
        if (AbstractDungeon.player.gameHandSize < 0) {
            AbstractDungeon.player.gameHandSize = 0;
        }
    }

    // 初始抽牌完成后去除
    @Override
    public void atStartOfTurnPostDraw() {
        AbstractDungeon.player.gameHandSize += this.amount;
        flash();
        addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, POWER_ID));
    }
}