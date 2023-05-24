package spio2023.cms.springboot.service;

import spio2023.cms.core.procedure.Setting;
import spio2023.cms.core.procedure.StepInterface;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;
import java.util.stream.IntStream;

public class WebStepInterface implements StepInterface {
    private final Setting setting;
    private final Stack<Double> referenceInputs;
    private final Stack<Double> testInputs;

    public WebStepInterface(Setting setting, List<Double> referenceInputs, List<Double> testInputs) {
        this.setting = setting;
        this.referenceInputs = makeInputsStack(referenceInputs);
        this.testInputs = makeInputsStack(testInputs);
    }

    @Override
    public void showMessage(String s) {}

    @Override
    public Double[] getReferencedInput() {
        return provideInput(setting.getMeasurementSeries(), referenceInputs);
    }

    @Override
    public Double[] getCheckedInput() {
        return provideInput(setting.getMeasurementSeries(), testInputs);
    }

    private Double[] provideInput(int measurementsNumber, Stack<Double> source) {
        Double[] input = new Double[measurementsNumber];
        IntStream.range(0, measurementsNumber).forEach(i -> {
            input[i] = source.pop();
        });
        return input;
    }

    private Stack<Double> makeInputsStack(List<Double> inputs) {
        var inputsCopy = new ArrayList<>(inputs);
        Collections.reverse(inputsCopy);
        var nextInputs = new Stack<Double>();
        nextInputs.addAll(inputsCopy);
        return nextInputs;
    }
}
