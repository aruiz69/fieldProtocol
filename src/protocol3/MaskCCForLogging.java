package protocol3;



public class MaskCCForLogging<T> {
    private T t;
    ApplyMask<T> process;

    MaskCCForLogging(ApplyMask<T> process) {
        this.process = process;
    }

    public T getMaskData(T t) {
        return process.applyMask(t);
    }


}

interface ApplyMask<T> {
    T applyMask(T forMask);
}