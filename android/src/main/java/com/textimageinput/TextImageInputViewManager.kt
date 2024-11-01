package com.textimageinput

import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.uimanager.SimpleViewManager
import com.facebook.react.uimanager.ThemedReactContext
import com.facebook.react.uimanager.annotations.ReactProp

class TextImageInputViewManager(private val reactContext: ReactApplicationContext) :
  SimpleViewManager<TextImageInputView>() {

  override fun getName() = "TextImageInputView"

  override fun createViewInstance(reactContext: ThemedReactContext): TextImageInputView {
    return TextImageInputView(reactContext)
  }

  @ReactProp(name = "color")
  fun setColor(view: TextImageInputView, color: String) {
    view.setTextColor(android.graphics.Color.parseColor(color))
  }

  @ReactProp(name = "fontSize")
  fun setFontSize(view: TextImageInputView, fontSize: Float) {
    view.textSize = fontSize
  }

  override fun getExportedCustomBubblingEventTypeConstants(): Map<String, Any>? {
    return mapOf(
      "onFocus" to mapOf(
        "phasedRegistrationNames" to mapOf("bubbled" to "onFocus")
      ),
      "onBlur" to mapOf(
        "phasedRegistrationNames" to mapOf("bubbled" to "onBlur")
      ),
      "onChange" to mapOf(
        "phasedRegistrationNames" to mapOf("bubbled" to "onChange")
      )
    )
  }
}
