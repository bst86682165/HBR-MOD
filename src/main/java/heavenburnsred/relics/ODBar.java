package heavenburnsred.relics;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
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
import heavenburnsred.cards.Defend;
import heavenburnsred.patches.HBRRelicClick;


import java.util.Objects;

import static heavenburnsred.BasicMod.makeID;

public class ODBar extends HBRRelicClick{
    private static final String NAME = "ODBar"; //The name will be used for determining the image file as well as the ID.
    public static final String ID = makeID(NAME); //This adds the mod's prefix to the relic ID, resulting in modID:MyRelic
    private static final AbstractRelic.RelicTier RARITY = AbstractRelic.RelicTier.COMMON; //The relic's rarity.
    private static final AbstractRelic.LandingSound SOUND = AbstractRelic.LandingSound.CLINK; //The sound played when the relic is clicked.

    private static final int HITCOUNT = 120;
    private int handcards = 0;
    private int monstercounts = 0;

    public ODBar() {
        super(ID, NAME, RARITY, SOUND);
        this.counter = 0;
    }

    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (card.type == AbstractCard.CardType.ATTACK) {
            if (card.target == AbstractCard.CardTarget.ENEMY) {
                this.counter = this.counter + card.magicNumber;
            }
            else if (card.target == AbstractCard.CardTarget.ALL_ENEMY) {
                for (AbstractMonster mon : (AbstractDungeon.getMonsters()).monsters) {
                    if (!mon.isDeadOrEscaped())
                        monstercounts++;
                }
                this.counter = this.counter + monstercounts;
                monstercounts = 0;
            }
            //else if (Objects.equals(card.cardID, "heavenburnsred:Defend")){
            //    this.counter = this.counter + 2;
            //}

            if (this.counter > HITCOUNT){
                this.counter = HITCOUNT;
            }
        }
    }

    public void onRightClick() {
        AbstractPlayer p = AbstractDungeon.player;
        if (!this.usedUp && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT && this.counter >= 40) {
            this.counter = this.counter - 40;
            handcards = AbstractDungeon.player.hand.size();
            addToBot(new DiscardAction(p,p,handcards,true));
            addToBot(new DrawCardAction(p,5));
            addToBot(new GainEnergyAction(3));
        }

    }


    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }
}
