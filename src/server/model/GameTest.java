package server.model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import shared.Weapon;

import static org.junit.Assert.*;

public class GameTest {
    User user = new User("Nicklas", null, null);


    Lobby lobby = new Lobby("lobby" , user);
    Game game = new Game(lobby, 5);

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void makeMove() {
        game.makeMove(user, Weapon.ROCK);
    }

    @Test
    public void start() {
    }
}