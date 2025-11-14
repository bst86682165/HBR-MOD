package heavenburnsred.powers;

import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.ThoughtBubble;

import static heavenburnsred.BasicMod.makeID;

public class NeverHurtAnyonePower extends BasePower {
    public static final String POWER_ID = makeID(NeverHurtAnyonePower.class.getSimpleName());
    private static final PowerType TYPE = PowerType.BUFF;

    public NeverHurtAnyonePower(AbstractCreature owner, int amount) {
        super(POWER_ID, TYPE, false, owner, amount);
    }

    @Override
    public void onPlayCard(AbstractCard card, AbstractMonster m) {
        if (card.type == AbstractCard.CardType.ATTACK) {
            String description = card.name;
            description = (card.upgraded && description.endsWith("+")) ? description.substring(0, description.length() - 1) : description;
            AbstractDungeon.effectList.add(new ThoughtBubble(AbstractDungeon.player.dialogX, AbstractDungeon.player.dialogY, 2.0F, description + "喵~", true));
            // 下面这个写法卡动画呃呃了
//            addToBot(new TalkAction(true, description + "喵~", 1.0F, 2.0F));
        }
    }

    public void updateDescription() {
        this.description = DESCRIPTIONS[0];
    }
}
