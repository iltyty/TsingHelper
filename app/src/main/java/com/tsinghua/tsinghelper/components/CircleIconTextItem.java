package com.tsinghua.tsinghelper.components;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tsinghua.tsinghelper.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class CircleIconTextItem extends RelativeLayout {
    @BindView(R.id.item_icon)
    CircleImageView itemIcon;
    @BindView(R.id.item_text)
    TextView itemText;

    private View mView;

    public CircleIconTextItem(Context context) {
        this(context, null);
    }

    public CircleIconTextItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CircleIconTextItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        int textSize;

        mView = LayoutInflater.from(context).inflate(R.layout.component_circle_icon_text_item,
                this, true);
        ButterKnife.bind(mView);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CircleIconTextItem);
        itemIcon.setImageResource(a.getResourceId(R.styleable.CircleIconTextItem_android_src,
                R.drawable.ic_community_item_32dp));
        itemIcon.setBorderColor(a.getColor(R.styleable.CircleIconTextItem_borderColor, Color.BLACK));
        itemIcon.setBorderWidth(a.getDimensionPixelSize(
                R.styleable.CircleIconTextItem_borderWidth, 0
        ));

        itemText.setText(a.getString(R.styleable.CircleIconTextItem_android_text));
        textSize = a.getDimensionPixelSize(R.styleable.CircleIconTextItem_android_textSize, 0);
        if (textSize > 0) {
            itemText.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        }
        itemText.setTextColor(a.getColor(R.styleable.CircleIconTextItem_android_textColor, Color.BLACK));
        a.recycle();
    }

    public static float convertPixelsToDp(float px, Context context){
        return px / ((float) context.getResources().getDisplayMetrics()
                .densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

}
