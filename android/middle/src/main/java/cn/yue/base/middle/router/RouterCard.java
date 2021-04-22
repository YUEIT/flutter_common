package cn.yue.base.middle.router;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.nio.BufferUnderflowException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import cn.yue.base.common.activity.TransitionAnimation;
import cn.yue.base.common.utils.code.TypeUtils;

public class RouterCard implements INavigation, Parcelable{

    private Uri uri;
    private Object tag;
    private Bundle mBundle;

    private String pactUrl;
    private String path;
    private int flags;
    private int timeout;
    private int enterAnim;
    private int exitAnim;
    private int transition; //入场方式
    private boolean isInterceptLogin; //是否登录拦截
    private INavigation navigation;

    public RouterCard(@NonNull INavigation navigation) {
        this.flags = -1;
        this.timeout = 300;
        this.mBundle = new Bundle();
        this.isInterceptLogin = false;
        this.transition = 0;
        this.navigation = navigation;
    }

    public void setNavigationImpl(@NonNull INavigation navigation) {
        this.navigation = navigation;
    }

    public void clear() {
        this.mBundle = new Bundle();
        this.path = null;
        this.isInterceptLogin = false;
        this.flags = 0;
        this.enterAnim = 0;
        this.exitAnim = 0;
        this.transition = 0;
    }

    public Object getTag() {
        return this.tag;
    }

    public RouterCard setTag(Object tag) {
        this.tag = tag;
        return this;
    }

    public Bundle getExtras() {
        return this.mBundle;
    }

    public int getEnterAnim() {
        return enterAnim;
    }

    public int getRealEnterAnim() {
        if (enterAnim > 0) {
            return enterAnim;
        } else {
            return TransitionAnimation.getStartEnterAnim(transition);
        }
    }

    public int getExitAnim() {
        return exitAnim;
    }

    public int getRealExitAnim() {
        if (exitAnim > 0) {
            return exitAnim;
        } else {
            return TransitionAnimation.getStartExitAnim(transition);
        }
    }

    public int getTransition() {
        return transition;
    }

    public int getTimeout() {
        return this.timeout;
    }

    public RouterCard setTimeout(int timeout) {
        this.timeout = timeout;
        return this;
    }

    public RouterCard setPactUrl(String pactUrl) {
        this.pactUrl = pactUrl;
        return this;
    }

    public String getPactUrl() {
        return pactUrl;
    }

    public RouterCard setPath(String path) {
        this.path = path;
        return this;
    }

    public String getPath() {
        if (TextUtils.isEmpty(path) && !TextUtils.isEmpty(pactUrl)) {
            String[] split = pactUrl.split("://");
            if (split.length == 2) {
                path = "/" + split[1];
            }
        }
        Log.d("luobiao", "getPath: " + path);
        return path;
    }

    public Uri getUri() {
        return this.uri;
    }

    public RouterCard setUri(Uri uri) {
        this.uri = uri;
        return this;
    }

    public RouterCard with(Bundle bundle) {
        if (null != bundle) {
            this.mBundle = bundle;
        }
        return this;
    }

    public RouterCard withFlags(int flag) {
        this.flags = flag;
        return this;
    }

    public int getFlags() {
        return this.flags;
    }

    public RouterCard withString(@Nullable String key, @Nullable String value) {
        this.mBundle.putString(key, value);
        return this;
    }

    public RouterCard withBoolean(@Nullable String key, boolean value) {
        this.mBundle.putBoolean(key, value);
        return this;
    }

    public RouterCard withShort(@Nullable String key, short value) {
        this.mBundle.putShort(key, value);
        return this;
    }

    public RouterCard withInt(@Nullable String key, int value) {
        this.mBundle.putInt(key, value);
        return this;
    }

    public RouterCard withLong(@Nullable String key, long value) {
        this.mBundle.putLong(key, value);
        return this;
    }

    public RouterCard withDouble(@Nullable String key, double value) {
        this.mBundle.putDouble(key, value);
        return this;
    }

    public RouterCard withByte(@Nullable String key, byte value) {
        this.mBundle.putByte(key, value);
        return this;
    }

    public RouterCard withChar(@Nullable String key, char value) {
        this.mBundle.putChar(key, value);
        return this;
    }

    public RouterCard withFloat(@Nullable String key, float value) {
        this.mBundle.putFloat(key, value);
        return this;
    }

    public RouterCard withCharSequence(@Nullable String key, @Nullable CharSequence value) {
        this.mBundle.putCharSequence(key, value);
        return this;
    }

    public RouterCard withParcelable(@Nullable String key, @Nullable Parcelable value) {
        this.mBundle.putParcelable(key, value);
        return this;
    }

    public RouterCard withParcelableArray(@Nullable String key, @Nullable Parcelable[] value) {
        this.mBundle.putParcelableArray(key, value);
        return this;
    }

    public RouterCard withParcelableArrayList(@Nullable String key, @Nullable ArrayList<? extends Parcelable> value) {
        this.mBundle.putParcelableArrayList(key, value);
        return this;
    }

    public RouterCard withSparseParcelableArray(@Nullable String key, @Nullable SparseArray<? extends Parcelable> value) {
        this.mBundle.putSparseParcelableArray(key, value);
        return this;
    }

    public RouterCard withIntegerArrayList(@Nullable String key, @Nullable ArrayList<Integer> value) {
        this.mBundle.putIntegerArrayList(key, value);
        return this;
    }

    public RouterCard withStringArrayList(@Nullable String key, @Nullable ArrayList<String> value) {
        this.mBundle.putStringArrayList(key, value);
        return this;
    }

    public RouterCard withCharSequenceArrayList(@Nullable String key, @Nullable ArrayList<CharSequence> value) {
        this.mBundle.putCharSequenceArrayList(key, value);
        return this;
    }

    public RouterCard withSerializable(@Nullable String key, @Nullable Serializable value) {
        this.mBundle.putSerializable(key, value);
        return this;
    }

    public RouterCard withByteArray(@Nullable String key, @Nullable byte[] value) {
        this.mBundle.putByteArray(key, value);
        return this;
    }

    public RouterCard withShortArray(@Nullable String key, @Nullable short[] value) {
        this.mBundle.putShortArray(key, value);
        return this;
    }

    public RouterCard withCharArray(@Nullable String key, @Nullable char[] value) {
        this.mBundle.putCharArray(key, value);
        return this;
    }

    public RouterCard withFloatArray(@Nullable String key, @Nullable float[] value) {
        this.mBundle.putFloatArray(key, value);
        return this;
    }

    public RouterCard withCharSequenceArray(@Nullable String key, @Nullable CharSequence[] value) {
        this.mBundle.putCharSequenceArray(key, value);
        return this;
    }

    public RouterCard withBundle(@Nullable String key, @Nullable Bundle value) {
        this.mBundle.putBundle(key, value);
        return this;
    }

    public RouterCard withMap(Map<String, Object> map) {
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            putObject(entry.getKey(), entry.getValue());
        }
        return this;
    }

    public RouterCard withMapString(Map<String, String> map) {
        for (Map.Entry<String, String> entry : map.entrySet()) {
            withString(entry.getKey(), entry.getValue());
        }
        return this;
    }


    private void putObject(String key, Object object) {
        if (object == null) {
            return;
        }
        if (object instanceof String) {
            mBundle.putString(key, (String) object);
        } else if (object instanceof Boolean) {
            mBundle.putBoolean(key, (Boolean) object);
        } else if (object instanceof Integer) {
            mBundle.putInt(key, (int) object);
        } else if (object instanceof Float) {
            mBundle.putFloat(key, (float) object);
        } else if (object instanceof Double) {
            mBundle.putDouble(key, (double) object);
        } else if (object instanceof Long) {
            mBundle.putLong(key, (long) object);
        } else if (object instanceof Map) {
            mBundle.putSerializable(key, new BundleMap((Map) object));
        } else if (object instanceof ArrayList) {
            ArrayList arrayList = (ArrayList) object;
            if (!arrayList.isEmpty()) {
                Object ob = arrayList.get(0);
                if(ob instanceof String) {
                    mBundle.putStringArrayList(key, (ArrayList<String>) object);
                } else if (ob instanceof Integer) {
                    mBundle.putIntegerArrayList(key, (ArrayList<Integer>) object);
                } else {
                    mBundle.putParcelableArrayList(key, (ArrayList<? extends Parcelable>) object);
                }
            } else {
                mBundle.putParcelableArrayList(key, (ArrayList<? extends Parcelable>) object);
            }
        }
    }

    public RouterCard withTransition(int enterAnim, int exitAnim) {
        this.enterAnim = enterAnim;
        this.exitAnim = exitAnim;
        return this;
    }

    public RouterCard withTransitionStyle(int transitionStyle) {
        this.transition = transitionStyle;
        return this;
    }

    @Override
    public String toString() {
        return "RouterCard{" +
                "uri=" + uri +
                ", tag=" + tag +
                ", mBundle=" + mBundle +
                ", pactUrl='" + pactUrl + '\'' +
                ", path='" + path + '\'' +
                ", flags=" + flags +
                ", timeout=" + timeout +
                ", enterAnim=" + enterAnim +
                ", exitAnim=" + exitAnim +
                ", transition=" + transition +
                ", isInterceptLogin=" + isInterceptLogin +
                ", navigation=" + navigation +
                '}';
    }

    public RouterCard setInterceptLogin() {
        isInterceptLogin = true;
        return this;
    }

    public boolean isInterceptLogin() {
        return isInterceptLogin;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(uri, flags);
        dest.writeBundle(mBundle);
        dest.writeString(path);
        dest.writeInt(flags);
        dest.writeInt(timeout);
        dest.writeInt(enterAnim);
        dest.writeInt(exitAnim);
        dest.writeInt(transition);
        dest.writeByte((byte) (isInterceptLogin ? 1 : 0));
    }

    protected RouterCard(Parcel in) {
        uri = in.readParcelable(Uri.class.getClassLoader());
        mBundle = in.readBundle();
        path = in.readString();
        flags = in.readInt();
        timeout = in.readInt();
        enterAnim = in.readInt();
        exitAnim = in.readInt();
        transition = in.readInt();
        isInterceptLogin = in.readByte() != 0;
    }

    public static final Creator<RouterCard> CREATOR = new Creator<RouterCard>() {
        @Override
        public RouterCard createFromParcel(Parcel in) {
            return new RouterCard(in);
        }

        @Override
        public RouterCard[] newArray(int size) {
            return new RouterCard[size];
        }
    };

    @Override
    public void bindRouterCard(RouterCard routerCard) {

    }

    @Override
    public void navigation(Context context) {
        if (navigation != null) {
            navigation.navigation(context);
        }
    }

    @Override
    public void navigation(@NonNull Context context, Class toActivity) {
        if (navigation != null) {
            navigation.navigation(context, toActivity);
        }
    }

    @Override
    public void navigation(Activity context, int requestCode) {
        if (navigation != null) {
            navigation.navigation(context, requestCode);
        }
    }

    @Override
    public void navigation(@NonNull Activity context, Class toActivity, int requestCode) {
        if (navigation != null) {
            navigation.navigation(context, toActivity, requestCode);
        }
    }
}
