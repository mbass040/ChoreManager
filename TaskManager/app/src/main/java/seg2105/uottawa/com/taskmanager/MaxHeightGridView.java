package seg2105.uottawa.com.taskmanager;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * Inspired by stackoverflow answer: https://stackoverflow.com/a/24186541/5029537
 */

public class MaxHeightGridView extends GridView {
    public MaxHeightGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MaxHeightGridView(Context context) {
        super(context);
    }

    public MaxHeightGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        // Limit the height of our GridView to 450 px, any overflow will make the GridView scrollable
        int expandSpec = MeasureSpec.makeMeasureSpec(450, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
