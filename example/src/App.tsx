import { Button, StyleSheet, View } from 'react-native';
import TextImageInputView, {
  TextImageInputViewRef,
} from 'react-native-text-image-input';

import { useRef } from 'react';

export default function App() {
  const textImageInputViewRef = useRef<TextImageInputViewRef>(null);

  return (
    <View style={styles.container}>
      <TextImageInputView
        ref={textImageInputViewRef}
        color={'black'}
        fontSize={20}
        style={styles.box}
      />
      <Button
        title="이미지 삽입"
        onPress={() =>
          textImageInputViewRef.current?.insertImage(
            'https://beta-static.sooplive.com/beta-soop/emoticons/Basic/starkthumb.png'
          )
        }
      />
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
    backgroundColor: 'green',
    paddingHorizontal: 60,
  },
  box: {
    width: '100%',
    minHeight: 60,
    marginVertical: 20,
    backgroundColor: 'white',
  },
});
