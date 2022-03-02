package cn.yue.base.common.activity.rx;

import androidx.annotation.Nullable;

public final class OutsideLifecycleException extends IllegalStateException {

    public OutsideLifecycleException(@Nullable String detailMessage) {
        super(detailMessage);
    }
}