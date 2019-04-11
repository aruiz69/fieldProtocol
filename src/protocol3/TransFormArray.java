package protocol3;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TransFormArray<T> {
    String beginArray;
    String endArray;
    String elementSeparator;
    T transformElement;
    Map<Integer, String> mapperTrans = new HashMap();


    public TransFormArray(String beginArray, String endArray, String elementSeparator, T transformElement, Map<Integer, String> mapperTrans) {
        this.beginArray = beginArray;
        this.endArray = endArray;
        this.elementSeparator = elementSeparator;
        this.transformElement = transformElement;
        this.mapperTrans = mapperTrans;
    }

    public String execute(){

        //List<String> mapperTrans.entrySet().stream().map().collect(Collectors.toList());
        return null;

    }
}
