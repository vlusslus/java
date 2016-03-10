package ru.cbr.candidatesCheck.web;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.cbr.candidatesCheck.domen.Candidate;
import ru.cbr.candidatesCheck.service.CandidateService;
import java.util.List;

@RestController
@RequestMapping(value = "/candidate")
public class CandidateController {
    @Autowired
    private CandidateService candidateService;

    public CandidateController() {

    }

    @RequestMapping(method = RequestMethod.GET)
    List<Candidate> candidate(){
        return candidateService.getAll();
    }

    @RequestMapping(method = RequestMethod.POST)
    Candidate add(@RequestBody Candidate candidate){
        return candidateService.add(candidate);
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public List<Candidate> delete(@RequestBody Candidate candidate) {
        candidateService.delete(candidate);
        System.out.println(candidate.getName());
        return candidateService.getAll();
    }
}