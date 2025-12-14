package heavenburnsred.cards.skill;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.unique.RemoveDebuffsAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import heavenburnsred.cards.BaseCard;
import heavenburnsred.character.MyCharacter;
import heavenburnsred.actions.ApplyNotStackingPowerAction;
import heavenburnsred.powers.CriticalHit;
import heavenburnsred.powers.OverDriveState;
import heavenburnsred.util.CardStats;

public class TrailBlazer extends BaseCard {
    public static final String ID = makeID(TrailBlazer.class.getSimpleName()); //makeID adds the mod ID, so the final ID will be something like "modID:MyCard"
    public static final CardStats info = new CardStats(
            MyCharacter.Meta.CARD_COLOR, //The card color. If you're making your own character, it'll look something like this. Otherwise, it'll be CardColor.RED or similar for a basegame character color.
            CardType.SKILL, //The type. ATTACK/SKILL/POWER/CURSE/STATUS
            CardRarity.UNCOMMON, //Rarity. BASIC is for starting cards, then there's COMMON/UNCOMMON/RARE, and then SPECIAL and CURSE. SPECIAL is for cards you only get from events. Curse is for curses, except for special curses like Curse of the Bell and Necronomicurse.
            CardTarget.SELF, //The target. Single target is ENEMY, all enemies is ALL_ENEMY. Look at cards similar to what you want to see what to use.
            2);//The card's base cost. -1 is X cost, -2 is no cost for unplayable cards like curses, or Reflex.

    private static final int BLOCK = 10;  // 使用为加攻效果的回合数

    public TrailBlazer() {
        super(ID,info); //Pass the required information to the BaseCard constructor.
        //Sets the card's damage and how much it changes when upgraded.
        setBlock(BLOCK);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GainBlockAction(p,block));
        addToBot(new ApplyNotStackingPowerAction(p,p,new CriticalHit(p,-1)));
        if(p.hasPower(OverDriveState.POWER_ID)){
            // 升级清除所有负面
            if (this.upgraded) {
                addToBot(new RemoveDebuffsAction(p));
            }
            // 未升级获得人工制品
            else {
                addToBot(new ApplyPowerAction(p, p, new ArtifactPower(p, 1), 1));
            }
        }
    }

    @Override
    public AbstractCard makeCopy() { //Optional
        return new TrailBlazer();
    }
}

