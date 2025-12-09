package heavenburnsred.cards.skill;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.watcher.ChooseOneAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import heavenburnsred.actions.ApplyHBRStackPowerAction;
import heavenburnsred.cards.BaseCard;
import heavenburnsred.cards.HbrTags;
import heavenburnsred.character.MyCharacter;
import heavenburnsred.powers.AttackUp;
import heavenburnsred.relics.Attribute;
import heavenburnsred.util.CardStats;

import java.util.ArrayList;

import static heavenburnsred.util.GeneralUtils.removePrefix;
import static heavenburnsred.util.TextureLoader.getCardTextureString;

public class ProIntelligencer extends BaseCard {
    public static final String ID = makeID(ProIntelligencer.class.getSimpleName()); //makeID adds the mod ID, so the final ID will be something like "modID:MyCard"
    public static final CardStats info = new CardStats(
            MyCharacter.Meta.CARD_COLOR, //The card color. If you're making your own character, it'll look something like this. Otherwise, it'll be CardColor.RED or similar for a basegame character color.
            CardType.SKILL, //The type. ATTACK/SKILL/POWER/CURSE/STATUS
            CardRarity.UNCOMMON, //Rarity. BASIC is for starting cards, then there's COMMON/UNCOMMON/RARE, and then SPECIAL and CURSE. SPECIAL is for cards you only get from events. Curse is for curses, except for special curses like Curse of the Bell and Necronomicurse.
            CardTarget.SELF, //The target. Single target is ENEMY, all enemies is ALL_ENEMY. Look at cards similar to what you want to see what to use.
            1);//The card's base cost. -1 is X cost, -2 is no cost for unplayable cards like curses, or Reflex.

    private boolean isRight = false;
    private ProIntelligencer fatherCard = null;

    private static final int BLOCK = 8;
    private static final int UPG_BLOCK = 2;

    private static final int MAGIC = 2;
    private static final int UPG_MAGIC = 1;

    private static final int DRAW_CARDS = 2;
    private static final int DRAW_PLUS_CARDS = 2;
    private static final int DOWN_ZY = -2;
    private static final int LIMIT_ZY = 20;

    // 默认生成左边卡（智障）
    public ProIntelligencer() {
        this(false);
    }
    // 生成普通卡，有cardsToPreview，无fatherCard
    public ProIntelligencer(boolean isRight) {
        this(isRight, false);
        // 最普通的卡才有cardsToPreview，防止循环调用
        this.cardsToPreview = new ProIntelligencer(!isRight, true);
        // 用于保证不会被增加hit数
        this.tags.add(HbrTags.SELECT_CARD);
    }
    // 用来展示的卡
    public ProIntelligencer(boolean isRight, boolean forDisplay) {
        // 卡图和hit数初始化在super中
        super(ID,info,getCardTextureString((isRight ? (removePrefix(ID) + "_R") : (removePrefix(ID) + "_L")), CardType.SKILL));

        setBlock(BLOCK, UPG_BLOCK);
        setMagic(MAGIC, UPG_MAGIC);

        // 记录是左边卡（可镰）还是右边卡（可怜）
        this.isRight = isRight;

        // 卡面描述初始化，有转换关键词
        switchDescription();
    }
    // 用来被选择的卡
    public ProIntelligencer(boolean isRight, ProIntelligencer fatherCard) {
        // 卡图初始化在super中
        super(ID+(isRight ? "_R" : "_L"),info,getCardTextureString((isRight ? (removePrefix(ID) + "_R") : (removePrefix(ID) + "_L")), CardType.SKILL));

        setBlock(BLOCK, UPG_BLOCK);
        setMagic(MAGIC, UPG_MAGIC);

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

    @Override
    // 供正常卡调用
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (this.fatherCard==null) {
            ArrayList<AbstractCard> stanceChoices = new ArrayList<>();
            AbstractCard ProIntelligencer_L_Choice = new ProIntelligencer(false, this);
            AbstractCard ProIntelligencer_R_Choice = new ProIntelligencer(true, this);
            stanceChoices.add(ProIntelligencer_L_Choice);
            stanceChoices.add(ProIntelligencer_R_Choice);
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
        ProIntelligencer fatherCard = this.fatherCard;
        if (fatherCard!=null) {
            AbstractPlayer p = AbstractDungeon.player;
            if (this.isRight) {
                Attribute.AddTempAttribute(0, 0, 0, magicNumber);
                AbstractRelic attribute = AbstractDungeon.player.getRelic(Attribute.ID);
                int draw_card = (((Attribute)attribute).getCurZY() <= LIMIT_ZY) ? DRAW_CARDS : DRAW_CARDS+DRAW_PLUS_CARDS;
                // 防止抽自己需要addToTop
                addToTop(new DrawCardAction(draw_card));
            } else {
                Attribute.AddTempAttribute(0, 0, 0, DOWN_ZY);
                addToBot(new GainBlockAction(p, p, this.block));
                addToBot(new ApplyHBRStackPowerAction(p,p,new AttackUp(p, 1, 1)));
            }
            this.fatherCard.switchAll(this.isRight);
        }
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

    // 更改卡图
    private void switchPortrait() {
        loadCardImage(getCardTextureString((isRight ? (removePrefix(ID) + "_R") : (removePrefix(ID) + "_L")), CardType.SKILL));
    }

    // 更改展示卡
    private void switchDisplayCard() {
        this.cardsToPreview = new ProIntelligencer(!isRight,true);
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
        // 复制普通卡都是直接默认左边（智障）
        if (this.fatherCard==null) return super.makeStatEquivalentCopy();
        // 复制选择卡（就是展示大图，应该也不太可能在其他地方复制选择卡了）
        else {
            ProIntelligencer candidate = new ProIntelligencer(this.isRight, this.fatherCard);
            if (this.upgraded) candidate.upgrade();
            return candidate;
        }
    }
}
