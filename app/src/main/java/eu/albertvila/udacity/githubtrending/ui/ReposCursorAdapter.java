package eu.albertvila.udacity.githubtrending.ui;

import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import eu.albertvila.udacity.githubtrending.R;
import eu.albertvila.udacity.githubtrending.data.db.DbContract;

/**
 * Created by Albert Vila Calvo on 13/9/16.
 */
public class ReposCursorAdapter extends RecyclerView.Adapter<ReposCursorAdapter.ViewHolder> {

    private Cursor cursor;

    public ReposCursorAdapter() {
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_repo, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (cursor != null && cursor.moveToPosition(position)) {
            holder.bind(cursor);
        }
    }

    @Override
    public int getItemCount() {
        return (cursor == null) ? 0 : cursor.getCount();
    }

    public void swapCursor(Cursor newCursor) {
        cursor = newCursor;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView nameTextView;

        public ViewHolder(View itemView) {
            super(itemView);

            nameTextView = (TextView) itemView.findViewById(R.id.list_item_repo_name);
        }

        public void bind(Cursor cursor) {
            nameTextView.setText(cursor.getString(cursor.getColumnIndexOrThrow(DbContract.Repo.COLUMN_URL)));
        }

    }
}
