package protocol3;

import java.util.function.Predicate;

class PredicateAndMessage<T> {
    final String message;
    final Predicate<T> predicate;

    public PredicateAndMessage(Predicate<T> predicate, String message) {
        this.message = message;
        this.predicate = predicate;
    }

    public String getMessage() {
        return message;
    }

    public Predicate<T> getPredicate() {
        return predicate;
    }

}
