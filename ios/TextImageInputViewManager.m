#import <React/RCTViewManager.h>

@interface RCT_EXTERN_MODULE(TextImageInputViewManager, RCTViewManager)

// properties
RCT_EXPORT_VIEW_PROPERTY(color, NSString)
RCT_EXPORT_VIEW_PROPERTY(fontSize, NSNumber)

// callback
RCT_EXPORT_VIEW_PROPERTY(onFocus, RCTDirectEventBlock)
RCT_EXPORT_VIEW_PROPERTY(onBlur, RCTDirectEventBlock)
RCT_EXPORT_VIEW_PROPERTY(onChange, RCTDirectEventBlock)

// ref
RCT_EXTERN_METHOD(insertImage:(nonnull NSNumber *)node imageUrl:(NSString *)url)
RCT_EXTERN_METHOD(dismissKeyboard:(nonnull NSNumber *)node)
RCT_EXTERN_METHOD(focusTextInput:(nonnull NSNumber *)node)

@end
