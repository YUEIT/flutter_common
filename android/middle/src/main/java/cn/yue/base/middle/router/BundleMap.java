package cn.yue.base.middle.router;

import java.io.Serializable;
import java.util.Map;

/**
 * Description :
 * Created by yue on 2020/4/22
 */
public class BundleMap implements Serializable {

    private static final long serialVersionUID = 4502025925715013070L;
    private Map map;

    public BundleMap(Map map) {
        this.map = map;
    }

    public Map getMap() {
        return map;
    }

    public void setMap(Map map) {
        this.map = map;
    }

    @Override
    public String toString() {
        return "BundleMap{" +
                "map=" + map +
                '}';
    }
}
