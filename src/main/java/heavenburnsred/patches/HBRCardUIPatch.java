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
import com.megacrit.cardcrawl.screens.SingleCardViewPopup;
import heavenburnsred.cards.attack.ElegantSerious;
import heavenburnsred.cards.attack.HBRHitAndTypeAttackCard;
import heavenburnsred.util.HBRImageMaster;

import java.sql.Ref;
import java.util.Objects;


public class HBRCardUIPatch {

    static ReflectionHacks.RMethod renderHelperAbstractCard = ReflectionHacks.privateMethod(AbstractCard.class, "renderHelper", SpriteBatch.class, Color.class, TextureAtlas.AtlasRegion.class, float.class, float.class);
    static ReflectionHacks.RMethod renderHelperPopup = ReflectionHacks.privateMethod(SingleCardViewPopup.class, "renderHelper", SpriteBatch.class, float.class, float.class, TextureAtlas.AtlasRegion.class);
    static ReflectionHacks.RMethod renderDynamicFramePopup = ReflectionHacks.privateMethod(SingleCardViewPopup.class, "renderDynamicFrame", SpriteBatch.class, float.class, float.class, float.class, float.class);

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
                            renderHelperAbstractCard.invoke(_inst, sb, renderColor, HBRImageMaster.HBR_CARD_FRAME_ATTACK_RARE, x, y);
                            break;
                        }
                    case BASIC:
                    case CURSE:
                    case COMMON:
                        renderHelperAbstractCard.invoke(_inst, sb, renderColor, HBRImageMaster.HBR_CARD_FRAME_ATTACK_COMMON, x, y);
                        break;
                    case UNCOMMON:
                        renderHelperAbstractCard.invoke(_inst, sb, renderColor, HBRImageMaster.HBR_CARD_FRAME_ATTACK_UNCOMMON, x, y);
                        break;
                    case RARE:
                        renderHelperAbstractCard.invoke(_inst, sb, renderColor, HBRImageMaster.HBR_CARD_FRAME_ATTACK_RARE, x, y);
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
                ((HBRHitAndTypeAttackCard)_inst).renderHit(sb,false);
            }
        }
    }

    // 渲染卡牌右上角的HIT数和图标（直接调用HBRHitAndTypeAttackCard的renderHit即可）
    @SpirePatch(clz = AbstractCard.class, method = "renderCard")
    public static class renderHitForRenderCard {
        @SpireInsertPatch(rloc = 1307 - 1291)
        public static void InsertRender(AbstractCard _inst, SpriteBatch sb, boolean hovered, boolean selected) {
            if (_inst instanceof HBRHitAndTypeAttackCard) {
                ((HBRHitAndTypeAttackCard)_inst).renderHit(sb,false);
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
                renderHelperAbstractCard.invoke(_inst, sb, renderColor, ImageMaster.CARD_BANNER_RARE, x, y);
                return SpireReturn.Return();
            } else {  // 其他类型的卡继续原函数，使用正常边框
                return SpireReturn.Continue();
            }
        }
    }

    // 渲染popup大图卡牌右上角的HIT数和图标（直接调用HBRHitAndTypeAttackCard的renderHit即可）
    @SpirePatch(clz = SingleCardViewPopup.class, method = "render")
    public static class renderHitForPopup {
        @SpireInsertPatch(rloc = 360 - 338)
        public static void InsertRender(SingleCardViewPopup _inst, SpriteBatch sb) {
            AbstractCard card = ReflectionHacks.getPrivate(_inst, SingleCardViewPopup.class,"card");
            if (card instanceof HBRHitAndTypeAttackCard) {
                ((HBRHitAndTypeAttackCard)card).renderHit(sb,true);
            }
        }
    }

    // 替换popup中攻击卡的边框
    @SpirePatch(clz = SingleCardViewPopup.class, method = "renderFrame")
    public static class AttackFramePopupSubstitute {
        @SpirePrefixPatch
        public static SpireReturn<Void> aheadReturn(SingleCardViewPopup _inst, SpriteBatch sb) {
            AbstractCard card = ReflectionHacks.getPrivate(_inst, SingleCardViewPopup.class,"card");
            if (card instanceof HBRHitAndTypeAttackCard) {
                TextureAtlas.AtlasRegion tmpImg = null;
                switch (card.rarity) {
                    case SPECIAL:  // 给优雅整肃金色的！
                        if (Objects.equals(card.cardID, ElegantSerious.ID)) {
                            tmpImg = HBRImageMaster.HBR_CARD_FRAME_ATTACK_RARE_L;
                            break;
                        }
                    case BASIC:
                    case CURSE:
                    case COMMON:
                        tmpImg = HBRImageMaster.HBR_CARD_FRAME_ATTACK_COMMON_L;
                        break;
                    case UNCOMMON:
                        tmpImg = HBRImageMaster.HBR_CARD_FRAME_ATTACK_UNCOMMON_L;
                        break;
                    case RARE:
                        tmpImg = HBRImageMaster.HBR_CARD_FRAME_ATTACK_RARE_L;
                        break;
                }
                if (tmpImg != null) {
                    renderHelperPopup.invoke(_inst, sb, Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F, tmpImg);
                }
                float tWidth = AbstractCard.typeWidthAttack;
                float tOffset = AbstractCard.typeOffsetAttack;
                // 这玩意干啥的我不知道，原作的卡图框有什么动态变化么？
                renderDynamicFramePopup.invoke(_inst, sb, Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F, tOffset, tWidth);
                return SpireReturn.Return();
            }
            // 非HBR的攻击卡不替换边框
            else {
                return SpireReturn.Continue();
            }
        }
    }

    // 渲染攻击类型的文字和5种不同类型的攻击图标
    @SpirePatch(clz = SingleCardViewPopup.class, method = "renderCardTypeText")
    public static class AttackTypePopupSubstitute {
        @SpirePrefixPatch
        public static SpireReturn<Void> aheadReturn(SingleCardViewPopup _inst, SpriteBatch sb) {
            AbstractCard card = ReflectionHacks.getPrivate(_inst, SingleCardViewPopup.class,"card");
            if (card instanceof HBRHitAndTypeAttackCard) {
                // 将文字渲染位置 3.0F * Settings.scale 向右移动 + 23.0F * Settings.scale
                Color CARD_TYPE_COLOR = ReflectionHacks.getPrivateStatic(SingleCardViewPopup.class, "CARD_TYPE_COLOR");
                FontHelper.renderFontCentered(sb, FontHelper.panelNameFont, "攻击", Settings.WIDTH / 2.0F + 3.0F * Settings.scale + 23.0F * Settings.scale, Settings.HEIGHT / 2.0F - 40.0F * Settings.scale, CARD_TYPE_COLOR);
                // 根据攻击类型渲染图标
                float positionX = Settings.WIDTH / 2.0F + 3.0F * Settings.scale - 38.0F * Settings.scale;
                float positionY = Settings.HEIGHT / 2.0F - 40.0F * Settings.scale;
                switch (((HBRHitAndTypeAttackCard)card).attackPreference) {
                    case WP:  // 先用文字代替
                        FontHelper.renderFontCentered(sb, FontHelper.panelNameFont, "均", positionX, positionY, CARD_TYPE_COLOR);
                        break;
                    case LL:  // 先用文字代替
                        FontHelper.renderFontCentered(sb, FontHelper.panelNameFont, "力", positionX, positionY, CARD_TYPE_COLOR);
                        break;
                    case LQ:  // 先用文字代替
                        FontHelper.renderFontCentered(sb, FontHelper.panelNameFont, "灵", positionX, positionY, CARD_TYPE_COLOR);
                        break;
                    case TJ:  // 先用文字代替
                        FontHelper.renderFontCentered(sb, FontHelper.panelNameFont, "体", positionX, positionY, CARD_TYPE_COLOR);
                        break;
                    case ZY:  // 先用文字代替
                        FontHelper.renderFontCentered(sb, FontHelper.panelNameFont, "智", positionX, positionY, CARD_TYPE_COLOR);
                        break;
                    default:  // 不可能运行到这的，运行到这肯定哪里赋值type出问题了
                        FontHelper.renderFontCentered(sb, FontHelper.panelNameFont, "?", positionX, positionY, CARD_TYPE_COLOR);
                        break;
                }
                return SpireReturn.Return();
            } else {  // 其他类型的卡继续原函数，正常文字
                return SpireReturn.Continue();
            }
        }
    }

    // 替换优雅整肃popup卡面上方的banner为金色
    @SpirePatch(clz = SingleCardViewPopup.class, method = "renderCardBanner")
    public static class renderBannerPopupSubstitute {
        @SpirePrefixPatch
        public static SpireReturn<Void> aheadReturn(SingleCardViewPopup _inst, SpriteBatch sb) {
            AbstractCard card = ReflectionHacks.getPrivate(_inst, SingleCardViewPopup.class,"card");
            if (Objects.equals(card.cardID, ElegantSerious.ID)) {
                renderHelperPopup.invoke(_inst, sb, Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F, ImageMaster.CARD_BANNER_RARE_L);
                return SpireReturn.Return();
            } else {  // 其他类型的卡继续原函数，使用正常边框
                return SpireReturn.Continue();
            }
        }
    }
}
