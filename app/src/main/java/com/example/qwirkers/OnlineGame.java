package com.example.qwirkers;

import static com.example.qwirkers.Utility.Utilities.PLAYER_LIST;
import static com.example.qwirkers.Utility.Utilities.SERVER_ADDRESS;
import static com.example.qwirkers.Utility.Utilities.SHEARED_PREF;
import static com.example.qwirkers.Utility.Utilities.USER_AVATAR;
import static com.example.qwirkers.Utility.Utilities.USER_NAME;
import static com.example.qwirkers.Utility.Utilities.createBoard;
import static com.example.qwirkers.Utility.Utilities.createDialog;
import static com.example.qwirkers.Utility.Utilities.createHand;
import static com.example.qwirkers.Utility.Utilities.createPlayers;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qwirkers.Utility.BoardAdapter;
import com.example.qwirkers.Utility.EqualSpaceItemDecoration;
import com.example.qwirkers.Utility.HandAdapter;
import com.example.qwirkers.Utility.LobbyAvatarAdapter;
import com.example.qwirkers.Utility.PlayerAdapter;
import com.example.qwirkers.Utility.Utilities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import Game.Enums.Dimension;
import Game.Models.ClientGame;
import Game.Models.Move;
import Game.Models.Player;
import Game.Models.Position;
import Game.Models.Tile;
import Server.Client;
import Server.messages.Message;
import Server.messages.client.FinishPlay;
import Server.messages.client.Join;
import Server.messages.client.Leave;
import Server.messages.client.PlayMove;
import Server.messages.client.Trade;
import Server.messages.server.Confirmation;
import Server.messages.server.FinishedPlay;
import Server.messages.server.GameOver;
import Server.messages.server.Joined;
import Server.messages.server.Left;
import Server.messages.server.PlayedMove;
import Server.messages.server.StartGame;
import Server.messages.server.TileRefill;
import Server.messages.server.Traded;

public class OnlineGame extends AppCompatActivity {
    private SharedPreferences sharedPref;
    private Client client;

    private RecyclerView hand;
    private GridView board;
    private RecyclerView players;
    private Button bag;

    private ClientGame game;
    private PlayerAdapter playerAdapter;
    private BoardAdapter boardAdapter;
    private HandAdapter handAdapter;
    private LobbyAvatarAdapter lobbyAvatarAdapter;

    private Tile selectedTile;
    private List<Position> validMoves;
    private List<Tile> tradeTiles;

    private Player player;
    private Dialog waitingModal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.online_game);

        sharedPref = getSharedPreferences(SHEARED_PREF, MODE_PRIVATE);

        client = Client.getInstance();
        client.setReceiver(message -> runOnUiThread(() -> onMessageReceived(message)));
        client.connect(sharedPref.getString(SERVER_ADDRESS, ""));

        game = new ClientGame();

        // region Helper arrays

        validMoves = new ArrayList<>();
        tradeTiles = new ArrayList<>();

        // endregion

        // region Hand view and adapter

        handAdapter = new HandAdapter(this, new ArrayList<>());
        hand = createHand(findViewById(R.id.player_hand), OnlineGame.this, handAdapter);

        // endregion

        // region Board view and adapter

        boardAdapter = new BoardAdapter(this, R.layout.game_tile, game.getBoard());
        board = createBoard(findViewById(R.id.game_board), OnlineGame.this, boardAdapter);

        // endregion

        // region Players view and adapter

        List<Player> player_list = Arrays.asList(new Player("", -1), new Player("", -1));

        playerAdapter = new PlayerAdapter(this, player_list, game.currentPlayer);
        players = createPlayers(findViewById(R.id.players), OnlineGame.this, playerAdapter);

        // endregion

        // region Bag button view

        bag = findViewById(R.id.bag);
        bag.setText("0");

        // endregion

        handAdapter.setOnClickListener(view -> {
            // when it is not players turn do nothing
            if (!playerAdapter.currentPlayer().equals(player))
                return;

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

                    // message sent to server
                    client.send(new PlayMove(client.session, boardAdapter.selectedPosition(i), selectedTile));

                    handAdapter.notifyDataSetChanged();

                    // update player card
                    playerAdapter.notifyDataSetChanged();

                }
            }
        });

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (!client.isConnected()) {
            Toast.makeText(OnlineGame.this, "Connection failed", Toast.LENGTH_SHORT).show();
            finish();
        }

        waiting();
    }

    // DONE: waiting animation
    public void waiting() {
        waitingModal = createDialog(OnlineGame.this, R.layout.lobby_modal, Gravity.CENTER, WindowManager.LayoutParams.WRAP_CONTENT);

        Button cancelButton = waitingModal.findViewById(R.id.cancel_button);

        lobbyAvatarAdapter = new LobbyAvatarAdapter(this, new ArrayList<>());

        RecyclerView waiting_players = waitingModal.findViewById(R.id.waiting_players);
        waiting_players.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        waiting_players.setAdapter(lobbyAvatarAdapter);
        waiting_players.addItemDecoration(new EqualSpaceItemDecoration(5));

        List<View> circles = Arrays.asList(
                waitingModal.findViewById(R.id.circle_01),
                waitingModal.findViewById(R.id.circle_02),
                waitingModal.findViewById(R.id.circle_03));
        Utilities.LoadingAnimation loading = new Utilities.LoadingAnimation(OnlineGame.this, circles);

        waitingModal.setCancelable(false);
        waitingModal.setCanceledOnTouchOutside(false);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                client.send(new Leave(client.session, client.name));
                finish();
                waitingModal.dismiss();
            }
        });

        loading.start();

        waitingModal.show();
    }

    // DONE:
    public void done(View view) {
        client.send(new FinishPlay(client.session));

        cleanup();
    }

    // DONE:
    public void undo(View view) {
        Move move = game.undoLastMove();

        client.send(new PlayMove(client.session, move.getPosition(), move.getTile()));
    }

    // DONE:
    public void clear(View view) {
        while (game.moves() > 0) {
            undo(view);
        }
    }

    // TODO: implement trade
    public void trade(View view) {
        final Dialog dialog = createDialog(OnlineGame.this, R.layout.trade_dialog, Gravity.BOTTOM, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // region Trade Button

        Button tradeButton = dialog.findViewById(R.id.trade);

        tradeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                client.send(new Trade(client.session, tradeTiles));
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
        HandAdapter handAdapter_ = new HandAdapter(this, player.getHand());
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

    // DONE:
    public void pass(View view) {
        client.send(new Leave(client.session, client.name));

        finish();
    }

    // DONE:
    public void quit(View view) {
        client.send(new Leave(client.session, client.name));

        finish();
    }

    // DONE:
    private void cleanup() {
        // remove highlight on all blocks on the board
        validMoves.clear();
        boardAdapter.highlightValidMoves(validMoves);

        // deselect all tiles in hand
        selectedTile = null;
        handAdapter.highlight(null);
    }

    // TODO:
    public void onMessageReceived(Message message) {

        // DONE:
        if (message instanceof Confirmation) {
            Confirmation msg = (Confirmation) message;

            client.session = msg.session;
            client.name = sharedPref.getString(USER_NAME, "Player");

            this.player = new Player(client.name, sharedPref.getInt(USER_AVATAR, -1));

            client.send(new Join(msg.session, this.player));

            Log.i("SERVER_MESSAGE", "Connection has been confirmed, player is registered as " + client.name);
        }

        // DONE:
        else if (message instanceof FinishedPlay) {
            FinishedPlay msg = (FinishedPlay) message;

            playerAdapter.setPlayers(msg.players);
            playerAdapter.setCurrentPlayer(msg.player);
            bag.setText(msg.bagSize);

            if(player.equals(msg.player))
                Toast.makeText(OnlineGame.this, "It's your turn", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(OnlineGame.this, "It's " + msg.player.getName() + "'s turn", Toast.LENGTH_SHORT).show();
        }

        // DONE:
        else if (message instanceof GameOver) {
            GameOver msg = (GameOver) message;

            Intent intent = new Intent(this, GameRanking.class);
            intent.putExtra(PLAYER_LIST, new ArrayList<>(msg.players));
            finish();
            startActivity(intent);
        }

        // DONE:
        else if (message instanceof Joined) {
            Joined msg = (Joined) message;

            lobbyAvatarAdapter.setPlayers(msg.players);

            if (!client.name.equals(msg.name))
                Toast.makeText(OnlineGame.this, msg.name + " has joined the game!", Toast.LENGTH_SHORT).show();
        }

        // DONE:
        else if (message instanceof Left) {
            Left msg = (Left) message;

            playerAdapter.setPlayers(msg.players);
            playerAdapter.setCurrentPlayer(msg.player);
            bag.setText(msg.bagSize);

            if (!client.name.equals(msg.name))
                Toast.makeText(OnlineGame.this, msg.name + " has left the game!", Toast.LENGTH_SHORT).show();
        }

        // DONE:
        else if (message instanceof PlayedMove) {
            PlayedMove msg = (PlayedMove) message;

            playerAdapter.setCurrentPlayer(msg.player);

            game.placeTile(msg.tile, msg.position);

            boardAdapter.notifyDataSetChanged();
        }

        // DONE:
        else if (message instanceof StartGame) {
            StartGame msg = (StartGame) message;
            findPlayer(msg.players);

            playerAdapter.setPlayers(msg.players);
            playerAdapter.setCurrentPlayer(msg.activePlayer);

            handAdapter.setTiles(this.player.getHand());

            bag.setText(msg.bagSize);

            waitingModal.dismiss();
        }

        // DONE:
        else if (message instanceof TileRefill) {
            TileRefill msg = (TileRefill) message;

            if (msg.name.equals(client.name)) {

                for (Tile tile : msg.tiles)
                    handAdapter.add(tile);

                return;
            }

            final Dialog dialog = createDialog(OnlineGame.this, R.layout.tile_notification, Gravity.BOTTOM, WindowManager.LayoutParams.FLAG_FULLSCREEN);

            TextView title = dialog.findViewById(R.id.modal_title);
            title.setText("Refilling hand");

            HandAdapter adapter = new HandAdapter(this, msg.tiles);
            RecyclerView tiles = createHand(dialog.findViewById(R.id.tiles), OnlineGame.this, adapter);

            dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface dialogInterface) {
                    new CountDownTimer(2000, 1000) {
                        @Override
                        public void onTick(long l) {

                        }

                        @Override
                        public void onFinish() {
                            if (dialog.isShowing())
                                dialog.dismiss();
                        }
                    };
                }
            });

            dialog.show();

        }

        // DONE:
        else if (message instanceof Traded) {
            Traded msg = (Traded) message;

            if (msg.name.equals(client.name)) {

                for (Tile tile : msg.tiles)
                    handAdapter.add(tile);

                return;
            }

            final Dialog dialog = createDialog(OnlineGame.this, R.layout.tile_notification, Gravity.BOTTOM, WindowManager.LayoutParams.FLAG_FULLSCREEN);

            TextView title = dialog.findViewById(R.id.modal_title);
            title.setText("Tiles traded");

            HandAdapter adapter = new HandAdapter(this, msg.tiles);
            RecyclerView tiles = createHand(dialog.findViewById(R.id.tiles), OnlineGame.this, adapter);

            dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface dialogInterface) {
                    new CountDownTimer(2000, 1000) {
                        @Override
                        public void onTick(long l) {

                        }

                        @Override
                        public void onFinish() {
                            if (dialog.isShowing())
                                dialog.dismiss();
                        }
                    };
                }
            });

            dialog.show();
        }

    }

    private void findPlayer(List<Player> players) {
        for (Player player : players)
            if (this.player.equals(player)) {
                this.player = player;
                return;
            }
    }

}
