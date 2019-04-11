package protocol3;

import protocol2.Fields;

public  class Error<T> {
    Fields<T> field;
    String message;

    public Error(Fields<T> field, String message) {
        this.field = field;
        this.message = message;
    }

    public Fields<T> getField() {
        return field;
    }

    public String getMessage() {
        return message;
    }

}