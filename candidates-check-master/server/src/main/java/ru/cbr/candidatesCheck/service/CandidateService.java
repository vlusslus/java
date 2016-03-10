package ru.cbr.candidatesCheck.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.cbr.candidatesCheck.domen.Candidate;
import ru.cbr.candidatesCheck.repository.ICandidateRepository;
import javax.annotation.PostConstruct;
import java.util.List;

@Service
public class CandidateService {
    @Autowired
    private ICandidateRepository candidateRepository;


    @PostConstruct
    public void init() {
        candidateRepository.save(new Candidate("Иван", "Иванов"));
        candidateRepository.save(new Candidate("Петр", "Петров"));
        candidateRepository.save(new Candidate("Василий", "Васичкин"));
    }

    @Transactional
    public List<Candidate> getAll(){
        return candidateRepository.findAll();
    }

    @Transactional
    public Candidate add(Candidate candidate){
        for (Candidate storedCandidate:getAll()){
            if (storedCandidate.getName().equalsIgnoreCase(candidate.getName()) &&
                    storedCandidate.getSurname().equalsIgnoreCase(candidate.getSurname())){
                throw new RuntimeException();
            }
        }
        candidateRepository.save(candidate);
        return candidate;
    }
    @Transactional
    public void delete(Candidate candidate)
    {
        candidateRepository.delete(candidate);
    }
}