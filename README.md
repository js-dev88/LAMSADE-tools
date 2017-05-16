[![Build Status](https://travis-ci.org/LAntoine/LAMSADE-tools.svg?branch=master)](https://travis-ci.org/LAntoine/LAMSADE-tools)

## LAMSADE-tools


Le LAMSADE est une unité de recherche de Paris-Dauphine. Ce projet a pour but de fournir des outils de gestion administrative utiles à cette unité (ou plus généralement aux unités de recherche).

== Récits utilisateur
 - [x] Génération de papier à en-tête avec les coordonnées d’un chercheur passées en paramètres (voir link:LAMSADE[canevas]), format Asciidoc ou HTML ou OpenDocument Writer. **SetCoordinates**

 - [ ] Génération de papier à en-tête d’après coordonnées indiquées dans l’interface graphique

 - [x] Récupération des coordonnées d’un chercheur depuis l’annuaire. **yearbookInfos**


 - [ ] Génération du formulaire annuel de demande de PRES, d’après données passées en paramètres / fournies par interface graphique / récupérée depuis fichier / récupérée depuis internet

 - [x] Création conférence. URL, Titre, Date début et fin, Frais d’inscription. **Conferences**

 - [ ] Génèration d'un fichier / entrée format VCal à partir des dates de conférences.

 - [ ] Lecture du fichier et édition conférences.

 - [x] Recherche d’une conférence existante : par nom, par URL… **Conferences**

 - [ ] Utilisation format standard calendrier pour édition des conférences.

 - [ ] Moyen aller : a → b (bus) → c (avion) etc. Pour chaque transport, indiquer : fourchette prix, à rembourser ?, à acheter ?, original stocké ? \+ possibilité stocker fichier (ou donner URL ?). Moyen retour, même chose.

 - [x] Montrer périple sur une carte (lieu départ, lieu d’arrivée si rien d’autre rempli). **Map**

 - [ ] Sur place : l’utilisateur peut indiquer moyens de transports et fourchette prix.

 - [ ] Génération d’une demande d’ordre de mission (OM) (voir link:LAMSADE/ordre_de_mission.ods[canevas] OM).

 - [ ] Génération d’une demande de mission jeune chercheur (JC) (voir link:LAMSADE/demande_de_mission_jeune_chercheur.odt[canevas] JC).

 - [ ] L’utilisateur peut entrer une demande scannée (après l’avoir signé à la main), le logiciel la conserve.

 - [ ] Génération et stockage de la demande d’ordre de mission (OM) ou de la demande JC. Si déjà une, elle passe dans l’historique.

 - [ ] L’utilisateur peut entrer son mot de passe de son compte e-mail @dauphine. Le logiciel le stocke en suivant le mécanisme de conservation des données secrètes de l’OS.

 - [ ] Réception des demandes scannées envoyées sur l’e-mail du chercheur (reconnaissable par l’adresse expéditeur : \photocopieur@lamsade.dauphine.fr), à la demande de l’utilisateur.

 - [ ] Envoi demande par e-mail.

== Refs
* link:open_data.adoc[Dauphine Open Data] : récupération de données
* Cf. application link:Test-ODS[] (utilisant Apache ODFToolkit) pour modification de feuilles de calcul.

