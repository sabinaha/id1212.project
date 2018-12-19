package shared;

public enum Response {
    // LOGIN
    LOGIN_SUCCESSFUL,
    LOGIN_INCORRECT_CRED,
    LOGIN_FAILED,

    // REGISTRY
    REG_SUCCESSFUL,
    REG_DUPL_USERNAME,
    REG_FAILED,

    // MISC
    UNKNOWN_ERROR
}
