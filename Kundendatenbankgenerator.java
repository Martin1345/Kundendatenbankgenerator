import java.awt.Desktop; // Zum automatischen Öffnen der RTF-Datei nach dem Speichern
import java.io.*; // Klasse zum schreiben der RTF-Datei
import java.util.*;

public class Kundendatenbankgenerator {
 private static final int Kundenanzahl = 23480; // Anzahl der zu generierenden Kunden
    private static final String Dateiname = "Kundendatenbank.rtf"; // Name der Zieldatei
    private static final String Zeichensatz = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz"; // Erlaubte Zeichen für den Zufallsgenerator
    private static final Random random = new Random(); // Zufallsgenerator zur Generierung von zufälligen Worten und Zahlen
    private static final int schriftgroesse = 6; // Schriftgröße für die RTF-Datei

    // Generiere ein Zufallswort mit variabler Länge
    private static String generiereZufallswort(int minLaenge, int maxLaenge) {
        int Laenge = random.nextInt(maxLaenge - minLaenge + 1) + minLaenge; // Zufällige Länge des Strings zwischen minLaenge und maxLaenge
        StringBuilder sb = new StringBuilder(); // StringBuilder zur Erstellung des Strings
        for (int i = 0; i < Laenge; i++) { // Solange die zufällige Länge noch nicht erreicht wurde
            sb.append(Zeichensatz.charAt(random.nextInt(Zeichensatz.length()))); // Füge ein zufälliges Zeichen des Zeichensatzes hinzu
        }
        return sb.toString(); // Sobald die Länge erreicht wurde, gebe den String zurück
    }

    // Erstelle einen vollständigen Namen aus Vor- und Nachname
    private static String generiere_Name() {
        return generiereZufallswort(5, 10) + " " + generiereZufallswort(5, 10); // Generiere einen Vor- und Nachnamen mit jeweils 5 bis 10 Buchstaben getrennt durch Leerzeichen
    }

    // Generiere zufälligen Straßennamen und Hausnummer
    private static String generiere_Strasse() {
        return generiereZufallswort(8, 15) + " " + (random.nextInt(100) + 1); // Generiere einen Straßennamen mit 8 bis 15 Zeichen und eine Hausnummer im Bereich 1 bis 100
    }

    // Generiere einen zufälligen Stadtnamen
    private static String generiere_Stadt() {
        return generiereZufallswort(6, 12); // Generiere einen Namen für die Stadt mit 6 bis 12 Zeichen
    }

    // Erzeuge eine Ziffernfolge der gewünschten Länge
    private static String Zufallszahl(int Laenge) {
        StringBuilder sb = new StringBuilder(); // Neuer StringBuilder zur Erschaffung von Zufallszahlen
        for (int i = 0; i < Laenge; i++) { // Solange die geforderte Länge noch nicht erreicht wurde
            sb.append(random.nextInt(10)); // Füge eine zufällige Ziffer zwischen 0 und 9 hinzu
        }
        return sb.toString(); // Rückgabe des Strings aus Ziffern
    }

    // Baue eine deutsche IBAN aus Zufallszahlen
    private static String generiere_IBAN() {
        return "DE" + Zufallszahl(2) + " " + Zufallszahl(4) + " " + Zufallszahl(4) + " "
                + Zufallszahl(4) + " " + Zufallszahl(4) + " " + Zufallszahl(2); // Generierung einer IBAN nach dem deutschen IBAN-Muster
    }

    // Erstelle eine Liste mit zufällig generierten Kundendatensätzen
    private static List<String[]> generiere_Kundendatenbank() {
      List<String[]> Datenbank = new ArrayList<>(Kundenanzahl); // Erstelle ArrayList mit anfänglicher Kapazität Kundenanzahl

        for (int i = 0; i < Kundenanzahl; i++) { // Solange die festgelegte Anzahl an Kunden nicht erreicht wurde
            String Name = generiere_Name(); // Generiere Namen
            String Email = Name.toLowerCase().replace(" ", ".") + "@example.com"; // Generiere eine E-Mail-Adresse der Form Vorname.Nachname@example.com
            String IBAN = generiere_IBAN(); // Generiere IBAN
            String Strasse = generiere_Strasse(); // Generiere Straße
            String PLZ = String.format("%05d", 1000 + random.nextInt(90000)); // Generiere eine 5-stellige PLZ
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
            writer.write("\\fs" + (schriftgroesse * 2)); // Schriftgröße setzen (RTF rechnet in halben Punkten)

            for (String[] Kunde : Datenbank) { // Durchlaufe jeden Datensatz
                String Name = Kunde[0].length() > 30 ? Kunde[0].substring(0, 30) + "..." : Kunde[0]; 
                // Kürze Namen ggf.
                String Email = Kunde[1]; // E-Mail-Adresse übernehmen
                String IBAN = Kunde[2]; // IBAN übernehmen
                String Adresse = Kunde[3] + ", " + Kunde[4] + " " + Kunde[5]; // Adresse zusammenbauen

                writer.write(Name + ";" + Email + ";" + IBAN + ";" + Adresse + "\\line\n"); // Eintrag und Zeilenumbruch (RTF-konform)
            }

            writer.write("}"); // RTF-Ende
            System.out.println("RTF-Datei gespeichert: " + Dateiname); // Erfolgsmeldung bei erfolgreicher Speicherung

            Desktop.getDesktop().open(new File(Dateiname)); // Öffnet die Datei automatisch im Standardeditor
        } catch (IOException e) {
            System.err.println("Fehler beim Schreiben der RTF-Datei: " + e.getMessage()); // Fehlermeldung bei fehlgeschlagener Speicherung
        }
    }

    // Hauptmethode: Startpunkt des Programms
    public static void main(String[] args) {
        List<String[]> Datenbank = generiere_Kundendatenbank(); // Erzeuge Kundendatenbank
        speichere_als_RTF(Datenbank); // Speichere als RTF-Datei mit Formatierung
    }
}

