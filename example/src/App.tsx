import { Button, StyleSheet, View } from 'react-native';
import TextImageInputView, {
  TextImageInputViewRef,
} from 'react-native-text-image-input';

import { useCallback, useRef } from 'react';

export default function App() {
  const textImageInputViewRef = useRef<TextImageInputViewRef>(null);

  const onChange = useCallback((event: { nativeEvent: { text: string } }) => {
    console.log(event.nativeEvent.text);
  }, []);

  const onFocus = useCallback(() => {
    console.log('Focus');
  }, []);

  const onBlur = useCallback(() => {
    console.log('Blur');
  }, []);

  return (
    <View style={styles.container}>
      <TextImageInputView
        ref={textImageInputViewRef}
        color={'black'}
        fontSize={20}
        style={styles.box}
        onChange={onChange}
        onFocus={onFocus}
        onBlur={onBlur}
      />
      <Button
        title="이미지 삽입"
        onPress={() =>
          textImageInputViewRef.current?.insertImage(
            'https://beta-static.sooplive.com/beta-soop/emoticons/Basic/starkthumb.png'
          )
        }
      />
      <View style={{ flexDirection: 'row', gap: 8 }}>
        <Button
          title="focus"
          onPress={() => textImageInputViewRef.current?.focus()}
        />
        <Button
          title="blur"
          onPress={() => textImageInputViewRef.current?.blur()}
        />
      </View>
      <View style={{ gap: 8 }}>
        <Button
          title="focus without keyboard"
          onPress={() => textImageInputViewRef.current?.focusWithoutKeyboard()}
        />
        <Button
          title="present keyboard"
          onPress={() => textImageInputViewRef.current?.presentKeyboard()}
        />
        <Button
          title="dismiss keyboard"
          onPress={() => textImageInputViewRef.current?.dismissKeyboard()}
        />
      </View>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'center',
    paddingTop: 150,
    backgroundColor: 'green',
    paddingHorizontal: 60,
  },
  box: {
    width: 200,
    minHeight: 60,
    marginVertical: 20,
    backgroundColor: 'white',
  },
});
