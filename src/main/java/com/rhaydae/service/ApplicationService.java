package com.rhaydae.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.rhaydae.dto.ApplicationRequest;
import com.rhaydae.dto.ApplicationResponse;
import com.rhaydae.entities.Application;
import com.rhaydae.entities.JobOffers;
import com.rhaydae.entities.User;
import com.rhaydae.enums.ApplicationStatus;
import com.rhaydae.enums.Role;
import com.rhaydae.repositories.ApplicationRepository;
import com.rhaydae.repositories.JobOffersRepository;
import com.rhaydae.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ApplicationService {
	
	private final ApplicationRepository applicationRepository;
	private final UserRepository userRepository;
	private final JobOffersRepository jobOffersRepository;
	private final NotificationService notificationService;
	private final EmailService emailService;

	private User findUser(String email) {
        
		   return userRepository.findByEmail(email)
                           .orElseThrow(() -> new RuntimeException("User not found"));
    }
	
	private Application findApplication(Long applicationId) {
        return applicationRepository.findById(applicationId)
            .orElseThrow(() -> new RuntimeException("Application not found"));
    }
	
	private ApplicationResponse toResponse(Application application) {
        return new ApplicationResponse(
            application.getId(),
            application.getAppliedAt(),
            application.getStatus(),
            application.getCandidate().getId(),
            application.getJobOffers().getId()
        );
    }
	
	@Transactional
    public ApplicationResponse apply(ApplicationRequest dto, String email) {

        User candidate = findUser(email);

        if (candidate.getRole() != Role.CANDIDATE) {
            throw new AccessDeniedException(
                "Only candidates can apply for job offers");
        }

        JobOffers jobOffer = jobOffersRepository
            .findById(dto.jobOfferId())
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Job offer not found"));

        if (!Boolean.TRUE.equals(jobOffer.getActive())) {
            throw new ResponseStatusException(
                HttpStatus.CONFLICT, "This job offer is inactive");
        }

        if (applicationRepository
                .existsByCandidate_IdAndJobOffers_Id(
                    candidate.getId(), jobOffer.getId())) {
            throw new ResponseStatusException(
                HttpStatus.CONFLICT,
                "You have already applied for this job offer");
        }

        Application application = Application.builder()
            .candidate(candidate)
            .jobOffers(jobOffer)
            .appliedAt(LocalDateTime.now())
            .status(ApplicationStatus.PENDING)
            .build();

        return toResponse(applicationRepository.save(application));
    }
	
	@Transactional(readOnly = true)
    public List<ApplicationResponse> getMyApplications(String email) {
        User candidate = findUser(email);

        if (candidate.getRole() != Role.CANDIDATE) {
            throw new AccessDeniedException(
                "Only candidates can view their applications");
        }

        return applicationRepository
            .findByCandidate_Id(candidate.getId())
            .stream()
            .map(this::toResponse)
            .toList();
    }
	
	@Transactional(readOnly = true)
    public List<ApplicationResponse> getApplicationsForJob(Long jobOfferId, String email) {

        User recruiter = findUser(email);

        if (recruiter.getRole() != Role.RECRUITER) {
            throw new AccessDeniedException(
                "Only recruiters can view applications for their offers");
        }

        JobOffers jobOffer = jobOffersRepository.findById(jobOfferId)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Job offer not found"));

        return applicationRepository
            .findByJobOffers_Id(jobOffer.getId())
            .stream()
            .map(this::toResponse)
            .toList();
    }

	@Transactional
    public ApplicationResponse updateStatus(Long applicationId, ApplicationStatus newStatus, String email) {

        User recruiter = findUser(email);

        if (recruiter.getRole() != Role.RECRUITER) {
            throw new AccessDeniedException(
                "Only recruiters can update application status");
        }

        if (newStatus == null || newStatus == ApplicationStatus.PENDING) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Status must be ACCEPTED or REJECTED");
        }

        Application application = findApplication(applicationId);
        
        ApplicationStatus oldStatus = application.getStatus();
        
        if(oldStatus == newStatus ) {
        	return toResponse(application);
        }

        application.setStatus(newStatus);
        
        Application savedApplication =
                applicationRepository.save(application);
        
        String message;
        
        if (newStatus == ApplicationStatus.ACCEPTED) {
            message = "Votre candidature pour l'offre \""
                    + application.getJobOffers().getTitle()
                    + "\" a été acceptée.";
        } else {
            message = "Votre candidature pour l'offre \""
                    + application.getJobOffers().getTitle()
                    + "\" a été refusée.";
        }
        
        // Créer une notification pour le candidat.
        notificationService.createNotif(
                application.getCandidate().getId(),
                message);
        
     // Envoyer un email au candidat
        emailService.sendApplicationStatusEmail(
                application.getCandidate().getEmail(),
                application.getJobOffers().getTitle(),
                newStatus.name()
        );

        return toResponse(applicationRepository.save(savedApplication));
    }
	

}
