package heavenburnsred.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;

import java.util.ArrayList;

public class tDKDAction extends AbstractGameAction {

    private int damage;
    private DamageInfo.DamageType damageType;
    private AbstractPlayer player;

    public tDKDAction(AbstractPlayer player, int damage) {
        this.player = player;
        this.damage = damage;
        this.damageType = DamageInfo.DamageType.NORMAL;
        this.actionType = ActionType.DAMAGE;
    }

    @Override
    public void update() {
        if (this.isDone) {
            return;
        }

        int totalDamageDealt = 0;
        ArrayList<AbstractMonster> monsters = AbstractDungeon.getMonsters().monsters;

        // 遍历所有敌人，造成伤害并累计实际伤害
        for (AbstractMonster m : monsters) {
            if (!m.isDeadOrEscaped()) {
                DamageInfo damageInfo = new DamageInfo(player, damage, damageType);
                m.damage(damageInfo);
                totalDamageDealt += damageInfo.output;
                AbstractDungeon.effectList.add(new FlashAtkImgEffect(m.hb.cX, m.hb.cY, AttackEffect.SLASH_HORIZONTAL));
            }
        }

        // 根据总伤害提升hit数
        if (totalDamageDealt > 0) {
            addToBot(new ChangeODBarHitAction(totalDamageDealt));
        }

        this.isDone = true;
    }
}
