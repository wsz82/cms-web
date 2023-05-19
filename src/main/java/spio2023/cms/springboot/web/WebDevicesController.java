package spio2023.cms.springboot.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import spio2023.cms.springboot.database.repository.DeviceRepository;

@Controller
public class WebDevicesController {
    private final DeviceRepository deviceRepository;

    public WebDevicesController(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

    @GetMapping("/home/devices")
    public String devices(Model model) {
        model.addAttribute("devices", deviceRepository.findAllListedProjectedBy());
        return "devices";
    }
}