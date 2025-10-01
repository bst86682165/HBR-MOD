package heavenburnsred.powers;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class HBRTurnStackPower extends BasePower {
    protected int stack_layers;  // 存储该buff的层数，例如单独发动可以设为2
    protected boolean isEffected;  // 默认为false

    public HBRTurnStackPower(String POWER_ID, AbstractPower.PowerType TYPE, boolean TURN_BASED, AbstractCreature owner, int amount, int stack_layers) {
        super(POWER_ID, TYPE, TURN_BASED, owner, null, amount, false);
        this.stack_layers = stack_layers;
//        this.updateDescription();  // 所有描述更新都在ApplyHBRStackPowerAction加到状态栏之后完成
    }

    public int getStack_layers() {
        return this.stack_layers;
    }

    @Override
    public int compareTo(AbstractPower other) {
        // 能够直接用priority排序
        int cmp = this.priority - other.priority;
        if (cmp != 0) return cmp;

        // 如果 priority 相同，再比较回合数（amount）
        // 注意：要处理 null 或不存在的情况
        Integer thisTurns = this.amount;
        Integer otherTurns = (other instanceof HBRTurnStackPower) ? other.amount : null;

        // 非本mod的排后面，剩下的回合数小的在前
        return otherTurns == null ? -1 : thisTurns - otherTurns;
    }

    // 对于debuff，回合结束后要么清除能力，要么减少回合层数
    @Override
    public void atEndOfRound() {
        if (this.type == PowerType.DEBUFF) {
            if (this.amount == 0) {
                addToBot((AbstractGameAction) new RemoveSpecificPowerAction(this.owner, this.owner, this.ID));
            } else {
                addToBot((AbstractGameAction) new ReducePowerAction(this.owner, this.owner, this.ID, 1));
            }
        }
    }

    // 对于buff，回合结束后要么清除能力，要么减少回合层数
    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (this.type == PowerType.BUFF) {
            if (this.amount == 0) {
                addToBot((AbstractGameAction) new RemoveSpecificPowerAction(this.owner, this.owner, this.ID));
            } else {
                addToBot((AbstractGameAction) new ReducePowerAction(this.owner, this.owner, this.ID, 1));
            }
        }
    }

    @Override
    public void atStartOfTurn() {
        // 回合开始时由于层数减少也要更新描述
        this.updateDescription();
    }

    // 计算不同回合数的buff的总层数，上限为2
    protected int calculateTotalLayers() {
        int total_layers = 0;
        for (AbstractPower p : this.owner.powers) {
            if (p.ID.equals(this.ID)) {
                total_layers += ((HBRTurnStackPower)p).stack_layers;
            }
        }
        return Math.min(total_layers, 2);
    }

    // 叠加时叠加左上角的buff层数而不是右下角的回合层数
    @Override
    public void stackPower(int stack_layers) {
        this.fontScale = 8.0F;
        this.stack_layers += stack_layers;
    }

    // render左上角buff/debuff层数，分别为绿/红色
    @Override
    public void renderAmount(SpriteBatch sb, float x, float y, Color c) {
        super.renderAmount(sb, x, y, c);
        c = this.type == PowerType.DEBUFF ? Color.RED : Color.GREEN;
        if (this.stack_layers != 0) {
            FontHelper.renderFontRightTopAligned(sb, FontHelper.powerAmountFont, Integer.toString(this.stack_layers), x, y + 16.0F * Settings.scale, this.fontScale, c);
        }
    }
}
