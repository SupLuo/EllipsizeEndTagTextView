# EllipsizeEndTagTextView

  TextView 末尾显示省略号时，可以在省略号的后面增加用户自定义标签文本，并可以设置点击。
  
  运行截图：
  ![运行截图](https://github.com/SupLuo/EllipsizeEndTagTextView/blob/master/sample/%E6%88%AA%E5%B1%8F_20190528_141802.jpg)
  
# 特点：
   测试的机型都运行良好，比百度等查找到的实现方式更稳定，支持Kotlin以及几种特殊的文本结构（比如显示省略号所在的行只是一个换行符而没有实际内容，再比如省略号后面的所有行都只是换行符而没有实际内容）。
   可以放在ListView中
  
# 使用：
  1、可以直接用于系统控件TextView
    
   ```
    //java
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
                
    //kotlin
        //类似java，也可以使用扩展方法
      normalText.applyEndTagEllipsizeText(...)
                
   ```
   
   2、自定义控件使用
   ```
   //xml使用场景
       <halo.android.widget.EllipsizeEndTagTextView
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="@string/text_string"
            android:maxLines="2" 
            android:ellipsize="end"
            app:endTag="全文"
            app:endTagColor="@color/colorPrimary"/>
       
       //endTag属性设置标签文本
       //endTagColor属性设置标签文本颜色
       //maxLines属性必须设置
       //ellipsize也必须为end：只支持end属性
       
    //更改文本调用控件setTextForEndTagEllipsize方法，不能调用TextView提供的setText方法，否则无效果
   ```
   
# 配置：
    添加依赖,在module的gradle文件中添加以下语句：
    ```
        implementation 'halo.android.widget:ellipsizeendtagtextview:1.0'
    ```
