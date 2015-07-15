import opendial.DialogueSystem;
import opendial.domains.Domain;
import opendial.readers.XMLDomainReader;
import opendial.Settings;

public class OpenDialDemo {
    public static void main (String[] args) {
        // creating the dialogue system
        DialogueSystem system = new DialogueSystem();

        // Extracting the dialogue domain
        Domain domain = XMLDomainReader.extractDomain(Paths.xmlDomainPath);
        system.changeDomain(domain);

        Settings setting = system.getSettings();
        setting.params.put("acousticmodel", Paths.acousticModelFolderPath);
        setting.params.put("dictionary", Paths.dictionaryPath);
        setting.params.put("grammar", Paths.grammarPath);
        system.changeSettings(setting);

        // Adding new domain modules (optional)
        system.attachModule(new MaryTTS(system));
        system.attachModule(new SphinxASR(system));

        // When used as part of another application, we often want to switch off the OpenDial GUI
        //system.getSettings().showGUI = false;

        // Finally, start the system
        system.startSystem();
    }
}