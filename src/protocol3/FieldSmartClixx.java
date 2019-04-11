package protocol3;




import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FieldSmartClixx <T> {
    //Conditions Field
    private boolean isRequire;
    private String name;
    private int position;
    private PredicateAndMessage<T> constraint;
    private MaskCCForLogging<T> maskForLogging;
    private List<Error<T>> errors = new ArrayList<>();



    public boolean isRequire() {
        return isRequire;
    }

    public void setRequire(boolean require) {
        isRequire = require;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public PredicateAndMessage<T> getConstraint() {
        return constraint;
    }

    public void setConstraint(PredicateAndMessage<T> constraint) {
        this.constraint = constraint;
    }

    public MaskCCForLogging<T> getMaskForLogging() {
        return maskForLogging;
    }

    public void setMaskForLogging(MaskCCForLogging<T> maskForLogging) {
        this.maskForLogging = maskForLogging;
    }

    public List<Error<T>> getErrors() {
        return errors;
    }

    public void setErrors(List<Error<T>> errors) {
        this.errors = errors;
    }


}
