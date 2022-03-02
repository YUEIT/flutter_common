package cn.yue.base.middle.mvvm.data;

import android.os.Bundle;

public class FinishModel {
    private int resultCode;
    private Bundle bundle;

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public Bundle getBundle() {
        return bundle;
    }

    public void setBundle(Bundle bundle) {
        this.bundle = bundle;
    }
}
