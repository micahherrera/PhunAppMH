package com.micahherrera.warelication.eventdetail;

import com.micahherrera.warelication.data.Event;
import com.micahherrera.warelication.data.SWDBHelper;

/**
 * Created by micahherrera on 11/20/16.
 */

public interface EventDetailContract {

    interface View {
        void call(String phone);

        void showShareMenu();

        void goToMap(String address);

        void showDetails(Event event);

    }

    interface Presenter {

        void getEventDetails(String id, SWDBHelper swdbHelper);

    }
}
