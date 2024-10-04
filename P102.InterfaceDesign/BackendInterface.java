import java.util.List;
import java.io.IOException;

public interface BackendInterface {

    void readData(String filename) throws IOException;

    List<String> getRange(int low, int high);

    List<String> searchBPM(int BPM, int delta);

    int numYear(int year);

}
