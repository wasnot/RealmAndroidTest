package net.wasnot.android.databasetest.adapter;

import net.wasnot.android.databasetest.R;
import net.wasnot.android.databasetest.SQLite.UserColumns;

import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.TextView;

/**
 * TODO: Replace the implementation with code for your data type.
 */
public class CursorRecyclerViewAdapter extends RecyclerView.Adapter<CursorRecyclerViewAdapter.ViewHolder> {

    private final Cursor mCursor;

    private final AbsListView.OnItemClickListener mListener;

    private int indexAge;

    private int indexName;

    public CursorRecyclerViewAdapter(Cursor cursor, AbsListView.OnItemClickListener listener) {
        mCursor = cursor;
        mListener = listener;
        indexAge = cursor.getColumnIndex(UserColumns.AGE);
        indexName = cursor.getColumnIndex(UserColumns.NAME);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        if (mCursor.moveToPosition(position)) {
            holder.mIdView.setText(mCursor.getInt(indexAge) + "");
            holder.mContentView.setText(mCursor.getString(indexName));
        }
    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public final View mView;

        public final TextView mIdView;

        public final TextView mContentView;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.id);
            mContentView = (TextView) view.findViewById(R.id.content);
            mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != mListener) {
                        // Notify the active callbacks interface (the activity, if the
                        // fragment is attached to one) that an item has been selected.
                        mListener.onItemClick(null, v, getAdapterPosition(), 0);
                    }
                }
            });
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
