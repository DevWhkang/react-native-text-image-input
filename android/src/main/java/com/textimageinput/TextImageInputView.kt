package com.textimageinput

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import com.facebook.react.bridge.Arguments
import com.facebook.react.bridge.ReactContext
import com.facebook.react.bridge.WritableMap
import com.facebook.react.modules.core.DeviceEventManagerModule

class TextImageInputView(context: Context) : AppCompatEditText(context) {

  init {
    this.addTextChangedListener(object : TextWatcher {
      override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }

      override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        val event = Arguments.createMap().apply {
          putString("text", s.toString())
        }
        sendEvent("onChange", event)
      }

      override fun afterTextChanged(s: Editable?) { }
    })

    this.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
      if (hasFocus) {
        sendEvent("onFocus", null)
      } else {
        sendEvent("onBlur", null)
      }
    }
  }

  private fun sendEvent(eventName: String, params: WritableMap?) {
    val reactContext = context as ReactContext
    reactContext
      .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter::class.java)
      .emit(eventName, params)
  }
}
