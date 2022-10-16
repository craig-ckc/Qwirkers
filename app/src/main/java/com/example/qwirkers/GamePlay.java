package com.example.qwirkers;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qwirkers.Utility.BoardAdapter;
import com.example.qwirkers.Utility.EqualSpaceItemDecoration;
import com.example.qwirkers.Utility.HandAdapter;
import com.example.qwirkers.Utility.PlayerAdapter;

import java.util.ArrayList;
import java.util.List;

import Game.Enums.Dimension;
import Game.Models.OnlineGame;
import Game.Models.Player;
import Game.Models.Position;
import Game.Models.Tile;

public class GamePlay extends AppCompatActivity {
    private PlayerAdapter playerAdapter;
    private BoardAdapter boardAdapter;
    private HandAdapter handAdapter;

    private List<Position> validMoves;
    private Tile selectedTile;
    private Player currentPlayer;
    private OnlineGame game;
    private List<Tile> tradeTiles;

    private RecyclerView currentHand;
    private GridView boardUI;
    private HorizontalScrollView scrollView;
    private Button bag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_play);

        // getting players passed from the prev activity
        ArrayList<Player> players = (ArrayList<Player>) getIntent().getSerializableExtra(Home.PLAYERS_MESSAGE);


        // Game instance
        game = new OnlineGame(players);

        game.start();
        currentPlayer = game.getCurrentPlayer();

        validMoves = new ArrayList<>();

        // region Instantiating the adapters NOTE: optimal to use thread on this code section thread

        playerAdapter = new PlayerAdapter(this, game.getPlayers());
        boardAdapter = new BoardAdapter(this, R.layout.game_tile, game.getBoard(0));
        handAdapter = new HandAdapter(this, currentPlayer.getHand());
        bag = findViewById(R.id.bag);
        tradeTiles = new ArrayList<>();

        // endregion

        // region Players view setup NOTE: optimal to use thread on this code section thread

        playerAdapter.setCurrentPlayer(currentPlayer);

        RecyclerView playersUI = findViewById(R.id.players);
        playersUI.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
        playersUI.setAdapter(playerAdapter);
        playersUI.addItemDecoration(new EqualSpaceItemDecoration(5));

        // endregion


        // region Board view setup NOTE: optimal to use thread on this code section thread

        scrollView = findViewById(R.id.horizontalScrollView);

        boardUI = findViewById(R.id.game_board);
        boardUI.getLayoutParams().width = (Dimension.TILESIZE.getDim() * (Dimension.DIMX.getDim() + 1));
        boardUI.requestLayout();
        boardUI.setColumnWidth(Dimension.TILESIZE.getDim());
        boardUI.setAdapter(boardAdapter);

        // endregion


        // region Hand view setup NOTE: optimal to use thread on this code section thread

        currentHand = findViewById(R.id.player_hand);
        currentHand.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        currentHand.setAdapter(handAdapter);
        currentHand.addItemDecoration(new EqualSpaceItemDecoration(5));
        currentHand.setMinimumHeight((int) Math.round(Dimension.TILESIZE.getDim() * 1.1));
        currentHand.setMinimumWidth((int) Math.round(Dimension.TILESIZE.getDim() * 1.1));

        // endregion

        bag.setText(String.valueOf(game.getBagSize()));

        // Scroll to center of view
        scrollView.smoothScrollTo(boardUI.getLayoutParams().width / 2, 0);
        boardUI.smoothScrollToPosition((Dimension.DIMX.getDim() * Dimension.DIMY.getDim()) / 2);

        // on board block click event
        boardUI.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(GamePlay.this, String.valueOf(i), Toast.LENGTH_SHORT).show();

                if (selectedTile == null) return;

                if (game.placeTile(selectedTile, boardAdapter.selectedPosition(i))) {
                    boardAdapter.notifyDataSetChanged();

                    // deselect all blocks on the board
                    validMoves.clear();
                    boardAdapter.highlightValidMoves(validMoves);

                    // remove tile from players hand
                    handAdapter.remove(selectedTile);

                    // deselect all tiles in hand
                    selectedTile = null;
                    handAdapter.setSelectedTile(selectedTile);

                    // update player card
                    playerAdapter.notifyDataSetChanged();
                }
            }
        });

        // in hand tile click event
        handAdapter.setOnClickListener(view -> {
            // Get view holder of the view.
            HandAdapter.TileViewHolder viewHolder = (HandAdapter.TileViewHolder) currentHand.findContainingViewHolder(view);

            if (selectedTile == viewHolder.tile) {
                selectedTile = null;
                handAdapter.setSelectedTile(null);
                return;
            }

            // Get the tile from the view holder
            selectedTile = viewHolder.tile;

            // Do something with the tile.
            handAdapter.setSelectedTile(selectedTile);


            validMoves = game.validMoves(selectedTile);

            // TODO: Following code is slowing down app
            boardAdapter.highlightValidMoves(validMoves);
        });
    }

    public void done(View view) {
        game.donePlay();

        currentPlayer = game.getCurrentPlayer();
        playerAdapter.setCurrentPlayer(currentPlayer);
        handAdapter.setTiles(currentPlayer.getHand());

        cleanup();

    }

    public void undo(View view) {
        game.undoLastMove();

        cleanup();
    }

    public void clear(View view) {
        game.clearMoves();

        cleanup();
    }

    public void trade(View view) {
        //region Dialog Frame ## NOTE: optimal to use thread on this code section thread

        final Dialog dialog = new Dialog(this, R.style.MyDialog);
        //We have added a title in the custom layout. So let's disable the default title.
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //The user will be able to cancel the dialog bu clicking anywhere outside the dialog.
        dialog.setCancelable(true);
        //Mention the name of the layout of your custom dialog.
        dialog.setContentView(R.layout.trade_dialog);

        dialog.setCanceledOnTouchOutside(true);

        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);

        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.dimAmount = 0.7f;
        dialog.getWindow().setAttributes(lp);

        Button tradeButton = dialog.findViewById(R.id.trade);
        Button cancelButton = dialog.findViewById(R.id.cancel);

        RecyclerView currentHand = dialog.findViewById(R.id.player_hand);
        RecyclerView tradeHand = dialog.findViewById(R.id.trade_in);

        //endregion


        // region Current player's hand ## NOTE: optimal to use thread on this code section thread

        HandAdapter handAdapter_ = new HandAdapter(this, currentPlayer.getHand());
        currentHand.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        currentHand.setAdapter(handAdapter_);
        currentHand.addItemDecoration(new EqualSpaceItemDecoration(5));
        currentHand.setMinimumHeight((int) Math.round(Dimension.TILESIZE.getDim() * 1.1));
        currentHand.setMinimumWidth((int) Math.round(Dimension.TILESIZE.getDim() * 1.1));

        // endregion


        // region Trade in tiles ## NOTE: optimal to use thread on this code section thread

        HandAdapter tradeAdapter_ = new HandAdapter(this, tradeTiles);
        tradeHand.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        tradeHand.setAdapter(tradeAdapter_);
        tradeHand.addItemDecoration(new EqualSpaceItemDecoration(5));
        tradeHand.setMinimumHeight((int) Math.round(Dimension.TILESIZE.getDim() * 1.1));
        tradeHand.setMinimumWidth((int) Math.round(Dimension.TILESIZE.getDim() * 1.1));

        // endregion

        handAdapter_.setOnClickListener(v -> {
            try {
                // Get view holder of the view.
                HandAdapter.TileViewHolder viewHolder = (HandAdapter.TileViewHolder) currentHand.findContainingViewHolder(v);

                // Get the tile from the view holder
                Tile tile = viewHolder.tile;

                handAdapter_.remove(tile); // NOTE: optimal to use thread on this code section thread
                tradeAdapter_.add(tile); // NOTE: optimal to use thread on this code section thread

                cleanup();
            } catch (Exception e) {
                return;
            }
        });

        tradeAdapter_.setOnClickListener(v -> {
            try {
                // Get view holder of the view.
                HandAdapter.TileViewHolder viewHolder = (HandAdapter.TileViewHolder) tradeHand.findContainingViewHolder(v);

                // Get the tile from the view holder
                Tile tile = viewHolder.tile;

                tradeAdapter_.remove(tile); // NOTE: optimal to use thread on this code section thread
                handAdapter_.add(tile); // NOTE: optimal to use thread on this code section thread

                cleanup();

            } catch (Exception e) {
                return;
            }
        });


        tradeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                game.trade(tradeTiles);
                tradeTiles.clear();
                cleanup();
                dialog.dismiss();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handAdapter.add(tradeTiles);
                tradeTiles.clear();
                cleanup();
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public void pass(View view) {
        game.passPlay();

        currentPlayer = game.getCurrentPlayer();
        playerAdapter.setCurrentPlayer(currentPlayer);

        handAdapter.setTiles(currentPlayer.getHand());

        Toast.makeText(this, currentPlayer.toString(), Toast.LENGTH_SHORT).show();

        cleanup();
    }

    private void cleanup() {
        // deselect all blocks on the board
        validMoves.clear();
        boardAdapter.highlightValidMoves(validMoves);

        // deselect all tiles in hand
        selectedTile = null;
        handAdapter.setSelectedTile(selectedTile);

        bag.setText(String.valueOf(game.getBagSize()));

        boardAdapter.notifyDataSetChanged();
        handAdapter.notifyDataSetChanged();
        playerAdapter.notifyDataSetChanged();
    }

    public void finish(View view) {
        finish();
    }
}
