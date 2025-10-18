package heavenburnsred.patches;


import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import heavenburnsred.cards.attack.ElegantSerious;
import heavenburnsred.cards.attack.HBRHitAndTypeAttackCard;
import heavenburnsred.util.HBRImageMaster;

import java.util.Objects;


public class HBRCardUIPatch {

    static ReflectionHacks.RMethod renderHelper = ReflectionHacks.privateMethod(AbstractCard.class, "renderHelper", SpriteBatch.class, Color.class, TextureAtlas.AtlasRegion.class, float.class, float.class);

    // 替换攻击卡的边框，只替换小卡，右键单卡放大需要patch popup类
    @SpirePatch(clz = AbstractCard.class, method = "renderAttackPortrait")
    public static class AttackFrameSmallSubstitute {
        @SpirePrefixPatch
        public static SpireReturn<Void> aheadReturn(AbstractCard _inst, SpriteBatch sb, float x, float y) {
            if (_inst instanceof HBRHitAndTypeAttackCard) {
                Color renderColor = ReflectionHacks.getPrivate(_inst, AbstractCard.class, "renderColor");
                // 根据稀有度替换边框
                switch (_inst.rarity) {
                    case SPECIAL:  // 给优雅整肃金色的！
                        if (Objects.equals(_inst.cardID, ElegantSerious.ID)) {
                            renderHelper.invoke(_inst, sb, renderColor, HBRImageMaster.HBR_CARD_FRAME_ATTACK_RARE, x, y);
                            break;
                        }
                    case BASIC:
                    case CURSE:
                    case COMMON:
                        renderHelper.invoke(_inst, sb, renderColor, HBRImageMaster.HBR_CARD_FRAME_ATTACK_COMMON, x, y);
                        break;
                    case UNCOMMON:
                        renderHelper.invoke(_inst, sb, renderColor, HBRImageMaster.HBR_CARD_FRAME_ATTACK_UNCOMMON, x, y);
                        break;
                    case RARE:
                        renderHelper.invoke(_inst, sb, renderColor, HBRImageMaster.HBR_CARD_FRAME_ATTACK_RARE, x, y);
                        break;
                }
                return SpireReturn.Return();
            } else {  // 其他类型的卡继续原函数，使用正常边框
                return SpireReturn.Continue();
            }
        }
    }

    // 渲染攻击类型的文字和5种不同类型的攻击图标
    @SpirePatch(clz = AbstractCard.class, method = "renderType")
    public static class AttackTypeSubstitute {
        @SpireInsertPatch(rloc = 2665 - 2640, localvars = {"text", "font"})
        public static SpireReturn<Void> aheadReturn(AbstractCard _inst, SpriteBatch sb, String text, BitmapFont font) {
            if (_inst instanceof HBRHitAndTypeAttackCard) {
                // 将文字渲染位置向右移动
                // 这个renderRotatedText函数主要调offset即可，代表先偏移后旋转，可以维持和卡牌总体的相对静止，直接调x和y会先旋转再偏移，是错的
                Color typeColor = ReflectionHacks.getPrivate(_inst, AbstractCard.class, "typeColor");
                FontHelper.renderRotatedText(sb, font, text, _inst.current_x, _inst.current_y - 22.0F * _inst.drawScale * Settings.scale, 10.0F * _inst.drawScale * Settings.scale, -1.0F * _inst.drawScale * Settings.scale, _inst.angle, false, typeColor);
                // 根据攻击类型渲染图标
                switch (((HBRHitAndTypeAttackCard)_inst).attackPreference) {
                    case WP:  // 先用文字代替
                        FontHelper.renderRotatedText(sb, font, "均", _inst.current_x, _inst.current_y - 22.0F * _inst.drawScale * Settings.scale, -20.0F * _inst.drawScale * Settings.scale, -1.0F * _inst.drawScale * Settings.scale, _inst.angle, false, typeColor);
                        break;
                    case LL:  // 先用文字代替
                        FontHelper.renderRotatedText(sb, font, "力", _inst.current_x, _inst.current_y - 22.0F * _inst.drawScale * Settings.scale, -20.0F * _inst.drawScale * Settings.scale, -1.0F * _inst.drawScale * Settings.scale, _inst.angle, false, typeColor);
                        break;
                    case LQ:  // 先用文字代替
                        FontHelper.renderRotatedText(sb, font, "灵", _inst.current_x, _inst.current_y - 22.0F * _inst.drawScale * Settings.scale, -20.0F * _inst.drawScale * Settings.scale, -1.0F * _inst.drawScale * Settings.scale, _inst.angle, false, typeColor);
                        break;
                    case TJ:  // 先用文字代替
                        FontHelper.renderRotatedText(sb, font, "体", _inst.current_x, _inst.current_y - 22.0F * _inst.drawScale * Settings.scale, -20.0F * _inst.drawScale * Settings.scale, -1.0F * _inst.drawScale * Settings.scale, _inst.angle, false, typeColor);
                        break;
                    case ZY:  // 先用文字代替
                        FontHelper.renderRotatedText(sb, font, "智", _inst.current_x, _inst.current_y - 22.0F * _inst.drawScale * Settings.scale, -20.0F * _inst.drawScale * Settings.scale, -1.0F * _inst.drawScale * Settings.scale, _inst.angle, false, typeColor);
                        break;
                    default:  // 不可能运行到这的，运行到这肯定哪里赋值type出问题了
                        FontHelper.renderRotatedText(sb, font, "?", _inst.current_x, _inst.current_y - 22.0F * _inst.drawScale * Settings.scale, -20.0F * _inst.drawScale * Settings.scale, -1.0F * _inst.drawScale * Settings.scale, _inst.angle, false, typeColor);
                        break;
                }
                return SpireReturn.Return();
            } else {  // 其他类型的卡继续原函数，正常文字
                return SpireReturn.Continue();
            }
        }
    }

    // 渲染library内右上角的HIT数和图标（直接调用HBRHitAndTypeAttackCard的renderHit即可）
    @SpirePatch(clz = AbstractCard.class, method = "renderInLibrary")
    public static class renderHitForRenderInLibrary {
        @SpireInsertPatch(rloc = 1258 - 1232)
        public static void InsertRender(AbstractCard _inst, SpriteBatch sb) {
            if (_inst instanceof HBRHitAndTypeAttackCard) {
                ((HBRHitAndTypeAttackCard)_inst).renderHit(sb);
            }
        }
    }

    // 渲染卡牌右上角的HIT数和图标（直接调用HBRHitAndTypeAttackCard的renderHit即可）
    @SpirePatch(clz = AbstractCard.class, method = "renderCard")
    public static class renderHitForRenderCard {
        @SpireInsertPatch(rloc = 1307 - 1291)
        public static void InsertRender(AbstractCard _inst, SpriteBatch sb, boolean hovered, boolean selected) {
            if (_inst instanceof HBRHitAndTypeAttackCard) {
                ((HBRHitAndTypeAttackCard)_inst).renderHit(sb);
            }
        }
    }

    // 替换优雅整肃卡面上方的banner为金色
    @SpirePatch(clz = AbstractCard.class, method = "renderBannerImage")
    public static class renderBannerImageSubstitute {
        @SpirePrefixPatch
        public static SpireReturn<Void> aheadReturn(AbstractCard _inst, SpriteBatch sb, float x, float y) {
            if (Objects.equals(_inst.cardID, ElegantSerious.ID)) {
                Color renderColor = ReflectionHacks.getPrivate(_inst, AbstractCard.class, "renderColor");
                renderHelper.invoke(_inst, sb, renderColor, ImageMaster.CARD_BANNER_RARE, x, y);
                return SpireReturn.Return();
            } else {  // 其他类型的卡继续原函数，使用正常边框
                return SpireReturn.Continue();
            }
        }
    }
}
