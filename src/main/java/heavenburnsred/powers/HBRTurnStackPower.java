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
    public int stack_layers;  // 存储该buff的层数，例如单独发动可以设为2
    // apply这里用public可能不够安全，不过别瞎调用这个应该就可以了

    public HBRTurnStackPower(String POWER_ID, AbstractPower.PowerType TYPE, boolean TURN_BASED, AbstractCreature owner, int amount, int stack_layers) {
        super(POWER_ID, TYPE, TURN_BASED, owner, null, amount, false);
        this.stack_layers = stack_layers;
        this.updateDescription();  // 保证建立完完整实例后调用一遍描述更新
    }

    // 回合结束后要么清除能力，要么减少回合层数
    public void atEndOfRound() {
        if (this.amount == 0) {
            addToBot((AbstractGameAction)new RemoveSpecificPowerAction(this.owner, this.owner, this.ID));
        } else {
            addToBot((AbstractGameAction)new ReducePowerAction(this.owner, this.owner, this.ID, 1));
        }
    }

    // 叠加时叠加左上角的buff层数而不是右下角的回合层数
    @Override
    public void stackPower(int stack_layers) {
        this.fontScale = 8.0F;
        this.stack_layers += stack_layers;
    }

    // render左上角buff层数
    @Override
    public void renderAmount(SpriteBatch sb, float x, float y, Color c) {
        super.renderAmount(sb, x, y, c);

        if (this.stack_layers != 0) {
            FontHelper.renderFontRightTopAligned(sb, FontHelper.powerAmountFont, Integer.toString(this.stack_layers), x - 16.0F * Settings.scale, y + 16.0F * Settings.scale, this.fontScale, Color.GREEN);
        }
    }
}
