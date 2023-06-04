package spio2023.cms.springboot.web.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import spio2023.cms.core.unit.Prefix;
import spio2023.cms.springboot.database.repository.CalibrationRepository;
import spio2023.cms.springboot.database.repository.ResultRepository;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;

@Controller
public class WebReportController {

    private final ResultRepository resultRepository;
    private final CalibrationRepository calibrationRepository;

    public WebReportController(ResultRepository resultRepository, CalibrationRepository calibrationRepository) {
        this.resultRepository = resultRepository;
        this.calibrationRepository = calibrationRepository;
    }

    @GetMapping("/home/reports/{calibrationId}/download")
    public void reportDownload(@PathVariable Long calibrationId, HttpServletResponse response) {
        var json = requestJson(calibrationId);
        requestReport(response, json);
    }

    private void requestReport(HttpServletResponse response, String json) {
        var url = "https://demo.cs-cart.pl/calibration/index.php?dispatch=ss_calibration.raport";
        try (var httpClient = HttpClients.createDefault()) {
            var httpPost = new HttpPost(url);
            var requestEntity = new StringEntity(json, ContentType.APPLICATION_JSON);
            httpPost.setEntity(requestEntity);
            var pdfResponse = httpClient.execute(httpPost);
            var responseEntity = pdfResponse.getEntity();
            var inputStream = responseEntity.getContent();
            var bytes = inputStream.readAllBytes();

            try (var baos = new ByteArrayOutputStream()) {
                response.setHeader("Content-Disposition", "attachment; filename=plik.pdf");
                response.setContentType("application/pdf");

                baos.write(bytes);
                baos.flush();

                response.setContentLength(baos.size());

                baos.writeTo(response.getOutputStream());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String requestJson(Long calibrationId) {
        var results = resultRepository.findAllByCalibrationId(calibrationId);
        var calibration = calibrationRepository.findById(calibrationId).get();
        var procedure = calibration.getProcedure();
        var instrument = procedure.getInstrument();

        var resultsJson = new JSONArray();
        var timestamp = new SimpleDateFormat("yyyy-MM-dd").format(calibration.getTimestamp());
        var rawJson = new JSONObject()
                .put("_report", new JSONObject()
                        .put("timestamp", timestamp)
                        .put("deviceSerialNumber", calibration.getDeviceSerialNumber())
                        .put("procedureName", procedure.getName())
                        .put("referenceInstrument", instrument.getModel())
                        .put("results", resultsJson)
                );
        for (var result: results) {
            var controlPoint = result.getControlPoint();
            var measurementType = result.getMeasurementType();

            var units = measurementType.getUnits();
            var parameters = controlPoint.getParameters();

            var size = units.size();
            if (size != parameters.size()) {
                throw new IllegalArgumentException("Units and Parameters size is not the same.");
            }
            var testValueUnit = units.get(0).getName();
            var sb = new StringBuilder();
            sb.append(measurementType.getSymbol()).append(" ").append(measurementType.getName()).append("/ ");
            for (int i = 0; i < size; i++) {
                var parameter = parameters.get(i);
                var unit = units.get(i);
                var paramString = parameter.getValue() + Prefix.valueOf(parameter.getPrefix()).getSymbol() + unit.getName();
                sb.append(paramString).append(" ");
            }

            var description = sb.toString();

            var symbol = result.getPrefix().getSymbol();
            var suffix = " " + symbol + testValueUnit;
            var refValue = result.getMeanReferenceValue();
            var testValue = result.getMeanTestValue();
            var resultJson = new JSONObject()
                    .put("controlPoint", description)
                    .put("timestamp", result.getTimestamp())
                    .put("meanReferenceValue", refValue + suffix)
                    .put("meanTestValue", testValue + suffix)
                    .put("error", result.getError() + suffix)
                    .put("uncertainty", result.getUncertainty() + suffix)
                    .put("lowerBoundary", result.getLowerBoundary() + suffix)
                    .put("upperBoundary", result.getUpperBoundary() + suffix)
                    .put("pass", result.isPass());
            resultsJson.put(resultJson);
        }
        return rawJson.toString();
    }

}