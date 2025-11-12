package heavenburnsred.relics;

import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DiscardAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import heavenburnsred.actions.ApplyNotStackingPowerAction;
import heavenburnsred.actions.ChangeODBarHitAction;
import heavenburnsred.cards.HbrTags;
import heavenburnsred.cards.attack.DoubleInOne;
import heavenburnsred.patches.HBRRelicClick;
import heavenburnsred.cards.BaseCard;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.core.Settings;
import com.badlogic.gdx.graphics.Color;
import heavenburnsred.powers.ChargePower;
import heavenburnsred.powers.OverDriveState;
import heavenburnsred.util.TextureLoader;

import java.util.Objects;

import static heavenburnsred.BasicMod.makeID;
import static heavenburnsred.BasicMod.relicPath;

public class ODBar extends HBRRelicClick {
    private static final String NAME = "ODBar"; // The name will be used for determining the image file as well as the
                                                // ID.
    public static final String ID = makeID(NAME); // This adds the mod's prefix to the relic ID, resulting in
                                                  // modID:MyRelic
    private static final AbstractRelic.RelicTier RARITY = AbstractRelic.RelicTier.STARTER; // The relic's rarity.
    private static final AbstractRelic.LandingSound SOUND = AbstractRelic.LandingSound.CLINK; // The sound played when
                                                                                              // the relic is clicked.
    public final int hit_limit;
    private static final int HIT_LIMIT = 120;

    public ODBar() {
        super(ID, NAME, RARITY, SOUND);
        this.counter = 0;
        this.hit_limit = HIT_LIMIT;
        loadOtherTexture();
    }

    @Override
    public void setCounter(int counter) {
        int old_counter = this.counter;
        super.setCounter(counter);
        onCounterChanged(old_counter, counter);
    }

    // 把3个其他状态都加载进游戏
    private void loadOtherTexture() {
        this.img = TextureLoader.getTextureNull(relicPath("ODBar_1" + ".png"), true);
        this.img = TextureLoader.getTextureNull(relicPath("ODBar_2" + ".png"), true);
        this.img = TextureLoader.getTextureNull(relicPath("ODBar_3" + ".png"), true);
        this.img = TextureLoader.getTextureNull(relicPath(NAME + ".png"), true);
    }

    // 返回应该显示的od条数值
    private int counterRank(int counter) {
        if (counter < 40) {
            return 0;
        } else if (counter < 80) {
            return 1;
        } else if (counter < 120) {
            return 2;
        } else {
            return 3;
        }
    }

    // 在任何可能改变od条显示图片的时候更改图片并加入flash动画效果
    public void onCounterChanged(int old_counter, int new_counter) {
        if (counterRank(old_counter) != counterRank(new_counter)) {
            // 切换不同图片
            switch (counterRank(new_counter)) {
                case 0:
                    this.img = TextureLoader.getTextureNull(relicPath(NAME + ".png"), true);
                    break;
                case 1:
                    this.img = TextureLoader.getTextureNull(relicPath("ODBar_1" + ".png"), true);
                    break;
                case 2:
                    this.img = TextureLoader.getTextureNull(relicPath("ODBar_2" + ".png"), true);
                    break;
                case 3:
                    this.img = TextureLoader.getTextureNull(relicPath("ODBar_3" + ".png"), true);
                    break;
            }
            // 动画效果
            this.flash();
        }
    }

    public void onUseCard(AbstractCard card, UseCardAction action) {
        addToBot(new ChangeODBarHitAction(card));
    }

    public void onRightClick() {
        if (AbstractDungeon.screen != AbstractDungeon.CurrentScreen.NONE) return;
        AbstractPlayer p = AbstractDungeon.player;
        if (!this.usedUp && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT
                && this.counter >= 40) {
            addToBot(new ApplyNotStackingPowerAction(p,p,new OverDriveState(p,-1)));
            this.counter = this.counter - 40;
            onCounterChanged(this.counter + 40, this.counter);
            int handCards = AbstractDungeon.player.hand.size();
            addToBot(new DiscardAction(p, p, handCards, true));
            addToBot(new DrawCardAction(p, 5));
            addToBot(new GainEnergyAction(3));
        }
    }

    @Override
    public void renderCounter(SpriteBatch sb, boolean inTopPanel) {
        Color c;
        if (this.counter >= 0) {
            c = Color.WHITE;
        } else {
            c = Color.RED;
        }
        if (inTopPanel) {
            float offsetX = (float) ReflectionHacks.getPrivate(this, AbstractRelic.class, "offsetX");
            FontHelper.renderFontRightTopAligned(sb, FontHelper.topPanelInfoFont,
                    Integer.toString(this.counter), offsetX + this.currentX + 30.0F * Settings.scale, this.currentY - 7.0F * Settings.scale, c);
        } else {
            FontHelper.renderFontRightTopAligned(sb, FontHelper.topPanelInfoFont,
                    Integer.toString(this.counter), this.currentX + 30.0F * Settings.scale, this.currentY - 7.0F * Settings.scale, c);
        }
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    public static int getCounter() {
        AbstractRelic relic = AbstractDungeon.player.getRelic(ID);
        if (relic instanceof ODBar) {
            return relic.counter;
        }
        return 0; // 没有遗物时默认 0 当然理论上不可能没有该遗物
    }
}
