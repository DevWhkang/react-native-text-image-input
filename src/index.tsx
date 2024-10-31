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
  // properties
  style: ViewStyle;
  color: string;
  fontSize: number;

  // callback
  onFocus?: () => void;
  onBlur?: () => void;
  onChange?: (event: { nativeEvent: { text: string } }) => void;
};

const ComponentName = 'TextImageInputView';

const NativeTextImageInputViewComponent =
  requireNativeComponent<TextImageInputProps>(ComponentName);

export interface TextImageInputViewRef {
  insertImage: (imageUrl: string) => void;
  dismissKeyboard: () => void;
  focus: () => void;
  blur: () => void;
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

  const dismissKeyboard = useCallback(() => {
    const node = findNodeHandle(internalRef.current);
    if (node) {
      TextImageInputViewManager.dismissKeyboard(node);
    }
  }, []);

  const focus = useCallback(() => {
    const node = findNodeHandle(internalRef.current);
    if (node) {
      TextImageInputViewManager.focus(node);
    }
  }, []);

  const blur = useCallback(() => {
    const node = findNodeHandle(internalRef.current);
    if (node) {
      TextImageInputViewManager.blur(node);
    }
  }, []);

  useImperativeHandle(
    ref,
    () => ({
      insertImage,
      dismissKeyboard,
      focus,
      blur,
    }),
    [insertImage, dismissKeyboard, focus, blur]
  );

  return (
    <NativeTextImageInputViewComponent
      {...props}
      ref={internalRef}
      onFocus={props.onFocus}
      onBlur={props.onBlur}
      onChange={props.onChange}
    />
  );
});

export default TextImageInputView;
