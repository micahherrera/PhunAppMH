package com.micahherrera.warelication.data;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by micahherrera on 11/19/16.
 */

public class EventDataSource implements EventRepository {
    @Override
    public void getEvents(final LoadEventsCallback callback) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://raw.githubusercontent.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        EventService service = retrofit.create(EventService.class);
        Call<List<Event>> call = service.getEvents();
        Callback<List<Event>> listCallback = new Callback<List<Event>>() {
            @Override
            public void onResponse(Call<List<Event>> call, Response<List<Event>> response) {
                callback.onEventsLoaded(response.body());
            }

            @Override
            public void onFailure(Call<List<Event>> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        };
        call.enqueue(listCallback);
    }

    @Override
    public void getDBEvents(LoadEventsCallback callback, SWDBHelper swdbHelper) {
        List<Event> eventList = new ArrayList<>();
        Cursor cursor = swdbHelper.getEvents();
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            Event event = new Event();
            event.setId(cursor.getInt(cursor.getColumnIndex(SWDBHelper.ID)));
            event.setDescription(cursor.getString(cursor.getColumnIndex(SWDBHelper.DESCRIPTION)));
            event.setDate(cursor.getString(cursor.getColumnIndex(SWDBHelper.DATE)));
            event.setImage(cursor.getString(cursor.getColumnIndex(SWDBHelper.IMAGE)));
            event.setLocationline1(cursor.getString(cursor.getColumnIndex(SWDBHelper.LOCATIONLINE1)));
            event.setLocationline2(cursor.getString(cursor.getColumnIndex(SWDBHelper.LOCATIONLINE2)));
            event.setPhone(cursor.getString(cursor.getColumnIndex(SWDBHelper.PHONE)));
            event.setTimestamp(cursor.getString(cursor.getColumnIndex(SWDBHelper.TIMESTAMP)));
            event.setTitle(cursor.getString(cursor.getColumnIndex(SWDBHelper.TITLE)));
            eventList.add(event);
            cursor.moveToNext();
        }
        if(eventList.size()>0) {
            callback.onEventsLoaded(eventList);
        } else {
            callback.onError("noEventsSaved");
        }
    }

    @Override
    public void clearEvents(SaveEventCallback callback, SWDBHelper swdbHelper) {
        swdbHelper.clearEvents();
        callback.onEventSaved(true);
    }

    @Override
    public void saveEvent(List<Event> eventsToSave, SaveEventCallback callback, SWDBHelper swdbHelper) {
        swdbHelper.saveEvents(eventsToSave);
    }

    @Override
    public void getEvent(LoadEventCallback callback, SWDBHelper swdbHelper, String id) {
        Cursor cursor = swdbHelper.getEvent(id);
        cursor.moveToFirst();
        Event event = new Event();
        event.setId(cursor.getInt(cursor.getColumnIndex(SWDBHelper.ID)));
        event.setDescription(cursor.getString(cursor.getColumnIndex(SWDBHelper.DESCRIPTION)));
        event.setDate(cursor.getString(cursor.getColumnIndex(SWDBHelper.DATE)));
        event.setImage(cursor.getString(cursor.getColumnIndex(SWDBHelper.IMAGE)));
        event.setLocationline1(cursor.getString(cursor.getColumnIndex(SWDBHelper.LOCATIONLINE1)));
        event.setLocationline2(cursor.getString(cursor.getColumnIndex(SWDBHelper.LOCATIONLINE2)));
        event.setPhone(cursor.getString(cursor.getColumnIndex(SWDBHelper.PHONE)));
        event.setTimestamp(cursor.getString(cursor.getColumnIndex(SWDBHelper.TIMESTAMP)));
        event.setTitle(cursor.getString(cursor.getColumnIndex(SWDBHelper.TITLE)));

        callback.onEventLoaded(event);

    }


}
