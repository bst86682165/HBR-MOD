package heavenburnsred.util;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.helpers.ImageMaster;


import static heavenburnsred.BasicMod.imagePath;

public class HBRImageMaster {
    public static TextureAtlas.AtlasRegion HBR_CARD_FRAME_ATTACK_COMMON = getCardUIRegion("hbr_frame_attack_common", 0, 55.5f);
    public static TextureAtlas.AtlasRegion HBR_CARD_FRAME_ATTACK_UNCOMMON = getCardUIRegion("hbr_frame_attack_uncommon", 0, 55.5f);
    public static TextureAtlas.AtlasRegion HBR_CARD_FRAME_ATTACK_RARE = getCardUIRegion("hbr_frame_attack_rare", 0, 55.5f);

    public static TextureAtlas.AtlasRegion getCardUIRegion(final String Name, float offsetX, float offsetY) {
        String textureString = imagePath("cardui/" + Name + ".png");
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
