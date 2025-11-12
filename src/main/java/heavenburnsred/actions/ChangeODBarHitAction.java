
package heavenburnsred.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import heavenburnsred.cards.BaseCard;
import heavenburnsred.cards.HbrTags;
import heavenburnsred.cards.attack.DoubleInOne;
import heavenburnsred.powers.AttributeCal;
import heavenburnsred.relics.ODBar;

import java.util.Objects;

import static heavenburnsred.BasicMod.makeID;

public class ChangeODBarHitAction extends AbstractGameAction {
    AbstractCard card;

    // 直充od/消耗od调用这个
    public ChangeODBarHitAction(int del_hit) {
        this.amount = del_hit;
        this.card = null;
    }
    // 打攻击牌在ODBar遗物里调用这个
    public ChangeODBarHitAction(AbstractCard card) {
        this.amount = 0;
        this.card = card;
    }

    @Override
    public void update() {
        // 有该遗物时生效hit数改变，防止其他角色拿本mod卡报错
        if (AbstractDungeon.player.hasRelic(ODBar.ID)) {
            ODBar odbar = (ODBar)AbstractDungeon.player.getRelic(ODBar.ID);

            // 保存旧的counter数
            int old_counter = odbar.counter;

            // 如果只有amount被设置，则为直充od/消耗od
            if (card == null && amount != 0) {
                odbar.counter += amount;
            }
            // 有卡牌的情况，为ODBar遗物调用，攻击卡，且非切换卡改变hit
            else if (card != null && !card.tags.contains(HbrTags.SELECT_CARD) && card.type == AbstractCard.CardType.ATTACK) {
                int hit_per_mon;
                if (card.hasTag(HbrTags.HIT)) {  // 理论上有hit的一定是attack，表示本mod的攻击牌
                    hit_per_mon = ((BaseCard) card).customVar("hit");
                }
                else {
                    hit_per_mon = 1;  // 非本mod的攻击牌，给1hit数
                }
                if (card.target == AbstractCard.CardTarget.ENEMY) {
                    odbar.counter += hit_per_mon;
                } else if (card.target == AbstractCard.CardTarget.ALL_ENEMY) {
                    int monsterCounts = 0;  // 计算并储存场上剩余怪物数量
                    for (AbstractMonster mon : (AbstractDungeon.getMonsters()).monsters) {
                        if (!mon.isDeadOrEscaped())
                            monsterCounts++;
                    }
                    odbar.counter += monsterCounts * hit_per_mon;
                }
            }

            // 最后判断是否溢出
            if (odbar.counter > odbar.hit_limit) {
                odbar.counter = odbar.hit_limit;
            }

            // 利用新旧counter数检测counter的改变，是否需要图片修改
            int new_counter = odbar.counter;
            if (new_counter != old_counter) odbar.onCounterChanged(old_counter, new_counter);
        }
        this.isDone = true;
    }
}
