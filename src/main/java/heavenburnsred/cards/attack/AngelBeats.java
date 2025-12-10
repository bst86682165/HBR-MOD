package heavenburnsred.cards.attack;

import com.evacipated.cardcrawl.mod.stslib.actions.tempHp.AddTemporaryHPAction;
import com.evacipated.cardcrawl.mod.stslib.patches.core.AbstractCreature.TempHPField;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import heavenburnsred.character.MyCharacter;
import heavenburnsred.powers.AlreadySignUp;
import heavenburnsred.powers.AngelBeatsLoseHPPower;
import heavenburnsred.util.CardStats;

public class AngelBeats extends HBRHitAndTypeAttackCard {
    public static final String ID = makeID(AngelBeats.class.getSimpleName());
    public static final CardStats info = new CardStats(
            MyCharacter.Meta.CARD_COLOR, //The card color. If you're making your own character, it'll look something like this. Otherwise, it'll be CardColor.RED or similar for a basegame character color.
            CardType.ATTACK, //The type. ATTACK/SKILL/POWER/CURSE/STATUS
            CardRarity.RARE, //Rarity. BASIC is for starting cards, then there's COMMON/UNCOMMON/RARE, and then SPECIAL and CURSE. SPECIAL is for cards you only get from events. Curse is for curses, except for special curses like Curse of the Bell and Necronomicurse.
            CardTarget.ENEMY, //The target. Single target is ENEMY, all enemies is ALL_ENEMY. Look at cards similar to what you want to see what to use.
            1);//The card's base cost. -1 is X cost, -2 is no cost for unplayable cards like curses, or Reflex.

    private static final int DAMAGE = 15;
    private static final int UPG_DAMAGE = 5;

    private static final int MAGIC = 25;
    private static final int UPG_MAGIC = 15;

    private static final float HP_UP_LIMIT = 1.5f;

    public AngelBeats() {
        super(ID,info,HBRAttackType.LL);

        setDamage(DAMAGE, UPG_DAMAGE);
        setMagic(MAGIC, UPG_MAGIC);
    }

    @Override
    public void applyPowers() {
        AbstractPlayer p = AbstractDungeon.player;
        int ADD_HP = Math.min(p.maxHealth * magicNumber / 100,
                (int)(p.maxHealth * HP_UP_LIMIT) - p.currentHealth - TempHPField.tempHp.get(p));
        this.baseDamage = (int)((this.upgraded ? DAMAGE+UPG_DAMAGE : DAMAGE) * (ADD_HP + p.currentHealth + TempHPField.tempHp.get(p)) / (float)p.maxHealth);
        super.applyPowers();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 取正常比例血量恢复或1.5倍上限的最小值
        int ADD_HP = Math.min(p.maxHealth * magicNumber / 100,
                (int)(p.maxHealth * HP_UP_LIMIT) - p.currentHealth - TempHPField.tempHp.get(p));
        // 恢复血量和临时血量，超上限的部分进行临时血量的增加
        int TMP_HP = ADD_HP + p.currentHealth - p.maxHealth;
        if (TMP_HP > 0) {
            addToBot(new HealAction(p, p, ADD_HP-TMP_HP));
            addToBot(new AddTemporaryHPAction(p, p, TMP_HP));
        }
        else {
            addToBot(new HealAction(p, p, ADD_HP));
        }
        // 临时增加的血量通过debuff失去
        addToBot(new ApplyPowerAction(p,p,new AngelBeatsLoseHPPower(p,ADD_HP), ADD_HP));
        addToBot(new DamageAction(m, new DamageInfo(p, damage, DamageInfo.DamageType.NORMAL), AbstractGameAction.AttackEffect.SLASH_VERTICAL));
    }
}
