package halo.android.widget.sample

import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.TextView
import android.widget.Toast
import halo.android.widget.EllipsizeEndTagTextView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val normalText = findViewById<TextView>(R.id.normalText)

        EllipsizeEndTagTextView.setEndTagForEllipsizeText(normalText,
            "常规控件：这是一个很长很长的文本这是一个很长很长的文本这是一个很长很长的文本这是一个很长很长的文" +
                    "本这是一个很长很长的文本这是一个很长很长的文本这是一个很长很长的文本这是一个很长很长的文本这是一个很长很长的文本这是一个" +
                    "很长很长的文本这是一个很长很长的文本" +
                    "这是一个很长很长的文本这是一个很长很长的文本这是一个很长很长的文本这是一个很长很长的文本",
            "可以点击",
            Color.RED,
            View.OnClickListener { Toast.makeText(this@MainActivity, "点击了文本控件", Toast.LENGTH_LONG).show() })

        findViewById<View>(R.id.listSample).setOnClickListener {
            AlertDialog.Builder(it.context)
                .setMessage("使用自定义控件即可")
                .show()
        }
    }
}
