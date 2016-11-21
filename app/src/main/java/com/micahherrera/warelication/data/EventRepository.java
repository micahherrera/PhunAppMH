package com.micahherrera.warelication.data;

import java.util.List;

/**
 * Created by micahherrera on 11/19/16.
 */

public interface EventRepository {

    interface LoadEventsCallback {

        void onEventsLoaded(List<Event> eventList);

        void onError(String error);

    }

    interface LoadEventCallback{

        void onEventLoaded(Event event);

        void onError(String error);
    }

    interface SaveEventCallback {

        void onEventSaved(boolean success);

        void onError(String error);
    }

    void getEvents(LoadEventsCallback callback);

    void getDBEvents(LoadEventsCallback callback, SWDBHelper swdbHelper);

    void clearEvents(SaveEventCallback callback, SWDBHelper swdbHelper);

    void saveEvent(List<Event> eventToSave, SaveEventCallback callback, SWDBHelper swdbHelper);

    void getEvent(LoadEventCallback callback, SWDBHelper swdbHelper, String id);
}
