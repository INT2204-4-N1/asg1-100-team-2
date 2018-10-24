
package dictionary;

import GoogleAPI.Audio;
import GoogleAPI.GoogleTranslate;
import GoogleAPI.Language;
import java.io.IOException;
import java.util.TreeMap;
import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javazoom.jl.decoder.JavaLayerException;

/**
 *
 * @author HP
 */
public class dictionary_EV extends Base_dictionary{
    /**
     * khai báo constructor
     * mục đích: cung cấp bộ nhớ cho mảng và đọc danh sách từ vào mảng bằng phương thức insertFormFile()
     */
    public dictionary_EV(){
        Words = new TreeMap();
        path = "E_V";
        insertFromFile(path+".zip");
    }
    @Override
    public void makeVoice(String Word){
        System.setProperty("mbrola.base", "src\\source_voice\\mbrola");
        VoiceManager voice_manager = VoiceManager.getInstance();
        Voice voice = voice_manager.getVoice("mbrola_us1");
        voice.allocate();
        voice.speak(Word);
        voice.deallocate();
    }
    
    @Override
    public void makeVoicebyAPI(String Word){
        try {
               Audio audio = Audio.getInstance();
               InputStream sound = null;
               try {
                   sound = audio.getAudio(Word, Language.ENGLISH);
               } catch (IOException ex) {
                   Logger.getLogger(dic_aplication.class.getName()).log(Level.SEVERE, null, ex);
               }
               audio.play(sound);
           } catch (JavaLayerException ex) {
               Logger.getLogger(dic_aplication.class.getName()).log(Level.SEVERE, null, ex);
           }
    }
    
    @Override
    public String TranslatebyAPI(String Word){
        try {
            String str = GoogleTranslate.translate("en","vi",Word);
            return str;
        } catch (IOException ex) {
            Logger.getLogger(dictionary_EV.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
