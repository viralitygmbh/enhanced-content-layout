/*
 * Copyright (C) 2015 Virality GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.virality.enhancedcontentlayout;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * Wrapper layout for actual content of fragments or activities which allows to
 * - show a progress bar
 * - show messages (e.g. errors or empty)
 * - enable swipe to refresh
 */
public class EnhancedContentLayout extends FrameLayout {

    private View content;
    private ProgressBar progress;
    private TextView message;
    private SwipeRefreshLayout swipeRefreshLayout;

    public EnhancedContentLayout(Context context) {
        super(context);
    }

    public EnhancedContentLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EnhancedContentLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public EnhancedContentLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    /**
     * Create a content view which wraps around the {@code contentLayout} and enhances it.
     * @param context The context.
     * @param contentLayout The content layout to wrap around.
     * @param container Optional: Container which is used as root for inflation.
     */
    public EnhancedContentLayout(@NonNull Context context, @LayoutRes int contentLayout, @Nullable ViewGroup container) {
        this(context, contentLayout, container, null);
    }

    /**
     * Create a content view which wraps around the {@code contentLayout} and enhances it.
     * @param context The context.
     * @param contentLayout The content layout to wrap around.
     * @param container Optional: Container which is used as root for inflation.
     * @param inflater Optional: Inflater to inflate the layouts.
     */
    public EnhancedContentLayout(@NonNull Context context, @LayoutRes int contentLayout, @Nullable ViewGroup container, @Nullable LayoutInflater inflater) {
        super(context);
        if (inflater == null) inflater = LayoutInflater.from(context);
        this.content = inflater.inflate(contentLayout, container, false);
        initAdditions(context, this, inflater);
    }

    private void init(@NonNull Context context){
        FrameLayout frameLayout = new FrameLayout(context);
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            ((ViewGroup) child.getParent()).removeView(child);
            frameLayout.addView(child);
        }
        this.content = frameLayout;

        initAdditions(context, this, null);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        init(getContext());
    }

    private void initAdditions(@NonNull Context context, @NonNull ViewGroup container, @Nullable LayoutInflater inflater) {
        if (inflater == null) inflater = LayoutInflater.from(context);
        View additions = inflater.inflate(R.layout.enhanced_content_layout, container, false);

        this.progress = (ProgressBar) additions.findViewById(R.id.enhanced_content_layout_progress);
        this.message = (TextView) additions.findViewById(R.id.enhanced_content_layout_message);
        this.swipeRefreshLayout = (SwipeRefreshLayout) additions.findViewById(R.id.enhanced_content_layout_swipe_refresh_layout);
        this.swipeRefreshLayout.setEnabled(false);
        this.swipeRefreshLayout.addView(content);

        this.addView(additions);
    }

    /**
     * Hide content and message and show a progress bar.
     * If swipe to refresh is enabled and currently refreshing, the progress bar is not shown.
     */
    public void showProgress() {
        this.content.setVisibility(GONE);
        this.message.setVisibility(GONE);
        if (!this.swipeRefreshLayout.isEnabled() || !this.swipeRefreshLayout.isRefreshing()) {
            this.progress.setVisibility(VISIBLE);
        }
    }

    /**
     * Show a message at the top.
     * Can be used to show error, empty message, etc.
     * @param message The message.
     */
    public void showMessage(@NonNull String message) {
        hideProgress();
        this.content.setVisibility(GONE);
        this.message.setText(message);
        this.message.setVisibility(VISIBLE);
    }

    /**
     * Show a message at the top.
     * Can be used to show error, empty message, etc.
     * @param message The string resource of the message.
     */
    public void showMessage(@StringRes int message) {
        if (getContext() != null){
            showMessage(getContext().getString(message));
        }
    }

    /**
     * Hide message and progress and show the content.
     */
    public void showContent() {
        hideProgress();
        this.message.setVisibility(GONE);
        this.content.setVisibility(VISIBLE);
    }

    /**
     * Enable swipe to refresh.
     * @param listener The listener which is triggered when the user refreshes.
     */
    public void enableSwipeToRefresh(@NonNull SwipeRefreshLayout.OnRefreshListener listener){
        this.swipeRefreshLayout.setOnRefreshListener(listener);
        this.swipeRefreshLayout.setEnabled(true);
    }

    private void hideProgress(){
        this.swipeRefreshLayout.setRefreshing(false);
        this.progress.setVisibility(GONE);
    }
}