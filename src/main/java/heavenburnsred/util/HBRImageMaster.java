package heavenburnsred.util;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.helpers.ImageMaster;


import static heavenburnsred.BasicMod.imagePath;

public class HBRImageMaster {
    public static TextureAtlas.AtlasRegion HBR_CARD_FRAME_ATTACK_COMMON = getCardUIRegion("cardui/hbr_frame_attack_common", 0, 55.5f);
    public static TextureAtlas.AtlasRegion HBR_CARD_FRAME_ATTACK_UNCOMMON = getCardUIRegion("cardui/hbr_frame_attack_uncommon", 0, 55.5f);
    public static TextureAtlas.AtlasRegion HBR_CARD_FRAME_ATTACK_RARE = getCardUIRegion("cardui/hbr_frame_attack_rare", 0, 55.5f);
    public static TextureAtlas.AtlasRegion HBR_CARD_FRAME_ATTACK_COMMON_L = getCardUIRegion("cardui/hbr_frame_attack_common_L", 2.5f, 114.5f);
    public static TextureAtlas.AtlasRegion HBR_CARD_FRAME_ATTACK_UNCOMMON_L = getCardUIRegion("cardui/hbr_frame_attack_uncommon_L", 2.5f, 114.5f);
    public static TextureAtlas.AtlasRegion HBR_CARD_FRAME_ATTACK_RARE_L = getCardUIRegion("cardui/hbr_frame_attack_rare_L", 2.5f, 114.5f);

    public static TextureAtlas.AtlasRegion RICE_FIELD_0 = getCardUIRegion("animation/riceField/riceParticle_0", 0, 0);
    public static TextureAtlas.AtlasRegion RICE_FIELD_1 = getCardUIRegion("animation/riceField/riceParticle_1", 0, 0);
    public static TextureAtlas.AtlasRegion RICE_FIELD_2 = getCardUIRegion("animation/riceField/riceParticle_2", 0, 0);
    public static TextureAtlas.AtlasRegion RICE_FIELD_3 = getCardUIRegion("animation/riceField/riceParticle_3", 0, 0);
    public static TextureAtlas.AtlasRegion RICE_FIELD_4 = getCardUIRegion("animation/riceField/riceParticle_4", 0, 0);
    public static TextureAtlas.AtlasRegion RICE_FIELD_5 = getCardUIRegion("animation/riceField/riceParticle_5", 0, 0);
    public static TextureAtlas.AtlasRegion RICE_FIELD_6 = getCardUIRegion("animation/riceField/riceParticle_6", 0, 0);

    public static final Color RED_BORDER_COLOR = new Color(1.0F, 0.1F, 0.1F, 0.25F);

    public static TextureAtlas.AtlasRegion getCardUIRegion(final String Name, float offsetX, float offsetY) {
        String textureString = imagePath(Name + ".png");
        Texture cardUITexture;
        cardUITexture = ImageMaster.loadImage(textureString);

        // 我们是沿着边剪切下来
        int tw = cardUITexture.getWidth();
        int th = cardUITexture.getHeight();
        TextureAtlas.AtlasRegion region = new TextureAtlas.AtlasRegion(cardUITexture, 0, 0, tw, th);

        // 仿照原atlas设置，实际画图似乎不需要用到name和index
        region.name = Name;
        region.originalWidth = tw;
        region.originalHeight = th;
        region.index = -1;

        // 图片的偏置，用来游戏内渲染画图，计算公式为原atlas中offset-(orig-size)/2，根据AbstractCard的renderHelper函数推导
        region.offsetX = offsetX;
        region.offsetY = offsetY;

        return region;
    }
}
