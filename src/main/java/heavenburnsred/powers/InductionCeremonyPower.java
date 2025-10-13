package heavenburnsred.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import heavenburnsred.relics.Attribute;

import java.util.Objects;

import static heavenburnsred.BasicMod.makeID;

public class InductionCeremonyPower extends BasePower {
    public static final String POWER_ID = makeID(InductionCeremonyPower.class.getSimpleName());
    private static final PowerType TYPE = PowerType.BUFF;
    private static int Point = 0;
    private static int Tpoint = 0;

    public InductionCeremonyPower(AbstractCreature owner, int point) {
        super(POWER_ID, TYPE, false, owner, 1);
        Point = point;
    }

    public void onInitialApplication(){
        Tpoint = Point;
        ((Attribute) AbstractDungeon.player.getRelic(Attribute.ID)).AddTempAttribute(Point,Point,Point,Point);
        updateDescription();
    }

    public void stackPower(int point){
        Tpoint += Point;
        ((Attribute) AbstractDungeon.player.getRelic(Attribute.ID)).AddTempAttribute(Point,Point,Point,Point);
        updateDescription();
    }

    public void updateDescription() {
        this.description = DESCRIPTIONS[0]+Tpoint+DESCRIPTIONS[1];
    }

    public void onRemove(){
        new Attribute().AddTempAttribute(Tpoint,Tpoint,Tpoint,Tpoint);
        Tpoint = 0;
    }

}
