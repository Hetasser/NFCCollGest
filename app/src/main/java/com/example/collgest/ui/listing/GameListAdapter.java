package com.example.collgest.ui.listing;

import com.example.collgest.R;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.Arrays;

public class GameListAdapter extends RecyclerView.Adapter<GameListAdapter.ViewHolder> {

    private String[][] localDataSet;

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView gameNameView;
        private final TextView gameMinPlayersView;
        private final TextView gameMaxPLayersView;
        private final TextView gameDurationView;
        private final TextView gameTypesView;
        private final TextView gameCheckedOutToView;
        private final TextView gameLastPlayedView;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View
            gameNameView = (TextView) view.findViewById(R.id.listing_gamename);
            gameMinPlayersView = (TextView) view.findViewById(R.id.listing_gameminplayer);
            gameMaxPLayersView = (TextView) view.findViewById(R.id.listing_gamemaxplayers);
            gameDurationView = (TextView) view.findViewById(R.id.listing_gameduration);
            gameTypesView = (TextView) view.findViewById(R.id.listing_gametypes);
            gameCheckedOutToView = (TextView) view.findViewById(R.id.listing_gamecheckedoutto);
            gameLastPlayedView = (TextView) view.findViewById(R.id.listing_gamelastplayed);

        }

        public TextView getGameNameView() {return gameNameView;}
        public TextView getGameMinPlayersView() {return gameMinPlayersView;}
        public TextView getGameMaxPLayersView() {return gameMaxPLayersView;}
        public TextView getGameDurationView() {return gameDurationView;}
        public TextView getGameTypesView() {return gameTypesView;}
        public TextView getGameCheckedOutToView() {return gameCheckedOutToView;}
        public TextView getGameLastPlayedView() {return gameLastPlayedView;}
    }

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param dataSet String[] containing the data to populate views to be used
     * by RecyclerView.
     */
    public GameListAdapter(String[][] dataSet) {
        localDataSet = dataSet;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.listing_listitem, viewGroup, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.getGameNameView().setText(localDataSet[position][0]);
        viewHolder.getGameMinPlayersView().setText(localDataSet[position][1]);
        viewHolder.getGameMaxPLayersView().setText(localDataSet[position][2]);
        viewHolder.getGameDurationView().setText(localDataSet[position][3]);
        viewHolder.getGameTypesView().setText(localDataSet[position][4]);
        viewHolder.getGameCheckedOutToView().setText(localDataSet[position][5]);
        viewHolder.getGameLastPlayedView().setText(localDataSet[position][6]);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return localDataSet.length;
    }
}
