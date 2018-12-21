package shared;

import java.io.Serializable;

/**
 * Enums which the server uses to send feedback to the client.
 */
public enum Response implements Serializable {
    // LOGIN
    LOGIN_SUCCESSFUL,
    LOGIN_INCORRECT_CRED,

    // REGISTRY
    REG_SUCCESSFUL,
    REG_DUPL_USERNAME,
    REG_FAILED,

    //LOBBY
    LOBBY_CREATE_SUCCESS,
    LOBBY_JOIN_SUCCESS,
    LOBBY_JOIN_FAILED,
    LOBBY_ALREADY_EXISTS,
    LOBBY_DONT_EXISTS,
    LOBBY_GAME_ONGOING_ERROR,
    LOBBY_USER_NOT_IN_LOBBY,
    LOBBY_LEAVE_SUCCESSFUL,

    //GAME
    GAME_STARTED,
    GAME_DONE,
    GAME_PROMPT_ACTION,
    GAME_NO_ONGOING_GAME,
    GAME_CANT_START_SOLO
}
