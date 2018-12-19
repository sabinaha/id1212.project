package shared;

import java.io.Serializable;

public enum Response implements Serializable {
    // LOGIN
    LOGIN_SUCCESSFUL,
    LOGIN_INCORRECT_CRED,
    LOGIN_FAILED,

    // REGISTRY
    REG_SUCCESSFUL,
    REG_DUPL_USERNAME,
    REG_FAILED,

    //
    LOBBY_CREATE_SUCCESS,
    LOBBY_JOIN_SUCCESS,
    LOBBY_JOIN_FAILED,
    LOBBY_ALREADY_EXISTS,
    LOBBY_DONT_EXISTS,

    // MISC
    UNKNOWN_ERROR
}
