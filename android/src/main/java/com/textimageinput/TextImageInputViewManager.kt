package com.textimageinput

import android.graphics.BitmapFactory
import android.os.Build
import androidx.annotation.RequiresApi
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactMethod
import com.facebook.react.uimanager.SimpleViewManager
import com.facebook.react.uimanager.ThemedReactContext
import com.facebook.react.uimanager.UIManagerModule
import com.facebook.react.uimanager.annotations.ReactProp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.URL

class TextImageInputViewManager(private val reactContext: ReactApplicationContext) : SimpleViewManager<TextImageInputView>() {

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

  @ReactMethod
  fun focusTextInput(node: Int) {
    val view = getView(node)
    view?.focus()
  }

  @ReactMethod
  fun dismissKeyboard(node: Int) {
    val view = getView(node)
    view?.dismissKeyboard()
  }

  @RequiresApi(Build.VERSION_CODES.Q)
  @ReactMethod
  fun insertImage(node: Int, imageUrl: String) {
    val view = getView(node)
    CoroutineScope(Dispatchers.IO).launch {
      try {
        val bitmap = downloadImage(imageUrl)
        withContext(Dispatchers.Main) {
          view?.insertImage(bitmap)
        }
      } catch (e: Exception) {
        e.printStackTrace()
      }
    }
  }

  private fun downloadImage(url: String) = URL(url).openConnection().apply { connect() }.getInputStream().use { BitmapFactory.decodeStream(it) }

  private fun getView(node: Int): TextImageInputView? {
    return reactContext.getNativeModule(UIManagerModule::class.java)
      ?.resolveView(node) as? TextImageInputView
  }
}
