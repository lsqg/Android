package lc.nsu.edu.cn.mysafe.db.domain;

/**
 * Created by 刘畅 on 2017/7/15.
 */
public class BlackNumberInfo {
    public String phone;
    public String mode;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    @Override
    public String toString() {
        return "BlackNumberInfo{" +
                "phone='" + phone + '\'' +
                ", mode='" + mode + '\'' +
                '}';
    }
}
