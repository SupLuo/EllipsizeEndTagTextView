package halo.android.widget.sample;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import halo.android.widget.EllipsizeEndTagTextView;

/**
 * Created by Lucio on 2019/5/28.
 */
public class MainJavaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView normalText = findViewById(R.id.normalText);
        EllipsizeEndTagTextView.setEndTagForEllipsizeText(normalText,
                "常规控件：这是一个很长很长的文本这是一个很长很长的文本这是一个很长很长的文本这是一个很长很长的文" +
                        "本这是一个很长很长的文本这是一个很长很长的文本这是一个很长很长的文本这是一个很长很长的文本这是一个很长很长的文本这是一个" +
                        "很长很长的文本这是一个很长很长的文本" +
                        "这是一个很长很长的文本这是一个很长很长的文本这是一个很长很长的文本这是一个很长很长的文本",
                "可以点击",
                Color.RED,
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(MainJavaActivity.this, "点击了文本控件", Toast.LENGTH_LONG).show();
                    }
                });

    }
}
