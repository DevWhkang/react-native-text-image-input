import {
  requireNativeComponent,
  UIManager,
  Platform,
  type ViewStyle,
  NativeModules,
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

const TextImageInputView =
  UIManager.getViewManagerConfig(ComponentName) != null
    ? requireNativeComponent<TextImageInputProps>(ComponentName)
    : () => {
        throw new Error(LINKING_ERROR);
      };

export function insertImage(imageUrl: string): void {
  TextImageInputViewManager.insertImage(imageUrl);
}

export default TextImageInputView;
