package com.jooyer.jooyerretrofit.rxlife;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import rx.Observable;
import rx.functions.Func1;
import rx.subjects.BehaviorSubject;

/**
 * Created by Jooyer on 2016/11/28
 */

public abstract class RxFragment extends Fragment {
    public static final String STATE_SAVE_IS_HIDDEN = "STATE_SAVE_IS_HIDDEN";
    protected BehaviorSubject<FragmentLifeCycleEvent> lifeSubject = BehaviorSubject.create();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        lifeSubject.onNext(FragmentLifeCycleEvent.ATTACH);
//        LogUtils.i("RxFragment","======onAttach========");
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        LogUtils.i("RxFragment","======onViewCreated========");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lifeSubject.onNext(FragmentLifeCycleEvent.CREATE);
//        LogUtils.i("RxFragment","======onCreate========");

        if (null != savedInstanceState) {
            boolean isSupportHidden = savedInstanceState.getBoolean(STATE_SAVE_IS_HIDDEN);
            FragmentTransaction ft = getFragmentManager().beginTransaction();

            if (isSupportHidden) {
                ft.hide(this);
            } else {
                ft.show(this);
            }
            ft.commit();
        }
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        lifeSubject.onNext(FragmentLifeCycleEvent.CREATE_VIEW);
//        LogUtils.i("RxFragment","======onCreateView========");
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        LogUtils.i("RxFragment","======onActivityCreated========");
    }

    @Override
    public void onStart() {
        super.onStart();
        lifeSubject.onNext(FragmentLifeCycleEvent.START);
//        LogUtils.i("RxFragment","======onStart========");
    }


    @Override
    public void onResume() {
        super.onResume();
        lifeSubject.onNext(FragmentLifeCycleEvent.RESUME);
    }

    @Override
    public void onPause() {
        super.onPause();
        lifeSubject.onNext(FragmentLifeCycleEvent.PAUSE);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(STATE_SAVE_IS_HIDDEN, isHidden());

    }

    @Override
    public void onStop() {
        super.onStop();
        lifeSubject.onNext(FragmentLifeCycleEvent.STOP);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        lifeSubject.onNext(FragmentLifeCycleEvent.DESTROY_VIEW);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        lifeSubject.onNext(FragmentLifeCycleEvent.DESTROY);
    }


    @Override
    public void onDetach() {
        super.onDetach();
        lifeSubject.onNext(FragmentLifeCycleEvent.DETACH);
    }


    @NonNull
    public <T> Observable.Transformer<T, T> bindUntilEvent(@NonNull final FragmentLifeCycleEvent event) {
        return new Observable.Transformer<T, T>() {
            @Override
            public Observable<T> call(Observable<T> sourceObservable) {
                Observable<FragmentLifeCycleEvent> compareLifecycleObservable =
                        lifeSubject.takeFirst(new Func1<FragmentLifeCycleEvent, Boolean>() {
                            @Override
                            public Boolean call(FragmentLifeCycleEvent fragmentLifeCycleEvent) {
//                                LogUtils.i("info","======RxFragment========" + event + "==========" + fragmentLifeCycleEvent);
                                /* 当返回true时表示停止请求网络 */
                                return fragmentLifeCycleEvent.equals(event);
                            }
                        });
                return sourceObservable.takeUntil(compareLifecycleObservable);
            }
        };
    }


}
