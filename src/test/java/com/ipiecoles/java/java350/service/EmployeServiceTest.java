package com.ipiecoles.java.java350.service;

import com.ipiecoles.java.java350.exception.EmployeException;
import com.ipiecoles.java.java350.model.Employe;
import com.ipiecoles.java.java350.model.Entreprise;
import com.ipiecoles.java.java350.model.NiveauEtude;
import com.ipiecoles.java.java350.model.Poste;
import com.ipiecoles.java.java350.repository.EmployeRepository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityExistsException;
import java.time.LocalDate;

import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class EmployeServiceTest {

    @InjectMocks
    private EmployeService employeService;
    @Mock
    private EmployeRepository employeRepository;

    @Test
    void testEmbaucheEmployeUn() throws EmployeException {
        //Given
        String nom = "Chirac";
        String prenom ="Jacques";
        Poste poste =Poste.TECHNICIEN;
        NiveauEtude niveauEtude = NiveauEtude.BTS_IUT;
        Double tempPartiel = 1.0;

        when(employeRepository.findLastMatricule()).thenReturn(null);
        when(employeRepository.findByMatricule("T00001")).thenReturn(null);

        //When
        employeService.embaucheEmploye(nom,prenom, poste, niveauEtude,tempPartiel);


        //Then
        ArgumentCaptor<Employe> employeCaptor = ArgumentCaptor.forClass(Employe.class);
        Mockito.verify(employeRepository).save(employeCaptor.capture());
        Employe employe = employeCaptor.getValue();

        Assertions.assertThat(employe.getNom()).isEqualTo(nom);
        Assertions.assertThat(employe.getPrenom()).isEqualTo(prenom);
        Assertions.assertThat(employe.getSalaire()).isEqualTo(1825.464);
        Assertions.assertThat(employe.getTempsPartiel()).isEqualTo(1.0);
        Assertions.assertThat(employe.getDateEmbauche()).isEqualTo(LocalDate.now());
        Assertions.assertThat(employe.getMatricule()).isEqualTo("T00001");

    }

    @Test
    void testEmbaucheEmployeExisteDeja() throws EmployeException {
        //Given
        String nom = "Doe";
        String prenom = "John";
        Poste poste = Poste.TECHNICIEN;
        NiveauEtude niveauEtude = NiveauEtude.BTS_IUT;
        Double tempsPartiel = 1.0;
        Employe employeExistant = new Employe("Doe", "John", "T00001", LocalDate.now(), 1500d, 1, 1.0);

        when(employeRepository.findLastMatricule()).thenReturn(null);
        when(employeRepository.findByMatricule("T00001")).thenReturn(employeExistant);

        //When
        try {
            employeService.embaucheEmploye(nom, prenom, poste, niveauEtude, tempsPartiel);
            Assertions.fail("la méthode aurait dû lever une exception");
        }catch (Exception e){

            //Then
            Assertions.assertThat(e).isInstanceOf(EntityExistsException.class);
            Assertions.assertThat(e.getMessage()).isEqualTo("L'employé de matricule T00001 existe déjà en BDD");
            Mockito.verify(employeRepository, Mockito.never()).save(Mockito.any(Employe.class));
        }
    }

    @ParameterizedTest
    @CsvSource({
            " 0 , 1 ",
            " 890 , 1 ",
            " 933 , 1 ",
            " 1100 , 3 ",
            " 1560 , 6",
    })
    void testCalculPerformanceCommercial(Long caTraite, Integer newPerformance) throws EmployeException {
        //Given
        String matricule = "C13568";
        Long objectifCa = 1000L;
        Employe employe = new Employe("Michael","Scott",matricule,LocalDate.now(), Entreprise.SALAIRE_BASE,1,1d);
        when(employeRepository.findByMatricule(matricule)).thenReturn(employe);
        when(employeRepository.avgPerformanceWhereMatriculeStartsWith("C")).thenReturn(1d);
        when(employeRepository.save(Mockito.any(Employe.class))).thenAnswer(AdditionalAnswers.returnsFirstArg());

        //When
        employeService.calculPerformanceCommercial(matricule,caTraite,objectifCa);

        //Then
        ArgumentCaptor<Employe> employeCaptor = ArgumentCaptor.forClass(Employe.class);
        Mockito.verify(employeRepository).save(employeCaptor.capture());
        Employe employeWithNewPerformance = employeCaptor.getValue();
        Assertions.assertThat(employeWithNewPerformance.getPerformance()).isEqualTo(newPerformance);
    }

    @ParameterizedTest
    @CsvSource({
            "C13568, , 20000, Le chiffre d'affaire traité ne peut être négatif ou null !",
            "C13568, -20000, 20000, Le chiffre d'affaire traité ne peut être négatif ou null !",

            "C13568, 20000, , L'objectif de chiffre d'affaire ne peut être négatif ou null !",
            "C13568, 20000, -60000, L'objectif de chiffre d'affaire ne peut être négatif ou null !",

            "T00001, 20000, 20000, Le matricule ne peut être null et doit commencer par un C !",
            ", 20000, 20000, Le matricule ne peut être null et doit commencer par un C !",

            "C13568, 20000, 30000, Le matricule C13568 n'existe pas !",
    })
    void testCalculPerformanceCommercialExceptions(String matricule, Long caTraite, Long objectifCa, String exceptionMessage) {
        //Given
        try {
            //When
            employeService.calculPerformanceCommercial(matricule, caTraite, objectifCa);
            Assertions.fail("La méthode aurait du lancer une exception");
        } catch (Exception e) {
            //Then
            Assertions.assertThat(e).isInstanceOf(EmployeException.class);
            Assertions.assertThat(e.getMessage()).isEqualTo(exceptionMessage);
        }
    }
}