package com.textimageinput

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.text.Editable
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.TextWatcher
import android.text.style.ImageSpan
import android.text.style.DynamicDrawableSpan
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.AppCompatEditText
import com.facebook.react.bridge.Arguments
import com.facebook.react.bridge.ReactContext
import com.facebook.react.modules.core.DeviceEventManagerModule

class TextImageInputView(context: Context) : AppCompatEditText(context) {

  init {
    setFocusListener()
    setTextChangeListener()
  }

  fun focus() {
    requestFocus()
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
  }

  fun dismissKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
  }

  fun insertImage(bitmap: Bitmap) {
    val lineHeight = this.lineHeight
    val drawable = BitmapDrawable(resources, bitmap).apply {
      setBounds(0, 0, lineHeight - 6, lineHeight - 6)
    }

    val imageSpan = ImageSpan(drawable, DynamicDrawableSpan.ALIGN_CENTER)
    val cursorPosition = selectionStart
    val spannableString = SpannableStringBuilder(text).apply {
      insert(cursorPosition, " ")
      setSpan(imageSpan, cursorPosition, cursorPosition + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    }

    text = spannableString
    setSelection(cursorPosition + 1)
  }

  private fun setFocusListener() {
    setOnFocusChangeListener { _, hasFocus ->
      val eventName = if (hasFocus) "onFocus" else "onBlur"
      sendEvent(eventName)
    }
  }

  private fun setTextChangeListener() {
    addTextChangedListener(object : TextWatcher {
      override fun afterTextChanged(s: Editable?) {
        sendEvent("onChange", s.toString())
      }
      override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
      override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    })
  }

  private fun sendEvent(eventName: String, text: String? = null) {
    val reactContext = context as? ReactContext ?: return
    val params = Arguments.createMap().apply {
      if (text != null) putString("text", text)
    }
    reactContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter::class.java)
      .emit(eventName, params)
  }
}
