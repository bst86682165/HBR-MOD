package heavenburnsred.relics;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.actions.watcher.ChangeStanceAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import heavenburnsred.stances.RiceField;

import static heavenburnsred.BasicMod.makeID;

public class RiceOfZang extends BaseRelic {
    private static final String NAME = RiceOfZang.class.getSimpleName(); //The name will be used for determining the image file as well as the ID.
    public static final String ID = makeID(NAME); //This adds the mod's prefix to the relic ID, resulting in modID:MyRelic
    private static final RelicTier RARITY = RelicTier.BOSS; //The relic's rarity.
    private static final LandingSound SOUND = LandingSound.CLINK; //The sound played when the relic is clicked.


    public RiceOfZang() {
        super(ID, NAME, RARITY, SOUND);
    }

    public void atBattleStart() {
        flash();
        addToTop((AbstractGameAction)new ChangeStanceAction(RiceField.STANCE_ID));
        addToTop((AbstractGameAction)new RelicAboveCreatureAction((AbstractCreature)AbstractDungeon.player, this));
    }

    @Override
    public void onUseCard(AbstractCard targetCard, UseCardAction useCardAction) {
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }
}
