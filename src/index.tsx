import React, {
  forwardRef,
  useImperativeHandle,
  useRef,
  useCallback,
} from 'react';
import {
  requireNativeComponent,
  UIManager,
  Platform,
  type ViewStyle,
  NativeModules,
  findNodeHandle,
} from 'react-native';

const { TextImageInputViewManager } = NativeModules;

const LINKING_ERROR =
  `The package 'react-native-text-image-input' doesn't seem to be linked. Make sure: \n\n` +
  Platform.select({ ios: "- You have run 'pod install'\n", default: '' }) +
  '- You rebuilt the app after installing the package\n' +
  '- You are not using Expo Go\n';

type TextImageInputProps = {
  color: string;
  fontSize: number;
  style: ViewStyle;
};

const ComponentName = 'TextImageInputView';

const NativeTextImageInputView =
  UIManager.getViewManagerConfig(ComponentName) != null
    ? requireNativeComponent<TextImageInputProps>(ComponentName)
    : () => {
        throw new Error(LINKING_ERROR);
      };

const TextImageInputView = forwardRef((props: TextImageInputProps, ref) => {
  const internalRef =
    useRef<React.ComponentPropsWithRef<typeof NativeTextImageInputView>>(null);

  const insertImage = useCallback((imageUrl: string) => {
    const node = findNodeHandle(internalRef.current);
    if (node) {
      TextImageInputViewManager.insertImage(node, imageUrl);
    }
  }, []);

  // useImperativeHandle을 통해 외부에서 ref를 사용할 때 insertImage 함수에 접근할 수 있도록 설정
  useImperativeHandle(
    ref,
    () => ({
      insertImage,
    }),
    [insertImage]
  );

  return <NativeTextImageInputView {...props} ref={internalRef} />;
});

export default TextImageInputView;
