package com.textimageinput

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.Build

import android.widget.EditText

import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod

import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ImageSpan
import android.text.style.DynamicDrawableSpan
import android.view.inputmethod.InputMethodManager

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

import androidx.annotation.RequiresApi
import com.facebook.react.uimanager.UIManagerModule
import java.net.URL

class TextImageInputModule(private val reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext) {
  override fun getName() = "TextImageInputViewManager"

  @ReactMethod
  fun focus(node: Int) {
      val editText = getView(node)
      editText?.let {
          reactContext.runOnUiQueueThread {
              it.requestFocus()
              val imm = reactContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
              imm.showSoftInput(it, InputMethodManager.SHOW_IMPLICIT)
          }
      }
  }

  @ReactMethod
  fun blur(node: Int) {
    val editText = getView(node)
    editText?.let {
      reactContext.runOnUiQueueThread {
        val imm = reactContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(it.windowToken, 0)

        it.clearFocus()
      }
    }
  }

  @ReactMethod
  fun dismissKeyboard(node: Int) {
    val editText = getView(node)
    editText?.let {
      reactContext.runOnUiQueueThread {
        val imm = reactContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(it.windowToken, 0)
      }
    }
  }

  @RequiresApi(Build.VERSION_CODES.Q)
  @ReactMethod
  fun insertImage(node: Int, imageUrl: String) {
    // val context = currentActivity ?: return
    val editText = reactContext.getNativeModule(UIManagerModule::class.java)?.resolveView(node) as? EditText ?: return

    CoroutineScope(Dispatchers.IO).launch {
      try {
        val bitmap = downloadImage(imageUrl)
        withContext(Dispatchers.Main) {
          editText.insertImage(bitmap)
        }
      } catch (e: Exception) {
        e.printStackTrace()
      }
    }
  }

  private fun downloadImage(url: String): Bitmap {
    val connection = URL(url).openConnection()
    connection.connect()
    return BitmapFactory.decodeStream(connection.getInputStream())
  }

  private fun getView(node: Int): EditText? {
    return reactContext.getNativeModule(UIManagerModule::class.java)
      ?.resolveView(node) as? EditText
  }
}

@RequiresApi(Build.VERSION_CODES.Q)
fun EditText.insertImage(bitmap: Bitmap) {
  val lineHeight = this.lineHeight

  val drawable = BitmapDrawable(resources, bitmap).apply {
    setBounds(0, 0, lineHeight - 6, lineHeight - 6)
  }

  val imageSpan = ImageSpan(drawable, DynamicDrawableSpan.ALIGN_CENTER)

  val cursorPosition = selectionStart

  val spannableString = SpannableStringBuilder(text).apply {
    insert(cursorPosition, " ")
    setSpan(
      imageSpan,
      cursorPosition,
      cursorPosition + 1,
      Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
    )
  }

  text = spannableString

  setSelection(cursorPosition + 1)
}

//fun EditText.insertImage(bitmap: Bitmap) {
//  // 빈 텍스트일 경우, 렌더링 완료를 대기
//  if (text.isEmpty()) {
//    viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
//      override fun onPreDraw(): Boolean {
//        // 렌더링 완료 시 리스너 제거 후 이미지 삽입
//        viewTreeObserver.removeOnPreDrawListener(this)
//        insertImageInternal(bitmap)
//        return true
//      }
//    })
//  } else {
//    // 텍스트가 있을 경우 바로 삽입
//    insertImageInternal(bitmap)
//  }
//}
//
//private fun EditText.insertImageInternal(bitmap: Bitmap) {
//  val textPaint = this.paint
//  val fontMetrics = textPaint.fontMetricsInt
//  val drawable = BitmapDrawable(resources, bitmap)
//
//  // 이미지의 bounds를 텍스트의 ascent와 bottom 값에 맞게 설정
//  val imageHeight = fontMetrics.descent - fontMetrics.ascent
//  drawable.setBounds(0, 0, imageHeight, imageHeight)
//
//  // ImageSpan을 사용하여 이미지 삽입
//  val imageSpan = ImageSpan(drawable)
//
//  // 현재 커서 위치를 저장
//  val cursorPosition = selectionStart
//
//  // 기존 텍스트와 스타일을 유지한 상태로 이미지 삽입
//  val spannableString = SpannableStringBuilder(text).apply {
//    insert(cursorPosition, " ") // 빈 공백 추가
//    setSpan(
//      imageSpan,
//      cursorPosition,
//      cursorPosition + 1,
//      Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
//    )
//  }
//
//  // EditText에 갱신된 문자열 설정
//  text = spannableString
//
//  // 커서를 이미지 뒤로 이동
//  setSelection(cursorPosition + 1)
//}
