package heavenburnsred.powers;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.BlurPower;
import heavenburnsred.cards.skill.FallingintoaFantasy;

import java.util.Objects;

import static heavenburnsred.BasicMod.makeID;

public class DelightfulDashPower extends BasePower{
    public static final String POWER_ID = makeID(DelightfulDashPower.class.getSimpleName());
    private static final PowerType TYPE = PowerType.BUFF;
    private static final boolean TURN_BASED = false;
    private static boolean Upgraded = false;
    private static boolean First = true;
    //The only thing TURN_BASED controls is the color of the number on the power icon.
    //Turn based powers are white, non-turn based powers are red or green depending on if their amount is positive or negative.
    //For a power to actually decrease/go away on its own they do it themselves.
    //Look at powers that do this like VulnerablePower and DoubleTapPower.

    public DelightfulDashPower(AbstractCreature owner, int amount, boolean Upgraded) {
        super(POWER_ID, TYPE, TURN_BASED, owner, amount);
        DelightfulDashPower.Upgraded = Upgraded;
    }

    public int onAttacked(DamageInfo info, int damageAmount) {
        if (info.type != DamageInfo.DamageType.THORNS && info.type != DamageInfo.DamageType.HP_LOSS && info.owner != null && info.owner != this.owner && damageAmount > 0 && First) {
            flash();
            addToTop(new AbstractGameAction() {
                         @Override
                         public void update() {
                             if(Objects.equals(AbstractDungeon.player.hand.group.get(AbstractDungeon.player.hand.group.size() - 2).cardID, FallingintoaFantasy.ID)){
                                 addToBot(new GainBlockAction(AbstractDungeon.player,4));
                                 if (DelightfulDashPower.Upgraded){
                                     addToBot(new ApplyPowerAction(AbstractDungeon.player,AbstractDungeon.player,new BlurPower(AbstractDungeon.player,1)));
                                 }
                             }
                             this.isDone = true;
                         }
                     });
            addToTop(new MakeTempCardInHandAction(new FallingintoaFantasy(),1));
            addToTop(new DrawCardAction(1));
            First = false;
        }
        return damageAmount;
    }

    public void atStartOfTurn(){
        First = false;
    }

    public void atEndOfTurn(boolean isPlayer){
        First = true;
    }

    public void updateDescription() {
        this.description = DESCRIPTIONS[0];
    }
}
