package cn.yue.base.common.activity.rx;

import androidx.annotation.CheckResult;
import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle.Event;

import io.reactivex.Observable;
import io.reactivex.functions.Function;

public class RxLifecycleAndroid {
    private static final Function<Event, Event> LIFECYCLE = new Function<Event, Event>() {
        public Event apply(Event lastEvent) throws Exception {
            switch(lastEvent) {
                case ON_CREATE:
                    return Event.ON_DESTROY;
                case ON_START:
                    return Event.ON_STOP;
                case ON_RESUME:
                    return Event.ON_PAUSE;
                case ON_PAUSE:
                    return Event.ON_STOP;
                case ON_STOP:
                    return Event.ON_DESTROY;
                case ON_DESTROY:
                    throw new OutsideLifecycleException("Cannot bind to Activity lifecycle when outside of it.");
                default:
                    throw new UnsupportedOperationException("Binding to " + lastEvent + " not yet implemented");
            }
        }
    };

    private RxLifecycleAndroid() {
        throw new AssertionError("No instances");
    }

    @NonNull
    @CheckResult
    public static <T> LifecycleTransformer<T> bind(@NonNull Observable<Event> lifecycle) {
        return RxLifecycle.bind(lifecycle, LIFECYCLE);
    }

}
