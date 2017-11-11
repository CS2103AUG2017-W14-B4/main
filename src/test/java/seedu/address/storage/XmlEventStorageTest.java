package seedu.address.storage;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;
import seedu.address.commons.exceptions.DataConversionException;
import seedu.address.commons.util.FileUtil;
import seedu.address.model.EventList;
import seedu.address.model.ReadOnlyEventList;
import seedu.address.model.event.Event;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static seedu.address.testutil.TypicalEvents.FIRST;
import static seedu.address.testutil.TypicalEvents.SEVENTH;
import static seedu.address.testutil.TypicalEvents.EIGHTH;
import static seedu.address.testutil.TypicalEvents.getTypicalEventList;

//@@author LeonChowWenHao

public class XmlEventStorageTest {
    private static final String TEST_DATA_FOLDER = FileUtil.getPath("./src/test/data/XmlEventStorageTest/");

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();

    @Test
    public void readEventStorage_nullFilePath_throwsNullPointerException() throws Exception {
        thrown.expect(NullPointerException.class);
        readEventStorage(null);
    }

    private java.util.Optional<ReadOnlyEventList> readEventStorage(String filePath) throws Exception {
        return new XmlEventStorage(filePath).readEventStorage(addToTestDataPathIfNotNull(filePath));
    }

    private String addToTestDataPathIfNotNull(String prefsFileInTestDataFolder) {
        return prefsFileInTestDataFolder != null
                ? TEST_DATA_FOLDER + prefsFileInTestDataFolder
                : null;
    }

    @Test
    public void read_missingFile_emptyResult() throws Exception {
        assertFalse(readEventStorage("NonExistentFile.xml").isPresent());
    }

    @Test
    public void read_notXmlFormat_exceptionThrown() throws Exception {

        thrown.expect(DataConversionException.class);
        readEventStorage("NotXmlFormatEventStorage.xml");

        /* IMPORTANT: Any code below an exception-throwing line (like the one above) will be ignored.
         * That means you should not have more than one exception test in one method
         */
    }

    @Test
    public void readAndSaveEventStorage_allInOrder_success() throws Exception {
        String filePath = testFolder.getRoot().getPath() + "TempEventStorage.xml";
        EventList original = getTypicalEventList();
        XmlEventStorage xmlEventStorage = new XmlEventStorage(filePath);

        //Save in new file and read back
        xmlEventStorage.saveEventStorage(original, filePath);
        ReadOnlyEventList readBack = xmlEventStorage.readEventStorage(filePath).get();
        assertEquals(original, new EventList(readBack));

        //Modify data, overwrite exiting file, and read back
        original.addEvent(new Event(SEVENTH));
        original.removeEvent(new Event(FIRST));
        xmlEventStorage.saveEventStorage(original, filePath);
        readBack = xmlEventStorage.readEventStorage(filePath).get();
        assertEquals(original, new EventList(readBack));

        //Save and read without specifying file path
        original.addEvent(new Event(EIGHTH));
        xmlEventStorage.saveEventStorage(original); //file path not specified
        readBack = xmlEventStorage.readEventStorage().get(); //file path not specified
        assertEquals(original, new EventList(readBack));

    }

    @Test
    public void saveEventStorage_nullEventStorage_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        saveEventStorage(null, "SomeFile.xml");
    }

    @Test
    public void getPersonList_modifyList_throwsUnsupportedOperationException() {
        XmlSerializableAddressBook addressBook = new XmlSerializableAddressBook();
        thrown.expect(UnsupportedOperationException.class);
        addressBook.getPersonList().remove(0);
    }

    @Test
    public void getTagList_modifyList_throwsUnsupportedOperationException() {
        XmlSerializableAddressBook addressBook = new XmlSerializableAddressBook();
        thrown.expect(UnsupportedOperationException.class);
        addressBook.getTagList().remove(0);
    }

    /**
     * Saves {@code eventList} at the specified {@code filePath}.
     */
    private void saveEventStorage(ReadOnlyEventList eventList, String filePath) {
        try {
            new XmlEventStorage(filePath).saveEventStorage(eventList, addToTestDataPathIfNotNull(filePath));
        } catch (IOException ioe) {
            throw new AssertionError("There should not be an error writing to the file.", ioe);
        }
    }

    @Test
    public void saveEventStorage_nullFilePath_throwsNullPointerException() throws IOException {
        thrown.expect(NullPointerException.class);
        saveEventStorage(new EventList(), null);
    }


}
