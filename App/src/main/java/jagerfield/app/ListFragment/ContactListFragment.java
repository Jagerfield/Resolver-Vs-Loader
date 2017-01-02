package jagerfield.app.ListFragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import jagerfield.ContentResolverVsCursorLoader.R;
import jagerfield.app.ContactDetailView.ContactDetailActivity;
import jagerfield.comparison.lib.Contact.Contact;
import jagerfield.comparison.lib.ImportContacts;
import jagerfield.app.Utilities.C;
import org.greenrobot.eventbus.EventBus;
import java.util.ArrayList;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

public class ContactListFragment extends Fragment {

    ArrayList<Contact> contactsList = new ArrayList<>();
    private RecyclerView recyclerView;
    private ContactListFragment contactListFragment;
    private long endTime;
    private long startTime;
    private TextView tvTimer;
    private String cursorType;
    private View view;

    public ContactListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_contact_list, container, false);

        tvTimer = (TextView) view.findViewById(R.id.tvTimer);

        cursorType = getArguments().getString(C.CURSOR_TYPE);

        if (cursorType == null) {
            cursorType = C.CURSOR_CONTENT_RESOLVER;
        }

        Context context = view.getContext();
        recyclerView = (RecyclerView) view.findViewById(R.id.contactListFragment);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        contactListFragment = this;

//        EventBus.getDefault().register(this);
//        contactsList = EventBus.getDefault().removeStickyEvent((new ArrayList<Contact>()).getClass());

        if (contactsList == null) {
            contactsList = new ArrayList<>();
        }

        startTime = 0l;
        endTime = 0l;

        /**
         * Fetch mobile contacts
         */
        if (cursorType.equals(C.CURSOR_CONTENT_RESOLVER)) {
            startTime = System.nanoTime();

            new ImportContacts(getActivity(), new ImportContacts.ContentResolverCallback() {

                @Override
                public void getMobileContacts(ArrayList<Contact> contacts) {
                    endTime = System.nanoTime();
                    tvTimer.setText("Execution time : " + ((endTime - startTime) / 1000000) + "ms");

                    contactsList = contacts;

                    if (contactsList != null) {
                        recyclerView.setAdapter(new ContactListViewAdapter(contactListFragment, contactsList));
                    }

                }
            });
        } else if (cursorType.equals(C.CURSOR_LOADER)) {
            startTime = System.nanoTime();

            new ImportContacts(getActivity(), new ImportContacts.CursorLoaderCallback()
            {
                @Override
                public void getMobileContacts(ArrayList<Contact> contacts) {
                    endTime = System.nanoTime();
                    tvTimer.setText("Execution time : " + ((endTime - startTime) / 1000000) + "ms");

                    contactsList = contacts;

                    if (contactsList != null) {
                        recyclerView.setAdapter(new ContactListViewAdapter(contactListFragment, contactsList));
                    }
                }
            });
        }

        return view;
    }

    /**
     * Returns a new instance of this fragment
     */
    public static ContactListFragment newInstance(String cursorType) {
        ContactListFragment fragment = new ContactListFragment();
        Bundle args = new Bundle();
        args.putString(C.CURSOR_TYPE, cursorType);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Fragment Contact List Adapter
     */
    private class ContactListViewAdapter extends RecyclerView.Adapter<ContactListViewAdapter.ViewHolder> {

        private ContactListFragment fragment;
        private Context context;
        private ArrayList<Contact> contactList = new ArrayList<>();

        public ContactListViewAdapter(ContactListFragment fragment, ArrayList<Contact> contactList) {
            this.fragment = fragment;
            context = fragment.getActivity();
            this.contactList = contactList;
        }

        @Override
        public ContactListViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_contact_list_item, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ContactListViewAdapter.ViewHolder holder, final int position) {
            holder.vhContact = contactList.get(position);
            holder.name.setText(holder.vhContact.getFirstName() + " " + holder.vhContact.getLastName());

            String imageUri = contactList.get(position).getPhotoUri();
            Glide.with(context)
                    .load(imageUri)
                    .error(R.drawable.person)
                    .bitmapTransform(new CropCircleTransformation(context))
                    .into(holder.image);

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (contactList == null) {
                        return;
                    }

                    Intent i = new Intent(context, ContactDetailActivity.class);
                    EventBus.getDefault().postSticky(contactList.get(position));
                    context.startActivity(i);
                }
            });
        }

        @Override
        public int getItemCount() {
            return contactList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final TextView name;
            public final ImageView image;
            public Contact vhContact;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                name = (TextView) view.findViewById(R.id.tv_name);
                image = (ImageView) view.findViewById(R.id.iv_contact_Image);
            }
        }
    }
}
