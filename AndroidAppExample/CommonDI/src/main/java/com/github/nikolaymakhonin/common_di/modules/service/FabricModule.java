package com.github.nikolaymakhonin.common_di.modules.service;

import android.content.Context;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;
import com.crashlytics.android.core.CrashlyticsCore;
import com.github.nikolaymakhonin.common_di.scopes.PerService;
import com.github.nikolaymakhonin.logger.Log;
import com.github.nikolaymakhonin.logger.LogEventInfo;
import com.github.nikolaymakhonin.logger.LogPriority;
import com.github.nikolaymakhonin.utils.DeviceUtils;

import java.net.ConnectException;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import io.fabric.sdk.android.Fabric;
import rx.Observable;
import rx.Observer;
import rx.schedulers.Schedulers;

@Module
public class FabricModule {

    private static final String LOG_TAG = "FabricModule";
    
    @Provides
    @PerService
    @Named("isEnabled")
    public static boolean isEnabled() {
        return !DeviceUtils.isEmulator();
    }

    @Provides
    @PerService
    public static Fabric getInstance(
        Context context,
        @Named("isEnabled") boolean enabled,
        @Named("FabricLogObserver") Observer<LogEventInfo> logObserver)
    {
        CrashlyticsCore core = new CrashlyticsCore.Builder().disabled(!enabled).build();
        Fabric instance = Fabric.with(context, new Crashlytics.Builder().core(core).build());

        if (enabled) {
            // Send to Fabric all the errors from the log
            Log.getLogObservable().subscribe(logObserver);
        }

        return instance;
    }

    @Provides
    @PerService
    @Named("FabricLogObserver")
    public static Observer<LogEventInfo> getFabricLogObserver() {
        Observer<LogEventInfo> observer = new Observer<LogEventInfo>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                logEventHandler(new LogEventInfo(LogPriority.Error, LOG_TAG, null, e, false));
            }

            @Override
            public void onNext(LogEventInfo logEventInfo) {
                logEventHandler(logEventInfo);
            }
        };
        
        return observer;
    }
    
    private static void logEventHandler(LogEventInfo logEventInfo) {
        try {
            if (logEventInfo.exception instanceof ConnectException
                || logEventInfo.exception instanceof UnknownHostException)
            {
                // Do not have access to the Internet, not an error of the program
                return;
            }

            if (logEventInfo.exception != null) {
                Crashlytics.setString("tag", logEventInfo.tag);
                Crashlytics.setString("message", logEventInfo.message);
                Crashlytics.setInt("priority", logEventInfo.priority);
                Crashlytics.logException(logEventInfo.exception);
                if (logEventInfo.terminateApplication) {
                    // If fatal error, send error to fabric in new thread and close application
                    Observable.just(null).observeOn(Schedulers.computation()).subscribe(o -> {
                        //noinspection finally
                        try {
                            Crashlytics.getInstance().core.getFabric().getExecutorService()
                                .awaitTermination(60, TimeUnit.SECONDS);
                        } catch (InterruptedException e1) {
                            e1.printStackTrace();
                        } finally {
                            System.exit(1);
                        }
                    });
                }
            } else {
                Crashlytics.log(logEventInfo.priority, logEventInfo.tag, logEventInfo.message);
            }

            // Errors counter
            Answers.getInstance().logCustom(
                new CustomEvent("LogEvent").putCustomAttribute("priority", LogPriority.toString(logEventInfo.priority)));

        } catch (Exception exception) {
            Log.e("FragmenterController", null, exception);
        }
    }
}
