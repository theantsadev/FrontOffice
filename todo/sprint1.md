* Sprint 1

TL : mike , 3327
Dev1 : Vicky , 3254 -> FO
Dev2 :  Jordi , 3351 -> BO

### Hotel
 - id (PK)
 - nom

### Reservation 
 - id (PK)
 - id_client (4 chiffres) varchar (saisissena fotsiny , pas de table)
 - nb_passager int 
 - date_heure_arrivee datetime
 - id_hotel (FK)



FrontOffice (meme base) (spring mvc , miantso api)
 - liste reservation + recherche par date (pas datetime) 
   affichage 
   - creer page misy tableau de liste reservation 
   - pour chaque reservation , on affiche les infos

   integration
   - appel api JSON sprint 9 de getAllReservation() 
   - appel api JSON de getReservationByDate(Date date) 

