package swipedelete;

import java.io.Serializable;

/**
 * 实体类
 */
public class TestBean implements Serializable {

    private String name;
    private int imgRes;
    private String info;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImgRes() {
        return imgRes;
    }

    public void setImgRes(int imgRes) {
        this.imgRes = imgRes;
    }

    public String getInfo(){return info;}

    public void setInfo(String info) {
        this.info = info;
    }
}
