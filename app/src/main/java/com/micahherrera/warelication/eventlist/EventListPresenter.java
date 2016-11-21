package com.micahherrera.warelication.eventlist;

import android.util.Log;

import com.micahherrera.warelication.data.Event;
import com.micahherrera.warelication.data.EventRepository;
import com.micahherrera.warelication.data.SWDBHelper;

import java.util.List;

/**
 * Created by micahherrera on 11/19/16.
 */

public class EventListPresenter implements EventListContract.Presenter {

    private EventRepository mRepo;
    private EventListContract.View mView;

    public EventListPresenter(EventRepository repo, EventListContract.View view){
        mRepo = repo;
        mView = view;

    }


    @Override
    public void loadEvents() {
        if(mView.checkConnection()) {

            mRepo.getEvents(new EventRepository.LoadEventsCallback() {
                @Override
                public void onEventsLoaded(List<Event> eventList) {
                    mView.showEvents(eventList, true);
                }

                @Override
                public void onError(String error) {
                    mView.showNoEventsView();
                    Log.d("TAG", "onError: "+error);
                }
            });
        } else if (!mView.checkConnection()){
            loadEventsFromDB();
        }
    }

    public void loadEventsFromDB(){
        SWDBHelper swdbHelper = mView.getDBHelper();
        mRepo.getDBEvents(new EventRepository.LoadEventsCallback() {
            @Override
            public void onEventsLoaded(List<Event> eventList) {
                mView.showEvents(eventList, false);
            }

            @Override
            public void onError(String error) {
                mView.showNoEventsView();
                Log.d("TAG", "onError: "+error);
            }
        }, swdbHelper);

    }

    @Override
    public void saveEvents(final List<Event> eventList, final SWDBHelper swdbHelper) {
        mRepo.clearEvents(new EventRepository.SaveEventCallback() {
            @Override
            public void onEventSaved(boolean success) {
                mRepo.saveEvent(eventList, new EventRepository.SaveEventCallback() {
                    @Override
                    public void onEventSaved(boolean success) {
                        Log.d("TAG", "onEventSaved: saved");
                    }

                    @Override
                    public void onError(String error) {
                        Log.d("TAG", "onError: couldNotCache");
                    }
                }, swdbHelper);
            }

            @Override
            public void onError(String error) {
                Log.d("TAG", "onError: couldNotClearCache");
            }
        }, swdbHelper);



    }
}
