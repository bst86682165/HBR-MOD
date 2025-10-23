package heavenburnsred.stances;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.StanceStrings;
import com.megacrit.cardcrawl.stances.AbstractStance;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;
import com.megacrit.cardcrawl.vfx.stance.DivinityParticleEffect;
import com.megacrit.cardcrawl.vfx.stance.DivinityStanceChangeParticle;
import com.megacrit.cardcrawl.vfx.stance.StanceAuraEffect;

import static heavenburnsred.BasicMod.makeID;


public class RiceField extends AbstractStance {

    public static final String STANCE_ID = makeID(AbstractStance.class.getSimpleName());

    private static final StanceStrings stanceString = new StanceStrings() {
        {NAME = "稻香领域";
        DESCRIPTION = new String[]{"你造成的攻击伤害提升 #b40 %"};}
    };

    private static long sfxId = -1L;

    public RiceField() {
        this.ID = STANCE_ID;
        this.name = stanceString.NAME;
        updateDescription();
    }

    public float atDamageGive(float damage, DamageInfo.DamageType type) {
        if (type == DamageInfo.DamageType.NORMAL)
            return damage * 1.4F;
        return damage;
    }

    // 动画效果
    public void updateAnimation() {
        if (!Settings.DISABLE_EFFECTS) {
            this.particleTimer -= Gdx.graphics.getDeltaTime();
            if (this.particleTimer < 0.0F) {
                this.particleTimer = 0.2F;
                // 先用神格的粒子效果
                AbstractDungeon.effectsQueue.add(new DivinityParticleEffect());
            }
        }
        this.particleTimer2 -= Gdx.graphics.getDeltaTime();
        if (this.particleTimer2 < 0.0F) {
            this.particleTimer2 = MathUtils.random(0.45F, 0.55F);
            // 通过patch调整光晕效果为金色
            AbstractDungeon.effectsQueue.add(new StanceAuraEffect(RiceField.STANCE_ID));
        }
    }

    public void updateDescription() {
        this.description = stanceString.DESCRIPTION[0];
    }

    public void onEnterStance() {
        if (sfxId != -1L)
            stopIdleSfx();
        // 先用神格的音效
        CardCrawlGame.sound.play("STANCE_ENTER_DIVINITY");
        sfxId = CardCrawlGame.sound.playAndLoop("STANCE_LOOP_DIVINITY");
        // 全屏闪光效果，渲染为金黄色
        AbstractDungeon.effectsQueue.add(new BorderFlashEffect(Color.GOLD, true));
        // 粒子放射效果，渲染为金黄色
        for(int i = 0; i < 20; ++i) {
            AbstractDungeon.effectsQueue.add(new DivinityStanceChangeParticle(Color.GOLD, AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY));
        }
    }

    public void onExitStance() {
        stopIdleSfx();
    }

    // 先用神格的音效
    public void stopIdleSfx() {
        if (sfxId != -1L) {
            CardCrawlGame.sound.stop("STANCE_LOOP_DIVINITY", sfxId);
            sfxId = -1L;
        }
    }
}