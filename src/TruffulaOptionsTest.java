import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

public class TruffulaOptionsTest {

  @Test
  void testValidDirectoryIsSet(@TempDir File tempDir) throws FileNotFoundException {
    // Arrange: Prepare the arguments with the temp directory
    File directory = new File(tempDir, "subfolder");
    directory.mkdir();
    String directoryPath = directory.getAbsolutePath();
    String[] args = {"-nc", "-h", directoryPath};

    // Act: Create TruffulaOptions instance
    TruffulaOptions options = new TruffulaOptions(args);

    // Assert: Check that the root directory is set correctly
    assertEquals(directory.getAbsolutePath(), options.getRoot().getAbsolutePath());
    assertTrue(options.isShowHidden());
    assertFalse(options.isUseColor());
  }

  @Test
  void testValidDirectoryIsSetNoFlags(@TempDir File tempDir) throws FileNotFoundException {
    File directory = new File(tempDir, "subfolder");
    directory.mkdir();
    String directoryPath = directory.getAbsolutePath();
    String[] args = {directoryPath};

    TruffulaOptions options = new TruffulaOptions(args);

    assertEquals(directory.getAbsolutePath(), options.getRoot().getAbsolutePath());
    assertFalse(options.isShowHidden());
    assertTrue(options.isUseColor());
  }

  @Test
  void testMissingPath() {
    String[] args = {};

    assertThrows(IllegalArgumentException.class, () -> new TruffulaOptions(args));
  }

  @Test
  void testUnknownFlag(@TempDir File tempDir) {
    File directory = new File(tempDir, "subfolder");
    directory.mkdir();
    String directoryPath = directory.getAbsolutePath();
    String[] args = {"-buttsbuttsbuttsbutts", directoryPath};

    assertThrows(IllegalArgumentException.class, () -> new TruffulaOptions(args));
  }

  @Test
  void testDirectoryDoesNotExist(@TempDir File tempDir) {
    File aBlackHole = new File(tempDir, "NOOOOO");
    String directoryPath = aBlackHole.getAbsolutePath();
    String[] args = {directoryPath};

    assertThrows(FileNotFoundException.class, () -> new TruffulaOptions(args));
  }

  @Test
  void testDirectoryIsAFile(@TempDir File tempDir) throws IOException {
    File absolutelyNot = new File(tempDir, "not_pornography.jpg");
    absolutelyNot.createNewFile();
    String[] args = {absolutelyNot.getAbsolutePath()};

    assertThrows(FileNotFoundException.class, () -> new TruffulaOptions(args));
  }
}
