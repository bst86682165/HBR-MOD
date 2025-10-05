package heavenburnsred.cards.skill;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.ThoughtBubble;
import heavenburnsred.cards.BaseCard;
import heavenburnsred.cards.attack.UltraSisters;
import heavenburnsred.character.MyCharacter;
import heavenburnsred.patches.UpgradeCardsByTypeAction;
import heavenburnsred.powers.UltraSistersPower;
import heavenburnsred.util.CardStats;
import java.util.Objects;

public class FallingintoaFantasy extends BaseCard {
    public static final String ID = makeID(FallingintoaFantasy.class.getSimpleName()); //makeID adds the mod ID, so the final ID will be something like "modID:MyCard"
    public static final CardStats info = new CardStats(
            MyCharacter.Meta.CARD_COLOR, //The card color. If you're making your own character, it'll look something like this. Otherwise, it'll be CardColor.RED or similar for a basegame character color.
            CardType.SKILL, //The type. ATTACK/SKILL/POWER/CURSE/STATUS
            CardRarity.SPECIAL, //Rarity. BASIC is for starting cards, then there's COMMON/UNCOMMON/RARE, and then SPECIAL and CURSE. SPECIAL is for cards you only get from events. Curse is for curses, except for special curses like Curse of the Bell and Necronomicurse.
            CardTarget.SELF, //The target. Single target is ENEMY, all enemies is ALL_ENEMY. Look at cards similar to what you want to see what to use.
            1);//The card's base cost. -1 is X cost, -2 is no cost for unplayable cards like curses, or Reflex.

    //These will be used in the constructor. Technically you can just use the values directly,
    //but constants at the top of the file are easy to adjust.
    private static final int MAGIC = 1;
    private static final int UPG_BLOCK = 2;

    public FallingintoaFantasy() {
        super(ID,info); //Pass the required information to the BaseCard constructor.
        setMagic(MAGIC);
        setBlock(0,UPG_BLOCK);//Sets the card's damage and how much it changes when upgraded.
        this.isEthereal = true;
    }

    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            upgradeBlock(UPG_BLOCK);
            this.isEthereal = false;
            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
    private int countCardsInHand() {
        if (AbstractDungeon.player == null || AbstractDungeon.player.hand == null) {
            return 0;
        }
        int count = 0;
        for (AbstractCard card : AbstractDungeon.player.hand.group) {
            if (Objects.equals(card.cardID, FallingintoaFantasy.ID)) {
                count++;
            }
        }
        return count;
    }

    private void updateCostBasedOnHand() {
        int CardCount = countCardsInHand();

        if (CardCount >= 2) {
            if (this.costForTurn != 0) {
                this.costForTurn = 0;
                this.isCostModifiedForTurn = true;
            }
        } else {
            if (this.costForTurn != baseCost) {
                this.costForTurn = baseCost;
                this.isCostModifiedForTurn = (baseCost != this.cost);
            }
        }
    }
    private void playEffects() {
        CardCrawlGame.sound.play("CARD_UPGRADE");
        AbstractDungeon.effectList.add(new ThoughtBubble(
                AbstractDungeon.player.dialogX, AbstractDungeon.player.dialogY, 3.0f,
                "这就是大岛家的羁绊！", true
        ));
    }


    @Override
    public void applyPowers() {
        super.applyPowers();
        updateCostBasedOnHand();
    }
    @Override
    public void triggerOnOtherCardPlayed(AbstractCard c) {
        updateCostBasedOnHand();
    }

    @Override
    public void triggerWhenDrawn() {
        updateCostBasedOnHand();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        {
            addToBot(new DrawCardAction(magicNumber));
            if(this.upgraded){
                addToBot(new GainBlockAction(p,block));
            }
        }
    }

    @Override
    public AbstractCard makeCopy() { //Optional
        return new FallingintoaFantasy();
    }

    @Override
    public AbstractCard makeStatEquivalentCopy(){
        AbstractCard MakeNewCard = super.makeStatEquivalentCopy();
        if(AbstractDungeon.isPlayerInDungeon()){
            if(AbstractDungeon.player.hasPower(UltraSistersPower.POWER_ID)){
                MakeNewCard.upgrade();
            }
            else {
            int count = 0;
            for (AbstractCard card : AbstractDungeon.player.hand.group) {
                if (Objects.equals(card.cardID, FallingintoaFantasy.ID)) {
                    count++;
                }
            }
            for (AbstractCard card : AbstractDungeon.player.drawPile.group) {
                if (Objects.equals(card.cardID, FallingintoaFantasy.ID)) {
                    count++;
                }
            }
            for (AbstractCard card : AbstractDungeon.player.discardPile.group) {
                if (Objects.equals(card.cardID, FallingintoaFantasy.ID)) {
                    count++;
                }
            }
            if (count == 11) {
                MakeNewCard.upgrade();
                addToBot(new UpgradeCardsByTypeAction(card -> card.cardID.contains("FallingintoaFantasy"), "沉入梦幻"));
                addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new UltraSistersPower(AbstractDungeon.player, 1)));
                addToBot(new MakeTempCardInHandAction(new UltraSisters(), 1));
                playEffects();
            }
            }
        }
        return MakeNewCard;
    }

}
