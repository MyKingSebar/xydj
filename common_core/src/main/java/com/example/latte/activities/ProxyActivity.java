package com.example.latte.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ContentFrameLayout;

import com.example.latte.R;
import com.example.latte.delegates.LatteDelegate;

import me.yokeyword.fragmentation.BuildConfig;
import me.yokeyword.fragmentation.ExtraTransaction;
import me.yokeyword.fragmentation.Fragmentation;
import me.yokeyword.fragmentation.ISupportActivity;
import me.yokeyword.fragmentation.SupportActivityDelegate;
import me.yokeyword.fragmentation.anim.FragmentAnimator;
import me.yokeyword.fragmentation.debug.DebugStackDelegate;

public abstract class ProxyActivity extends AppCompatActivity implements ISupportActivity {

    private final SupportActivityDelegate DELEGATE = new SupportActivityDelegate(this);

    private DebugStackDelegate mDebugStackDelegate;

    public abstract LatteDelegate setRootDelegate();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DELEGATE.onCreate(savedInstanceState);
        initContainer(savedInstanceState);
        mDebugStackDelegate = new DebugStackDelegate(this);

        mDebugStackDelegate.onCreate(Fragmentation.getDefault().getMode());
    }

    private void initContainer(@Nullable Bundle savedInstanceState) {
        @SuppressLint("RestrictedApi")
        final ContentFrameLayout container = new ContentFrameLayout(this);
        container.setId(R.id.delegate_container);
        setContentView(container);
        if (savedInstanceState == null) {
            DELEGATE.loadRootFragment(R.id.delegate_container, setRootDelegate(),false,false);
        }
    }
    @Override
    public void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDebugStackDelegate.onPostCreate(Fragmentation.getDefault().getMode());
    }

    /**
     * 显示栈视图dialog,调试时使用
     */
    public void showFragmentStackHierarchyView() {
        mDebugStackDelegate.showFragmentStackHierarchyView();
    }

    /**
     * 显示栈视图日志,调试时使用
     */
    public void logFragmentStackHierarchy(String TAG) {
        mDebugStackDelegate.logFragmentRecords(TAG);
    }

    @Override
    protected void onDestroy() {
        DELEGATE.onDestroy();
        super.onDestroy();
        mDebugStackDelegate.onDestroy();
        System.gc();
        System.runFinalization();
    }

    @Override
    public SupportActivityDelegate getSupportDelegate() {
        return DELEGATE;
    }

    @Override
    public ExtraTransaction extraTransaction() {
        return DELEGATE.extraTransaction();
    }

    @Override
    public FragmentAnimator getFragmentAnimator() {
        return DELEGATE.getFragmentAnimator();
    }

    @Override
    public void setFragmentAnimator(FragmentAnimator fragmentAnimator) {
        DELEGATE.setFragmentAnimator(fragmentAnimator);
    }

    @Override
    public FragmentAnimator onCreateFragmentAnimator() {
        return DELEGATE.onCreateFragmentAnimator();
    }

    @Override
    public void onBackPressedSupport() {
        DELEGATE.onBackPressedSupport();
    }

    @Override
    public void onBackPressed() {
        DELEGATE.onBackPressed();
    }

    @Override
    public void post(Runnable runnable) {
        DELEGATE.post(runnable);
    }
}
