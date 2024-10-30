import {
  forwardRef,
  useImperativeHandle,
  useRef,
  useCallback,
  type Component,
} from 'react';
import {
  requireNativeComponent,
  type ViewStyle,
  NativeModules,
  findNodeHandle,
  type NativeMethods,
} from 'react-native';

const { TextImageInputViewManager } = NativeModules;

type TextImageInputProps = {
  color: string;
  fontSize: number;
  style: ViewStyle;
};

const ComponentName = 'TextImageInputView';

const NativeTextImageInputViewComponent =
  requireNativeComponent<TextImageInputProps>(ComponentName);

export interface TextImageInputViewRef {
  insertImage: (imageUrl: string) => void;
}

const TextImageInputView = forwardRef<
  TextImageInputViewRef,
  TextImageInputProps
>((props, ref) => {
  const internalRef = useRef<Component<TextImageInputProps> & NativeMethods>(
    null
  );

  const insertImage = useCallback((imageUrl: string) => {
    const node = findNodeHandle(internalRef.current);
    if (node) {
      TextImageInputViewManager.insertImage(node, imageUrl);
    }
  }, []);

  useImperativeHandle(
    ref,
    () => ({
      insertImage,
    }),
    [insertImage]
  );

  return <NativeTextImageInputViewComponent {...props} ref={internalRef} />;
});

export default TextImageInputView;
