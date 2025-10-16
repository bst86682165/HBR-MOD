package heavenburnsred.cards.power;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.defect.IncreaseMiscAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.combat.PowerBuffEffect;
import heavenburnsred.cards.BaseCard;
import heavenburnsred.character.MyCharacter;
import heavenburnsred.powers.AlreadySignUp;
import heavenburnsred.powers.PlayHBRForever;
import heavenburnsred.util.CardStats;

public class SignUpDaily extends BaseCard {
    public static final String ID = makeID(SignUpDaily.class.getSimpleName()); //makeID adds the mod ID, so the final ID will be something like "modID:MyCard"
    public static final CardStats info = new CardStats(
            MyCharacter.Meta.CARD_COLOR, //The card color. If you're making your own character, it'll look something like this. Otherwise, it'll be CardColor.RED or similar for a basegame character color.
            CardType.POWER, //The type. ATTACK/SKILL/POWER/CURSE/STATUS
            CardRarity.UNCOMMON, //Rarity. BASIC is for starting cards, then there's COMMON/UNCOMMON/RARE, and then SPECIAL and CURSE. SPECIAL is for cards you only get from events. Curse is for curses, except for special curses like Curse of the Bell and Necronomicurse.
            CardTarget.SELF, //The target. Single target is ENEMY, all enemies is ALL_ENEMY. Look at cards similar to what you want to see what to use.
            1);//The card's base cost. -1 is X cost, -2 is no cost for unplayable cards like curses, or Reflex.
    public final int SIGNUPTIMES = 7;


    public SignUpDaily() {
        super(ID,info); //Pass the required information to the BaseCard constructor.
         //Sets the card's damage and how much it changes when upgraded.
        this.misc = SIGNUPTIMES;  // 存储跨卡（战斗中和牌组中）的重要信息
        this.baseMagicNumber = this.misc;
        this.magicNumber = this.misc;
    }

    public void applyPowers() {
        this.baseMagicNumber = this.misc;
        this.magicNumber = this.misc;
        super.applyPowers();
        initializeDescription();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (this.misc <= 1) {
            CardCrawlGame.sound.play("GOLD_GAIN");
            AbstractDungeon.player.gainGold(220);
            if (this.upgraded) {
                addToBot((AbstractGameAction) new IncreaseMiscAction(this.uuid, this.misc, SIGNUPTIMES-1));
            }
            else {
                CardCrawlGame.sound.play("CARD_EXHAUST");
                for (int i = 0; i < AbstractDungeon.player.masterDeck.group.size(); i++) {
                    AbstractCard c = AbstractDungeon.player.masterDeck.group.get(i);
                    if (c.uuid.equals(this.uuid)) {
                        AbstractDungeon.player.masterDeck.removeCard(c);
                        break; // 找到后删除并退出循环
                    }
                }
            }
        } else {
            addToBot((AbstractGameAction)new IncreaseMiscAction(this.uuid, this.misc, -1));
        }
        // 玩一辈子红烧天堂
        if (this.upgraded) {
            if (!p.hasPower(PlayHBRForever.POWER_ID)) {
                addToBot(new ApplyPowerAction(p,p,new PlayHBRForever(p,-1)));
            } else {
                AbstractPower effectPower = p.getPower(PlayHBRForever.POWER_ID);
                effectPower.flash();
                AbstractDungeon.effectList.add(new PowerBuffEffect(p.hb.cX - p.animX, p.hb.cY + p.hb.height / 2.0F, effectPower.name));
            }
        } else {
            // 今日已签到
            if (!p.hasPower(AlreadySignUp.POWER_ID)) {
                addToBot(new ApplyPowerAction(p,p,new AlreadySignUp(p,-1)));
            } else {
                AbstractPower effectPower = p.getPower(AlreadySignUp.POWER_ID);
                effectPower.flash();
                AbstractDungeon.effectList.add(new PowerBuffEffect(p.hb.cX - p.animX, p.hb.cY + p.hb.height / 2.0F, effectPower.name));
            }
        }
    }

    @Override
    public AbstractCard makeCopy() { //Optional
        return new SignUpDaily();
    }
}
