package seedu.address.model;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.Objects;

import javafx.collections.ObservableList;
import seedu.address.logic.commands.exceptions.DeleteOnCascadeException;
import seedu.address.model.event.Event;
import seedu.address.model.event.ReadOnlyEvent;
import seedu.address.model.event.UniqueEventList;
import seedu.address.model.event.exceptions.DuplicateEventException;
import seedu.address.model.event.exceptions.EventNotFoundException;
import seedu.address.model.event.exceptions.PersonHaveParticipateException;
import seedu.address.model.event.exceptions.PersonNotParticipateException;
import seedu.address.model.person.Person;
import seedu.address.model.person.ReadOnlyPerson;
import seedu.address.model.person.exceptions.DuplicatePersonException;
import seedu.address.model.person.exceptions.PersonNotFoundException;

//@@author Adoby7
/**
 * Wraps all events at event list level
 */
public class EventList implements ReadOnlyEventList {
    private final UniqueEventList events;

    public EventList() {
        events = new UniqueEventList();
    }

    /**
     * Creates an EventList using the Persons and Tags and events in the {@code toBeCopied}
     */
    public EventList(ReadOnlyEventList toBeCopied) {
        this();
        resetData(toBeCopied);
    }

    public void setEvents(List<? extends ReadOnlyEvent> events) throws DuplicateEventException {
        this.events.setEvents(events);
    }

    /**
     * Resets the existing data of this {@code EventList} with {@code newData}.
     */
    public void resetData(ReadOnlyEventList newData) {
        requireNonNull(newData);
        try {
            setEvents(newData.getEventList());
        } catch (DuplicateEventException e) {
            assert false : "EventList should not have duplicate events";
        }
    }

    /**
     * Adds a event to the event list.
     * Also checks the new event's participants and updates {@link #events} with any new persons found,
     * and updates the Person objects in the event to point to those in {@link #events}.
     *
     * @throws DuplicateEventException if an equivalent person already exists.
     */
    public void addEvent(ReadOnlyEvent e) throws DuplicateEventException {
        Event newEvent = new Event(e);
        events.add(newEvent);
    }

    /**
     * Adds a event to the specific position in list.
     * Only used to undo deletion
     */
    public void addEvent(int position, ReadOnlyEvent e) {
        Event newEvent = new Event(e);
        events.add(position, newEvent);
    }

    /**
     * Replaces the given person {@code target} in the list with {@code editedReadOnlyPerson}.
     * {@code AddressBook}'s tag list will be updated with the tags of {@code editedReadOnlyPerson}.
     *
     * @throws DuplicatePersonException if updating the person's details causes the person to be equivalent to
     *                                  another existing person in the list.
     * @throws PersonNotFoundException  if {@code target} could not be found in the list.
     */
    public void updateEvent(ReadOnlyEvent target, ReadOnlyEvent editedReadOnlyEvent)
            throws DuplicateEventException, EventNotFoundException {
        requireNonNull(editedReadOnlyEvent);

        Event editedEvent = new Event(editedReadOnlyEvent);
        events.setEvent(target, editedEvent);
    }


    /**
     * Removes {@code key} from this {@code EventList}.
     *
     * @throws EventNotFoundException if the {@code key} is not in this {@code EventList}.
     */
    public boolean removeEvent(ReadOnlyEvent key) throws EventNotFoundException, DeleteOnCascadeException {
        if (events.remove(key)) {
            return true;
        } else {
            throw new EventNotFoundException();
        }
    }

    /**
     * Remove a specific person from the participant list of an event
     */
    public void removeParticipant(ReadOnlyPerson person, Event targetEvent)
            throws PersonNotParticipateException {

        events.removeParticipant(person, targetEvent);
    }

    public void addParticipant(Person person, Event targetEvent)
            throws PersonHaveParticipateException {
        events.addParticipant(person, targetEvent);
    }

    // @@author HouDenghao
    /**
     * Sorts the event list.
     */
    public void sortEvents() {
        events.sort();
    }

    //// util methods

    // @@author
    @Override
    public ObservableList<ReadOnlyEvent> getEventList() {
        return events.asObservableList();
    }

    @Override
    public String toString() {
        return events.asObservableList().size() + " events";
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof EventList // instanceof handles nulls
                && this.events.equals(((EventList) other).events));
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(events);
    }
}