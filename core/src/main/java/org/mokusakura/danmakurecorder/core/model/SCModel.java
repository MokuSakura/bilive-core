package org.mokusakura.danmakurecorder.core.model;

/**
 * @author MokuSakura
 */
public class SCModel extends DanmakuModelBase {
    protected Double price;
    protected String message;
    protected Integer time;
    protected Integer guardLevel;
    protected String guardName;
    protected Integer medalLevel;
    protected String medalName;


    protected SCModel() {}

    public Double getPrice() {
        return price;
    }

    public SCModel setPrice(Double price) {
        this.price = price;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public SCModel setMessage(String message) {
        this.message = message;
        return this;
    }

    public Integer getTime() {
        return time;
    }

    public SCModel setTime(Integer time) {
        this.time = time;
        return this;
    }

    public Integer getGuardLevel() {
        return guardLevel;
    }

    public SCModel setGuardLevel(Integer guardLevel) {
        this.guardLevel = guardLevel;
        return this;
    }

    public String getGuardName() {
        return guardName;
    }

    public SCModel setGuardName(String guardName) {
        this.guardName = guardName;
        return this;
    }

    public Integer getMedalLevel() {
        return medalLevel;
    }

    public SCModel setMedalLevel(Integer medalLevel) {
        this.medalLevel = medalLevel;
        return this;
    }

    public String getMedalName() {
        return medalName;
    }

    public SCModel setMedalName(String medalName) {
        this.medalName = medalName;
        return this;
    }
}
