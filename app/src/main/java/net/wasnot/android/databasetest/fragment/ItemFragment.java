package net.wasnot.android.databasetest.fragment;

import net.wasnot.android.databasetest.R;
import net.wasnot.android.databasetest.Realm.User;
import net.wasnot.android.databasetest.SQLite.TestDatabase;
import net.wasnot.android.databasetest.adapter.CursorRecyclerViewAdapter;
import net.wasnot.android.databasetest.adapter.MyItemRecyclerViewAdapter;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import io.realm.Realm;

/**
 * A fragment representing a list of Items.
 * <p />
 * interface.
 */
public class ItemFragment extends Fragment {

    private static final String ARG_COLUMN_COUNT = "column-count";

    private static final String ARG_TYPE = "type";

    private int mColumnCount = 1;

    private int mType = 0;

    private Realm mRealm;

    private Cursor mCursor;

    private RecyclerView mRecyclerView;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ItemFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static ItemFragment newInstance(int columnCount, int type) {
        ItemFragment fragment = new ItemFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        args.putInt(ARG_TYPE, type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
            mType = getArguments().getInt(ARG_TYPE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            mRecyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                mRecyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
//            recyclerView.setAdapter(new MyItemRecyclerViewAdapter(DummyContent.ITEMS, mListener));
            if (mType == 0) {
                mRealm = Realm.getDefaultInstance();
                mRecyclerView.setAdapter(new MyItemRecyclerViewAdapter(mRealm.where(User.class).findAll(), null));
            } else {
                new AsyncTask<Void, Void, Cursor>() {
                    @Override
                    protected Cursor doInBackground(Void[] params) {
                        TestDatabase db = new TestDatabase(getActivity());
                        return db.getCursor(null, null, null, null);
                    }

                    @Override
                    protected void onPostExecute(Cursor o) {
                        super.onPostExecute(o);
                        mCursor = o;
                        mRecyclerView.setAdapter(new CursorRecyclerViewAdapter(mCursor, null));
                    }
                }.execute();
            }
        }
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mRealm != null) {
            mRealm.close();
        }
        if (mCursor != null) {
            mCursor.close();
        }
    }
}
