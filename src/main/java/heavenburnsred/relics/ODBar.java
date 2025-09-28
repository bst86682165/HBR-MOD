package heavenburnsred.relics;

import com.megacrit.cardcrawl.actions.common.DiscardAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import heavenburnsred.cards.HbrTags;
import heavenburnsred.patches.HBRRelicClick;
import heavenburnsred.cards.BaseCard;

import static heavenburnsred.BasicMod.makeID;

public class ODBar extends HBRRelicClick {
    private static final String NAME = "ODBar"; // The name will be used for determining the image file as well as the
                                                // ID.
    public static final String ID = makeID(NAME); // This adds the mod's prefix to the relic ID, resulting in
                                                  // modID:MyRelic
    private static final AbstractRelic.RelicTier RARITY = AbstractRelic.RelicTier.STARTER; // The relic's rarity.
    private static final AbstractRelic.LandingSound SOUND = AbstractRelic.LandingSound.CLINK; // The sound played when
                                                                                              // the relic is clicked.

    private static final int HITLIMIT = 120;

    public ODBar() {
        super(ID, NAME, RARITY, SOUND);
        this.counter = 0;
    }

    public void onUseCard(AbstractCard card, UseCardAction action) {
        // 攻击卡按hit数计算
        if (card.hasTag(HbrTags.HIT)) {
            if (card.type == AbstractCard.CardType.ATTACK) {  // 理论上有hit的一定是attack，但这个判断还是先放着吧
                if (card.target == AbstractCard.CardTarget.ENEMY) {
                    this.counter += ((BaseCard)card).customVar("hit");
                } else if (card.target == AbstractCard.CardTarget.ALL_ENEMY) {
                    int monsterCounts = 0;  // 计算并储存场上剩余怪物数量
                    for (AbstractMonster mon : (AbstractDungeon.getMonsters()).monsters) {
                        if (!mon.isDeadOrEscaped())
                            monsterCounts++;
                    }
                    this.counter += monsterCounts * ((BaseCard)card).customVar("hit");
                }
            }
        }

        // 直充od单独再计算
        if (card.hasTag(HbrTags.DIRECT_OD)) {
            this.counter += ((BaseCard)card).customVar("direct_od");
        }

        // 最后判断是否溢出
        if (this.counter > HITLIMIT) {
            this.counter = HITLIMIT;
        }
    }

    public void onRightClick() {
        AbstractPlayer p = AbstractDungeon.player;
        if (!this.usedUp && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT
                && this.counter >= 40) {
            this.counter = this.counter - 40;
            int handCards = AbstractDungeon.player.hand.size();
            addToBot(new DiscardAction(p, p, handCards, true));
            addToBot(new DrawCardAction(p, 5));
            addToBot(new GainEnergyAction(3));
        }

    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }
}
