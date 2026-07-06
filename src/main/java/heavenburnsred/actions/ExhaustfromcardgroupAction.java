package heavenburnsred.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;


public class ExhaustfromcardgroupAction extends AbstractGameAction {

    public static final String[] TEXT = new String[]{"请选择1张牌消耗"};

    private AbstractPlayer p;
    private int Pile;

    public ExhaustfromcardgroupAction(int amount, int PileType) {
        this.p = AbstractDungeon.player;
        setValues(this.p, AbstractDungeon.player, amount);
        this.actionType = AbstractGameAction.ActionType.CARD_MANIPULATION;
        this.duration = Settings.ACTION_DUR_MED;
        this.Pile = PileType;
    }

    public void update() {
        if (this.duration == Settings.ACTION_DUR_MED) {
            CardGroup tmp = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
            if (this.Pile == 1){
                for (AbstractCard c : this.p.drawPile.group) {
                    tmp.addToRandomSpot(c);
                }
            }
            if (this.Pile == 2){
                for (AbstractCard c : this.p.discardPile.group) {
                    tmp.addToRandomSpot(c);
                }
            }
            if (tmp.isEmpty()) {
                this.isDone = true;
                return;
            }
            AbstractDungeon.gridSelectScreen.open(tmp, this.amount, TEXT[0], false);
            tickDuration();
            return;
        }
        if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
            for (AbstractCard c : AbstractDungeon.gridSelectScreen.selectedCards) {
                c.unhover();
                if (this.Pile == 1){
                this.p.drawPile.moveToExhaustPile(c);}
                if (this.Pile == 2){
                this.p.discardPile.moveToExhaustPile(c);}
                /*if (c.type == AbstractCard.CardType.CURSE || c.type == AbstractCard.CardType.STATUS)
                {
                    addToBot(new DrawCardAction(1));
                }*/
            }

            AbstractDungeon.gridSelectScreen.selectedCards.clear();
        }
        tickDuration();
    }}