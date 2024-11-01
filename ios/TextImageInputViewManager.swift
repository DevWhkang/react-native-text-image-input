import React

@objc(TextImageInputViewManager)
class TextImageInputViewManager: RCTViewManager {

  override func view() -> (TextImageInputView) {
    return TextImageInputView()
  }

  @objc override static func requiresMainQueueSetup() -> Bool {
    return false
  }

  @objc func insertImage(_ node: NSNumber, imageUrl url: String) {
    DispatchQueue.main.async {
      guard let view = self.bridge.uiManager.view(forReactTag: node) as? TextImageInputView else { return }
      view.insertImage(url)
    }
  }

  @objc func dismissKeyboard(_ node: NSNumber) {
    DispatchQueue.main.async {
      guard let view = self.bridge.uiManager.view(forReactTag: node) as? TextImageInputView else { return }
      view.inputView = UIView()
      view.reloadInputViews()
    }
  }

  @objc func blur(_ node: NSNumber) {
    DispatchQueue.main.async {
      guard let view = self.bridge.uiManager.view(forReactTag: node) as? TextImageInputView else { return }
      view.endEditing(true)
    }
  }

  @objc func focus(_ node: NSNumber) {
    DispatchQueue.main.async {
      guard let view = self.bridge.uiManager.view(forReactTag: node) as? TextImageInputView else { return }
      view.becomeFirstResponder()
      view.inputView = nil
      view.reloadInputViews()
    }
  }
}

class TextImageInputView: UITextView {
  @objc var onFocus: RCTBubblingEventBlock?
  @objc var onBlur: RCTBubblingEventBlock?
  @objc var onChange: RCTBubblingEventBlock?

  override init(frame: CGRect, textContainer: NSTextContainer?) {
    super.init(frame: frame, textContainer: textContainer)
    self.delegate = self
  }

  required init?(coder: NSCoder) {
    super.init(coder: coder)
    self.delegate = self
  }

  @objc var color: String = "" {
    didSet {
      self.textColor = hexStringToUIColor(hexColor: color) // 글씨 색상 설정
    }
  }

  @objc var fontSize: NSNumber = 16 {
    didSet {
      self.font = UIFont.systemFont(ofSize: CGFloat(truncating: fontSize))
    }
  }

  func hexStringToUIColor(hexColor: String) -> UIColor {
    let stringScanner = Scanner(string: hexColor)
    if hexColor.hasPrefix("#") {
        stringScanner.currentIndex = hexColor.index(after: hexColor.startIndex)
    }
    var color: UInt64 = 0
    stringScanner.scanHexInt64(&color)

    let r = CGFloat(Int(color >> 16) & 0x000000FF)
    let g = CGFloat(Int(color >> 8) & 0x000000FF)
    let b = CGFloat(Int(color) & 0x000000FF)

    return UIColor(red: r / 255.0, green: g / 255.0, blue: b / 255.0, alpha: 1)
  }

  func insertImage(_ url: String) {
    guard let url = URL(string: url) else { return }

    let task = URLSession.shared.dataTask(with: url) { [weak self] (data, response, error) in
      guard let data = data, let image = UIImage(data: data) else {
        print("load image failed: \(String(describing: error))")
        return
      }
      self?.insertImage(image)
    }
    task.resume()
  }

  private func insertImage(_ image: UIImage) {
    DispatchQueue.main.async {
      // 현재 커서 위치를 저장
      let currentCursorLocation = self.selectedRange.location

      // 현재 텍스트 속성을 유지한 새로운 attributedString 생성
      let attributedString = NSMutableAttributedString(attributedString: self.attributedText)

      // NSTextAttachment를 사용하여 이미지 삽입
      let imageAttachment = NSTextAttachment()
      imageAttachment.image = image

      // 이미지 크기 설정 (텍스트 높이에 맞춰 조정 가능)
      let imageHeight = self.font?.lineHeight ?? 20

      // 폰트의 capHeight를 사용해 y 위치 조정
      let yOffset = -(self.font?.capHeight ?? 0) / 2 // capHeight의 절반을 음수로 설정
      imageAttachment.bounds = CGRect(x: 0, y: yOffset, width: imageHeight, height: imageHeight)

      // 이미지와 텍스트 속성 유지
      let imageString = NSAttributedString(attachment: imageAttachment)
      attributedString.insert(imageString, at: currentCursorLocation)

      // 현재 폰트 설정을 유지하면서 attributedText 업데이트
      let fullRange = NSRange(location: 0, length: attributedString.length)
      attributedString.addAttribute(.font, value: self.font!, range: fullRange)

      // UITextView에 갱신된 문자열 설정
      self.attributedText = attributedString

      // 커서를 이미지 뒤로 이동
      self.selectedRange = NSRange(location: currentCursorLocation + 1, length: 0)
    }
  }

}

extension TextImageInputView: UITextViewDelegate {
  func textViewDidBeginEditing(_ textView: UITextView) {
    onFocus?(["target": reactTag as Any])
  }

  func textViewDidEndEditing(_ textView: UITextView) {
    onBlur?(["target": reactTag as Any])
  }

  func textViewDidChange(_ textView: UITextView) {
    onChange?(["text": textView.text ?? "", "target": reactTag as Any])
  }
}
