package protocol2;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Fields<T> {
    boolean isRequire;
    String name;
    int position;
    PredicateAndMessage<T> constraint;
    MaskForLogging<T> maskForLogging;
    List<Error<T>> errors = new ArrayList<>();

    T value;

    public Fields() {
        isRequire = false;
        name = "";
        position = -1;
    }

    public Fields(boolean isRequire, String name, int position, T value) {
        this.isRequire = isRequire;
        this.name = name;
        this.position = position;
        this.value = value;
    }

    public static class BuilderField<T> {
        Fields<T> field;
        Map<String, Fields> fields;

        public BuilderField(String name) {
            field = new Fields();
            field.name = name;
        }

        public BuilderField addNewField(String name) {
            Fields<T> next = new Fields();
            field = next;
            field.name = name;
            return this;
        }

        public BuilderField isRequire(boolean required) {
            field.isRequire = required;
            return this;
        }

        public BuilderField name(String name) {
            field.name = name;
            return this;
        }

        public BuilderField position(int position) {
            field.position = position;
            return this;
        }

        public BuilderField constrains(PredicateAndMessage<T> constrains) {
            field.constraint = constrains;
            return this;
        }

        public BuilderField maskForLogging(MaskForLogging<T> maskForLogging) {
            field.maskForLogging = maskForLogging;
            return this;
        }

        public BuilderField value(T t) {
            field.setValue(t);
            return this;
        }

        public Fields build(Map<String, Fields> fields) {
            validateLenghtAndIndex(fields);
            return field;
        }

        public BuilderField addInGroup(Map<String, Fields> fields) {
            fields.put(field.name, field);
            return this;
        }

        private void validateLenghtAndIndex(Map<String, Fields> fields) {
            OptionalInt maxVal = fields.values().stream()
                    .mapToInt(f -> f.position)
                    .max();
            int val = maxVal.isPresent() ? maxVal.getAsInt() : -1;
            if (val != fields.size() - 1) {
                //TODO  create exception
            }
        }


    }

    public Fields(boolean isRequire, String name, int position,
                  PredicateAndMessage<T> constraint, MaskForLogging<T> maskFolLogging, T value) {
        this.isRequire = isRequire;
        this.name = name;
        this.position = position;
        this.constraint = constraint;
        this.maskForLogging = maskForLogging;
        this.value = value;
    }

    private void addConstraint(PredicateAndMessage<T> constraint) {
        this.constraint = constraint;
    }

    public Fields[] getArrayFields(Map<String, Fields> fields) {
        Fields lfiedl;
        int size = fields.size();
        Fields[] array = new Fields[size];
        for (Map.Entry<String, Fields> entry : fields.entrySet()) {
            lfiedl = entry.getValue();
            array[lfiedl.position] = lfiedl;
        }
        return array;
    }

    public Fields[] getArrayFieldMask(Map<String, Fields> fields) {
        Fields[] array = getArrayFields(fields);
        List result = Arrays.asList(array).stream().map(t -> {
            t.value = t.getValueMaskForLoggin();
            return t;
        }).collect(Collectors.toList());
        result.toArray(array);
        return array ;
    }

    public void setFieldValue(Map<String, Fields> fields, T t) {
        if (fields.containsKey(name)) {
            fields.get(name).setValue(t);
        }
    }

    public List<Error<T>> applyConstrain(T t) {

        if (constraint != null && !constraint.predicate.test(t)) {
            errors.add(new Error(this, constraint.getMessage()));
        }
        return errors;
    }

    public T getValueMaskForLoggin() {
        if (maskForLogging != null) {
            return maskForLogging.getMaskData(this.value);
        } else {
            return value;
        }
    }

    public void setValue(T value) {
        if (!applyConstrain(value).isEmpty()) {
            throw new RuntimeException("Constrain error");
        }
        this.value = value;
    }

    public T getValue() {
        return value;
    }

    public boolean isRequire() {
        return isRequire;
    }

    public String getName() {
        return name;
    }

    public int getPosition() {
        return position;
    }
}

interface ApplyMask<T> {
    T applyMask(T forMask);
}

class MaskForLogging<T> {
    private T t;
    ApplyMask<T> process;

    MaskForLogging(ApplyMask<T> process) {
        this.process = process;
    }

    public T getMaskData(T t) {
        return process.applyMask(t);
    }

}

class Error<T> {
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

class SellGiftCardMessage {

    public static void main(String[] args) {
        Map<String, Fields> fields = new HashMap<>();
        Fields<String> address = new Fields.BuilderField("address")
                .isRequire(false)
                .constrains(new PredicateAndMessage<String>((s -> s.length() > 5), "Address not valid"))
                .position(0)
                .addInGroup(fields)
                .build(fields);

        Fields<String> name = new Fields.BuilderField("name")
                .isRequire(true)
                .constrains(new PredicateAndMessage<String>((s -> s.matches("(\\w.+)")), "Name not valid"))
                .position(2)
                .addInGroup(fields)
                .build(fields);

        Fields<String> email = new Fields.BuilderField("email")
                .isRequire(true)
                .position(1)
                .addInGroup(fields)
                .build(fields);

        Fields<String> cc = new Fields.BuilderField("cc")
                .isRequire(true)
                .constrains(new PredicateAndMessage<String>((s -> s.matches("\\d{16}")), "CC max 16 digits"))
                .maskForLogging(new MaskForLogging<String>(s -> {
                    char[] array = s.toCharArray();
                    Arrays.fill(array, 4, 12, '*');
                    return new String(array);
                }))
                .position(3)
                .addInGroup(fields)
                .build(fields);


        address.setValue("Villa del Carbon 66");
        name.setValue("Israel Ruiz");
        email.setValue("x222@wnco.com");
        cc.setValue("1111111111111111");
        System.out.println(cc.getValueMaskForLoggin());
        List<String> result =
                Arrays.asList(address.getArrayFieldMask(fields)).stream().map(t -> (String) t.getValue()).collect(Collectors.toList());
        System.out.println(String.join(",", result));


    }


}