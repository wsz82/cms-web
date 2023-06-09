package spio2023.cms.springboot.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import spio2023.cms.springboot.database.projection.CalibrationElement;
import spio2023.cms.springboot.web.dto.CalibrationFill;
import spio2023.cms.springboot.web.dto.StepFill;
import spio2023.cms.springboot.web.service.WebCalibrationService;

import java.util.List;

@Controller
public class WebCalibrationController {
    private final WebCalibrationService webCalibrationService;

    public WebCalibrationController(WebCalibrationService webCalibrationService) {
        this.webCalibrationService = webCalibrationService;
    }

    @GetMapping("/home/calibration-service/{procedureId}")
    public String initCalibration(Model model, @PathVariable Long procedureId) {
        model.addAttribute("procedureId", procedureId);
        model.addAttribute("calibrationFill", new CalibrationFill());
        return "calibration_init";
    }

    @PostMapping("/home/calibration-service/{procedureId}")
    public String startCalibration(@ModelAttribute CalibrationFill calibrationFill, @PathVariable Long procedureId) {
        var calibrationId = webCalibrationService.initCalibration(procedureId, calibrationFill);
        return "redirect:/home/calibration-service/" + calibrationId + "/1";
    }

    @GetMapping("/home/calibration-service/{calibrationId}/{stepNumber}")
    public String showStep(Model model, @PathVariable Long calibrationId, @PathVariable int stepNumber,
                           @RequestParam(required = false) String hasPassed,
                           @RequestParam(required = false) String wasInputStep) {
        var stepDTO = webCalibrationService.showStep(calibrationId, stepNumber);
        model.addAttribute("calibrationId", stepDTO.getCalibrationId());
        model.addAttribute("procedureName", stepDTO.getProcedureName());
        model.addAttribute("isLastStep", stepDTO.isLastStep());
        model.addAttribute("isInputStep", stepDTO.isInputStep());
        model.addAttribute("referenceValue", stepDTO.getReferenceValue());
        model.addAttribute("stepFill", stepDTO.getStepFill());
        model.addAttribute("referenceValuesFromControlPoint", stepDTO.isReferenceValuesFromControlPoint());
        model.addAttribute("measurementName", stepDTO.getMeasurementName());
        model.addAttribute("measurementSymbol", stepDTO.getMeasurementSymbol());
        model.addAttribute("parameters", stepDTO.getParameters());
        model.addAttribute("message", stepDTO.getMessage());
        model.addAttribute("resolution", stepDTO.getResolution());
        model.addAttribute("hasPassed", hasPassed);
        model.addAttribute("wasInputStep", wasInputStep);
        return "calibration";
    }

    @PostMapping("/home/calibration-service/{calibrationId}/{stepNumber}")
    public String fillStep(@ModelAttribute StepFill stepFill, @PathVariable Long calibrationId, @PathVariable int stepNumber,
                           RedirectAttributes redirectAttributes) {
        var stepFillResult = webCalibrationService.stepFill(stepFill, calibrationId, stepNumber);
        if (stepFillResult.isLastStep()) {
//            return "redirect:/results/search/findAllByCalibrationId?id=" + calibrationId;
            return "redirect:/home/calibrations";
        }
        redirectAttributes.addAttribute("hasPassed", stepFillResult.isPass());
        redirectAttributes.addAttribute("wasInputStep", stepFillResult.isWasInputStep());
        var nextStepNumber = stepNumber + 1;
        return "redirect:/home/calibration-service/" + calibrationId + "/" + nextStepNumber;
    }


    @GetMapping("/home/calibrations")
    public String calibrations(Model model) {
        var calibrations = webCalibrationService.listedCalibrations();
        model.addAttribute("calibrations", calibrations);
        return "calibrations";
    }
}