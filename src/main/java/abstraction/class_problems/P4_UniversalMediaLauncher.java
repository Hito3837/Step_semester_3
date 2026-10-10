// Universal Media Launcher — IS-A (extends) vs CAN-DO (implements) reasoning.
// AudioFile IS-A MediaFile: it genuinely has a fileId and a format, so inheritance fits.
// Podcast is only CAN-DO Playable: forcing it under MediaFile would saddle it with a fileId
// and getFormatInfo() it has no use for — the interface gives it the shared 'played' behavior
// with none of the wrong state. launchAll() depends only on Playable, so both kinds play
// through one polymorphic loop.

interface Playable {
    String play();

    String play(int fromSecond);

    String pause();
}

abstract class MediaFile {
    private static int nextFile = 1001;
    private final String fileId;

    MediaFile() {
        this.fileId = "MF-" + (nextFile++);
    }

    public abstract String getFormatInfo();

    String getFileId() {
        return fileId;
    }
}

class AudioFile extends MediaFile implements Playable {
    private final String title;

    public AudioFile(String title) {
        super();
        this.title = title;
    }

    @Override
    public String play() {
        return "Playing audio: " + title;
    }

    @Override
    public String play(int fromSecond) {
        return "Playing audio: " + title + " from " + (fromSecond / 60) + ":"
                + String.format("%02d", fromSecond % 60);
    }

    @Override
    public String pause() {
        return "Paused audio: " + title;
    }

    @Override
    public String getFormatInfo() {
        return "Audio file, ID: " + getFileId();
    }
}

class Podcast implements Playable {
    private final String showName;
    private final int episodeNumber;

    public Podcast(String showName, int episodeNumber) {
        this.showName = showName;
        this.episodeNumber = episodeNumber;
    }

    @Override
    public String play() {
        return "Streaming episode " + episodeNumber + " of " + showName;
    }

    @Override
    public String play(int fromSecond) {
        return "Streaming episode " + episodeNumber + " of " + showName + " from "
                + (fromSecond / 60) + ":" + String.format("%02d", fromSecond % 60);
    }

    @Override
    public String pause() {
        return "Paused streaming: " + showName + " episode " + episodeNumber;
    }
}

public class P4_UniversalMediaLauncher {
    static void launchAll(Playable[] items) {
        for (Playable item : items) {
            System.out.println(item.play());
        }
    }

    public static void main(String[] args) {
        AudioFile a = new AudioFile("Morning Jazz");
        System.out.println(a.play());
        System.out.println(a.play(30));
        System.out.println(a.getFormatInfo());

        Podcast p = new Podcast("Tech Talk", 12);
        System.out.println(p.play());

        Playable ref = a; // upcasting: AudioFile stored as the Playable interface type
        System.out.println(ref.play());

        launchAll(new Playable[] { ref, p });
    }
}
