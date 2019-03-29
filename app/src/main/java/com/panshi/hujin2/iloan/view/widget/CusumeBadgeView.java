package com.panshi.hujin2.iloan.view.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.jauker.widget.BadgeView;
import com.panshi.hujin2.iloan.util.DensityUtil;


/**
 * Created by Administrator on 2018/7/19 0019.
 */

public class CusumeBadgeView extends BadgeView {
    private Context context;
    public CusumeBadgeView(Context context) {
        super(context);
        this.context=context;
    }

    public CusumeBadgeView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CusumeBadgeView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void setTargetView(View target) {
        if(this.getParent() != null) {
            ((ViewGroup)this.getParent()).removeView(this);
        }

        if(target != null) {
            if(target.getParent() instanceof FrameLayout) {
                ((FrameLayout)target.getParent()).removeView(this);
                ((FrameLayout)target.getParent()).addView(this);
                ((FrameLayout)target.getParent()).requestLayout();
            } else if(target.getParent() instanceof ViewGroup) {
                ViewGroup parentContainer = (ViewGroup)target.getParent();
                int groupIndex = parentContainer.indexOfChild(target);
                parentContainer.removeView(target);
                FrameLayout badgeContainer = new FrameLayout(this.getContext());
                ViewGroup.LayoutParams parentlayoutParams = target.getLayoutParams();
               // parentContainer.setBackgroundColor(getResources().getColor(R.color.app_bg));
                parentContainer.addView(badgeContainer, groupIndex, parentlayoutParams);
                FrameLayout.LayoutParams lp=new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,FrameLayout.LayoutParams.WRAP_CONTENT);
                lp.topMargin=DensityUtil.dip2px(context,5);
                lp.rightMargin= DensityUtil.dip2px(context,5);
               // badgeContainer.setBackgroundColor(getResources().getColor(R.color.app_bg));
                badgeContainer.addView(target,lp);
                badgeContainer.addView(this,this.getLayoutParams());
                badgeContainer.requestLayout();
          //      badgeContainer.setBackgroundColor(getResources().getColor(R.color.black_alpha_40));
            } else if(target.getParent() == null) {
                Log.e(this.getClass().getSimpleName(), "ParentView is needed");
            }

        }
    }
}
