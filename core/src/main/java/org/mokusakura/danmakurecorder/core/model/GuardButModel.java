package org.mokusakura.danmakurecorder.core.model;

/**
 * @author MokuSakura
 */
public class GuardButModel extends DanmakuModelBase {
    protected Integer guardLevel;
    protected String guardName;
    protected Integer giftId;
    protected String giftName;
    protected Double giftPrice;


    protected GuardButModel() {}

    public Integer getGuardLevel() {
        return guardLevel;
    }

    public GuardButModel setGuardLevel(Integer guardLevel) {
        this.guardLevel = guardLevel;
        return this;
    }

    public String getGuardName() {
        return guardName;
    }

    public GuardButModel setGuardName(String guardName) {
        this.guardName = guardName;
        return this;
    }

    public Integer getGiftId() {
        return giftId;
    }

    public GuardButModel setGiftId(Integer giftId) {
        this.giftId = giftId;
        return this;
    }

    public String getGiftName() {
        return giftName;
    }

    public GuardButModel setGiftName(String giftName) {
        this.giftName = giftName;
        return this;
    }

    public Double getGiftPrice() {
        return giftPrice;
    }

    public GuardButModel setGiftPrice(Double giftPrice) {
        this.giftPrice = giftPrice;
        return this;
    }
}
