package com.micahherrera.warelication.eventdetail;

import com.micahherrera.warelication.data.Event;
import com.micahherrera.warelication.data.EventRepository;
import com.micahherrera.warelication.data.SWDBHelper;

/**
 * Created by micahherrera on 11/21/16.
 */

public class EventDetailPresenter implements EventDetailContract.Presenter{

    private EventDetailContract.View mView;
    private EventRepository mRepo;

    public EventDetailPresenter(EventDetailContract.View view, EventRepository repo){
        mView = view;
        mRepo = repo;
    }

    @Override
    public void getEventDetails(String id, SWDBHelper swdbHelper) {
        mRepo.getEvent(new EventRepository.LoadEventCallback() {
            @Override
            public void onEventLoaded(Event event) {
                mView.showDetails(event);
            }

            @Override
            public void onError(String error) {

            }
        }, swdbHelper, id);
    }
}
