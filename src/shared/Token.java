package shared;

import java.io.Serializable;

/**
 * Token identifying a logged in user
 */
public class Token implements Serializable {

    private final long id;

    public Token(int id) {
        this.id = id;
    }

    public long getId() {
        return this.id;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Token))
            return false;
        Token compareTo = (Token) obj;
        if (compareTo.getId() != this.id)
            return false;
        return true;
    }
}
