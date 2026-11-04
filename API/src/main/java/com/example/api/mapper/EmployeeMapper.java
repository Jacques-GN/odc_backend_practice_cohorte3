package com.example.api.mapper;

import com.example.api.dto.EmployeeRequestDTO;
import com.example.api.dto.EmployeeResponseDTO;
import com.example.api.model.Employee;

/**
 * ============================================================================
 * CLASSE UTILITAIRE : EmployeeMapper
 * ============================================================================
 * 
 * 1. RÔLE (L'analogie du "Traducteur" ou "Douanier") :
 * ---------------------------------------------------
 * Cette classe fait le pont entre deux mondes distincts :
 * - Le monde EXTÉRIEUR (Le Client / Le Réseau) : dialogue via des DTOs
 *   (Data Transfer Objects).
 * - Le monde INTÉRIEUR (L'Application / La BDD) : dialogue via des Entités JPA
 *   (@Entity Employee).
 * 
 * 2. POURQUOI NE PAS EXPOSER DIRECTEMENT L'ENTITÉ "Employee" ?
 * ------------------------------------------------------------
 * a) SÉCURITÉ (Anti-fuite) :
 *    L'entité 'Employee' possède le champ 'password'. Si on renvoie l'entité brute
 *    en JSON lors d'un GET /employees, le mot de passe (même haché) part sur le réseau.
 *    Le mapper permet d'exclure physiquement ce champ de la réponse.
 * 
 * b) DÉCOUPLAGE :
 *    Si demain le schéma de la base de données change (ex: renommage de colonne SQL),
 *    l'API publique consommée par le frontend ou le mobile ne casse pas.
 * 
 * c) RESPONSABILITÉ UNIQUE :
 *    Ni le Contrôleur ni le Service ne doivent s'encombrer de la copie manuelle
 *    champ par champ. Tout est centralisé ici.
 * 
 * EN RÉSUMÉ POUR L'APPRENANT :
 * « Le Mapper transforme ce que le client envoie en un objet que la base comprend
 *   (toEntity), et transforme ce que la base renvoie en un objet sécurisé que
 *   le client a le droit de voir (toResponseDTO). »
 * ============================================================================
 */
public final class EmployeeMapper {

    /**
     * Constructeur privé :
     * Empêche toute instanciation accidentelle ("new EmployeeMapper()").
     * Cette classe ne contient que des méthodes statiques utilitaires.
     */
    private EmployeeMapper() {
    }

    /**
     * SENS SORTANT : Entité (BDD) -> DTO de Réponse (Client)
     * 
     * Utilisé lors d'un GET ou après création (POST) pour renvoyer les données.
     * On prend l'entité issue de la base et on filtre :
     * -> SEULS les champs autorisés (id, prénom, nom, email) sont recopiés.
     * -> Le mot de passe reste STRICTEMENT en interne, il ne sort jamais.
     * 
     * @param employee L'entité complète provenant de la base de données
     * @return L'objet réponse sécurisé sans mot de passe
     */
    public static EmployeeResponseDTO toResponseDTO(Employee employee) {
        if (employee == null) {
            return null;
        }

        return new EmployeeResponseDTO(
                employee.getId(),
                employee.getFirstName(),
                employee.getLastName(),
                employee.getMail()
        );
    }

    /**
     * SENS ENTRANT : DTO de Requête (Client) -> Entité (BDD)
     * 
     * Utilisé lors d'un POST (création) :
     * Le client envoie un JSON avec prénom, nom, email et mot de passe.
     * Le mapper convertit ce DTO en une nouvelle instance d'Entité JPA 'Employee',
     * prête à être transmise au Service puis au Repository.
     * 
     * @param dto Les données saisies par l'utilisateur
     * @return L'entité Employee prête pour la persistance
     */
    public static Employee toEntity(EmployeeRequestDTO dto) {
        if (dto == null) {
            return null;
        }

        Employee employee = new Employee();
        employee.setFirstName(dto.getFirstName());
        employee.setLastName(dto.getLastName());
        employee.setMail(dto.getMail());
        employee.setPassword(dto.getPassword());
        return employee;
    }
}
