package ch.alv.batches.partnerjobs;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;

/**
 * Controller to start the a
 */
@Controller
public class PartnerJobController {


    @Resource(name = "partnerJobsImportJob")
    private Job partnerJobsImportJob;

    @Resource
    private JobLauncher jobLauncher;

    @RequestMapping(value = "/jobs/partnerJobImport", method = RequestMethod.GET)
    public HttpEntity startCompanyImport() throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {
        JobExecution result = jobLauncher.run(partnerJobsImportJob, null);
        if (result.getExitStatus().getExitCode().equals("FAILED")) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }


}
