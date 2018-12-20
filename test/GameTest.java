import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import server.model.Game;
import server.model.Lobby;
import server.model.User;
import shared.Weapon;

public class GameTest {
    User user1 = new User("Nicklas", null, null);
    User user2 = new User("Emil", null, null);
    User user3 = new User("Sabina", null, null);
    User user4 = new User("Dumdum", null, null);


    Lobby lobby = new Lobby("test", user1);
    Game game;

    @Before
    public void setUp() throws Exception {
        lobby.addUser(user2);
        lobby.addUser(user3);
        lobby.addUser(user4);
        game = new Game(lobby);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void makeMove() {
        game.makeMove(user1, Weapon.ROCK);
        game.makeMove(user2, Weapon.ROCK);
        game.makeMove(user3, Weapon.PAPER);
        game.makeMove(user4, Weapon.SCISSORS);

    }

    @Test
    public void start() {

    }
}