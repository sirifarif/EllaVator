import edu.cmu.sphinx.api.StreamSpeechRecognizer
import edu.cmu.sphinx.api.Configuration;

import javax.sound.sampled.AudioInputStream

import org.testng.annotations.*

import javax.sound.sampled.AudioSystem

import opendial.Settings;

public class SpeechIOTest {

        String recognize(AudioInputStream audio) {
        Configuration configuration = new Configuration();
        configuration.setAcousticModelPath(Paths.acousticModelFolderPath);
        configuration.setDictionaryPath(Paths.dictionaryPath);
        configuration.setGrammarPath(Paths.grammarPath);
        configuration.setUseGrammar(true);
        configuration.setGrammarName(Paths.grammarName);

        StreamSpeechRecognizer recognizer = new StreamSpeechRecognizer(configuration)
        recognizer.startRecognition(audio);
        return recognizer.getResult().getHypothesis()
    }

    @DataProvider
    Object[][] expandedGrammar() {
        return [
            [AudioSystem.getAudioInputStream(getClass().getResourceAsStream('SpeechInput/room-one.wav')), 'room one'],
        ]
    }

    @Test(dataProvider = 'expandedGrammarEnglish')
    void canRecognizeGrammar(AudioInputStream audio, String expected) {
        def actual = recognize(audio)
        assert actual == expected
    }

    @Test
    void CheckFilesInFolder() {
        File folder = new File(Paths.acousticModelFolderPath);
        int mustHaveFilesCount = 10;
        printf("AM folder")
        printf(folder.toString())
//        assertEquals(mustHaveFilesCount, folder.listFiles().length);
        Map<String, Boolean> requiredFiles = new HashMap<String, Boolean>();
        // add all the required files
        requiredFiles.put("file name", false);
        for (File file : folder.listFiles()) {
            if (requiredFiles.containsKey(file.getName())) {
                // if file exists, mark it as true
                requiredFiles.put(file.getName(), true);
            }
        }
        assertFalse(requiredFiles.containsValue(false));	// all files must exist. False must not be found in
        // hash map
    }

    @Test
    public void ValidateDictionary() {
        try {
            FileInputStream fstream = new FileInputStream(Paths.dictionaryPath);
            BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
            String line;
            try {
                while ((line = br.readLine()) != null) {
                    // check the required format, eg. first character lower case, etc.
                    assertFalse(Character.isUpperCase(line.charAt(0)));	//first character must be lower case
                }
            } catch (IOException e) {
                e.printStackTrace();
                fail("Failed to read line. Check StackTrace");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            fail("Dictionary file not found!");
        }
    }
}
