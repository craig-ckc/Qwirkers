package com.example.qwirkers;

import static com.example.qwirkers.Utility.Utilities.*;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qwirkers.Utility.BoardAdapter;
import com.example.qwirkers.Utility.HandAdapter;
import com.example.qwirkers.Utility.PlayerAdapter;

import java.util.ArrayList;
import java.util.List;

import Game.Models.GameOnline;
import Game.Models.Player;
import Game.Models.Position;
import Game.Models.Tile;

import Server.GameClient;
import Server.messages.Message;
import Server.messages.server.MoveReceived;

public class OnlineGame extends AppCompatActivity {
    private SharedPreferences sharedPref;
    private GameClient client;

    private RecyclerView hand;
    private GridView board;
    private RecyclerView players;
    private Button bag;

    private GameOnline game;
    private PlayerAdapter playerAdapter;
    private BoardAdapter boardAdapter;
    private HandAdapter handAdapter;

    private Tile selectedTile;
    private List<Position> validMoves;
    private List<Tile> tradeTiles;

    private Player player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.online_game);

        sharedPref = getSharedPreferences(SHEARED_PREF, MODE_PRIVATE);

        game = new GameOnline();

        String serverAddress = getIntent().getStringExtra(SERVER_ADDRESS);
        String handle = sharedPref.getString(USER_NAME, "Player");

        client = GameClient.getInstance();
        client.setReceiver(message -> runOnUiThread(() -> onMessageReceived(message)));
        client.connect(serverAddress, handle);


        if (!client.isConnected()) {
            Toast.makeText(OnlineGame.this, "Connection failed", Toast.LENGTH_SHORT).show();
            finish();
        }

        // TODO: get six tiles for the server
        player = new Player(sharedPref.getString(USER_NAME, "Player"), sharedPref.getInt(USER_AVATAR, -1));

        // region Helper arrays

        validMoves = new ArrayList<>();
        tradeTiles = new ArrayList<>();

        // endregion

        // region Hand view and adapter

        handAdapter = new HandAdapter(this, player.getHand());
        hand = createHand(findViewById(R.id.player_hand), OnlineGame.this, handAdapter);

        // endregion

        // region Board view and adapter

        boardAdapter = new BoardAdapter(this, R.layout.game_tile, game.getBoard());
        board = createBoard(findViewById(R.id.game_board), OnlineGame.this, boardAdapter);

        // endregion

        // region Players view and adapter

        // TODO: Get List of players from server

        // playerAdapter = new PlayerAdapter(this, offlineGame.getPlayers(), currentPlayer);
        players = createPlayers(findViewById(R.id.players), OnlineGame.this, playerAdapter);

        // endregion

        // region Bag button view

        // TODO: get the bag size from the server

        bag = findViewById(R.id.bag);
        // bag.setText(String.valueOf(offlineGame.getBagSize()));

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

    }

    public void done(View view) {

    }

    public void undo(View view) {

    }

    public void clear(View view) {

    }

    public void trade(View view) {

    }

    public void pass(View view) {

    }

    public void quit(View view) {

    }

    public void onMessageReceived(Message msg) {
        if(msg instanceof MoveReceived){
            MoveReceived move = (MoveReceived) msg;

            game.placeTile(move.move.getTile(), move.move.getPosition());

            boardAdapter.notifyDataSetChanged();

            // TODO: update the playerAdapter list from the server
        }

        if(msg instanceof MoveReceived){
            MoveReceived move = (MoveReceived) msg;

            game.placeTile(move.move.getTile(), move.move.getPosition());

            boardAdapter.notifyDataSetChanged();

            // TODO: update the playerAdapter list from the server
        }
    }

}
