package com.example.qwirkers;

import static com.example.qwirkers.Utility.Utilities.PLAYER_LIST;
import static com.example.qwirkers.Utility.Utilities.createDialog;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
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
import Game.Models.LocalGame;
import Game.Models.Player;
import Game.Models.Position;
import Game.Models.Tile;

public class OfflineGame extends AppCompatActivity {
    private RecyclerView hand;
    private GridView board;
    private RecyclerView players;
    private Button bag;

    private PlayerAdapter playerAdapter;
    private BoardAdapter boardAdapter;
    private HandAdapter handAdapter;

    private LocalGame game;
    private Player currentPlayer;
    private Tile selectedTile;
    private List<Position> validMoves;
    private List<Tile> tradeTiles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.offline_game);

        // getting players passed from the prev activity
        ArrayList<Player> playerList = (ArrayList<Player>) getIntent().getSerializableExtra(PLAYER_LIST);

        // Game instance
        game = new LocalGame(playerList);
        game.start();
        currentPlayer = game.getCurrentPlayer();

        // region Helper arrays

        validMoves = new ArrayList<>();
        tradeTiles = new ArrayList<>();

        // endregion

        // region Hand view and adapter

        hand = findViewById(R.id.player_hand);
        hand.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        hand.addItemDecoration(new EqualSpaceItemDecoration(5));
        hand.setMinimumHeight((int) Math.round(Dimension.TILESIZE.getDim() * 1.1));
        hand.setMinimumWidth((int) Math.round(Dimension.TILESIZE.getDim() * 6.6));

        handAdapter = new HandAdapter(this, currentPlayer.getHand());
        hand.setAdapter(handAdapter);

        // endregion

        // region Board view and adapter

        board = findViewById(R.id.game_board);
        board.getLayoutParams().width = (Dimension.TILESIZE.getDim() * (Dimension.DIMX.getDim() + 1));
        board.requestLayout();
        board.setColumnWidth(Dimension.TILESIZE.getDim());

        boardAdapter = new BoardAdapter(this, R.layout.game_tile, game.getBoard(0));
        board.setAdapter(boardAdapter);

        // endregion

        // region Players view and adapter
        players = findViewById(R.id.players);
        players.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
        players.addItemDecoration(new EqualSpaceItemDecoration(5));

        playerAdapter = new PlayerAdapter(this, game.getPlayers());
        playerAdapter.setCurrentPlayer(currentPlayer);
        players.setAdapter(playerAdapter);

        // endregion

        // region Bag button view

        bag = findViewById(R.id.bag);
        bag.setText(String.valueOf(game.getBagSize()));

        // endregion

        handAdapter.setOnClickListener(view -> {
            // Get view holder of the view.
            HandAdapter.TileViewHolder viewHolder = (HandAdapter.TileViewHolder) hand.findContainingViewHolder(view);

            if (selectedTile == viewHolder.tile) {
                selectedTile = null;
                handAdapter.highlight(null);
                return;
            }

            // Get the tile from the view holder
            selectedTile = viewHolder.tile;

            // Do something with the tile.
            handAdapter.highlight(selectedTile);

            validMoves = game.validMoves(selectedTile);

            // TODO: Following code is slowing down app
            boardAdapter.highlightValidMoves(validMoves);
        });

        board.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (selectedTile == null) return;

                if (game.placeTile(selectedTile, boardAdapter.selectedPosition(i))) {
                    boardAdapter.notifyDataSetChanged();

                    // deselect all blocks on the board
                    validMoves.clear();
                    boardAdapter.highlightValidMoves(validMoves);

                    // deselect all tiles in hand
                    selectedTile = null;
                    handAdapter.highlight(selectedTile);

                    // Dynamic board code
                    // board.getLayoutParams().width = (Dimension.TILESIZE.getDim() * (offlineGame.getBoardWidth() + 1));

                    handAdapter.notifyDataSetChanged();

                    // update player card
                    playerAdapter.notifyDataSetChanged();

                }
            }
        });

    }

    public void done(View view) {
        if(game.done()){
            Intent intent = new Intent(this, GameRanking.class);
            intent.putExtra(PLAYER_LIST, new ArrayList<>(game.getPlayers()));
            finish();
            startActivity(intent);
        }

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
        final Dialog dialog = createDialog(OfflineGame.this, R.layout.trade_dialog, Gravity.BOTTOM, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // region Trade Button

        Button tradeButton = dialog.findViewById(R.id.trade);

        tradeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                game.trade(tradeTiles);
                tradeTiles.clear();
                cleanup();
                dialog.dismiss();
            }
        });

        // endregion

        // region Cancel Button

        Button cancelButton = dialog.findViewById(R.id.cancel);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handAdapter.add(tradeTiles);
                tradeTiles.clear();
                cleanup();
                dialog.dismiss();
            }
        });

        // endregion

        // region Current Hand: tile that are still in your hand that will not be traded

        RecyclerView currentHand = dialog.findViewById(R.id.player_hand);
        HandAdapter handAdapter_ = new HandAdapter(this, currentPlayer.getHand());
        currentHand.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        currentHand.setAdapter(handAdapter_);
        currentHand.addItemDecoration(new EqualSpaceItemDecoration(5));
        currentHand.setMinimumHeight((int) Math.round(Dimension.TILESIZE.getDim() * 1.1));
        currentHand.setMinimumWidth((int) Math.round(Dimension.TILESIZE.getDim() * 6.6));

        // endregion

        // region Trade Hand: tile that you will be trading in from your hand

        RecyclerView tradeHand = dialog.findViewById(R.id.trade_in);
        HandAdapter tradeAdapter_ = new HandAdapter(this, tradeTiles);
        tradeHand.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        tradeHand.setAdapter(tradeAdapter_);
        tradeHand.addItemDecoration(new EqualSpaceItemDecoration(5));
        tradeHand.setMinimumHeight((int) Math.round(Dimension.TILESIZE.getDim() * 1.1));
        tradeHand.setMinimumWidth((int) Math.round(Dimension.TILESIZE.getDim() * 6.6));

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

    public void quit(View view) {
        finish();
    }

    private void cleanup() {
        // deselect all blocks on the board
        validMoves.clear();
        boardAdapter.highlightValidMoves(validMoves);

        // deselect all tiles in hand
        selectedTile = null;
        handAdapter.highlight(selectedTile);

        bag.setText(String.valueOf(game.getBagSize()));

        boardAdapter.notifyDataSetChanged();

        handAdapter.notifyDataSetChanged();
        playerAdapter.notifyDataSetChanged();
    }

}
