package spio2023.cms.springboot.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import spio2023.cms.springboot.web.StepFill;
import spio2023.cms.springboot.web.service.WebCalibrationService;

@Controller
public class WebCalibrationController {
    private final WebCalibrationService webCalibrationService;

    public WebCalibrationController(WebCalibrationService webCalibrationService) {
        this.webCalibrationService = webCalibrationService;
    }

    @GetMapping("/home/calibration-service/{procedureId}")
    public String initCalibration(@PathVariable Long procedureId) {
        var calibrationId = webCalibrationService.initCalibration(procedureId);
        return "redirect:/home/calibration-service/" + calibrationId + "/1";
    }

    @GetMapping("/home/calibration-service/{calibrationId}/{stepNumber}")
    public String showStep(Model model, @PathVariable Long calibrationId, @PathVariable int stepNumber) {
        webCalibrationService.showStep(model, calibrationId, stepNumber);
        return "calibration";
    }

    @PostMapping("/home/calibration-service/{calibrationId}/{stepNumber}")
    public String fillStep(@ModelAttribute StepFill stepFill, @PathVariable Long calibrationId, @PathVariable int stepNumber) {
        var stepFillResult = webCalibrationService.stepFilll(stepFill, calibrationId, stepNumber);
        if (stepFillResult.isLastStep()) {
            return "redirect:/results/search/findAllByCalibrationId?id=" + calibrationId;
//            return "redirect:/home/calibration-service/" + calibrationId + "/results"; //todo Results page
        }
        var nextStepNumber = stepNumber + 1;
        return "redirect:/home/calibration-service/" + calibrationId + "/" + nextStepNumber;
    }

}