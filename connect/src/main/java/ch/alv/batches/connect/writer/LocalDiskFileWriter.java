package ch.alv.batches.connect.writer;

import org.springframework.batch.item.ItemWriter;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * Saves copies of the given Files into a configurable locale folder.
 *
 * @since: 1.0.0
 */
public class LocalDiskFileWriter implements ItemWriter<File> {

    private final String localFolder;

    public LocalDiskFileWriter(String localFolder) throws IOException {
        this.localFolder = localFolder;
        File folder = new File(localFolder);
        if (!folder.exists()) {
            folder.mkdirs();
        }
    }

    @Override
    public void write(List<? extends File> list) throws Exception {
        for (File file : list) {
            try {
                Files.copy(file.toPath(), Paths.get(getLocalFolder() + "/" + file.getName()));
            } catch (FileAlreadyExistsException e) {
                // TODO handle exception
            }

        }
    }

    public String getLocalFolder() {
        return localFolder;
    }

}
