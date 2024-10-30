#import <React/RCTViewManager.h>

@interface RCT_EXTERN_MODULE(TextImageInputViewManager, RCTViewManager)

RCT_EXPORT_VIEW_PROPERTY(color, NSString)
RCT_EXPORT_VIEW_PROPERTY(fontSize, NSNumber)

RCT_EXTERN_METHOD(insertImage:(nonnull NSNumber *)node imageUrl:(NSString *)url)

@end
