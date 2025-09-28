package heavenburnsred.powers;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;

import static heavenburnsred.BasicMod.makeID;
import static heavenburnsred.patches.MonsterPointMap.MPMap;
import static heavenburnsred.relics.Attribute.ATTpoint;
import static heavenburnsred.relics.Attribute.hbrTJ;

public class AttributeCal extends BasePower {
    public static final String POWER_ID = makeID("AttributeCal");
    private static final AbstractPower.PowerType TYPE = AbstractPower.PowerType.BUFF;
    private static final boolean TURN_BASED = false;
    //The only thing TURN_BASED controls is the color of the number on the power icon.
    //Turn based powers are white, non-turn based powers are red or green depending on if their amount is positive or negative.
    //For a power to actually decrease/go away on its own they do it themselves.
    //Look at powers that do this like VulnerablePower and DoubleTapPower.

    public AttributeCal(AbstractCreature owner, int amount) {
        super(POWER_ID, TYPE, TURN_BASED, owner, amount);
    }

    public float atDamageGive(float damage, DamageInfo.DamageType type) {
        if (type == DamageInfo.DamageType.NORMAL) {
            float DamageRatio = 0;
            if (ATTpoint >= 10) {
                DamageRatio = 2;
            } else if (ATTpoint < 10 && ATTpoint >= 0) {
                DamageRatio = ATTpoint / 10f + 1;
            } else if (ATTpoint < 0 && ATTpoint >= -20) {
                DamageRatio = (20 + ATTpoint) / 20f;
            } else if (ATTpoint < -20) {
                DamageRatio = 0;
            }
            damage = damage * DamageRatio;
        }
        return damage;
    }

    public int onAttacked(DamageInfo info, int damageAmount){
        int floor = AbstractDungeon.floorNum;
        int act = AbstractDungeon.actNum;
        int DEFpoint = MPMap.get(info.owner.id) + 6 * act + floor / 4 - hbrTJ ;
        float DamageRatio = 0;
        if (DEFpoint >= 10) {
            DamageRatio = 2;
        } else if (DEFpoint < 10 && DEFpoint >= 0) {
            DamageRatio = DEFpoint / 10f + 1;
        } else if (DEFpoint < 0 && DEFpoint >= -20) {
            DamageRatio = (20 + DEFpoint) / 20f;
        } else if (DEFpoint < -20) {
            DamageRatio = 0;
        }
        damageAmount = (int)(damageAmount * DamageRatio);
        return damageAmount;
    }

    public void updateDescription() {
        this.description = DESCRIPTIONS[0];
    }
}
