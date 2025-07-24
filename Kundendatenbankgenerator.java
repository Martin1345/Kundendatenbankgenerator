import java.awt.Desktop; // Enthält die Mehode zur automatischen Öffnung nach Speicherung der Datei
import java.io.*; // Enthält die Methoden zum Schreiben in eine Datei
import java.util.*;

public class Kundendatenbankgenerator {
 private static final int Kundenanzahl = 23480; // Anzahl der zu generierenden Kunden
    private static final String Dateiname = "Kundendatenbank.rtf"; // Name unter dem die Datei gespeichert wird
    private static final String Zeichensatz = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz"; // Erlaubte Zeichen für den Zufallsgenerator
    private static final Random random = new Random(); // Zufallsgenerator zur Generierung von zufälligen Worten und Zahlen
    private static final int Schriftgroesse = 6; // Schriftgröße für die RTF-Datei; so kleinm, damit Kundendaten in einer Zeile passen

    
    private static String generiere_Zufallswort(int minLaenge, int maxLaenge) {// Methode zur Generierung eines zufälligen Wortes 
     //mit einer Länge zwischen minLaenge und maxLaenge
        int Laenge = random.nextInt(maxLaenge - minLaenge + 1) + minLaenge; // Zufällige Länge des Strings zwischen minLaenge und maxLaenge
        StringBuilder sb = new StringBuilder(); // StringBuilder zur Erstellung des Strings
        for (int i = 0; i < Laenge; i++) { // Solange die zufällige Länge noch nicht erreicht wurde
            sb.append(Zeichensatz.charAt(random.nextInt(Zeichensatz.length()))); // Füge ein zufälliges Zeichen des Zeichensatzes hinzu
        }
        return sb.toString(); // Sobald die Länge erreicht wurde, gebe den String zurück
    }

    private static String generiere_Name() {// Erstellung eines zufälligen Vor- und Nachnamens
        return generiere_Zufallswort(5, 10) + " " + generiere_Zufallswort(5, 10); // Generiere einen Vor- und Nachnamen mit jeweils 5 bis 10 Buchstaben getrennt durch Leerzeichen
    }

    private static String generiere_Strasse() {// Generiere eine zufällige Straße mit Hausnummer
        return generiere_Zufallswort(8, 15) + " " + (random.nextInt(100) + 1); // Generiere einen Straßennamen mit 8 bis 15 Zeichen und eine Hausnummer im Bereich 1 bis 100
    }


    private static String generiere_Stadt() {// Generierung einer zufälligen Stadt mit 6 bis 12 Zeichen
        return generiere_Zufallswort(6, 12); // Generiere einen Namen für die Stadt mit 6 bis 12 Zeichen
    }

    
    private static String Zufallszahl(int Laenge) {// Erzeugung einer Folge von Ganzzahlen beliebiger Länge
        StringBuilder sb = new StringBuilder(); // Neuer StringBuilder zur Erschaffung von Zufallszahlen
        for (int i = 0; i < Laenge; i++) { // Solange die geforderte Länge noch nicht erreicht wurde
            sb.append(random.nextInt(10)); // Füge eine zufällige Ziffer zwischen 0 und 9 hinzu
        }
        return sb.toString(); // Rückgabe des Strings aus Ziffern
    }

    // Baue eine deutsche IBAN aus Zufallszahlen
    private static String generiere_IBAN() {
        return "DE" + Zufallszahl(2) + " " + Zufallszahl(4) + " " + Zufallszahl(4) + " "
                + Zufallszahl(4) + " " + Zufallszahl(4) + " " + Zufallszahl(2); // Generierung einer deutschen IBAN mit 22 Ziffern, aufgeteilt in 5 Teile   
    }

    private static List<String[]> generiere_Kundendatenbank() {// Erstellung der Kundendatenbank mit der festgelegten Anazahl an Zufallskunden
      List<String[]> Datenbank = new ArrayList<>(Kundenanzahl); // Erstelle eine Liste zur Speicherung sofort mit der festgelegten Anzahl an Kunden

        for (int i = 0; i < Kundenanzahl; i++) { // Solange die festgelegte Anzahl an Kunden nicht erreicht wurde
            String Name = generiere_Name(); // Generiere Namen
            String Email = Name.toLowerCase().replace(" ", ".") + "@example.com"; // Generiere eine E-Mail-Adresse der Form Vorname.Nachname@example.com
            String IBAN = generiere_IBAN(); // Generiere IBAN
            String Strasse = generiere_Strasse(); // Generiere Straße
            String PLZ = String.format("%05d", 1000 + random.nextInt(90000)); // Generiere eine 5-stellige PLZ nach dem Format 10000 bis 99999
            String Stadt = generiere_Stadt(); // Generiere Stadt

            Datenbank.add(new String[]{Name, Email, IBAN, Strasse, PLZ, Stadt}); // Füge den Datensatz zur Datenbank hinzu
        }
        return Datenbank; // Rückgabe der vollständig generierten Datenbank
    }

    // Schreibe die Kundendaten in eine RTF-Datei mit einstellbarer Schriftgröße
    private static void speichere_als_RTF(List<String[]> Datenbank) {
        

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(Dateiname))) {
            writer.write("{\\rtf1\\ansi\\deff0"); // RTF-Kopf für Standardformat
            writer.write("{\\fonttbl{\\f0 Courier New;}}"); // Definierte Schriftart
            writer.write("\\fs" + (Schriftgroesse * 2)); // Schriftgröße setzen (RTF rechnet in halben Punkten)

            for (String[] Kunde : Datenbank) { // Durchlaufe jeden Datensatz
                String Name = Kunde[0].length() > 30 ? Kunde[0].substring(0, 30) + "..." : Kunde[0]; 
                // Kürze Namen falls länger als 30 Zeichen
                String Email = Kunde[1]; // E-Mail-Adresse übernehmen
                String IBAN = Kunde[2]; // IBAN übernehmen
                String Adresse = Kunde[3] + ", " + Kunde[4] + " " + Kunde[5]; // Setze Adresse zusammen aus Straße, PLZ und Stadt

                writer.write(Name + ";" + Email + ";" + IBAN + ";" + Adresse + "\\line\n"); //Schreibe die Daten in die RTF-Datei generiere auch eine Zeilenumbruch nach jedem Datensatz
            }

            writer.write("}"); // RTF-Ende
            System.out.println("RTF-Datei gespeichert: " + Dateiname); // Gib bei erfolgreicher Speicherung eine Erfolgsmeldung aus

            Desktop.getDesktop().open(new File(Dateiname)); // Öffnet die RTF-Datei automatisch nach dem Speichern, falls der Desktop-Support verfügbar ist falls nicht, wird eine Fehlermeldung ausgegeben
        } catch (IOException e) {
            System.err.println("Fehler beim Schreiben der RTF-Datei: " + e.getMessage()); // Falls die RTF-Datei nicht geschrieben werden kann, wird eine Fehlermeldung ausgegeben
        }
    }

    // Hauptmethode: Startpunkt des Programms
    public static void main(String[] args) {
        List<String[]> Datenbank = generiere_Kundendatenbank(); // Erzeuge Kundendatenbank
        speichere_als_RTF(Datenbank); // Speichere als RTF-Datei mit Formatierung
    }
}

