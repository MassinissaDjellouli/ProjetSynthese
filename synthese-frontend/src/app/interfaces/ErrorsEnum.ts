export enum Errors{
    INVALID_CREDENTIALS = "Identifiants invalides",
    WRONG_PASSWORD = "Mot de passe invalide",
    NO_ESTABLISHMENTS = "Aucun établissement trouvé",
    INVALID_ESTABLISHMENT = "Établissement invalide",
    UNKNOWN_ERROR = "Erreur inconnue",
    ALREADY_EXISTING_STUDENT = "Cet élève existe déjà sur cet établissement",
    ALREADY_EXISTING_MANAGER = "Ce gestionnaire existe déjà sur cet établissement",
    ALREADY_EXISTING_TEACHER = "Ce professeur existe déjà sur cet établissement",
    ALREADY_EXISTING_PROGRAM = "Un de ces programmes existe déjà sur cet établissement",
    ALREADY_EXISTING_COURSE = "Un de ces cours existe déjà pour ce programme",
}