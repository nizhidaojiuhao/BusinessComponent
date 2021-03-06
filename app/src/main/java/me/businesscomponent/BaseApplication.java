package me.businesscomponent;

import android.app.Activity;
import android.app.Application;

import com.from.business.http.HttpBusiness;
import com.from.view.picture.ui.SelectImageActivity;
import com.from.view.swipeback.SimpleSwipeBackDelegate;
import com.from.view.swipeback.SwipeBackHelper;
import com.from.view.swipeback.SwipeOptions;
import com.squareup.leakcanary.LeakCanary;

import java.util.ArrayList;
import java.util.List;

import me.businesscomponent.http.Api;
import timber.log.Timber;

/**
 * @author Vea
 * @since 2019-01
 */
public class BaseApplication extends Application {
    public static String TAG = "HttpX";

    @Override
    public void onCreate() {
        super.onCreate();
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);

        /**
         * 必须在 Application 的 onCreate 方法中执行 SwipeBackHelper.init 来初始化滑动返回
         * 第一个参数：应用程序上下文
         * 第二个参数：如果发现滑动返回后立即触摸界面时应用崩溃，请把该界面里比较特殊的 View 的 class 添加到该集合中，目前在库中已经添加了 WebView 和 SurfaceView
         * 第三个参数：如果有些第三方库 Activity 不需要 swipeBack 可使用Option 配置
         * 第四个参数：滑动代理，扩展滑动开始和结束事件
         */

        List<String> exclude = new ArrayList<>();
        exclude.add(SelectImageActivity.class.getSimpleName());
        SwipeOptions options = SwipeOptions.builder()
            .exclude(exclude) // 排除不需要侧滑的类
            // 以下默认值
//            .isWeChatStyle(true)  //  是否是微信滑动返回样式
//            .isTrackingLeftEdge(true)     // 是否仅仅跟踪左侧边缘的滑动返回
//            .isShadowAlphaGradient(true)     // 阴影区域的透明度是否根据滑动的距离渐变
//            .isNavigationBarOverlap(false)     // 底部导航条是否悬浮在内容上
//            .isNeedShowShadow(true) // 否显示滑动返回的阴影效果
//            .shadowResId() // 阴影资源 id
            .build();
        SwipeBackHelper.init(this, null, options, new SimpleSwipeBackDelegate() {
            @Override
            public void onSwipeBackLayoutExecuted(Activity activity) {

            }

            //            @Override
//            public void onSwipeBackLayoutCancel() {
//                super.onSwipeBackLayoutCancel();
//            }
//
//            @Override
//            public void onSwipeBackLayoutSlide(float slideOffset) {
//                super.onSwipeBackLayoutSlide(slideOffset);
//            }
        });

        Timber.plant(new Timber.DebugTree());

        HttpBusiness.init(this, Api.APP_DOMAIN);
    }
}
