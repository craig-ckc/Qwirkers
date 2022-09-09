package com.example.qwirkers;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import QwirkleGame.Enums.Dimension;
import QwirkleGame.Game.Game;
import QwirkleGame.Game.Player;
import QwirkleGame.Game.Position;
import QwirkleGame.Game.Tile;

public class MainActivity extends AppCompatActivity {
    private PlayerAdapter playerAdapter;
    private BoardAdapter boardAdapter;
    private HandAdapter handAdapter;

    private List<Position> validMoves;
    private Tile selectedTile;
    private Player currentPlayer;
    private Game game;
    private List<Tile> tradeTiles;

    private RecyclerView currentHand;
    private GridView boardUI;
    private HorizontalScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Game instance
        game = new Game(new Player[]{new Player("Craig", 11), new Player("David", 2), new Player("Fiona", 7), new Player("Robin", 5)});

        // Tiles in the hands of the current player
        currentPlayer = game.getCurrentPlayer();

        validMoves = new ArrayList<>();

        // region Instantiating the adapters

        playerAdapter = new PlayerAdapter(this, game.getPlayers());
        boardAdapter = new BoardAdapter(this, R.layout.game_tile, game.getBoard(0));
        handAdapter = new HandAdapter(this, currentPlayer.getHand());

        // endregion


        // region Players view setup

        playerAdapter.setCurrentPlayer(currentPlayer);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(),2);
        RecyclerView playersUI = findViewById(R.id.players);
        playersUI.setLayoutManager(layoutManager);
        playersUI.setAdapter(playerAdapter);
        playersUI.addItemDecoration(new EqualSpaceItemDecoration(5));

        // endregion


        // region Board view setup

        scrollView =findViewById(R.id.horizontalScrollView);

        boardUI = findViewById(R.id.game_board);
        boardUI.getLayoutParams().width = (Dimension.TILESIZE.getDim() * (Dimension.DIMX.getDim() + 1));
        boardUI.requestLayout();
        boardUI.setColumnWidth(Dimension.TILESIZE.getDim());
        boardUI.setAdapter(boardAdapter);

        // endregion


        // region Hand view setup

        LinearLayoutManager layoutHorizontalManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,true);
        currentHand = findViewById(R.id.player_hand);
        currentHand.setLayoutManager(layoutHorizontalManager);
        currentHand.setAdapter(handAdapter);
        currentHand.addItemDecoration(new EqualSpaceItemDecoration(5));
        currentHand.setMinimumHeight(110);
        currentHand.setMinimumWidth(110);

        // endregion


        // Scroll to center of view
        scrollView.smoothScrollTo(boardUI.getLayoutParams().width / 2, 0);
        boardUI.smoothScrollToPosition(Dimension.DIMX.getDim() * (Dimension.DIMX.getDim() / 2));

        // on board block click event
        boardUI.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(selectedTile == null) return;

                if(game.placeTile(selectedTile, boardAdapter.selectedPosition(i))){
                    boardAdapter.notifyDataSetChanged();

                    // deselect all blocks on the board
                    validMoves.clear();
                    boardAdapter.highlightValidMoves(validMoves);

                    // deselect all tiles in hand
                    selectedTile = null;
                    handAdapter.setSelectedTile(selectedTile);

                    // remove tile from players hand
                    currentPlayer.getHand().remove(selectedTile);
                    handAdapter.notifyDataSetChanged();
                    selectedTile = null;

                    // update player card
                    playerAdapter.notifyDataSetChanged();
                }
            }
        });

        // in hand tile click event
        handAdapter.setOnClickListener( view -> {
            // Get view holder of the view.
            HandAdapter.TileViewHolder viewHolder = (HandAdapter.TileViewHolder) currentHand.findContainingViewHolder(view);

            if(selectedTile == viewHolder.tile){
                if(handAdapter.isMultiselect()){
                    tradeTiles.remove(selectedTile);
                }

                selectedTile = null;
                handAdapter.setSelectedTile(selectedTile);

                return;
            }

            // Get the tile from the view holder
            selectedTile = viewHolder.tile;

            if(handAdapter.isMultiselect()){
                tradeTiles.add(selectedTile);
            }

            validMoves = game.validMoves(selectedTile);

            // Do something with the tile.
            handAdapter.setSelectedTile(selectedTile);

            boardAdapter.highlightValidMoves(validMoves);
        });
    }

    public void done(View view){
        game.donePlay();

        currentPlayer = game.getCurrentPlayer();
        playerAdapter.setCurrentPlayer(currentPlayer);
        handAdapter.setTiles(currentPlayer.getHand());

        cleanup();

    }

    public void undo(View view){
        game.undoLastMove();

        cleanup();
    }

    public void clear(View view){
        game.clearMoves();

        cleanup();
    }

    public void trade(View view){
        if(handAdapter.isMultiselect()){
            game.trade(tradeTiles);

            handAdapter.setMultiselect(false);

            cleanup();

            return;
        }

        tradeTiles = new ArrayList<>();

        selectedTile = null;
        handAdapter.setSelectedTile(selectedTile);


        handAdapter.setMultiselect(true);
    }

    public void pass(View view){
        game.passPlay();

        currentPlayer = game.getCurrentPlayer();
        playerAdapter.setCurrentPlayer(currentPlayer);

        handAdapter.setTiles(currentPlayer.getHand());

        Toast.makeText(this, currentPlayer.toString(), Toast.LENGTH_SHORT).show();

        cleanup();
    }

    private void cleanup(){
        // deselect all blocks on the board
        validMoves.clear();
        boardAdapter.highlightValidMoves(validMoves);

        // deselect all tiles in hand
        selectedTile = null;
        handAdapter.setSelectedTile(selectedTile);

        boardAdapter.notifyDataSetChanged();
        handAdapter.notifyDataSetChanged();
        playerAdapter.notifyDataSetChanged();
    }

}