package heavenburnsred.powers;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import heavenburnsred.relics.Attribute;

import static heavenburnsred.BasicMod.makeID;

public class AttributeCal extends BasePower {
    public static final String POWER_ID = makeID("AttributeCal");
    private static final AbstractPower.PowerType TYPE = AbstractPower.PowerType.BUFF;
    private static final boolean TURN_BASED = false;

    public AttributeCal(AbstractCreature owner, int amount) {
        super(POWER_ID, TYPE, TURN_BASED, owner, amount);
    }

//收到伤害时计算减伤
    public int onAttacked(DamageInfo info, int damageAmount){
        if (info.type == DamageInfo.DamageType.NORMAL) {
            int MonsterPoint = info.owner.getPower("heavenburnsred:MonsterPoint").amount;
            int DEFpoint = MonsterPoint - Attribute.getHbrTJ();
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
            damageAmount = (int) (damageAmount * DamageRatio);
            return damageAmount;
        }
        return damageAmount;
    }

    public void updateDescription() {
        this.description = DESCRIPTIONS[0];
    }

    // 防止战斗中被移除，未测试
    public void onRemove(){
        if (AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT){
            addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new AttributeCal(AbstractDungeon.player, -1)));
        }
    }
}
