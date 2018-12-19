package shared;

import java.io.Serializable;

public enum Response implements Serializable {
    // LOGIN
    LOGIN_SUCCESSFUL,
    LOGIN_INCORRECT_CRED, //Måste tas hand om som ett exception, eftersom att ingen är inloggad
    LOGIN_FAILED, //Måste tas hand om som ett exception, eftersom att ingen är inloggad

    // REGISTRY
    REG_SUCCESSFUL, //Måste tas hand om som ett exception, eftersom att ingen är inloggad
    REG_DUPL_USERNAME, //Måste tas hand om som ett exception, eftersom att ingen är inloggad
    REG_FAILED, //Måste tas hand om som ett exception, eftersom att ingen är inloggad

    //
    LOBBY_CREATE_SUCCESS,
    LOBBY_JOIN_SUCCESS,
    LOBBY_JOIN_FAILED, //Måste tas hand om som ett exception, om ingen är inloggad
    LOBBY_ALREADY_EXISTS,
    LOBBY_DONT_EXISTS,
    LOBBY_GAME_ONGOING_ERROR,

    // MISC
    UNKNOWN_ERROR
}
