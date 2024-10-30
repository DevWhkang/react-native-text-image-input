package com.textimageinput

import android.graphics.Color
import android.widget.EditText

import com.facebook.react.uimanager.SimpleViewManager
import com.facebook.react.uimanager.ThemedReactContext
import com.facebook.react.uimanager.annotations.ReactProp


class TextImageInputViewManager : SimpleViewManager<EditText>() {

  companion object {
    var sharedEditText: EditText? = null
  }

  override fun getName() = "TextImageInputView"

  override fun createViewInstance(reactContext: ThemedReactContext): EditText {
    val editText = EditText(reactContext)
    sharedEditText = editText
    return editText
  }

  @ReactProp(name = "color")
  fun setColor(view: EditText, color: String) {
    view.setTextColor(Color.parseColor(color))
  }

  @ReactProp(name = "fontSize")
  fun setFontSize(view: EditText, fontSize: Float) {
    view.textSize = fontSize
  }
}
