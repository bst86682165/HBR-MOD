package heavenburnsred.cards.attack;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import heavenburnsred.cards.skill.FallingintoaFantasy;
import heavenburnsred.character.MyCharacter;
import heavenburnsred.patches.CountCards;
import heavenburnsred.util.CardStats;

public class UltraSisters extends HBRHitAndTypeAttackCard {
    public static final String ID = makeID(UltraSisters.class.getSimpleName()); //makeID adds the mod ID, so the final ID will be something like "modID:MyCard"
    public static final CardStats info = new CardStats(
            MyCharacter.Meta.CARD_COLOR, //The card color. If you're making your own character, it'll look something like this. Otherwise, it'll be CardColor.RED or similar for a basegame character color.
            CardType.ATTACK, //The type. ATTACK/SKILL/POWER/CURSE/STATUS
            CardRarity.SPECIAL, //Rarity. BASIC is for starting cards, then there's COMMON/UNCOMMON/RARE, and then SPECIAL and CURSE. SPECIAL is for cards you only get from events. Curse is for curses, except for special curses like Curse of the Bell and Necronomicurse.
            CardTarget.ENEMY, //The target. Single target is ENEMY, all enemies is ALL_ENEMY. Look at cards similar to what you want to see what to use.
            1);//The card's base cost. -1 is X cost, -2 is no cost for unplayable cards like curses, or Reflex.

    //These will be used in the constructor. Technically you can just use the values directly,
    //but constants at the top of the file are easy to adjust.
    private static final int DAMAGE = 3;
    private static final int UPG_DAMAGE = 1;
    private static final int BLOCK = 4;
    private static final int UPG_BLOCK = 1;
    private int cachedBlock = 0;

    public UltraSisters() {
        super(ID,info); //Pass the required information to the BaseCard constructor.

        setDamage(DAMAGE, UPG_DAMAGE);//Sets the card's damage and how much it changes when upgraded.
        setBlock(BLOCK,UPG_BLOCK);
        this.cardsToPreview = new FallingintoaFantasy();
    }

    public void applyPowers() {
        int FFcount = CountCards.CountCardsInWholeDeck(FallingintoaFantasy.ID);
        int realBaseDamage = this.baseDamage;   // 先保存真正的基础伤害
        int realBaseBlock = this.baseBlock;
        this.baseDamage = this.baseDamage * FFcount;
        this.baseBlock = this.baseBlock * FFcount;
        super.applyPowers();    // 调用父类方法
        this.cachedBlock = this.block;
        this.baseDamage = realBaseDamage;  // 恢复基础
        this.baseBlock = realBaseBlock;
        this.isDamageModified = (this.damage != this.baseDamage);
        this.isBlockModified = (this.block != this.baseBlock);
    }

    public void calculateCardDamage(AbstractMonster mo) {
        int FFcount = CountCards.CountCardsInWholeDeck(FallingintoaFantasy.ID);
        int realBaseDamage = this.baseDamage;
        this.baseDamage = this.baseDamage * FFcount;
        super.calculateCardDamage(mo);  // 计算对特定怪物的伤害（考虑易伤、虚弱等）
        this.baseDamage = realBaseDamage;
        this.isDamageModified = (this.damage != this.baseDamage);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        //int FFcount = CountCards.CountCardsInWholeDeck(FallingintoaFantasy.ID);
        addToBot(new GainBlockAction(p,p,cachedBlock));
        addToBot(new DamageAction(m, new DamageInfo(p, damage, DamageInfo.DamageType.NORMAL), AbstractGameAction.AttackEffect.SLASH_VERTICAL));
    }

    @Override
    public AbstractCard makeCopy() { //Optional
        return new UltraSisters();
    }
}
