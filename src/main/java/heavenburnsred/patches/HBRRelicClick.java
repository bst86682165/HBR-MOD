package heavenburnsred.patches;


import com.megacrit.cardcrawl.helpers.input.InputHelper;
import heavenburnsred.relics.BaseRelic;


public abstract class HBRRelicClick extends BaseRelic {
    private boolean RclickStart = false;

    private boolean Rclick = false;

    public HBRRelicClick(String id, String imageName, RelicTier tier, LandingSound sfx) {
        super(id, imageName, tier, sfx);
    }

    public abstract void onRightClick();

    public void update() {
        super.update();
        if (this.RclickStart && InputHelper.justReleasedClickRight) {
            if (this.hb.hovered)
                this.Rclick = true;
            this.RclickStart = false;
        }
        if (this.isObtained && this.hb != null && this.hb.hovered && InputHelper.justClickedRight)
            this.RclickStart = true;
        if (this.Rclick) {
            this.Rclick = false;
            onRightClick();
        }
    }
}
