package spio2023.cms.springboot.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import spio2023.cms.core.sample.SampleData_BC06;
import spio2023.cms.core.sample.SampleData_PP_METEX_3610;
import spio2023.cms.springboot.database.model.procedure.Procedure;
import spio2023.cms.springboot.database.repository.ProcedureRepository;

@Configuration
public class LoadDatabase {

    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

    @Bean
    CommandLineRunner initDatabase(ProcedureRepository repository) {
        repository.deleteAll();
        return args -> {
            log.info("Preloading " + repository.save(new Procedure("BC06", SampleData_BC06.procedure())));
            log.info("Preloading " + repository.save(new Procedure("PP METEX3610", SampleData_PP_METEX_3610.procedure())));
        };
    }

}
