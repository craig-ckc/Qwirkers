package Game.Models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Server.Client;

public class ServerGame {
    public final Map<Client, Player> players;
    private Bag bag;
    private Rules rules;
    private Client currentPlayer;
    private Board board;
    private final List<Move> moves;
    private final Map<Position, Integer> points;

    public ServerGame() {
        this.board = new Board();
        this.players = new HashMap<>();
        this.bag = new Bag();
        this.rules = new Rules();
        this.moves = new ArrayList<>();
        this.points = new HashMap<>();
    }

    public Player start() {
        dealHands();
        arrangePlayers();

        currentPlayer = clients().stream().filter(client -> client.turn == 0).findAny().orElse(null);

        return players.get(currentPlayer);
    }

    public int size() {
        return players.size();
    }

    public Client currentPlayer() {
        return currentPlayer;
    }

    public Player player() {
        return players.get(currentPlayer);
    }

    public void addClient(Client client) {
        players.put(client, null);
    }

    public Player removeClient(Client client) {
        return players.remove(client);
    }

    public void registerPlayer(Client client, Player player) {
        players.replace(client, player);
    }

    public List<Client> clients() {
        return new ArrayList<>(players.keySet());
    }

    public List<Player> players() {
        return new ArrayList<>(players.values());
    }

    public int bagSize() {
        return bag.getSize();
    }

    private void dealHands() {
        for (Player player : players.values()) {
            for (int i = 0; i < Player.MAXHANDSIZE; i++) {
                player.receiveTile(bag.takeTile());
            }
        }
    }

    public void arrangePlayers() {
        List<Player> _players;

        if (players.entrySet().iterator().next().getKey().turn == -1)
            _players = rules.playerOrder(new ArrayList<>(players.values()));
        else
            _players = new ArrayList<>(players.values());

        for (Client client : players.keySet()) {
            client.turn = _players.indexOf(players.get(client));
        }
    }

    public Player nextPlayer() {
        Client temp = clients().stream()
                .filter(client -> client.turn == (currentPlayer.turn + 1) % players.size())
                .findAny()
                .orElse(null);

        currentPlayer = temp;

        return players.get(temp);
    }

    public boolean placeTile(Tile tile, Position position) {
        if (!rules.isValid(position, tile))
            return false;

        Move move = new Move(player().removeTile(tile), position);
        board.setBlock(position, tile);
        moves.add(move);

        Map<Position, Integer> entries = rules.scores(move, moves);
        for (Position pos : entries.keySet()){
            if (points.containsKey(pos)){
                if(entries.get(pos) > points.get(pos))
                    points.replace(pos, entries.get(pos));
            }
            else
                points.put(pos, entries.get(pos));
        }

        return true;
    }

    public void scorePlay() {
        int score = 0;

        for (Integer point : points.values())
            score += point;

        player().addPoints((score > 0) ? score : 1);
    }

    public void scorePlay(int score) {
        player().addPoints(score);
    }

    public boolean done() {
        scorePlay();

        moves.clear();
        points.clear();

        if (bag.getSize() < 1 && player().getHand().size() < 1) {
            scorePlay(6);
            return true;
        }

        while (player().getHand().size() < Player.MAXHANDSIZE) {
            Tile tile = bag.takeTile();

            if(tile == null)
                break;

                player().receiveTile(tile);
        }

        nextPlayer();

        return false;
    }
}
