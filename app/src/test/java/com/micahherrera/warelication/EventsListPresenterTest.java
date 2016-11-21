package com.micahherrera.warelication;

import com.micahherrera.warelication.data.Event;
import com.micahherrera.warelication.data.EventDataSource;
import com.micahherrera.warelication.data.EventRepository;
import com.micahherrera.warelication.eventlist.EventListContract;
import com.micahherrera.warelication.eventlist.EventListPresenter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by micahherrera on 11/21/16.
 */

public class EventsListPresenterTest {

    private static List<Event> EVENTS = new ArrayList<>();

    @Mock
    EventRepository eventsRepository;
    @Mock
    EventListContract.View eventsView;

    private EventListPresenter eventsPresenter;

    /**
     * {@link ArgumentCaptor} is a powerful Mockito API to capture argument values and use them to
     * perform further actions or assertions on them.
     */
    @Captor
    private ArgumentCaptor<EventDataSource.LoadEventsCallback> loadEventsCallbackCaptor;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        eventsPresenter = new EventListPresenter(eventsRepository, eventsView);

        Event event = new Event();
        event.setId(1);
        event.setPhone("123");
        event.setTitle("Title");
        event.setImage("file/path/file.png");
        event.setTimestamp("0000000");
        event.setDate("12-12-2012");
        event.setLocationline1("Naboo");
        event.setLocationline2("Naboo");
        event.setDescription("description");
        // Test data
        EVENTS.add(event);

        System.out.println("setUp: Events size is: " +EVENTS.size());

        when(eventsView.checkConnection()).thenReturn(true);
    }

    @Test
    public void loadAllEventsFromRepositoryAndLoadIntoView() {
        // Given an initialized TasksPresenter with initialized events
        // When loading of Events is requested
        eventsPresenter.loadEvents();


        // Callback is captured and invoked with stubbed tasks
        verify(eventsRepository).getEvents(loadEventsCallbackCaptor.capture());
        loadEventsCallbackCaptor.getValue().onEventsLoaded(EVENTS);

        ArgumentCaptor<Boolean> sourceCaptor = ArgumentCaptor.forClass(Boolean.class);
        ArgumentCaptor<List> showEventsArgumentCaptor = ArgumentCaptor.forClass(List.class);
        verify(eventsView).showEvents(showEventsArgumentCaptor.capture(), sourceCaptor.capture());
        assertTrue(showEventsArgumentCaptor.getValue().size() == 1);
        assertTrue(sourceCaptor.getValue().booleanValue() == Boolean.TRUE);
    }
}