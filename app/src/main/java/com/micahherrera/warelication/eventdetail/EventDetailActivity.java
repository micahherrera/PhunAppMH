package com.micahherrera.warelication.eventdetail;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.micahherrera.warelication.R;
import com.micahherrera.warelication.data.Event;
import com.micahherrera.warelication.data.EventDataSource;
import com.micahherrera.warelication.data.EventRepository;
import com.micahherrera.warelication.data.SWDBHelper;
import com.micahherrera.warelication.utils.DateUtils;
import com.squareup.picasso.Picasso;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EventDetailActivity extends AppCompatActivity implements EventDetailContract.View{

    @BindView(R.id.event_detail_image)
    ImageView eventImage;

    @BindView(R.id.event_detail_date)
    TextView eventDate;

    @BindView(R.id.event_detail_title)
    TextView eventTitle;

    @BindView(R.id.event_detail_description)
    TextView eventDescription;

    @BindView(R.id.event_detail_location)
    TextView eventLocation;

    @BindView(R.id.toolbar_layout)
    CollapsingToolbarLayout collapsingToolbar;

    private EventDetailContract.Presenter mPresenter;
    private EventRepository mRepo;
    private String mAddress;
    private String mPhone;
    private ShareActionProvider mShareActionProvider;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);

        mRepo = new EventDataSource();
        mPresenter = new EventDetailPresenter(this, mRepo);

        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        getDetails(id);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToMap(mAddress);
            }
        });

        collapsingToolbar.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
        collapsingToolbar.setTitle("");
        toolbar.setTitle("Ware Lication");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("");
    }

    @Override
    public void call(String phone) {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:"+ phone));
        startActivity(callIntent);

    }

    @Override
    public void showShareMenu() {

    }

    @Override
    public void goToLink() {

    }

    @Override
    public void goToMap(String address) {
        String uri = String.format(Locale.ENGLISH, "geo:0,0?q="+address);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        EventDetailActivity.this.startActivity(mapIntent);

    }

    @Override
    public void showDetails(Event event) {
        Picasso.with(this)
                .load(event.getImage())
                .fit()
                .error(R.drawable.placeholder_nomoon)
                .into(eventImage);

        eventDate.setText(DateUtils.getDateFromFormat(event.getDate()));
        eventDescription.setText(event.getDescription());
        eventTitle.setText(event.getTitle());
        eventLocation.setText(event.getLocationline1() + ", "+event.getLocationline2());
        mAddress = event.getLocationline1() + ", "+event.getLocationline2();
        mPhone = event.getPhone();
    }

    public void getDetails(String id){
        SWDBHelper swdbHelper = SWDBHelper.getInstance(this);
        mPresenter.getEventDetails(id, swdbHelper);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_event_detail, menu);

        MenuItem item = menu.findItem(R.id.action_share);

        mShareActionProvider = new ShareActionProvider(this);
        mShareActionProvider.setShareIntent(getShareIntent());
        MenuItemCompat.setActionProvider(item, mShareActionProvider);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_call:
                call(mPhone);
                break;
            case android.R.id.home:
                finish();
                break;

        }

        return super.onOptionsItemSelected(item);
    }

    private Intent getShareIntent(){

            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_TEXT, "Join me for "+eventTitle.getText().toString() + " on " +
                    eventDate.getText().toString() + " at " + mAddress);
            shareIntent.setType("text/plain");

            return shareIntent;


    }

}
