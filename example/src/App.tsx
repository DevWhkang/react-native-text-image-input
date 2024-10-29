import { Button, StyleSheet, View } from 'react-native';
import TextImageInputView, { insertImage } from 'react-native-text-image-input';

export default function App() {
  return (
    <View style={styles.container}>
      <TextImageInputView color={'red'} fontSize={20} style={styles.box} />
      <Button
        title="이미지 삽입"
        onPress={() =>
          insertImage(
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
    minHeight: 40,
    marginVertical: 20,
  },
});
