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
  Pressable,
  StyleSheet,
} from 'react-native';

const { TextImageInputViewManager } = NativeModules;

type TextImageInputProps = {
  // properties
  containerStyle?: ViewStyle;
  style?: ViewStyle;
  color?: string;
  fontSize?: number;

  // callback
  onPress?: () => void;
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
  presentKeyboard: () => void;
  focusWithoutKeyboard: () => void;
  focus: () => void;
  blur: () => void;
}

const TextImageInputView = forwardRef<
  TextImageInputViewRef,
  TextImageInputProps
>(({ containerStyle, onPress, ...props }, ref) => {
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

  const presentKeyboard = useCallback(() => {
    const node = findNodeHandle(internalRef.current);
    if (node) {
      TextImageInputViewManager.presentKeyboard(node);
    }
  }, []);

  const focusWithoutKeyboard = useCallback(() => {
    const node = findNodeHandle(internalRef.current);
    if (node) {
      TextImageInputViewManager.focusWithoutKeyboard(node);
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
      presentKeyboard,
      focusWithoutKeyboard,
      focus,
      blur,
    }),
    [
      insertImage,
      dismissKeyboard,
      presentKeyboard,
      focusWithoutKeyboard,
      focus,
      blur,
    ]
  );

  return (
    <Pressable style={[styles.container, containerStyle]} onPress={onPress}>
      <NativeTextImageInputViewComponent
        {...props}
        ref={internalRef}
        style={{ ...styles.input, ...props.style }}
      />
    </Pressable>
  );
});

export default TextImageInputView;

const styles = StyleSheet.create({
  container: {},
  input: {},
});
