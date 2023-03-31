package fr.hedwin.objects.modele;

public class NotAssociateException extends Exception {
    public NotAssociateException(Apport apport){
        super("Aucune informations nutritionnel n'est associé à "+apport.name());
    }
}
