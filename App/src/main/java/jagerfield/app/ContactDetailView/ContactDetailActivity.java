package jagerfield.app.ContactDetailView;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import jagerfield.ContentResolverVsCursorLoader.R;
import jagerfield.comparison.lib.Contact.Contact;
import jagerfield.comparison.lib.ElementContainers.AddressContainer;
import jagerfield.comparison.lib.ElementContainers.EmailContainer;
import jagerfield.comparison.lib.ElementContainers.EventContainer;
import jagerfield.comparison.lib.ElementContainers.NoteContainer;
import jagerfield.comparison.lib.ElementContainers.NumberContainer;
import jagerfield.comparison.lib.ElementContainers.WebsiteContainer;
import jagerfield.app.ContactDetailView.RecyclerViewsAdapters.AddressViewAdapter;
import jagerfield.app.ContactDetailView.RecyclerViewsAdapters.EmailViewAdapter;
import jagerfield.app.ContactDetailView.RecyclerViewsAdapters.EventViewAdaptor;
import jagerfield.app.ContactDetailView.RecyclerViewsAdapters.NoteViewAdaptor;
import jagerfield.app.ContactDetailView.RecyclerViewsAdapters.NumberViewAdapter;
import jagerfield.app.ContactDetailView.RecyclerViewsAdapters.WebsiteViewAdaptor;
import jagerfield.app.Utilities.C;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import java.util.LinkedList;

public class ContactDetailActivity extends AppCompatActivity {

    private ImageView ivName;
    private ImageView ivNumber;
    private ImageView ivAddress;
    private ImageView ivEmail;
    private ImageView ivWebsite;
    private ImageView ivNote;
    private ImageView ivEvent;
    private Toolbar toolbar;
    private AppBarLayout appBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_contact);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        appBar = (AppBarLayout) findViewById(R.id.app_bar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onGreenRobotEvent(Contact contact) {
        /*
         * Posted from : holder.mView.setOnClickListener - ContactListViewAdapter
         */
        EventBus.getDefault().removeStickyEvent(Contact.class);

        if (contact == null) {
            return;
        }

        loadContactImage(contact.getPhotoUri());
        loadNamesField(contact);
        loadNumbersField(contact);
        loadAddressField(contact);
        loadEmailField(contact);
        loadWebsiteField(contact);
        loadEventField(contact);
        loadNoteField(contact);
    }

    @Override
    public void onResume() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    private void loadContactImage(String imageDir) {
        final ImageView imageView = (ImageView) findViewById(R.id.contactImage);

        bindfieldIcons();

        /**
         * The material Palette.Swatch extracts six different color shades from the image for
         * coloring the icons and titles.
         * To get the palette colors, the image has t be a bitmap. Use Glide to generate the bitmap
         * and when it is ready extract the colors.
         *
         */
        Glide
                .with(getApplicationContext())
                .load(imageDir)
                .asBitmap()
                .into(new SimpleTarget<Bitmap>(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL) {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {
                        imageView.setImageBitmap(resource);

                        Palette.Swatch color = C.getImageColor(C.VIBRANT, resource);

                        if (color != null) {
                            /**
                             * Recolor the icons
                             */
                            recolorFieldIcons(color);
                        }
                    }
                });

    }

    private void recolorFieldIcons(Palette.Swatch color) {
        ivName.setColorFilter(color.getRgb());
        ivNumber.setColorFilter(color.getRgb());
        ivAddress.setColorFilter(color.getRgb());
        ivEmail.setColorFilter(color.getRgb());
        ivWebsite.setColorFilter(color.getRgb());
        ivNote.setColorFilter(color.getRgb());
        ivEvent.setColorFilter(color.getRgb());
    }

    private void bindfieldIcons() {
        ivName = (ImageView) findViewById(R.id.ivName);
        ivNumber = (ImageView) findViewById(R.id.ivNumber);
        ivAddress = (ImageView) findViewById(R.id.ivAddress);
        ivEmail = (ImageView) findViewById(R.id.ivEmail);
        ivWebsite = (ImageView) findViewById(R.id.ivWebsite);
        ivNote = (ImageView) findViewById(R.id.ivNote);
        ivEvent = (ImageView) findViewById(R.id.ivEvent);
    }


    private Contact getArguments() {

        GsonBuilder builder = new GsonBuilder();
        builder.excludeFieldsWithoutExposeAnnotation();
        Gson gsonBuilder = builder.create();

        String gsonListString = getIntent().getStringExtra(C.CONTACT);
        Contact contact = gsonBuilder.fromJson(gsonListString, Contact.class);

        return contact;
    }

    private void loadNamesField(Contact contact) {
        if (contact == null) {
            return;
        }

        TextView displayedName = (TextView) findViewById(R.id.tvValue_1);
        TextView firstName = (TextView) findViewById(R.id.tvValue_2);
        TextView lastName = (TextView) findViewById(R.id.tvValue_3);

        /**
         * If a name is missing then hide it
         */
        setViewState(displayedName, contact.getDisplaydName());
        boolean fName = setViewState(firstName, contact.getFirstName());
        boolean lName = setViewState(lastName, contact.getLastName());

        StringBuilder title = new StringBuilder();

        if (fName) {
            title.append(contact.getFirstName() + " ");
        }
        if (lName) {
            title.append(contact.getLastName());
        }

        /**
         * Set activity's title to show contact first and last names
         */
        getSupportActionBar().setTitle(title.toString().trim());

        displayedName.setText(contact.getDisplaydName());
        firstName.setText(contact.getFirstName());
        lastName.setText(contact.getLastName());

    }

    private boolean setViewState(TextView tv, String value) {
        if (value.trim().isEmpty()) {
            tv.setVisibility(View.GONE);
            return false;
        } else {
            tv.setVisibility(View.VISIBLE);
            return true;
        }
    }

    private void loadNumbersField(Contact contact) {
        if (contact == null || contact.getNumbers() == null) {
            return;
        }

        RecyclerView rvNumers = (RecyclerView) findViewById(R.id.rvNumbers);

        LinkedList<NumberContainer> numberList = contact.getNumbers();

        if (numberList == null) {
            return;
        }
        rvNumers.setLayoutManager(new LinearLayoutManager(this));
        rvNumers.setAdapter(new NumberViewAdapter(this, numberList));
    }

    private void loadAddressField(Contact contact) {
        if (contact == null || contact.getAddresses() == null) {
            return;
        }

        RecyclerView rvAddress = (RecyclerView) findViewById(R.id.rvAddress);

        LinkedList<AddressContainer> addressList = contact.getAddresses();

        if (addressList == null) {
            return;
        }
        rvAddress.setLayoutManager(new LinearLayoutManager(this));
        rvAddress.setAdapter(new AddressViewAdapter(this, addressList));
    }

    private void loadEmailField(Contact contact) {
        if (contact == null || contact.getEmails() == null) {
            return;
        }

        RecyclerView rvEmail = (RecyclerView) findViewById(R.id.rvEmail);

        LinkedList<EmailContainer> emailList = contact.getEmails();

        if (emailList == null) {
            return;
        }
        rvEmail.setLayoutManager(new LinearLayoutManager(this));
        rvEmail.setAdapter(new EmailViewAdapter(this, emailList));
    }

    private void loadWebsiteField(Contact contact) {
        if (contact == null || contact.getWebsites() == null) {
            return;
        }

        RecyclerView rvWebsite = (RecyclerView) findViewById(R.id.rvWebsite);

        LinkedList<WebsiteContainer> websiteList = contact.getWebsites();

        if (websiteList == null) {
            return;
        }
        rvWebsite.setLayoutManager(new LinearLayoutManager(this));
        rvWebsite.setAdapter(new WebsiteViewAdaptor(this, websiteList));
    }

    private void loadEventField(Contact contact) {
        if (contact == null || contact.getEvents() == null) {
            return;
        }

        RecyclerView rvEvent = (RecyclerView) findViewById(R.id.rvEvent);

        LinkedList<EventContainer> eventList = contact.getEvents();

        if (eventList == null) {
            return;
        }
        rvEvent.setLayoutManager(new LinearLayoutManager(this));
        rvEvent.setAdapter(new EventViewAdaptor(this, eventList));
    }

    private void loadNoteField(Contact contact) {
        if (contact == null || contact.getNotes() == null) {
            return;
        }

        RecyclerView rvNote = (RecyclerView) findViewById(R.id.rvNote);

        LinkedList<NoteContainer> noteList = contact.getNotes();

        if (noteList == null) {
            return;
        }
        rvNote.setLayoutManager(new LinearLayoutManager(this));
        rvNote.setAdapter(new NoteViewAdaptor(this, noteList));
    }
}











