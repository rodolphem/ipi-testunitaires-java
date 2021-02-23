package com.ipiecoles.java.java350.model;

import com.ipiecoles.java.java350.service.EmployeService;
import org.apache.commons.lang.ObjectUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.LocalDate;

public class EmployeTest {

    @Test
    //Embauché en 2020. On est en 2020 : 0 an ancienneté
    public void testNombreAnneeAncienneteNow(){
        //Given
        LocalDate now = LocalDate.now();
        Employe employe = new Employe();
        employe.setDateEmbauche(now);

        //When
        Integer nbAnneesAnciennete = employe.getNombreAnneeAnciennete();

        //Then
        Assertions.assertThat(nbAnneesAnciennete).isEqualTo(0);
    }

    @Test
    //Embauché en 2019. On est en 2020 : 1 an ancienneté
    public void testNombreAnneeAncienneteNMoins1(){
        //Given
        LocalDate nMoins1 = LocalDate.now().minusYears(1);
        Employe employe = new Employe();
        employe.setDateEmbauche(nMoins1);

        //When
        Integer nbAnneesAnciennete = employe.getNombreAnneeAnciennete();

        //Then
        Assertions.assertThat(nbAnneesAnciennete).isEqualTo(1);
    }

    @Test
    //Embauché en 2021. On est en 2020 : 0 an ancienneté
    public void testNombreAnneeAncienneteNPlus1(){
        //Given
        LocalDate nPlus1 = LocalDate.now().plusYears(1);
        Employe employe = new Employe();
        employe.setDateEmbauche(nPlus1);

        //When
        Integer nbAnneesAnciennete = employe.getNombreAnneeAnciennete();

        //Then
        Assertions.assertThat(nbAnneesAnciennete).isEqualTo(0);
    }

    @Test
    //Embauché en 2021. On est en 2020 : 0 an ancienneté
    public void testNombreAnneeAncienneteNull(){
        //Given
        Employe employe = new Employe();
        employe.setDateEmbauche(null);

        //When
        Integer nbAnneesAnciennete = employe.getNombreAnneeAnciennete();

        //Then
        Assertions.assertThat(nbAnneesAnciennete).isEqualTo(0);
    }

    @Test
    //le pourcentage est t-il présent
    public void testAugmenterSalairePourcentagePresent(){
        //Given
         Employe employe = new Employe();
         Double pourcentage = 0d;

         //When
        employe.augmenterSalaire(pourcentage);

         //Then
        Assertions.assertThat(pourcentage).isNotEqualTo(null);
    }


    @Test
    //test pourcentage supérieur à 1
    public void testAugmenterSalaireInf(){
        //Given
        Double pourcentage = 2d;
        Employe employe = new Employe();
        employe.setSalaire(1000d);
        Double resultat = employe.getSalaire();

        //When
        employe.augmenterSalaire(pourcentage);

        //Then
        Assertions.assertThat(employe.getSalaire()).isGreaterThan(1);

    }


    @Test
    //le salaire à t-il augmenté
    public void testAugmenterSalaireAugmenter(){
        //Given
        Double pourcentage = 2d;
        Employe employe = new Employe();
        employe.setSalaire(1000d);

        //When
        employe.augmenterSalaire(pourcentage);

        //Then
        Assertions.assertThat(employe.getSalaire()).isGreaterThan(1000.0);
    }

    @Test
    //l'employe à t-il un salaire
    public void testAugmenterSalairePrésent(){
        //Given
        Employe employe = new Employe();
        Double pourcentage = 1.3d;
        employe.setSalaire(null);

        //Then
        Assertions.assertThatThrownBy(() -> employe.augmenterSalaire(pourcentage)).hasMessage("Le salaire ne peut pas être nul");
    }


    //Test paramétré pour la NbRTT
    @ParameterizedTest
    @CsvSource({
            "'2019-01-01', 8",
            "'2021-01-01', 10",
            "'2022-01-01', 10",
            "'2032-01-01', 11" })
    void testNbRTT(LocalDate date, Integer nbRTT) {
        Employe employe = new Employe("Saint", "Exupery", "M00123", LocalDate.now(), 1600.0, 1, 1.0);

        Integer nbRtt = employe.getNbRtt(date);
        Assertions.assertThat(nbRtt).isEqualTo(nbRTT);
    }

    @Test
    public void testCalculPerformanceCommercialCaTraiteVautNegatif(){
        //Given
        EmployeService employeService = new EmployeService();

        Assertions.assertThatThrownBy(() -> employeService.calculPerformanceCommercial("C233", -2l, 2l)).hasMessage("Le chiffre d'affaire traité ne peut être négatif ou null !");
    }

    @Test
    public void testCalculPerformanceCommercialCaTraiteVautNull(){
        //Given
        EmployeService employeService = new EmployeService();

        Assertions.assertThatThrownBy(() -> employeService.calculPerformanceCommercial("C233", null, 2l)).hasMessage("Le chiffre d'affaire traité ne peut être négatif ou null !");
    }

    @Test
    public void testCalculPerformanceCommercialObjectifCaNegatif(){
        //Given
        EmployeService employeService = new EmployeService();

        Assertions.assertThatThrownBy(() -> employeService.calculPerformanceCommercial("C1432", 2l, -2l)).hasMessage("L'objectif de chiffre d'affaire ne peut être négatif ou null !");
    }

    @Test
    public void testCalculPerformanceCommercialObjectifCaNull(){
        //Given
        EmployeService employeService = new EmployeService();

        Assertions.assertThatThrownBy(() -> employeService.calculPerformanceCommercial("C1432", 2l, null)).hasMessage("L'objectif de chiffre d'affaire ne peut être négatif ou null !");
    }

    @Test
    public void testCalculPerformanceCommercialMatriculeStartWithC(){
        //Given
        EmployeService employeService = new EmployeService();

        Assertions.assertThatThrownBy(() -> employeService.calculPerformanceCommercial("P1234", 2l, 2l)).hasMessage("Le matricule ne peut être null et doit commencer par un C !");
    }

    @Test
    public void testCalculPerformanceCommercialMatriculeNull(){
        //Given
        EmployeService employeService = new EmployeService();

        Assertions.assertThatThrownBy(() -> employeService.calculPerformanceCommercial(null, 2l, 2l)).hasMessage("Le matricule ne peut être null et doit commencer par un C !");
    }

}
