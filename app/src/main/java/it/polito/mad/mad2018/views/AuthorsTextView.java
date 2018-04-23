package it.polito.mad.mad2018.views;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;

public class AuthorsTextView extends android.support.v7.widget.AppCompatTextView {
    public AuthorsTextView(Context context) {
        super(context);
    }

    public AuthorsTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AuthorsTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        String finalText = text.toString().replace(",", ", ")
                .replace("[", "")
                .replace("]", "")
                .replace("\"", "");
        super.setText(finalText, type);
    }
}
