package com.textimageinput

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import com.facebook.react.bridge.Arguments
import com.facebook.react.uimanager.ThemedReactContext
import com.facebook.react.uimanager.events.RCTEventEmitter


class TextImageInputView(context: Context) : AppCompatEditText(context) {

  init {
    this.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
      val reactContext = context as ThemedReactContext
      if (hasFocus) {
        reactContext.getJSModule(RCTEventEmitter::class.java).receiveEvent(
          id,
          "onFocus",
          Arguments.createMap()
        )
      } else {
        reactContext.getJSModule(RCTEventEmitter::class.java).receiveEvent(
          id,
          "onBlur",
          Arguments.createMap()
        )
      }
    }

    this.addTextChangedListener(object : TextWatcher {
      override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

      override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        val reactContext = context as ThemedReactContext
        val event = Arguments.createMap().apply {
          putString("text", s.toString())
        }
        reactContext.getJSModule(RCTEventEmitter::class.java).receiveEvent(
          id,
          "onChange",
          event
        )
      }

      override fun afterTextChanged(s: Editable?) {}
    })
  }
}
