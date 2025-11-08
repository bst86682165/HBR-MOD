package heavenburnsred.cards.skill;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import heavenburnsred.actions.QuickTemporalActionHelper;
import heavenburnsred.cards.BaseCard;
import heavenburnsred.character.MyCharacter;
import heavenburnsred.powers.TokenPower;
import heavenburnsred.util.CardStats;

public class TokenInstinct extends BaseCard {
    public static final String ID = makeID(TokenInstinct.class.getSimpleName()); //makeID adds the mod ID, so the final ID will be something like "modID:MyCard"
    public static final CardStats info = new CardStats(
            MyCharacter.Meta.CARD_COLOR, //The card color. If you're making your own character, it'll look something like this. Otherwise, it'll be CardColor.RED or similar for a basegame character color.
            CardType.SKILL, //The type. ATTACK/SKILL/POWER/CURSE/STATUS
            CardRarity.UNCOMMON, //Rarity. BASIC is for starting cards, then there's COMMON/UNCOMMON/RARE, and then SPECIAL and CURSE. SPECIAL is for cards you only get from events. Curse is for curses, except for special curses like Curse of the Bell and Necronomicurse.
            CardTarget.SELF, //The target. Single target is ENEMY, all enemies is ALL_ENEMY. Look at cards similar to what you want to see what to use.
            1);//The card's base cost. -1 is X cost, -2 is no cost for unplayable cards like curses, or Reflex.

    //These will be used in the constructor. Technically you can just use the values directly,
    //but constants at the top of the file are easy to adjust.
    private static final int BLOCK = 6;
    private static final int UPG_BLOCK = 2;
    private static final int TOKEN_GAIN = 1;

    public TokenInstinct() {
        super(ID,info); //Pass the required information to the BaseCard constructor.

        setBlock(BLOCK,UPG_BLOCK);
        setMagic(TOKEN_GAIN);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        {
            addToBot(new ApplyPowerAction(p, p, new TokenPower(p, this.magicNumber), this.magicNumber));
            QuickTemporalActionHelper.addToBotQuick(() -> {
                int token_layers = p.getPower(TokenPower.POWER_ID).amount;
                addToTop(new ReducePowerAction(p, p, TokenPower.POWER_ID, token_layers));
                for (int i = 0; i < token_layers; i++) {
                    addToTop(new GainBlockAction(p, p, this.block));
                }
            });
        }
    }

}
