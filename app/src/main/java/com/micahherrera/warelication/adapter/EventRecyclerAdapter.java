package com.micahherrera.warelication.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.micahherrera.warelication.R;
import com.micahherrera.warelication.data.Event;
import com.micahherrera.warelication.eventlist.EventListContract;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by micahherrera on 11/20/16.
 */

public class EventRecyclerAdapter
        extends RecyclerView.Adapter<EventRecyclerAdapter.EventRecyclerHolder> {

    private List<Event> mEventList;
    private LayoutInflater inflater;
    private EventListContract.View mView;

    public EventRecyclerAdapter(List<Event> eventList, EventListContract.View view){
        mEventList = eventList;
        mView = view;

    }

    @Override
    public EventRecyclerAdapter.EventRecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = inflater.from(parent.getContext()).inflate(R.layout.cardview_event_list, parent, false);
        EventRecyclerHolder eventHolder = new EventRecyclerHolder(v);
        return eventHolder;
    }

    @Override
    public void onBindViewHolder(EventRecyclerHolder holder, int position) {
        if(mEventList.get(position).getImage()!=null) {
            //holder.putThePhoto(mEventList.get(position).getImage());
            Picasso.with(holder.itemView.getContext())
                    .load(mEventList.get(position).getImage())
                    .resize(250, 250)
                    .centerCrop()
                    .error(R.drawable.placeholder_nomoon)
                    .into(holder.eventImage);
        } else {
            holder.eventImage.setImageResource(R.drawable.placeholder_nomoon);
        }
        holder.eventTitle.setText(mEventList.get(position).getTitle());
        holder.eventLocation.setText(mEventList.get(position).getLocationline1() + ", "
                + mEventList.get(position).getLocationline2());
        holder.eventDate.setText(com.micahherrera.warelication.utils.DateUtils.
                getDateFromFormat(mEventList.get(position).getDate()));
        holder.eventDescription.setText(mEventList.get(position).getDescription());
        holder.id = mEventList.get(position).getId();
    }


    @Override
    public int getItemCount() {
        return mEventList.size();
    }

    public void replaceData(List<Event> Events) {
        setList(Events);
        notifyDataSetChanged();
    }

    private void setList(List<Event> Events) {
        mEventList = Events;
    }

    public class EventRecyclerHolder extends RecyclerView.ViewHolder implements View.OnClickListener{


        private TextView eventDate;

        private ImageView eventImage;

        //@BindView(R.id.card_event_title)
        private TextView eventTitle;

        //@BindView(R.id.card_event_location)
        private TextView eventLocation;

        //@BindView(R.id.card_event_description)
        private TextView eventDescription;

        //@BindView(R.id.card_share_button)
        private TextView eventShareButton;

        private int id;


        public EventRecyclerHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);
            eventImage = (ImageView) itemView.findViewById(R.id.card_event_image);
            eventTitle = (TextView) itemView.findViewById(R.id.card_event_title);
            eventLocation = (TextView) itemView.findViewById(R.id.card_event_location);
            eventDescription = (TextView) itemView.findViewById(R.id.card_event_description);
            eventShareButton = (TextView) itemView.findViewById(R.id.card_share_button);
            eventShareButton.setOnClickListener(this);
            eventDate = (TextView) itemView.findViewById(R.id.card_event_date);

        }

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.card_share_button:

                    mView.showShareMenu(eventTitle.getText().toString(),
                            eventDate.getText().toString(), eventLocation.getText().toString());
                    Log.d("TAG", "onClick: share");
                    break;
                default:
                    mView.goToDetail(Integer.toString(id));
                    Log.d("TAG", "onClick: errything");
                    break;
            }

        }

    }
}
