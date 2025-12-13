package heavenburnsred.cards.attack;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import heavenburnsred.actions.ApplyHBRStackPowerAction;
import heavenburnsred.character.MyCharacter;
import heavenburnsred.powers.DefendDown;
import heavenburnsred.util.CardStats;

public class MythOfInari extends HBRHitAndTypeAttackCard {
    public static final String ID = makeID(MythOfInari.class.getSimpleName());
    public static final CardStats info = new CardStats(
            MyCharacter.Meta.CARD_COLOR, //The card color. If you're making your own character, it'll look something like this. Otherwise, it'll be CardColor.RED or similar for a basegame character color.
            CardType.ATTACK, //The type. ATTACK/SKILL/POWER/CURSE/STATUS
            CardRarity.UNCOMMON, //Rarity. BASIC is for starting cards, then there's COMMON/UNCOMMON/RARE, and then SPECIAL and CURSE. SPECIAL is for cards you only get from events. Curse is for curses, except for special curses like Curse of the Bell and Necronomicurse.
            CardTarget.ENEMY, //The target. Single target is ENEMY, all enemies is ALL_ENEMY. Look at cards similar to what you want to see what to use.
            2);//The card's base cost. -1 is X cost, -2 is no cost for unplayable cards like curses, or Reflex.

    private static final int DAMAGE = 7;
    private static final int UPG_DAMAGE = 2;

    private static final int MAGIC = 1;
    private static final int UPG_MAGIC = 1;

    private static final int DEBUFF_TURN = 2;

    private boolean costDown = false;  // 存储本回合是否已经减费，防止重复减费

    public MythOfInari() {
        super(ID,info);

        setDamage(DAMAGE, UPG_DAMAGE);
        setMagic(MAGIC, UPG_MAGIC);
    }

    // 确保复制时一并复制
    @Override
    public AbstractCard makeStatEquivalentCopy() {
        AbstractCard candidate = super.makeStatEquivalentCopy();
        ((MythOfInari)candidate).costDown = this.costDown;
        return candidate;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DamageAction(m, new DamageInfo(p, damage, DamageInfo.DamageType.NORMAL), AbstractGameAction.AttackEffect.SLASH_VERTICAL));
        addToBot(new ApplyHBRStackPowerAction(m,p,new DefendDown(m, DEBUFF_TURN, magicNumber)));
    }

    public boolean enemyHasDefendDown() {
        boolean ans = false;
        for (AbstractMonster m : (AbstractDungeon.getCurrRoom()).monsters.monsters) {
            if (!m.isDeadOrEscaped() && m.hasPower(DefendDown.POWER_ID)) {
                ans = true;
                break;
            }
        }
        return ans;
    }

    @Override
    public void applyPowers() {
        super.applyPowers();

        // 如果本回合刚刚上降防，costDown仍为false，可以减1费。（理论上降防不会消失，回合内不需要还原费用）
        if (!this.costDown && enemyHasDefendDown()) {
            // 费用0时再增加后减费失效，重新抽回来会刷新
            costForTurn -= 1;
            isCostModifiedForTurn = true;
            if (costForTurn < 0) costForTurn = 0;
            costDown = true;
        }
    }

    // 确保保留的牌到下一回合也刷新
    @Override
    public void atTurnStart() {
        costDown = false;
    }

    // 与costforturn都是进弃牌堆刷新
    @Override
    public void onMoveToDiscard() {
        costDown = false;
    }

    @Override
    public void triggerOnGlowCheck() {
        this.glowColor = AbstractCard.BLUE_BORDER_GLOW_COLOR.cpy();
        for (AbstractMonster m : (AbstractDungeon.getCurrRoom()).monsters.monsters) {
            if (!m.isDeadOrEscaped() && m.hasPower(DefendDown.POWER_ID)) {
                this.glowColor = AbstractCard.GOLD_BORDER_GLOW_COLOR.cpy();
                break;
            }
        }
    }
}
