package ch.alv.batches.connect.writer;

import org.springframework.batch.item.ItemWriter;

import java.io.File;
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

    public LocalDiskFileWriter(String localFolder) {
        this.localFolder = localFolder;
    }

    @Override
    public void write(List<? extends File> list) throws Exception {
        for (File file : list) {
            Files.copy(file.toPath(), Paths.get(getLocalFolder() + "/" + file.getName()));
        }
    }

    public String getLocalFolder() {
        return localFolder;
    }

}
