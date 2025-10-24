package heavenburnsred.cards.attack;

import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import heavenburnsred.cards.BaseCard;
import heavenburnsred.cards.HbrTags;
import heavenburnsred.util.CardStats;


public abstract class HBRHitAndTypeAttackCard extends BaseCard {

    private final Color ENERGY_COST_MODIFIED_COLOR = ReflectionHacks.getPrivate(this, AbstractCard.class, "ENERGY_COST_MODIFIED_COLOR");
    private final ReflectionHacks.RMethod renderHelper = ReflectionHacks.privateMethod(AbstractCard.class, "renderHelper", SpriteBatch.class, Color.class, TextureAtlas.AtlasRegion.class, float.class, float.class);
    private final ReflectionHacks.RMethod getHitFont = ReflectionHacks.privateMethod(AbstractCard.class, "getEnergyFont");

    public enum HBRAttackType{
        LL,
        LQ,
        TJ,
        ZY,
        WP
    }

    public HBRAttackType attackPreference;

    // 不写偏好就默认无偏，不写hit数就hit数为1
    public HBRHitAndTypeAttackCard(String ID, CardStats info) {
        this(ID, info, HBRAttackType.WP, 1, 0);
    }
    public HBRHitAndTypeAttackCard(String ID, CardStats info, int hit) {
        this(ID, info, HBRAttackType.WP, hit, 0);
    }
    public HBRHitAndTypeAttackCard(String ID, CardStats info, int hit, int upgradeHit) {
        this(ID, info, HBRAttackType.WP, hit, upgradeHit);
    }
    public HBRHitAndTypeAttackCard(String ID, CardStats info, HBRAttackType attackPreference) {
        this(ID, info, attackPreference, 1, 0);
    }
    public HBRHitAndTypeAttackCard(String ID, CardStats info, HBRAttackType attackPreference, int hit) {
        this(ID, info, attackPreference, hit, 0);
    }
    public HBRHitAndTypeAttackCard(String ID, CardStats info, HBRAttackType attackPreference, int hit, int upgradeHit) {
        super(ID, info);
        this.attackPreference = attackPreference;
        setCustomVar("hit", hit, upgradeHit);
        tags.add(HbrTags.HIT);
    }
    // 如果想特殊指定图片可以用这两个构造方法，但是估计不会用到的
    public HBRHitAndTypeAttackCard(String ID, CardStats info, String cardImage, HBRAttackType attackPreference, int hit) {
        this(ID, info, cardImage, attackPreference, hit, 0);
    }
    public HBRHitAndTypeAttackCard(String ID, CardStats info, String cardImage, HBRAttackType attackPreference, int hit, int upgradeHit) {
        super(ID, info, cardImage);
        this.attackPreference = attackPreference;
        setCustomVar("hit", hit, upgradeHit);
        tags.add(HbrTags.HIT);
    }

    // render牌右上角的hit数，目前没有图片，先用文字代替
    public void renderHit(SpriteBatch sb, boolean isPopup) {
        if (this.isLocked || !this.isSeen)
            return;
        // 小卡图
        if (!isPopup) {
            Color hitTextColor = Color.WHITE.cpy();
            if (isCustomVarModified("hit")) {
                hitTextColor = ENERGY_COST_MODIFIED_COLOR;
            }
            hitTextColor.a = this.transparency;
            String text = "Hit: " + customVar("hit");
            BitmapFont font = getHitFont.invoke(this);
            // 这个renderRotatedText函数主要调offset即可，代表先偏移后旋转，可以维持和卡牌总体的相对静止，直接调x和y会先旋转再偏移，是错的
            // + IMG_WIDTH * this.drawScale * 0.75f这一项是我在能量text位置基础上加入的新偏移，向右移动一定的距离到右上角，0.75f可以修改
            FontHelper.renderRotatedText(sb, font, text, this.current_x, this.current_y, -132.0F * this.drawScale * Settings.scale + IMG_WIDTH * this.drawScale * 0.75f, 192.0F * this.drawScale * Settings.scale, this.angle, false, hitTextColor);
        } else {
            // 大图的颜色和位置略有不同
            Color c = null;
            if (isCustomVarModified("hit")) {
                c = Settings.GREEN_TEXT_COLOR;
            } else {
                c = Settings.CREAM_COLOR;
            }
            String text = "Hit: " + customVar("hit");
            // 这里调x即可
            FontHelper.renderFont(sb, FontHelper.SCP_cardEnergyFont, text, Settings.WIDTH / 2.0F + 70.0F * Settings.scale, Settings.HEIGHT / 2.0F + 404.0F * Settings.scale, c);
        }
    }

    public void onBreakBlock() {}
}
