/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dictionary;

import GoogleAPI.Audio;
import GoogleAPI.GoogleTranslate;
import GoogleAPI.Language;
import java.io.IOException;
import java.io.InputStream;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javazoom.jl.decoder.JavaLayerException;

/**
 *
 * @author Hoang Dao
 */
public class dictionary_VE extends Base_dictionary{
    public dictionary_VE(){
        Words = new TreeMap();
        path = "V_E";
        insertFromFile(path+".zip");
    }
    @Override
    public void makeVoice(String Word){}
    @Override
    public void makeVoicebyAPI(String Word){
        try {
               Audio audio = Audio.getInstance();
               InputStream sound = null;
               try {
                   sound = audio.getAudio(Word, Language.VIETNAMESE);
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
            String str = GoogleTranslate.translate("vi","en",Word);
            return str;
        } catch (IOException ex) {
            Logger.getLogger(dictionary_EV.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
