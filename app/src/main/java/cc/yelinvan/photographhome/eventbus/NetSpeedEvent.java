package cc.yelinvan.photographhome.eventbus;

/**
 * Create by johnson on 2019/1/16 下午8:41
 * 网速event
 */
public class NetSpeedEvent {
    private int speed;  //当前网速
    private String unit;    //单位

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    @Override
    public String toString() {
        return speed + unit;
    }
}
