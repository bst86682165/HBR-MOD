package heavenburnsred.effects.stances;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import heavenburnsred.util.HBRImageMaster;


public class RiceFieldParticleEffect extends AbstractGameEffect {
    private float x;
    private float y;
    private float vY;
    private float dur_div2;
    private TextureAtlas.AtlasRegion img;
    private boolean flipped;


    public RiceFieldParticleEffect() {
        this.scale = Settings.scale;
        // 起始稻苗
        this.img = HBRImageMaster.RICE_FIELD_0;
        this.scale = MathUtils.random(0.7F, 1.3F);
        this.startingDuration = this.scale + 0.8F;
        this.duration = this.startingDuration;
        this.scale *= Settings.scale;
        // 后1/3时间开始渐隐
        this.dur_div2 = this.duration * 2 / 3.0F;
        this.color = new Color(MathUtils.random(0.65F, 0.85F), MathUtils.random(0.55F, 0.75F), MathUtils.random(0.15F, 0.35F), 0.0F);
        this.x = AbstractDungeon.player.hb.cX + MathUtils.random(-AbstractDungeon.player.hb.width / 2.0F - 50.0F * Settings.scale, AbstractDungeon.player.hb.width / 2.0F + 50.0F * Settings.scale);
        // 在脚下渲染
        this.y = AbstractDungeon.player.hb.cY + MathUtils.random(-AbstractDungeon.player.hb.height / 2.0F, - AbstractDungeon.player.hb.height / 2.0F + 50.0F * Settings.scale);

        this.renderBehind = MathUtils.randomBoolean(0.8f);
        this.flipped = MathUtils.randomBoolean();

        this.x -= (float)this.img.packedWidth / 2.0F;

    }

    public void update() {
        // 后1/3时间fade out
        if (this.duration > this.dur_div2) {
            this.color.a = Interpolation.fade.apply(1.0F, 0.0F, (this.duration - this.dur_div2) / (1 - this.dur_div2));
        } else {
            this.color.a = Interpolation.fade.apply(0.0F, 1.0F, this.duration / this.dur_div2);
        }

        if (this.duration > this.startingDuration * 0.95F) {
            this.img = HBRImageMaster.RICE_FIELD_0;
        } else if (this.duration > this.startingDuration * 0.88F) {
            this.img = HBRImageMaster.RICE_FIELD_1;
        } else if (this.duration > this.startingDuration * 0.8F) {
            this.img = HBRImageMaster.RICE_FIELD_2;
        } else if (this.duration > this.startingDuration * 0.75F) {
            this.img = HBRImageMaster.RICE_FIELD_3;
        } else if (this.duration > this.startingDuration * 0.7F) {
            this.img = HBRImageMaster.RICE_FIELD_4;
        } else if (this.duration > this.startingDuration * 0.65F) {
            this.img = HBRImageMaster.RICE_FIELD_5;
        } else {
            this.img = HBRImageMaster.RICE_FIELD_6;
        }

        this.duration -= Gdx.graphics.getDeltaTime();
        if (this.duration < 0.0F) {
            this.isDone = true;
        }
    }

    public void render(SpriteBatch sb) {
        sb.setColor(this.color);
        sb.setBlendFunction(770, 1);

        float drawX = this.scale;
        if (this.flipped) drawX = -drawX;

        sb.draw(this.img, this.x, this.y, (float)this.img.packedWidth / 2.0F, (float)this.img.packedHeight / 2.0F, (float)this.img.packedWidth, (float)this.img.packedHeight, drawX, this.scale*1.2f, this.rotation);
        sb.setBlendFunction(770, 771);
    }

    public void dispose() {
    }
}
