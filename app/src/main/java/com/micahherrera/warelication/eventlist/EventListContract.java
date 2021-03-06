package com.micahherrera.warelication.eventlist;

import com.micahherrera.warelication.data.Event;
import com.micahherrera.warelication.data.SWDBHelper;

import java.util.List;

/**
 * Created by micahherrera on 11/19/16.
 */

public interface EventListContract {

    interface View {

        void goToDetail(String id);

        void showShareMenu(String title, String date, String address);

        void showEvents(List<Event> eventList, boolean source);

        void showNoEventsView();

        boolean checkConnection();

        SWDBHelper getDBHelper();

    }

    interface Presenter {

        void loadEvents();

        void loadEventsFromDB();

        void saveEvents(List<Event> eventList, SWDBHelper swdbHelper);

    }
}
