package heavenburnsred.cards.attack;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.watcher.ChooseOneAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.LoseStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import heavenburnsred.actions.ApplyHBRStackPowerAction;
import heavenburnsred.actions.BlockRelatedDamageAction;
import heavenburnsred.cards.HbrTags;
import heavenburnsred.character.MyCharacter;
import heavenburnsred.powers.AttackUp;
import heavenburnsred.relics.ODBar;
import heavenburnsred.util.CardStats;

import java.util.ArrayList;

import static heavenburnsred.util.GeneralUtils.removePrefix;
import static heavenburnsred.util.TextureLoader.getCardTextureString;

public class DoubleInOne extends HBRHitAndTypeAttackCard {
    public static final String ID = makeID(DoubleInOne.class.getSimpleName()); //makeID adds the mod ID, so the final ID will be something like "modID:MyCard"
    public static final CardStats info = new CardStats(
            MyCharacter.Meta.CARD_COLOR, //The card color. If you're making your own character, it'll look something like this. Otherwise, it'll be CardColor.RED or similar for a basegame character color.
            CardType.ATTACK, //The type. ATTACK/SKILL/POWER/CURSE/STATUS
            CardRarity.RARE, //Rarity. BASIC is for starting cards, then there's COMMON/UNCOMMON/RARE, and then SPECIAL and CURSE. SPECIAL is for cards you only get from events. Curse is for curses, except for special curses like Curse of the Bell and Necronomicurse.
            CardTarget.ENEMY, //The target. Single target is ENEMY, all enemies is ALL_ENEMY. Look at cards similar to what you want to see what to use.
            1);//The card's base cost. -1 is X cost, -2 is no cost for unplayable cards like curses, or Reflex.

    //These will be used in the constructor. Technically you can just use the values directly,
    //but constants at the top of the file are easy to adjust.
    private static final int DAMAGE = 4;
    private static final int UPG_DAMAGE = 1;
    private static final int HIT_L = 3;
    private static final int HIT_R = 8;
    private AbstractMonster NowTargetMonster = null;
    private boolean isRight = false;
    private DoubleInOne fatherCard = null;
    private static final int AttackUpTurn = 1;
    private static final int AttackUpLayers = 1;
    private static final int TempStrength = 2;

    // 默认生成右边卡（可怜）
    public DoubleInOne() {
        this(true);
    }
    // 生成普通卡，有cardsToPreview，无fatherCard
    public DoubleInOne(boolean isRight) {
        this(isRight, false);
        // 最普通的卡才有cardsToPreview，防止循环调用
        this.cardsToPreview = new DoubleInOne(!isRight, true);
        // 用于保证不会被增加hit数
        this.tags.add(HbrTags.SELECT_CARD);
    }
    // 用来展示的卡
    public DoubleInOne(boolean isRight, boolean forDisplay) {
        // 卡图和hit数初始化在super中
        super(ID,info,getCardTextureString((isRight ? (removePrefix(ID) + "_R") : (removePrefix(ID) + "_L")), CardType.ATTACK),HBRAttackType.LQ,(isRight ? HIT_R : HIT_L));

        setDamage(DAMAGE, UPG_DAMAGE);

        // 记录是左边卡（可镰）还是右边卡（可怜）
        this.isRight = isRight;

        // 卡面描述初始化，有转换关键词
        switchDescription();
    }
    // 用来展示并被选择的卡
    public DoubleInOne(boolean isRight, DoubleInOne fatherCard) {
        // 卡图和hit数初始化在super中
        super(ID+(isRight ? "_R" : "_L"),info,getCardTextureString((isRight ? (removePrefix(ID) + "_R") : (removePrefix(ID) + "_L")), CardType.ATTACK),HBRAttackType.LQ,(isRight ? HIT_R : HIT_L));
        setDamage(DAMAGE, UPG_DAMAGE);
        // 记录是左边卡（可镰）还是右边卡（可怜）
        this.isRight = isRight;
        // 记录相应的father
        this.fatherCard = fatherCard;
    }

    @Override
    // 升级时同时升级preview的卡
    public void upgrade() {
        super.upgrade();
        if (this.cardsToPreview != null) {
            this.cardsToPreview.upgrade();
        }
    }

    // 用来在不同卡之间传递攻击目标
    public AbstractMonster getNowTargetMonster() {
        return NowTargetMonster;
    }

    public void setNowTargetMonster(AbstractMonster nowTargetMonster) {
        NowTargetMonster = nowTargetMonster;
    }

    @Override
    // 供正常卡调用
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (this.fatherCard==null) {
            NowTargetMonster = m;
            ArrayList<AbstractCard> stanceChoices = new ArrayList<>();
            AbstractCard DoubleInOne_L_Choice = new DoubleInOne(false, this);
            AbstractCard DoubleInOne_R_Choice = new DoubleInOne(true, this);
            stanceChoices.add(DoubleInOne_L_Choice);
            stanceChoices.add(DoubleInOne_R_Choice);
            for (AbstractCard c : stanceChoices) {
                if (this.upgraded) c.upgrade();
                c.applyPowers();
            }
            addToBot(new ChooseOneAction(stanceChoices));
        }
    }

    @Override
    // 供有fatherCard的choose卡调用
    public void onChoseThisOption() {
        DoubleInOne fatherCard = this.fatherCard;
        if (fatherCard!=null) {
            AbstractPlayer p = AbstractDungeon.player;
            p.getRelic(ODBar.ID).onUseCard(this, null);
            AbstractMonster m = fatherCard.getNowTargetMonster();
            fatherCard.setNowTargetMonster(null);
            if (m != null && AbstractDungeon.getMonsters().monsters.contains(m) && !m.isDeadOrEscaped()) {
                if (this.isRight) {
                    addToBot(new DamageAction(m, new DamageInfo(p, damage, DamageInfo.DamageType.NORMAL), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
                    addToBot(new DamageAction(m, new DamageInfo(p, damage, DamageInfo.DamageType.NORMAL), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
                    addToBot(new ApplyPowerAction(p, p, new StrengthPower(p, TempStrength), TempStrength));
                    addToBot(new ApplyPowerAction(p, p, new LoseStrengthPower(p, TempStrength), TempStrength));
                } else {
                    addToBot(new BlockRelatedDamageAction(m, new DamageInfo(p, damage, DamageInfo.DamageType.NORMAL), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL, this));
                    addToBot(new BlockRelatedDamageAction(m, new DamageInfo(p, damage, DamageInfo.DamageType.NORMAL), AbstractGameAction.AttackEffect.SLASH_DIAGONAL, this));
                    addToBot(new BlockRelatedDamageAction(m, new DamageInfo(p, damage, DamageInfo.DamageType.NORMAL), AbstractGameAction.AttackEffect.SLASH_VERTICAL, this));
                }
                this.fatherCard.switchAll(this.isRight);
            }
        }
    }

    @Override
    public void onBreakBlock() {
        AbstractPlayer p = AbstractDungeon.player;
        addToBot(new ApplyHBRStackPowerAction(p,p,new AttackUp(p, AttackUpTurn, AttackUpLayers)));
    }

    @Override
    public AbstractCard makeCopy() { //Optional
        // 默认是右边
        return new DoubleInOne();
    }

    public void switchAll(boolean isRight) {
        // 选择了不同的卡就switch
        if (this.isRight != isRight) {
            // 把本身的isRight和相应的图片、描述更改了
            this.isRight = isRight;
            switchDisplayCard();
            switchPortrait();
            switchDescription();
        }
    }

    // 更改卡图和hit数
    private void switchPortrait() {
        loadCardImage(getCardTextureString((isRight ? (removePrefix(ID) + "_R") : (removePrefix(ID) + "_L")), CardType.ATTACK));
        this.setCustomVar("hit",(isRight ? HIT_R : HIT_L));
    }

    // 更改展示卡
    private void switchDisplayCard() {
        this.cardsToPreview = new DoubleInOne(!isRight,true);
        if (this.upgraded) this.cardsToPreview.upgrade();
    }

    // 根据左右更改卡面描述,有切换关键词
    private void switchDescription() {
        if (this.isRight) {
            this.rawDescription = cardStrings.DESCRIPTION + cardStrings.EXTENDED_DESCRIPTION[1];
        } else {
            this.rawDescription = cardStrings.DESCRIPTION + cardStrings.EXTENDED_DESCRIPTION[0];
        }
        initializeDescription();
    }

    @Override
    public AbstractCard makeStatEquivalentCopy() {
        // 复制普通卡都是直接默认右边（可怜）
        if (this.fatherCard==null) return super.makeStatEquivalentCopy();
        // 复制选择卡（就是展示大图，应该也不太可能在其他地方复制选择卡了）
        else {
            DoubleInOne candidate = new DoubleInOne(this.isRight, this.fatherCard);
            if (this.upgraded) candidate.upgrade();
            return candidate;
        }
    }
}
